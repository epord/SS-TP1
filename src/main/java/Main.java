import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

    	int n = 100;
    	int m = 5;
    	double l = 20.0;
    	boolean isPeriodic = true;

    	double rc = 3.0;
    	double maxParticleRadius = 0.3;

		if (l/m < rc + 2*maxParticleRadius) {
			throw new IllegalStateException("L/M < rc + 2r");
		}

    	Board board = new Board(m, l, isPeriodic);
    	Random random = new Random(4);
    	List<Particle> particles = generateParticles(n, l, rc, maxParticleRadius, random);
    	board.addParticles(particles);

		System.out.println("Estático:");
		System.out.println(n);
		System.out.println(l);

		System.out.println("Dinámico:");
		System.out.println("t0");
		particles.forEach(System.out::println);

		System.out.println("Partículas adyacentes a id=0");
		board.getInteractingParticles(particles.get(0)).forEach(System.out::println);
	}




    public static List<Particle> generateParticles(int n, double l, double rc, double maxParticleRadius, Random random) {
    	List<Particle> particles = new ArrayList<>();
    	for (int i = 0; i < n; i++) {
    		double randX = random.nextDouble() * l;
    		double randY = random.nextDouble() * l;
    		double radius = random.nextDouble() * maxParticleRadius;

    		particles.add(new ParticleImpl(i, radius, rc, randX, randY));
		}
		return particles;
	}

}
