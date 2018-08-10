import java.util.*;
import java.util.stream.Collectors;

public class Board {

	private Map<Integer, Particle> particles;
	private List<List<Cell>> cells;
	private int m;
	private double l;
	private double cellLength;
	private boolean isPeriodic;

	public Board(int m, double l, boolean isPeriodic) {
		this.particles = new HashMap<>();
		generateBoard(m);
		this.m = m;
		this.l = l;
		this.isPeriodic = isPeriodic;
		cellLength = l / m;
	}

	private void generateBoard(int m) {
		this.cells = new ArrayList<>();
		for (int row = 0; row < m; row++) {
			List<Cell> currentRow = new ArrayList<>();
			for (int col = 0; col < m; col++) {
				currentRow.add(new CellImpl(col, row));
			}
			this.cells.add(currentRow);
		}
	}

	public Collection<Cell> getAdyacentCells(Cell cell) {
		ArrayList<Cell> adyacentCells = new ArrayList<>();
		if (isPeriodic) {
			for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
				for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
					if (x >= 0 && x < m && y >= 0 && y < m) {
						adyacentCells.add(cells.get(y).get(x));
					} else {
						Cell ghostCell = new CellImpl(x, y);
						Cell current = cells.get((y + m) % m).get((x + m) % m);
						for (Particle particle : current.getParticles()) {
							double newX = particle.getX() + (x - current.getX()) * (l/m);
							double newY = particle.getY() + (y - current.getY()) * (l/m);
							ghostCell.addParticle(new ParticleImpl(particle.getId(), particle.getRadius(), particle.getInteractionRadius(), newX, newY));
						}
						adyacentCells.add(ghostCell);
					}
				}
			}
		} else {
			for (int x = cell.getX() - 1 < 0 ? 0 : cell.getX() - 1; x <= (cell.getX() + 1 >= m ? m - 1 : cell.getX() + 1); x++) {
				for (int y = cell.getY() - 1 < 0 ? 0 : cell.getY() - 1; y <= (cell.getY() + 1 >= m ? m - 1 : cell.getY() + 1); y++) {
					adyacentCells.add(cells.get(y).get(x));
				}
			}
		}
		return adyacentCells;
	}

	public Cell getCellFromPosition(double x, double y) {
		int cellX = new Double(Math.floor(x / cellLength)).intValue();
		int cellY = new Double(Math.floor(y / cellLength)).intValue();
		return cells.get(cellY).get(cellX);
	}

	public Cell getParticleCell(Particle particle) {
		return getCellFromPosition(particle.getX(), particle.getY());
	}

	public void addParticle(Particle particle) {
		particles.put(particle.getId(), particle);
		getCellFromPosition(particle.getX(), particle.getY()).addParticle(particle);
	}

	public void addParticles(Collection<Particle> particles) {
		particles.stream().forEach(p -> addParticle(p));
	}

	public Collection<Particle> getInteractingParticles(Particle particle) {
		Collection<Cell> adjacent = getAdyacentCells(getParticleCell(particle));
		List<Particle> particles = new ArrayList<>();
		Set<Integer> particleIds = new HashSet<>();

		for (Cell cell: adjacent) {
			for (Particle p: cell.getParticles()) {
				if (!particleIds.contains(p.getId())) {
					if (particle.isInteractingWith(p)) {
						particleIds.add(p.getId());
						particles.add(this.particles.get(p.getId()));
					}
				}
			}
		}

//		adjacent.forEach(
//				cell -> {
//					cell.getParticles().stream().filter(particle::isInteractingWith).forEach( p -> particles.add(this.particles.get(p.getId())));
//				}
//		);
		return particles;
	}

//	public Collection<Particle> getInteractingParticlesBruteForce(Particle particle) {
//		List<Particle> interactingParticles = new LinkedList<>();
//		particles.values().forEach(p -> {
//			if (particle.isInteractingWith(p)) {
//				interactingParticles.add(this.particles.get(p.getId()));
//			}
//		});
//		return interactingParticles;
//	}

}
