package c8y.rss.write;

import c8y.rss.model.Feed;
import c8y.rss.model.FeedMessageAlarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;

@Component
public class RSSFeedWriterForAlarm {
    private static final Logger logger = LoggerFactory.getLogger(RSSFeedWriterForAlarm.class);

    public void write(Feed rssFeed, String outputFile) throws Exception {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(outputFile));
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");

        // create and write a start tag
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);

        // create open tag
        eventWriter.add(end);

        StartElement rssStart = eventFactory.createStartElement("", "", "rss");
        eventWriter.add(rssStart);
        eventWriter.add(eventFactory.createAttribute("version", "2.0"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createStartElement("", "", "channel"));
        eventWriter.add(end);

        // Write the different nodes

        createNode(eventWriter, "title", rssFeed.getTitle());
        createNode(eventWriter, "link", rssFeed.getLink());
        createNode(eventWriter, "description", rssFeed.getDescription());
        createNode(eventWriter, "language", rssFeed.getLanguage());
        createNode(eventWriter, "copyright", rssFeed.getCopyright());
        createNode(eventWriter, "pubdate", rssFeed.getPubDate());

        for (FeedMessageAlarm entry : rssFeed.getMessagesForAlarm()) {
            eventWriter.add(eventFactory.createStartElement("", "", "alarm"));
            eventWriter.add(end);

            createNode(eventWriter, "id", entry.getId());
            createNode(eventWriter, "sourceId", entry.getSourceId());
            createNode(eventWriter, "sourceName", entry.getSourceName());
            createNode(eventWriter, "severity", entry.getSeverity());
            createNode(eventWriter, "text", entry.getText());
            createNode(eventWriter, "status", entry.getStatus());
            createNode(eventWriter, "time", entry.getTime());
            createNode(eventWriter, "creationTime", entry.getCreationTime());
            //createNode(eventWriter, "lastUpdated", ""); //TODO: Check if lastUpdated is needed or not. If yes, then finish this part.

            //eventWriter.add(end);

            eventWriter.add(eventFactory.createEndElement("", "", "alarm"));
            eventWriter.add(end);

        }

        //eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "channel"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "rss"));

        eventWriter.add(end);

        eventWriter.add(eventFactory.createEndDocument());

        eventWriter.close();


    }


    private void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

}
