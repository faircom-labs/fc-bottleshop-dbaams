package com.faircom.playground.model.gql;

import com.faircom.playground.util.FilterField;

public class BeerFilter {

    private FilterField<Integer> breweryId;
    private FilterField<Integer> ibu;

    private FilterField<Float> abv;
    private FilterField<Float> ounces;

    private FilterField<String> style;

    public FilterField<Float> getAbv() {

        return abv;
    }

    public FilterField<Integer> getBreweryId() {

        return breweryId;
    }

    public FilterField<Integer> getIbu() {

        return ibu;
    }

    public FilterField<Float> getOunces() {

        return ounces;
    }

    public FilterField<String> getStyle() {

        return style;
    }

    public void setAbv(FilterField<Float> abv) {

        this.abv = abv;
    }

    public void setBreweryId(FilterField<Integer> breweryId) {

        this.breweryId = breweryId;
    }

    public void setIbu(FilterField<Integer> ibu) {

        this.ibu = ibu;
    }

    public void setOunces(FilterField<Float> ounces) {

        this.ounces = ounces;
    }

    public void setStyle(FilterField<String> style) {

        this.style = style;
    }
}
