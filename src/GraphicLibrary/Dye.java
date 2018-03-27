package GraphicLibrary;

public class Dye {
	
	private int r;
	private int g;
	private int b;
	private double alpha;
	
	public Dye(int r, int g, int b, double alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}

	public Dye(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = 1;
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

}
