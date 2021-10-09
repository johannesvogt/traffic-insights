package nz.jvogt.trafficinsights.model;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.LocalDateTime;
import java.util.Objects;

public class TrafficCount {

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss")
    @CsvBindByPosition(position = 0)
    private LocalDateTime dateTime;

    @CsvBindByPosition(position = 1)
    private Integer count;

    public TrafficCount(LocalDateTime dateTime, Integer count) {
        this.dateTime = dateTime;
        this.count = count;
    }

    public TrafficCount() {}

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficCount that = (TrafficCount) o;
        return Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, count);
    }

    @Override
    public String toString() {
        return "TrafficCount{" +
                "timestamp=" + dateTime +
                ", count=" + count +
                '}';
    }
}
