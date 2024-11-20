package simulation;

import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.Caterpillar;
import simulation.entity.animal.herbivore.Duck;

public class Main {
    public static void main(String[] args) {
        Field field = new Field();
        Duck duck = new Duck(field, 1, 2);
        Duck duck2 = new Duck(field, 2, 2);
        Caterpillar caterpillar = new Caterpillar(field, 1, 2);
        Caterpillar caterpillar2 = new Caterpillar(field, 5, 4);
        Caterpillar caterpillar3 = new Caterpillar(field, 5, 3);

        System.out.println(field);
        duck2.move(Animal.Direction.LEFT, Animal.Direction.RIGHT, Animal.Direction.DOWN, Animal.Direction.RIGHT);
        duck2.move(Animal.Direction.RIGHT, Animal.Direction.RIGHT, Animal.Direction.RIGHT);
        duck2.move(Animal.Direction.RIGHT, Animal.Direction.RIGHT, Animal.Direction.RIGHT, Animal.Direction.UP);

        System.out.println("--- End of simulation ---");
        System.out.println(field);
    }
}
