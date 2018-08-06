
public class ParticleImpl implements Particle{

	private int id;
    private double radius;
    private double interactionRadius;
    private double x;
    private double y;

    public ParticleImpl(int id, double radius, double interactionRadius, double x, double y) {
    	this.id = id;
        this.radius = radius;
        this.interactionRadius = interactionRadius;
        this.x = x;
        this.y = y;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return id + " " + x + " " + y + " " + radius + " " + interactionRadius;
	}
}
