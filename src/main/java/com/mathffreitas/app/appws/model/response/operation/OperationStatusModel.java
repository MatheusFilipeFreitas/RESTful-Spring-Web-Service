package com.mathffreitas.app.appws.model.response.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationStatusModel {
    private String operationResult;
    private String operationName;

}
