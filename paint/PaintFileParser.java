package ca.utoronto.utm.paint;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.paint.Color;
/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about
 * its effort to parse a file. After a successful parse, an instance
 * will have an ArrayList of PaintCommand suitable for rendering.
 * If there is an error in the parse, the instance stores information
 * about the error. For more on the format of Version 1.0 of the paint 
 * save file format, see the associated documentation.
 * 
 * @author 
 *
 */
public class PaintFileParser {
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage =""; // error encountered during parse
	private PaintModel paintModel; 
	
	/**
	 * Below are Patterns used in parsing 
	 */
	private Pattern pFileStart=Pattern.compile("^PaintSaveFileVersion1.0$");
	private Pattern pFileEnd=Pattern.compile("^EndPaintSaveFile$");

	private Pattern pCircleStart=Pattern.compile("^Circle$");
	private Pattern pCenter=Pattern.compile("^center:[(][0-9]+,[0-9]+[)]$");
	private Pattern pRadius=Pattern.compile("^radius:[0-9]+$");
	private Pattern pCircleEnd=Pattern.compile("^EndCircle$");
	
	private Pattern pRectangleStart=Pattern.compile("^Rectangle$");
	private Pattern pP1=Pattern.compile("^p1:[(][0-9]+,[0-9]+[)]$");
	private Pattern pP2=Pattern.compile("^p2:[(][0-9]+,[0-9]+[)]$");
	private Pattern pRectangleEnd=Pattern.compile("^EndRectangle$");
	
	private Pattern pSquiggleStart=Pattern.compile("^Squiggle$");
	private Pattern pSquiggleEnd=Pattern.compile("^EndSquiggle$");
	
	private Pattern pPolylineStart=Pattern.compile("^Polyline$");
	private Pattern pPolylineEnd=Pattern.compile("^EndPolyline$");
	
	private Pattern pPoints=Pattern.compile("^points$");
	private Pattern pPoint=Pattern.compile("^point:[(][0-9]+,[0-9]+[)]$");
	private Pattern pEndPoints=Pattern.compile("^endpoints$");

	
	
	private Pattern pColor=Pattern.compile("^color:([0-9]|[1-8][0-9]|9[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]),([0-9]|[1-8][0-9]|9[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]),([0-9]|[1-8][0-9]|9[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
	private Pattern pFilled=Pattern.compile("^filled:(true|false)");
	
	
	private Pattern digit = Pattern.compile("[0-9]+");
	private Pattern whiteSpace=Pattern.compile("");

	// ADD MORE!!
	
	/**
	 * Store an appropriate error message in this, including 
	 * lineNumber where the error occurred.
	 * @param mesg
	 */
	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}
	
	/**
	 * 
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}
	
	/**
	 * Parse the inputStream as a Paint Save File Format file.
	 * The result of the parse is stored as an ArrayList of Paint command.
	 * If the parse was not successful, this.errorMessage is appropriately
	 * set, with a useful error message.
	 * 
	 * @param inputStream the open file to parse
	 * @param paintModel the paint model to add the commands to
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream, PaintModel paintModel) {
		this.paintModel = paintModel;
		this.errorMessage="";
		
		// During the parse, we will be building one of the 
		// following commands. As we parse the file, we modify 
		// the appropriate command.
		
		CircleCommand circleCommand = null; 
		RectangleCommand rectangleCommand = null;
		SquiggleCommand squiggleCommand = null;
		PolylineCommand polylineCommand = null;
		
		Color color = null;
		boolean filled = false;
		ArrayList<Point> points=new ArrayList<Point>();
		Point center = null;
		Point p1 = null;
		Point p2 = null;
		Point point = null;
		int radius = 0;
		Pattern currentCommand = null;
	
		try {	
			int state=0; Matcher m, c, r, s, p,e, end; String l;
			
			this.lineNumber=0;
			while ((l = inputStream.readLine()) != null) {
				l = l.replaceAll("\\s+","");
				m=whiteSpace.matcher(l);
				if (m.matches()){
					break;
				}
				this.lineNumber++;
				System.out.println(lineNumber+" "+l+" "+state);
				switch(state){
					case 0:
						m=pFileStart.matcher(l);
						if(m.matches()){
							state=1;
							break;
						}
						error("Expected Start of Paint Save File");
						return false;
					case 1: // Looking for the start of a new object or end of the save file
						c=pCircleStart.matcher(l);
						if(c.matches()){
							currentCommand = pCircleStart;
							state=2; 
							break;
						}
						r=pRectangleStart.matcher(l);
						if(r.matches()) {
							currentCommand = pRectangleStart;
							state=2;
							break;
						}
						s=pSquiggleStart.matcher(l);
						if(s.matches()) {
							currentCommand=pSquiggleStart;
							squiggleCommand = new SquiggleCommand();
							state=2; 
							break;
						}
						p=pPolylineStart.matcher(l);
						if(p.matches()) {
							currentCommand=pPolylineStart;
							polylineCommand = new PolylineCommand();
							state=2; 
							break;
						}
						e=pFileEnd.matcher(l);
						if(e.matches()) {
							state = 20;
							break;
						}
						error("Expected a new object or end of file");
						return false;				
					case 2:
						// ADD CODE
						m=pColor.matcher(l);
						if(m.matches()) {
							color = ColorParser(l);
							state = 3;
							break;
						}
						error("Expected valid color");
						return false;
					case 3:
						m=pFilled.matcher(l);
						if(m.matches()) {
							filled = fillParser(l);
							state = 4;
							break;
						}
						error("Expected valid filled parameter");
						return false;
					
					case 4:
						
						if (currentCommand == pCircleStart) {
							c=pCenter.matcher(l);
							if (c.matches()) {
								center = PointParser(l);
								state = 5;
								break;
							}
							error("Radius not good");
							return false;
						}
						else if (currentCommand == pRectangleStart) {
							r=pP1.matcher(l);
							if (r.matches()) {
								p1 = RectPointParser(l);
								state = 5;
								break;
							}
							error("Expected a point P1");
							return false;
						}
						else if (currentCommand == pSquiggleStart ||currentCommand == pPolylineStart) {
							s=pPoints.matcher(l);
							if (s.matches()) {
								state = 6;
								break;
							}
							error("Expected points");
							return false;
						}
						
						error("Invalid command");
						return false;
						
					case 5:
						if (currentCommand == pCircleStart) {
							c=pRadius.matcher(l);
							if (c.matches()) {
								radius = IntParser(l);
								state = 8;
								break;
							}
							error("Invalid center Point");
							return false;
						}
						else if (currentCommand == pRectangleStart) {
							r=pP2.matcher(l);
							if (r.matches()) {
								p2 = RectPointParser(l);
								state = 8;
								break;
							}
							error("Expected a point P2");
							return false;
						}
						error("Command is not valid - not a circle or rectangle");
						return false;
					
					case 6: // add points to arrayList for squiggle and polyline
						end=pEndPoints.matcher(l);
						
						if(end.matches()) {
							state = 8;
							break;
						}
						m=pPoint.matcher(l);
						if(m.matches()) {
							point = PointParser(l); // returns a point
							if (currentCommand == pSquiggleStart) {
								squiggleCommand.add(point);
							}
							if (currentCommand == pPolylineStart) {
								polylineCommand.add(point);
							}
							state = 6;
							break;
						}
						error("Expected end points or More Points");
						return false;
					
					case 8:
						if (currentCommand == pCircleStart) {
						c=pCircleEnd.matcher(l);
							if(c.matches()){
								circleCommand = new CircleCommand(center, radius);
								circleCommand.setColor(color);
								circleCommand.setFill(filled);
								paintModel.addCommand(circleCommand);
								state=1; 
								break;
							}
						}
						else if (currentCommand == pRectangleStart) {
							r=pRectangleEnd.matcher(l);
							if(r.matches()) {
								rectangleCommand = new RectangleCommand(p1, p2);
								rectangleCommand.setColor(color);
								rectangleCommand.setFill(filled);
								paintModel.addCommand(rectangleCommand);
								state=1;
								break;
							}
						}
						else if (currentCommand == pSquiggleStart) {
							s=pSquiggleEnd.matcher(l);
							if(s.matches()) {
								squiggleCommand.setColor(color);
								squiggleCommand.setFill(filled);
								paintModel.addCommand(squiggleCommand);
								state=1; 
								break;
							}
						}
						else if (currentCommand == pPolylineStart) {
							p=pPolylineEnd.matcher(l);
							if(p.matches()) {
								state=1; 
								polylineCommand.setColor(color);
								polylineCommand.setFill(filled);
								paintModel.addCommand(polylineCommand);
								break;
							}
						}
						error("Expected ending of the specific shape");
						return false;

					case 20: // Reached end of the File
						error("Expected nothing more");
						return false;
				}
			}
		}  catch (Exception e){
			
		}
		return true;
	}
	
	
	/**
	 * Parse through l and extract the number
	 * 
	 * @param l line to be parsed
	 * @return an integer
	 */
	private int IntParser(String l) {
		// TODO Auto-generated method stub
		int number = 0;
		l=l.replace("radius:", "");		
		number = Integer.parseInt(l);
		
		System.out.println("test");

		System.out.println(number);

		return number;
	}
	/**
	 * Parse through l and extract the boolean value for the filled parameter
	 * @param l
	 * @return boolean
	 */

	private boolean fillParser(String l) {
		// TODO Auto-generated method stub
		if (!(l.contains("false"))){
			return true;	
		}
		return false;
	}
	
	/**
	 * Parse through l and extract the color for the color parameter
	 * @param l
	 * @return Color
	 */
	private Color ColorParser(String l) {
		// TODO Auto-generated method stub
		int r=0,g=0,b=0,i=0;
		Matcher d = digit.matcher(l);
		//l=l.replace("color:","");
		l.split(",");
		while (d.find()) {
			if (i==0) {
				r=Integer.parseInt(d.group());
				i++;
			}
			else if (i==1) {
				g=Integer.parseInt(d.group());
				i++;
			}
			else {
				b=Integer.parseInt(d.group());
				i++;
			}
		}


		return Color.rgb(r,g,b);
	}
	
	/**
	 * Parse through l and extract the given point. Either a point for squiggle and
	 * polyline or the center of the circle
	 * @param l
	 * @return Point
	 */
	private Point PointParser(String l) {
		int x = 0,y = 0, i=0;
		Matcher d = digit.matcher(l);
		
		l=l.replace("point:", "");
		l=l.replace("center:","");
		
		while(d.find()) {
			if(i==0) {
				x=Integer.parseInt(d.group());
				i++;
			}
			else {
				y=Integer.parseInt(d.group());
				i++;
			}
		}


		return new Point(x,y);
	}
	/**
	 * Parse through l and extract p1 or p2 for rectangle
	 * @param l
	 * @return Point
	 */
	
	private Point RectPointParser(String l) {
		int x=0, y=0,i=0;
		Matcher d = digit.matcher(l);
		
		while(d.find()) {
			if(i==0) {
				i++;
			}
			else if(i==1) {
				x=Integer.parseInt(d.group());
				i++;
			}
			else if(i==2) {
				y=Integer.parseInt(d.group());
				i++;
			}
		}
		System.out.println("testing");
		System.out.println(x);
		System.out.println(y);
		return new Point(x,y);
		
	}
}
