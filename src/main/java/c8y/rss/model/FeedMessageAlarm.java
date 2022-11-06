package c8y.rss.model;

import org.springframework.stereotype.Component;

@Component
public class FeedMessageAlarm {
    private String id;
    private String sourceId;
    private String sourceName;
    private String severity;
    private String text;
    private String type;
    private String status;
    private String time;
    private String creationTime;
    private String lastUpdated;

    public FeedMessageAlarm() {
    }

    public FeedMessageAlarm(String id, String sourceId, String sourceName, String severity, String text, String type, String status, String time, String creationTime, String lastUpdated) {
        this.id = id;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.severity = severity;
        this.text = text;
        this.type = type;
        this.status = status;
        this.time = time;
        this.creationTime = creationTime;
        this.lastUpdated = lastUpdated;
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
