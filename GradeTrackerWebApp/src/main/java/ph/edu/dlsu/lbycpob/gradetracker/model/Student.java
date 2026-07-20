package ph.edu.dlsu.lbycpob.gradetracker.model;
// ============================================================
// Student.java
// ============================================================
public class Student {

    private String name;
    private String idNumber;
    private double rawGrade;
    private String numericGrade;
    private char   letterRank;
    /** Default constructor (required by Spring Data and Thymeleaf). */
    public Student() { }
    /** Full constructor -- mirrors the original desktop signature. */
    public Student(String name, String idNumber,
                   double rawGrade, String numericGrade, char letterRank) {
        this.name         = name.trim();
        this.idNumber     = idNumber.trim();
        this.rawGrade     = Math.round(rawGrade * 100.0) / 100.0;
        this.numericGrade = numericGrade.trim();
        this.letterRank   = Character.toUpperCase(letterRank);
    }

    // ---- Getters ----
    public String getName()        { return name; }
    public String getIdNumber()    { return idNumber; }
    public double getRawGrade()    { return rawGrade; }
    public String getNumericGrade(){ return numericGrade; }
    public char   getLetterRank()  { return letterRank; }

    // ---- Setters (used by Thymeleaf / tests) ----
    public void setName(String name)               { this.name = name; }
    public void setIdNumber(String idNumber)       { this.idNumber = idNumber; }
    public void setRawGrade(double rawGrade)       { this.rawGrade = rawGrade; }
    public void setNumericGrade(String numericGrade){ this.numericGrade = numericGrade; }
    public void setLetterRank(char letterRank)     { this.letterRank = letterRank; }

    @Override
    public String toString() {
        return "Student{name='" + name + "', id='" + idNumber
                + "', raw=" + rawGrade + ", grade=" + numericGrade
                + ", rank=" + letterRank + '}';
    }
}
//
