package shape.actions;

import java.util.Vector;

public class ActionJournal {
	
	private Vector<ICommand> undoList;
	private Vector<ICommand> redoList;
	
	public ActionJournal() {
		undoList = new Vector<ICommand>();
		redoList = new Vector<ICommand>();
	}
	
	public void add(ICommand c) {
		undoList.add(c);
		redoList.clear();
	}
	
	public boolean canUndo() {
		return (undoList.size() > 0);
	}
	
	public boolean canRedo() {
		return (redoList.size() > 0);
	}
	
	public void undo() {
		//ICommand c = undoList.get(undoList.size()-1);
		ICommand c = undoList.remove(undoList.size()-1);
		c.unexecute();
		redoList.add(c);
	}
	
	public void redo() {
		//ICommand c = redoList.get(undoList.size()-1);
		ICommand c = redoList.remove(redoList.size()-1);
		c.execute();
		undoList.add(c);
	}

}
