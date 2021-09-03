package ca.utoronto.utm.paint;

public class ShapeManipulatorFactory {
	public static ShapeManipulatorStrategy create(String strategyName, PaintModel paintModel){
		ShapeManipulatorStrategy strategy=null;
		if(strategyName=="Circle"){
			strategy=new CircleManipulatorStrategy(paintModel);
		} else if(strategyName=="Squiggle"){
			strategy=new SquiggleManipulatorStrategy(paintModel);
		} else if(strategyName=="Rectangle"){
			strategy=new RectangleManipulatorStrategy(paintModel);
		} else if(strategyName=="Polyline") {
			strategy=new PolylineManipulatorStrategy(paintModel);
		}
		return strategy;
	}
}
