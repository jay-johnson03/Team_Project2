package project2.project2.debug.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import project2.project2.utils.FileUtil;

public class selecTest {

  public static void main(String[] args) {
    selecTest test = new selecTest();
    test.setUp();
    test.testSelect();
    test.testSelectMultiple();
  }

  @BeforeEach
  public void setUp() {
    // Initialize the database tables before each test
    FileUtil.initializeTables();
    System.out.println("\nDatabase initialized.");

    // Add test data to the users table
    // Id, Name, Email, Password, IsProfessor
    String[] testStudent = {"testSteve", "stu@email.com", "password", "false"};
    String[] testProfessor = {"testBob", "prof@email.com", "password", "true"};
    FileUtil.insert(testStudent, FileUtil.USERS_TABLE);
    FileUtil.insert(testProfessor, FileUtil.USERS_TABLE);
    System.out.println("Test data added to users table.");

    // Add test data to the courses table
    // Course_Id, Course_Name, Professor_Id
    String[] testCourse1 = {"Course 101", "2"};
    String[] testCourse2 = {"Course 102", "1"};
    FileUtil.insert(testCourse1, FileUtil.COURSES_TABLE);
    FileUtil.insert(testCourse2, FileUtil.COURSES_TABLE);
    System.out.println("Test data added to courses table.");

    // Add test data to the grades table
    // Grade_Id, User_Id, Course_Id, Assignment_Id, Grade
    String[] testGrade1 = {"1", "1", "1", "100"};
    String[] testGrade2 = {"1", "2", "2", "90"};
    FileUtil.insert(testGrade1, FileUtil.GRADES_TABLE);
    FileUtil.insert(testGrade2, FileUtil.GRADES_TABLE);
    System.out.println("Test data added to grades table.");

    // Add test data to the assignments table
    // Assignment_Id, Assignment_Name
    String[] testAssignment1 = {"Assignment 1"};
    String[] testAssignment2 = {"Assignment 2"};
    FileUtil.insert(testAssignment1, FileUtil.ASSIGNMENTS_TABLE);
    FileUtil.insert(testAssignment2, FileUtil.ASSIGNMENTS_TABLE);
    System.out.println("Test data added to assignments table.\n");
  }

  @Test
  public void testSelect() {
    System.out.println("\nRunning testSelect...");
    // Test the select method
    List<String[]> results = FileUtil.select(1, "Course 101", FileUtil.COURSES_TABLE);
    System.out.println("Results: " + results);
    assertEquals(1, results.size(), "Should return one result"); // failure; returning 0
    assertEquals("1", results.get(0)[0], "Course ID should be 1");
    assertEquals("Course 101", results.get(0)[1], "Course name should be Course 101");
    assertEquals("2", results.get(0)[2], "Professor ID should be 2");

    // Test the select method with no results// failure: returning 1
    List<String[]> emptyResults = FileUtil.select(2, "Nonexistent Course", FileUtil.COURSES_TABLE);
    assertTrue(emptyResults.isEmpty(), "Should return no results");
  }

  @Test
  public void testSelectMultiple() {
    System.out.println("\nRunning testSelectMultiple...");
    List<String[]> results = FileUtil.select(1, "1", FileUtil.GRADES_TABLE);
    for (String[] result : results) {
      System.out.println("Result: " + String.join(", ", result));
    }
    assertEquals(2, results.size(), "Should return two results");
    assertEquals("1", results.get(0)[0], "Grade ID should be 1");
    assertEquals("1", results.get(0)[1], "User ID should be 1");
    assertEquals("1", results.get(0)[2], "Course ID should be 1");
    assertEquals("1", results.get(0)[3], "Assignment ID should be 1");
    assertEquals("100", results.get(0)[4], "Grade should be 100");
    assertEquals("2", results.get(1)[0], "Grade ID should be 2");
    assertEquals("1", results.get(1)[1], "User ID should be 1");
    assertEquals("2", results.get(1)[2], "Course ID should be 2");
    assertEquals("2", results.get(1)[3], "Assignment ID should be 2");
    assertEquals("90", results.get(1)[4], "Grade should be 90");;
  }

}
