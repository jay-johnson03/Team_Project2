package project2.classes;

public class Grade {
    private int id;
    private int courseId;
    private int userId;
    private String name;
    private Double grade; 

    public Grade(int id, int courseId, int userId, String name, Double grade) {
        this.id = id;
        this.courseId = courseId;
        this.userId = userId;
        this.name = name;
        this.grade = grade;
    }
    public int getId() {
        return id;
    }
    public int getCourseId() {
        return courseId;
    }
    public int getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public Double getGrade() {
        return grade;
    }
    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
