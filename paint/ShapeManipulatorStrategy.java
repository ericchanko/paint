package ca.utoronto.utm.paint;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


public class ShapeManipulatorStrategy implements  EventHandler<MouseEvent> {
	private PaintModel paintModel;
	
	ShapeManipulatorStrategy(PaintModel paintModel){
		this.paintModel=paintModel;
	}
	
	void addCommand(PaintCommand command){
		this.paintModel.addCommand(command);
	}
	
	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			this.mouseDragged(event);
		} else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			this.mousePressed(event);
		} else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
			this.mouseMoved(event);
		} else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			this.mouseClicked(event);
		} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			this.mouseReleased(event);
		} else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
			this.mouseEntered(event);
		} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
			this.mouseExited(event);
		}
	}
	public void mouseMoved(MouseEvent e) { }
	public void mouseDragged(MouseEvent e) { }
	public void mouseClicked(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
}
