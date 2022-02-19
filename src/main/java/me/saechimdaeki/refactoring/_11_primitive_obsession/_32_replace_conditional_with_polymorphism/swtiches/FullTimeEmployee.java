package me.saechimdaeki.refactoring._11_primitive_obsession._32_replace_conditional_with_polymorphism.swtiches;

public class FullTimeEmployee extends Employee{
    @Override
    public int vacationHours() {
        return 120;
    }

    @Override
    public boolean canAccessTo(String project) {
        return true;
    }
}
