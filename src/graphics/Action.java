package graphics;

import java.util.Arrays;
import java.util.List;

public class Action {
	private Point fromPosition;
	private Point toPosition;
	
	private Piece movedPiece;
	private Piece captured;
	private int rokadeState = 0;
	private boolean isPassant = false;
	private Piece promotionPiece = null;
	//Things like rokade and promotion causes changes to the bricks in the isValidMove, if true this will prevent the changes
	private boolean isATest = false;
	private boolean hasMovedHasChanged = false;
	public boolean hasMovedHasChanged() {return hasMovedHasChanged;}
	public boolean isATest() {return isATest;}
	public Piece getPromotionPiece() {return promotionPiece;}
	public int getRokadeState() {return rokadeState;}
	public Piece getCapturedPiece() {return captured;}
	public Piece getMovedPiece() {return movedPiece;}
	public Point getFromPosition() {return fromPosition;}
	public Point getToPosition() {return toPosition;}
	public boolean isPassant() {return isPassant;}
	
	public void setHasMovedHasChanged(boolean hasMoved) {
		this.hasMovedHasChanged = hasMoved;
	}
	public void setPromotionPiece(Piece promotion) {
		this.promotionPiece = promotion;
	}
	

	/*, Piece captured, boolean isBrokade*/
	public Action(Point from, Point to) {
		this.fromPosition = from;
		this.toPosition = to;
	}
	
	public Action(Point from, Point to, boolean isATest){
		this.fromPosition = from;
		this.toPosition = to;
		this.isATest = isATest;
		
	}
	
	public void SetRookBrokade(int state) {
		this.rokadeState = state;
	}
	
	public void SetIsPassant(boolean yes) {
		isPassant = yes;
	}
	
	public void SetCapturedPiece(Piece piece) {
		if(piece == null)
			return;
		/*captured = piece.clone();*/
		captured = piece;
	}
	
	public void SetMovedPiece(Piece piece) {
		if(piece == null)
			return;
		/*movedPiece = piece.clone();*/
		movedPiece = piece;
	}
	
	public String saveString() {
		return fromPosition.getX() + "," + fromPosition.getY() + "," + toPosition.getX() + "," + toPosition.getY();
	}
	public static Action loadString(String string) {
		Action a;
		Point f = null;
		Point t = null;
		String s[] = string.split(",");
		for(int i = 0; i < s.length; i ++) {
			switch(i) {
			case 1:
				f = new Point(Integer.parseInt(s[i-1]),Integer.parseInt(s[i]));
				break;
			
			case 3:
				t = new Point(Integer.parseInt(s[i-1]),Integer.parseInt(s[i]));
				break;
			}
		}
		
		a = new Action(f,t);
		return a;
	}
	
	static List<Character> alphabet = Arrays.asList('a','b','c','d','e','f','g','h');
	
	public String intToLetters(int t) {
		return alphabet.get(t).toString();
	}
	
	public String typeToString() {
		if(movedPiece.getClass() == King.class) {
			return "K";
		}
		if(movedPiece.getClass() == Queen.class) {
			return "Q";
		}
		if(movedPiece.getClass() == Rook.class) {
			return "R";
		}
		if(movedPiece.getClass() == Knight.class) {
			return "N";
		}
		if(movedPiece.getClass() == Bishop.class) {
			return "B";
		}
		return "";
	}
	
	public String pointToString(Point p) {
		return intToLetters(p.getX()) + (p.getY() + 1);
	}
	
	public String toString() { 
		if(rokadeState == 1) {
			return "0-0-0";
		}
		else if(rokadeState == 2) {
			return "0-0";
		}
		String s = typeToString();
		if(captured != null) {
			s += "x";
		}
		s += pointToString(toPosition);
		//TODO passant and promotion and Disambiguating moves
		return s;
	} 
}
