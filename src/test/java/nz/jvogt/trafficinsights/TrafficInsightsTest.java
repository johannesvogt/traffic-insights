package nz.jvogt.trafficinsights;

import nz.jvogt.trafficinsights.model.DailyTrafficCount;
import nz.jvogt.trafficinsights.model.TrafficCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static java.util.Collections.emptyList;
import static nz.jvogt.trafficinsights.TestUtils.dailyTrafficCounts;
import static nz.jvogt.trafficinsights.TestUtils.trafficCounts;
import static org.junit.jupiter.api.Assertions.*;

public class TrafficInsightsTest {

    @Test
    @DisplayName("Calculating the total count of a dataset should return the correct result")
    void testTotalCount() {
        // given
        TrafficInsights insights = new TrafficInsights(trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", "12",
                "2016-12-01T06:00:00", "14"
        ));

        // when
        int result = insights.totalCount();

        // then
        assertEquals(31, result);
    }

    @Test
    @DisplayName("Calculating the total count of an empty dataset should return 0")
    void testTotalCountEmptyList() {
        // given
        TrafficInsights insights = new TrafficInsights(emptyList());

        // when
        int result = insights.totalCount();

        // then
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Instantiating the class with null should throw a NullPointerException")
    void testNullInstantiation() {

        assertThrows(NullPointerException.class, () ->
                new TrafficInsights(null)
        );
    }

    @Test
    @DisplayName("Null count-values should be interpreted as '0' ")
    void testTotalCountWithNullValue() {
        // given
        TrafficInsights insights = new TrafficInsights(trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", null,
                "2016-12-01T06:00:00", "14"
        ));

        // when
        int result = insights.totalCount();

        // then
        assertEquals(19, result);
    }

    @Test
    @DisplayName("Daily aggregations for a dataset should return the correct value")
    void testDailyCount() {
        // given
        TrafficInsights insights = new TrafficInsights(trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", "8",
                "2016-12-02T06:00:00", "14",
                "2016-12-02T07:00:00", "1"
        ));

        // when
        List<DailyTrafficCount> result = insights.dailyCount();

        // then
        List<DailyTrafficCount> expected = dailyTrafficCounts(
                "2016-12-01", "13",
                "2016-12-02", "15"
        );
        assertIterableEquals(expected, result);
    }

    @Test
    @DisplayName("Daily aggregations of an empty input-list should return an empty list")
    void testDailyCountEmptyList() {
        // given
        TrafficInsights insights = new TrafficInsights(emptyList());

        // when
        List<DailyTrafficCount> result = insights.dailyCount();

        // then
        assertIterableEquals(emptyList(), result);
    }

    @Test
    @DisplayName("Calculating the 3 largest count records should return the correct value")
    void testMaximumRecords() {
        // given
        TrafficInsights insights = new TrafficInsights(trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", "8",
                "2016-12-02T06:00:00", "14",
                "2016-12-02T07:00:00", "1",
                "2016-12-05T07:00:00", "100"
        ));

        // when
        List<TrafficCount> result = insights.maxRecords(3);

        // then
        List<TrafficCount> expected = trafficCounts(
                "2016-12-05T07:00:00", "100",
                "2016-12-02T06:00:00", "14",
                "2016-12-01T05:30:00", "8"
        );
        assertIterableEquals(expected, result);
    }

    @Test
    @DisplayName("Calculating the 3 largest count records of an empty list should return an empty list")
    void testMaximumRecordsEmptyList() {
        // given
        TrafficInsights insights = new TrafficInsights(emptyList());

        // when
        List<TrafficCount> result = insights.maxRecords(3);

        // then
        assertIterableEquals(emptyList(), result);
    }

    @Test
    @DisplayName("Calculating the sequence of 3 with the minimum aggregated count should return the correct value")
    void testMinimumTrafficSequence() {
        // given
        TrafficInsights insights = new TrafficInsights(trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", "8",
                "2016-12-02T06:00:00", "14",
                "2016-12-02T07:00:00", "1",
                "2016-12-05T07:00:00", "2",
                "2016-12-05T07:30:00", "2",
                "2016-12-05T08:00:00", "3",
                "2016-12-05T08:00:00", "3"
        ));

        // when
        List<TrafficCount> result = insights.minTrafficSequence(3);

        // then
        List<TrafficCount> expected = trafficCounts(
                "2016-12-05T07:00:00", "2",
                "2016-12-05T07:30:00", "2",
                "2016-12-05T08:00:00", "3"
        );
        assertIterableEquals(expected, result);
    }

    @Test
    @DisplayName("Minimum sequence of 3 with non-matching interval should return empty list")
    void testMinimumTrafficSequenceNotMatching() {
        // given
        TrafficInsights insights = new TrafficInsights(trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", "8",
                "2016-12-02T06:00:00", "14",
                "2016-12-02T07:00:00", "1",
                "2016-12-05T07:00:00", "2",
                "2016-12-05T07:30:00", "2",
                "2016-12-05T08:00:00", "3",
                "2016-12-05T08:00:00", "3"
        ), Duration.ofMinutes(35));

        // when
        List<TrafficCount> result = insights.minTrafficSequence(3);

        // then
        List<TrafficCount> expected = emptyList();
        assertIterableEquals(expected, result);
    }

    @Test
    @DisplayName("Minimum sequence of length 1 always returns the minimum record")
    void testMinimumTrafficSequenceOfOne() {
        // given
        TrafficInsights insights = new TrafficInsights(trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", "8",
                "2016-12-02T06:00:00", "14",
                "2016-12-02T07:00:00", "1",
                "2016-12-05T07:00:00", "2",
                "2016-12-05T07:30:00", "2",
                "2016-12-05T08:00:00", "3",
                "2016-12-05T08:00:00", "3"
        ), Duration.ofMinutes(35));

        // when
        List<TrafficCount> result = insights.minTrafficSequence(1);

        // then
        List<TrafficCount> expected = trafficCounts(
                "2016-12-02T07:00:00", "1"
        );
        assertIterableEquals(expected, result);
    }

    @Test
    @DisplayName("Minimum sequence of 3 with empty input list returns an empty list")
    void testMinimumTrafficSequenceEmtpyList() {
        // given
        TrafficInsights insights = new TrafficInsights(emptyList());

        // when
        List<TrafficCount> result = insights.minTrafficSequence(3);

        // then
        assertIterableEquals(emptyList(), result);
    }

}
