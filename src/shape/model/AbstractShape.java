package shape.model;

import shape.graphicapplication.Color;

public abstract class AbstractShape implements IShape {
	
	private IShape parent;
	private Point position;
	private double rotation;
	private Point rotationCenter;
	private Color color;

	public AbstractShape() {
		this.parent = null;
		this.position = new Point(0, 0);
		this.rotation = 0;
		this.rotationCenter = new Point(0, 0);
		this.color = new Color(0, 0, 0);
	}

	public AbstractShape(Point position) {
		this.parent = null;
		this.position = position;
		this.rotation = 0;
		this.rotationCenter = new Point(0, 0);
		this.color = new Color(0, 0, 0);
	}

	@Override
	public IShape clone() {
		try {
			IShape s = (IShape) super.clone();
			((AbstractShape) s).parent = null;
			Point pos = getPosition();
			((AbstractShape) s).position = new Point(pos.getX(), pos.getY());
			Point center = getRotationCenter();
			((AbstractShape) s).rotationCenter = new Point(center.getX(), center.getY());
			((AbstractShape) s).color = color.clone();
			return s;
		} catch (CloneNotSupportedException e) {}
		
		return null;
	}
	
	public void attributeEditorCreate() {
		
	}

	void update() {
		Point pos = getPosition();
		rotationCenter = new Point(pos.getX()+getWidth()/2, pos.getY()+getHeight()/2);
	}

	@Override
	public IShape getParent() {
		return parent;
	}

	void setParent(IShape parent) {
		this.parent = parent;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Point getRotationCenter() {
		return rotationCenter;
	}
	
	public void setRotationCenter(Point p) {
		this.rotationCenter = p;
	}
	
	@Override
	public boolean isInside(Point min, Point max) {
		Point p = getPosition();
		return ((p.getX() >= min.getX() && p.getY() >= min.getY()) && 
				(p.getX() + getWidth() <= max.getX() && 
				 p.getY() + getHeight() <= max.getY()));
	}

}
