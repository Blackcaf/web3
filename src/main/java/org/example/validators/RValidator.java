package org.example.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import org.example.config.Config;

@FacesValidator("rValidator")
public class RValidator implements Validator<Double> {

    @Override
    public void validate(FacesContext context, UIComponent component, Double value) throws ValidatorException {
        if (value == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Validation Error", "R value cannot be empty"));
        }

        double min = Config.getMinR().doubleValue();
        double max = Config.getMaxR().doubleValue();

        if (value < min || value > max) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Validation Error",
                    String.format("R value must be between %.1f and %.1f", min, max)));
        }
    }
}