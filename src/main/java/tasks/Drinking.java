package tasks;

public abstract class Drinking {
    private final String drink;

    public Drinking(String drink) {
        this.drink = drink;
    }

    public String getName() {
        return drink;
    }

    public void drinking() {
        System.out.println("I'm drinking " + getName());
    }
}
