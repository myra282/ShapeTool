package Shape;

import java.util.ListIterator;
import java.util.Vector;

public class ShapeComposite extends AbstractShape {
	
	private double width;
	private double height;
	private Vector<IShape> shapes;

	public ShapeComposite() {
		super();
		shapes = new Vector<IShape>();
		width = 0;
		height = 0;
	}
	
	@Override
	public void notify(double diffX, double diffY) {
		if (getParent() != null) {
			getParent().notify(diffX, diffY);
		}
		else {
			Point pos = getPosition();
			Point newPos = new Point(pos.getX()+diffX, pos.getY()+diffY);
			update(newPos);
			setPosition(newPos);
		}
	}
	
	public void add(IShape s) {
		s.setParent(this);
		shapes.add(s);
		updatePosition();
	}
	
	public IShape remove(IShape s) {
		int id = shapes.indexOf(s);
		IShape removed = shapes.remove(id);
		updatePosition();
		return removed;
	}
	
	public void updatePosition() {
		// compute new position
		double x = shapes.get(0).getPosition().getX();
		double y = shapes.get(0).getPosition().getY();
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    Point pos = item.getPosition();
		    if (pos.getX() < x) {
		    	x = pos.getX();
		    }
		    if (pos.getY() < y) {
		    	y = pos.getY();
		    }
		}
		Point p = new Point(x,y);
		update(p);
		setPosition(p);
	}
	
	@Override
	public void update(Point newPos) {
		Point oldPos = getPosition();
		// compute new width and height
		width = this.computeWidth();
		height = this.computeHeight();
		// update rotation center
		super.update(newPos);
		// update each shape position
		double diffX = newPos.getX() - oldPos.getX();
		double diffY = newPos.getY() - oldPos.getY();
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    Point pos = item.getPosition();
		    item.setPosition(new Point(pos.getX()+diffX, pos.getY()+diffY));
		}
	}

	public Vector<IShape> getShapes() {
		return shapes;
	}

	@Override
	public void attributeEditorCreate() {
		
	}
	
	private double computeWidth() {
		double minx = shapes.get(0).getPosition().getX();
		double maxx = 0;
		double x,w;
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    x = item.getPosition().getX();
		    w = x + item.getWidth();
		    if (x < minx) {
		    	minx = x;
		    }
		    if (w > maxx) {
		    	maxx = w;
		    }
		}
		return (maxx - minx);
	}
	
	private double computeHeight() {
		double miny = shapes.get(0).getPosition().getY();
		double maxy = 0;
		double y,h;
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    y = item.getPosition().getY();
		    h = y + item.getHeight();
		    if (y < miny) {
		    	miny = y;
		    }
		    if (h > maxy) {
		    	maxy = h;
		    }
		}
		return (maxy - miny);
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void resize(double ratio) {
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    item.resize(ratio);
		}
	}

}
