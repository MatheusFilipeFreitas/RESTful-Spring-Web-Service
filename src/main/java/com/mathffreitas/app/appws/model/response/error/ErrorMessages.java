package com.mathffreitas.app.appws.model.response.error;

public enum ErrorMessages {

    // for all
    MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    AUTHENTICATION_ERROR("Authentication failed"),

    // user
    USER_RECORD_ALREADY_EXISTS("User record already exists"),
    USER_NO_RECORD_FOUND("User record with provided id is not found"),
    USER_COULD_NOT_UPDATE_RECORD("Could not update user record"),
    USER_COULD_NOT_DELETE_RECORD("Could not delete user record"),
    USER_EMAIL_ADDRESS_NOT_VERIFIED("User email address could not be verified"),
    USER_PASSWORD_TOKEN_EXPIRED("User reset password token has already expired"),
    USER_PASSWORD_TOKEN_NOT_FOUND("User reset password token is not found"),

    // address
    ADDRESS_RECORD_ALREADY_EXISTS("Address record already exists"),
    ADDRESS_NO_RECORD_FOUND("Address record with provided id is not found"),
    ADDRESS_COULD_NOT_UPDATE_RECORD("Could not update address record"),
    ADDRESS_COULD_NOT_DELETE_RECORD("Could not delete address record");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
