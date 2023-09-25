package com.github.adrianjesussilva.textimageforge.logic.text;

import java.awt.Color;
import java.awt.Font;

import com.github.adrianjesussilva.textimageforge.enumerator.TextAling;

import lombok.Builder;
import lombok.Data;

/**
 * POJO that represents the text and the characteristics of the text
 * 
 * @author Adrian Jesus Simoes Silva
 *
 */
@Data
public class TextForge {
	
	// Attributes 
	private String text;
	private TextAling textAling;
	private Font font;
	private Color color;
	
	private Integer width;
	private Integer height;
	
	private Integer xAxis;
	private Integer yAxis;

	// Constructor
	@Builder
	public TextForge(String text, TextAling textAling, Font font, Color color) {
		super();
		this.text = text;
		this.textAling = textAling;
		this.font = font;
		this.color = color;
	}
	
}
