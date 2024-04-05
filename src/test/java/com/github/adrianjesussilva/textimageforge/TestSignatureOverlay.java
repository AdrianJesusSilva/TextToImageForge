package com.github.adrianjesussilva.textimageforge;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

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
 * Unit tests of signature overlaying in vouchers
 * 
 * @author Ana Leticia Ibarra
 *
 */
@Log4j2
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestSignatureOverlay {

	private static final Font FONT = new Font("Courier Prime",Font.PLAIN, 12); 

	/**
	 * Method that generates voucher from given text
	 * @param text (String) Text that may contain whitespaces
	 * @return (String) Text without whitespaces
	 */
	public void voucherImage(String fileName, String [] voucherLines, String signature) throws IOException {
		String tmpDirName = System.getProperty("java.io.tmpdir") + File.separator + "vpos-server";
		File tmpDir = new File(tmpDirName);
		log.info("Temp directory: " + tmpDir);
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
			log.info("Temp directory created at " + tmpDir);
		}

		// Try to create voucher
		log.info("Voucher is being built");
		ImageForge forge;
		try {
			forge = ImageForge.builder().build();

			for (String line : voucherLines) {

				if (line.startsWith(" ", 1)) {
					line = clearWhiteSpace(line);
					forge.addLine(TextForge.builder().text(line + "    ").font(FONT).color(Color.BLACK).textAlign(TextAlign.CENTER).build());
				} else if (line.equals("")) {
					forge.addLine(TextForge.builder().text(" ").textAlign(TextAlign.CENTER).font(FONT).color(Color.BLACK).build());
				} else {
					forge.addLine(TextForge.builder().text(line + "    ").textAlign(TextAlign.CENTER).font(FONT).color(Color.BLACK).build());
				}
			}

			// Save voucher in file
			String filePath = tmpDirName + File.separator + fileName + ".png";
			
			// Create and save image
			log.info("Creating and saving generated voucher " + fileName);

			forge.forgeImage(filePath, ImageEncoder.png, (signature!= null && !signature.isEmpty()? signature: null));

		} catch (InvalidTextForgeConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Method that clears whitespaces in text
	 * @param text (String) Text that may contain whitespaces
	 * @return (String) Text without whitespaces
	 */
	private static String clearWhiteSpace(String text){
		if(!text.equals("")){
			StringBuilder sb = new StringBuilder(text);

			int end = 0;
			for(end=0; end<text.length(); end++){
				if(text.charAt(end)!=' '){break;}
			}

			sb.delete(0, end);

			return sb.toString();
		}

		return text;

	}
	/**
	 * Test to validate image generation with signature
	 */
	@Test
	@DisplayName("Test 00 Validation test")
	void test00ValidationTest () {
		log.info("Starting validation of voucher generation with signature");
		String fileName = "voucher-signature";
		String signature = "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";
		String [] voucherLines={"","        VOUCHER 0 "," ----------------------------------------", "this is a voucher"};
        assertDoesNotThrow(() -> voucherImage(fileName,voucherLines , signature));
		log.info("Ending validation of voucher generation");
	}

	/**
	 * Test to validate image generation without signature
	 */
	@Test
	@DisplayName("Test 01 Validation test")
	void test01ValidationTest () {
		log.info("Starting validation of voucher generation");
		String fileName = "voucher";
		String [] voucherLines={"","        VOUCHER 1 "," ", "this is a voucher"};
		assertDoesNotThrow(() -> voucherImage(fileName,voucherLines , null));
		log.info("Ending validation of voucher generation");
	}
	
	/**
	 * Test to validate image generation with signature smaller than voucher width
	 */
	@Test
	@DisplayName("Test 02 Validation test with small signature")
	void test02ValidationTest () {
		log.info("Starting validation of voucher generation with small signature");
		String fileName = "voucher-small-signature";
		String signature = "iVBORw0KGgoAAAANSUhEUgAAABQAAABaCAIAAAA3ueFGAAAAAXNSR0IArs4c6QAAAANzQklUCAgI2+FP4AAAAvxJREFUWIXtl7tS3DAUhn+Z9GiTmsFUSRNmlTegzZICymQnyVDzCJ5J8gA8AlTUa2YYSqioodra+wKJvH3ik0JrcSzrYjZDKv5qJevzOT4XSQsarKIowFQURYZ/0DP8DD/D/xkmovVhIYT9PZ/P+eRoNHqE2/f399YdIYRSCsndp2kaItJaO440TZO2bABj1oZAKSWEGOr2zc0NH+7u7mLtVG1tba0PG6XhSKqHBmw+n/NQG71IwgDKsizLks+MRqOVV3FprTc3N7kjUkqtNRGl4ePjY8eR2WxmHqXhPM85eXBwYB8l4KqqHLPGYaNgtInIeMgnlVJSSjsMwiYxWmueoclkwtc8cZFE9JRwvyQfAUc+OAYbbLlcWvt9LxKpury8tC8iolU/cAshORUCoKoqviAGHx4ecpJXdQLuV3VZlh7Y7MyOTk5OOJnneX9N5g0jgNvbW/5oOp16ohpy23SPhe/u7vpr/LA9loyklN5l/jxfX1/z4d7enneZCxMRgLquBTLRPn3z9vUf/E7D8U5w5N+3hRCEJgn7v5mizZSAByqjtmPWgUO92rWQZT4f/W7bi0/Ccn+qLMuLiws+81K+El4zTsU5ZyIAeyama9vpRACz2czbsx5YKcXJ/u4RhPu7x2KxiMCZ/XJ0z0QhxHg83t7eTkfb5PlX/RNtvgk0+fA+QuIJzypqa5YCxduBnSK1w1DxZp23NgLtSECI8KVjBYfeGnK1A/PB1dUVH7rHmteCrRDHC1shofJ8sGzudXbIKyQYMPvL/Iuw2t/fT/js5lngocJEk9xAPcmIbElOCjywWdGIpulZdt6buNDE1YUJov3oDWzYHTNR23Vdn5+fh9YlUnV2drZYLPiD4I7pwNT+VbTa2dk5OjqKkyvY7URg+vETv5TH4LWVgUXLiABkg474DMDp6akTrSE+P1jmU3meD4kWAFRV5dgpiiKy0Xf6+ce378uu5YE+A50uXPkcOhP7wtfPXywspfReE4PwUtfvxgqAUsq5iyf1FytwXKrys4/uAAAAAElFTkSuQmCC";
		String [] voucherLines={"","        VOUCHER 2 "," ", "this is a voucher"};
		assertDoesNotThrow(() -> voucherImage(fileName,voucherLines , signature));
		
		log.info("Ending validation of voucher generation");
	}

}
