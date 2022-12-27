package c8y.rss.event;

import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.event.CumulocitySeverities;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.sdk.client.Param;
import com.cumulocity.sdk.client.QueryParam;
import com.cumulocity.sdk.client.alarm.AlarmFilter;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.event.EventCollection;
import com.cumulocity.sdk.client.event.EventFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@Component
public class ReadEvents {
    private static final Logger logger = LoggerFactory.getLogger(ReadEvents.class);

    @Autowired
    private EventApi eventApi;

    @Autowired
    private MicroserviceSubscriptionsService subscriptionsService;

    @EventListener
    private void onMicroserviceSubscriptionAddedEvent(final MicroserviceSubscriptionAddedEvent event) {
        String tenantId = event.getCredentials().getTenant();
        this.subscriptionsService.runForTenant(tenantId, () -> {
            //logger.info("The subscribed tenant id for event: " + tenantId);
        });
    }

    QueryParam revertParam = new QueryParam(new Param() {
        @Override
        public String getName() {
            return "revert";
        }
    }, "true");

    public void readLatestEvents_OLD(String deviceId, String type, int batchSize) {

        EventCollection eventCollection = eventApi.getEventsByFilter(new EventFilter().bySource(GId.asGId(deviceId)).byType(type));
        Iterable<EventRepresentation> erIterable = eventCollection.get(batchSize, revertParam).getEvents();
        erIterable.forEach((er) -> {
            logger.info("The event type: " + er.getType());
            logger.info("The event text: " + er.getText());
        });
    }

    public String readLatestEvents(String sourceId, String type, String batchSize) {
        EventFilter eventFilter = new EventFilter();
        if(sourceId != null) {
            eventFilter.bySource(GId.asGId(sourceId));
        }
        if(type != null) {
            eventFilter.byType(type);
        }
        if(batchSize == null) {
            batchSize = "5";
        }
        return readLatestEvents_OPTION2(eventFilter, Integer.valueOf(batchSize));
    }

    public String readLatestEvents_OPTION2(EventFilter eventFilter, int batchSize) {
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
            titleElement.appendChild(doc.createTextNode("Cumulocity latest event detail"));
            channelElement.appendChild(titleElement);
            Element linkElement = doc.createElement("link");
            linkElement.appendChild(doc.createTextNode("https://cumulocity.com/"));
            channelElement.appendChild(linkElement);
            Element descriptionElement = doc.createElement("description");
            descriptionElement.appendChild(doc.createTextNode("RSS feed to provide details of the latest Cumulocity events"));
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

            EventCollection eventCollection = eventApi.getEventsByFilter(eventFilter);
            Iterable<EventRepresentation> erIterable = eventCollection.get(batchSize, revertParam).getEvents();
            erIterable.forEach((er) -> {
                Element eventElement = doc.createElement("event");
                channelElement.appendChild(eventElement);
                Element idElement = doc.createElement("id");
                idElement.appendChild(doc.createTextNode(er.getId().getValue()));
                eventElement.appendChild(idElement);
                Element sourceIdElement = doc.createElement("sourceId");
                sourceIdElement.appendChild(doc.createTextNode(er.getSource().getId().getValue()));
                eventElement.appendChild(sourceIdElement);
                Element sourceNameElement = doc.createElement("sourceName");
                sourceNameElement.appendChild(doc.createTextNode(er.getSource().getName()));
                eventElement.appendChild(sourceNameElement);
                Element textElement = doc.createElement("text");
                textElement.appendChild(doc.createTextNode(er.getText()));
                eventElement.appendChild(textElement);
                Element typeElement = doc.createElement("type");
                typeElement.appendChild(doc.createTextNode(er.getType()));
                eventElement.appendChild(typeElement);
                Element timeElement = doc.createElement("time");
                timeElement.appendChild(doc.createTextNode(er.getDateTime().toString()));
                eventElement.appendChild(timeElement);
                Element creationTimeElement = doc.createElement("creationTime");
                creationTimeElement.appendChild(doc.createTextNode(er.getCreationDateTime().toString()));
                eventElement.appendChild(creationTimeElement);
            });
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);
            logger.info("\n" + writer.toString());
            return writer.toString();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
        return "";
    }
}