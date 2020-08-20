package com.deck.cards.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@Getter
@ApiModel
/** Represents a value associated with a playing card. */
public enum FaceValue {
	ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);
	
	private final Integer value;

	FaceValue(final Integer faceValue) {
        this.value = faceValue;
    }

}
