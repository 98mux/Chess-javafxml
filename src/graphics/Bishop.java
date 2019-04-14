package graphics;

public class Bishop extends Piece{
	
	public Bishop(Point position, boolean isWhite) {
		 super(position,isWhite);
	 }
	 
	 public boolean isValidMove(Board board, Action action) {
		 
		 Piece p = board.getPiece(action.getToPosition());
		 if(p != null && p.isWhite == this.isWhite)
			 return false;
		 
		 Point difference = Point.difference(action.getFromPosition(), action.getToPosition());
			if(Math.abs(((double)difference.getX()) / ((double)difference.getY())) != 1) {
				return false;
			}
			
			
		 if(board.allEmptyBetweenPoints(action.getFromPosition(), action.getToPosition())) {
			 return true;
		 }
		 return false;
	 }
	 
	 public boolean canMove(Board board, Point thisPosition) {
		 areArgumentsLegal(board,thisPosition);

		 if(isValidMove(board, new Action(thisPosition, thisPosition.Translate(1,1)))
				 ||
				 isValidMove(board, new Action(thisPosition, thisPosition.Translate(1,-1)))
						 ||
						 isValidMove(board, new Action(thisPosition, thisPosition.Translate(-1,-1)))
								 ||
								 isValidMove(board, new Action(thisPosition, thisPosition.Translate(1,-1)))) {
			 return true;
		 }
		 return false;
		}
		 
	 
 public void nextTurn() {
		 
	 }
	 public int value() { return 30;}
	 @Override
		public Piece clone() {
			Bishop b = new Bishop(position,isWhite);
			b.position = position;
			return b;
		}
}
