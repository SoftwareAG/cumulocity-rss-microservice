package c8y.rss.rest;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import c8y.rss.alarm.ReadAlarms;
import c8y.rss.event.ReadEvents;

@RestController
public class RSSRest {
    private static final Logger logger = LoggerFactory.getLogger(RSSRest.class);

    @Autowired
    private ReadAlarms readAlarms;

    @Autowired
    private ReadEvents readEvents;

    @RequestMapping(value = "/alarms/rss.xml", method = RequestMethod.GET, produces = {MediaType.APPLICATION_RSS_XML_VALUE})
    public ResponseEntity<String> readLatestAlarms(@QueryParam("source") String source, @QueryParam("severity") String severity,
                                           @QueryParam("status") String status, @QueryParam("type") String type, @QueryParam("feedSize") String feedSize) {

        logger.info("The values of deviceId: {}, severity: {}, status: {}, type: {}, batchSize: {}", source, severity, status, type, feedSize);

        String rssFeedXMLString = readAlarms.readLatestAlarms(source, severity, status, type, feedSize);
        return new ResponseEntity<>(rssFeedXMLString, HttpStatus.OK);
    }

    @RequestMapping(value = "/events/rss.xml", method = RequestMethod.GET, produces = {MediaType.APPLICATION_RSS_XML_VALUE})
    public ResponseEntity<String> readLatestEvents(@QueryParam("source") String source, @QueryParam("type") String type, @QueryParam("feedSize") String feedSize) {

        logger.info("The values of deviceId: {}, type: {}, batchSize: {}", source, type, feedSize);

        String rssFeedXMLString = readEvents.readLatestEvents(source, type, feedSize);
        return new ResponseEntity<>(rssFeedXMLString, HttpStatus.OK);
    }

}
