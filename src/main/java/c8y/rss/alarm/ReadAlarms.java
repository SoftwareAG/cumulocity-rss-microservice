package c8y.rss.alarm;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.event.CumulocitySeverities;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.sdk.client.Param;
import com.cumulocity.sdk.client.QueryParam;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.alarm.AlarmCollection;
import com.cumulocity.sdk.client.alarm.AlarmFilter;

@Component
public class ReadAlarms {
    private static final Logger logger = LoggerFactory.getLogger(ReadAlarms.class);

    @Autowired
    private AlarmApi alarmApi;

    QueryParam revertParam = new QueryParam(new Param() {
        @Override
        public String getName() {
            return "revert";
        }
    }, "true");

    public String readLatestAlarms(String sourceId, String severity, String status, String type, String feedSize) {
        AlarmFilter alarmFilter = new AlarmFilter();
        if(sourceId != null) {
            alarmFilter.bySource(GId.asGId(sourceId));
        }
        if(severity != null) {
            alarmFilter.bySeverity(CumulocitySeverities.valueOf(severity));
        }
        if(status != null) {
            alarmFilter.byStatus(CumulocityAlarmStatuses.valueOf(status));
        }
        if(type != null) {
            alarmFilter.byType(type);
        }
        if(feedSize == null) {
            feedSize = "5";
        }
        return buildAlarmFeed(alarmFilter, Integer.valueOf(feedSize));
    }

    public String buildAlarmFeed(AlarmFilter alarmFilter, int feedSize) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("rss");
            rootElement.setAttribute("version", "2.0");
            doc.appendChild(rootElement);
            Element channelElement = doc.createElement("channel");
            rootElement.appendChild(channelElement);
            Element titleElement = doc.createElement("title");
            titleElement.appendChild(doc.createTextNode("Cumulocity latest alarm detail"));
            channelElement.appendChild(titleElement);
            Element linkElement = doc.createElement("link");
            linkElement.appendChild(doc.createTextNode("https://cumulocity.com/"));
            channelElement.appendChild(linkElement);
            Element descriptionElement = doc.createElement("description");
            descriptionElement.appendChild(doc.createTextNode("RSS feed to provide details of the latest Cumulocity alarms"));
            channelElement.appendChild(descriptionElement);
            Element languageElement = doc.createElement("language");
            languageElement.appendChild(doc.createTextNode("en"));
            channelElement.appendChild(languageElement);
            Element copyrightElement = doc.createElement("copyright");
            copyrightElement.appendChild(doc.createTextNode("Copyright hold by GCC, Software AG Germany"));
            channelElement.appendChild(copyrightElement);
            Calendar cal = new GregorianCalendar();
            Date creationDate = cal.getTime();
            SimpleDateFormat date_format = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
            String pubdate = date_format.format(creationDate);
            Element pubdateElement = doc.createElement("pubdate");
            pubdateElement.appendChild(doc.createTextNode(pubdate));
            channelElement.appendChild(pubdateElement);

            AlarmCollection alarmCollection = alarmApi.getAlarmsByFilter(alarmFilter);
            Iterable<AlarmRepresentation> arIterable = alarmCollection.get(feedSize, revertParam).getAlarms();
            arIterable.forEach((ar) -> {
                Element alarmElement = doc.createElement("alarm");
                channelElement.appendChild(alarmElement);
                Element idElement = doc.createElement("id");
                idElement.appendChild(doc.createTextNode(ar.getId().getValue()));
                alarmElement.appendChild(idElement);
                Element sourceIdElement = doc.createElement("sourceId");
                sourceIdElement.appendChild(doc.createTextNode(ar.getSource().getId().getValue()));
                alarmElement.appendChild(sourceIdElement);
                Element sourceNameElement = doc.createElement("sourceName");
                sourceNameElement.appendChild(doc.createTextNode(ar.getSource().getName()));
                alarmElement.appendChild(sourceNameElement);
                Element severityElement = doc.createElement("severity");
                severityElement.appendChild(doc.createTextNode(ar.getSeverity()));
                alarmElement.appendChild(severityElement);
                Element textElement = doc.createElement("text");
                textElement.appendChild(doc.createTextNode(ar.getText()));
                alarmElement.appendChild(textElement);
                Element typeElement = doc.createElement("type");
                typeElement.appendChild(doc.createTextNode(ar.getType()));
                alarmElement.appendChild(typeElement);
                Element statusElement = doc.createElement("status");
                statusElement.appendChild(doc.createTextNode(ar.getStatus()));
                alarmElement.appendChild(statusElement);
                Element timeElement = doc.createElement("time");
                timeElement.appendChild(doc.createTextNode(ar.getDateTime().toString()));
                alarmElement.appendChild(timeElement);
                Element creationTimeElement = doc.createElement("creationTime");
                creationTimeElement.appendChild(doc.createTextNode(ar.getCreationDateTime().toString()));
                alarmElement.appendChild(creationTimeElement);

            });

            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);
            logger.debug("\n" + writer.toString());
            return writer.toString();
        } catch (ParserConfigurationException | TransformerException e) {
        	logger.error("Error creating event rss feed: ", e);
        }
        return "";
    }

}
