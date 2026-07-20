package ph.edu.dlsu.lbycpob.gradetracker.dto;

// ============================================================
// ClassStatsResult.java
// ============================================================
public class ClassStatsResult {

    private int    totalStudents;
    private String highestName;
    private double highestGrade;
    private char   highestRank;
    private String lowestName;
    private double lowestGrade;
    private char   lowestRank;
    private double classMean;
    private char   meanRank;

    // ---- Getters and setters ----

    public int    getTotalStudents()              { return totalStudents; }
    public void   setTotalStudents(int v)         { this.totalStudents = v; }

    public String getHighestName()                { return highestName; }
    public void   setHighestName(String v)        { this.highestName = v; }

    public double getHighestGrade()               { return highestGrade; }
    public void   setHighestGrade(double v)       { this.highestGrade = v; }

    public char   getHighestRank()                { return highestRank; }
    public void   setHighestRank(char v)          { this.highestRank = v; }

    public String getLowestName()                 { return lowestName; }
    public void   setLowestName(String v)         { this.lowestName = v; }

    public double getLowestGrade()                { return lowestGrade; }
    public void   setLowestGrade(double v)        { this.lowestGrade = v; }

    public char   getLowestRank()                 { return lowestRank; }
    public void   setLowestRank(char v)           { this.lowestRank = v; }

    public double getClassMean()                  { return classMean; }
    public void   setClassMean(double v)          { this.classMean = v; }

    public char   getMeanRank()                   { return meanRank; }
    public void   setMeanRank(char v)             { this.meanRank = v; }
}

