import java.util.ArrayList;
import java.util.Collection;

public class Board {
    
    private ArrayList<ArrayList<Cell>> cells;
    private int gridSize;
    private double length;
    private double cellSize;
    private boolean isPeriodic;

    public Board(int gridSize, double length, boolean isPeriodic) {
        //TODO create cells
        this.gridSize = gridSize;
        this.length = length;
        this.isPeriodic = isPeriodic;
        cellSize = length / gridSize;
    }


    public Collection<Cell> getAdyacentCells(Cell cell) {
        ArrayList<Cell> adyacentCells = new ArrayList<Cell>();
        if (isPeriodic) {
            for (int x = (cell.getX()-1+gridSize)%gridSize; x <= (cell.getX()+1)%gridSize; x++) {
                for (int y = (cell.getY()-1+gridSize)%gridSize; y <= (cell.getY()+1)%gridSize; y++) {
                    adyacentCells.add(cells.get(y).get(x));
                }
            }
        } else {
            for (int x = cell.getX()-1 < 0 ? 0 : cell.getX()-1; x <= (cell.getX()+1 > gridSize ? gridSize-1 : cell.getX()+1); x++) {
                for (int y = cell.getY()-1 < 0 ? 0 : cell.getY()-1; y <= (cell.getY()+1 > gridSize ? gridSize-1 : cell.getY()+1); y++) {
                    adyacentCells.add(cells.get(y).get(x));
                }
            }
        }
        return adyacentCells;
    }

    public Cell getCellFromPosition(double x, double y) {
        int cellX = new Double(Math.floor(x / cellSize)).intValue();
        int cellY = new Double(Math.floor(y / cellSize)).intValue();
        return cells.get(cellY).get(cellX);
    }

    public Cell getParticleCell(Particle particle) {
        return getCellFromPosition(particle.getX(), particle.getY());
    }

    
}
