package automaton;

public class World {
    private Cell[][] cells;
    private Integer width;
    private Integer height;
    private Boolean periodic=false;

    public World(Integer width, Integer height){
        cells = new Cell[width][height];
        this.width = width;
        this.height = height;
        fillCellsWith(State.DEAD);
    }

    public Cell getCellAt(Integer x, Integer y){
        if(x >= width || x < 0 || y >= height || y < 0){
            throw new IllegalStateException("Illegal cell index x:" + x + " y:"+y);
        }
        return cells[x][y];
    }

    public void fillCellsWith(State state){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(state);
            }
        }
    }

    public void setCellState(Integer x, Integer y, State state){
        Cell cell = getCellAt(x,y);
        cell.setState(state);
    }

    public Boolean isPeriodic(){
        return periodic;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                sb.append(getCellAt(i,j).getState().toString(), 0, 1);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
