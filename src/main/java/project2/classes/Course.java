package project2.classes;

public class Course {
    private int id;
    private String name;
    private String professorId;

    public Course(int id, String name, String professorId) {
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
    public String getProfessorId() {
        return professorId;
    }
}
