package project2.classes;

import java.util.List;
import project2.project2.utils.FileUtil;

public class User {

  private int id;
  private String name;
  private String email;
  private Boolean isProfessor;
  private List<String[]> courses; // List of courses the user is enrolled in or teaches

  // private List<String[]> grades; // List of grades the user has received

  public User(int id, String name, String email, Boolean isProfessor) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.isProfessor = isProfessor;

    if (isProfessor) {
      fetchCourses(2); // Fetch courses taught by the professor
    } else {
      fetchCourses(1); // Fetch courses enrolled in by the student
    }
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Boolean getIsProfessor() {
    return isProfessor;
  }

  public void fetchCourses(int col) {
    this.courses = FileUtil.select(
      col,
      String.valueOf(this.id),
      FileUtil.COURSES_TABLE
    );
  }
  // public void fetchGrades(int col) {
  //     this.grades = FileUtil.select(col, String.valueOf(this.id), FileUtil.GRADES_TABLE);

  // }

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
