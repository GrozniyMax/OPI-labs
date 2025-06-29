package com.web.primefacesexampletest.beans;

import com.web.primefacesexampletest.dto.Point;
import com.web.primefacesexampletest.dto.RawPoint;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PointConverter {

    private static final Float coordinatesXMax = 4f;
    private static final Float coordinatesXMin = -4f;

    private static final Float coordinatesYMax = 4f;
    private static final Float coordinatesYMin = -4f;

    private static final Float coordinatesRMax = 4f;
    private static final Float coordinatesRMin = 0f;

    private Integer pixelsXMax;
    private Integer pixelsXMin;

    private Integer pixelsYMax;
    private Integer pixelsYMin;

    private Integer pixelsRMax;
    private Integer pixelsRMin = 0;

    @PostConstruct
    public void init() {
        pixelsXMax = ImageBean.width / 2;
        pixelsXMin = -pixelsXMax;

        pixelsYMax = ImageBean.height / 2;
        pixelsYMin = -pixelsYMax;

        pixelsRMax = ImageBean.width / 2;
    }

    public Point getPoint(RawPoint point) {
        Integer xRelative = point.getX() - pixelsXMax;
        Integer yRelative = pixelsYMax - point.getY();
        System.out.println("Перевожу в координаты относительно центра");
        System.out.printf("X: from %s to %s\n", point.getX(), xRelative);
        System.out.printf("Y: from %s to %s\n", point.getY(), yRelative);


        Float newX = (((float) (xRelative - pixelsXMin)) / (pixelsXMax - pixelsXMin)) * (coordinatesXMax - coordinatesXMin) + coordinatesXMin;
        Float newY = (((float) (yRelative - pixelsYMin)) / (pixelsYMax - pixelsYMin)) * (coordinatesYMax - coordinatesYMin) + coordinatesYMin;

        var result =  new Point(newX, newY);
        System.out.printf("Converted %s to %s\n", point, result);

        return result;
    }

    public RawPoint getRawPoint(Point point) {
        int newX = Math.round(((point.getX() - coordinatesXMin) / (coordinatesXMax - coordinatesXMin)) * (pixelsXMax - pixelsXMin) + pixelsXMin);
        int newY = Math.round(((point.getY() - coordinatesYMin) / (coordinatesYMax - coordinatesYMin)) * (pixelsYMax - pixelsYMin) + pixelsYMin);

        newX  += pixelsXMax;
        newY  = pixelsYMax - newY;
        RawPoint result = new RawPoint(newX, newY);
        System.out.printf("Converted %s to %s\n", point, result);

        return result;
    }

    public Integer getPixelsR(Float r) {
        Integer newR = Math.round(((r - coordinatesRMin) / (coordinatesRMax - coordinatesRMin)) * (pixelsRMax - pixelsRMin) + pixelsRMin);
        System.out.printf("Converted r :%f to %d\n", r, newR);
        return newR;
    }

    public Integer getPixelX(Float x) {
        return Math.round(((x - coordinatesXMin) / (coordinatesXMax - coordinatesXMin)) * (pixelsXMax - pixelsXMin) + pixelsXMin);
    }

    public Integer getPixelY(Float y) {
        return Math.round(((y - coordinatesYMin) / (coordinatesYMax - coordinatesYMin)) * (pixelsYMax - pixelsYMin) + pixelsYMin);
    }
}
