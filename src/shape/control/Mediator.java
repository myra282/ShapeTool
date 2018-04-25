package shape.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.ListIterator;
import java.util.Vector;

import javafx.application.Application;
import shape.actions.ActionJournal;
import shape.actions.CommandAdd;
import shape.actions.CommandChange;
import shape.actions.CommandGroup;
import shape.actions.CommandMove;
import shape.actions.CommandRemove;
import shape.actions.CommandRemoveAll;
import shape.actions.CommandRoundCorners;
import shape.actions.CommandUngroup;
import shape.actions.ICommand;
import shape.graphicapplication.ApplicationFx;
import shape.graphicapplication.IApplication;
import shape.model.Color;
import shape.model.IShapeSimple;
import shape.model.Point;
import shape.model.Rectangle;
import shape.model.RegularPolygon;
import shape.model.ShapeComposite;
import shape.model.ShapeMemento;

public class Mediator {
	
	private Vector<IShapeSimple> tools;
	private Vector<IShapeSimple> shapes;
	private Vector<Integer> selected;
	
	private ActionJournal journal;
	private IApplication view;
	private static Mediator controller = new Mediator();
	
	private Mediator() {
		tools = new Vector<IShapeSimple>();
		shapes = new Vector<IShapeSimple>();
		selected = new Vector<Integer>();
		journal = new ActionJournal();
		
		/*Thread t1 = new Thread() {
            @Override
            public void run() {
            	Application.launch(ApplicationFx.class);
            }
        };
        t1.start();
        while (!t1.isAlive() || ApplicationFx.getInstance() == null) {
        	// Wait for initialisation
        }*/
        //begin();
	}
	
	public static Mediator getInstance() {
		return controller;
	}
	
	public void setIApplication(IApplication app) {
		view = app;
	}
	
	public void begin() {
		//view = ApplicationFx.getInstance();
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
	
	public ListIterator<IShapeSimple> shapeIterator(int index) {
		return shapes.listIterator(index);
	}
	
	public ListIterator<IShapeSimple> toolsIterator(int index) {
		return tools.listIterator(index);
	}
	
	public boolean isInBoard(IShapeSimple s) {
		Point min = new Point(0, 0);
		Point max = new Point(IApplication.BOARD_WIDTH, IApplication.BOARD_HEIGHT);
		return s.isInside(min, max);
	}
	
	public boolean isInToolbar(IShapeSimple s) {
		Point min = new Point(0, 0);
		Point max = new Point(IApplication.BAR_MAX_WIDTH, IApplication.BOARD_HEIGHT);
		return s.isInside(min, max);
	}
	
	public void eraseAll() {
		ICommand cmd = new CommandRemoveAll(shapes);
		cmd.execute();
    	journal.add(cmd);
		redraw();
	}
	
	public void select(Point p) {
		selected.removeAllElements();
		for (ListIterator<IShapeSimple> i = shapeIterator(shapes.size()); i.hasPrevious();) {
		    IShapeSimple item = i.previous();
		    if (item.contains(p)) {
		    	selected.add(shapes.indexOf(item));
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
		for (ListIterator<IShapeSimple> i = shapeIterator(0); i.hasNext();) {
		    IShapeSimple item = i.next();
		    if (item.isInside(new Point(minx, miny), new Point(maxx, maxy))) {
		    	selected.add(shapes.indexOf(item));
		    }
		}
	}
	
	public void group() {
		if (selected.size() > 1) {
			ICommand cmd = new CommandGroup(shapes, selected);
			cmd.execute();
	    	journal.add(cmd);
			selected.removeAllElements();
			redraw();
		}
	}
	
	public void ungroup() {
		IShapeSimple s = shapes.get(selected.get(0));
		if (selected.size() == 1 && s instanceof ShapeComposite) {
			ICommand cmd = new CommandUngroup(shapes, (ShapeComposite) s);
			cmd.execute();
	    	journal.add(cmd);
			selected.removeAllElements();
			redraw();
		}
	}
	
	public void roundCorners(Rectangle r) {
		ICommand cmd = new CommandRoundCorners(r);
		cmd.execute();
    	journal.add(cmd);
    	redraw();
	}
	
	public void changeAttributes(IShapeSimple s, ShapeMemento mem) {
		ICommand cmd = new CommandChange(s, mem);
		cmd.execute();
    	journal.add(cmd);
    	redraw();
	}
	
	public void redraw() {
		view.clear();
		for (ListIterator<IShapeSimple> i = toolsIterator(0); i.hasNext();) {
		    IShapeSimple item = i.next();
		    view.addTool(item);
		}
		for (ListIterator<IShapeSimple> i = shapeIterator(0); i.hasNext();) {
		    IShapeSimple item = i.next();
		    view.draw(item);
		}
		view.addEvents();
	}
	
	public IShapeSimple getShapeFromPoint(Point p) {
		for (ListIterator<IShapeSimple> i = shapeIterator(shapes.size()); i.hasPrevious();) {
			IShapeSimple item = i.previous();
		    if (item.contains(p)) { //Move
	    		return item;
		    }
		}
		return null;
	}
	
	public IShapeSimple getToolFromPoint(Point p) {
		for (ListIterator<IShapeSimple> i = toolsIterator(tools.size()); i.hasPrevious();) {
			IShapeSimple item = i.previous();
			if (item.contains(p)) {
				return item;
			}
		}
		return null;
	}
	
	private Point computeNewPos(IShapeSimple s, Point p1, Point p2) {
		Point oldPos = s.getPosition();
    	double stepX = p1.getX() - oldPos.getX();
    	double stepY = p1.getY() - oldPos.getY();
    	return new Point(p2.getX()-stepX, p2.getY()-stepY);
	}

	public void handleMouseEvent(Point p1, Point p2, boolean mouseKey) { //mouseKey : true for primary key
		if (mouseKey && p1 != null) {
			IShapeSimple item = getShapeFromPoint(p1);
			if (item != null) {
		    	ICommand cmd = new CommandMove(item, computeNewPos(item, p1, p2));
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
			IShapeSimple item = getShapeFromPoint(p1);
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
			IShapeSimple item = getShapeFromPoint(p1);
			if (item != null) {
				IShapeSimple newTool = item.clone();
		    	Point newPos = computeNewPos(item, p1, p2);
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
			IShapeSimple item = getToolFromPoint(p1);
			if (item != null) {
		    	ICommand cmd = new CommandMove(item, computeNewPos(item, p1, p2));
		    	cmd.execute();
		    	journal.add(cmd);
			}
			redraw();
		}
	}

	public void handleTrashToolEvent(Point p1, boolean mouseKey) {
		if (mouseKey && p1 != null) {
			IShapeSimple item = getToolFromPoint(p1);
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
			IShapeSimple item = getToolFromPoint(p1);
			if (item != null) {
				IShapeSimple shape = item.clone();
		    	shape.setPosition(computeNewPos(item, p1, p2));
		    	if (isInBoard(shape)) {
		    		ICommand cmd = new CommandAdd(shapes, shape);
		    		cmd.execute();
			    	journal.add(cmd);
		    	}
			}
			redraw();
		}
	}
	
	private boolean save(String name, Vector<IShapeSimple> v) {		
		File file =  new File(name) ;
		ObjectOutputStream ostream;
		try {
			ostream = new ObjectOutputStream(new FileOutputStream(file));
			ostream.writeObject(v) ;
			ostream.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void save(String name) {
		boolean warning = !save(name, shapes);
		String message;
		if (warning) {
			message = "An error occured when trying to save !";
		}
		else {
			message = "Your file was correctly saved.";
		}
		view.displayMessage(message, warning);
	}
	
	private boolean load(String name, Vector<IShapeSimple> v) {
		File file =  new File(name) ;
		ObjectInputStream istream;
		try {
			istream = new ObjectInputStream(new FileInputStream(file));
			Vector<IShapeSimple> tmp = (Vector<IShapeSimple>) istream.readObject() ;
			Enumeration<IShapeSimple> e = tmp.elements();
		    while (e.hasMoreElements()) {
		    	v.add(e.nextElement());
		    }
			istream.close();
			redraw();
			return true;
		} catch (IOException | ClassNotFoundException e) {
			return false;
		}
	}
	
	public void load(String name) {
		boolean warning = !load(name, shapes);
		String message;
		if (warning) {
			message = "An error occured when loading !";
		}
		else {
			message = "Your file was correctly loaded.";
		}
		view.displayMessage(message, warning);
	}

}
