package com.github.adrianjesussilva.textimageforge.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumerator that list the possibles aligns for the text in the image, relative to the margin
 * 
 * @author Adrian Jesus Simoes Silva 
 * @author Ana Leticia Ibarra
 *
 */

@AllArgsConstructor
public enum TextAlign {

	LEFT("left"),
	CENTER("center"),
	RIGHT("right");

	// Atributos
	@Getter private final String position;

}
