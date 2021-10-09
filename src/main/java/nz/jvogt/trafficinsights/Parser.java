package nz.jvogt.trafficinsights;

import com.opencsv.bean.CsvToBeanBuilder;
import nz.jvogt.trafficinsights.model.TrafficCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Parser using opencsv framework (space-delimited).
 */
public class Parser {

    protected static final Logger logger = LoggerFactory.getLogger(Parser.class);

    static List<TrafficCount> parseTrafficCounts(File file) throws FileNotFoundException {

        logger.debug("Parsing file '{}'...", file.toString());

        return new CsvToBeanBuilder<TrafficCount>(new FileReader(file))
                .withType(TrafficCount.class)
                .withSeparator(' ')
                .build()
                .parse();
    }

}
