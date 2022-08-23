package com.mathffreitas.app.appws.activity;

public interface EmailSender {

    void sendVerification (String to, String email);
    public boolean sendPasswordReset(String to, String email);
}
