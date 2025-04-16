package project2.classes;

public class course {
    private int id;
    private String name;
    private String professorId;

    public course(int id, String name, String professorId) {
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

    // adding a table to show all the courses for students to enroll in
    

    // adding a table to show all the courses for professors to manage their courses


    /*
     * the goal of this project is to create a course management system that allows
     * students to enroll in courses, professors to manage their courses, and for grades to be posted
     * students can enroll and unenroll in courses (same with professors)
     * professors can add and remove students from their courses
     * students can view their grades and professors can post grades
     * students can view their courses and professors can view their students 
     * 
     * how to implement this:
     * make a table that shows only for professors all the students in the class information
     * make a table that shows only for students all the courses they are enrolled in
     * make a table of the available courses for students to enroll in
     * a button for students to unenroll in a course
     * a button for professors to add and remove students from their courses in the table
     */
}
