package Shape;

public class RegPoly extends AbstractShape {
	
	private int nbEdges;
	private double edgeWidth;
	private double radius;

	public RegPoly(Point position, int nbEdges, double edgeWidth) {
		super(position);
		this.nbEdges = nbEdges;
		this.edgeWidth = edgeWidth;
		this.radius = edgeWidth / (2 * Math.sin(Math.toRadians(180/nbEdges)));
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

	@Override
	public double getWidth() {
		return 2 * radius;
	}

	@Override
	public double getHeight() {
		return 2 * radius;
	}

	public double getRadius() {
		return radius;
	}

}
