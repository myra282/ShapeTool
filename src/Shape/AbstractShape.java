package Shape;

import GraphicLibrary.Color;

public abstract class AbstractShape implements IShape {
	
	private Point position;
	private double rotation;
	private Point rotationCenter;
	//private int translation
	private Color color;

	public AbstractShape() {
	}

	public AbstractShape(Point position) {
		this.position = position;
		this.color = new Color(0, 0, 0);
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

}
