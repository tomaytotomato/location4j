package com.tomaytotomato.location4j.usecase.geo;

import com.tomaytotomato.location4j.model.lookup.City;
import java.math.BigDecimal;

public interface GeoLocation {

  City findNearestCity(double latitude, double longitude);

  City findNearestCity(BigDecimal latitude, BigDecimal longitude);

}
