package com.emall.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 自定义通用异常
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class GeneralException extends RuntimeException {
    private String message;
}
