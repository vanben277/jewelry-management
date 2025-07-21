package com.example.jewelry_management.exception;

import com.example.jewelry_management.dto.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null, e.getErrorCode()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException e) {
        log.error("Not found: {}", String.valueOf(e));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null, e.getErrorCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        log.error("Sever error: {}", String.valueOf(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null, "INTERNAl_SERVER"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException ife) {
            if (ife.getTargetType().isEnum()) {
                String fieldName = ife.getPath().getFirst().getFieldName();
                String invalidValue = ife.getValue().toString();
                String enumName = ife.getTargetType().getSimpleName();

                return ResponseEntity.badRequest().body(new ApiResponse(
                        "Giá trị không hợp lệ cho trường '" + fieldName + "': '" + invalidValue + "'. Hãy dùng giá trị hợp lệ cho " + enumName,
                        null,
                        "BAD_REQUEST"
                ));
            }
        }

        return ResponseEntity.badRequest().body(new ApiResponse(
                "Body yêu cầu không được để trống hoặc sai định dạng",
                null,
                "BAD_REQUEST"
        ));
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ApiResponse> handleIncorrectResultSizeException(IncorrectResultSizeDataAccessException e) {
        log.error("Multiple results found for query: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Truy van tra ve nhieu ket qua hon mong doi", null, "MULTIPLE_RESULTS_FOUND"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgNotValidEx(MethodArgumentNotValidException e) {
        var allErrors = e.getBindingResult().getAllErrors();
        StringBuilder resultMessage = new StringBuilder("Dữ liệu bạn nhập không hợp lệ: ");
        int count = 0;
        for (var error : allErrors) {
            resultMessage.append(error.getDefaultMessage());
            if (count != (allErrors.size() - 1)) {
                resultMessage.append(", ");
            }
            count++;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(resultMessage.toString(), null, null));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null, "UNAUTHORIZED"));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse> handleForbiddenException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), null, "FORBIDDEN"));
    }
}
