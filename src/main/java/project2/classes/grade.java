package project2.classes;

public class grade {
    private int id;
    private int userId;
    private int courseId;
    private int assignmentId;
    private Double grade; 

    public grade(int id, int userId, int courseId, int assignmentId, Double grade) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.assignmentId = assignmentId;
        this.grade = grade;
    }
    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public int getCourseId() {
        return courseId;
    }
    public int getAssignmentId() {
        return assignmentId;
    }
    public Double getGrade() {
        return grade;
    }
    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
