SELECT * FROM homeworktracker.course;
SELECT * FROM homeworktracker.homework;

INSERT INTO `homeworktracker`.`class`
(`ClassName`)
VALUES("Senior A");

INSERT INTO `homeworktracker`.`class`
(`ClassName`)
VALUES("Senior B");

INSERT INTO `homeworktracker`.`class`
(`ClassName`)
VALUES("Junior A");

INSERT INTO `homeworktracker`.`class`
(`ClassName`)
VALUES("Junior B");

INSERT INTO `homeworktracker`.`course`
(`CourseName`, `StartDate`, `EndDate`)
VALUES ('Java', '2019/09/03', '2019/12/13');

INSERT INTO `homeworktracker`.`course`
(`CourseName`, `StartDate`, `EndDate`)
VALUES ('Server Side Web', '2019/09/03', '2019/12/13');

INSERT INTO `homeworktracker`.`course`
(`CourseName`, `StartDate`, `EndDate`)
VALUES ('N-Tier', '2019/09/03', '2019/12/13');

INSERT INTO `homeworktracker`.`homework`
(`HomeworkDesc`, `HomeworkType`, `DueDate`, `CourseID`, `ClassID`)
VALUES ('Assignment 1', 1, '2019/11/15', 1, 1);

INSERT INTO `homeworktracker`.`homework`
(`HomeworkDesc`, `HomeworkType`, `DueDate`, `CourseID`, `ClassID`)
VALUES ('Assignment 2', 2, '2019/11/22', 1, 1);

INSERT INTO `homeworktracker`.`homework`
(`HomeworkDesc`, `HomeworkType`, `DueDate`, `CourseID`, `ClassID`)
VALUES ('Project 1', 2, '2019/12/3', 2, 1);

INSERT INTO `homeworktracker`.`homework`
(`HomeworkDesc`, `HomeworkType`, `DueDate`, `CourseID`, `ClassID`)
VALUES ('Project 2', 1, '2019/12/13', 2, 1);

INSERT INTO `homeworktracker`.`homework`
(`HomeworkDesc`, `HomeworkType`, `DueDate`, `CourseID`, `ClassID`)
VALUES ('Assignment 3', 2, '2019/12/13', 1, 1);

INSERT INTO `homeworktracker`.`homework`
(`HomeworkDesc`, `HomeworkType`, `DueDate`, `CourseID`, `ClassID`)
VALUES ('Homework 1', 1, '2019/12/12', 3, 1);

INSERT INTO `homeworktracker`.`homework`
(`HomeworkDesc`, `HomeworkType`, `DueDate`, `CourseID`, `ClassID`)
VALUES ('Assignment 1', 2, '2019/12/12', 3, 1);

INSERT INTO `homeworktracker`.`student`
( `ClassID`, `Name`)
VALUES (2, 'Jeremy Stevens' );

INSERT INTO `homeworktracker`.`courses_class`
(`CourseID`, `ClassID`)
VALUES(1, 1);

INSERT INTO `homeworktracker`.`courses_class`
(`CourseID`, `ClassID`)
VALUES(1, 2);

INSERT INTO `homeworktracker`.`courses_class`
(`CourseID`, `ClassID`)
VALUES(2, 1);

INSERT INTO `homeworktracker`.`courses_class`
(`CourseID`, `ClassID`)
VALUES(3, 1);

select * from homework;
select * from courses_class;

-- Select all homework based off class
SELECT Course.CourseID, HomeworkDesc, HomeworkType, DueDate FROM Class 
INNER JOIN Courses_Class 
ON Class.ClassID = Courses_Class.ClassID 
INNER JOIN Course
ON Courses_Class.CourseID = Course.CourseID 
INNER JOIN Homework
ON Course.CourseID = Homework.CourseID
WHERE Class.ClassID = 1 AND homework.duedate > CURDATE();


SELECT CourseName FROM Course WHERE CourseID = 1;

SELECT Class.ClassID, Class.ClassName FROM Courses_class
INNER JOIN Class 
ON Courses_class.ClassID = Class.ClassID
 WHERE Courses_class.CourseID = 1;
 
 SELECT * FROM Student WHERE ClassID = 1;

SELECT CourseID, CourseName FROM Course;

SELECT * FROM Student;

SELECT * FROM Class WHERE ClassId = 2;

SELECT * FROM homework;

SELECT Homework.HomeworkID, DateCompleted, HomeworkDesc, HomeworkType, DueDate, CourseID, ClassID FROM homework_students
INNER JOIN homework
ON homework_students.HomeworkID = homework.HomeworkID;
