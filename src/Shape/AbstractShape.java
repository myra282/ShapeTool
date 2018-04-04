package Shape;

import GraphicLibrary.Dye;

public abstract class AbstractShape implements IShape {
	
	private IShape parent;
	private Point position;
	private double rotation;
	private Point rotationCenter;
	//private int translation
	private Dye color;

	public AbstractShape() {
		this.parent = null;
		this.position = new Point(0, 0);
		this.rotation = 0;
		this.rotationCenter = new Point(0, 0);
		this.color = new Dye(0, 0, 0);
	}

	public AbstractShape(Point position) {
		this.parent = null;
		this.position = position;
		this.rotation = 0;
		this.rotationCenter = new Point(0, 0);
		this.color = new Dye(0, 0, 0);
	}

	@Override
	public IShape clone() {
		try {
			return (IShape) super.clone();
		} catch (CloneNotSupportedException e) {}
		
		return null;
	}
	
	public void attributeEditorCreate() {
		
	}

	@Override
	public void update() {
		Point pos = getPosition();
		rotationCenter = new Point(pos.getX()+getWidth()/2, pos.getY()+getHeight()/2);
	}

	@Override
	public IShape getParent() {
		return parent;
	}

	@Override
	public void setParent(IShape parent) {
		this.parent = parent;
	}
	
	/*public void notifyParent(Point p) {
		if (parent != null) {
			parent.notifyParent(p);
		}
		else {
			Point oldPos = getPosition();
			double diffX = p.getX() - oldPos.getX();
			double diffY = p.getY() - oldPos.getY();
			setPosition(new Point(oldPos.getX()+diffX, oldPos.getY()+diffY));
		}
	}*/

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

	public Dye getColor() {
		return color;
	}

	public void setColor(Dye color) {
		this.color = color;
	}

	public Point getRotationCenter() {
		return rotationCenter;
	}
	
	public void setRotationCenter(Point p) {
		this.rotationCenter = p;
	}
	
	public boolean contained(Point min, Point max) {
		Point p = getPosition();
		return ((p.getX() >= min.getX() && p.getY() >= min.getY()) && 
				(p.getX() + getWidth() <= max.getX() && 
				 p.getY() + getHeight() <= max.getY()));
	}

}
