package com.web.primefacesexampletest.beans;

import com.web.primefacesexampletest.dto.Point;
import com.web.primefacesexampletest.dto.RawPoint;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ValidateBean {


    @Inject
    private ImageBean imageBean;


    public boolean validate(Point point, Float r) {
        switch (point.getQuadrant()) {
            case 1:
                return false;
            case 2:
                return -r <= point.getX() && point.getY() <= r / 2;
            case 3:
                return point.getX() * point.getX() + point.getY() * point.getY() <= r * r;
            case 4:
                return point.getY() >= r * point.getX() - r / 2;
            default:{
                System.out.println("Invalid quadrant");
                return false;
            }
        }
    }


}
