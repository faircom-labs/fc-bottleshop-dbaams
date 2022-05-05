package com.faircom.playground;

import com.faircom.playground.data.BeerDb;
import com.faircom.playground.model.CoolerBeer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class CoolersListener {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Inject
    BeerDb db;

    @Incoming("coolers")
    public void consume(byte[] msg) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonNode actualObj = mapper.readTree(msg);

            int coolerId = actualObj.get("coolerId").asInt();
            int beerId = actualObj.get("beerId").asInt();
            String change = actualObj.get("change").asText();

            CoolerBeer coolerBeer = db.getCoolerBeer(coolerId, beerId);

            boolean update = (coolerBeer != null);

            int quantity;

            if ("+".equals(change)) {

                quantity = update ? coolerBeer.getQuantity() + 1 : 1;
            }
            else {

                quantity = (update && coolerBeer.getQuantity() > 0) ? coolerBeer.getQuantity() -1 : 0;
            }

            db.setCoolerBeer(coolerId, beerId, quantity, update);

        }
        catch (IOException ex) {

            logger.atSevere().withCause(ex).log(ex.getMessage());
        }
    }
}
