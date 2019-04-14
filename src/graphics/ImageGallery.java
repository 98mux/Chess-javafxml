package graphics;

import javafx.scene.image.Image;

public class ImageGallery {
	public Image KingWhite;
	public Image KingBlack;
	
	public Image QueenWhite;
	public Image QueenBlack;
	
	public Image RookWhite;
	public Image RookBlack;
	
	public Image PawnWhite;
	public Image PawnBlack;
	
	public Image BishopWhite;
	public Image BishopBlack;
	
	public Image KnightWhite;
	public Image KnightBlack;
	
	public Image canMoveImage;
	
	int Id = 0;
	public int getCurrentID() {Id ++; return Id;}
	
	public ImageGallery() {
		String url = "";
		KingWhite = new Image(getClass().getResource(url + "KingW.png").toExternalForm());
		KingBlack = new Image(getClass().getResource(url + "KingB.png").toExternalForm());
		
		QueenWhite = new Image(getClass().getResource(url + "QueenW.png").toExternalForm());
		QueenBlack = new Image(getClass().getResource(url + "QueenB.png").toExternalForm());
		
		RookWhite = new Image(getClass().getResource(url + "RookW.png").toExternalForm());
		RookBlack = new Image(getClass().getResource(url + "RookB.png").toExternalForm());
		
		PawnWhite = new Image(getClass().getResource(url + "PawnW.png").toExternalForm());
		PawnBlack = new Image(getClass().getResource(url + "PawnB.png").toExternalForm());
		
		BishopWhite = new Image(getClass().getResource(url + "BishopW.png").toExternalForm());
		BishopBlack = new Image(getClass().getResource(url + "BishopB.png").toExternalForm());
		
		KnightWhite = new Image(getClass().getResource(url + "KnightW.png").toExternalForm());
		KnightBlack = new Image(getClass().getResource(url + "KnightB.png").toExternalForm());
		
		canMoveImage = new Image(getClass().getResource(url + "canMoveGraphics.png").toExternalForm());

	}

}
