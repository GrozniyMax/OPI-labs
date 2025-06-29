package com.web.primefacesexampletest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RawPoint {

    @NonNull
    Integer x;

    @NonNull
    Integer y;
}
