package com.deck.cards.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.deck.cards.dto.GameResponse;
import com.deck.cards.dto.PlayerWithValue;
import com.deck.cards.model.Card;
import com.deck.cards.model.DeckOfCards;
import com.deck.cards.model.Suit;
import com.deck.cards.service.GameService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/game")
public class GameController {
	
	private GameService service;
	
	
	public GameController(GameService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "Create a Game", response = GameResponse.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "Successfully created a Game"),
	        @ApiResponse(code = 400, message = "Bad request"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 500, message = "Unexpetced error")
	})
	@PostMapping(value = "/create")
	@ResponseStatus(value = HttpStatus.CREATED)
	public GameResponse createGame() {
		GameResponse response = new GameResponse();
		response.setGameId(service.createGame());
		return response;
	}
	
	
	@ApiOperation(value = "Delete a Game")
	@ApiResponses(value = {
	        @ApiResponse(code = 204, message = "Successfully deleted a Game"),
	        @ApiResponse(code = 400, message = "No game with the provided Id")
	})
	@DeleteMapping(value ="{gameId}/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable("gameId") Long gameId) {
        service.deleteGame(gameId);
    }
	
	@GetMapping("/new/deck")
	@ResponseStatus(value = HttpStatus.OK)
	public DeckOfCards createDeck() {
		return service.createDeck();
	}
	
	@ApiOperation(value = "Add a deck to Game")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully added a deck to Game"),
	        @ApiResponse(code = 400, message = "No game with the provided Id")
	})
	@PostMapping(value = "/{gameId}/add/deck/")
	@ResponseStatus(value = HttpStatus.OK)
	public void addDeck(@PathVariable("gameId") Long gameId) {
		service.addDeckToGame(gameId);
	}
	
	
	@ApiOperation(value = "Deal a card to player in the Game", response = Card.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully dealt a card to player"),
	        @ApiResponse(code = 400, message = "No game with the provided Id")
	})
	@PostMapping("/{gameId}/player/{playerId}/deal")
	@ResponseStatus(value = HttpStatus.OK)
    public Card dealCard(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId) {
        return service.deal(gameId, playerId);
    }
	
	@ApiOperation(value = "Get all players in the Game", responseContainer="List")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "success"),
	        @ApiResponse(code = 400, message = "No game with the provided Id")
	})
	@GetMapping("/{gameId}/players")
	@ResponseStatus(value = HttpStatus.OK)
    public List<PlayerWithValue> getPlayersForGame(@PathVariable("gameId") Long gameId) {
        return service.getPlayersSortedForGame(gameId);
    }
	
	
	@ApiOperation(value = "Get all undealt cards in the game by suit", responseContainer="Map")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "success"),
	        @ApiResponse(code = 400, message = "No game with the provided Id")
	})
	@GetMapping("/{gameId}/unDealtCardsBySuit")
	@ResponseStatus(value = HttpStatus.OK)
    public Map<Suit, Long> getUnDealtCardsForGame(@PathVariable("gameId") Long gameId) {
        return service.getUnDealtCardsBySuit(gameId);
    }
	
	
	@ApiOperation(value = "Get all undealt cards in the game sorted", responseContainer="List")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "success"),
	        @ApiResponse(code = 400, message = "No game with the provided Id")
	})
	@GetMapping("/{gameId}/unDealtCardsSorted")
	@ResponseStatus(value = HttpStatus.OK)
    public List<Card> getSortedRemainingCardsForGame(@PathVariable("gameId") Long gameId) {
        return service.getSortedUndealtCards(gameId);
    }
	
	
	@ApiOperation(value = "Shuffle all the cards in the game")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully shuffled all the cards"),
	        @ApiResponse(code = 400, message = "No game with the provided Id")
	})
	@PostMapping(value = "{gameId}/shuffle")
	@ResponseStatus(value = HttpStatus.OK)
	void shuffle(@PathVariable("gameId") Long gameId) {
		service.shuffle(gameId);
	}
	
	
	
	

}
