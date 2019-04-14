package graphics;


public class Pawn extends Piece{
	//Used to do the speical move
	private boolean didDoubleMove = false;
	
	public boolean getDidDoubleMove() {return didDoubleMove;}
	
	private int direction = -1;
	private int yStartPos = 6;
	
	public Pawn(Point position, boolean isWhite) {
		 super(position,isWhite);
		 if(isWhite() == true) {
			 direction = 1;
			 yStartPos = 1;
		 }
	 }

	 
	 public boolean isValidMove(Board board, Action action) {
		 areArgumentsLegal(board,action.getFromPosition());
		 
		 
		 Piece OtherPiece = board.getPiece(action.getToPosition());
		 
		 
		 Point difference = Point.difference(action.getFromPosition(), action.getToPosition());
		 
		 if(OtherPiece == null) {
			 if( difference.getY() == direction*2 && action.getFromPosition().getY() == yStartPos && difference.getX() == 0) {
				 didDoubleMove=true;
				 return true;
			 }
			 
			 if(difference.getY() == direction && difference.getX() == 0) {
				 promotion(board,action);
				 return true;
			 }
			 
		 }
		 else {
			 if(OtherPiece.isWhite() == isWhite()) {
				 return false;
			 }
			 
			 if(difference.getY() == direction && Math.abs(difference.getX()) == 1) {
				 promotion(board,action);
				 return true;
			 }
		 }
		 
		 //Special move can only be done if it is 
		 if(difference.getY() != direction || Math.abs(difference.getX()) != 1) {
			 return false;
		 }
		 //Special move
		 Point speicalPiecePos = action.getToPosition().Translate(new Point(0,-direction));
		 Piece specialPiece = board.getPiece(speicalPiecePos);
		 if(specialPiece != null) {
			 if(specialPiece.isWhite() != this.isWhite()) {
				 if(specialPiece.getClass() == Pawn.class) {
					 if(((Pawn)specialPiece).didDoubleMove == true) {
						 if(action.isATest()) { //Skip the actual passant, its just a test
							 return true;
						 }
						 action.SetCapturedPiece(specialPiece);
						 action.SetIsPassant(true);
						 board.setPiece(speicalPiecePos, null);
						 return true;
					 }
				 }
			 }
		 }
		 
		 return false;
	 }
	 
	 private void promotion(Board board,Action action) {
		 if(action.isATest()) { //Dont actually do the promotion if it is just a test
			 return;
		 }
		 
		 //Just set it to Queen right now
		
		 
		 if(isWhite()) {
			 if(action.getToPosition().getY() == 7) {
				 Piece promotionPiece = new Queen(action.getToPosition(),isWhite());
				 board.setPiece(action.getFromPosition(), null);
				 board.setPiece(action.getToPosition(),promotionPiece);
				 action.setPromotionPiece(promotionPiece);
			 }
		 }
		 else {
			 if(action.getToPosition().getY() == 0) {
				 Piece promotionPiece = new Queen(action.getToPosition(),isWhite());
				 board.setPiece(action.getFromPosition(), null);
				 board.setPiece(action.getToPosition(),promotionPiece);
				 action.setPromotionPiece(promotionPiece);
			 }
		 }
	 }
	 
	 public boolean canMove(Board board, Point thisPosition) {
		 areArgumentsLegal(board,thisPosition);

		 if(isValidMove(board, new Action(thisPosition, thisPosition.Translate(0,direction)))
				 ||
				 isValidMove(board, new Action(thisPosition, thisPosition.Translate(0,direction*2)))
						 ||
						 isValidMove(board, new Action(thisPosition, thisPosition.Translate(1,direction)))
								 ||
								 isValidMove(board, new Action(thisPosition, thisPosition.Translate(-1,direction)))) {
			 return true;
		 }
		 
		 
		 
		 return false;
	 }
	 
	 
 public void nextTurn() {
		 didDoubleMove = false;
		 
	 }
	 public int value() { return 10;}
	 
	 @Override
		public Piece clone() {
			Pawn p = new Pawn(position,isWhite);
			p.position = position;
			p.didDoubleMove = this.didDoubleMove;
			p.direction = this.direction;
			p.yStartPos = this.yStartPos;
			return p;
		}
}
