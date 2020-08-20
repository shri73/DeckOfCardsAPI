package com.deck.cards.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ErrorResponse {
	
    //General error message about nature of error
    private String message;
 
    //Specific errors in API request processing
    private List<String> details;
	
	 @Override
	 public String toString() {
			StringBuilder builder = new StringBuilder();
	        builder.append("{\"message\" :");
	        builder.append(message);
	        builder.append(", \"details\" :");
	        builder.append(details);
	        builder.append("}");
	    return builder.toString();
			
		}


}
