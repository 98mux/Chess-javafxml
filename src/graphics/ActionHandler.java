package graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class ActionHandler {
	
	Board board;
	
	MinMax AI;
	
	Stack<Action> actions = new Stack<Action>();
	Stack<Action> undoneActions = new Stack<Action>();
	
	
	Stockfish stockfish;
	
	
	
	public boolean isWhiteTurn() {if((turn%2)==0) return true; return false;}
	public int getFullMove() {
		return (int)Math.floor(((double)(turn + 2)) / 2);
	}
	public int turn = 0;
	private String exception = "";
	
	int halfmoveSinceLastCaptureorPawnAdvance = 0;
	
	
	
	public String getException() {
		return exception;
	}
	
	public ActionHandler(Board board) {
		this.board = board;
		AI = new MinMax(board,false);
		stockfish = new Stockfish();
		if (stockfish.startEngine()) {
			System.out.println("Engine has started..");
			stockfish.sendCommand("uci");
		} else {
			System.out.println("Oops! Something went wrong..");
		}
	}
	
	public void undo() {
		if(actions.size() == 0) {
			exception = "Nothing to undo";
			return;
		}
		Action a = actions.pop();
		handleUndoAction(a);

	}
	
	public void save(String filename) {
		try {
		new Save(actions,filename + ".txt");
		}
		catch(IOException e) {}
	}
	public void Load(String filename) {
		ResetAndLoadActions(Load.LoadFile(filename + ".txt"));
	}
	
	public void ResetAndLoadActions(Stack<Action> actions) {
		turn = 0;
		board.reset();
		this.actions.clear();
		for(int i = 0; i < actions.size(); i ++) {
			newAction(actions.get(i));
		}
	}
	
	public void redo() {
		if(undoneActions.size() == 0) {
			exception = "Nothing to redo";
			return;
		}
		Action a = undoneActions.pop();
		handleAction(a);

	}
	
	public void newAction( Action action) throws IllegalArgumentException{
		handleAction(action);
		undoneActions.clear();
	}
	
	public void handleAction( Action action) throws IllegalArgumentException {
		actions.push(action);
		movePiece( action);
	}
	public void handleUndoAction(Action action) {
		undoneActions.push(action);
		undoMovePiece( action);
		turn --;
		
	}
	
	int movePiece(Action action) {
		try {
			int i = movePiece(action, isWhiteTurn());
			exception = "";
			nextTurn();
			return i;
		}
		catch(IllegalArgumentException e) {
			//Action unsuccesfull
			actions.remove(action);
			exception = e.getLocalizedMessage();
			throw e;
			//return -1;
		}
		
	}
	
	//Returns -1 if failed :) returns 0 if no one takes, returns points if taken
		public int movePiece(Action action, boolean isWhiteTurn) {
			
			int points = 0;
			Piece movingPiece = board.getPiece(action.getFromPosition());
			action.SetMovedPiece(movingPiece);
			Piece pieceTo = board.getPiece(action.getToPosition());
			action.SetCapturedPiece(pieceTo);
			//If no moving piece
			if(movingPiece == null) {
				throw new IllegalArgumentException("You cant move air");
			}
			
			//If the same color
			if(pieceTo != null) {
				if(movingPiece.isWhite() == pieceTo.isWhite()) {
					throw new IllegalArgumentException("You cant capture your own color");
				}
				points = pieceTo.value();
			}
			
			//Must be on the right colours turn

				// even, aka white's turn
			if(isWhiteTurn && !movingPiece.isWhite()) {
				throw new IllegalArgumentException("It is the White's turn");
			}

			if(!isWhiteTurn && !movingPiece.isBlack()) {
					// odd, black's turn
				throw new IllegalArgumentException("It is the Black's turn");
			}
			
			
			
			
			if(!movingPiece.isValidMove(board, action)) {
				throw new IllegalArgumentException("Invalid move");
			}
			
			//TODO Can now use the undoAction function instead of the code below
			
			Piece backup1 = board.getPiece(action.getFromPosition());
			Piece backup2 = board.getPiece(action.getToPosition());
			
			//Already done in isValidMove if the action is a promotion
			if(action.getPromotionPiece() == null) {
				board.setPiece(action.getFromPosition(),null);
				board.setPiece(action.getToPosition(),movingPiece);
			}
			
			//If this move makes your king go in check then the move is invalid
			Point kingsPos = board.getKingsPosition(isWhiteTurn);
			King king = ((King)board.getPiece(kingsPos));
			if(king.isInCheck(board, kingsPos, kingsPos)) {
				undoMovePiece(action);
				//board.setPiece(action.getFromPosition(),backup1);
				//board.setPiece(action.getToPosition(),backup2);
				throw new IllegalArgumentException("That would cause checkmate");
			}
			
			if(action.getCapturedPiece() != null) {
				halfmoveSinceLastCaptureorPawnAdvance = -1;
			}
			if(action.getMovedPiece().getClass() == Pawn.class) {
				halfmoveSinceLastCaptureorPawnAdvance = -1;
			}
			
			
			
			return points;
		}
	
	void undoMovePiece(Action action) {
		
		
		
		Piece captured = action.getCapturedPiece();
		Piece movedPiece = action.getMovedPiece();
		//Action was a promotion
		if(action.getPromotionPiece() != null) {
			//Replace the promotion piece with the pawn
			board.setPiece(action.getToPosition(),action.getMovedPiece());
		}
		
		int brokadeState = action.getRokadeState();
		//Brokade does not work as intended
		if(brokadeState != 0) {
			Piece rook = null;
			int x = 0;
			if(brokadeState == 1) {
				rook = board.getPiece(action.getFromPosition().Translate(-1,0));
			}
			else {
				rook = board.getPiece(action.getFromPosition().Translate(1,0));
				x = 7;
			}
			//Remove the other rook
			//It should not be null
			if(rook != null) {
				((Rook)rook).Reset();
				((King)movedPiece).Reset();
				board.setPiece(rook.position, null);
				board.setPiece(new Point(x,action.getFromPosition().getY()), rook);
			}
		}
		
		
		board.setPiece(action.getFromPosition(), movedPiece);
		
		if(action.isPassant()) {
			board.setPiece(captured.getPosition(), captured);
			board.setPiece(action.getToPosition(), null);
		}
		else {
			board.setPiece(action.getToPosition(), captured);
		}
	
	}
	
	public void nextTurn() {
		
		
		//exception = "";
		turn ++;
		halfmoveSinceLastCaptureorPawnAdvance ++;
		
		int currentState = currentState();
		
		if(currentState == 1) {
			exception = "CHECKMATE";
		}
		else if(currentState == 2) {
			exception = "REMIS";
		}
		
		//System.out.print("TURN NUMBER : " + turn);
		//System.out.println();
		//board.printBoard();
		board.callAllNextTurn(isWhiteTurn());
		
		//This is for AI, comment out if you are not using AI
		if(currentState == 0 && !isWhiteTurn()) {
			
			//String PGNmove = stockfish.getBestMove(toFenString(), 200);
			//System.out.print("AI CHOOSES TO DO THIS: " + PGNmove);
			
			//AI's turn
			//newAction(AI.findBestMove(board));
			//newAction(AI.getBestAction(board));
		}
	}
	/*1.Piece placement (from white's perspective). Each rank is described, starting with rank 8 and ending with rank 1; within each rank, the contents of each square are described from file "a" through file "h". Following the Standard Algebraic Notation (SAN), each piece is identified by a single letter taken from the standard English names (pawn = "P", knight = "N", bishop = "B", rook = "R", queen = "Q" and king = "K").[1] White pieces are designated using upper-case letters ("PNBRQK") while black pieces use lowercase ("pnbrqk"). Empty squares are noted using digits 1 through 8 (the number of empty squares), and "/" separates ranks.
2.Active colour. "w" means White moves next, "b" means Black.
3.Castling availability. If neither side can castle, this is "-". Otherwise, this has one or more letters: "K" (White can castle kingside), "Q" (White can castle queenside), "k" (Black can castle kingside), and/or "q" (Black can castle queenside).
4.En passant target square in algebraic notation. If there's no en passant target square, this is "-". If a pawn has just made a two-square move, this is the position "behind" the pawn. This is recorded regardless of whether there is a pawn in position to make an en passant capture.[2]
5.Halfmove clock: This is the number of halfmoves since the last capture or pawn advance. This is used to determine if a draw can be claimed under the fifty-move rule.
6.Fullmove number: The number of the full move. It starts at 1, and is incremented after Black's move.*/
	public String toFenString() {
		String s = "";
		int numbOfEmptySpaces = 0;
		for(int y = 0; y < 8; y ++) {
			for(int x = 0; x < 8; x ++){
				int y2 = Math.abs(y - 7);
				//Rank starts at top
				Piece p = board.getPiece(new Point(x,y2));
				
				
				if(p != null && numbOfEmptySpaces > 0) {
					s += numbOfEmptySpaces;
					numbOfEmptySpaces = 0;
				}
				
				if(p == null) {
					numbOfEmptySpaces ++;
				}
				else if(p.isWhite()) {
					if(p.getClass() == King.class) {
						s += "K";
					}
					else if(p.getClass() == Bishop.class) {
						s += "B";
					}
					else if(p.getClass() == Knight.class) {
						s += "N";
					}
					else if(p.getClass() == Rook.class) {
						s += "R";
					}
					else if(p.getClass() == Queen.class) {
						s += "Q";
					}
					else if(p.getClass() == Pawn.class) {
						s += "P";
					}
				
				}
				else {
					if(p.getClass() == King.class) {
						s += "k";
					}
					else if(p.getClass() == Bishop.class) {
						s += "b";
					}
					else if(p.getClass() == Knight.class) {
						s += "n";
					}
					else if(p.getClass() == Rook.class) {
						s += "r";
					}
					else if(p.getClass() == Queen.class) {
						s += "q";
					}
					else if(p.getClass() == Pawn.class) {
						s += "p";
					}
				}
				
				
			}
			if(numbOfEmptySpaces > 0) {
				s+= numbOfEmptySpaces;
				numbOfEmptySpaces = 0;
			}
			if(y != 7) {
				s += "/";
			}
		}
		s+= " ";
		if(isWhiteTurn() == true) {
			s += "w";
		}
		else {
			s += "b";
		}
		Point whitekingpos = board.getKingsPosition(true);
		Piece whiteking = board.getPiece(whitekingpos);
		s += " ";
		boolean canCastle = false;
		if(whitekingpos.equals(new Point(4,0))) {
			Piece rook1 = board.getPiece(new Point(7,0));
			if(rook1 != null) {
				if(rook1.getClass() == Rook.class) {
					if(whiteking.hasMoved == false && rook1.hasMoved == false) {
						s+="K";
						canCastle = true;
					}
				}
			}
			
			Piece rook2 = board.getPiece(new Point(0,0));
			if(rook2 != null) {
				if(rook2.getClass() == Rook.class) {
					if(whiteking.hasMoved == false && rook2.hasMoved == false) {
						s+="Q";
						canCastle = true;
					}
				}
			}
		}
		
		Point blackkingpos = board.getKingsPosition(false);
		Piece blackking = board.getPiece(blackkingpos);
		if(blackkingpos.equals(new Point(4,7))) {
			Piece rook1 = board.getPiece(new Point(7,7));
			if(rook1 != null) {
				if(rook1.getClass() == Rook.class) {
					if(blackking.hasMoved == false && rook1.hasMoved == false) {
						s+="k";
						canCastle = true;
					}
				}
			}
			
			Piece rook2 = board.getPiece(new Point(0,7));
			if(rook2 != null) {
				if(rook2.getClass() == Rook.class) {
					if(blackking.hasMoved == false && rook2.hasMoved == false) {
						s+="q";
						canCastle = true;
					}
				}
			}
		}
		
		if(canCastle == false) {
			s+= "-";
		}
		
		s+=" ";
		
		if(actions.size() > 0) {
		Action lastAction = actions.peek();
		
		Piece p = lastAction.getMovedPiece();
		
		if(p != null) {
		if(p.getClass() == Pawn.class) {
			Pawn pawn = (Pawn)p;
			boolean hasDoubleMoved = pawn.getDidDoubleMove();
			if(pawn.isWhite()) {
				if(lastAction.getToPosition().getY() == 3) {
					s += lastAction.getToPosition().Translate(0,-1).toNotationString();
				}
				else {s += "-";}
			}
			else {
				if(lastAction.getToPosition().getY() == 4) {
					s += lastAction.getToPosition().Translate(0,1).toNotationString();
				}
				else {s += "-";}
			}
		}
		else {s += "-";}
		}
		else {s += "-";}
		}
		else {s += "-";}
		
		s += " ";
		s += halfmoveSinceLastCaptureorPawnAdvance;
		
		s += " ";
		s += getFullMove();
		/*if(lastAction.hasMovedHasChanged()) {
			
		}
		else {
			s+="-";
		}*/
		
		
		return s;
	}
	

	//Requires the king to be in check and cannot move;
	//and no other brick can move to block the attack or kill the attacker;
	//or if you cant move your king or any other brick (and your king is not in check)
	//Or if any of your bricks can take the attacking brick
	//If a moves makes it so that the king is in check is not allowed
	
	//Checkmate, after the other player have done a turn, never on the players'turn can he get checkmated
	
	//There is only one brick that makes the king get in check, there cant possibly be 2, i think
	//Find all open spaces, between the attacker and the king and check if anyone can take them (or take the attacker)
	
	//0 = nothing, 1 = checkmate, 2 = remis
	public int currentState() {
		//Unfinished
		Point pos = board.getKingsPosition(isWhiteTurn());
		King king = ((King)board.getPiece(pos));
		

		
		boolean canKingMove = king.canMove(board, pos);
		boolean isInCheck = king.isInCheck(board, pos, pos);
		
		
		
		if(!canKingMove && isInCheck) {
			//Can any bricks take the attacker? Or take the empty space between the attacker and the king
			//Hva skjer når det er to attackers? er dette et problem? De kan vel ikke bli gjort noe med, kan de?
			List<Point> blockablePosition = new ArrayList<Point>();
			Point attacker = board.getAttackersPosition(pos, isWhiteTurn());
			
			Point difference = Point.difference(pos, attacker);
			
			Point inBetween = new Point(pos.getX(), pos.getY());
			for(;true;) {
				
				if(difference.getX() == 0 && difference.getY() == 0) {
					break;
				}
				
				if(difference.getX() < 0) {
					difference.TranslateThis(1, 0);
					inBetween.TranslateThis(-1, 0);
				}
				else if(difference.getX() > 0) {
					difference.TranslateThis(-1, 0);
					inBetween.TranslateThis(1, 0);
				}
				
				if(difference.getY() < 0) {
					difference.TranslateThis(0, 1);
					inBetween.TranslateThis(0, -1);
				}
				else if(difference.getY() > 0) {
					difference.TranslateThis(0, -1);
					inBetween.TranslateThis(0, 1);
				}
				
				blockablePosition.add(inBetween.newPoint());
			}
			
			//Check if any of the enemy bricks can go to the empty tiles
			for(int n = 0; n < blockablePosition.size(); n ++) {
				
				for(int x = 0; x < 8; x ++) {
					for(int y = 0; y < 8; y ++) {
						Point loopPos = new Point(x,y);
						Piece p = board.getPiece(loopPos);
						if(p!= null) {
							if(p.getClass() != king.getClass()) {
								if(p.isWhite() == isWhiteTurn()) {
									if(p.isValidMove(board, new Action(loopPos, blockablePosition.get(n)))) {
										return 0;
									}
								}
							}
						}
					}
				}
				
			}
			
			
			
			return 1;
		}
		
		//No bricks can move, king is isnt in check and king cant move, so it must be REMIS
		//DETTE ER REMIKODE
		if(!isInCheck && !canKingMove) {
			//can any of the bricks move?
			for(int x = 0; x < 8; x ++) {
				for(int y = 0; y < 8; y ++) {
					Point loopPos = new Point(x,y);
					Piece p = board.getPiece(loopPos);
					if(p!= null) {
						if(p.isWhite() == isWhiteTurn()) {
							if(p.canMove(board, loopPos)) {
								return 0;
							}
						}
					}
				}
			}
			
			return 2;
		}
		
		
		return 0;
	}
	
	
}
