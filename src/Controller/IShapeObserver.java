package Controller;

import java.util.ListIterator;

import Shape.IShape;
import Shape.Point;
import Shape.ShapeComposite;

public class IShapeObserver {

	public void updatePosition(IShape s, Point p) {
		if (s.getParent() != null) {
			updatePosition(s.getParent(), p);
		}
		else {
			Point oldPos = s.getPosition();
			double diffX = p.getX() - oldPos.getX();
			double diffY = p.getY() - oldPos.getY();
			Point newPos = new Point(oldPos.getX()+diffX, oldPos.getY()+diffY);
			s.setPosition(newPos);
			// update each shape position
			for (ListIterator<IShape> i = ((ShapeComposite) s).getShapes().listIterator(); i.hasNext();) {
			    IShape item = i.next();
			    Point pos = item.getPosition();
			    item.setPosition(new Point(pos.getX()+diffX, pos.getY()+diffY));
			}
		}
	}
	
}
