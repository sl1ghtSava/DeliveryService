package model;

public class Package {

    private int id;
    private String description;
    private double weight;

    public Package(int id, String description, double weight) {
        this.id = id;
        this.description = description;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }
}