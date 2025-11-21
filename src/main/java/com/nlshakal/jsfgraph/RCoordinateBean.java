package com.nlshakal.jsfgraph;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Data
@NoArgsConstructor
public class RCoordinateBean implements Serializable {
    private Double r = 0.0;


    public void validateRBeanValue(FacesContext facesContext, UIComponent uiComponent, Object o){
        if (o == null){
            FacesMessage message = new FacesMessage("R value should be in (1, 4) interval");
            throw new ValidatorException(message);
        }
    }

    @PostConstruct
    public void init() {
        if (r == null || r == 0.0) {
            r = 3.0;
        }
    }
}
