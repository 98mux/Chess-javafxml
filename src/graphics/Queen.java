package graphics;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
	
	 
	public Queen(Point position, boolean isWhite) {
		 super(position,isWhite);
	 }
	 
	 public boolean isValidMove(Board board, Action action) {
		 
		 Point difference = Point.difference(action.getFromPosition(), action.getToPosition());
		 
		 Piece p = board.getPiece(action.getToPosition());
		 if(p != null && p.isWhite == this.isWhite)
			 return false;
		 
		if(Math.abs(((double)difference.getX()) / ((double)difference.getY())) != 1 && difference.getX() * difference.getY() != 0) {
				return false;
		}
			
			
		 if(board.allEmptyBetweenPoints(action.getFromPosition(), action.getToPosition())) {
			 return true;
		 }
		 return false;
	 }
	 
	
	 public boolean canMove(Board board, Point thisPosition) {
		 areArgumentsLegal(board,thisPosition);
		 
		 for(int x = -1; x <=1 ; x ++) {
			 for(int y = -1; y <= 1; y++) {
				 Point newpos = thisPosition.Translate(new Point(x,y));
				 
				 //Skip this one
				 if(x == 0 && y == 0)
					 continue;
				 if(!newpos.inBound()) {
					 continue;
				 }
				if(isValidMove(board,new Action(thisPosition,newpos))){
					return true;
				}
				
				 
			 }
		 }
		 return false;
	 }
	 
	 
 public void nextTurn() {
		 
	 }
	 public int value() { return 90;}
	 
	 @Override
		public Piece clone() {
			Queen q = new Queen(position,isWhite);
			q.position = position;
			return q;
		}
	 
}
