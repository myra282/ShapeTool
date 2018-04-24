package shape.actions;

import java.util.Vector;

import shape.model.IShape;

public class CommandRemove implements ICommand {
	
	private Vector<IShape> vector;
	private IShape shape;

	public CommandRemove(Vector<IShape> vector, IShape shape) {
		this.vector = vector;
		this.shape = shape;
	}

	@Override
	public void execute() {
		vector.remove(shape);
	}

	@Override
	public void unexecute() {
		vector.add(shape);
	}

}
