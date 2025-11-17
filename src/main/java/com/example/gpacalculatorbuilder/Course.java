package com.example.gpacalculatorbuilder;

public class Course {
    private String name;
    private String code;
    private int credit;
    private String teacher1;
    private String teacher2;
    private String grade;

    public Course(String name, String code, int credit, String teacher1, String teacher2, String grade) {
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
        this.grade = grade;
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public int getCredit() { return credit; }
    public String getTeacher1() { return teacher1; }
    public String getTeacher2() { return teacher2; }
    public String getGrade() { return grade; }
}
