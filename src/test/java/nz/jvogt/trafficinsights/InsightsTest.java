package nz.jvogt.trafficinsights;

import nz.jvogt.trafficinsights.model.DailyTrafficCount;
import nz.jvogt.trafficinsights.model.TrafficCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nz.jvogt.trafficinsights.TestUtils.dailyTrafficCounts;
import static nz.jvogt.trafficinsights.TestUtils.trafficCounts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class InsightsTest {

    @Test
    @DisplayName("Calculating the total count of a dataset should return the correct result")
    void testTotalCount() {
        // given
        Insights insights = new Insights(trafficCounts(
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
    @DisplayName("Null values should be interpreted as '0' ")
    void testTotalCountWithNullValue() {
        // given
        Insights insights = new Insights(trafficCounts(
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
        Insights insights = new Insights(trafficCounts(
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
    @DisplayName("Calculating the 3 largest count records should return the correct value")
    void testMaximumRecords() {
        // given
        Insights insights = new Insights(trafficCounts(
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
    @DisplayName("Calculating the sequence of 3 with the minimum aggregated count should return the correct value")
    void testMinimumTrafficSequence() {
        // given
        Insights insights = new Insights(trafficCounts(
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

}
