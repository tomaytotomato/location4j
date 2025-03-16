package com.tomaytotomato.location4j;

import com.tomaytotomato.location4j.loader.CountriesDataLoader;
import com.tomaytotomato.location4j.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.normaliser.TextNormaliser;

public final class LocationServiceBuilder {

  private TextNormaliser textNormaliser = new DefaultTextNormaliser();
  private CountriesDataLoader countriesDataLoader = new DefaultCountriesDataLoaderImpl();

  LocationServiceBuilder() {
  }

  public LocationServiceBuilder withCountriesDataLoader(CountriesDataLoader countriesDataLoader) {
    this.countriesDataLoader = countriesDataLoader;
    return this;
  }

  public LocationServiceBuilder withTextNormaliser(TextNormaliser textNormaliser) {
    this.textNormaliser = textNormaliser;
    return this;
  }

  public LocationService build() {
    return new LocationService(textNormaliser, countriesDataLoader);
  }
}
