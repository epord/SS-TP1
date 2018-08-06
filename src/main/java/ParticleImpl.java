
public class ParticleImpl implements Particle{

    private double radius;
    private double interactionRadius;
    private double x;
    private double y;

    public ParticleImpl(double radius, double interactionRadius, double x, double y) {
        this.radius = radius;
        this.interactionRadius = interactionRadius;
        this.x = x;
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public double getInteractionRadius() {
        return interactionRadius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isInteractingWith(Particle other) {
		double distance = Math.sqrt(Math.pow((this.getX() - other.getX()), 2) + Math.pow((this.getY() - other.getY()), 2));
		return distance < this.getRadius() + other.getRadius() + this.getInteractionRadius();
	}
}
