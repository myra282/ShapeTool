package GraphicLibrary;

public class Color {
	
	private int r;
	private int g;
	private int b;
	private int alpha;
	
	public Color(int r, int g, int b, int alpha) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}

	public Color(int r, int g, int b) {
		super();
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

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

}
