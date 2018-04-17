package shape.control;

import java.util.Vector;

import shape.model.IShapeSimple;

public class CommandAdd implements ICommand {
	
	private Vector<IShapeSimple> vector;
	private IShapeSimple shape;

	public CommandAdd(Vector<IShapeSimple> vector, IShapeSimple shape) {
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
