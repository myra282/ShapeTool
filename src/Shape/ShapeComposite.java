package Shape;

import java.util.Iterator;
import java.util.Vector;

public class ShapeComposite extends AbstractShape {
	
	private double width;
	private double height;
	private Vector<IShape> shapes;

	public ShapeComposite() {
		shapes = new Vector<IShape>();
		width = 0;
		height = 0;
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
		double x = shapes.get(0).getPosition().getX();
		double y = shapes.get(0).getPosition().getY();
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
		
		width = this.computeWidth();
		height = this.computeHeight();
		
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
	
	private double computeWidth() {
		double minx = shapes.get(0).getPosition().getX();
		double maxx = 0;
		double x,w;
		for (Iterator<IShape> i = shapes.iterator(); i.hasNext();) {
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
		for (Iterator<IShape> i = shapes.iterator(); i.hasNext();) {
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
		for (Iterator<IShape> i = shapes.iterator(); i.hasNext();) {
		    IShape item = i.next();
		    item.resize(ratio);
		}
	}

}
