package project2.project2.debug.test;

import org.junit.jupiter.api.*;
import project2.project2.utils.FileUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileUtilTest {

    public static void main(String[] args) throws IOException {
        FileUtilTest test = new FileUtilTest();
        test.setUp();
        // test.testInitializeTables();
        // test.testCreateAccount();
        // test.testCreateAccountDuplicateEmail();
        // test.testLogInCheckSuccess();
        // test.testLogInCheckFailureWrongPassword();
        // test.testLogInCheckFailureNoAccount();
        test.testInsertAndSelect();
        // test.testDelete();
    }

    @BeforeEach
    public void setUp() {
            FileUtil.initializeTables();
            System.out.println("\nDatabase initialized.");
        }

        @Test
        public void testInitializeTables() {
            System.out.println("\n\nTesting table initialization...");
            assertTrue(Files.exists(Paths.get("database/users.csv")));
            assertTrue(Files.exists(Paths.get("database/courses.csv")));
            assertTrue(Files.exists(Paths.get("database/grades.csv")));
            assertTrue(Files.exists(Paths.get("database/assignments.csv")));
            System.out.println("\nAll tables initialized successfully.");
        }

        @Test
        public void testCreateAccount() {
            System.out.println("\nTesting account creation...");
            FileUtil.createAccount("John Doe", "john@example.com", "password123", false);

            String[][] users = FileUtil.select(2, "john@example.com", "database/users.csv");
            assertEquals(1, users[0]);
            assertEquals("John Doe", users[0][1]);
            assertEquals("john@example.com", users[0][2]);

            System.out.println("\nAccount created successfully.");
        }

        @Test
        public void testCreateAccountDuplicateEmail() {
            System.out.println("\nTesting account creation with duplicate email...");
            FileUtil.createAccount("John Doe", "john@example.com", "password123", false);
            FileUtil.createAccount("Jane Doe", "john@example.com", "password456", false);

            String[][] users = FileUtil.select(2, "john@example.com", "database/users.csv");
            assertEquals(1, users[0]);
            assertEquals("John Doe", users[0][1]);
            System.out.println("\nDuplicate email test passed. Only one account created.");
        }

        @Test
        public void testLogInCheckSuccess() {
            System.out.println("\nTesting successful login...");
            FileUtil.createAccount("John Doe", "john@example.com", "password123", false);

            FileUtil.logInCheck("john@example.com", "password123", 
                name -> assertEquals("John Doe", name), 
                () -> fail("Login should not fail"));
            System.out.println("\nLogin successful.");
        }

        @Test
        public void testLogInCheckFailureWrongPassword() {
            System.out.println("\nTesting login failure with wrong password...");
            FileUtil.createAccount("John Doe", "john@example.com", "password123", false);

            FileUtil.logInCheck("john@example.com", "wrongpassword", 
                name -> fail("Login should fail"), 
                () -> assertTrue(true));
            System.out.println("\nLogin failed as intended.");
        }

        @Test
        public void testLogInCheckFailureNoAccount() {
            System.out.println("\nTesting login failure with no account...");
            FileUtil.logInCheck("nonexistent@example.com", "password123", 
                name -> fail("Login should fail"), 
                () -> assertTrue(true));
            System.out.println("\nLogin failed as intended.");
        }

        @Test
        public void testInsertAndSelect() {
            System.out.println("\nTesting insert and select...");
            FileUtil.insert(new String[]{"Test Course", "Professor A"}, "database/courses.csv");

            String[][] courses = FileUtil.select(1, "Test Course", "database/courses.csv");
            System.out.println("Courses found: " + courses[0][0]);
            assertEquals(1, courses[0]);
            assertEquals("Test Course", courses[0][1]);

            System.out.println("\nInsert and select test passed.");
        }

        @Test
        public void testDelete() throws IOException {
            System.out.println("\nTesting delete...");
            FileUtil.createAccount("John Doe", "john@example.com", "password123", false);

            String[][] usersBefore = FileUtil.select(2, "john@example.com", "database/users.csv");
            assertEquals(1, usersBefore[0]);

            FileUtil.delete(usersBefore[0][0], "database/users.csv");

            String[][] usersAfter = FileUtil.select(2, "john@example.com", "database/users.csv");
            assertTrue(usersAfter.length == 0, "User should be deleted");

            System.out.println("\nDelete test passed.");
        }
    }
