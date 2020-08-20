package com.deck.cards.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@Getter
@ApiModel
/** Represents a suit associated with a playing card. */
public enum Suit {
	
	HEARTS(1),
    SPADES(2),
    CLUBS(3),
    DIAMONDS(4);

	private final Integer value;

    Suit(final Integer suit) {
        this.value = suit;
    }
    

}
