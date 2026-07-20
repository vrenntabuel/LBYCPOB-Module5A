package ph.edu.dlsu.lbycpob.gradetracker.dto;

import jakarta.validation.constraints.*;

// ============================================================
// StudentFormDTO.java  (no desktop equivalent)
//   Mapping to original desktop prompts:
//     name              <- inputName()
//     idNumber          <- inputIdNumber()     (+ IDVerifier check in service)
//     module1..module5  <- inputLabPerformance() loop
//     classParticipation<- inputComponentScore("Class Participation")
//     teacherEvaluation <- inputComponentScore("Teacher's Evaluation")
//     practicalExam     <- inputComponentScore("Practical Exam")
//     project           <- inputComponentScore("Project")
// ============================================================
public class StudentFormDTO {
    // ---- Identity fields ----
    @NotBlank(message = "Student name is required.")
    private String name;

    @NotBlank(message = "ID number is required.")
    @Size(min = 8, max = 8, message = "ID number must be exactly 8 digits.")
    @Pattern(regexp = "\\d{8}", message = "ID number must contain exactly 8 digits.")
    private String idNumber;

    // ---- Lab Performance: 5 module scores (40% of grade) ----
    @DecimalMin(value = "0.0", message = "Module 1 score must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Module 1 score must be 0 - 100.")
    private double module1;

    @DecimalMin(value = "0.0", message = "Module 2 score must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Module 2 score must be 0 - 100.")
    private double module2;

    @DecimalMin(value = "0.0", message = "Module 3 score must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Module 3 score must be 0 - 100.")
    private double module3;

    @DecimalMin(value = "0.0", message = "Module 4 score must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Module 4 score must be 0 - 100.")
    private double module4;

    @DecimalMin(value = "0.0", message = "Module 5 score must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Module 5 score must be 0 - 100.")
    private double module5;

    // ---- Other grade components ----
    @DecimalMin(value = "0.0", message = "Class Participation must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Class Participation must be 0 - 100.")
    private double classParticipation;

    @DecimalMin(value = "0.0", message = "Teacher's Evaluation must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Teacher's Evaluation must be 0 - 100.")
    private double teacherEvaluation;

    @DecimalMin(value = "0.0", message = "Practical Exam score must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Practical Exam score must be 0 - 100.")
    private double practicalExam;

    @DecimalMin(value = "0.0", message = "Project score must be 0 - 100.")
    @DecimalMax(value = "100.0", message = "Project score must be 0 - 100.")
    private double project;

    // ---- Getters and Setters ----
    public String getName()                  { return name; }
    public void   setName(String name)       { this.name = name; }

    public String getIdNumber()              { return idNumber; }
    public void   setIdNumber(String id)     { this.idNumber = id; }

    public double getModule1()               { return module1; }
    public void   setModule1(double v)       { this.module1 = v; }

    public double getModule2()               { return module2; }
    public void   setModule2(double v)       { this.module2 = v; }

    public double getModule3()               { return module3; }
    public void   setModule3(double v)       { this.module3 = v; }

    public double getModule4()               { return module4; }
    public void   setModule4(double v)       { this.module4 = v; }

    public double getModule5()               { return module5; }
    public void   setModule5(double v)       { this.module5 = v; }

    public double getClassParticipation()    { return classParticipation; }
    public void   setClassParticipation(double v) { this.classParticipation = v; }

    public double getTeacherEvaluation()     { return teacherEvaluation; }
    public void   setTeacherEvaluation(double v)  { this.teacherEvaluation = v; }

    public double getPracticalExam()         { return practicalExam; }
    public void   setPracticalExam(double v) { this.practicalExam = v; }

    public double getProject()               { return project; }
    public void   setProject(double v)       { this.project = v; } }

