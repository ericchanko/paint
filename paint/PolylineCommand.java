package ca.utoronto.utm.paint;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

public class PolylineCommand extends PaintCommand {

private Point p1;
private Point p2;

	
private ArrayList<Point> points=new ArrayList<Point>();
	
/**
 * Add point p to ArrayList
 * @param p
 */
	public void add(Point p){ 
		this.points.add(p); 
		this.setChanged();
		this.notifyObservers();
	}
/**
 * Set p1 (starting point)
 * @param p1
 */
	public void startPoint(Point p1) {
		this.p1 = p1;
		this.setChanged();
		this.notifyObservers();
	}
/**
 * Set p2
 * @param p2
 */
	public void endPoint(Point p2) {
		this.p2 = p2;
		this.setChanged();
		this.notifyObservers();
	}
	
	public ArrayList<Point> getPoints(){ return this.points; }

	public void execute(GraphicsContext g) {
		// TODO Auto-generated method stub
		if (p1 != null && p2 != null) {
			g.setStroke(this.getColor());
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
		ArrayList<Point> points = this.getPoints();
		g.setStroke(this.getColor());
		for(int i=0;i<points.size()-1;i++){
			Point p1 = points.get(i);
			Point p2 = points.get(i+1);
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
		
	}

}
