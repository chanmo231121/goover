package com.teamsparta.goover.exception


import com.teamsparta.goover.exception.dto.ErrorResponse
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException.BadRequest


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e:IllegalArgumentException):ResponseEntity<ErrorResponse>{
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(ErrorResponse(e.message))
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(e:DuplicateKeyException):ResponseEntity<ErrorResponse>{
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse("닉네임이 사용중입니다"))
    }

}