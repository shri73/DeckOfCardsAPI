package com.deck.cards.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@Entity
@ApiModel(description = "Deck of Cards in the Game")
public class DeckOfCards {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deckId;
	
	/** represents all cards 1-52 in a deck*/
	@ApiModelProperty(notes = "A list of the cards - 52")
	@ElementCollection
	private List<Card> cards;
	
	
	public DeckOfCards(){
		this.cards = Arrays.stream(Suit.values())
		        .flatMap(suit -> Arrays.stream(FaceValue.values()).map(face -> new Card(suit, face)))
		        .collect(Collectors.toList());
	}
	

	
	
	

}
