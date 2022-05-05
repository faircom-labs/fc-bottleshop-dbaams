package com.faircom.playground.data;

import FairCom.CtreeDb.Engine;
import FairCom.CtreeDb.Types.FIND_MODE;
import FairCom.CtreeDb.Types.SessionType;
import com.faircom.db.client.*;
import com.faircom.db.client.Record;
import com.faircom.db.client.helper.FieldComparison;
import com.faircom.db.client.helper.FieldFilter;
import com.faircom.db.client.helper.RecordFinder;
import com.faircom.db.client.helper.RecordMapper;
import com.faircom.playground.model.*;
import com.google.common.flogger.FluentLogger;
import io.quarkus.runtime.Startup;

import javax.inject.Singleton;
import java.util.List;

@Startup
@Singleton
public class BeerDb {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    Session session;

    Database db;

    Table tblBreweries;
    Table tblBeers;
    Table tblCoolerBeers;

    RecordFinder<Brewery> breweryFinder;
    RecordFinder<Beer> beerFinder;
    RecordFinder<CoolerBeer> coolerBeerFinder;

    public BeerDb() {

        try {

            if (!Engine.isValid())
                Engine.StartDatabaseEngine();

            session = new Session(SessionType.CTDB.getCode());

            session.logon("", "", "", true);

            Schema schema = session.getSchema();

            try {

                db = schema.connectToDatabase("store");
            }
            catch (FDBException ex) {

                schema.createDatabase("store", ".");

                db = schema.connectToDatabase("store");

                BeerDbBuilder dbBuilder = new BeerDbBuilder(session, db);

                dbBuilder.createSchemaLoadSampleData();
            }

            tblBreweries = db.getTable("breweries");
            tblBeers = db.getTable("beers");
            tblCoolerBeers = db.getTable("coolers");
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }

        RecordMapper<Brewery> breweryMapper = new BreweryMapper();
        RecordMapper<Beer> beerMapper = new BeerMapper();
        RecordMapper<CoolerBeer> coolerBeerMapper = new CoolerBeerMapper();

        breweryFinder = new RecordFinder<>(tblBreweries, breweryMapper);
        beerFinder = new RecordFinder<>(tblBeers, beerMapper);
        coolerBeerFinder = new RecordFinder<>(tblCoolerBeers, coolerBeerMapper);
    }

    public List<Beer> getBeers(List<FieldComparison> fieldComparisons) {

        if (fieldComparisons.isEmpty())
            return beerFinder.findAll();
        else
            return beerFinder.findMany(fieldComparisons);
    }

    public List<Beer> getBeersByBrewery(int breweryId) {

        return beerFinder.findMany("beerBrewery", "breweryId", breweryId);
    }

    public List<Brewery> getBreweries() {

        return breweryFinder.findAll();
    }

    public Brewery getBreweryById(int id) {

        FieldFilter breweryIdFilter = FieldFilter.create("id", id);

        return breweryFinder.findOne("breweryId", breweryIdFilter);
    }

    public CoolerBeer getCoolerBeer(int coolerId, int beerId) {

        FieldFilter coolerIdFilter = FieldFilter.create("coolerId", coolerId);
        FieldFilter beerIdFilter = FieldFilter.create("beerId", beerId);

        return coolerBeerFinder.findOne("coolerIdBeerId", coolerIdFilter, beerIdFilter);
    }

    public List<CoolerBeer> getCoolerBeers() {

        return coolerBeerFinder.findAll();
    }

    public void setCoolerBeer(int coolerId, int beerId, int quantity, boolean update) {

        try (Table tmpTblCoolerBeers = db.getTable("coolers")) {

            Record coolerBeerRec;

            if (update) {

                IndexExactMatch coolerBeerIndex = tmpTblCoolerBeers.newIndexExactMatch();

                RecordFields coolerBeerIndexFields = coolerBeerIndex.getFields();

                coolerBeerIndexFields.setFieldAsInt("coolerId", coolerId);
                coolerBeerIndexFields.setFieldAsInt("beerId", beerId);

                coolerBeerRec = coolerBeerIndex.find(FIND_MODE.EQUAL);
            }
            else
                coolerBeerRec = tmpTblCoolerBeers.newRecord();

            RecordFields coolerBeerRecFields = coolerBeerRec.getFields();

            coolerBeerRecFields.setFieldAsInt("coolerId", coolerId);
            coolerBeerRecFields.setFieldAsInt("beerId", beerId);
            coolerBeerRecFields.setFieldAsInt("quantity", quantity);

            TransactionManager tx = session.getTransactionManager();

            tx.upsertRecord(tmpTblCoolerBeers, coolerBeerRec);

            coolerBeerRec.close();
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }
        catch (Exception ex) {

            logger.atSevere().withCause(ex).log(ex.getMessage());
        }
    }
}
