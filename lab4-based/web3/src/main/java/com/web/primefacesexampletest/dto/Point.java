package com.web.primefacesexampletest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Point {

    @NonNull
    Float x;

    @NonNull
    Float y;

    public int getQuadrant() {
        if (x >= 0 && y >= 0) {
            return 1;
        } else if (x < 0 && y >= 0) {
            return 2;
        } else if (x < 0 && y < 0) {
            return 3;
        } else {
            return 4;
        }
    }

}
