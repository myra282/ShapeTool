package shape.model;

public class Rectangle extends AbstractShape {
	
	private double width;
	private double height;
	private boolean rounded;

	public Rectangle(Point position, double width, double height, boolean rounded) {
		super(position);
		this.width = Math.abs(width);
		this.height = Math.abs(height);
		this.rounded = rounded;
	}

	public Rectangle(Point position, double width, double height) {
		super(position);
		this.width = Math.abs(width);
		this.height = Math.abs(height);
		this.rounded = false;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = Math.abs(width);
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = Math.abs(height);
	}

	public boolean getRounded() {
		return rounded;
	}

	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}

	@Override
	public void scale(double ratio) {
		setWidth(width*Math.abs(ratio));
		setHeight(height*Math.abs(ratio));
	}

	@Override
	public boolean contains(Point p) {
		Point pos = getPosition();
		return ((p.getX() >= pos.getX() && p.getY() >= pos.getY()) && 
				(pos.getX() + getWidth() >= p.getX() && 
				 pos.getY() + getHeight() >= p.getY()));
	}

}
