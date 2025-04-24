package project2.classes;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import project2.project2.utils.FileUtil;

public class User {

  private int id;
  private String name;
  private String email;
  private Boolean isProfessor;

  private static String[][] courses;
  private String[][] grades;

  public User(int id) {
    String[][] userData = FileUtil.select(0, String.valueOf(id), FileUtil.USERS_TABLE);
    this.id = Integer.parseInt(userData[0][0]);
    this.name = userData[0][1];
    this.email = userData[0][2];
    // this.isProfessor = Boolean.parseBoolean(userData[0][4]);
    this.isProfessor = userData[0][4].trim().equalsIgnoreCase("true") || userData[0][4].trim().equals("1");

    if (isProfessor) {
      this.courses = FileUtil.select(2, String.valueOf(id), FileUtil.COURSES_TABLE);
    } else {
      String[][] enrollments = FileUtil.select(2, String.valueOf(id), FileUtil.ENROLLMENTS_TABLE);

      Set<String> courseIdSet = new HashSet<>();
      for (String[] enrollment : enrollments) {
        courseIdSet.add(enrollment[1]); // courseId is at index 1 in enrollments.csv
      }

      List<String[]> courseList = new java.util.ArrayList<>();
      for (String courseId : courseIdSet) {
        String[][] courseData = FileUtil.select(0, courseId, FileUtil.COURSES_TABLE);
        courseList.add(courseData[0]);
      }
      this.courses = new String[courseList.size()][];
      for (int i = 0; i < courseList.size(); i++) {
        this.courses[i] = courseList.get(i);
      }
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

  public static String[][] getCourses() {
    return courses;
  }

  public String[][] getGrades() {
    grades = FileUtil.select(2, String.valueOf(id), FileUtil.ASSIGNMENTS_TABLE);
    return grades;
  }

}