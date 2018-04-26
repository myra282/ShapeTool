package shape.actions;

import shape.model.IShapeSimple;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This class defines a rotation operation on a given shape
 * @see ICommand
 * @see IShapeSimple
 * 
 */
public class CommandRotate implements ICommand {
	
	private IShapeSimple shape;
	private double newD;
	private double oldD;

	public CommandRotate(IShapeSimple shape, double newD, double oldD) {
		this.shape = shape;
		this.newD = newD;
		this.oldD = oldD;
	}

	@Override
	public void execute() {
		shape.setRotation(newD);
	}

	@Override
	public void unexecute() {
		shape.setRotation(oldD);
	}

}
