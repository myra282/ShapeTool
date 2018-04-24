package shape.actions;

import shape.model.IShape;

public class CommandRotate implements ICommand {
	
	private IShape shape;
	private double newD;
	private double oldD;

	public CommandRotate(IShape shape, double newD, double oldD) {
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
