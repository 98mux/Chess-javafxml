package graphics;

import java.util.Arrays;
import java.util.List;

public class Point {
	private int x,y;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public String toString() {
		return x + "  " + y;
	}
	static List<Character> alphabet = Arrays.asList('a','b','c','d','e','f','g','h');
	public String toNotationString() {
		String s = "";
		s +=	 alphabet.get(x).toString();;
		s += (y+1);
		return s;
	}
	
	public boolean equals(Point point) {
		if(x == point.x && y == point.y) {
			return true;
		}
		return false;
	}
	
	public Point TranslateThis(Point point) {
		this.x += point.x;
		this.y += point.y;
		return this;
	}
	
	public Point TranslateThis(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	
	public Point newPoint() {
		return new Point(x,y);
	}
	
	public Point Translate(Point point) {
	
		return new Point(this.x+point.x,this.y+point.y);
	}
	public Point Translate(int x,int y) {
		
		return new Point(this.x+x,this.y+y);
	}
	
	
	public static Point Translate(Point p1, Point p2) {
		return Translate(p1.x,p1.y,p2.x,p2.y);
	} 
	
	public static Point Translate(int x, int y, int x2, int y2) {
		return new Point(x+x2,y+y2);
	}
	public boolean inBound() {
		if(x >= 0 && y >= 0 && x <= 7 && y <=7) {
			return true;
		}
		return false;
	}
	
	public Point normalized() {
		int x,y;
		if(this.x > 0) {
			x = 1;
		}
		else{
			x = -1;
		}
		
		if(this.y > 0) {
			y = 1;
		}
		else{
			y = -1;
		}
		return new Point(x,y);
		
	}
	
	public static Point difference(Point point1, Point point2) {
		return new Point(point2.x-point1.x,point2.y-point1.y);
	}
}
