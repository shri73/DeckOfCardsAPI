package com.deck.cards.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.deck.cards.dto.PlayerResponse;
import com.deck.cards.model.Card;
import com.deck.cards.service.PlayerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/player")
public class PlayerController {
	
	private PlayerService service;
	
	public PlayerController(PlayerService service) {
		
		this.service = service;
	}
	
	@ApiOperation(value = "Add a player in the game", response = PlayerResponse.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "Successfully created a player"),
	        @ApiResponse(code = 400, message = "Bad request"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 500, message = "Unexpected error")
	})
	@PostMapping(value = "{playerName}/addTo/{gameId}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public PlayerResponse addPlayerToGame(@PathVariable("playerName") String playerName, @PathVariable("gameId") Long gameId) {
		PlayerResponse response = new PlayerResponse();
		response.setPlayerId(service.addPlayer(playerName, gameId));
		return response;
	}
	
	
	@ApiOperation(value = "Remove a player in the game")
	@ApiResponses(value = {
	        @ApiResponse(code = 204, message = "Successfully deleted a player"),
	        @ApiResponse(code = 400, message = "Bad request")
	})
	@DeleteMapping("/{playerId}/remove/{gameId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removePlayer(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId) throws NoSuchFieldException {
        service.removePlayerFromGame(playerId, gameId);
    }
	
	@ApiOperation(value = "Get all cards from a player in the game")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "success"),
	        @ApiResponse(code = 400, message = "Bad request")
	})
	@GetMapping("/{playerId}/cards/{gameId}")
	@ResponseStatus(value = HttpStatus.OK)
    public List<Card> getPlayerCardsFromGame(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId) {
        return service.getPlayerCardsForGame(playerId, gameId);
    }

}
