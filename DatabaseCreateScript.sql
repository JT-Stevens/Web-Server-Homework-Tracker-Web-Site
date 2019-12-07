
CREATE DATABASE HomeWorkTracker;

USE HomeWorkTracker;

CREATE TABLE Class
(
 ClassID int NOT NULL AUTO_INCREMENT,
 ClassName varchar(10) NOT NULL,
 PRIMARY KEY(ClassId)
);

CREATE TABLE Student 
(
	StudentID int NOT NULL AUTO_INCREMENT,
    ClassID int NOT NULL,
    Name varchar(25) NOT NULL,
    PRIMARY KEY(StudentID),
    FOREIGN KEY(ClassID) REFERENCES Class(ClassID)
);

CREATE TABLE Course
(
	CourseID int NOT NULL AUTO_INCREMENT,
    CourseName varchar(30) NOT NULL,
    StartDate date NOT NULL,
    EndDate date NOT NULL,
    PRIMARY KEY(CourseID)
);

CREATE TABLE HomeWork
(
	HomeworkID int NOT NULL AUTO_INCREMENT,
    HomeworkDesc varchar(70) NOT NULL,
    HomeworkType enum('Assignment', 'Homework', 'Test')  NOT NULL,
    DueDate datetime,
    CourseID int NOT NULL,
    ClassID int NOT NULL,
    PRIMARY KEY(HomeworkID),
    FOREIGN KEY(CourseID) REFERENCES Course(CourseID),
    FOREIGN KEY(ClassID) REFERENCES Class(ClassID)
);
-- ALTER TABLE homeworktracker.homework ADD COLUMN CourseID int NOT NULL;
-- ALTER TABLE homeworktracker.homework ADD CONSTRAINT FK_CourseId FOREIGN KEY (CourseID) REFERENCES Course(CourseID);

CREATE TABLE Homework_Students
(
	StudentID int NOT NULL,
    HomeworkID int NOT NULL,
    DateCompleted date,
    CONSTRAINT PK_HomeworkStudent PRIMARY KEY
    (
        StudentID,
        HomeworkID
    ),
	FOREIGN KEY(StudentID) REFERENCES Student(StudentID),
    FOREIGN KEY(HomeworkID) REFERENCES Homework(HomeworkID)
);
-- ALTER TABLE homeworktracker.Homework_Students ADD COLUMN DateCompleted date AFTER StudentId; 

CREATE TABLE Course_Student
(
    CourseID int NOT NULL,
    StudentID int NOT NULL,
    CONSTRAINT PK_CourseStudent PRIMARY KEY
    (
		CourseID,
        StudentID
    ),
	FOREIGN KEY(CourseID) REFERENCES Course(CourseID),
    FOREIGN KEY(StudentID) REFERENCES Student(StudentID)
);

CREATE TABLE Courses_Class
(
	CourseID int NOT NULL,
    ClassID int NOT NULL,
    constraint PK_CoursesClass PRIMARY KEY
    (
    CourseID,
    ClassID
    ),
    FOREIGN KEY(CourseID) REFERENCES Course(CourseID),
    FOREIGN KEY(ClassID) REFERENCES Class(ClassID)
);




