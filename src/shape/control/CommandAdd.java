package shape.control;

import java.util.Vector;

import shape.model.IShape;

public class CommandAdd implements ICommand {
	
	private Vector<IShape> vector;
	private IShape shape;

	public CommandAdd(Vector<IShape> vector, IShape shape) {
		this.vector = vector;
		this.shape = shape;
	}

	@Override
	public void execute() {
		vector.add(shape);
	}

	@Override
	public void unexecute() {
		vector.remove(shape);
	}

}
