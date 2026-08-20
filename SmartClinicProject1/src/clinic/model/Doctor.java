package clinic.model;

public class Doctor extends Person {

    private String specialty;

    public Doctor(String id, String name,
                   String phone, String specialty) {

        super(id, name, phone);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public String displayInfo() {
        return String.format(
                "%s | %s | Specialty:%s | Phone:%s",
                id, name, specialty, phone
        );
    }
}