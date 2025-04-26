package project2.project2.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.mindrot.jbcrypt.BCrypt;
import project2.classes.Course;
import project2.classes.User;

public class FileUtil {

  // Paths to database tables
  public static final String USERS_TABLE = "database/users.csv";
  public static final String COURSES_TABLE = "database/courses.csv";
  public static final String GRADES_TABLE = "database/grades.csv";
  public static final String ASSIGNMENTS_TABLE = "database/assignments.csv";
  public static final String ENROLLMENTS_TABLE = "database/enrollments.csv";

  /**
   * Verifies user credentials against the database and logs in if valid.
   * @param email The user's email.
   * @param password The user's password.
   * @param onSuccess Callback for successful login.
   * @param onFailure Callback for failed login.
   */
  public static void logInCheck(
    String email,
    String password,
    Consumer<User> onSuccess,
    Runnable onFailure
  ) {
    String[][] results = select(2, email, USERS_TABLE); // Search for the email in the users table
    if (results.length == 0) {
      onFailure.run(); // No matching email found
      return;
    }

    String[] userData = results[0]; // Get the first matching row
    boolean passwordMatch = BCrypt.checkpw(password, userData[3].trim()); // Verify hashed password

    if (passwordMatch) {
      User user = new User(Integer.parseInt(userData[0]));
      onSuccess.accept(user); // Pass the user object to the success callback
    } else {
      onFailure.run(); // Password does not match
    }
  }

  /**
   * Initializes the database tables if they do not exist.
   */
  public static void initializeTables() {
    try {
      Files.createDirectories(Paths.get("database"));
    } catch (IOException e) {
      System.err.println("Error creating database directory.");
      e.printStackTrace();
    }

    createTable(USERS_TABLE, "Id,Name,Email,Password,IsProfessor\n");
    createTable(COURSES_TABLE, "Course_Id,Course_Name,Professor_Id\n");
    createTable(
      ASSIGNMENTS_TABLE,
      "Assignment_Id,Course_Id,User_Id,Assignment_Name,Grade\n"
    );
    createTable(ENROLLMENTS_TABLE, "Enrollment_Id,Course_Id,User_Id\n");
  }

  /**
   * Creates a table with the specified header if it does not exist.
   * @param tablePath The path to the table file.
   * @param header The header row for the table.
   */
  private static void createTable(String tablePath, String header) {
    if (!Files.exists(Paths.get(tablePath))) {
      try (FileWriter writer = new FileWriter(tablePath)) {
        writer.write(header);
      } catch (IOException e) {
        System.err.println("Error creating table: " + tablePath);
        e.printStackTrace();
      }
    }
  }

  /**
   * Creates a new user account, ensuring no duplicate emails.
   * @param name The user's name.
   * @param email The user's email.
   * @param password The user's password.
   * @param isProfessor Whether the user is a professor.
   */
  public static void createAccount(
    String name,
    String email,
    String password,
    boolean isProfessor
  ) {
    if (!Files.exists(Paths.get(USERS_TABLE))) {
      initializeTables();
    }

    try (
      BufferedReader reader = new BufferedReader(new FileReader(USERS_TABLE))
    ) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data[2].equals(email)) {
          System.err.println("Email already exists: " + email);
          return;
        }
      }
    } catch (IOException e) {
      System.err.println("Error checking for duplicate email.");
      e.printStackTrace();
    }

    String hashedPassword = HashingUtil.hashPassword(password);
    insert(
      new String[] { name, email, hashedPassword, String.valueOf(isProfessor) },
      USERS_TABLE
    );
  }

  /**
   * Inserts a new row into the specified table.
   * @param data The data to insert.
   * @param table The table to insert into.
   */
  public static void insert(String[] data, String table) {
    try (FileWriter writer = new FileWriter(table, true)) {
      int id = getNextId(table);
      String[] dataWithId = new String[data.length + 1];
      dataWithId[0] = String.valueOf(id);

      System.arraycopy(data, 0, dataWithId, 1, data.length);
      writer.write(String.join(",", dataWithId) + "\n");
    } catch (IOException e) {
      System.err.println("Error inserting data into table: " + table);
      e.printStackTrace();
    }
  }

  /**
   * Deletes a row from the specified table by ID.
   * @param id The ID of the row to delete.
   * @param table The table to delete from.
   * @throws IOException If an I/O error occurs.
   */
  public static void delete(String id, String table) throws IOException {
    String temp = "temp.csv";
    try (
      BufferedReader reader = new BufferedReader(new FileReader(table));
      FileWriter writer = new FileWriter(temp)
    ) {
      String header = reader.readLine(); // Read and write the header
      writer.write(header + "\n");

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (!data[0].equals(id)) {
          writer.write(line + "\n");
        }
      }
    }

    replaceFile(temp, table);
  }

  /**
   * Updates a specific column in a row identified by its ID.
   * @param id The ID of the row to update.
   * @param col The column index to update.
   * @param newValue The new value for the column.
   * @param table The table to update.
   * @throws IOException If an I/O error occurs.
   */
  public static void update(String id, int col, String newValue, String table)
    throws IOException {
    String temp = "temp.csv";
    try (
      BufferedReader reader = new BufferedReader(new FileReader(table));
      FileWriter writer = new FileWriter(temp)
    ) {
      String header = reader.readLine(); // Read and write the header
      writer.write(header + "\n");

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data[0].equals(id)) {
          data[col] = newValue; // Update the specified column
        }
        writer.write(String.join(",", data) + "\n");
      }
    }

    replaceFile(temp, table);
  }

  /**
   * Selects rows from a table based on a column value.
   * @param col The column index to search.
   * @param input The value to search for.
   * @param table The table to search.
   * @return A 2D array of matching rows.
   */
  public static String[][] select(int col, String input, String table) {
    List<String[]> resultsList = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(table))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data[col].trim().equals(input.trim())) {
          resultsList.add(data);
        }
      }
    } catch (IOException e) {
      System.err.println("Error selecting data from table: " + table);
      e.printStackTrace();
    }
    return resultsList.toArray(new String[0][]);
  }

  /**
   * Retrieves the next available ID for a table.
   * @param table The table to retrieve the ID from.
   * @return The next available ID.
   */
  public static int getNextId(String table) {
    int id = 0;
    try (BufferedReader reader = new BufferedReader(new FileReader(table))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] row = line.split(",");
        try {
          id = Math.max(id, Integer.parseInt(row[0]));
        } catch (NumberFormatException ignored) {}
      }
    } catch (IOException e) {
      System.err.println("Error retrieving next ID from table: " + table);
      e.printStackTrace();
    }
    return id + 1;
  }

  /**
   * Replaces the original file with a temporary file.
   * @param temp The temporary file path.
   * @param original The original file path.
   * @throws IOException If an I/O error occurs.
   */
  private static void replaceFile(String temp, String original)
    throws IOException {
    try {
      Files.delete(Paths.get(original));
      Files.move(Paths.get(temp), Paths.get(original));
    } catch (IOException e) {
      System.err.println("Error replacing file: " + original);
      throw e;
    }
  }

  // Static list of courses for demonstration purposes
  private static final List<Course> courses = new ArrayList<>();

  static {
    courses.add(new Course(1, "Programming I", 101));
    courses.add(new Course(2, "Programming II", 102));
    courses.add(new Course(3, "Data Structures", 102));
    courses.add(new Course(4, "Algorithms", 104));
    courses.add(new Course(5, "Computer Architecture", 103));
    courses.add(new Course(6, "Operating Systems", 103));
    courses.add(new Course(7, "Database Systems", 104));
  }

  /**
   * Retrieves all courses.
   * @return A list of all courses.
   */
  public static List<Course> getAllCourses() {
    return new ArrayList<>(courses);
  }

  /**
   * Adds a course to the static list.
   * @param course The course to add.
   */
  public static void addCourse(Course course) {
    courses.add(course);
  }

  /**
   * Removes a course from the static list by ID.
   * @param courseId The ID of the course to remove.
   */
  public static void removeCourseByID(int courseId) {
    courses.removeIf(course -> course.getId() == courseId);
  }
}
