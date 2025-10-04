package com.tomaytotomato.location4j.loader;

import com.tomaytotomato.location4j.aliases.DefaultLocationAliases;
import com.tomaytotomato.location4j.mapper.DefaultSearchLocationResultMapper;
import com.tomaytotomato.location4j.text.normaliser.DefaultTextNormaliser;
import com.tomaytotomato.location4j.text.tokeniser.DefaultTextTokeniser;
import com.tomaytotomato.location4j.usecase.lookup.LocationService;
import com.tomaytotomato.location4j.usecase.search.SearchLocationService;
import java.util.Objects;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestDataLoader {

  private static DataLoader dataLoader;
  private static LocationService locationService;
  private static SearchLocationService searchLocationService;

  protected DataLoader getDataLoader() {
    if (Objects.isNull(dataLoader)) {
      dataLoader = new DefaultDataLoader();
    }
    return dataLoader;
  }

  protected SearchLocationService getSearchLocationService() {
    if (Objects.isNull(searchLocationService)) {
      searchLocationService = SearchLocationService.builder()
          .withLocationAliases(new DefaultLocationAliases())
          .withLocationMapper(new DefaultSearchLocationResultMapper())
          .withTextNormaliser(new DefaultTextNormaliser())
          .withTextTokeniser(new DefaultTextTokeniser())
          .withDataLoader(getDataLoader())
          .build();
    }
    return searchLocationService;
  }

  protected LocationService getLocationService() {
    if (Objects.isNull(locationService)) {
      locationService = LocationService.builder()
          .withDataLoader(getDataLoader())
          .build();
    }
    return locationService;
  }

}
