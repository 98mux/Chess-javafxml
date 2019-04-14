package graphics;

import java.util.*;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class PieceImage {
	private ImageView image;
	private Piece piece;
	StackPane pane;
	AnchorPane boardGraphics;
	Board board;
	Chess game;
	
	boolean isBeingDragged = false;
	CanMoveGraphics canMoveGraphics;
	
	public PieceImage(Chess game,ImageGallery gallery,AnchorPane boardGraphics, Piece piece,CanMoveGraphics canMoveGraphics, Board board) {
		this.piece = piece;
		this.boardGraphics = boardGraphics;
		this.game = game;
		this.board = board;
		this.canMoveGraphics = canMoveGraphics;
		pane = new StackPane();
		image = new ImageView();
		
		installImage(gallery);
		updatePosition();
		
		boardGraphics.getChildren().add(pane);
		pane.getChildren().add(image);
	}
	private void installImage(ImageGallery gallery) {
		
		Class<?> c = piece.getClass();
		if(c == King.class) {
			if(piece.isWhite()) {
				image.setImage(gallery.KingWhite);
			}
			else {
				image.setImage(gallery.KingBlack);
			}
		}
		else if(c == Queen.class) {
			if(piece.isWhite()) {
				image.setImage(gallery.QueenWhite);
			}
			else {
				image.setImage(gallery.QueenBlack);
			}
		}
		else if(c == Rook.class) {
			if(piece.isWhite()) {
				image.setImage(gallery.RookWhite);
			}
			else {
				image.setImage(gallery.RookBlack);
			}
		}
		else if(c == Pawn.class) {
			if(piece.isWhite()) {
				image.setImage(gallery.PawnWhite);
			}
			else {
				image.setImage(gallery.PawnBlack);
			}
		}
		else if(c == Bishop.class) {
			if(piece.isWhite()) {
				image.setImage(gallery.BishopWhite);
			}
			else {
				image.setImage(gallery.BishopBlack);
			}
		}
		else if(c == Knight.class) {
			if(piece.isWhite()) {
				image.setImage(gallery.KnightWhite);
			}
			else {
				image.setImage(gallery.KnightBlack);
			}
		}

		pane.setOnMousePressed((MouseEvent e)->{
			List<Point> points = this.piece.getPossibleMoves(board);
			for(Point p : points) {
				canMoveGraphics.createMarker(p);
			}
			
		});
		pane.setOnMouseDragged((MouseEvent e)->{
			//Only to be called once
			if(isBeingDragged == false) {
				
				
			}
			pane.toFront();
			
			isBeingDragged = true;
			
			double x = (e.getSceneX()-boardGraphics.getLayoutX() - (image.getFitWidth() / 2) - 40);
			double y = (e.getSceneY()-boardGraphics.getLayoutY() - (image.getFitHeight()/ 2) - 40);
			pane.relocate(x,y);
			e.consume();
		});
	}
	
	public void mouseReleased() {
		canMoveGraphics.clear();
		if(isBeingDragged == true) {
			int x = (int)Math.abs((((pane.getLayoutX() + 30) / 60)));
			int y = (int)Math.abs((((pane.getLayoutY() - 30) / 60)-7));
			try {
				game.tryMove(piece.getPosition(), new Point(x,y));
			}
			catch(IllegalArgumentException e) {
				updatePosition();
			}
			updatePosition();
			isBeingDragged = false;
		}
	}
	
	public void updatePosition() {
		pane.relocate(piece.getPosition().getX() * 60, Math.abs((piece.getPosition().getY() * 60 )- 7*60));
	}
}
