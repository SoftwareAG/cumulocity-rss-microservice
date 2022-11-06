package c8y.rss.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedMessageMeasurement {
    private String datetime;
    private String id;
    private String sourceId;
    private String type;
    private String fragment;
    private List<FragmentSeries> seriesList;

    public FeedMessageMeasurement() {
    }

    public FeedMessageMeasurement(String datetime, String id, String sourceId, String type, String fragment, List<FragmentSeries> seriesList) {
        this.datetime = datetime;
        this.id = id;
        this.sourceId = sourceId;
        this.type = type;
        this.fragment = fragment;
        this.seriesList = seriesList;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public List<FragmentSeries> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<FragmentSeries> seriesList) {
        this.seriesList = seriesList;
    }

    @Override
    public String toString() {
        return "FeedMessage{" +
                "datetime='" + datetime + '\'' +
                ", id='" + id + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", type='" + type + '\'' +
                ", fragment='" + fragment + '\'' +
                ", seriesList=" + seriesList +
                '}';
    }
}
