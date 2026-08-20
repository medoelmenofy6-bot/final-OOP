package clinic.model;

public class Patient extends Person {

    private int age;
    private String gender;
    private String condition;

    public Patient(String id, String name, int age,
                   String gender, String phone, String condition) {

        super(id, name, phone);

        this.age = age;
        this.gender = gender;
        this.condition = condition;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public String displayInfo() {
        return String.format(
                "%s | %s | Age:%d | Gender:%s | Phone:%s | Condition:%s",
                id, name, age, gender, phone, condition
        );
    }
}