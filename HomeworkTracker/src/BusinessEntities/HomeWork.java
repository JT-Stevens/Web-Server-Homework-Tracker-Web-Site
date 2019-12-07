/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessEntities;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Jeremy Stevens
 * @project HomeworkTracker
 * @date 2019/11/13
 */
public class HomeWork {

    private int homeworkId;
    private String homeworkDescription;
    private HomeworkType homeworkType;
    private Date dueDate;
//    private Course course;
    private int courseID;
    private int classID;

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
    
    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkDescription() {
        return homeworkDescription;
    }

    public void setHomeworkDescription(String homeworkDescription) {
        this.homeworkDescription = homeworkDescription;
    }

    public HomeworkType getHomeworkType() {
        return homeworkType;
    }

    public void setHomeworkType(HomeworkType homeworkType) {
        this.homeworkType = homeworkType;
    }

//    public LocalDateTime getDueDate() {
//        return dueDate;
//    }
//
//    public void setDueDate(LocalDateTime dueDate) {
//        this.dueDate = dueDate;
//    }

//    public Course getCourse() {
//        return course;
//    }
//
//    public void setCourse(Course course) {
//        this.course = course;
//    }
    
    
}


