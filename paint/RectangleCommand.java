package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;

public class RectangleCommand extends PaintCommand {
	private Point p1,p2;
	public RectangleCommand(Point p1, Point p2){
		this.p1 = p1; this.p2=p2;
		this.setChanged();
		this.notifyObservers();
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
		this.setChanged();
		this.notifyObservers();
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
		this.setChanged();
		this.notifyObservers();
	}

	public Point getTopLeft(){
		return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
	}
	public Point getBottomRight(){
		return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
	}
	public Point getDimensions(){
		Point tl = this.getTopLeft();
		Point br = this.getBottomRight();
		return(new Point(br.x-tl.x, br.y-tl.y));
	}
	
	@Override
	public void execute(GraphicsContext g) {
		Point topLeft = this.getTopLeft();
		Point dimensions = this.getDimensions();
		if(this.isFill()){
			g.setFill(this.getColor());
			g.fillRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		} else {
			g.setStroke(this.getColor());
			g.strokeRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		}
	}
}
