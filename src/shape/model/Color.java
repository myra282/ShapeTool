package shape.model;

import java.io.Serializable;

public class Color implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6686743383101465278L;
	private int r;
	private int g;
	private int b;
	private double alpha;
	
	public Color(int r, int g, int b, double alpha) {
		this.r = Math.abs(r);
		this.g = Math.abs(g);
		this.b = Math.abs(b);
		double tmpAlpha = Math.abs(alpha);
		if (tmpAlpha > 1) {
			this.alpha = 1;
		}
		else {
			this.alpha = tmpAlpha;
		}
	}

	public Color(int r, int g, int b) {
		this.r = Math.abs(r);
		this.g = Math.abs(g);
		this.b = Math.abs(b);
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
		this.r = Math.abs(r);
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = Math.abs(g);
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = Math.abs(b);
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		double tmpAlpha = Math.abs(alpha);
		if (tmpAlpha > 1) {
			this.alpha = 1;
		}
		else {
			this.alpha = tmpAlpha;
		}
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
