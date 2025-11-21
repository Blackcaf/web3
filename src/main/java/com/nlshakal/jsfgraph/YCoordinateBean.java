package com.nlshakal.jsfgraph;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.validator.ValidatorException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class YCoordinateBean implements Serializable {
    private Double y = 0.0;


    public void validateYBeanValue(Object o){
        if (o == null){
            FacesMessage message = new FacesMessage("Y value should be in (-3, 5) interval");
            throw new ValidatorException(message);
        }
    }
}
