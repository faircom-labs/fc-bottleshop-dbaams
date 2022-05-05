package com.faircom.playground.data;

import com.faircom.db.client.FDBException;
import com.faircom.db.client.RecordFields;
import com.faircom.db.client.helper.RecordMapper;
import com.faircom.playground.model.CoolerBeer;
import com.google.common.flogger.FluentLogger;

public class CoolerBeerMapper implements RecordMapper<CoolerBeer> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public CoolerBeer fromRecordFields(RecordFields fields) {

        try {

            CoolerBeer coolerBeer = new CoolerBeer();

            coolerBeer.setCoolerId(fields.getFieldAsInt("coolerId"));
            coolerBeer.setBeerId(fields.getFieldAsInt("beerId"));
            coolerBeer.setQuantity(fields.getFieldAsInt("quantity"));

            return coolerBeer;
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }

        return null;
    }
}
