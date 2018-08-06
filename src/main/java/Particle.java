public interface Particle {

    double getRadius();
    double getX();
    double getY();
	boolean isInteractingWith(Particle other);
}
