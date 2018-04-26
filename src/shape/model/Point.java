package shape.model;

import java.io.Serializable;

public class Point implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6742121893913531922L;
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Point clone() {
		try {
			return (Point) super.clone();
		} catch (CloneNotSupportedException e) {}
		
		return null;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) {
	    	return false;
	    }
	    else if (other == this) {
	    	return true;
	    }
	    else if (!(other instanceof Point)) {
	    	return false;
	    }
	    else {
	    	Point otherPoint = (Point) other;
	    	if ((x == otherPoint.getX()) && (y == otherPoint.getY())) {
	    		return true;
	    	}
	    	else {
	    		return false;
	    	}
	    }
	}

	@Override
	public String toString() {
		return "Point [x = " + x + ", y = " + y + "]";
	}

}
