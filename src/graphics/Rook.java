package graphics;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
	
	Point startPos = null;
	
	public boolean hasMoved() {
		return this.hasMoved;
	}
	
	public Rook(Point position, boolean isWhite) {
		 super(position,isWhite);
		 startPos = position.newPoint();
	 }
	
	public void Reset() {
		hasMoved = false;
	}
	
	 public boolean isValidMove(Board board, Action action) {
		 
		 Piece p = board.getPiece(action.getToPosition());
		 if(p != null && p.isWhite == this.isWhite)
			 return false;
		 
		 Point difference = Point.difference(action.getFromPosition(), action.getToPosition());
			if(difference.getX() * difference.getY() != 0) {
				return false;
			}
			
			
		 if(board.allEmptyBetweenPoints(action.getFromPosition(), action.getToPosition())) {
			 return true;
		 }
		 return false;
	 }
	 
	 
	 
	 
	 public boolean canMove(Board board, Point thisPosition) {
		 areArgumentsLegal(board,thisPosition);

		 if(isValidMove(board, new Action(thisPosition, thisPosition.Translate(0,1)))
				 ||
				 isValidMove(board, new Action(thisPosition, thisPosition.Translate(1,0)))
						 ||
						 isValidMove(board,new Action( thisPosition, thisPosition.Translate(0,-1)))
								 ||
								 isValidMove(board, new Action( thisPosition, thisPosition.Translate(-1,0)))){
			 return true;
		 }
		 return false;
	 }
		 
 public void nextTurn() {
		 if(!startPos.equals(position)) {
			 hasMoved = true;
		 }
	 }
	 public int value() { return 50;}

	@Override
	public Piece clone() {
		Rook r = new Rook(position,isWhite);
		r.hasMoved = hasMoved;
		r.startPos = startPos;
		r.position = position;
		return r;
	}


	
}
