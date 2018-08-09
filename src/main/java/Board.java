import java.util.*;
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
		Set<Cell> cellsSet = new HashSet<>();
		ArrayList<Cell> adyacentCells = new ArrayList<>();
		if (isPeriodic) {
			int x = (cell.getX() - 1 + m) % m;
			for (int xCounter = 0; xCounter < 3; xCounter++) {
				int y = (cell.getY() - 1 + m) % m;
				for (int yCounter = 0; yCounter < 3; yCounter++) {
					Cell current = cells.get((y + yCounter) % m).get((x + xCounter) % m);
					if (!cellsSet.contains(current)) {
						cellsSet.add(current);
						adyacentCells.add(current);
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
					cell.getParticles().stream().filter(p -> isParticleInteractingWith(particle, p)).forEach(p -> particles.add(p));
				}
		);
		return particles;
	}

	public Collection<Particle> getInteractingParticlesBruteForce(Particle particle) {
		List<Particle> particles = new ArrayList<>();
		cells.stream().forEach(
				list -> list.stream().forEach(
						cell -> {
							cell.getParticles().stream().filter(p -> isParticleInteractingWith(particle, p)).forEach(p -> particles.add(p));
						}
				)
		);
		return particles;
	}

	public boolean isParticleInteractingWith(Particle p1, Particle p2) {
		if (!isPeriodic) {
			return p1.isInteractingWith(p2);
		} else {
			Cell p1Cell = getParticleCell(p1);
			Cell p2Cell = getParticleCell(p2);

			double projectedX = p2.getX();
			double projectedY = p2.getY();

			if (p2Cell.getX() == (p1Cell.getX() + 1) % m && p1Cell.getX() == m - 1) {
				projectedX += l;
			}
			if (p2Cell.getY() == (p1Cell.getY() + 1) % m && p1Cell.getY() == m - 1) {
				projectedY += l;
			}
			if ((p2Cell.getX() + 1) % m == p1Cell.getX() && p1Cell.getX() == 0) {
				projectedX -= l;
			}
			if ((p2Cell.getY() + 1) % m == p1Cell.getY() && p1Cell.getY() == 0) {
				projectedY -= l;
			}

			double projectedDistance = Math.sqrt(Math.pow((p1.getX() - projectedX), 2) + Math.pow((p1.getY() - projectedY), 2));
			double directDistance = Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));

			return Math.min(projectedDistance, directDistance) < p1.getRadius() + p2.getRadius() + p1.getInteractionRadius();
		}
	}

}
