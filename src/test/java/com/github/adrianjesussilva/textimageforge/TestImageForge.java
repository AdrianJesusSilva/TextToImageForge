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
		assertTrue(hash.equals("5518824470C13880F598552DC38581AF"));
		
		// mostrar imagen
//		final byte[] showImage = image;
//		assertDoesNotThrow(() -> showImage(showImage), "failed to show the image");
		
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
		assertTrue(hash.equals("5B4B66CD4708551607828FFF47FBF669"));
		
		// mostrar imagen
//		final byte[] showImage = image;
//		assertDoesNotThrow(() -> showImage(showImage), "failed to show the image");
		
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
		assertTrue(hash.equals("A181797BE705FBFE494E919AB621BEE3"));
		
		// mostrar imagen
//		final byte[] showImage = image;
//		assertDoesNotThrow(() -> showImage(showImage), "failed to show the image");
		
		log.info("Ending Dynamic Size Image");
	}

}
