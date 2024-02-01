package com.teamsparta.goover.global.exception


import com.teamsparta.goover.global.exception.dto.ErrorResponse
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e:IllegalArgumentException):ResponseEntity<ErrorResponse>{
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }
    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(e:DuplicateKeyException):ResponseEntity<ErrorResponse>{
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse("닉네임이 사용중입니다"))
    }
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(ex: ModelNotFoundException): ResponseEntity<ErrorResponse> {
        val errorMessage = "존재하지 않습니다"
        println(errorMessage)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(errorMessage))
    }

}