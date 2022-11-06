package c8y.rss.model;

import org.springframework.stereotype.Component;

@Component
public class FeedMessageEvent {
    private String id;
    private String sourceId;
    private String sourceName;
    private String text;
    private String type;
    private String time;
    private String creationTime;
    private String lastUpdated;

    public FeedMessageEvent() {
    }

    public FeedMessageEvent(String id, String sourceId, String sourceName, String text, String type, String time, String creationTime, String lastUpdated) {
        this.id = id;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.text = text;
        this.type = type;
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
