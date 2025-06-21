package com.example.jewelry_management.exception;

import com.example.jewelry_management.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Lỗi hệ thống", null, "INTERNAl_SERVER"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse("Body yêu cầu không được để trống hoặc sai định dạng", null, "BAD_REQUEST"));
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
}
