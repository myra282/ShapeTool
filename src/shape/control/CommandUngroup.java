package shape.control;

import java.util.ListIterator;
import java.util.Vector;

import shape.model.IShape;
import shape.model.ShapeComposite;

public class CommandUngroup implements ICommand {
	
	private Vector<IShape> vector;
	private ShapeComposite selected;
	
	public CommandUngroup(Vector<IShape> vector, ShapeComposite s) {
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
		for (ListIterator<IShape> i = selected.iterator(); i.hasNext();) {
		    IShape item = i.next();
		    group.add(item);
		}
		vector.removeAll(group.getShapes());
		vector.add(group);
	}

}
