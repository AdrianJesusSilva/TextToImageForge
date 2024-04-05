package com.github.adrianjesussilva.textimageforge;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.github.adrianjesussilva.textimageforge.enumerator.ImageEncoder;
import com.github.adrianjesussilva.textimageforge.enumerator.TextAlign;
import com.github.adrianjesussilva.textimageforge.logic.exception.InvalidTextForgeConfigException;
import com.github.adrianjesussilva.textimageforge.logic.image.ImageForge;
import com.github.adrianjesussilva.textimageforge.logic.text.TextForge;

import lombok.extern.log4j.Log4j2;

/**
 * Test case of the image forge 
 * 
 * by the nature of the porpuse of the implementation, the test case should validate manually until is implemented a computer vision checker
 * 
 * @author Adrian Jesus Simoes Silva 
 *
 */
@Log4j2
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestImageForge {

	// private methods 
	private void showImage(byte[] image) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
		ImageIcon icon = new ImageIcon(bufferedImage);
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(500, 500);
		JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private String calculateMD5(byte[] image) throws NoSuchAlgorithmException {
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(image);
		return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
		
	}
	
	/**
	 * Test to validate all the check of the raw text forge object
	 */
	@Test
	@DisplayName("Test 00 Validation test")
	void test00ValidationTest () {
		log.info("Starting validation of text forge adding text");
		// instance of the image forge 
		ImageForge iForge = ImageForge.builder().build();

		// Validation of the config of the text forge 
		log.info("Validation of the text aling");
		InvalidTextForgeConfigException exception = assertThrows(InvalidTextForgeConfigException.class, () -> iForge.addLine(TextForge.builder().text("test00ValidationTest").build()), "It should produce error");
		assertTrue(exception.getMessage().contains("a text aling must be defined"));
		
		log.info("Validation of the font");
		exception = assertThrows(InvalidTextForgeConfigException.class, () -> iForge.addLine(TextForge.builder().text("test00ValidationTest").textAlign(TextAlign.LEFT).build()), "It should produce error");
		assertTrue(exception.getMessage().contains("A font is required for the line"));
		
		log.info("Validation of the color");
		exception = assertThrows(InvalidTextForgeConfigException.class, () -> iForge.addLine(TextForge.builder().text("test00ValidationTest").textAlign(TextAlign.LEFT).font(new Font(Font.MONOSPACED, Font.PLAIN, 12)).build()), "It should produce error");
		assertTrue(exception.getMessage().contains("the text require a color definition"));
		
		log.info("Ending validation of text forge adding text");
	}
	
	@Test
	@DisplayName("Test 01 Dynamic Size Image")
	void test01DynamicSizeImage () {
		log.info("Starting Dynamic Size Image");
		
		ImageForge iForge = ImageForge.builder().build();
		
		assertDoesNotThrow(() -> iForge.addLine("----------------------------------------"), "It should not produce a exception");
		assertDoesNotThrow(() -> iForge.addLine("Test 01 Dynamic Size Image"), "It should not produce a exception");
		assertDoesNotThrow(() -> iForge.addLine("----------------------------------------"), "It should not produce a exception");
		
		byte[] image = null;
		try {
			image = iForge.forgeImage(ImageEncoder.jpg);
		} catch (IOException e) {
			fail ("it should not produce a exception");
		}
		assertNotNull(image);
		
		// TODO -- implementar la validacion automatica o manual de la imagen 
		// verificar que el hash concuerda
		String hash = null;	
		try {	
			hash = this.calculateMD5(image);	
		} catch (NoSuchAlgorithmException e) {	
			fail ("it should not produce a exception");	
		}	
		assertNotNull(hash);
		assertTrue(hash.equals("785CA42D77F2C8C39AA37F0A741B2FFE"));
		
		log.info("Ending Dynamic Size Image");
	}
	
	@Test
	@DisplayName("Test 02 Static Size Image")
	void test02StaticSizeImage () {
		log.info("Starting Static Size Image");
		
		ImageForge iForge = ImageForge.builder()
				.width(150)
				.dynamicWitdh(false)
				.height(75)
				.dynamicHeight(false)
				.superiorMargin(5)
				.rightMargin(10)
				.inferiorMargin(5)
				.leftMargin(10)
				.lineSpacing(2)
				.background(Color.GREEN)
				.build();
		
		assertDoesNotThrow(() -> iForge.addLine("----------------------------------------"), "It should not produce a exception");
		assertDoesNotThrow(() -> iForge.addLine("Test 02 Static Size Image"), "It should not produce a exception");
		assertDoesNotThrow(() -> iForge.addLine("----------------------------------------"), "It should not produce a exception");
		
		byte[] image = null;
		try {
			image = iForge.forgeImage(ImageEncoder.jpg);
		} catch (IOException e) {
			fail ("it should not produce a exception");
		}
		assertNotNull(image);
		
		// TODO -- implementar la validacion automatica o manual de la imagen 
		// verificar que el hash concuerda
		String hash = null;	
		try {	
			hash = this.calculateMD5(image);	
		} catch (NoSuchAlgorithmException e) {	
			fail ("it should not produce a exception");	
		}	
		assertNotNull(hash);
		assertTrue(hash.equals("12FC9B63A4B01B3656A62E8629FF111D"));
		
		log.info("Ending Static Size Image");
	}
	
	@Test
	@DisplayName("Test 03 Text Align")
	void test03TextAlign () {
log.info("Starting Dynamic Size Image");
		
		ImageForge iForge = ImageForge.builder().build();
		
		assertDoesNotThrow(() -> iForge.addLine("----------------------------------------"), "It should not produce a exception");
		assertDoesNotThrow(() -> iForge.addLine(TextForge.builder().text("Test 03 Text Align Left").textAlign(TextAlign.LEFT).font(new Font(Font.MONOSPACED, Font.PLAIN, 12)).color(Color.BLACK).build()), "It should not produce a exception");
		assertDoesNotThrow(() -> iForge.addLine(TextForge.builder().text("Test 03 Text Align Center").textAlign(TextAlign.CENTER).font(new Font(Font.MONOSPACED, Font.PLAIN, 12)).color(Color.BLACK).build()), "It should not produce a exception");
		assertDoesNotThrow(() -> iForge.addLine(TextForge.builder().text("Test 03 Text Align Right").textAlign(TextAlign.RIGHT).font(new Font(Font.MONOSPACED, Font.PLAIN, 12)).color(Color.BLACK).build()), "It should not produce a exception");
		assertDoesNotThrow(() -> iForge.addLine("----------------------------------------"), "It should not produce a exception");
		
		byte[] image = null;
		try {
			image = iForge.forgeImage(ImageEncoder.jpg);
		} catch (IOException e) {
			fail ("it should not produce a exception");
		}
		assertNotNull(image);
		
		// TODO -- implementar la validacion automatica o manual de la imagen 
		// verificar que el hash concuerda
		String hash = null;	
		try {	
			hash = this.calculateMD5(image);	
		} catch (NoSuchAlgorithmException e) {	
			fail ("it should not produce a exception");	
		}	
		assertNotNull(hash);
		assertTrue(hash.equals("2338425FCA976D3A8153E21BC7BA6ECF"));

		
		log.info("Ending Dynamic Size Image");
	}
	
	@Test
	@DisplayName("Test 04 Dynamic Size Image with Signature")
	void test04DynamicSizeImage () {
		
		int voucherHeight = 70;
		int voucherWidth = 120;
		
		//Vertical image 20 x 90
		String signature = "iVBORw0KGgoAAAANSUhEUgAAABQAAABaCAIAAAA3ueFGAAAAAXNSR0IArs4c6QAAAANzQklUCAgI2+FP4AAAAvxJREFUWIXtl7tS3DAUhn+Z9GiTmsFUSRNmlTegzZICymQnyVDzCJ5J8gA8AlTUa2YYSqioodra+wKJvH3ik0JrcSzrYjZDKv5qJevzOT4XSQsarKIowFQURYZ/0DP8DD/D/xkmovVhIYT9PZ/P+eRoNHqE2/f399YdIYRSCsndp2kaItJaO440TZO2bABj1oZAKSWEGOr2zc0NH+7u7mLtVG1tba0PG6XhSKqHBmw+n/NQG71IwgDKsizLks+MRqOVV3FprTc3N7kjUkqtNRGl4ePjY8eR2WxmHqXhPM85eXBwYB8l4KqqHLPGYaNgtInIeMgnlVJSSjsMwiYxWmueoclkwtc8cZFE9JRwvyQfAUc+OAYbbLlcWvt9LxKpury8tC8iolU/cAshORUCoKoqviAGHx4ecpJXdQLuV3VZlh7Y7MyOTk5OOJnneX9N5g0jgNvbW/5oOp16ohpy23SPhe/u7vpr/LA9loyklN5l/jxfX1/z4d7enneZCxMRgLquBTLRPn3z9vUf/E7D8U5w5N+3hRCEJgn7v5mizZSAByqjtmPWgUO92rWQZT4f/W7bi0/Ccn+qLMuLiws+81K+El4zTsU5ZyIAeyama9vpRACz2czbsx5YKcXJ/u4RhPu7x2KxiMCZ/XJ0z0QhxHg83t7eTkfb5PlX/RNtvgk0+fA+QuIJzypqa5YCxduBnSK1w1DxZp23NgLtSECI8KVjBYfeGnK1A/PB1dUVH7rHmteCrRDHC1shofJ8sGzudXbIKyQYMPvL/Iuw2t/fT/js5lngocJEk9xAPcmIbElOCjywWdGIpulZdt6buNDE1YUJov3oDWzYHTNR23Vdn5+fh9YlUnV2drZYLPiD4I7pwNT+VbTa2dk5OjqKkyvY7URg+vETv5TH4LWVgUXLiABkg474DMDp6akTrSE+P1jmU3meD4kWAFRV5dgpiiKy0Xf6+ce378uu5YE+A50uXPkcOhP7wtfPXywspfReE4PwUtfvxgqAUsq5iyf1FytwXKrys4/uAAAAAElFTkSuQmCC";		
		
		int signatureOriginalHeight = 90;
		int signatureTopPadding = 40;
		
		ImageForge iForge = ImageForge.builder()
				.height(voucherHeight)
				.width(voucherWidth)
				.superiorMargin(15)
				.leftMargin(15)
				.inferiorMargin(15)
				.rightMargin(15)
				.lineSpacing(2)
				.build();
		
		assertDoesNotThrow(() -> iForge.addLine("   TEST 04"), "It should not produce a exception");
		
		BufferedImage image = null;
		try {
			image = iForge.forgeImage(ImageEncoder.jpg, signature);
		} catch (IOException | InvalidTextForgeConfigException e) {
			fail ("it should not produce a exception");
		}
		assertNotNull(image);
		
		//Signature should not be resized if signature width is less than voucher width 
		Assertions.assertTrue(image.getHeight() <= voucherHeight + signatureOriginalHeight + signatureTopPadding, "The resulting image has height greater than expected.");
		
		//Width should not change
		Assertions.assertEquals(voucherWidth, image.getWidth());
		
		log.info("Ending Dynamic Size Image");
		
	}

}
