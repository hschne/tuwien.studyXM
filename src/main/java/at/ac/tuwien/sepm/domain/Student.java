package at.ac.tuwien.sepm.domain;

/**
 * Student extends Person, hence inherits all properties from Person class (main data of a person)
 */
public class Student extends Person {

    private String matnr;

    public Student() {
    }

    public Student(String matnr, String firstname, String lastname) {
        setMatnr(matnr);
        setFirstname(firstname);
        setLastname(lastname);
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

}
