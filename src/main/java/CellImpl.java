import java.util.ArrayList;
import java.util.Collection;

public class CellImpl implements Cell {

    private ArrayList<Particle> particles;

    public CellImpl(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    public Collection<Particle> getParticles() {
        return particles;
    }
}
