import java.util.ArrayList;
import java.util.Collection;

public class CellImpl implements Cell {

    private ArrayList<Particle> particles;
    private int x;
    private int y;

    public CellImpl(ArrayList<Particle> particles, int x, int y) {
        this.particles = particles;
        this.x = x;
        this.y = y;
    }

    public Collection<Particle> getParticles() {
        return particles;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
