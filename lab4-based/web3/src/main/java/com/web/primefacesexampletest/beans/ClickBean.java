package com.web.primefacesexampletest.beans;

import com.web.primefacesexampletest.database.Request;
import com.web.primefacesexampletest.database.RequestsRepository;
import com.web.primefacesexampletest.dto.Point;
import com.web.primefacesexampletest.dto.RawPoint;
import com.web.primefacesexampletest.mbeans.Area;
import com.web.primefacesexampletest.util.MBeanRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@ApplicationScoped
@Named("clickBean")
public class ClickBean {

    @Inject
    private ImageBean imageBean;

    @Inject
    private RequestsRepository requestRepository;

    @Inject
    private ValidateBean validateBean;

    @Inject
    private PointConverter pointConverter;

    @Getter
    private Float r = 1f;

    @Getter
    @Setter
    private Integer rawX;

    @Getter
    @Setter
    private Integer rawY;

    private final Area areaMBean = new Area();


    public void init(@Observes @Initialized(ApplicationScoped.class) Object unused) {
        MBeanRegistry.registerBean(areaMBean, "areaMBean");
    }

    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object unused) {
        MBeanRegistry.unregisterBean(areaMBean);
    }


    public boolean click(Integer rawX, Integer rawY) {
        Point point = pointConverter.getPoint(new RawPoint(rawX, rawY));
        var result = calculateAndSave(point, r);
        imageBean.redraw(r);
        return result.getHit();
    }

    public Request calculateAndSave(Float x, Float y, Float r) {
        Point point = new Point(x, y);
        return calculateAndSave(point, r);
    }

    public Request calculateAndSave(Point p, Float r) {
        var request = Request.builder()
                .x(p.getX())
                .y(p.getY())
                .r(r)
                .hit(validateBean.validate(p, r));

        request.endTime(Timestamp.valueOf(LocalDateTime.now()));
        var result = request.build();
        requestRepository.save(result);
        return result;
    }

    public StreamedContent getImage() {
        return imageBean.redraw(r);
    }

    public void setR(Float r) {
        this.r = r;
        areaMBean.setR(r);
    }


    public void handleCoordinates() {
        FacesContext context = FacesContext.getCurrentInstance();
        String x = context.getExternalContext().getRequestParameterMap().get("x");
        String y = context.getExternalContext().getRequestParameterMap().get("y");

        if (x != null && y != null) {
            System.out.println("Получены координаты: X=" + x + ", Y=" + y);
            boolean res = click(
                    Integer.parseInt(x),
                    Integer.parseInt(y)
            );

            PrimeFaces.current().ajax().addCallbackParam("result", res);
        }
    }
}
