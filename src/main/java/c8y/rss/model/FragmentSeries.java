package c8y.rss.model;

import java.math.BigDecimal;

public class FragmentSeries {
    private String series;
    private String unit;
    private BigDecimal value;

    public FragmentSeries() {
    }

    public FragmentSeries(String series, String unit, BigDecimal value) {
        this.series = series;
        this.unit = unit;
        this.value = value;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FragmentSeries{" +
                "series='" + series + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}
