package shape.actions;

import shape.model.IShapeSimple;
import shape.model.ShapeComposite;
import shape.model.ShapeMemento;
import shape.model.ShapeMementoComposite;

public class CommandChange implements ICommand {
	
	private IShapeSimple shape;
	private ShapeMemento info;
	private ShapeMemento old;

	public CommandChange(IShapeSimple shape, ShapeMemento info) {
		this.shape = shape;
		this.info = info;
		this.old = shape.createMemento();
	}

	@Override
	public void execute() {
		shape.restoreMemento(info);
	}

	@Override
	public void unexecute() {
		shape.restoreMemento(old);
		if (shape instanceof ShapeComposite) {
			((ShapeComposite) shape).restoreMementos((ShapeMementoComposite) old);
		}
	}

}
