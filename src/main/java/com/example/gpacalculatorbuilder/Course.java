package com.example.gpacalculatorbuilder;

public class Course {

    private int id;
    private String name;
    private String code;
    private int credit;
    private String teacher1;
    private String teacher2;
    private String grade;


    public Course(int id, String name, String code, int credit, String teacher1, String teacher2, String grade) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
        this.grade = grade;
    }


    public Course(String name, String code, int credit, String teacher1, String teacher2, String grade) {
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
        this.grade = grade;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public int getCredit() { return credit; }
    public String getTeacher1() { return teacher1; }
    public String getTeacher2() { return teacher2; }
    public String getGrade() { return grade; }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
