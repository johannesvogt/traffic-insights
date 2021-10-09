# Inspect Traffic Counts

## Overview
Simple CLI tool to inspect files containing traffic data.

## Usage
Main-class: 
```
nz.jvogt.trafficinsights.InspectTrafficCounts
```

Parameters:
```
InspectTrafficCounts [-i=IntervalMinutes] File
      File              File to process.
  -i=IntervalMinutes    Standard interval between traffic-count records in
                          input-file in minutes.
                          Default: 30
```

Example:
```
mvn compile

mvn exec:java -Dexec.mainClass="nz.jvogt.trafficinsights.InspectTrafficCounts" -Dexec.args="<path-to-input-file>"
```

Run unit-tests:
```
mvn test
```

## Notes

* Currently processing of the data is done in-memory, by processing an list of records ordered by record-timestamp. A few optimizations can be considered:
    * Indexing the data-structures in-memory, e.g. additional data-structures ordered by record-value, or store daily aggregations in a Map.
    * In case of very large datasets, the data-structure should be persisted in a queryable store, with indexes on the relevant columns and potentially tables for aggregated data.
