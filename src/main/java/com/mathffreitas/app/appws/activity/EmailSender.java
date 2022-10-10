package com.mathffreitas.app.appws.activity;

public interface EmailSender {

    void sendVerification (String to, String email);
    public boolean sendPasswordReset(String to, String email);
    public String buildPasswordResetTokenEmail (String name, String lastName, String link);
    public String buildEmailVerificationToken(String name, String lastName, String link);
}
