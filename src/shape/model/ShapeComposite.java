package shape.model;

import java.util.ListIterator;
import java.util.Vector;

import shape.graphicapplication.Color;

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
	public IShape clone() {
		IShape s = null;
		s = (IShape) (AbstractShape) super.clone();
		((ShapeComposite) s).shapes = new Vector<IShape>();
		for (ListIterator<IShape> i = iterator(); i.hasNext();) {
		    IShape item = i.next();
		    ((ShapeComposite) s).add(item.clone());
		}
		return s;
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
		((AbstractShape) s).setParent(this);
		shapes.add(s);
		update();
	}
	
	public IShape remove(IShape s) {
		int id = shapes.indexOf(s);
		IShape removed = shapes.remove(id);
		update();
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
	
	@Override
	public void setColor(Color c) {
		// update each shape color
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    if (c.equals(getColor())) {
		    	Color itemColor = item.getColor();
		    	itemColor.setAlpha(c.getAlpha());
		    	item.setColor(itemColor);
		    }
		    else {
		    	item.setColor(c);
		    }
		}
		super.setColor(c);
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
		super.setPosition(p);
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
	public void scale(double ratio) {
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    item.scale(ratio);
		    Point pos = item.getPosition();
		    double diffX = (pos.getX() - getPosition().getX()) * ratio;
		    double diffY = (pos.getY() - getPosition().getY()) * ratio;
		    item.setPosition(new Point(getPosition().getX() + diffX, getPosition().getY() + diffY));
		}
		update();
	}

	public ListIterator<IShape> iterator() {
		return shapes.listIterator();
	}
	
	@Override
	public boolean contains(Point p) {
		boolean res = false;
		for (ListIterator<IShape> i = shapes.listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    res |= item.contains(p);
		}
		return res;
	}

}
