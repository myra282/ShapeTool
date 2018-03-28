package Shape;

public class Rect extends AbstractShape {
	
	private double width;
	private double height;
	private double rounded;

	public Rect(Point position, double width, double height, double rounded) {
		super(position);
		this.width = width;
		this.height = height;
		this.rounded = rounded;
	}

	public Rect(Point position, double width, double height) {
		super(position);
		this.width = width;
		this.height = height;
		this.rounded = 0;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getRounded() {
		return rounded;
	}

	public void setRounded(double rounded) {
		this.rounded = rounded;
	}

	@Override
	public void attributeEditorCreate() {
		
	}

	@Override
	public void resize(double ratio) {
		setWidth(width*ratio);
		setHeight(height*ratio);
	}

}
