package shape.model;

public interface IShapeSimple extends Cloneable, IShape {
	
	public IShapeSimple clone();
	
	public IShapeSimple getParent();
	
	public boolean isInside(Point min, Point max);

}
