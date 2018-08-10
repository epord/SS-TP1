package automaton;

public class GOLTransformation implements Transformation{


    @Override
    public World transform(World world) {
        World constructed = new World(world.getWidth(),world.getHeight());
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                Integer alive = countNeighboursInState(world,i,j,State.ALIVE);

                if(State.ALIVE.equals(world.getCellAt(i,j).getState())){
                    if(alive<2){
                        constructed.setCellState(i,j,State.DEAD);
                    }else if (alive == 2 || alive == 3){
                        constructed.setCellState(i,j,State.ALIVE);
                    } else if (alive > 3){
                        constructed.setCellState(i,j,State.DEAD);
                    }
                } else {
                    if(alive==2){
                        constructed.setCellState(i,j,State.ALIVE);
                    }
                }

            }
        }

        return null;
    }

    private Integer countNeighboursInState(World world, Integer x, Integer y, State state){
        Integer count= 0;
        if(world.isPeriodic()){
            for (int i = (x-1); i < (x+1); i++) {
                for (int j = (y-1); j < (y+1); j++) {
                    if(x != i && y != j){
                        Cell cell = world.getCellAt(i%world.getWidth(),j%world.getHeight());
                        if(state.equals(cell.state)){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }
}
