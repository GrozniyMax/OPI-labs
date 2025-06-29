package com.web.primefacesexampletest.beans;

import com.web.primefacesexampletest.database.RequestsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;
import lombok.experimental.FieldDefaults;

@ApplicationScoped
@Named("formBean")

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class FormBean {

    @NonNull
    Float x;

    Float y;

    @Inject
    ClickBean clickBean;

    public String submit(Float r) {
        clickBean.calculateAndSave(x, y, r);
        clickBean.setR(r);
        return "index.xhtml";
    }

}
