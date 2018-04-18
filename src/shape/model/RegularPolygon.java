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
		double inc = 360 / nbEdges;
		double angle = getRotation();
		double radius = edgeWidth / (2 * Math.sin(Math.toRadians(180/nbEdges)));
		for (int i = 0 ; i < nbEdges ; ++i) {
			points[2 * i] = (radius * Math.cos(Math.toRadians(angle))) + radius;
			points[2 * i + 1] = (radius * Math.sin(Math.toRadians(angle))) + radius;
			angle += inc;
		}
		return points;
}

	@Override
	public void scale(double ratio) {
		setEdgeWidth(edgeWidth*ratio);
		this.radius = edgeWidth / (2 * Math.sin(Math.toRadians(180/nbEdges)));
	}
	
	@Override
	public boolean contains(Point p) {
		double[] points = computePoints();
		int i,j; 
		int nvert = points.length;
		boolean res = false;
		Point pos = getPosition();	
		for (i = 0, j = nvert - 2; i < nvert; j = i , i+=2) {
			Point pointi = new Point(points[i]+pos.getX(),points[i+1]+pos.getY());
			Point pointj = new Point(points[j]+pos.getX(),points[j+1]+pos.getY());
			if ((pointi.getY() > p.getY()) != (pointj.getY() > p.getY()) &&
			(p.getX() <= (pointj.getX() - pointi.getX()) * (p.getY() - pointi.getY()) / (pointj.getY() - pointi.getY()) + pointi.getX())) {
				res = !res;
			}
		}
		return res;
	}

}
