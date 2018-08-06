import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CellImpl implements Cell {

    private List<Particle> particles;
    private int x;
    private int y;

    public CellImpl(int x, int y) {
        this.particles = new ArrayList<>();
        this.x = x;
        this.y = y;
    }

    public Collection<Particle> getParticles() {
        return particles;
    }

	public void addParticle(Particle particle) {
		this.particles.add(particle);
	}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
