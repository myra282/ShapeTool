package shape.actions;

import java.util.Vector;

import shape.model.IShapeSimple;

public class CommandRemoveAll implements ICommand {
	
	private Vector<IShapeSimple> vector;
	private Vector<IShapeSimple> backup;

	public CommandRemoveAll(Vector<IShapeSimple> vector) {
		this.vector = vector;
		this.backup = new Vector<IShapeSimple>();
	}

	@Override
	public void execute() {
		backup.addAll(vector);
		vector.clear();
	}

	@Override
	public void unexecute() {
		vector.addAll(backup);
	}

}
