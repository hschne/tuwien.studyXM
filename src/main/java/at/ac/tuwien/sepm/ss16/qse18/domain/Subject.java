package at.ac.tuwien.sepm.ss16.qse18.domain;

public class Subject {

    private int subjectid;

    private String name;

    private float ects;

    private String semester;

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int fid) {
        this.subjectid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getEcts() {
        return ects;
    }

    public void setEcts(float ects) {
        this.ects = ects;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semster) {
        this.semester = semster;
    }
}
