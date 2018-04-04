package Controller;

import Shape.IShape;
import Shape.Point;
import Shape.ShapeComposite;

public class IShapeObserver {

	public void updatePosition(IShape s, Point p) {
		if (s.getParent() != null) {
			updatePosition(((ShapeComposite) s.getParent()), p);
		}
		else {
			s.setPosition(p);
		}
	}
	
	private void updatePosition(ShapeComposite s, Point p) {
		if (s.getParent() != null) {
			updatePosition(s.getParent(), p);
		}
		else {
			s.setPosition(p);
		}
	}
	
}
