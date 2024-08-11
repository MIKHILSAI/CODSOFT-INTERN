import java.util.*;

class Course {
    private String courseCode;
    private String title;
    private String description;
    private int capacity;
    private int enrolled;

    public Course(String courseCode, String title, String description, int capacity) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.enrolled = 0;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public boolean register() {
        if (enrolled < capacity) {
            enrolled++;
            return true;
        }
        return false;
    }

    public boolean drop() {
        if (enrolled > 0) {
            enrolled--;
            return true;
        }
        return false;
    }

    public String getDetails() {
        return String.format("%s: %s\nDescription: %s\nCapacity: %d\nEnrolled: %d\n",
                             courseCode, title, description, capacity, enrolled);
    }
}

class Student {
    private String studentID;
    private String name;
    private Set<String> registeredCourses;

    public Student(String studentID, String name) {
        this.studentID = studentID;
        this.name = name;
        this.registeredCourses = new HashSet<>();
    }

    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public Set<String> getRegisteredCourses() {
        return registeredCourses;
    }

    public boolean registerCourse(String courseCode) {
        return registeredCourses.add(courseCode);
    }

    public boolean dropCourse(String courseCode) {
        return registeredCourses.remove(courseCode);
    }
}

class CourseDatabase {
    private Map<String, Course> courses;

    public CourseDatabase() {
        this.courses = new HashMap<>();
    }

    public void addCourse(Course course) {
        courses.put(course.getCourseCode(), course);
    }

    public Course getCourse(String courseCode) {
        return courses.get(courseCode);
    }

    public Collection<Course> getAllCourses() {
        return courses.values();
    }

    public void displayCourses() {
        for (Course course : courses.values()) {
            System.out.println(course.getDetails());
        }
    }
}

class StudentDatabase {
    private Map<String, Student> students;

    public StudentDatabase() {
        this.students = new HashMap<>();
    }

    public void addStudent(Student student) {
        students.put(student.getStudentID(), student);
    }

    public Student getStudent(String studentID) {
        return students.get(studentID);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }
}

class CourseRegistrationSystem {
    private CourseDatabase courseDatabase;
    private StudentDatabase studentDatabase;

    public CourseRegistrationSystem(CourseDatabase courseDatabase, StudentDatabase studentDatabase) {
        this.courseDatabase = courseDatabase;
        this.studentDatabase = studentDatabase;
    }

    public void registerStudentForCourse(String studentID, String courseCode) {
        Student student = studentDatabase.getStudent(studentID);
        Course course = courseDatabase.getCourse(courseCode);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        if (course.register()) {
            if (student.registerCourse(courseCode)) {
                System.out.println("Registration successful!");
            } else {
                course.drop();
                System.out.println("Student already registered for this course.");
            }
        } else {
            System.out.println("Course is full.");
        }
    }

    public void removeStudentFromCourse(String studentID, String courseCode) {
        Student student = studentDatabase.getStudent(studentID);
        Course course = courseDatabase.getCourse(courseCode);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        if (student.dropCourse(courseCode)) {
            if (course.drop()) {
                System.out.println("Course dropped successfully.");
            } else {
                student.registerCourse(courseCode);
                System.out.println("Failed to drop course.");
            }
        } else {
            System.out.println("Student not registered for this course.");
        }
    }
}

public class STUDENTCOURSEREGISTRATIONSYSTEM {
    private static Scanner scanner = new Scanner(System.in);
    private static CourseDatabase courseDatabase = new CourseDatabase();
    private static StudentDatabase studentDatabase = new StudentDatabase();
    private static CourseRegistrationSystem registrationSystem = new CourseRegistrationSystem(courseDatabase, studentDatabase);

    public static void main(String[] args) {
        initializeData();
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            handleMenuChoice(choice);
        }
    }

    private static void initializeData() {
        courseDatabase.addCourse(new Course("CS101", "Introduction to Computer Science", "Learn the basics of computer science.", 30));
        courseDatabase.addCourse(new Course("MATH101", "Calculus I", "Introduction to calculus.", 25));

        studentDatabase.addStudent(new Student("S001", "Alice"));
        studentDatabase.addStudent(new Student("S002", "Bob"));
    }

    private static void displayMenu() {
        System.out.println("\n--- Course Registration System ---");
        System.out.println("1. Display Courses");
        System.out.println("2. Register for a Course");
        System.out.println("3. Drop a Course");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                courseDatabase.displayCourses();
                break;
            case 2:
                registerForCourse();
                break;
            case 3:
                dropCourse();
                break;
            case 4:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void registerForCourse() {
        System.out.print("Enter student ID: ");
        String studentID = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();
        registrationSystem.registerStudentForCourse(studentID, courseCode);
    }

    private static void dropCourse() {
        System.out.print("Enter student ID: ");
        String studentID = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();
        registrationSystem.removeStudentFromCourse(studentID, courseCode);
    }
}
