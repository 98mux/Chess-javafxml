package graphics;



public class Chess {
	
	Board board = new Board();
	ActionHandler actionHandler;

	public Chess() {
		actionHandler = new ActionHandler(board);
		board.reset();
		
	}
	public void tryMove(Point from, Point to) throws IllegalArgumentException{
		Action newAction = new Action(from,to);
		try {
			actionHandler.newAction(newAction);
		}
		catch(IllegalArgumentException e) {
			throw e;
		}
	}
}
