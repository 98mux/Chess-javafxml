package graphics;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController extends Application{
	 @Override
	    public void start(Stage primaryStage) {
	        try {
	            Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
	            
	           

	            Scene scene = new Scene(root,740,510);
	            primaryStage.setScene(scene);
	            primaryStage.show();
	            
	    			primaryStage.setScene(scene);
		            primaryStage.show();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public static void main(String[] args) {
	        launch(args);
	    }
	
/*
	public static void main(String[] args) {
		launch(args);
	}*/

	@FXML
	TextArea console ;

	@FXML
	TextField inputField ;
	@FXML
	AnchorPane board;
	
	@FXML
	TextField filename;
	
	
	@FXML
	TextArea actionsInGame;
	
	@FXML
	public void undo() {
		game.actionHandler.undo();
		graphics.updateGraphics();
	}
	@FXML
	public void redo() {
		game.actionHandler.redo();
		graphics.updateGraphics();
	}
	@FXML
	public void save() {
		game.actionHandler.save(filename.getText());
		graphics.updateGraphics();
	}
	@FXML
	public void load() {
		game.actionHandler.Load(filename.getText());
		graphics.updateGraphics();
	}

	//Her maa du deklarerere spillet ditt dersom det heter noe annet enn TicTacToe
	Chess game ;
	
	Graphics graphics;

	public void initialize(){
		game = new Chess() ;
		graphics = new Graphics(game,board,game.board);
		graphics.updateGraphics();
		
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
		    		//exception.setText(game.actionHandler.getException());
		    		
		    		String gameString = "GAME : ";
		    		for(int i = 0; i < game.actionHandler.actions.size(); i ++) {
		    			String action = game.actionHandler.actions.get(i).toString();
		    			gameString += "\n";
		    			gameString += i + ". " + action;
		    		}
		    		gameString += "\n\n" + game.actionHandler.getException();
		    		//gameString += "\n" + game.actionHandler.toFenString();
		    		actionsInGame.setText(gameString);
		    	 	actionsInGame.setScrollTop(Double.MAX_VALUE);
		    }
		}));
		/*
		actionsInGame.textProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		    	actionsInGame.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
		        //use Double.MIN_VALUE to scroll to the top
		    }
		});*/
		fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
		fiveSecondsWonder.play();

		
		//Her maa du opprette et objekt av spillet ditt med de argumentene som behoves for det - resten av koden vil gaa ut ifra at du har kalt den game
		
		//console.setText(game.toString());
		
		
		
		
		
	}

}
