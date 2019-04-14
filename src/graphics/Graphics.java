package graphics;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Graphics {
	private List<PieceImage> pieceImages = new ArrayList<PieceImage>();
	Board board;
	AnchorPane boardGraphics;
	ImageGallery gallery;
	Chess game;
	CanMoveGraphics canMoveGraphics;
	public Graphics(Chess game,AnchorPane boardGraphics, Board board) {
		this.board = board;
		this.boardGraphics = boardGraphics;
		this.boardGraphics.setId("board");
		this.game = game;
		gallery = new ImageGallery();
		canMoveGraphics = new CanMoveGraphics(boardGraphics, gallery);
		
		boardGraphics.setOnMouseReleased((MouseEvent e)->{
			for(PieceImage i : pieceImages) {
				i.mouseReleased();
			}
			updateGraphics();
		});
		
	}
	
	public void updateGraphics() {
		pieceImages.clear();
		
		List<Node> nodesToRemove = new ArrayList<Node>();
		for(Node n : boardGraphics.getChildren()) {
			if(n.getId() == null) {
				//ChessPieces dont have ID
				nodesToRemove.add(n);
			}
		}
		for(Node n : nodesToRemove) {
			boardGraphics.getChildren().remove(n);
		}
		
		for(int x = 0; x < 8; x ++)
			for(int y = 0; y < 8; y ++) {
				Piece p = board.getPiece(new Point(x,y));
				if(p != null) {
					PieceImage pieceImage = new PieceImage(game,gallery, boardGraphics,p,canMoveGraphics,board);
					pieceImages.add(pieceImage);
				}
			}
	}
}
