package com.deck.cards.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.deck.cards.exception.BadRequestException;
import com.google.common.collect.ImmutableList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@Entity
@ApiModel(description = "Game")
public class Game {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gameId;
	
	@ApiModelProperty(notes = "List of decks in current game")
	@Fetch(FetchMode.JOIN)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	/** Maintain a list of decks for each game */
    private List<DeckOfCards> listOfDecks = new ArrayList<>();

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ApiModelProperty(notes = "List of players in current game")
    /** All players participating in the game */
	private List<Player> players;
    
    @Column(name="CURRENT_POSITION")
    /** Index of the top card, on the undealt deck */
	private Integer currentCard = 0;  
	
    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "List of all the cards in current game", required = true)
    /** All cards of each deck are in current list of cards*/
	private List<Card> currentListOfCards;
	
	public Game(){
		this.listOfDecks = new ArrayList<>();
		this.currentListOfCards = new ArrayList<>();
		this.players = new ArrayList<>();
	}
	
	/** return an immutable list of the decks.*/
	public List<DeckOfCards> getListOfDecks() {
		return ImmutableList.copyOf(listOfDecks);
	}

	public void setListOfDecks(List<DeckOfCards> listOfDecks) {
		this.listOfDecks = listOfDecks;
	}
	
	/** to add a deck in game, all currents from the new deck to be added to 
	current list of cards, which will used for dealing*/
	public void addDeck(DeckOfCards deck) {
		if(deck != null) {
			listOfDecks.add(deck);
			currentListOfCards.addAll(deck.getCards());
		}
		else {
			throw new BadRequestException("Cannot add a null deck");
		}
		
	}
	
	
	
    
   


}
