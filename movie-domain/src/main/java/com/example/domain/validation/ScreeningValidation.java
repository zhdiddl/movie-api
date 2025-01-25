package com.example.domain.validation;

import com.example.domain.exception.CustomException;
import com.example.domain.exception.ErrorCode;
import com.example.domain.model.entity.Screening;
import org.springframework.stereotype.Component;

@Component
public class ScreeningValidation {

    public void validateScreening(Screening screening) {
        if (screening == null) {
            throw new CustomException(ErrorCode.INVALID_SCREENING);
        }
    }

}
