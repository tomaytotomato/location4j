package com.tomaytotomato;

import com.tomaytotomato.loader.CountriesDataLoader;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.text.normaliser.TextNormaliser;

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
