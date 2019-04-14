package graphics;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class CanMoveGraphics {
	List<ImageView> moveGraphics = new ArrayList<ImageView>();
	AnchorPane board;
	ImageGallery imageGallery;
	public CanMoveGraphics(AnchorPane board, ImageGallery imageGallery) {
		this.board = board;
		this.imageGallery = imageGallery;
		
	}
	public void createMarker(Point position) {
		ImageView image = new ImageView();
		image.setImage(imageGallery.canMoveImage);
		image.relocate(position.getX() * 60, Math.abs((position.getY() * 60 )- 7*60));
		image.toFront();
		board.getChildren().add(image);
		
		moveGraphics.add(image);
	}
	public void clear() {
		/*
		 * They will be cleared in graphics
		for(ImageView i : moveGraphics) {
			board.getChildren().remove(i);
		}*/
		moveGraphics.clear();
	}
}
