package clinic.model;

public class Appointment {

    private String id;
    private String patientId;
    private String doctorId;
    private String date;
    private String time;
    private String notes;
    private String status;

    public Appointment(String id, String patientId,
                       String doctorId, String date,
                       String time, String notes,
                       String status) {

        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String displayInfo() {
        return String.format(
                "%s | Patient:%s | Doctor:%s | %s %s | %s | %s",
                id, patientId, doctorId,
                date, time, status, notes
        );
    }
}