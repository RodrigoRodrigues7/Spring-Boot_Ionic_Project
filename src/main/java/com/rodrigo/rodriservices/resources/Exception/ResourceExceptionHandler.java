package com.rodrigo.rodriservices.resources.Exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.rodrigo.rodriservices.services.exceptions.AuthorizationException;
import com.rodrigo.rodriservices.services.exceptions.DataIntegrityException;
import com.rodrigo.rodriservices.services.exceptions.FileException;
import com.rodrigo.rodriservices.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {

		StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), "Not Found", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {

		StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Data Integrity", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

		ValidationError error = new ValidationError(System.currentTimeMillis(), HttpStatus.UNPROCESSABLE_ENTITY.value(),"Erro de Validação", e.getMessage(), request.getRequestURI());
		for(FieldError x : e.getBindingResult().getFieldErrors()) {
			error.addError(x.getField(), x.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
	}
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {

		StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(), "Acesso Negado", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
	
	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request) {

		StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro de Arquivo", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest request) {
		
		HttpStatus code = HttpStatus.valueOf(e.getErrorCode());
		StandardError error = new StandardError(System.currentTimeMillis(), code.value(), "Erro Amazon Service", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(code).body(error);
	}
	
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request) {
		
		StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro Amazon Client", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest request) {
		
		StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro Amazon S3", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
}
