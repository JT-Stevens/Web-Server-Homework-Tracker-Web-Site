/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homeworktracker;

import BusinessEntities.Course;
import BusinessEntities.HomeWork;
import BusinessEntities.HomeworkType;
import BusinessEntities.Student;
import BusinessEntities.WMADClass;
import DataAcess.HomeWorkFillDb;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jeremy
 */
public class HtmlViews {

    /**
     * GET Shows Class assignments
     *
     * @param classID id representing the class
     * @return the dynamic Html for the given class
     */
    public String ShowClassAssignments(int classID) {

        HomeWorkFillDb homeworkDb = new HomeWorkFillDb();
        WMADClass wmadClass = homeworkDb.getClassByID(classID);

        StringBuilder htmlBuild = new StringBuilder(addHead("Up coming homework"))
                .append("<style>")
                .append("h1, table { text-align: center; margin: auto; margin-top: 3em} ")
                .append("td, th { border:1px solid #ddd; padding: 1em; } ")
                .append("tr:nth-child(even){ background-color: #f2f2f2; } ")
                .append("tr:hover { background-color: #ddd } ")
                .append("table th { background-color: #3366ff; color: white} ")
                .append("</style>")
                .append("<body>")
                .append(addNavBar())
                .append("<h1>Here's all assignments for ")
                .append(wmadClass.getClassName())
                .append("!</h1>")
                .append("<br /><br /><br /><br />")
                .append("<table style= \"text-align: center;\"><tr>")
                .append("<td>Course Name</td>")
                .append("<td>Description</td>")
                .append("<td>Type</td>")
                .append("<td>Due Date</td></tr>");

        List<Course> courses = homeworkDb.getAllCoursesIdName();

        for (HomeWork hw : homeworkDb.getHomeworkByClass(classID)) {
            Course course = courses.stream()
                    .filter(c -> hw.getCourseID() == c.getCourseID())
                    .findAny()
                    .orElse(null);

            htmlBuild.append("<tr>")
                    .append("<td>")
                    .append(course.getCourseName())
                    .append("</td><td>")
                    .append(hw.getHomeworkDescription())
                    .append("</td><td>")
                    .append(hw.getHomeworkType().toString().toLowerCase())
                    .append("</td><td>")
                    .append(hw.getDueDate())
                    .append("</td>")
                    .append("</tr>");
        }

        htmlBuild.append("</table></body></html>\r\n");

        return htmlBuild.toString();
    }

    /**
     * View of students per class
     *
     * @param classID class to show students of
     * @return html string
     */
    public String showStudentsByClass(int classID) {
        HomeWorkFillDb homeWorkFillDb = new HomeWorkFillDb();

        WMADClass wmadClass = homeWorkFillDb.getClassByID(classID);
        List<Student> students = homeWorkFillDb.getAllStudentsByClass(classID);

        StringBuilder htmlBuild = new StringBuilder(addHead("Students"))
                .append("<style>")
                .append("h1, table { text-align: center; margin: auto; margin-top: 3em} ")
                .append("td, th { border:1px solid #ddd; padding: 1em; } ")
                .append("tr:nth-child(even){ background-color: #f2f2f2; } ")
                .append("tr:hover { background-color: #ddd } ")
                .append("table th { background-color: #3366ff; color: white} ")
                .append("</style>")
                .append("<body>")
                .append(addNavBar())
                .append("<h1>Here's all students from ")
                .append(wmadClass.getClassName())
                .append("!</h1>")
                .append("<br /><br /><br /><br />")
                .append("<table style= \"text-align: center;\"><tr>")
                .append("<td>ID</td>")
                .append("<td>Name</td>");

        for (Student student : students) {
            htmlBuild.append("<tr>")
                    .append("<td>")
                    .append(addAnchor(Integer.toString(student.getStudentID()), Integer.toString(student.getStudentID())))
                    .append("</td><td>")
                    .append(student.getName())
                    .append("</td><tr>");
        }

        htmlBuild.append("</table></body></html>");

        return htmlBuild.toString();
    }

//    public String showAssignmentsByStudent(int studentID) {
//        HomeWorkFillDb homeWorkFillDb = new HomeWorkFillDb();
//        Student student = homeWorkFillDb.getStudentByID(studentID);
//
//         StringBuilder htmlBuild = new StringBuilder(addHead("Students"))
//                .append("<style>")
//                .append("h1, table { text-align: center; margin: auto; margin-top: 3em} ")
//                .append("td, th { border:1px solid #ddd; padding: 1em; } ")
//                .append("tr:nth-child(even){ background-color: #f2f2f2; } ")
//                .append("tr:hover { background-color: #ddd } ")
//                .append("table th { background-color: #3366ff; color: white} ")
//                .append("</style>")
//                .append("<body>")
//                .append(addNavBar())
//                .append("<h1>Here's all homework for ")
//                .append(student.getName())
//                .append("!</h1>")
//                .append("<br /><br /><br /><br />")
//                .append("<table style= \"text-align: center;\"><tr>")
//        .append("<td>Course Name</td>")
//                .append("<td>Description</td>")
//                .append("<td>Type</td>")
//                .append("<td>Due Date</td></tr>");
//
//        List<Course> courses = homeWorkFillDb.getAllCoursesIdName();
//
//        for (HomeWork hw : homeWorkFillDb.get(classID)) {
//            Course course = courses.stream()
//                    .filter(c -> hw.getCourseID() == c.getCourseID())
//                    .findAny()
//                    .orElse(null);
//
//            htmlBuild.append("<tr>")
//                    .append("<td>")
//                    .append(course.getCourseName())
//                    .append("</td><td>")
//                    .append(hw.getHomeworkDescription())
//                    .append("</td><td>")
//                    .append(hw.getHomeworkType().toString().toLowerCase())
//                    .append("</td><td>")
//                    .append(hw.getDueDate())
//                    .append("</td>")
//                    .append("</tr>");
//        }
//
//        htmlBuild.append("</table></body></html>\r\n");
//    }

    /**
     * POST Create an assignment
     *
     * @return the Html for creating an assignment
     */
    public String createAssignment() {
        HomeWorkFillDb homeworkDb = new HomeWorkFillDb();

        StringBuilder htmlBuild = new StringBuilder(addHead("Create an Assignment"))
                .append(addNavBar())
                .append("<body>")
                .append("<br /><br /><br /><br />")
                .append("<form action=\"/createAssignment\" method =\"post\">")
                .append("Assignment Description: <input type=\"text\" name=\"desc\"><br />")
                .append("Homework Type: ")
                .append("<select name= \"homeworkType\">");

        for (HomeworkType hwt : HomeworkType.values()) {
            htmlBuild.append(addHtmlOption(hwt.ordinal(), hwt.name().toLowerCase()));
        }

        htmlBuild.append("</select>")
                .append("<br />")
                .append("DueDate: <input type=\"date\" name=\"due\"><br />")
                .append("Course: ")
                .append("<select name=\"course\">");

        for (Course course : homeworkDb.getAllCoursesIdName()) {
            htmlBuild.append(addHtmlOption(course.getCourseID(), course.getCourseName()));
        }

        htmlBuild.append("</select><br />")
                .append("Class: ")
                .append("<select name=\"class\">");

        for (WMADClass wmadClass : homeworkDb.getAllClasses()) {
            htmlBuild.append(addHtmlOption(wmadClass.getClassID(), wmadClass.getClassName()));
        }

        htmlBuild.append("</select>")
                .append("<br/><br/>")
                .append("<input type=\"submit\" value=\"Submit\"><br />")
                .append("</body></html>");

        return htmlBuild.toString();
    }

    /**
     * POST/GET Create a Student
     *
     * @return the Html for creating a student
     */
    public String createStudent() {
        HomeWorkFillDb homeworkDb = new HomeWorkFillDb();

        StringBuilder htmlBuild = new StringBuilder(addHead("Create Student"))
                .append(addNavBar())
                .append("<body>")
                .append("<br /><br /><br /><br />")
                .append("<form action=\"/CreateStudent\" method =\"post\">")
                .append("Student Name: <input type=\"text\" name=\"name\"><br />")
                .append("Class: ")
                .append("<select name=\"class\">");

        for (WMADClass wmadClass : homeworkDb.getAllClasses()) {
            htmlBuild.append(addHtmlOption(wmadClass.getClassID(), wmadClass.getClassName()));
        }
        htmlBuild.append("</Select>")
                .append("<br/><br/>")
                .append("<input type=\"submit\" value=\"Submit\"><br />")
                .append("</body></html>");
        return htmlBuild.toString();
    }

    private StringBuilder addAnchor(String href, String content) {
        return new StringBuilder("<a href=\"")
                .append(href)
                .append("\">")
                .append(content)
                .append("</a>");
    }

    /**
     * Helper method for added the Html head
     *
     * @param title The title to display for the page
     * @return StringBuilder of head content
     */
    private StringBuilder addHead(String title) {
        return new StringBuilder("<html>")
                .append("<head><title>")
                .append(title)
                .append("</title>")
                .append("<style>")
                .append("body {background-color: #b3cccc;}")
                .append("</Style>")
                .append("</head>");
    }

    /**
     * Helper method for adding navigation bar. Should be same page to page.
     *
     * @return StringBuilder of navigation bar content.
     */
    private StringBuilder addNavBar() {
        StringBuilder htmlBuild = new StringBuilder();
        return htmlBuild.append("<nav>")
                .append("Assignments<br />")
                .append("<a href=\"/Assignments/SrA\">Senior A </a> |")
                .append("<a href=\"/Assignments/SrB\"> Senior B </a> |")
                .append("<a href=\"/Assignments/JrA\"> Junior A </a> |")
                .append("<a href=\"/Assignments/JrB\"> Junior B </a> ")
                .append("<br/>")
                .append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                .append("<br/>")
                .append("Students<br />")
                .append(addAnchor("/Students/SrA", "Senior A |"))
                .append(addAnchor("/Students/SrB", "Senior B |"))
                .append(addAnchor("/Students/JrA", "Junior A |"))
                .append(addAnchor("/Students/JrB", "Junior B "))
                .append("<br/>")
                .append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                .append("<br/>")
                .append("<a href=/CreateAssignment> Create an Assignment</a> |")
                .append("<a href=/CreateStudent> Create a Student</a> |")
                .append("</nav>");
    }

    /**
     * Html helper that will add in a Html option tag
     *
     * @param value html option value
     * @param content html option content
     * @return A single open and closed html option element
     */
    private StringBuilder addHtmlOption(String value, String content) {
        return new StringBuilder("<option value = ")
                .append(value)
                .append(">")
                .append(content)
                .append("</option>");
    }

    /**
     * Html helper that will add in a Html option tag
     *
     * @param value html option value
     * @param content html option content
     * @return A single open and closed html option element
     */
    private StringBuilder addHtmlOption(int value, String content) {
        return new StringBuilder("<option value = ")
                .append(value)
                .append(">")
                .append(content)
                .append("</option>");
    }
}
