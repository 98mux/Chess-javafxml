package graphics;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
	
	Point startPos = null;
	
	public boolean hasMoved() {
		return this.hasMoved;
	}
	
	public King(Point position, boolean isWhite) {
		 super(position,isWhite);
		 startPos = position.newPoint();
	 }
	public void Reset() {
		hasMoved = false;
	}
	
	 public boolean isValidMove(Board board, Action action) {
		 areArgumentsLegal(board,action.getFromPosition());
		 
		 Piece p = board.getPiece(action.getToPosition());
		 if(p != null && p.isWhite == this.isWhite)
			 return false;
		 
		 if(isInCheck(board, action.getFromPosition(), action.getToPosition())) {
				return false;
				//throw new IllegalArgumentException("That will checkmate you m88");
			}
		 
		 if(tryDoRokade(board, action)) {
			 return true;
		 }
		 
		 //Need to have a function to see if the king is in check
		 if(!isValidMoveIgnoreCheck(board,action.getFromPosition(),action.getToPosition()))
			 return false;
		 
		
		
		 
		 return true;
	 }
	 
	 private boolean tryDoRokade(Board board, Action action) {
		 if(hasMoved == true) 
			 return false;
		 
		 Point difference = Point.difference(action.getFromPosition(), action.getToPosition());
		 
		 if(Math.abs(difference.getX()) != 2 || difference.getY() != 0) 
			 return false;
		 
		int rookXPos = 7;
		if(difference.getX() == -2) {
			rookXPos = 0;
			//Check if the tiles between are attacked
			if(isInCheck(board,action.getFromPosition(), action.getFromPosition().Translate(-1, 0)))
					return false;
			if(isInCheck(board,action.getFromPosition(), action.getFromPosition().Translate(-3, 0)))
				return false;
			
		}
		else {
			if(isInCheck(board,action.getFromPosition(), action.getFromPosition().Translate(1, 0)))
				return false;
		}
		
		//The spots between must be empty
		 if(!board.allEmptyBetweenPoints(action.getFromPosition(), new Point(rookXPos, action.getFromPosition().getY()))) {
			 return false;
		 }
		
		
		Piece rook = board.getPiece(new Point(rookXPos,action.getFromPosition().getY()));
		
		if(rook == null)
			return false;
		if(rook.getClass() != Rook.class) 
			return false;
		if(((Rook)rook).hasMoved == true) {
			return false;
		}
		
		if(action.isATest()) {
			return true; //Skip the actual rokade
		}
		
		/*Can do rokade*/
		board.setPiece(rook.position,null);
		if(difference.getX() == -2) {
			rookXPos = 3;
		}
		else {
			rookXPos = 5;
		}
		
		int state = 2;
		if(difference.getX() == -2) {state = 1;}
		action.SetRookBrokade(state);
		
		board.setPiece(new Point(rookXPos,action.getFromPosition().getY()),rook);
	
	
		
		return true;
		 
	 }

	 
	 public boolean canMove(Board board, Point position) {
		 areArgumentsLegal(board,position);
		 
		 for(int x = -1; x <=1 ; x ++) {
			 for(int y = -1; y <= 1; y++) {
				 Point newpos = position.Translate(new Point(x,y));
				 //Skip this one
				 if(x == 0 && y == 0)
					 continue;
				 
				 if(!newpos.inBound()) {
					 continue;
				 }
				if(isValidMove(board,new Action(position,newpos))){
					return true;
				}
				 
				 
			 }
		 }
		 return false;
	 }
	 
	 
	 //The current position to the king, and the position where the check test will happen
	 public boolean isInCheck(Board board, Point currentPosition, Point checkPosition) {
		 areArgumentsLegal(board,currentPosition);
		 
		 if(currentPosition.equals(checkPosition)) {
			 return isInCheckAtPos(board,checkPosition);
		 }
		 
		 Piece atCheckPos = board.getPiece(checkPosition);

		 board.setPiece(checkPosition, this);
		 board.setPiece(currentPosition, null);
		 boolean inCheck = true;
		 
		 inCheck = isInCheckAtPos(board,checkPosition);
		 
		 //Revert
		 board.setPiece(currentPosition, this);
		 board.setPiece(checkPosition, atCheckPos);
		 
		 
		 return inCheck;
	 }
	 
	 private boolean isInCheckAtPos(Board board, Point pos) {
			for(int x = 0; x < 8; x ++) {
				for(int y = 0; y < 8; y ++) {
					Point loopPos = new Point(x,y);
					Piece p = board.getPiece(loopPos);
					if(p != null) {
						if(p.isWhite() != isWhite) {
							//If the other bricks can move at the king position then the king is in check
							
							if(p.getClass() == King.class) {
								//Prevent forever loop
								if(((King)p).isValidMoveIgnoreCheck(board, loopPos, pos)) {

									return true;
								}
							}
							else {
								if(p.isValidMove(board, new Action(loopPos, pos,true))) {
									return true;
								}
							}
							
						}
					}
				}
			}
			return false;
		}
	 /*
	 @Override
	 public List<Point> getPossibleMoves(Board board, Point thisPosition){
		 //Dirty as fuck, way of doing this
		 List<Rook> rooks = new ArrayList<Rook>();
		 for(int x = 0; x < 8; x ++)
			 for(int y = 0; y < 8; y ++) {
				 Piece p = board.getPiece(new Point(x,y));
				 if(p != null)
					 if(p.getClass() == Rook.class)
						 rooks.add((Rook)(p.clone()));
			 }
		 List<Point> moves = super.getPossibleMoves(board,thisPosition);
		 
		 //The rooks can be moved by isvalidmove, so revert to original positions of the rooks
		 for(int x = 0; x < 8; x ++)
			 for(int y = 0; y < 8; y ++) {
				 Piece p = board.getPiece(new Point(x,y));
				 if(p != null)
					 if(p.getClass() == Rook.class)
						 board.setPiece(new Point(x,y), null);
			 }
		 
		 for(Rook r : rooks) {
			 board.setPiece(r.getPosition(), r);
		 }
		 
		 return moves;
	 }*/
	 
	 public boolean isValidMoveIgnoreCheck(Board board, Point thisPosition, Point toPosition) {
		 Point difference = Point.difference(thisPosition, toPosition);
		 
		 if(Math.abs(difference.getX()) > 1 || Math.abs(difference.getY()) > 1) {
			 return false;
		 }
		 return true;
	 }
	 
	 public void nextTurn() {
		 if(!startPos.equals(position)) {
			 hasMoved = true;
		 }
	 }
	 
	 public int value() { return 9000;}
	 
	 @Override
		public Piece clone() {
			King k = new King(position,isWhite);
			k.hasMoved = this.hasMoved;
			k.startPos = this.startPos;
			k.position = position;
			return k;
		}
}
