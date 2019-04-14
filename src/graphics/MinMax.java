package graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class MinMax {
	boolean isWhite;
	
	int[][] pawn = { {0,  0,  0,  0,  0,  0,  0,  0},
			{50, 50, 50, 50, 50, 50, 50, 50},
			{10, 10, 20, 30, 30, 20, 10, 10},
			 {5,  5, 10, 25, 25, 10,  5,  5},
			 {0,  0,  0, 20, 20,  0,  0,  0},
			 {5, -5,-10,  0,  0,-10, -5,  5},
			 {5, 10, 10,-20,-20, 10, 10,  5},
			 {0,  0,  0,  0,  0,  0,  0,  0}};
	
	int[][] knights = {{-50,-40,-30,-30,-30,-30,-40,-50},
			{-40,-20,  0,  0,  0,  0,-20,-40},
			{-30,  0, 10, 15, 15, 10,  0,-30},
			{-30,  5, 15, 20, 20, 15,  5,-30},
			{-30,  0, 15, 20, 20, 15,  0,-30},
			{-30,  5, 10, 15, 15, 10,  5,-30},
			{-40,-20,  0,  5,  5,  0,-20,-40},
			{-50,-40,-30,-30,-30,-30,-40,-50},};
	int[][] bishops = {{-20,-10,-10,-10,-10,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5, 10, 10,  5,  0,-10},
			{-10,  5,  5, 10, 10,  5,  5,-10},
			{-10,  0, 10, 10, 10, 10,  0,-10},
			{-10, 10, 10, 10, 10, 10, 10,-10},
			{-10,  5,  0,  0,  0,  0,  5,-10},
			{-20,-10,-10,-10,-10,-10,-10,-20}};
	
	int[][] rooks =   {{0,  0,  0,  0,  0,  0,  0,  0},
			  {5, 10, 10, 10, 10, 10, 10,  5},
			  {-5,  0,  0,  0,  0,  0,  0, -5},
			  {-5,  0,  0,  0,  0,  0,  0, -5},
			  {-5,  0,  0,  0,  0,  0,  0, -5},
			  {-5,  0,  0,  0,  0,  0,  0, -5},
			  {-5,  0,  0,  0,  0,  0,  0, -5},
				  { 0,  0,  0,  5,  5,  0,  0,  0}};
	int[][] queen = {{-20,-10,-10, -5, -5,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5,  5,  5,  5,  0,-10},
				{ -5,  0,  5,  5,  5,  5,  0, -5},
			  {0,  0,  5,  5,  5,  5,  0, -5},
			{-10,  5,  5,  5,  5,  5,  0,-10},
			{-10,  0,  5,  0,  0,  0,  0,-10},
			{-20,-10,-10, -5, -5,-10,-10,-20}};
	
	
	public MinMax(Board board, boolean isWhite) {
		this.isWhite = isWhite;
	}
	
	public Action getBestAction(Board board) {
		List<Move> moves = allMovesOnBoard(board.clone(),false);
		 int lowestVal = 9999;
	        Move bestMove = null;
	        for (int i = 0; i < moves.size(); i++) {
	        		Move m = moves.get(i);
	            if(m.pointsAtBoardInMove < lowestVal) {
	            		bestMove = minimax(2,m, m.board.clone(),isWhite);
	            }
	        }
	     return new Action(bestMove.p.getPosition(),bestMove.to);
	}
	
	Move minimax (int depth, Move currentMove, Board board, boolean isWhite) {
	    if (depth == 0) {
	        return currentMove;
	    }
	    List<Move> moves = currentMove.getMoves();
	    if (isWhite) {
	    	 int highestVal = -9999;
		        Move bestMove = null;
		        for (int i = 0; i < moves.size(); i++) {
		        		Move m = moves.get(i);
		            if(m.pointsAtBoardInMove > highestVal) {
		            		bestMove = minimax(depth - 1,m, m.board.clone(),!isWhite);
		            }
		        }
	        return bestMove;
	    } else {
	        int lowestVal = 9999;
	        Move bestMove = null;
	        for (int i = 0; i < moves.size(); i++) {
	        		Move m = moves.get(i);
	            if(m.pointsAtBoardInMove < lowestVal) {
	            		bestMove = minimax(depth - 1,m, m.board.clone(),!isWhite);
	            }
	            //game.undo();
	        }
	        return bestMove;
	    }
	}
	
	public int evaluateBoard(Board board) {
		int points = 0;
		for(int x = 0; x < 8; x ++)
			for(int y = 0; y < 8; y ++) {
				Point p = new Point(x,y);
				Piece piece = board.getPiece(p);
				
				if(piece == null)
					continue;
				if(piece.isWhite() == true) {
					points += piece.value();
				}
				else {
					points -= piece.value();
				}
				
			}
		
		return points;
	}
	
	/*
	public Action minimax(int depth, Board board, boolean isWhite) {
		if(depth == 0)
			return boardPoints(board,isWhite);
	}*/
	
	public Action findBestMove(Board board) {
		Board testBoard = board.clone();
		List<Move> moves = allMovesOnBoard(testBoard,isWhite);
		//moves.sort(new MoveComparator());
		//Collections.sort(moves, new MoveComparator());
	
		Move m = null;
		int value = -100000000;
		//Select the 5 best moves
		for(int i = 0; i < moves.size(); i ++) {
			int thisValue = moves.get(i).getPointsFromBestMove(2,!isWhite,1);
			if(thisValue > value) {
				value = thisValue;
				m = moves.get(i);
			}
		}
		
		return new Action(m.p.position,m.to);
	}
	
	
	public List<Move> allMovesOnBoard(Board board, boolean isWhite){
		List<Move> moves = new ArrayList<Move>();
		for(int x = 0; x < 8; x ++)
			for(int y = 0; y < 8; y ++) {
				Point p = new Point(x,y);
				Piece piece = board.getPiece(p);
				if(piece != null)
				if(piece.isWhite() == isWhite) {
					for(Point posTo : piece.getPossibleMoves(board)) {
						moves.add(new Move(piece,posTo,board.clone(), isWhite));
					}
				}
				
			}
		return moves;
	}
	
	private class Move{
		Piece p;
		Point to;
		int pointsAtBoardInMove;
		List<Move> movesMove = new ArrayList<Move>();
		Board board;
		public Move(Piece p, Point to, Board board, boolean isWhite) {
			this.p = p;
			this.to = to;
			this.board = board;
			pointsAtBoardInMove = boardPoints(board, isWhite);
			
			//Do the move
			/*Piece p2 = board.getPiece(p.position);
			board.setPiece(p.position, null);
			board.setPiece(to, p2);
			pointsAtBoardInMove = evaluateBoard(board);*/
		}
		public List<Move> getMoves(){
			return allMovesOnBoard(board,isWhite);
		}
		public int getPointsFromBestMove(int depth, boolean isWhite, int loweredSize) {
			if(depth == 0) {
				return pointsAtBoardInMove;
			}
			Board testBoard = board.clone();
			//Do the move
			Piece p2 = testBoard.getPiece(p.position);
			testBoard.setPiece(p.position, null);
			testBoard.setPiece(to, p2);
			
			List<Move> moves = allMovesOnBoard(testBoard,isWhite);
			//Collections.sort(moves, new MoveComparator());
			
			int value = -100000000;
			//Select the 5 best moves
			for(int i = 0; i < (int)(((double)moves.size() / loweredSize)); i ++) {
				int thisValue = moves.get(i).getPointsFromBestMove((depth - 1),!isWhite, (loweredSize+2));
				if(thisValue > value) {
					value = thisValue;
				}
			}
			return value;
		}
	}
	
	class MoveComparator implements Comparator<Move> {
		@Override
		public int compare(Move m1, Move m2) {
			int p1 = m1.pointsAtBoardInMove;
			int p2 = m2.pointsAtBoardInMove;
	 
			if (p1 > p2) {
				return -1;
			} else if (p1 < p2) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	public int boardPoints(Board board, boolean isWhite) {
		int points = 0;
		for(int x = 0; x < 8; x ++)
			for(int y = 0; y < 8; y ++) {
				Point p = new Point(x,y);
				Piece piece = board.getPiece(p);
				
				int y2 = Math.abs(y-7);
				
				if(piece == null)
					continue;
				if(!piece.isWhite())
					y2 = y;
				int val = piece.value();
				if(piece.getClass() == Rook.class)
					val += rooks[x][y];
				else if(piece.getClass() == Queen.class)
					val += queen[x][y];
				else if(piece.getClass() == Bishop.class)
					val += bishops[x][y];
				else if(piece.getClass() == Pawn.class)
					val += pawn[x][y];
				else if(piece.getClass() == Knight.class)
					val += knights[x][y];
				if(piece.isWhite() == isWhite) {
					points += piece.value();
				}
				else {
					points -= piece.value();
				}
				
			}
		
		return points;
	}
}
