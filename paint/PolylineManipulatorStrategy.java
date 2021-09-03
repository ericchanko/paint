package ca.utoronto.utm.paint;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PolylineManipulatorStrategy extends ShapeManipulatorStrategy {

	PolylineManipulatorStrategy(PaintModel paintModel) {
		super(paintModel);
		// TODO Auto-generated constructor stub
	}
	
	private PolylineCommand polylineCommand;
	
//	public void mouseDragged(MouseEvent e) {
//		this.polylineCommand.add(new Point((int)e.getX(), (int)e.getY()));
//	}
	
	public void mouseMoved(MouseEvent e) {
		Point p2 = new Point((int)e.getX(), (int)e.getY());
		if(this.polylineCommand != null) {
			polylineCommand.endPoint(p2);
		}
		//this.polylineCommand.setP2(p2);
	}
	
	
	public void mousePressed(MouseEvent e) {
		Point p1 = new Point((int)e.getX(), (int)e.getY());
		
		
		if (this.polylineCommand == null) {
			if (e.getButton() == MouseButton.PRIMARY) {
				this.polylineCommand = new PolylineCommand();
				polylineCommand.add(p1);
				polylineCommand.startPoint(p1);
				this.addCommand(polylineCommand);
			}
		}
		
		else {
			if (e.getButton() == MouseButton.PRIMARY) {
				polylineCommand.add(p1);
				polylineCommand.startPoint(p1);
				this.addCommand(polylineCommand);
				
			}
			else if (e.getButton() == MouseButton.SECONDARY) {
				if (polylineCommand.getPoints().size() >= 3) {
					this.addCommand(polylineCommand);
					polylineCommand = null;
				}
				
			}
		}
	}
}

