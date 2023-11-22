package com.github.adrianjesussilva.textimageforge.logic.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.adrianjesussilva.textimageforge.logic.exception.InvalidTextForgeConfigException;

import lombok.NoArgsConstructor;

/**
 * In charge of image overlaying and resizing 
 * 
 * @author Ana Leticia Ibarra
 * @version 0.1
 * 
 */

@NoArgsConstructor
public class ImageOverlay {

    /**
     * Method that overlay Images
     * @param background (BufferedImage) - Background image
     * @param fgImage (BufferedImage) - Foreground image
     * @param bgImageHeight (int) - Background image height
     * @return BufferedImage - Foreground image over background image
     * @throws InvalidTextForgeConfigException 
     */
    public BufferedImage overlayImages(BufferedImage background,
            BufferedImage foreground, int bgImageHeight) throws InvalidTextForgeConfigException {
        /**
         * Check if foreground's width and height is greater than background's 
         * If so, image can not be overlaid.
         */
        if (foreground.getHeight() > background.getHeight()
                || foreground.getWidth() > background.getWidth()) {
        	throw new InvalidTextForgeConfigException("The defined foreground's dimensions are bigger than the background's dimensions");
        }
        
        Graphics2D g = background.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       
        // Draw background image at (0,0)
        g.drawImage(background, 0, 0, null);
        
        // Draw foreground image at (x,y)
        g.drawImage(foreground, background.getWidth()/2 - foreground.getWidth()/2, bgImageHeight-20, null);
        g.dispose();
        return background;
    }

    /**
     * Method that writes specified buffer image to file
     * @param image (BufferedImage) - Buffered image to save
     * @param path (String) - Path where image will be saved ("C:/image.jpg")
     * @param extension (String) - Image type ("jpg","gif","png")
     * @throws IOException 
     */
    public void writeImage(BufferedImage image, String path,
            String extension) throws IOException {
    	BufferedImage bi = image;
        File outputfile = new File(path);
        ImageIO.write(bi, extension, outputfile); 
    }
    
    
    /**
     * Method that resizes an image according to the specified width and height
     * @param image (BufferedImage) - Buffered image to resize
     * @param width (int) - Target width 
     * @param height (int) - Target height
     * @throws IOException 
     */
    public BufferedImage resizeImage(BufferedImage image, int width, int height) throws IOException {
    	BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        return resizedImage;
    }
}
