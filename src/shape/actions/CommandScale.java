package shape.actions;

import shape.control.Mediator;
import shape.model.IShapeSimple;

public class CommandScale implements ICommand {
	
	private IShapeSimple shape;
	private double newScale;

	public CommandScale(IShapeSimple shape, double scale) {
		this.shape = shape;
		this.newScale = scale;
	}

	@Override
	public void execute() {
		shape.scale(newScale);
		if (!Mediator.getInstance().isInBoard(shape)) {
    		unexecute();
    		Mediator.getInstance().displayMessage("Invalid scale, your shape wouldn't be in board.", true);
    	}
	}

	@Override
	public void unexecute() {
		shape.scale(1/newScale);
	}

}
