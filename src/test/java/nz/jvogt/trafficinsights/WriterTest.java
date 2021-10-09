package nz.jvogt.trafficinsights;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import nz.jvogt.trafficinsights.model.DailyTrafficCount;
import nz.jvogt.trafficinsights.model.TrafficCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static nz.jvogt.trafficinsights.TestUtils.dailyTrafficCounts;
import static nz.jvogt.trafficinsights.TestUtils.trafficCounts;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WriterTest {

    @Test
    @DisplayName("Rendering a list of traffic-counts should result in the correct output string")
    void testRenderModelTrafficCounts() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // given
        List<TrafficCount> trafficCounts = trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", "12",
                "2016-12-01T06:00:00", "14"
        );
        // when
        String result = Writer.renderModel(trafficCounts);

        // then
        assertEquals(
                "2016-12-01T05:00:00 5\n" +
                "2016-12-01T05:30:00 12\n" +
                "2016-12-01T06:00:00 14\n", result);
    }

    @Test
    @DisplayName("Rendering an empty list should result in an empty string")
    void testRenderModelEmpty() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // given
        List<TrafficCount> trafficCounts = Collections.emptyList();

        // when
        String result = Writer.renderModel(trafficCounts);

        // then
        assertEquals("", result);
    }

    @Test
    @DisplayName("Calling renderModel with null-input should result in empty string")
    void testRenderModelNull() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // given
        List<TrafficCount> trafficCounts = null;

        // when
        String result = Writer.renderModel(trafficCounts);

        // then
        assertEquals("", result);

    }

    @Test
    @DisplayName("Rendering a list of daily traffic-counts should result in the correct output string")
    void testRenderModelDailyTrafficCounts() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // given
        List<DailyTrafficCount> dailyTrafficCounts = dailyTrafficCounts(
                "2016-12-01", "5",
                "2016-12-02", "12",
                "2016-12-03", "14"
        );

        // when
        String result = Writer.renderModel(dailyTrafficCounts);

        // then
        assertEquals(
                "2016-12-01 5\n" +
                        "2016-12-02 12\n" +
                        "2016-12-03 14\n", result);
    }

}
