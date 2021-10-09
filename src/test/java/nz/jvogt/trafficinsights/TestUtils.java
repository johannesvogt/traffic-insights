package nz.jvogt.trafficinsights;

import nz.jvogt.trafficinsights.model.DailyTrafficCount;
import nz.jvogt.trafficinsights.model.TrafficCount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

class TestUtils {

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    static List<TrafficCount> trafficCounts(String... params) {
        return IntStream.range(0, params.length).filter(n -> n % 2 == 1)
                .mapToObj(n ->
                        new TrafficCount(LocalDateTime.parse(params[n - 1], dateTimeFormatter), ofNullable(params[n]).map(Integer::parseInt).orElse(null))
                )
                .collect(toList());
    }

    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static List<DailyTrafficCount> dailyTrafficCounts(String... params) {
        return IntStream.range(0, params.length).filter(n -> n % 2 == 1)
                .mapToObj(n ->
                        new DailyTrafficCount(LocalDate.parse(params[n - 1], dateFormatter), ofNullable(params[n]).map(Integer::parseInt).orElse(null))
                )
                .collect(toList());
    }

}
