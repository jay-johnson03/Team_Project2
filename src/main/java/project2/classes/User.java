package project2.classes;

import java.util.ArrayList;
import java.util.List;
import project2.project2.utils.FileUtil;

public class User {
    private int id;
    private String name;
    private String email;
    private Boolean isProfessor;
    private List<Course> courses;
    private List<Grade> grades;

    public User(int id, String name, String email, Boolean isProfessor) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isProfessor = isProfessor;

        if (isProfessor) {
            // get all courses with user id as professor id
            List<Course> coursesTaught = new ArrayList<>();

            System.out.println("getting courses taught by " + name);
            List<String[]> results = FileUtil.select(2, String.valueOf(id), "database/courses.csv");
            System.out.println("results: " + results);

            for (String[] course : results) {
                coursesTaught.add(new Course(Integer.parseInt(course[0]), course[1], Integer.parseInt(course[2])));
            }

            this.courses = coursesTaught;
        } else {
            // get all courses by getting the course id from the grades that has matching
            // user id as well as getting grades along with it
            List<Grade> studentGrades = new ArrayList<>();
            System.out.println("getting courses taken by " + name);
            List<String[]> results = FileUtil.select(1, String.valueOf(id), "database/grades.csv");
            System.out.println("results: " + results);
            for (String[] grade : results) {
                studentGrades.add(new Grade(Integer.parseInt(grade[0]), Integer.parseInt(grade[1]),
                        Integer.parseInt(grade[2]), Integer.parseInt(grade[3]), Double.parseDouble(grade[4])));
            }

            this.grades = studentGrades;

            List<Course> studentCourses = new ArrayList<>();
            for (Grade grade : studentGrades) {
                List<String[]> courseResults = FileUtil.select(0, String.valueOf(grade.getCourseId()), "courses.csv");
                for (String[] course : courseResults) {
                    studentCourses.add(new Course(Integer.parseInt(course[0]), course[1], Integer.parseInt(course[2])));
                }
            }
            this.courses = studentCourses;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsProfessor() {
        return isProfessor;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Grade> getGrades() {
        return grades;
    }

}
/*
 * // addressed these issues, i just want to preserve jays ramblings
 * What needs to be done in this User class
 *
 * Get and Set new variables
 * Make loose methods for later on functions
 * integrate potential grade summaries
 *
 *
 * SDKNASKDBJAFBOUFOJNDFOUND
 * i dont understand what im supposed to be doing lowkey...
 *
 * okay so i make the variables and construcr
 * I ADDED SMTTTT
 * made a hella lot of getters and setters
 * continued to make loose methods for later??
 * LOOOK AT ME COMP SCI-ING?
 * btw i hate the term loose, it sounds gross
 * i thought about making some for the grades class but i think that would be a
 * bit much for now
 *
 * what's next? idfk.
 * maybe i start on the grades class while i let caleb do his nerd shit with the
 * course class?
 *
 */
