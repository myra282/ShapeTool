package shape.actions;

import java.util.ListIterator;
import java.util.Vector;

import shape.model.IShapeSimple;
import shape.model.ShapeComposite;

public class CommandGroup implements ICommand {
	
	private Vector<IShapeSimple> vector;
	private Vector<Integer> selected;
	private ShapeComposite shape;

	public CommandGroup(Vector<IShapeSimple> vector, Vector<Integer> selected) {
		this.vector = vector;
		this.selected = new Vector<Integer>();
		this.selected.addAll(selected);
	}

	@Override
	public void execute() {
		ShapeComposite group = new ShapeComposite();
		for (ListIterator<Integer> i = selected.listIterator(); i.hasNext();) {
		    int item = i.next();
		    group.add(vector.get(item));
		}
		vector.removeAll(group.getShapes());
		vector.add(group);
		shape = group;
	}

	@Override
	public void unexecute() {
		vector.remove(shape);
		vector.addAll(shape.getShapes());
	}

}
