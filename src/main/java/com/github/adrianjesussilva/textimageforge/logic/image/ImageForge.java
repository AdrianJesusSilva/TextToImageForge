package com.github.adrianjesussilva.textimageforge.logic.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;

import com.github.adrianjesussilva.textimageforge.enumerator.TextAlign;
import com.github.adrianjesussilva.textimageforge.logic.exception.InvalidTextForgeConfigException;
import com.github.adrianjesussilva.textimageforge.logic.text.TextForge;


/**
 * 
 * Forge implementation to generate an image with the given parameters and text
 * list
 * 
 * @author Adrian Jesus Simoes Silva
 * @author Ana Leticia Ibarra Luces
 *
 */


public class ImageForge {

	//Constants

	//Default margins, line spacing and image type
	private static final int DEFAULTMARGIN = 15;
	private static final int DEFAULTMARGINSUPINF = 15;
	private static final int DEFAULTLINESPACING = 2;
	private static final String DEFAULTIMAGETYPE = "png";

	// Attributes
	
	//Renders
	private BufferedImage beach;
	private Graphics2D sandbox;
	private Image image; 
	private BufferedImage signatureImg;
	
	//Defined sizes
	private final int margin;//Border margins
	private final int marginSupInf; //Superior and inferior margins for voucher
	private final int lineSpacing; //Spacing between lines
	private int voucherWidth; //Voucher width
	private int voucherHeight; //Voucher height
	
	//ArrayList containing lines from voucher
	private ArrayList<TextForge> voucherLines;
	
	//Voucher background
	private final String imageB64DataString; //whitebg 120 x 70 

	//Private methods
	
	/**
	 * Procedure that resizes the voucher's dimensions according to its content 
	 */
	private void resize(){
		this.voucherWidth = 0;
		this.voucherHeight = this.marginSupInf*2;
		
		//Adjust voucher width and height according to the content of each line
		for (TextForge line : this.voucherLines){
			//Adjust voucher width
			if(line.getWidth()>this.voucherWidth)
				this.voucherWidth = line.getWidth() + (margin*2);
			//Add line to height
			this.voucherHeight = this.voucherHeight + line.getHeight() + this.lineSpacing;
		}

		//Adjust voucher position according to its align
		for(TextForge line:this.voucherLines){line.ajustAlign(this.voucherWidth, this.margin);}
	}

	/**
	 * Procedure that renders the background image and places over it
	 * the defined content
	 * @param sandBox (Graphics2D) Sandbox in which all renders will be made 
	 */
	private void drawImageAndText(Graphics2D sandBox){
		//Byte decode of background image
		byte[] imageBytes = Base64.decodeBase64(imageB64DataString);

		//Load background as Image 
		this.image = new ImageIcon(imageBytes).getImage(); 

		//Create sandbox according to the calculated width and height
		sandBox.drawImage(image, 0, 0, voucherWidth, voucherHeight, null);

		//Draw each line
		for (TextForge linea : voucherLines){
			if(linea.getSignature()!=null){linea.drawTextAndSignature(sandBox, this.voucherWidth - this.margin);}
			else{linea.drawText(sandBox);}

		}
	}
	
	private void readSignature(String signature) throws InvalidTextForgeConfigException {
		//Decode B64 signature
		byte[] byteArray = Base64.decodeBase64(signature);
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		try {
			//Read signature into Image
			this.signatureImg = ImageIO.read(bais);
		} catch (IOException e) {
			throw new InvalidTextForgeConfigException("The given signature could not be read");
		}
	}

	
	//Constructor
	/**
	 *General constructor, sets default values of voucher 
	 * @throws InvalidTextForgeConfigException 
	 */
	public ImageForge(String signature) throws InvalidTextForgeConfigException{
		this.voucherWidth = 120; //Default width for white background
		this.voucherHeight = 70; //Default height for white background
		this.voucherLines = new ArrayList<TextForge>();
		this.margin = DEFAULTMARGIN;
		this.marginSupInf = DEFAULTMARGINSUPINF;
		this.lineSpacing = DEFAULTLINESPACING;
		//White background
		this.imageB64DataString = "/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAA8AAD/"+
				"4QMtaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49"+
				"Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRh"+
				"IHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29y"+
				"ZSA1LjMtYzAxMSA2Ni4xNDU2NjEsIDIwMTIvMDIvMDYtMTQ6NTY6MjcgICAgICAg"+
				"ICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8w"+
				"Mi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0"+
				"PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1s"+
				"bnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5z"+
				"OnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3Vy"+
				"Y2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1h"+
				"Y2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NTlCOUJFRjlDNjc3"+
				"MTFFM0JENDFENDAxREY3OTZFRUQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6"+
				"NTlCOUJFRkFDNjc3MTFFM0JENDFENDAxREY3OTZFRUQiPiA8eG1wTU06RGVyaXZl"+
				"ZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo1OUI5QkVGN0M2NzcxMUUz"+
				"QkQ0MUQ0MDFERjc5NkVFRCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo1OUI5"+
				"QkVGOEM2NzcxMUUzQkQ0MUQ0MDFERjc5NkVFRCIvPiA8L3JkZjpEZXNjcmlwdGlv"+
				"bj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pv/u"+
				"AA5BZG9iZQBkwAAAAAH/2wCEAAYEBAQFBAYFBQYJBgUGCQsIBgYICwwKCgsKCgwQ"+
				"DAwMDAwMEAwODxAPDgwTExQUExMcGxsbHB8fHx8fHx8fHx8BBwcHDQwNGBAQGBoV"+
				"ERUaHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8f"+
				"Hx8fH//AABEIAEYAeAMBEQACEQEDEQH/xABLAAEBAAAAAAAAAAAAAAAAAAAACAEB"+
				"AAAAAAAAAAAAAAAAAAAAABABAAAAAAAAAAAAAAAAAAAAABEBAAAAAAAAAAAAAAAA"+
				"AAAAAP/aAAwDAQACEQMRAD8AqkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH//Z";

		// Graphics init, used to calculate and render voucher
		this.beach = new BufferedImage(voucherWidth, voucherHeight, BufferedImage.TYPE_INT_RGB);
		sandbox = this.beach.createGraphics();
		sandbox.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Read and load signature 
		if(signature != null)
			 readSignature(signature);
	}

	/**
	 * Class constructor, sets given values 
	 * @param backgroundB64 (String)[Required] Background image in B64
	 * @param voucherWidth (int)[Required] Background image width in px
	 * @param voucherHeight (int) [Required] Background image height in px
	 * @param margin (int) [Optional] Voucher's left and right margin size in px
	 * @param marginSupInf (int) Top and bottom margin size in px
	 * @param lineSpacing (int) [Optional] Space between lines in px
	 * @param signature (String) [Optional] Client's signature in B64
	 * @throws Exception - If required values are not provided
	 */
	public ImageForge(String backgroundB64, int voucherWidth, int voucherHeight, int margin, int marginSupInf,int lineSpacing, String signature) throws Exception{
		if(backgroundB64!=null && !backgroundB64.equals("")){
			if(voucherWidth!=0 && voucherHeight != 0){
				//Set initial parameters
				this.imageB64DataString = backgroundB64;
				this.voucherWidth = voucherWidth;
				this.voucherHeight = voucherHeight;
				this.margin = (margin>0)?margin:DEFAULTMARGIN;
				this.marginSupInf = (marginSupInf>0)?marginSupInf:DEFAULTMARGINSUPINF;
				this.lineSpacing = (lineSpacing>0)?lineSpacing:DEFAULTLINESPACING;
				this.voucherLines = new ArrayList<>();

				// Graphics init, used to calculate and render voucher
				this.beach = new BufferedImage(this.voucherWidth,this.voucherHeight,BufferedImage.TYPE_INT_RGB);
				this.sandbox = this.beach.createGraphics();
				this.sandbox.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				//Read and load signature 
				if(signature != null)
					 readSignature(signature);
			}
			else{
				if(voucherWidth==0){throw new InvalidTextForgeConfigException("The defined background width must be greater than 0");}
				else if(voucherHeight==0){throw new InvalidTextForgeConfigException("The defined background height must be greater than 0");}
				else{throw new InvalidTextForgeConfigException("The defined background width and height values must be greater than 0");}
			}
		}
		else{throw new InvalidTextForgeConfigException("The defined background was not provided");}
	}

	//Public Methods
	
	/*Adding lines to voucher*/
	/**
	 * Method that adds line to voucher 
	 * @param line (String) - Line that will be added to voucher
	 * @param signature (String) - Client's signature in B64
	 * @param align (String) - Line's alignment 
	 * @param font (Font) - Text font
	 * @param color (Color) - Text color
	 */
	public void addLine(String line, String signature,TextAlign align,Font font, Color color){
		if(line!=null && !line.equals("")){
			if(this.voucherLines.isEmpty()){this.voucherLines.add(new TextForge(line, align, this.margin, this.marginSupInf, font, color, signature, sandbox));}
			else{
				float lastY = this.voucherLines.get(this.voucherLines.size()-1).getYAxis();
				lastY = lastY + this.voucherLines.get(this.voucherLines.size()-1).getHeight() + this.lineSpacing;
				this.voucherLines.add(new TextForge(line, align, this.margin, lastY, font, color, signature, sandbox));
			}
			this.resize();
		}
	}

	/**
	 * Method that adds line to voucher 
	 * @param line (String) - Line that will be added to voucher
	 */
	public void addLine(String line){

		if(line != null && !line.equals("")){
			if(this.voucherLines.isEmpty()){this.voucherLines.add(new TextForge(line, this.margin, this.marginSupInf+this.lineSpacing, this.sandbox));}
			else{
				float lastY = this.voucherLines.get(this.voucherLines.size()-1).getYAxis();
				lastY = lastY + this.voucherLines.get(this.voucherLines.size()-1).getHeight() + this.lineSpacing;
				this.voucherLines.add(new TextForge(line, this.margin, lastY, this.sandbox));
			}
			this.resize();
		}
	}

	/*Image rendering*/
	
	// Voucher rendering methods
	/**
	 * Method that renders voucher image and returns buffered image
	 * @return (BufferedImage) - Buffer that contains recently created voucher
	 */
	public BufferedImage forgeBufferedImage(){
		//Prepare sandbox for rendering
		this.beach = new BufferedImage(voucherWidth,voucherHeight, BufferedImage.TYPE_INT_RGB);
		sandbox = this.beach.createGraphics();
		sandbox.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//Voucher render
		this.drawImageAndText(sandbox);

		//Return voucher image
		return beach.getSubimage(0, 0, voucherWidth, voucherHeight);
	}

	/**
	 * Method that renders voucher image from buffered one
	 * @return (Image) - Rendered voucher image
	 */
	public Image forgeImage() {
		return Toolkit.getDefaultToolkit().createImage(forgeBufferedImage().getSource());
	}

	/**
	 * Method that returns voucher image in OutputStream
	 * @param imageType (String) Image type (PNG, JPG,...)
	 * @param os (OutputStream) 
	 * @return (OutputStream) Instance that contains image
	 * @throws IOException 
	 */
	public OutputStream forgeImage(String imageType, OutputStream os) throws IOException{
		ImageIO.write(this.forgeBufferedImage(), (imageType!=null && !imageType.equals("")?imageType:DEFAULTIMAGETYPE), os);
		return os;
	}

	/**
	 * Method that stores the generated voucher in the specified path
	 * @param path
	 * @param imageType
	 * @return (File)
	 * @throws IOException 
	 * @throws InvalidTextForgeConfigException 
	 */
	public File forgeImage(String path,String imageType) throws IOException, InvalidTextForgeConfigException{
		File voucherFile = new File(path);
		ImageOverlay overlay = new ImageOverlay();

		if (signatureImg != null) {
			signatureImg = overlay.resizeImage(signatureImg, (int) ((int)(this.voucherWidth-(margin*2))*0.75), (int)((this.voucherWidth-(margin*2))*0.75*signatureImg.getHeight())/signatureImg.getWidth());
			this.voucherHeight = this.voucherHeight + signatureImg.getHeight();
		}

		BufferedImage voucher = this.forgeBufferedImage(); 

		if(signatureImg != null) {
			BufferedImage overlayedImage = overlay.overlayImages(voucher, signatureImg, voucherHeight-signatureImg.getHeight());
			ImageIO.write(overlayedImage, (imageType!=null && !imageType.equals("")?imageType:DEFAULTIMAGETYPE), voucherFile);
		} else {
			ImageIO.write(voucher, (imageType!=null && !imageType.equals("")?imageType:DEFAULTIMAGETYPE), voucherFile);
		}
		
		return voucherFile;
	}

	/**
	 * Method that returns generated voucher in B64
	 * @param imageType (String) Image type (PNG, JPG,...)
	 * @return (String) Voucher image in B64 
	 * @throws IOException 
	 */
	public String forgeB64Image(String imageType) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(this.forgeBufferedImage(), (imageType!=null && !imageType.equals("")?imageType:DEFAULTIMAGETYPE), baos);
		baos.flush();
		byte[] imageBytes = baos.toByteArray();
		baos.close();
		return Base64.encodeBase64String(imageBytes);
	}


	/**
	 * Method that returns voucher image in OutputStream
	 * @param voucherB64Image (String) - Voucher image in B64 
	 * @param imageType (String) Image type (PNG, JPG,...)
	 * @param os (OutputStream) Stream where image will be placed
	 * @return (OutputStream)
	 * @throws IOException 
	 */
	public OutputStream forgeImage(String voucherB64Image, String imageType, OutputStream os) throws IOException{
		byte[] byteArray = Base64.decodeBase64(voucherB64Image);
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		BufferedImage image = ImageIO.read(bais);
		ImageIO.write(image, (imageType!=null && !imageType.equals("")?imageType:DEFAULTIMAGETYPE), os);
		return os;
	}

	/**
	 * Method that, given the voucher image in B64, saves it in a given path
	 * @param voucherB64Image (String) Voucher image in B64 
	 * @param path (String) 
	 * @param imageType (String) tImage type (PNG, JPG,...)
	 * @return (File) Image file
	 * @throws IOException 
	 */
	public File forgeImage(String voucherB64Image, String path, String imageType) throws IOException{
		byte[] byteArray = Base64.decodeBase64(voucherB64Image);
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		BufferedImage image = ImageIO.read(bais);
		File voucheFile = new File(path);
		ImageIO.write(image, (imageType!=null && !imageType.equals("")?imageType:DEFAULTIMAGETYPE), voucheFile);
		return voucheFile;
	}

}