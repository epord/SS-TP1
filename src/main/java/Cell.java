import java.util.Collection;

public interface Cell {

	Collection<Particle> getParticles();

	void addParticle(Particle particle);

	int getX();

	int getY();
}
