# Inspect Traffic Counts

## Overview
Simple CLI tool to inspect files containing traffic data.

Expected file-format:
```
2016-12-01T05:00:00 5
2016-12-01T05:30:00 12
2016-12-01T06:00:00 14
```

## Usage
Pre-conditions:
* Java 8 (or higher)
* Maven

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

