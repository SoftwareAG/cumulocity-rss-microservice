package c8y.rss.measurement;

import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.cumulocity.sdk.client.measurement.MeasurementCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ReadMeasurements {
    private static final Logger logger = LoggerFactory.getLogger(ReadMeasurements.class);

    @Autowired
    private MeasurementApi measurementApi;

    @Autowired
    private MicroserviceSubscriptionsService subscriptionsService;

    @EventListener
    private void onMicroserviceSubscriptionAddedEvent(final MicroserviceSubscriptionAddedEvent event) {
        String tenantId = event.getCredentials().getTenant();
        this.subscriptionsService.runForTenant(tenantId, () -> {
            //logger.info("The subscribed tenant id for measurement: " + tenantId);
            //readAllMeasurements(); // Just for quick testing
        });
    }

    public void readAllMeasurements() {
        MeasurementCollection measurementCollection = measurementApi.getMeasurements();
        Iterable<MeasurementRepresentation> mrIterable = measurementCollection.get(2000).allPages();
        mrIterable.forEach((mr) -> {
            String dateTime = mr.getDateTime().toString();
            String id = mr.getId().getValue();
            String sourceId = mr.getSource().getId().getValue();
            String type = mr.getType();

            Map<String, Object> properties = mr.getAttrs();
            Set<String> propertyKeySet = properties.keySet();
            for (String propertyKey: propertyKeySet) {
                logger.info("Fragment: {}", propertyKey);
                Map<String, Object> seriesObject = (Map<String, Object>) properties.get(propertyKey);
                List<String[]> seriesData = new ArrayList<>();
                for (String seriesKey: seriesObject.keySet()) {
                    //logger.info("Series: {}", seriesKey );
                    String[] sData = new String[3];
                    Map<String, Object> series = (Map<String, Object>) seriesObject.get(seriesKey);
                    Set<String> sKey = series.keySet();
                    String sUnitValue = "";
                    BigDecimal sValueValue = null;
                    for(String sk: sKey) {
                        //logger.info("The value of sk: {}", sk);
                        if(sk.equals("unit")) {
                            sUnitValue = (String) series.get(sk);
                            //logger.info("The value of s unit: {}", sUnitValue);
                        }
                        if(sk.equals("value")) {
                            sValueValue = BigDecimal.valueOf(Long.parseLong((series.get(sk)).toString()));
                            //logger.info("The value of s value: {}", sValueValue);
                        }
                    }
                    sData[0] = seriesKey;
                    sData[1] = sUnitValue;
                    sData[2] = sValueValue.toString();
                    seriesData.add(sData);
                    //logger.info("The dateTime {}, id {}, sourceId {}, type {}, fragment {}, series {}, unit {}, value {}", dateTime, id, sourceId, type, propertyKey, seriesKey, sUnitValue, sValueValue);
                    logger.info("The dateTime: {}, id: {}, sourceId: {}, type: {}, fragment: {}", dateTime, id, sourceId, type, propertyKey);
                    for (String[] sArray: seriesData) {
                        logger.info("Series Name: {}", sArray[0]);
                        logger.info("Series Unit: {}", sArray[1]);
                        logger.info("Series Value: {}", BigDecimal.valueOf(Double.valueOf(sArray[2])));
                    }
                }
            }
            //logger.info("---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----");
        });
    }
}
