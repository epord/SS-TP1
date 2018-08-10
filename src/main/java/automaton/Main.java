package automaton;

public class Main {
    public static void main(String[] args) {
        World world = new World(4,5);
        world.setCellState(2,3,State.ALIVE);
        world.setCellState(2,4,State.ALIVE);

        System.out.println(world);
        GOLTransformation transformation = new GOLTransformation();
        for (int i = 0; i < 3; i++) {
            world = transformation.transform(world);
            System.out.println(world);
        }
    }
}
