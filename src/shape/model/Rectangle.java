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
	
	@Override
	public ShapeMemento createMemento() {
		return new ShapeMemento(width, height, getPosition(), getRotation(), getRotationCenter(), getColor(), getRounded());
	}

	@Override
	public void restoreMemento(ShapeMemento mem) {
		this.setWidth(mem.getD1());
		this.setHeight(mem.getD2());
		this.setPosition(mem.getPosition().clone());
		this.setRotation(mem.getRotation());
		this.setRotationCenter(mem.getRotationCenter().clone());
		this.setRounded(mem.getRounded());
		this.setColor(mem.getColor());
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
	
	private Point computePoint(Point p) {
		Point c = getPosition();
		// translate point to origin
		double tmpX = p.getX() - c.getX();
		double tmpY = p.getY() - c.getY();
		// apply rotation
		double newX = tmpX * Math.cos(Math.toRadians(getRotation())) - tmpY * Math.sin(Math.toRadians(getRotation()));
		double newY = tmpX * Math.sin(Math.toRadians(getRotation())) + tmpY * Math.cos(Math.toRadians(getRotation()));
		// translate back
		return new Point(newX + c.getX(), newY + c.getY());
	}
	
	private Point[] computePoints() {
		Point oldPoints[] = new Point[4];
		Point newPoints[] = new Point[4];
		oldPoints[0] = getPosition();
		oldPoints[1] = new Point(getPosition().getX() + width, getPosition().getY());
		oldPoints[2] = new Point(getPosition().getX() + width, getPosition().getY() + height);
		oldPoints[3] = new Point(getPosition().getX(), getPosition().getY() + height);
		for (int i = 0 ; i < oldPoints.length ; ++i) {
			newPoints[i] = computePoint(oldPoints[i]);
		}
		return newPoints;
	}

	@Override
	public boolean contains(Point p) {
		Point[] points = computePoints();
		int i,j; 
		int nvert = points.length;
		boolean res = false;
		for (i = 0, j = nvert - 1; i < nvert; j = i , ++i) {
			Point pointi = new Point(points[i].getX(), points[i].getY());
			Point pointj = new Point(points[j].getX(), points[j].getY());
			if ((pointi.getY() >= p.getY()) != (pointj.getY() >= p.getY()) &&
			(p.getX() <= (pointj.getX() - pointi.getX()) * (p.getY() - pointi.getY()) / (pointj.getY() - pointi.getY()) + pointi.getX())) {
				res = !res;
			}
		}
		return res;
	}

}
