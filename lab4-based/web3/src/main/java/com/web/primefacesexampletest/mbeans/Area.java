package com.web.primefacesexampletest.mbeans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("area")
@ApplicationScoped
public class Area implements AreaMBean, Serializable {

    private volatile double area;

    private volatile double r;


    public void setR(double r) {
        this.r = r;
        computeArea();
    }

    @Override
    public double getArea() {
        return area;
    }

    @Override
    public void computeArea() {
        area = r * r + (r*r)/2 + Math.PI*r*r/4;
    }


}
