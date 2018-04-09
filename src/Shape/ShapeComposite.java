package Shape;

import java.util.ListIterator;
import java.util.Vector;

import GraphicLibrary.IObservableShape;

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
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}
	
	public Vector<IShape> getShapes() {
		return shapes;
	}
	
	public void add(IShape s) {
		s.setParent(this);
		shapes.add(s);
		computePosition();
	}
	
	public IShape remove(IShape s) {
		int id = shapes.indexOf(s);
		IShape removed = shapes.remove(id);
		computePosition();
		return removed;
	}
	
	@Override
	public void setPosition(Point p) {
		Point oldPos = getPosition();
		double diffX = p.getX() - oldPos.getX();
		double diffY = p.getY() - oldPos.getY();
		// update each shape position
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    Point pos = item.getPosition();
		    item.setPosition(new Point(pos.getX()+diffX, pos.getY()+diffY));
		}
		super.setPosition(p);
	}
	
	private void computeWidth() {
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
		width = (maxx - minx);
	}
	
	private void computeHeight() {
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
		height = (maxy - miny);
	}
	
	public void computePosition() {
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
		setPosition(p);
	}
	
	@Override
	public void update() {
		// compute new width and height
		computeWidth();
		computeHeight();
		// compute new position
		computePosition();
		// update rotation center
		super.update();
	}

	@Override
	public void attributeEditorCreate() {
		
	}

	@Override
	public void resize(double ratio) {
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    item.resize(ratio);
		}
	}

	@Override
	public void update(IObservableShape s) {
		throw new UnsupportedOperationException();
	}

	public ListIterator<IShape> iterator() {
		return shapes.listIterator();
	}

}
