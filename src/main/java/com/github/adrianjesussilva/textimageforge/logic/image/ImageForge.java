package com.github.adrianjesussilva.textimageforge.logic.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64OutputStream;

import com.github.adrianjesussilva.textimageforge.enumerator.ImageEncoder;
import com.github.adrianjesussilva.textimageforge.enumerator.TextAlign;
import com.github.adrianjesussilva.textimageforge.logic.exception.InvalidTextForgeConfigException;
import com.github.adrianjesussilva.textimageforge.logic.text.TextForge;

import lombok.Builder;

/**
 * 
 * Forge implementation to generate de image with the given parameters and text
 * list
 * 
 * @author Adrian Jesus Simoes Silva
 *
 */
@Builder
public class ImageForge {

	// Constants
	private static final int DEFAULT_WIDTH = 120;
	private static final int DEFAULT_HEIGHT = 70;
	private static final int DEFAULT_MARGIN = 15;
	private static final int DEFAULT_LINE_SPACING = 2;

	// Attributes
	private Integer width;
	private Boolean dynamicWitdh;
	private Integer height;
	private Boolean dynamicHeight;
	private Integer superiorMargin;
	private Integer rightMargin;
	private Integer inferiorMargin;
	private Integer leftMargin;
	private Integer lineSpacing;
	private Color background;
	private Integer signatureWidth;
	private Integer signatureHeight;
	
	private List<TextForge> lines;	
	
	
	// Private Methods
	/**
	 * Procedure that checks the given values and correct then if need it 
	 */
	private void setDefaultsValues() {
		if(Objects.isNull(width) || width <= 0)
			width = DEFAULT_WIDTH;
		if(Objects.isNull(dynamicWitdh))
			dynamicWitdh = true;
		if(Objects.isNull(height))
			height = DEFAULT_HEIGHT;
		if(Objects.isNull(dynamicHeight))
			dynamicHeight = true;
		if(Objects.isNull(superiorMargin) || superiorMargin < 0)
			superiorMargin = DEFAULT_MARGIN;
		if(Objects.isNull(rightMargin) || rightMargin < 0)
			rightMargin = DEFAULT_MARGIN;
		if(Objects.isNull(rightMargin) || rightMargin < 0)
			rightMargin = DEFAULT_MARGIN;
		if(Objects.isNull(inferiorMargin) || inferiorMargin < 0)
			inferiorMargin = DEFAULT_MARGIN;
		if(Objects.isNull(leftMargin) || leftMargin < 0)
			leftMargin = DEFAULT_MARGIN;
		if(Objects.isNull(lineSpacing) || lineSpacing < 0)
			lineSpacing = DEFAULT_LINE_SPACING;
		if(Objects.isNull(background))
			background = Color.WHITE;
	}
	
	/**
	 * function that draw in the buffered image the given text lines configured 
	 * @return {@link BufferedImage} - the rendered image with the given text
	 */
	private BufferedImage getBufferedImage() {
		// set default values if any missing 
		setDefaultsValues();
		
		// Pre calculate the image size with given text if apply
		BufferedImage bufferedImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = bufferedImage.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		FontRenderContext fontRenderContext = graphics2d.getFontRenderContext();
		if(dynamicHeight)
			height = superiorMargin + inferiorMargin;
		for(TextForge line : lines) {
			LineMetrics metrics = line.getFont().getLineMetrics(line.getText(), fontRenderContext);
			float lineWidth = (float) line.getFont().getStringBounds(line.getText(), fontRenderContext).getWidth();
			line.setWidth((int) Math.ceil(lineWidth));
			if(dynamicWitdh && (lineWidth + leftMargin + rightMargin) > width)
				width = (int) Math.ceil(lineWidth + leftMargin + rightMargin);
			line.setHeight((int) Math.ceil(metrics.getHeight()));
			if(dynamicHeight)
				height += (int) Math.ceil(metrics.getHeight());
		}
		
		if(signatureWidth != null && signatureHeight!=null) {
			
			//Avoid oversizing small images that don't requiere a strech
			double safeZoneWidth = (this.width-(leftMargin + rightMargin))*0.5;
			if ((int) safeZoneWidth <= signatureWidth) {
				height +=  (int)(safeZoneWidth*signatureHeight)/(signatureWidth) + 40;
			}
			else {
				height +=  (int)(signatureHeight) + 40;
			}
		}
			
		
		// prepare the elements to draw
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics2d = bufferedImage.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setBackground(background);
		graphics2d.setColor(background);
		graphics2d.fillRect(0, 0, width, height);
		
		// set coordinates of the text with the given configuration
		int lastY = superiorMargin;
		for(TextForge line : lines ) {
			// set x coordinate base on the aling 
			switch (line.getTextAlign()) {
				case LEFT:
					line.setXAxis(leftMargin);
					break;
				case CENTER:
					line.setXAxis((width - line.getWidth())/2);
					break;
				case RIGHT:
					line.setXAxis(width - rightMargin - line.getWidth());
			}
			
			// set y coordinate
			line.setYAxis(lastY);
			lastY += lineSpacing + line.getHeight();
			
			// draw the text
			graphics2d.setPaint(line.getColor());
			graphics2d.setFont(line.getFont());
			graphics2d.drawString(line.getText(), line.getXAxis(), line.getYAxis());
		}
		graphics2d.dispose();
		
		// return the image
		return bufferedImage;
	}
	
	/**
	 * Method that renders given signature in buffered image
	 * @return {@link BufferedImage} - the rendered image with the given signature
	 */
	private BufferedImage readSignature(String signature) throws InvalidTextForgeConfigException {
		//Decode B64 signature
		byte[] byteArray = Base64.decodeBase64(signature);
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		try {
			//Read signature into Image
			BufferedImage signatureImg = ImageIO.read(bais);
			signatureWidth = signatureImg.getWidth();
			signatureHeight = signatureImg.getHeight();
			return signatureImg;
		} catch (IOException e) {
			throw new InvalidTextForgeConfigException("The given signature could not be read");
		}
	}
	
	// Public Methods
	/**
	 * procedure to add lines to text to be draw in the image
	 * @param line {@link TextForge} - line of text to draw in the generate image 
	 * @throws InvalidTextForgeConfigException - in case of bad definition of the TextForge 
	 */
	public void addLine(TextForge line) throws InvalidTextForgeConfigException {
		if(Objects.nonNull(line)) {
			// Validate that the info is complete
			if(Objects.isNull(line.getTextAlign()))
				throw new InvalidTextForgeConfigException("a text aling must be defined");
			if(Objects.isNull(line.getFont()))
				throw new InvalidTextForgeConfigException("A font is required for the line");
			if(Objects.isNull(line.getColor()))
				throw new InvalidTextForgeConfigException("the text require a color definition");
			
			// add line to the collection
			if(Objects.isNull(lines))
				lines = new ArrayList<TextForge>();
			lines.add(line);
		}
	}
	
	/**
	 * procedure to add lines to text to be draw in the image
	 * @param line {@link String} - text to draw in the generate image 
	 * @throws InvalidTextForgeConfigException - in case of bad definition of the TextForge 
	 */
	public void addLine(String line) throws InvalidTextForgeConfigException {
		addLine(TextForge.builder().text(line).textAlign(TextAlign.LEFT).font(new Font(Font.MONOSPACED, Font.PLAIN, 12)).color(Color.BLACK).build());
	}

	/**
	 * function that generate the image with the loaded text 
	 * @param encoder {@link ImageEncoder} - the valid encoders for the image 
	 * @return - byte array with the encoded image
	 * @throws IOException - in case that can not encode the image
	 */
	public byte[] forgeImage(ImageEncoder encoder) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(this.getBufferedImage(), encoder.name(), output);
		output.flush();
		byte[] image = output.toByteArray();
		output.close();
		return image;
	}
	
	public BufferedImage forgeImage(ImageEncoder imageType, String signature) throws InvalidTextForgeConfigException, IOException{
		if(signature != null) {
			ImageOverlay overlay = new ImageOverlay();

			BufferedImage signatureImg = readSignature(signature);

			BufferedImage voucher = getBufferedImage();
			
			//Avoid oversizing small images that don't requiere a strech
			double safeZoneWidth = (this.width-(leftMargin + rightMargin))*0.5;
			
			if ((int) safeZoneWidth <= signatureImg.getWidth()) {
				signatureImg = overlay.resizeImage(signatureImg, (int) (safeZoneWidth), (int)(safeZoneWidth*signatureImg.getHeight())/signatureImg.getWidth());
			}
			
			this.height = this.height + signatureImg.getHeight();
			
			BufferedImage overlayedImage = overlay.overlayImages(voucher, signatureImg, height -(2*signatureImg.getHeight()));
			
			return overlayedImage;
		} else {
			return getBufferedImage();
		}
		
		
	}
	
	public File forgeImage(String path,ImageEncoder imageType, String signature) throws IOException, InvalidTextForgeConfigException{
		File voucherFile = new File(path);
		
		ImageIO.write(forgeImage(imageType, signature), (imageType!=null?imageType.name():ImageEncoder.png.name()), voucherFile);
		
		return voucherFile;
	}
	
	public String forgeImageB64(ImageEncoder imageType, String signature) throws IOException, InvalidTextForgeConfigException {
		BufferedImage img = forgeImage(imageType, signature);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStream b64 = new Base64OutputStream(os);
		ImageIO.write(img, "png", b64);
		return os.toString("UTF-8");
	}
	
}
