package shape.model;

public class Rectangle extends AbstractShape {
	
	private double width;
	private double height;
	private boolean rounded;

	public Rectangle(Point position, double width, double height, boolean rounded) {
		super(position);
		this.width = width;
		this.height = height;
		this.rounded = rounded;
	}

	public Rectangle(Point position, double width, double height) {
		super(position);
		this.width = width;
		this.height = height;
		this.rounded = false;
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

	public boolean getRounded() {
		return rounded;
	}

	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}

	@Override
	public void attributeEditorCreate() {
		
	}

	@Override
	public void scale(double ratio) {
		setWidth(width*ratio);
		setHeight(height*ratio);
	}

	@Override
	public boolean contains(Point p) {
		Point pos = getPosition();
		System.out.println(p.getX() >= pos.getX());
		System.out.println(p.getY() >= pos.getY());
		System.out.println(pos.getX() + getWidth() >= p.getX());
		System.out.println(pos.getY() + getHeight() >= p.getY());
		return ((p.getX() >= pos.getX() && p.getY() >= pos.getY()) && 
				(pos.getX() + getWidth() >= p.getX() && 
				 pos.getY() + getHeight() >= p.getY()));
	}

}
