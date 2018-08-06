import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

	private List<List<Cell>> cells;
	private int m;
	private double l;
	private double cellLength;
	private boolean isPeriodic;

	public Board(int m, double l, boolean isPeriodic) {
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
		ArrayList<Cell> adyacentCells = new ArrayList<Cell>();
		if (isPeriodic) {
			for (int x = (cell.getX() - 1 + m) % m; x <= (cell.getX() + 1) % m; x++) {
				for (int y = (cell.getY() - 1 + m) % m; y <= (cell.getY() + 1) % m; y++) {
					adyacentCells.add(cells.get(y).get(x));
				}
			}
		} else {
			for (int x = cell.getX() - 1 < 0 ? 0 : cell.getX() - 1; x <= (cell.getX() + 1 > m ? m - 1 : cell.getX() + 1); x++) {
				for (int y = cell.getY() - 1 < 0 ? 0 : cell.getY() - 1; y <= (cell.getY() + 1 > m ? m - 1 : cell.getY() + 1); y++) {
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
		getCellFromPosition(particle.getX(), particle.getY()).addParticle(particle);
	}

	public void addParticles(Collection<Particle> particles) {
		particles.stream().forEach(p -> addParticle(p));
	}

	public Collection<Particle> getInteractingParticles(Particle particle) {
		Collection<Cell> adjacent = getAdyacentCells(getParticleCell(particle));
		List<Particle> particles = new ArrayList<>();
		adjacent.stream().forEach(
				cell -> {
					cell.getParticles().stream().filter(p -> particle.isInteractingWith(p)).forEach(p -> particles.add(p));
				}
		);
		return particles;
	}

	public Collection<Particle> getInteractingParticlesBruteForce(Particle particle) {
		List<Particle> particles = new ArrayList<>();
		cells.stream().forEach(
				list -> list.stream().forEach(
						cell -> {
							cell.getParticles().stream().filter(p -> particle.isInteractingWith(p)).forEach(p -> particles.add(p));
						}
				)
		);
		return particles;
	}

}
