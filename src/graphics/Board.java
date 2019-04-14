package graphics;



public class Board {
	private Piece board[][] = new Piece[8][8];
	
	
	
	
	public Board() {
	}
	
	public Board clone() {
		Board cloneBoard = new Board();
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				//Null equals empty
				if(board[x][y] != null)
					cloneBoard.board[x][y] = board[x][y].clone();
			}
		}
		return cloneBoard;
	}
	
	public void reset() {
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				//Null equals empty
				board[x][y] = null;
			}
		}
		
		for(int x = 0; x < 8; x ++) {
			createPiece(new Pawn(new Point(x,6),false));
		}
		
		createPiece(new Rook(new Point(0,7),false));
		createPiece(new Rook(new Point(7,7),false));

		createPiece(new Knight(new Point(1,7),false));
		createPiece(new Knight(new Point(6,7),false));

		
		createPiece(new Bishop(new Point(2,7),false));
		createPiece(new Bishop(new Point(5,7),false));

		
		createPiece(new King(new Point(4,7),false));
		createPiece(new Queen(new Point(3,7),false));
		
		
		for(int x = 0; x < 8; x ++) {
			createPiece(new Pawn(new Point(x,1),true));
		}
		
		createPiece(new Rook(new Point(0,0),true));
		createPiece(new Rook(new Point(7,0),true));

		createPiece(new Knight(new Point(1,0),true));
		createPiece(new Knight(new Point(6,0),true));

		
		createPiece(new Bishop(new Point(2,0),true));
		createPiece(new Bishop(new Point(5,0),true));

		
		createPiece(new King(new Point(4,0),true));
		createPiece(new Queen(new Point(3,0),true));
	}
	
	private void createPiece(Piece p) {
		setPiece(p.position,p);
	}

	private Piece getPiece(int x, int y) {
		try {
		return board[x][y];
		}
		catch(ArrayIndexOutOfBoundsException exception) {
		    return null;
		}
	}
	
	
	public Point getKingsPosition(boolean isWhite) {
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				Point pos = new Point(x,y);
				Piece p = getPiece(pos);
				if(p != null) {
					if(p.getClass() == King.class) {
						if(p.isWhite() == isWhite) {
							return pos;
						}
					}
				}
			}
		}
		return null;
	}
	
	public Point getAttackersPosition(Point kingsPosition, boolean isWhite) {
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				Point pos = new Point(x,y);
				Piece p = getPiece(pos);
				if(p != null) {
					if(p.isWhite() == !isWhite) {
						if(p.isValidMove(this,  new Action(pos, kingsPosition,true))) {
							return pos;
						}
					}
				}
			}
		}
		return null;
	}
	
	public Piece getPiece(Point pos) {
		return getPiece(pos.getX(),pos.getY());
	}
	
	public Piece getPieceRelative(Point pos, Point relative) {
		return getPiece(pos.Translate(relative));
	}
	
	
	public void setPiece(Point pos, Piece piece) {
		board[pos.getX()][pos.getY()] = piece;
		if(piece != null)
			piece.setPosition(pos);
	}
	
	public boolean allEmptyBetweenPoints(Point from, Point to) {
		Point difference = Point.difference(from, to);
		Point normal = difference.normalized();
		
		
		if(from.getX() == to.getX()) {
			for(int y = 1; y < Math.abs(difference.getY()); y ++) {
				Piece p = getPiece(from.getX(),from.getY() + (y*normal.getY()));
				if(p != null) {
					return false;
				}
			}
		}
		else if(from.getY() == to.getY()) {
			for(int x = 1; x < Math.abs(difference.getX()); x ++) {
				Piece p = getPiece(from.getX() + (x*normal.getX()),from.getY());
				if(p != null) {
					return false;
				}
			}
		}
		//Doesnt work
		else {
			for(int n = 1; n < Math.abs(difference.getX()); n ++) {
				Piece p = getPiece(from.getX() + (n*normal.getX()),from.getY() + (n*normal.getY()));
				if(p != null) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void callAllNextTurn(boolean isWhiteTurn) {
		
		for(int x = 0; x < 8; x ++) {
			for(int y = 0; y < 8; y ++) {
				Piece p = getPiece(x,y);
				if(p == null)
					continue;
		//Must be on the right colours turn
				if(isWhiteTurn && p.isWhite()) {
					p.nextTurn();
				}
				else if(!isWhiteTurn && !p.isWhite()) {
					p.nextTurn();
				}
			}
		}
	}
	
	
	
	
}
