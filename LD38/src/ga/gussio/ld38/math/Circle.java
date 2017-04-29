package ga.gussio.ld38.math;

public class Circle {
	private double radius;

    private double x;
    private double y;
    
    public Circle(double x, double y, double radius){
    	this.x = x;
    	this.y = y;
    	this.radius = radius;
    }

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getXCenter(){
		return x+radius;
	}
	
	public double getYCenter(){
		return y+radius;
	}
	
	public boolean hasCollision(Circle circle){
		double xDiff = getXCenter() - circle.getXCenter();
	    double yDiff = getYCenter() - circle.getYCenter();

	    double distance = Math.sqrt((Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));

	    return distance < (radius + circle.getRadius());
	}
}
