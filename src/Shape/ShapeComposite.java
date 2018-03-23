package Shape;

import java.util.Iterator;
import java.util.Vector;

public class ShapeComposite extends AbstractShape {
	
	private Vector<IShape> shapes;

	public ShapeComposite() {
		shapes = new Vector<IShape>();
	}
	
	public void add(IShape s) {
		shapes.add(s);
		updateInfos();
	}
	
	public IShape remove(IShape s) {
		int id = shapes.indexOf(s);
		IShape removed = shapes.remove(id);
		updateInfos();
		return removed;
	}
	
	private void updateInfos() {
		double x = 0;
		double y = 0;
		for (Iterator<IShape> i = shapes.iterator(); i.hasNext();) {
		    IShape item = i.next();
		    Point pos = item.getPosition();
		    if (pos.getX() < x) {
		    	x = pos.getX();
		    }
		    if (pos.getY() < y) {
		    	y = pos.getY();
		    }
		}
		this.setPosition(new Point(x,y));
		
		int size = shapes.size();
		double xres = 0;
		double yres = 0;
		for (Iterator<IShape> i = shapes.iterator(); i.hasNext();) {
		    IShape item = i.next();
			xres += item.getRotationCenter().getX();
			xres += item.getRotationCenter().getY();
		}
		xres /= size;
		yres /= size;
		Point r = new Point(xres, yres);
		this.setRotationCenter(r);
	}

	public Vector<IShape> getShapes() {
		return shapes;
	}

	@Override
	public void attributeEditorCreate() {
		
	}

}
