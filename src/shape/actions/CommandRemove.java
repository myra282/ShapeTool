package shape.actions;

import java.util.Vector;

import shape.model.IShapeSimple;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This class defines a "remove element" operation on a given vector
 * @see ICommand
 * @see IShapeSimple
 * 
 */
public class CommandRemove implements ICommand {
	
	private Vector<IShapeSimple> vector;
	private IShapeSimple shape;

	public CommandRemove(Vector<IShapeSimple> vector, IShapeSimple shape) {
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
