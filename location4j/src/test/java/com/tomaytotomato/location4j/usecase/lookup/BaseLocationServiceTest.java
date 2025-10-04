package com.tomaytotomato.location4j.usecase.lookup;

import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseLocationServiceTest {
  
  private static LocationService sharedLocationService;
  
  protected LocationService getLocationService() {
    if (sharedLocationService == null) {
      sharedLocationService = LocationService.builder().build();
    }
    return sharedLocationService;
  }
}
