package shape.model;

public class RegularPolygon extends AbstractShape {
	
	private int nbEdges;
	private double edgeWidth;
	private double radius;

	public RegularPolygon(Point position, int nbEdges, double edgeWidth) {
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
	
	public double[] computePoints() {
		double points[] = new double[nbEdges * 2];
		double angle = 0;
		double inc = 360 / nbEdges;
		double radius = edgeWidth / (2 * Math.sin(Math.toRadians(180/nbEdges)));
		for (int i = 0 ; i < nbEdges ; ++i) {
			points[2 * i] = (radius * Math.cos(Math.toRadians(angle)));
			points[2 * i + 1] = (radius * Math.sin(Math.toRadians(angle)));
			angle += inc;
		}
		return points;
}

	@Override
	public void scale(double ratio) {
		setEdgeWidth(edgeWidth*ratio);
		this.radius = edgeWidth / (2 * Math.sin(Math.toRadians(180/nbEdges)));
	}

}
