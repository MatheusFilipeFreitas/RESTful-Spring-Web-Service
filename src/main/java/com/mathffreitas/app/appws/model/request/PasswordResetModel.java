package com.mathffreitas.app.appws.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetModel {
    private String token;
    private String password;

}
