package com.deck.cards.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
		@Override
	   protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	                 HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = ex.getBindingResult()
			    .getFieldErrors()
			    .stream()
			    .map(x -> x.getDefaultMessage())
			    .collect(Collectors.toList());

		ErrorResponse error = new ErrorResponse("Invalid Input", details);
		log.info(error.toString());
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	  }
		
		 @Override
		 protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

			  List<String> details = new ArrayList<>();
			  details.add(ex.getLocalizedMessage()); ErrorResponse error = new
			  ErrorResponse("Server Error", details);
			  
			   log.info(ex.getLocalizedMessage());
			  
			  return new ResponseEntity<Object>(error,
			  HttpStatus.INTERNAL_SERVER_ERROR);

		  }
		
		 @ExceptionHandler(GameNotFoundException.class)
		 public final ResponseEntity<Object> handleGameNotFoundException(GameNotFoundException ex) throws JsonProcessingException {
		        List<String> details = new ArrayList<>();
		        details.add(ex.getLocalizedMessage());
		        ErrorResponse error = new ErrorResponse("No Game Found", details);
		        log.info(ex.getLocalizedMessage());
		        return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
		    } 
		 
		 @ExceptionHandler(NoMoreCardsException.class)
		 public final ResponseEntity<Object> handleNoMoreCardsException(NoMoreCardsException ex) throws JsonProcessingException {
		        List<String> details = new ArrayList<>();
		        details.add(ex.getLocalizedMessage());
		        ErrorResponse error = new ErrorResponse("No more cards left", details);
		        log.info(ex.getLocalizedMessage());
		        return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
		    }  
		 
		 @ExceptionHandler(BadRequestException.class)
		 public final ResponseEntity<Object> handleBadRequestException(BadRequestException ex) throws JsonProcessingException {
		        List<String> details = new ArrayList<>();
		        details.add(ex.getLocalizedMessage());
		        ErrorResponse error = new ErrorResponse("Bad Request", details);
		        log.info(ex.getLocalizedMessage());
		        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
		    }   
	    

		@ExceptionHandler(Exception.class)
	    public final ResponseEntity<ErrorResponse> handleException(BadRequestException ex) throws JsonProcessingException {
	        List<String> details = new ArrayList<>();
	        details.add(ex.getLocalizedMessage());
	        ErrorResponse error = new ErrorResponse("Bad Request", details);
	        log.info(error.toString());
	        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	    }	

}
