package net.ogorkis.rest.exceptions;

import com.google.common.collect.ImmutableMap;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@XmlRootElement
@XmlAccessorType(FIELD)
class ExceptionJsonResponse {

    private Map<String, String> validationErrors;

    public ExceptionJsonResponse(String validationKey, String validationMessage) {
        this.validationErrors = ImmutableMap.of(validationKey, validationMessage);
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
