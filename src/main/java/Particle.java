public interface Particle {

	int getId();
    double getRadius();
    double getX();
    double getY();
	boolean isInteractingWith(Particle other);
}
