package ca.utoronto.utm.paint;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PaintModel extends Observable implements Observer {

	public void save(PrintWriter writer) {
		
		writer.println("Paint Save File Version 1.0");
		
		for(int i=0; i < this.commands.size(); i++) {
			PaintCommand command = this.commands.get(i);
			
			if(command instanceof RectangleCommand) {
				writer.println("Rectangle");
				
				writer.println(command.getColorString());
				if (command.isFill()) {
					writer.println("	filled:true");
				}
				if (!command.isFill()) {
					writer.println("	filled:false");
				}
				Point p1 = ((RectangleCommand) command).getP1();
				Point p2 = ((RectangleCommand) command).getP2();
				writer.println("	p1:(" + Integer.toString(p1.x)+","+ Integer.toString(p1.y)+")");
				writer.println("	p2:(" + Integer.toString(p2.x)+","+ Integer.toString(p2.y)+")");
				writer.println("End Rectangle");
				
			}
			if (command instanceof CircleCommand) {
				writer.println("Circle");
				writer.println(command.getColorString());
				if (command.isFill()) {
				writer.println("	filled:true");
				}
				if (!command.isFill()) {
					writer.println("	filled:false");
				}
				Point center = ((CircleCommand) command).getCentre();
				writer.println("	center:(" + Integer.toString(center.x)+","+ Integer.toString(center.y)+")");
				writer.println("	radius:"+Integer.toString(((CircleCommand) command).getRadius()));
				
				writer.println("End Circle");
				
			}
			if (command instanceof SquiggleCommand){
				writer.println("Squiggle");
				
				writer.println(command.getColorString());
				if (command.isFill()) {
					writer.println("	filled:true");
				}
				if (!command.isFill()) {
					writer.println("	filled:false");
				}
				writer.println("	points");
				
				ArrayList<Point> points = ((SquiggleCommand) command).getPoints();
				
				for (Point point: points) {
					writer.println("		point:("+Integer.toString(point.x)+","+ Integer.toString(point.y)+")");
				}					
				writer.println("	end Points");
				writer.println("End Squiggle");
				
			}
			if (command instanceof PolylineCommand) {
				writer.println("Polyline");
				writer.println(command.getColorString());
				if (command.isFill()) {
				writer.println("	filled:true");
				}
				if (!command.isFill()) {
					writer.println("	filled:false");
				}
				
				writer.println("	points");
				ArrayList<Point> points = ((PolylineCommand) command).getPoints();
				
				for (Point point: points) {
					writer.println("		point:("+Integer.toString(point.x)+","+Integer.toString(point.y)+")");
				}
				writer.println("	end points");
				writer.println("End Polyline");
			}
		}
		writer.println("End Paint Save File");
		writer.close();
	}
	public void reset(){
		for(PaintCommand c: this.commands){
			c.deleteObserver(this);
		}
		this.commands.clear();
		this.setChanged();
		this.notifyObservers();
	}
	
	public void addCommand(PaintCommand command){
		this.commands.add(command);
		command.addObserver(this);
		this.setChanged();
		this.notifyObservers();
	}
	
	private ArrayList<PaintCommand> commands = new ArrayList<PaintCommand>();

	public void executeAll(GraphicsContext g) {
		for(PaintCommand c: this.commands){
			c.execute(g);
		}
	}
	
	/**
	 * We Observe our model components, the PaintCommands
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers();
	}
}
