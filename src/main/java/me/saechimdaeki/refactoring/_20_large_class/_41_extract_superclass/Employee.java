package me.saechimdaeki.refactoring._20_large_class._41_extract_superclass;

public class Employee extends Party {

    private Integer id;

    private double monthlyCost;

    public Employee(String name) {
        super(name);
    }

    public double annualCost() {
        return this.monthlyCost * 12;
    }

    @Override
    double monthlyCost() {
        return monthlyCost;
    }

    public Integer getId() {
        return id;
    }


}