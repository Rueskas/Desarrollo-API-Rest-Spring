package com.iessanvicente.rest.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ProductoNotFoundException.class)
	public ResponseEntity<ApiError> handleProductoNorFoundException(ProductoNotFoundException e){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), e.getMessage());
		
		return ResponseEntity.status(error.getEstado()).body(error);
	}
	
	@ExceptionHandler(CategoriaNotFoundException.class)
	public ResponseEntity<ApiError> handleMappingJsonException(CategoriaNotFoundException e){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), e.getMessage());
		
		return ResponseEntity.status(error.getEstado()).body(error);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(status,LocalDateTime.now(), ex.getMessage());
		return ResponseEntity.status(status).headers(headers).body(apiError);
	}
	
	/*
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<ApiError> handleMappingJsonException(JsonMappingException e){
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
		
		return ResponseEntity.status(error.getEstado()).body(error);
	}
	*/
}
