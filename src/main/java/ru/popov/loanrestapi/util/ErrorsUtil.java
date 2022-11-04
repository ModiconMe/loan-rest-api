package ru.popov.loanrestapi.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.popov.loanrestapi.util.exceptions.LoanNotFoundException;

import java.util.List;

public class ErrorsUtil {
    public static void returnErrorsToClient(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage())
                    .append(";");
        }

        throw new LoanNotFoundException(errorMsg.toString());
    }
}
