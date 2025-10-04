package com.tomaytotomato;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class CityIgnoreTimezoneMixin {
    @JsonIgnore
    abstract void setTimezone(String timezone);
}
