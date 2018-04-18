package shape.control;

import java.util.Vector;

import shape.model.IShape;

public class CommandRemoveAll implements ICommand {
	
	private Vector<IShape> vector;
	private Vector<IShape> backup;

	public CommandRemoveAll(Vector<IShape> vector) {
		this.vector = vector;
		this.backup = new Vector<IShape>();
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
