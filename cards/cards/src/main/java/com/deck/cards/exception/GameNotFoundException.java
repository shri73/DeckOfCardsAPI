package com.deck.cards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) 
public class GameNotFoundException extends RuntimeException {
	
private static final long serialVersionUID = 1L;
	
	public GameNotFoundException (Long gameId) {
		super("Cannot find a game with ID: " + gameId);
	}

}
