package com.faircom.playground.data;

import FairCom.CtreeDb.Types.CREATE_MODE;
import FairCom.CtreeDb.Types.FIELD_TYPE;
import FairCom.CtreeDb.Types.KEY_TYPE;
import FairCom.CtreeDb.Types.SEG_MODE;
import com.faircom.db.client.*;
import com.faircom.db.client.Record;
import com.faircom.db.client.helper.Indexes;
import com.google.common.flogger.FluentLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class BeerDbBuilder {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private Session session;
    private Database db;

    public BeerDbBuilder(Session session, Database db) {

        this.session = session;
        this.db = db;
    }

    public void createSchemaLoadSampleData() {

        TransactionManager tx = session.getTransactionManager();

        try {

            tx.begin();

            createBreweriesTable();
            createBeersTable();
            createCoolerBeersTable();

            tx.commit();

            tx.begin();

            loadSampleBreweryData();
            loadSampleBeerData();

            tx.commit();
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }
    }

    private void createBreweriesTable() {

        try {

            Table tblBreweries = db.newTable();

            TableDefinition tblBreweriesDef = tblBreweries.getDefinition();

            tblBreweriesDef.addField("id", FIELD_TYPE.INTEGER, 0);
            tblBreweriesDef.addField("name", FIELD_TYPE.VARCHAR, 35);
            tblBreweriesDef.addField("city", FIELD_TYPE.VARCHAR, 19);
            tblBreweriesDef.addField("state", FIELD_TYPE.VARCHAR, 2);

            Indexes.createSimpleIndex(tblBreweriesDef, "breweryId", "id", false, false);
            Indexes.createSimpleIndex(tblBreweriesDef, "breweryState", "state", true, false);

            tblBreweries.create("breweries", false, CREATE_MODE.TRNLOG);
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }
    }

    private void createBeersTable() {

        try {

            Table tblBeers = db.newTable();

            TableDefinition tblBeersDef = tblBeers.getDefinition();

            tblBeersDef.addField("id", FIELD_TYPE.INTEGER, 0);
            tblBeersDef.addField("ibu", FIELD_TYPE.INTEGER, 0);
            tblBeersDef.addField("ounces", FIELD_TYPE.INTEGER, 0);
            tblBeersDef.addField("breweryId", FIELD_TYPE.INTEGER, 0);
            tblBeersDef.addField("abv", FIELD_TYPE.FLOAT, 0);
            tblBeersDef.addField("name", FIELD_TYPE.VARCHAR, 52);
            tblBeersDef.addField("style", FIELD_TYPE.VARCHAR, 35);

            Indexes.createSimpleIndex(tblBeersDef, "beerId", "id", false, false);
            Indexes.createSimpleIndex(tblBeersDef, "beerIbu", "ibu", true, true);
            Indexes.createSimpleIndex(tblBeersDef, "beerAbv", "abv", true, true);
            Indexes.createSimpleIndex(tblBeersDef, "beerStyle", "style", true, false);
            Indexes.createSimpleIndex(tblBeersDef, "beerBrewery", "breweryId", true, false);

            Index beerAbvIbuIdx = tblBeersDef.addIndex("beerAbvIbu", KEY_TYPE.FIXED_INDEX, true, true);

            beerAbvIbuIdx.getDefinition().addSegment(tblBeersDef.getField("abv"), SEG_MODE.SCHSEG);
            beerAbvIbuIdx.getDefinition().addSegment(tblBeersDef.getField("ibu"), SEG_MODE.SCHSEG);

            tblBeers.create("beers", false, CREATE_MODE.TRNLOG);
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }
    }

    private void createCoolerBeersTable() {

        try {

            Table tblCoolerBeers = db.newTable();

            TableDefinition tblCoolerBeersDefinition = tblCoolerBeers.getDefinition();

            tblCoolerBeersDefinition.addField("coolerId", FIELD_TYPE.INTEGER, 0);
            tblCoolerBeersDefinition.addField("beerId", FIELD_TYPE.INTEGER, 0);
            tblCoolerBeersDefinition.addField("quantity", FIELD_TYPE.INTEGER, 0);

            Indexes.createSimpleIndex(tblCoolerBeersDefinition, "coolerId", "coolerId", true, false);
            Indexes.createSimpleIndex(tblCoolerBeersDefinition, "coolerBeer", "beerId", true, false);
            Indexes.createSimpleIndex(tblCoolerBeersDefinition, "coolerQuantity", "quantity", true, false);

            Index coolerIdBeerIdIdx = tblCoolerBeersDefinition.addIndex("coolerIdBeerId", KEY_TYPE.FIXED_INDEX, false, true);

            coolerIdBeerIdIdx.getDefinition().addSegment(tblCoolerBeersDefinition.getField("coolerId"), SEG_MODE.SCHSEG);
            coolerIdBeerIdIdx.getDefinition().addSegment(tblCoolerBeersDefinition.getField("beerId"), SEG_MODE.SCHSEG);

            tblCoolerBeers.create("coolers", false, CREATE_MODE.TRNLOG);
        }
        catch (FDBException ex) {

            logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
        }
    }

    private void loadSampleBreweryData() {

        URL breweriesCsv = getClass().getClassLoader().getResource("breweries.csv");

        try {

            assert breweriesCsv != null;

            try (Scanner breweriesCsvScanner = new Scanner(new File(breweriesCsv.toURI()))) {

                try (Table tblBreweries = db.getTable("breweries")) {

                    while (breweriesCsvScanner.hasNextLine()) {

                        try (Scanner breweryValues = new Scanner(breweriesCsvScanner.nextLine())) {

                            breweryValues.useDelimiter(",");

                            Record breweryRec = tblBreweries.newRecord();

                            RecordFields breweryRecFields = breweryRec.getFields();

                            breweryRecFields.setFieldAsInt("id", Integer.valueOf(breweryValues.next()));
                            breweryRecFields.setFieldAsString("name", breweryValues.next());
                            breweryRecFields.setFieldAsString("city", breweryValues.next());
                            breweryRecFields.setFieldAsString("state", breweryValues.next());

                            tblBreweries.upsertRecord(breweryRec);

                            breweryRec.close();
                        }
                    }
                }
                catch (FDBException ex) {

                    logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
                }
                catch (Exception ex) {

                    logger.atSevere().withCause(ex).log(ex.getMessage());
                }
            }
        }
        catch (URISyntaxException | FileNotFoundException ex) {

            logger.atSevere().withCause(ex).log(ex.getMessage());
        }
    }

    private void loadSampleBeerData() {

        URL beersCsv = getClass().getClassLoader().getResource("beers.csv");

        try {

            assert beersCsv != null;

            try (Scanner beersCsvScanner = new Scanner(new File(beersCsv.toURI()))) {

                try  (Table tblBeers = db.getTable("beers")) {

                    while (beersCsvScanner.hasNextLine()) {

                        try (Scanner beerValues = new Scanner(beersCsvScanner.nextLine())) {

                            beerValues.useDelimiter(",");

                            Record beerRec = tblBeers.newRecord();

                            RecordFields beerRecFields = beerRec.getFields();

                            beerRecFields.setFieldAsString("name", beerValues.next());
                            beerRecFields.setFieldAsInt("id", Integer.valueOf(beerValues.next()));

                            String abv = beerValues.next();

                            if (abv.length() > 0)
                                beerRecFields.setFieldAsFloat("abv", new BigDecimal(abv).movePointRight(2).floatValue());

                            String ibu = beerValues.next();

                            if (ibu.length() > 0)
                                beerRecFields.setFieldAsInt("ibu", Integer.valueOf(ibu));

                            beerRecFields.setFieldAsInt("breweryId", Integer.valueOf(beerValues.next()));
                            beerRecFields.setFieldAsString("style", beerValues.next());
                            beerRecFields.setFieldAsFloat("ounces", Float.valueOf(beerValues.next()));

                            tblBeers.upsertRecord(beerRec);

                            beerRec.close();
                        }
                    }
                }
                catch (FDBException ex) {

                    logger.atSevere().withCause(ex).log("%s (%s)", ex.getMessage(), ex.getErrorCode());
                }
                catch (Exception ex) {

                    logger.atSevere().withCause(ex).log(ex.getMessage());
                }
            }
        }
        catch (URISyntaxException | FileNotFoundException ex) {

            logger.atSevere().withCause(ex).log(ex.getMessage());
        }
    }
}
