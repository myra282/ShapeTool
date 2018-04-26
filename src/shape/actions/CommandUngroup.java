package shape.actions;

import java.util.ListIterator;
import java.util.Vector;

import shape.model.IShapeSimple;
import shape.model.ShapeComposite;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This class defines an "ungroup" operation on a given ShapeComposite
 * @see ICommand
 * @see ShapeComposite
 * 
 */
public class CommandUngroup implements ICommand {
	
	private Vector<IShapeSimple> vector;
	private ShapeComposite selected;
	
	public CommandUngroup(Vector<IShapeSimple> vector, ShapeComposite s) {
		this.vector = vector;
		this.selected = s;
	}

	@Override
	public void execute() {
		vector.remove(selected);
		vector.addAll(selected.getShapes());
	}

	@Override
	public void unexecute() {
		ShapeComposite group = new ShapeComposite();
		for (ListIterator<IShapeSimple> i = selected.iterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    group.add(item);
		}
		vector.removeAll(group.getShapes());
		vector.add(group);
	}

}
