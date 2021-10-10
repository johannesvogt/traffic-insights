package nz.jvogt.trafficinsights;

import nz.jvogt.trafficinsights.model.DailyTrafficCount;
import nz.jvogt.trafficinsights.model.TrafficCount;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Class implementing insights operations on list of traffic-count records (datetime,count - pairs).
 *
 * Some operations require the standard interval between records, as the record-sequence may be incomplete. Default interval is 30 minutes.
 *
 * Notes:
 * The insights-calculations are done ad-hoc, based on a ordered list of records. In case this class is used in a different context (e.g. as part of an API), 
 * additional data-structures (ordered by count, aggregated by day) can be created on instantiation, to improve performance.
 *
 * In order to process larger amounts of data, the records should be instead persisted in a queryable data-store (SQL or NoSQL), with indexes supporting the 
 * queries required.
 */
class TrafficInsights {

    private final List<TrafficCount> trafficCountList;

    private final Duration duration;

    public TrafficInsights(List<TrafficCount> trafficCountList) {
        this(trafficCountList, Duration.ofMinutes(30));
    }

    public TrafficInsights(List<TrafficCount> trafficCountList, Duration duration) {
        this.duration = duration;

        // copy list and sort by date
        this.trafficCountList = new ArrayList<>(trafficCountList);
        this.trafficCountList.sort(comparing(TrafficCount::getDateTime));
    }

    /**
     *
     * @return Total traffic-count.
     */
    public int totalCount() {
        return sumAll(trafficCountList);
    }

    /**
     *
     * @return Aggregated traffic-count by day.
     */
    public List<DailyTrafficCount> dailyCount() {
        return trafficCountList.stream()
                .collect(groupingBy(record -> record.getDateTime().toLocalDate())).entrySet().stream()
                .map(entry -> new DailyTrafficCount(entry.getKey(), sumAll(entry.getValue())))
                .sorted(comparing(DailyTrafficCount::getDate))
                .collect(toList());
    }

    /**
     *
     * @return Maximum n records.
     */
    public List<TrafficCount> maxRecords(int n) {
        List<TrafficCount> copy = new ArrayList<>(trafficCountList);
        copy.sort(comparingInt(TrafficCount::getCount).reversed());
        return copy.stream().limit(n).collect(toList());
    }

    /**
     *
     * @return Sequence of n records (without gaps) with minimum aggregated count.
     */
    public List<TrafficCount> minTrafficSequence(int n) {
        return sliding(trafficCountList, n)
                // filter out windows that are not adhering to the defined duration (e.g. 30 minutes)
                .filter(l -> Duration.between(l.get(0).getDateTime(), l.get(l.size() - 1).getDateTime()).equals(this.duration.multipliedBy(n - 1)))
                .min(comparing(TrafficInsights::sumAll))
                .orElse(emptyList());
    }

    private static int sumAll(List<TrafficCount> trafficCounts) {
        return trafficCounts.stream()
                .map(TrafficCount::getCount)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static <T> Stream<List<T>> sliding(List<T> list, int size) {
        if(size > list.size()) {
            return Stream.empty();
        }
        return IntStream.range(0, list.size()-size+1)
                .mapToObj(start -> list.subList(start, start+size));
    }

}
