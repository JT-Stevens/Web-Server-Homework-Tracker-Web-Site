/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAcess;

import BusinessEntities.Course;
import BusinessEntities.HomeWork;
import BusinessEntities.HomeworkType;
import BusinessEntities.Homework_StudentJunc;
import BusinessEntities.Student;
import BusinessEntities.WMADClass;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeremy
 */
public class HomeWorkFillDb {

    private final static Logger ERROR_LOGGER = Logger.getLogger("errors");

    private String url = "";
    private String user = "";
    private String password = "";

    public HomeWorkFillDb() {
        try {
            Properties props = DALHelper.getProperties();

            url = props.getProperty("mysql.url");
            user = props.getProperty("mysql.username");
            password = props.getProperty("mysql.password");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get all due homework based off course.
     *
     * @param courseID course to get homework from
     * @return list of due homework
     */
    public List<HomeWork> getHomeworkByCourse(int courseID) {
        List<HomeWork> homeworks = new ArrayList();

        String sql = "SELECT * FROM homeworktracker.homework WHERE CourseID = " + courseID + ";";

        try {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            HomeWork homework = new HomeWork();

                            homework.setHomeworkId(rs.getInt("HomeworkID"));
                            homework.setHomeworkDescription(rs.getString("HomeworkDesc"));
                            homework.setHomeworkType(HomeworkType.valueOf(rs.getString("HomeworkType").toUpperCase()));
                            homework.setDueDate(rs.getDate("DueDate"));
                            homework.setCourseID(rs.getInt("CourseID"));

                            homeworks.add(homework);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return homeworks;
    }

    /**
     * Get all homework due by class. Will not show homework that is past due.
     *
     * @param classID class to get homework from
     * @return list of homework
     */
    public List<HomeWork> getHomeworkByClass(int classID) {
        List<HomeWork> homeworks = new ArrayList();

        String sql = "SELECT  Course.CourseID, HomeworkDesc, HomeworkType, DueDate FROM Class \n"
                + "INNER JOIN Courses_Class \n"
                + "ON Class.ClassID = Courses_Class.ClassID \n"
                + "INNER JOIN Course\n"
                + "ON Courses_Class.CourseID = Course.CourseID \n"
                + "INNER JOIN Homework\n"
                + "ON Course.CourseID = Homework.CourseID\n"
                + "WHERE Class.ClassID = " + classID + " AND homework.duedate > CURDATE();";

        try {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            HomeWork homework = new HomeWork();

//                            homework.setHomeworkId(rs.getInt("HomeworkID"));
                            homework.setCourseID(rs.getInt("CourseID"));
                            homework.setHomeworkDescription(rs.getString("HomeworkDesc"));
                            homework.setHomeworkType(HomeworkType.valueOf(rs.getString("HomeworkType").toUpperCase()));
                            homework.setDueDate(rs.getDate("DueDate"));

                            homeworks.add(homework);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return homeworks;
    }

    /**
     * Get all course Ids, and Cours eNames
     *
     * @return list of courses
     */
    public List<Course> getAllCoursesIdName() {
        List<Course> courses = new ArrayList();

        String sql = "SELECT CourseID, CourseName FROM Course;";
        try {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        Course course;
                        while (rs.next()) {
                            course = new Course();
                            course.setCourseID(rs.getInt("CourseID"));
                            course.setCourseName(rs.getString("CourseName"));

                            courses.add(course);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return courses;
    }

    /**
     * Get a individual class by id
     *
     * @param classID id of class to get
     * @return class
     */
    public WMADClass getClassByID(int classID) {
        String sql = "SELECT * FROM Class WHERE ClassId = " + classID + ";";
        WMADClass wmadClass = new WMADClass();;

        try {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            wmadClass.setClassID(rs.getInt("ClassID"));
                            wmadClass.setClassName(rs.getString("ClassName"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            ERROR_LOGGER.log(Level.SEVERE, "Problem in databbase: " + e.getMessage(), e);
        }

        return wmadClass;
    }

    /**
     * Retrieve a student based off Student ID
     * @param studentID the student to search for
     * @return Student
     */
    public Student getStudentByID(int studentID) {
        String sql = "SELECT * FROM Student WHERE StudentID = " + studentID + ";";
        Student student = new Student();

        try {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            student.setStudentID(rs.getInt("StudentID"));
                            student.setClassID(rs.getInt("ClassID"));
                            student.setName(rs.getString("Name"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            ERROR_LOGGER.log(Level.SEVERE, "Problem in databbase: " + e.getMessage(), e);
        }

        return student;
    }

//    public List<Homework_StudentJunc> getHomeworkByStudent(int studentID){
//                List<HomeWork> homeworks = new ArrayList();
//
//                
//        String sql = "SELECT * FROM homeworktracker.homework WHERE CourseID = " + studentID + ";";
//
//        try {
//            try (Connection conn = DriverManager.getConnection(url, user, password)) {
//                try (Statement st = conn.createStatement()) {
//                    try (ResultSet rs = st.executeQuery(sql)) {
//                        while (rs.next()) {
//                            HomeWork homework = new HomeWork();
//
//                            homework.setHomeworkId(rs.getInt("HomeworkID"));
//                            homework.setHomeworkDescription(rs.getString("HomeworkDesc"));
//                            homework.setHomeworkType(HomeworkType.valueOf(rs.getString("HomeworkType").toUpperCase()));
//                            homework.setDueDate(rs.getDate("DueDate"));
//                            homework.setCourseID(rs.getInt("CourseID"));
//
//                            homeworks.add(homework);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        return homeworks;
//    }
    
    /**
     * Get all students from the same class
     *
     * @param classID the class of students
     * @return list of students
     */
    public List<Student> getAllStudentsByClass(int classID) {
        String sql = "SELECT * FROM Student WHERE ClassID = " + classID + ";";
        List<Student> students = new ArrayList();
        try {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        Student student;
                        while (rs.next()) {
                            student = new Student();
                            student.setStudentID(rs.getInt("StudentID"));
                            student.setName(rs.getString("Name"));
                            student.setClassID(rs.getInt("ClassID"));

                            students.add(student);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ERROR_LOGGER.log(Level.SEVERE, "Problem in databbase: " + e.getMessage(), e);
        }
        return students;
    }

    /**
     * Get all classes
     *
     * @return all WMAD classes
     */
    public List<WMADClass> getAllClasses() {
        String sql = "SELECT * FROM Class";
        List<WMADClass> wmadClasses = new ArrayList();
        try {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        WMADClass wmadClass;
                        while (rs.next()) {
                            wmadClass = new WMADClass();
                            wmadClass.setClassID(rs.getInt("ClassID"));
                            wmadClass.setClassName(rs.getString("ClassName"));

                            wmadClasses.add(wmadClass);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ERROR_LOGGER.log(Level.SEVERE, "Problem in databbase: " + e.getMessage(), e);
        }
        return wmadClasses;
    }

    /**
     * Get list of classes by course
     *
     * @param CourseID course to filter classes by
     * @return list of classes
     */
    public List<WMADClass> getClassesByCourse(int CourseID) {
        String sql = "SELECT Class.ClassID, Class.ClassName FROM Courses_class\n"
                + "INNER JOIN Class \n"
                + "ON Courses_class.ClassID = Class.ClassID\n"
                + " WHERE Courses_class.CourseID = " + CourseID + ";";
        List<WMADClass> wmadClasses = new ArrayList();
        try {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        WMADClass wmadClass;
                        while (rs.next()) {
                            wmadClass = new WMADClass();
                            wmadClass.setClassID(rs.getInt("ClassID"));
                            wmadClass.setClassName(rs.getString("ClassName"));

                            wmadClasses.add(wmadClass);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ERROR_LOGGER.log(Level.SEVERE, "Problem in databbase: " + e.getMessage(), e);
        }
        return wmadClasses;
    }

    /**
     * Inserts a new assignment into db
     *
     * @param hw the assignment to insert
     * @return success or failure of insertion.
     */
    public boolean insertAssignment(HomeWork hw) {
        boolean result = false;
        try {
            String sql = "INSERT INTO `homeworktracker`.`homework`\n"
                    + "(`HomeworkDesc`, `HomeworkType`, `DueDate`, `CourseID`, `ClassID`)\n"
                    + "VALUES (?, ?, ?, ?, ?);";

            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, hw.getHomeworkDescription());
                    ps.setString(2, hw.getHomeworkType().name());
                    ps.setDate(3, new java.sql.Date(hw.getDueDate().getTime()));
                    ps.setInt(4, hw.getCourseID());
                    ps.setInt(5, hw.getClassID());

                    int recordsAffected = ps.executeUpdate();

                    result = recordsAffected == 1;
                }
            }
        } catch (Exception e) {
            ERROR_LOGGER.log(Level.SEVERE, "Problem in databbase: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Insert new student into the system
     *
     * @param student student to insert
     * @return success or failure of insertion
     */
    public boolean insertStudent(Student student) {
        boolean result = false;
        try {
            String sql = "INSERT INTO `homeworktracker`.`student`\n"
                    + "( `ClassID`, `Name`)\n"
                    + "VALUES (?, ? );";

            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, student.getClassID());
                    ps.setString(2, student.getName());

                    int recordsAffected = ps.executeUpdate();

                    result = recordsAffected == 1;
                }
            }
        } catch (Exception e) {
            ERROR_LOGGER.log(Level.SEVERE, "Problem in databbase: " + e.getMessage(), e);

        }
        return result;
    }
}
