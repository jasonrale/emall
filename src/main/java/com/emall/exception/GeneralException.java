package com.emall.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class GeneralException extends RuntimeException {
    private String message;
}
