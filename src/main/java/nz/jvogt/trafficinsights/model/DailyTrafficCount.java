package nz.jvogt.trafficinsights.model;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;
import java.util.Objects;

public class DailyTrafficCount {

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByPosition(position = 0)
    private LocalDate date;

    @CsvBindByPosition(position = 1)
    private Integer count;

    public DailyTrafficCount(LocalDate date, Integer count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyTrafficCount that = (DailyTrafficCount) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, count);
    }
}
