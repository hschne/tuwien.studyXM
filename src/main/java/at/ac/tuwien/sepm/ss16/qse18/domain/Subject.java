package at.ac.tuwien.sepm.ss16.qse18.domain;

public class Subject {

    private int fid;

    private String name;

    private double ects;

    private String semester;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEcts() {
        return ects;
    }

    public void setEcts(double ects) {
        this.ects = ects;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semster) {
        this.semester = semster;
    }
}
