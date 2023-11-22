package com.github.adrianjesussilva.textimageforge.logic.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.adrianjesussilva.textimageforge.enumerator.TextAlign;

import lombok.Data;

/**
 * In charge of manipulating text thet will be placed in voucher image
 *  
 * @author Adrian Jesus Silva Simoes
 * @version 0.1
 * 
 */

@Data
public class TextForge {
    
    //Constants
    public static final Font DEFAULTFONT = new Font("Courier New",Font.PLAIN, 12);
    
    //Attributes
       //Content of each voucher line
    private String text;
    	//Client's signature
    private String signature;
        //X and Y axis coordinates where text will be placed 
    private float xAxis;
    private float yAxis;
    	//Text alignment in voucher  
    private TextAlign align;
    	//Text width and height in px
    private int height;
    private int width;
        //Text color and font
    private final Font font;
    private Color color;
    
    //Private methods
    /**
     * Method that calculates width and height of a voucher's given line
     * @param shadow (Graphics2D) 
     */
    private void sizeCalculator(Graphics2D shadow){
        FontMetrics fm = shadow.getFontMetrics(this.font);
        this.height = fm.getHeight();
        this.width = fm.stringWidth(this.text);
    }
    

    
    //Constructors
    /**
     * TextForge constructor
     * @param text (String) - Text content for a line in voucher
     * @param xAxis (int) - X Axis coordinate where text will be placed (left to right)
     * @param yAxis (int) - Y axis coordinate where text will be placed (top to bottom
     * @param shadow (Graphics2D)
     */
    public TextForge(String text, float xAxis, float yAxis, Graphics2D shadow){
        this.text = text;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.color = Color.BLACK;
        this.align = TextAlign.LEFT;
        this.font = DEFAULTFONT;
        this.sizeCalculator(shadow);
        
    }
    
    /**
     * TextForge constructor
     * @param text (String) - Text content for a line in voucher
     * @param xAxis (int) - X Axis coordinate where text will be placed (left to right)
     * @param yAxis (int) - Y axis coordinate where text will be placed (top to bottom
     * @param align (String) - Text alignment
     * @param font (Font) - Text font
     * @param color (Color) - Text color
     * @param shadow (Graphics2D) 
     */
    public TextForge(String text, TextAlign align,float xAxis, float yAxis, Font font, Color color, Graphics2D shadow){
        this.text = text;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.color = (color!=null)?color:Color.BLACK;
        this.align = (align!=null)? align: TextAlign.LEFT;
        this.font = (font!=null)?font:DEFAULTFONT;
        this.sizeCalculator(shadow);
    }
    
    /**
     * TextForge constructor
     * @param text (String) - Text content for a line in voucher
     * @param xAxis (int) - X Axis coordinate where text will be placed (left to right)
     * @param yAxis (int) - Y axis coordinate where text will be placed (top to bottom
     * @param align (String) - Text alignment
     * @param font (Font) - Text font
     * @param color (Color) - Text color
     * @param signature (String) - Client's signature
     * @param shadow (Graphics2D)
     */
    public TextForge(String text, TextAlign align,float xAxis, float yAxis, Font font, Color color, String signature, Graphics2D shadow){
        this.text = text;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.color = (color!=null)?color:Color.BLACK;
        this.align = (align!=null )? align: TextAlign.LEFT;
        this.signature = signature;
        this.font = (font!=null)?font:DEFAULTFONT;
        this.sizeCalculator(shadow);
    }
    
  //Public methods
    /**
     * Method that adjusts text alignment in voucher
     * @param width
     * @param margin 
     */
    public void ajustAlign(int width, int margin){
        if(this.align.equals(TextAlign.RIGHT)){this.xAxis = width - (this.width+margin);}
        else if(this.align.equals(TextAlign.CENTER)){this.xAxis = (width/2) - (this.width/2);}
        else{this.xAxis = margin;}
    }
    
    /**
     * Method that places the text in given graphic
     * @param sandBox (Graphics2D) 
     */
    public void drawText(Graphics2D sandBox){
        FontRenderContext frc = sandBox.getFontRenderContext();
        TextLayout tl = new TextLayout(text, font, frc);
        sandBox.setColor(color);
        Rectangle2D bounds = tl.getBounds();
        tl.draw(sandBox, this.xAxis - (float)bounds.getX(), this.yAxis - (float)bounds.getY());
    }
    
    /**
     * Method that places the text in given graphic at a given point in xAxis
     * @param sandBox (Graphics2D) 
     * @param width (float) 
     */
    public void drawTextAndSignature(Graphics2D sandBox, float width){
        try {
            FontRenderContext frc = sandBox.getFontRenderContext();
            TextLayout tl = new TextLayout(text, font, frc);
            sandBox.setColor(color);
            Rectangle2D bounds = tl.getBounds();
            tl.draw(sandBox, this.xAxis - (float)bounds.getX(), this.yAxis - (float)bounds.getY());
            Image firma = null;
            int ejeX = (int) (this.xAxis - bounds.getX() + this.width);
            int ejeY = (int) (this.yAxis+this.height);
            sandBox.drawImage(firma, ejeX, ejeY, null);
        } 
        catch (Exception ex) {
            Logger.getLogger(TextForge.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    

    
    
    
}
