package nz.jvogt.trafficinsights;

import nz.jvogt.trafficinsights.model.TrafficCount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import static nz.jvogt.trafficinsights.TestUtils.trafficCounts;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ParserTest {

    @Test
    @DisplayName("Parsing a correct input-file should return correct list of objects")
    void testParseFile() throws FileNotFoundException {
        // given
        File file = new File("src/test/resources/traffic-records-correct.csv");

        // when
        List<TrafficCount> parsedList = Parser.parseTrafficCounts(file);

        // then
        List<TrafficCount> expected = trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", "12",
                "2016-12-01T06:00:00", "14"
                );
        assertIterableEquals(expected, parsedList);
    }

    @Test
    @DisplayName("Parsing an empty file should return an empty list")
    void testParseFileEmpty() throws FileNotFoundException {
        // given
        File file = new File("src/test/resources/traffic-records-empty.csv");

        // when
        List<TrafficCount> parsedList = Parser.parseTrafficCounts(file);

        // then
        assertIterableEquals(Collections.EMPTY_LIST, parsedList);
    }

    @Test
    @DisplayName("Parsing a malformatted date should throw a RuntimeException")
    void testParseFileWrongDateFormat() {
        // given
        File file = new File("src/test/resources/traffic-records-wrong-date-format.csv");

        // when, then
        Assertions.assertThrows(RuntimeException.class, () ->
                Parser.parseTrafficCounts(file)
        );
    }

    @Test
    @DisplayName("Parsing a record with missing count value should result in a null-value")
    void testParseFileMissingCountValue() throws FileNotFoundException {
        // given
        File file = new File("src/test/resources/traffic-records-missing-count-value.csv");

        // when
        List<TrafficCount> parsedList = Parser.parseTrafficCounts(file);

        // then
        // then
        List<TrafficCount> expected = trafficCounts(
                "2016-12-01T05:00:00", "5",
                "2016-12-01T05:30:00", null,
                "2016-12-01T06:00:00", "14"
        );
        assertIterableEquals(expected, parsedList);
    }

    @Test
    @DisplayName("Parsing a malformatted date should throw a FileNotFoundException")
    void testParseFileMissingFile() {
        // given
        File file = new File("src/test/resources/not-existing.csv");

        // when, then
        Assertions.assertThrows(FileNotFoundException.class, () ->
                Parser.parseTrafficCounts(file)
        );
    }

}
