package graphics;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
	 boolean isWhite;
	 
	 protected Point position;
	 
	 
	 protected boolean hasMoved = false;
	 
	 public boolean getHasMoved() {
		 return hasMoved;
	 }
	 public void setHasMoved(boolean hasMoved) {
		 this.hasMoved = hasMoved;
	 }
	 
	 public Piece(Point position, boolean isWhite) {
		 this.position = position;
		 this.isWhite = isWhite;
	 }
	 
	 
	 
	 public abstract boolean isValidMove(Board board, Action action);
	 
	 public abstract boolean canMove(Board board, Point thisPosition);
	 
	 public List<Point> getPossibleMoves(Board board, Point thisPosition){
		 //Lazy way of doing this...
		 List<Point> possibleMoves = new ArrayList<Point>();
		 for(int x = 0; x < 8; x ++) 
		 for(int y = 0; y < 8; y ++){
			 Point p = new Point(x,y);
			 if(isValidMove(board, new Action(thisPosition,p,true))) {
				 possibleMoves.add(p);
			 }
		 }
		 return possibleMoves;
	 }
	 
	 public List<Point> getPossibleMoves(Board board){
		 return getPossibleMoves(board,position);
	 }
	 
	 
	 protected void areArgumentsLegal(Board board, Point position) {
		 Piece thisPiece = board.getPiece(position);
		 //Make sure the reference on this position is this
		 if(thisPiece != this) {
			 throw new IllegalArgumentException("This position must be the same position for the brick that is called");
		 }
	 }
	 
	 
	 public void setPosition(Point pos) {
		 position = pos;
	 }
	 public Point getPosition() {
		 return position;
	 }
	 
	 public abstract void nextTurn() ;
	 
	 public boolean isBlack() {
		 return !isWhite;
	 }
	 public boolean isWhite() {
		 return isWhite;
	 }
	 
	 public abstract Piece clone();
	 
	 public abstract int value();
	 
	 public String toString() {
		 return position.toString();
	 }
}
