package nz.jvogt.trafficinsights;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import java.io.File;
import java.time.Duration;

import static nz.jvogt.trafficinsights.Parser.parseTrafficCounts;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

/**
 * Main class to inspect files of Traffic-Counts
 */
@CommandLine.Command(name = "InspectTrafficCounts")
public class InspectTrafficCounts implements Runnable {

    @CommandLine.Parameters(paramLabel = "File", description = "File to process.")
    private File inputFile;

    @CommandLine.Option(names = "-i", paramLabel = "IntervalMinutes", description = "Standard interval between traffic-count records in input-file in minutes.", defaultValue = "30", showDefaultValue = ALWAYS)
    private int intervalMinutes;

    @CommandLine.Spec
    CommandSpec spec;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void run() {
        validate();

        logger.info("Inspecting file '{}', using {} minutes interval ...", inputFile.toString(), intervalMinutes);

        try {
            TrafficInsights insights = new TrafficInsights(parseTrafficCounts(inputFile), Duration.ofMinutes(intervalMinutes));

            System.out.println("\n---- Total count:");
            System.out.println(insights.totalCount() + "\n");

            System.out.println("---- Daily count:");
            System.out.println(Writer.renderModel(insights.dailyCount()));

            System.out.println("---- Top 3 records:");
            System.out.println(Writer.renderModel(insights.maxRecords(3)));

            System.out.println("---- Sequence of 3 with minimal count:");
            System.out.println(Writer.renderModel(insights.minTrafficSequence(3)));

        } catch (Exception e) {
            logger.error("Error when processing file '{}'...", inputFile.toString(), e);
            System.exit(1);
        }
    }


    private void validate() {
        if (!inputFile.exists()) {
            throw new ParameterException(spec.commandLine(),
                    "File '" + inputFile + "' does not exist.");
        }
        if (inputFile.isDirectory()) {
            throw new ParameterException(spec.commandLine(),
                    "'" + inputFile + "' is a directory.");
        }
    }

    public static void main(String[] args) {
        // use picocli CommandLine to handle arguments
        int exitCode = new CommandLine(new InspectTrafficCounts()).execute(args);
        System.exit(exitCode);
    }
}

