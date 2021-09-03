package ca.utoronto.utm.paint;

import java.util.Observable;
import java.util.Observer;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

class PaintPanel extends StackPane implements Observer, EventHandler<MouseEvent> {

	private PaintModel model; 
	private Canvas canvas;
	private ShapeManipulatorStrategy strategy;
	
	public PaintPanel(PaintModel model) {

		this.canvas = new Canvas(500, 500);
		this.getChildren().add(this.canvas);
		// The canvas is transparent, so the background color of the
		// containing pane serves as the background color of the canvas.
		this.setStyle("-fx-background-color: white");
		this.canvas.addEventHandler(MouseEvent.ANY, this);
		this.setPaintModel(model);
		
	}

	/**
	 * Hookup the paint model to this 
	 * @param model
	 */
	public void setPaintModel(PaintModel model) {
		if(model==null)return;
		if(this.model!=null) {
			this.model.deleteObserver(this);
		}
		this.model = model;
		this.setShapeManipulatorStrategy(new ShapeManipulatorStrategy(this.model)); // set to the empty strategy
		this.model.addObserver(this);
		this.repaint();
	}
	
	public void repaint() {
		GraphicsContext g = this.canvas.getGraphicsContext2D();
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		this.model.executeAll(g);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.repaint();
	}

	@Override
	public void handle(MouseEvent event) {
		this.strategy.handle(event);
	}
	public void setShapeManipulatorStrategy(ShapeManipulatorStrategy strategy) {
		this.strategy = strategy;
	}
}

