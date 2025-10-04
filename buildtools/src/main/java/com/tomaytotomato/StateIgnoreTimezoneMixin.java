package com.tomaytotomato;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class StateIgnoreTimezoneMixin {
    @JsonIgnore
    abstract void setTimezone(Object timezone);
}
