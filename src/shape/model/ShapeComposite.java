package shape.model;

import java.util.ListIterator;
import java.util.Vector;

public class ShapeComposite extends AbstractShape {
	
	private double width;
	private double height;
	private Vector<IShapeSimple> shapes;

	public ShapeComposite() {
		super();
		shapes = new Vector<IShapeSimple>();
		width = 0;
		height = 0;
	}
	
	@Override
	public IShapeSimple clone() {
		IShapeSimple s = null;
		s = (IShapeSimple) super.clone();
		((ShapeComposite) s).shapes = new Vector<IShapeSimple>();
		for (ListIterator<IShapeSimple> i = iterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
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
	
	public Vector<IShapeSimple> getShapes() {
		return shapes;
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

	public ListIterator<IShapeSimple> iterator() {
		return shapes.listIterator();
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
