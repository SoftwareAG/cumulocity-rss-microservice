package c8y.rss.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Feed {
    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;
    private String pubDate;

    final List<FeedMessageAlarm> alarmEntries = new ArrayList<FeedMessageAlarm>();
    final List<FeedMessageEvent> eventEntries = new ArrayList<FeedMessageEvent>();
    final List<FeedMessageMeasurement> measurementEntries = new ArrayList<FeedMessageMeasurement>();

    public Feed() {
    }

    public Feed(String title, String link, String description, String language, String copyright, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    public List<FeedMessageAlarm> getMessagesForAlarm() {
        return alarmEntries;
    }

    public List<FeedMessageEvent> getMessagesForEvent() {
        return eventEntries;
    }

    public List<FeedMessageMeasurement> getMessagesForMeasurement() {
        return measurementEntries;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
