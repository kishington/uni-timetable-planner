package ua.com.foxminded.university.planner;

public class Subject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Subject [name=" + name + "]";
    }

}
