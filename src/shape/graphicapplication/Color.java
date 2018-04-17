package shape.graphicapplication;

public class Color implements Cloneable {
	
	private int r;
	private int g;
	private int b;
	private double alpha;
	
	public Color(int r, int g, int b, double alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}

	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = 1;
	}
	
	@Override
	public Color clone() {
		try {
			return (Color) super.clone();
		} catch (CloneNotSupportedException e) {}
		
		return null;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) {
	    	return false;
	    }
	    else if (other == this) {
	    	return true;
	    }
	    else if (!(other instanceof Color)) {
	    	return false;
	    }
	    else {
	    	Color otherColor = (Color) other;
	    	if ((r == otherColor.r) && (g == otherColor.g) && (b == otherColor.b)) {
	    		return true;
	    	}
	    	else {
	    		return false;
	    	}
	    }
	}

}
