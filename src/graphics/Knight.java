package graphics;

public class Knight extends Piece{
	
	 
	public Knight(Point position, boolean isWhite) {
		 super(position,isWhite);
	 }
	 
	 public boolean isValidMove(Board board, Action action) {
		 areArgumentsLegal(board,action.getFromPosition());
		 Point difference = Point.difference(action.getFromPosition(), action.getToPosition());
		 
		 Piece p = board.getPiece(action.getToPosition());
		 if(p != null && p.isWhite == this.isWhite)
			 return false;
		 
		 if((Math.abs(difference.getX()) + Math.abs(difference.getY()) == 3) && (difference.getX() != 0 && difference.getY() != 0))
			 return true;
		 
		 return false;
	 }
	 
	 public boolean canMove(Board board, Point thisPosition) {
		 areArgumentsLegal(board,thisPosition);
		 if(isValidMove(board, new Action(thisPosition, thisPosition.Translate(1,2)))
				 ||
				 isValidMove(board, new Action(thisPosition, thisPosition.Translate(-1,2)))
						 ||
						 isValidMove(board, new Action(thisPosition, thisPosition.Translate(-1,-2)))
								 ||
								 isValidMove(board, new Action(thisPosition, thisPosition.Translate(1,-2)))
								 
								 || isValidMove(board, new Action(thisPosition, thisPosition.Translate(2,1)))
								 ||
								 isValidMove(board, new Action(thisPosition, thisPosition.Translate(-2,1)))
										 ||
										 isValidMove(board, new Action(thisPosition, thisPosition.Translate(-2,-1)))
												 ||
												 isValidMove(board, new Action(thisPosition, thisPosition.Translate(2,-1)))) {
			 return true;
		 }
		 
		 
		 
		 return false;
	 }
	 
 public void nextTurn() {
		 
	 }
	 public int value() { return 30;}
	 
	 @Override
		public Piece clone() {
			Knight k = new Knight(position,isWhite);
			k.position = position;
			return k;
		}
}
