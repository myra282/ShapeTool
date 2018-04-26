package shape.model;

import java.util.ListIterator;
import java.util.Vector;

public class ShapeComposite extends AbstractShape {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1603757406664688043L;
	private double width;
	private double height;
	private Vector<IShapeSimple> shapes;

	public ShapeComposite() {
		super();
		shapes = new Vector<IShapeSimple>();
		width = 0;
		height = 0;
	}
	
	public ListIterator<IShapeSimple> iterator() {
		return shapes.listIterator();
	}
	
	@Override
	public IShapeSimple clone() {
		IShapeSimple s = null;
		s = (IShapeSimple) (AbstractShape) super.clone();
		((ShapeComposite) s).shapes = new Vector<IShapeSimple>();
		for (ListIterator<IShapeSimple> i = iterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    ((ShapeComposite) s).add(item.clone());
		}
		return s;
	}
	
	@Override
	public void update() {
		computeWidth();
		computeHeight();
		computePosition();
		super.update();
	}
	
	@Override
	public ShapeMemento createMemento() {
		return new ShapeMemento(0, 0, getPosition(), getRotation(), getRotationCenter(), getColor(), getRounded());
	}

	@Override
	public void restoreMemento(ShapeMemento mem) {
		this.setPosition(mem.getPosition().clone());
		this.setRotation(mem.getRotation());
		this.setRotationCenter(mem.getRotationCenter().clone());
		this.setColor(mem.getColor());
	}
	
	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}
	
	public Vector<IShapeSimple> getShapes() {
		return shapes;
	}
	
	@Override
	public void setPosition(Point p) {
		Point oldPos = getPosition();
		double diffX = p.getX() - oldPos.getX();
		double diffY = p.getY() - oldPos.getY();
		// update each shape position
		for (ListIterator<IShapeSimple> i = shapes.listIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    Point pos = item.getPosition();
		    item.setPosition(new Point(pos.getX()+diffX, pos.getY()+diffY));
		}
		super.setPosition(p);
	}
	
	@Override
	public void setColor(Color c) {
		// update each shape color
		for (ListIterator<IShapeSimple> i = shapes.listIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
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
	
	public void add(IShapeSimple s) {
		((AbstractShape) s).setParent(this);
		shapes.add(s);
		update();
	}
	
	public IShapeSimple remove(IShapeSimple s) {
		int id = shapes.indexOf(s);
		IShapeSimple removed = shapes.remove(id);
		update();
		return removed;
	}
	
	private void computeWidth() {
		double minx = shapes.get(0).getPosition().getX();
		double maxx = 0;
		double x,w;
		for (ListIterator<IShapeSimple> i = shapes.listIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
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
		for (ListIterator<IShapeSimple> i = shapes.listIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
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
		for (ListIterator<IShapeSimple> i = shapes.listIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
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
	public void scale(double ratio) {
		for (ListIterator<IShapeSimple> i = shapes.listIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    item.scale(ratio);
		    Point pos = item.getPosition();
		    double diffX = (pos.getX() - getPosition().getX()) * ratio;
		    double diffY = (pos.getY() - getPosition().getY()) * ratio;
		    item.setPosition(new Point(getPosition().getX() + diffX, getPosition().getY() + diffY));
		}
		update();
	}
	
	@Override
	public boolean contains(Point p) {
		boolean res = false;
		for (ListIterator<IShapeSimple> i = shapes.listIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    res |= item.contains(p);
		}
		return res;
	}

}
