package shape.model;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This class is a Memento for the IShapeSimple classes
 * @see IShape
 * @see IShapeSimple
 * @see AbstractShape
 */
public class ShapeMemento implements IShape {
	
	private double d1, d2;
	private Point position;
	private double rotation;
	private Point rotationCenter;
	private Color color;
	private boolean rounded;
	
	public ShapeMemento(double d1, double d2, Point position, double rotation, Point rotationCenter, Color color, boolean rounded) {
		this.d1 = d1;
		this.d2 = d2;
		this.position = position.clone();
		this.rotation = rotation;
		this.rotationCenter = rotationCenter.clone();
		this.color = color.clone();
		this.rounded = rounded;
	}
	
	public double getD1() {
		return d1;
	}

	public double getD2() {
		return d2;
	}

	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public double getRotation() {
		return rotation;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Point getRotationCenter() {
		return rotationCenter;
	}

	public boolean getRounded() {
		return rounded;
	}

}
