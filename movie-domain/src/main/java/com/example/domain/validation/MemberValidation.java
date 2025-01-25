package com.example.domain.validation;

import com.example.domain.exception.CustomException;
import com.example.domain.exception.ErrorCode;
import com.example.domain.model.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberValidation {

    public void validateMember(Member member) {
        if (member == null) {
            throw new CustomException(ErrorCode.INVALID_MEMBER);
        }
    }

}
