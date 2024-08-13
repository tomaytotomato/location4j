package com.tomaytotomato;

import com.tomaytotomato.aliases.DefaultLocationAliases;
import com.tomaytotomato.aliases.LocationAliases;
import com.tomaytotomato.loader.CountriesDataLoader;
import com.tomaytotomato.loader.DefaultCountriesDataLoaderImpl;
import com.tomaytotomato.mapper.DefaultLocationMapper;
import com.tomaytotomato.mapper.LocationMapper;
import com.tomaytotomato.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.text.normaliser.TextNormaliser;
import com.tomaytotomato.text.tokeniser.DefaultTextTokeniser;
import com.tomaytotomato.text.tokeniser.TextTokeniser;

/**
 * Allows the customisation and creation of the {@link SearchLocationService}
 */
public final class SearchLocationServiceBuilder {

  private TextTokeniser textTokeniser = new DefaultTextTokeniser();
  private TextNormaliser textNormaliser = new DefaultTextNormaliser();
  private LocationMapper locationMapper = new DefaultLocationMapper();
  private LocationAliases locationAliases = new DefaultLocationAliases();
  private CountriesDataLoader countriesDataLoader = new DefaultCountriesDataLoaderImpl();

  SearchLocationServiceBuilder() {
  }

  public SearchLocationServiceBuilder withTextTokeniser(TextTokeniser textTokeniser) {
    this.textTokeniser = textTokeniser;
    return this;
  }

  public SearchLocationServiceBuilder withTextNormaliser(TextNormaliser textNormaliser) {
    this.textNormaliser = textNormaliser;
    return this;
  }

  public SearchLocationServiceBuilder withLocationMapper(LocationMapper locationMapper) {
    this.locationMapper = locationMapper;
    return this;
  }

  public SearchLocationServiceBuilder withLocationAliases(LocationAliases locationAliases) {
    this.locationAliases = locationAliases;
    return this;
  }

  public SearchLocationServiceBuilder withCountriesDataLoader(
      CountriesDataLoader countriesDataLoader) {
    this.countriesDataLoader = countriesDataLoader;
    return this;
  }

  public SearchLocationService build() {
    return new SearchLocationService(textTokeniser, textNormaliser, locationMapper,
        countriesDataLoader,
        locationAliases);
  }
}
