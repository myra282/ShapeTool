package shape.control;

import java.util.ListIterator;
import java.util.Vector;

import javafx.application.Application;
import shape.graphicapplication.ApplicationFx;
import shape.graphicapplication.Color;
import shape.graphicapplication.IApplication;
import shape.model.IShape;
import shape.model.Point;
import shape.model.Rectangle;
import shape.model.RegularPolygon;
import shape.model.ShapeComposite;

public class Controller {
	
	private Vector<IShape> tools;
	private Vector<IShape> shapes;
	private Vector<IShape> selected;
	
	private ActionJournal journal;
	private IApplication view;
	private static Controller controller = new Controller();
	
	private Controller() {
		tools = new Vector<IShape>();
		shapes = new Vector<IShape>();
		selected = new Vector<IShape>();
		journal = new ActionJournal();
		
		Thread t1 = new Thread() {
            @Override
            public void run() {
            	Application.launch(ApplicationFx.class);
            }
        };
        t1.start();
        while (!t1.isAlive() || ApplicationFx.getInstance() == null) {
        	// Wait for initialisation
        }
        begin();
	}
	
	public static Controller getInstance() {
		return controller;
	}
	
	public void begin() {
		view = ApplicationFx.getInstance();
		shape.model.Rectangle rect = new Rectangle(new Point(30, 5), 60, 40);
		rect.setColor(new Color(200, 30, 30));
		RegularPolygon poly = new RegularPolygon(new Point(30, 60), 5, 40);
		poly.setColor(new Color(30, 30, 200));
		tools.add(rect);
		tools.add(poly);
		redraw();
	}
	
	public boolean canUndo() {
		return journal.canUndo();
	}
	
	public boolean canRedo() {
		return journal.canRedo();
	}
	
	public void undo() {
		journal.undo();
		redraw();
	}
	
	public void redo() {
		journal.redo();
		redraw();
	}
	
	public boolean isInBoard(IShape s) {
		Point min = new Point(0, 0);
		Point max = new Point(IApplication.BOARD_WIDTH, IApplication.BOARD_HEIGHT);
		return s.isInside(min, max);
	}
	
	public boolean isInToolbar(IShape s) {
		Point min = new Point(0, 0);
		Point max = new Point(IApplication.BAR_MAX_WIDTH, IApplication.BOARD_HEIGHT);
		return s.isInside(min, max);
	}
	
	public void dragNDrop(IShape s, Point p) {
		IShape s2 = s.clone();
		s2.setPosition(p);
		addShape(s2);
		redraw();
	}
	
	public void eraseAll() {
		for (ListIterator<IShape> i = shapeIterator(); i.hasNext();) {
			i.next();
		    i.remove();
		}
		redraw();
	}
	
	public void select(Point p) {
		selected.removeAllElements();
		for (ListIterator<IShape> i = shapeIterator(); i.hasNext();) {
		    IShape item = i.next();
		    if (item.contains(p)) {
		    	selected.add(item);
		    	break;
		    }
		}
	}
	
	public void select(Point p1, Point p2) {
		selected.removeAllElements();
		double minx, miny, maxx, maxy;
		if (p1.getX() < p2.getX()) {
			minx = p1.getX();
			maxx = p2.getX();
		}
		else {
			minx = p2.getX();
			maxx = p1.getX();
		}
		if (p1.getY() < p2.getY()) {
			miny = p1.getY();
			maxy = p2.getY();
		}
		else {
			miny = p2.getY();
			maxy = p1.getY();
		}
		for (ListIterator<IShape> i = shapeIterator(); i.hasNext();) {
		    IShape item = i.next();
		    if (item.isInside(new Point(minx, miny), new Point(maxx, maxy))) {
		    	selected.add(item);
		    }
		}
	}
	
	public void group() {
		if (selected.size() > 1) {
			ShapeComposite group = new ShapeComposite();
			for (ListIterator<IShape> i = selected.listIterator(); i.hasNext();) {
			    IShape item = i.next();
			    rmShape(item);
			    group.add(item);
			}
			addShape(group);
		}
		selected.removeAllElements();
	}
	
	public void ungroup() {
		if (selected.size() == 1) {
			IShape s = selected.get(0);
			if (s instanceof ShapeComposite) {
				rmShape(s);
				for (ListIterator<IShape> i = ((ShapeComposite) s).getShapes().listIterator(); i.hasNext();) {
				    IShape item = i.next();
				    addShape(item);
				    i.remove();
				}
			}
		}
	}
	
	public void redraw() {
		view.clear();
		for (ListIterator<IShape> i = toolsIterator(); i.hasNext();) {
		    IShape item = i.next();
		    view.addTool(item);
		}
		for (ListIterator<IShape> i = shapeIterator(); i.hasNext();) {
		    IShape item = i.next();
		    view.draw(item);
		}
		view.addEvents();
	}
	
	public ListIterator<IShape> shapeIterator() {
		return shapes.listIterator();
	}
	
	public void addShape(IShape s) {
		shapes.add(s);
	}

	public void rmShape(IShape s) {
		shapes.remove(s);
	}
	
	public ListIterator<IShape> toolsIterator() {
		return tools.listIterator();
	}
	
	public IShape getShapeFromPoint(Point p) {
		for (ListIterator<IShape> i = shapeIterator(); i.hasNext();) {
			IShape item = i.next();
		    if (item.contains(p)) { //Move
	    		return item;
		    }
		}
		return null;
	}
	
	public IShape getToolFromPoint(Point p) {
		for (ListIterator<IShape> i = toolsIterator(); i.hasNext();) {
			IShape item = i.next();
			if (item.contains(p)) {
				return item;
			}
		}
		return null;
	}

	public void handleMouseEvent(Point p1, Point p2, boolean mouseKey) { //mouseKey : true for primary key
		if (mouseKey && p1 != null) {
			IShape item = getShapeFromPoint(p1);
			if (item != null) {
				Point oldPos = item.getPosition();
		    	double stepX = p1.getX() - oldPos.getX();
		    	double stepY = p1.getY() - oldPos.getY();
		    	Point newPos = new Point(p2.getX()-stepX, p2.getY()-stepY);
		    	ICommand cmd = new CommandMove(item, newPos);
		    	cmd.execute();
		    	journal.add(cmd);
			}
			else {
				select(p1, p2);
			}
			redraw();
		}
	}
	
	public void handleTrashEvent(Point p1, boolean mouseKey) { //mouseKey : true for primary key
		if (mouseKey && p1 != null) {
			IShape item = getShapeFromPoint(p1);
			if (item != null) {
	    		ICommand cmd = new CommandRemove(shapes, item);
	    		cmd.execute();
		    	journal.add(cmd);
			}
			redraw();
		}
	}
	
	public void handleNewToolEvent(Point p1, Point p2, boolean mouseKey) { //mouseKey : true for primary key
		if (mouseKey && p1 != null) {
			IShape item = getShapeFromPoint(p1);
			if (item != null) {
				IShape newTool = item.clone();
				Point oldPos = newTool.getPosition();
		    	double stepX = p1.getX() - oldPos.getX();
		    	double stepY = p1.getY() - oldPos.getY();
		    	Point newPos = new Point(p2.getX()-stepX, p2.getY()-stepY);
		    	if (newTool.getWidth() > IApplication.BAR_MAX_WIDTH) {
					newTool.scale(0.8 * IApplication.BAR_MAX_WIDTH / newTool.getWidth());
					newPos.setX(0);
				}
		    	newTool.setPosition(newPos);
		    	if (isInToolbar(newTool)) {
		    		ICommand cmd = new CommandAdd(tools, newTool);
		    		cmd.execute();
			    	journal.add(cmd);
		    	}
			}
			redraw();
		}
	}

	public void handleMouseToolEvent(Point p1, Point p2, boolean mouseKey) {
		if (mouseKey && p1 != null) {
			IShape item = getToolFromPoint(p1);
			if (item != null) {
				Point oldPos = item.getPosition();
		    	double stepX = p1.getX() - oldPos.getX();
		    	double stepY = p1.getY() - oldPos.getY();
		    	Point newPos = new Point(p2.getX()-stepX, p2.getY()-stepY);
		    	ICommand cmd = new CommandMove(item, newPos);
		    	cmd.execute();
		    	journal.add(cmd);
			}
			redraw();
		}
	}

	public void handleTrashToolEvent(Point p1, boolean mouseKey) {
		if (mouseKey && p1 != null) {
			IShape item = getToolFromPoint(p1);
			if (item != null) {
	    		ICommand cmd = new CommandRemove(tools, item);
	    		cmd.execute();
		    	journal.add(cmd);
			}
			redraw();
		}
	}

	public void handleDragToolEvent(Point p1, Point p2, boolean mouseKey) {
		if (mouseKey && p1 != null) {
			IShape item = getToolFromPoint(p1);
			if (item != null) {
				IShape shape = item.clone();
				Point oldPos = shape.getPosition();
		    	double stepX = p1.getX() - oldPos.getX();
		    	double stepY = p1.getY() - oldPos.getY();
		    	Point newPos = new Point(p2.getX()-stepX, p2.getY()-stepY);
		    	shape.setPosition(newPos);
		    	if (isInBoard(shape)) {
		    		ICommand cmd = new CommandAdd(shapes, shape);
		    		cmd.execute();
			    	journal.add(cmd);
		    	}
			}
			redraw();
		}
	}

}
