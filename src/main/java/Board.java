import java.util.*;
import java.util.stream.Collectors;

public class Board {

	private Set<Particle> particles;
	private List<List<Cell>> cells;
	private int m;
	private double l;
	private double cellLength;
	private boolean isPeriodic;

	public Board(int m, double l, boolean isPeriodic) {
		this.particles = new HashSet<>();
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
		particles.add(particle);
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
		List<Particle> interactingParticles = new LinkedList<>();
		particles.stream().forEach(p -> {
			if (isParticleInteractingWith(particle, p)) {
				interactingParticles.add(p);
			}
		});
		return interactingParticles;
	}

	public boolean isParticleInteractingWith(Particle p1, Particle p2) {
		if (!isPeriodic) {
			return p1.isInteractingWith(p2);
		} else {
			Cell p1Cell = getParticleCell(p1);
			Cell p2Cell = getParticleCell(p2);

			double projectedX = p2.getX();
			double projectedY = p2.getY();

			int cornerCounter = 0;

			// Si la celda de p1 está en el borde derecho y la p2 se encuentra en una del borde izquierdo
			if (p2Cell.getX() == (p1Cell.getX() + 1) % m && p1Cell.getX() == m - 1) {
				projectedX += l;
				cornerCounter++;
			}
			// Si la celda de p1 está en el borde inferior y la p2 se encuentra en una del borde superior
			if (p2Cell.getY() == (p1Cell.getY() + 1) % m && p1Cell.getY() == m - 1) {
				projectedY += l;
				cornerCounter++;
			}
			// Si la celda de p1 está en el borde izquierdo y la p2 se encuentra en una del borde derecho
			if ((p2Cell.getX() + 1) % m == p1Cell.getX() && p1Cell.getX() == 0) {
				projectedX -= l;
				cornerCounter++;
			}
			// Si la celda de p1 está en el borde superior y la p2 se encuentra en una del borde inferior
			if ((p2Cell.getY() + 1) % m == p1Cell.getY() && p1Cell.getY() == 0) {
				projectedY -= l;
				cornerCounter++;
			}

			double minDistance;
			double directDistance = Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
			if (cornerCounter < 2) {
				double projectedDistance = Math.sqrt(Math.pow((p1.getX() - projectedX), 2) + Math.pow((p1.getY() - projectedY), 2));
				minDistance = Math.min(projectedDistance, directDistance);
			} else {
				List<Double> distances = new LinkedList<>();
				distances.add(directDistance);
				distances.add(Math.sqrt(Math.pow(p1.getX() - projectedX, 2) + Math.pow(p1.getY() - projectedY, 2)));
				distances.add(Math.sqrt(Math.pow(p1.getX() - (projectedX == p2.getX()+l? projectedX-l : projectedX+l), 2) + Math.pow(p1.getY() - projectedY, 2)));
				distances.add(Math.sqrt(Math.pow(p1.getX() - projectedX, 2) + Math.pow(p1.getY() - (projectedY == p2.getY()+l? projectedY-l : projectedY+l), 2)));
				minDistance = distances.stream().min(Double::compare).get();
			}
			return minDistance < p1.getRadius() + p2.getRadius() + p1.getInteractionRadius();
		}
	}

}
