package ca.utoronto.utm.paint;
import java.util.Observable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class PaintCommand extends Observable {
	private Color color;
	private boolean fill;
	
	PaintCommand(){
		// Pick a random color for this
		int r = (int)(Math.random()*256);
		int g = (int)(Math.random()*256);
		int b= (int)(Math.random()*256);
		this.color = Color.rgb(r, g, b);
		
		this.fill = (1==(int)(Math.random()*2));
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public boolean isFill() {
		return fill;
	}
	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
	public String getColorString() {
		int r = (int) (this.color.getRed()*256);
		int g = (int) (this.color.getGreen()*256);
		int b = (int) (this.color.getBlue()*256);

		String s = "";
		s+="\tcolor:"+r+","+g+","+b;
		return s;
	}
	public String toString(){
		int r = (int) this.color.getRed();
		int g = (int) this.color.getGreen();
		int b = (int) this.color.getBlue();

		String s = "";
		s+="\tcolor:"+r+","+g+","+b+"\n";
		s+="\tfilled:"+this.fill+"\n";
		return s;
	}
	
	public abstract void execute(GraphicsContext g);
}
