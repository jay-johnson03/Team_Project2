package project2.classes;

public class Course {
    private int id;
    private String name;
    private int professorId;

    public Course(int id, String name, int professorId) {
        this.id = id;
        this.name = name;
        this.professorId = professorId;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getProfessorId() {
        return professorId;
    }
}
