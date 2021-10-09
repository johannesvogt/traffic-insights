package nz.jvogt.trafficinsights;

import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.List;

/**
 * Rendering models to a string equivalent to input-file format (space delimited csv format).
 */
public class Writer {

    protected static final Logger logger = LoggerFactory.getLogger(Writer.class);

    static <T> String renderModel(List<T> modelList) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        logger.debug("Rendering csv string for '{}'...", modelList);

        StringWriter sw = new StringWriter();

        StatefulBeanToCsv<T> sbc = new StatefulBeanToCsvBuilder<T>(sw)
                .withSeparator(' ')
                .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
                .build();

        sbc.write(modelList);
        return sw.toString();
    }

}
