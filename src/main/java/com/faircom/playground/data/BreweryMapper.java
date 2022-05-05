package com.faircom.playground.data;

import com.faircom.db.client.FDBException;
import com.faircom.db.client.RecordFields;
import com.faircom.db.client.helper.RecordMapper;
import com.faircom.playground.model.Brewery;
import com.google.common.flogger.FluentLogger;

public class BreweryMapper implements RecordMapper<Brewery> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public Brewery fromRecordFields(RecordFields fields) {

        try {

            Brewery brewery = new Brewery();

            brewery.setId(fields.getFieldAsInt("id"));
            brewery.setName(fields.getFieldAsString("name"));
            brewery.setCity(fields.getFieldAsString("city"));
            brewery.setState(fields.getFieldAsString("state"));

            return brewery;
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }

        return null;
    }
}
