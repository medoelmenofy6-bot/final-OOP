package clinic.model;

public class Prescription {

    private String id;
    private String appointmentId;
    private String medicine;
    private String dosage;
    private String instructions;

    public Prescription(String id, String appointmentId,
                         String medicine, String dosage,
                         String instructions) {

        this.id = id;
        this.appointmentId = appointmentId;
        this.medicine = medicine;
        this.dosage = dosage;
        this.instructions = instructions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String displayInfo() {
        return String.format(
                "%s | Appointment:%s | Medicine:%s | Dosage:%s | %s",
                id, appointmentId, medicine, dosage, instructions
        );
    }
}