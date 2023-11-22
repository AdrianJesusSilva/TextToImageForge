package com.github.adrianjesussilva.textimageforge.logic.text;

import java.awt.Color;
import java.awt.Font;

import com.github.adrianjesussilva.textimageforge.enumerator.TextAlign;

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
	private TextAlign textAlign;
	private Font font;
	private Color color;
	
	private Integer width;
	private Integer height;
	
	private Integer xAxis;
	private Integer yAxis;

	// Constructor
	@Builder
	public TextForge(String text, TextAlign textAlign, Font font, Color color) {
		super();
		this.text = text;
		this.textAlign = textAlign;
		this.font = font;
		this.color = color;
	}
	
}
