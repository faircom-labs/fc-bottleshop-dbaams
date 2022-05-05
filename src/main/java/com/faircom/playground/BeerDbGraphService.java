package com.faircom.playground;

import com.faircom.db.client.helper.FieldComparison;
import com.faircom.playground.model.*;
import com.faircom.playground.data.BeerDb;
import com.faircom.playground.model.gql.BeerFilter;
import com.faircom.playground.model.gql.BeerSorter;
import com.faircom.playground.util.*;
import com.google.common.flogger.FluentLogger;
import org.eclipse.microprofile.graphql.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@GraphQLApi
public class BeerDbGraphService {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Inject
    BeerDb db;

    @Query("getBreweries")
    @Description("Get breweries")
    public List<Brewery> getBreweries(@Name("skip") Paginator paginator) {

        List<Brewery> breweries = db.getBreweries();

        if (paginator == null)
            return breweries;

        return breweries.stream().skip(paginator.getStart() - 1).limit(paginator.getCount()).collect(Collectors.toList());
    }

    @Query("getBrewery")
    @Description("Get brewery by id")
    public Brewery getBrewery(int id) {

        return db.getBreweryById(id);
    }

    @Query("getBeers")
    @Description("Get beers")
    public List<Beer> getBeers(@Name("filter") BeerFilter beerFilter, @Name("skip") Paginator paginator, @Name("sort") BeerSorter sorter) {

        List<FieldComparison> fieldComparisons = new ArrayList<>();

        if (beerFilter != null) {

            FilterField<Float> abvFilter = beerFilter.getAbv();
            FilterField<Integer> breweryIdFilter = beerFilter.getBreweryId();
            FilterField<Integer> ibuFilter = beerFilter.getIbu();
            FilterField<Float> ouncesFilter = beerFilter.getOunces();
            FilterField<String> styleFilter = beerFilter.getStyle();

            if (abvFilter != null) {

                FieldComparison abvComparison = FieldComparison.create("abv", String.valueOf(abvFilter.getValue()), abvFilter.getOperator().code);

                fieldComparisons.add(abvComparison);
            }

            if (breweryIdFilter != null) {

                FieldComparison breweryIdComparison = FieldComparison.create("breweryId", String.valueOf(breweryIdFilter.getValue()), breweryIdFilter.getOperator().code);

                fieldComparisons.add(breweryIdComparison);
            }

            if (ibuFilter != null) {

                FieldComparison ibuComparison = FieldComparison.create("ibu", String.valueOf(ibuFilter.getValue()), ibuFilter.getOperator().code);

                fieldComparisons.add(ibuComparison);
            }

            if (ouncesFilter != null) {

                FieldComparison ouncesComparison = FieldComparison.create("ounces", String.valueOf(ouncesFilter.getValue()), ibuFilter.getOperator().code);

                fieldComparisons.add(ouncesComparison);
            }

            if (styleFilter != null) {

                FieldComparison styleComparison = FieldComparison.create("style", styleFilter.getValue(), styleFilter.getOperator().code);

                fieldComparisons.add(styleComparison);
            }
        }

        List<Beer> beers = db.getBeers(fieldComparisons);

        Stream<Beer> results;

        if (sorter != null) {

            SortDirection abv = sorter.getAbv();
            SortDirection ibu = sorter.getIbu();

            List<SortField> sortFields = new ArrayList<>();

            if (abv != null) {

                SortField abvSortField = new SortField("abv", abv);

                sortFields.add(abvSortField);
            }

            if (ibu != null) {

                SortField ibuSortField = new SortField("ibu", ibu);

                sortFields.add(ibuSortField);
            }

            Comparator<Object> comparator = new CompoundComparator(sortFields);

            results = beers.stream().sorted(comparator::compare);
        }
        else
            results = beers.stream();


        if (paginator == null)
            return results.collect(Collectors.toList());

        return results.skip(paginator.getStart() - 1).limit(paginator.getCount()).collect(Collectors.toList());
    }

    @Query("getCoolerBeers")
    @Description("Get beers in coolers")
    public List<CoolerBeer> getCoolerBeers(@Name("skip") Paginator paginator) {

        List<CoolerBeer> coolerBeers = db.getCoolerBeers();

        if (paginator == null)
            return coolerBeers;

        return coolerBeers.stream().skip(paginator.getStart() - 1).limit(paginator.getCount()).collect(Collectors.toList());
    }

    public List<Beer> getBeers(@Source Brewery brewery) {

        return db.getBeersByBrewery(brewery.getId());
    }

    public Brewery getBrewery(@Source Beer beer) {

        return db.getBreweryById(beer.getBreweryId());
    }
}
