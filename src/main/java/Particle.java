public interface Particle {

	int getId();
    double getRadius();
    double getInteractionRadius();
    double getX();
    double getY();
	boolean isInteractingWith(Particle other);
}
