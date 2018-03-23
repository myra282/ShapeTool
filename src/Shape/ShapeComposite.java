package Shape;

import java.util.Vector;

import GraphicLibrary.Point;

public class ShapeComposite extends AbstractShape {
	
	private Vector<IShape> shapes;

	public ShapeComposite() {
		shapes = new Vector<IShape>();
	}
	
	public void add(IShape s) {
		shapes.add(s);
	}
	
	public IShape remove(IShape s) {
		int id = shapes.indexOf(s);
		return shapes.remove(id);
	}

	@Override
	public void attributeEditorCreate() {
		
	}

}
