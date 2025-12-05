package org.example.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import org.example.config.Config;

@FacesValidator("yValidator")
public class YValidator implements Validator<Double> {

    @Override
    public void validate(FacesContext context, UIComponent component, Double value) throws ValidatorException {
        if (value == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Validation Error", "Y value cannot be empty"));
        }

        double min = Config.getMinY();
        double max = Config.getMaxY();

        if (value < min || value > max) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Validation Error",
                    String.format("Y value must be between %.1f and %.1f", min, max)));
        }
    }
}