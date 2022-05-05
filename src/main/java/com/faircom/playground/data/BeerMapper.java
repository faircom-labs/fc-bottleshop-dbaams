package com.faircom.playground.data;

import com.faircom.db.client.FDBException;
import com.faircom.db.client.RecordFields;
import com.faircom.db.client.helper.RecordMapper;
import com.faircom.playground.model.Beer;
import com.google.common.flogger.FluentLogger;

public class BeerMapper implements RecordMapper<Beer> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public Beer fromRecordFields(RecordFields fields) {

        try {

            Beer beer = new Beer();

            beer.setId(fields.getFieldAsInt("id"));

            Integer ibu = fields.getFieldAsInt("ibu");

            if (ibu != null)
                beer.setIbu(ibu);

            beer.setBreweryId(fields.getFieldAsInt("breweryId"));

            beer.setOunces(fields.getFieldAsFloat("ounces"));

            String abv = fields.getFieldAsString("abv");

            if (abv != null)
                beer.setAbv(fields.getFieldAsFloat("abv"));

            beer.setName(fields.getFieldAsString("name"));
            beer.setStyle(fields.getFieldAsString("style"));

            return beer;
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }

        return null;
    }
}
