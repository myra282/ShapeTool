package Shape;

public class RegularPolygon extends AbstractShape {
	
	private int nbEdges;
	private double edgeWidth;

	public RegularPolygon(Point position, int nbEdges, double edgeWidth) {
		super(position);
		this.nbEdges = nbEdges;
		this.edgeWidth = edgeWidth;
	}

	public int getNbEdges() {
		return nbEdges;
	}

	public void setNbEdges(int nbEdges) {
		this.nbEdges = nbEdges;
	}

	public double getEdgeWidth() {
		return edgeWidth;
	}

	public void setEdgeWidth(double edgeWidth) {
		this.edgeWidth = edgeWidth;
	}

	@Override
	public void attributeEditorCreate() {
		
	}

}
