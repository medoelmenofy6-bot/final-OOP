package clinic.service;


import clinic.model.*;
import java.nio.file.Path;
import java.util.*;
import  clinic.model.Patient;
import  clinic.model.Prescription;
import  clinic.model.Person;
import  clinic.model.Doctor;
import  clinic.model.Appointment;
/**
 * Core business logic for the Smart Clinic Management System.
 */
public class ClinicManager {
    private final Map<String, Patient> patients = new LinkedHashMap<>();
    private final Map<String, Doctor> doctors = new LinkedHashMap<>();
    private final Map<String, Appointment> appointments = new LinkedHashMap<>();
    private final Map<String, Prescription> prescriptions = new LinkedHashMap<>();
    private final List<Person> people = new ArrayList<>();

    private final FileManager fileManager;
    private int patientCounter = 1;
    private int doctorCounter = 1;
    private int appointmentCounter = 1;
    private int prescriptionCounter = 1;

    public ClinicManager() {
        this.fileManager = new FileManager(Path.of("data"));
        loadAll();
    }

    private String nextId(String prefix, int number) {
        return String.format("%s-%03d", prefix, number);
    }

    private void refreshCounters() {
        patientCounter = Math.max(patientCounter, maxNumericId(patients.keySet(), "PT-") + 1);
        doctorCounter = Math.max(doctorCounter, maxNumericId(doctors.keySet(), "DR-") + 1);
        appointmentCounter = Math.max(appointmentCounter, maxNumericId(appointments.keySet(), "AP-") + 1);
        prescriptionCounter = Math.max(prescriptionCounter, maxNumericId(prescriptions.keySet(), "PR-") + 1);
    }

    private int maxNumericId(Collection<String> ids, String prefix) {
        int max = 0;
        for (String id : ids) {
            if (id != null && id.startsWith(prefix)) {
                try {
                    int n = Integer.parseInt(id.substring(prefix.length()));
                    max = Math.max(max, n);
                } catch (Exception ignored) {}
            }
        }
        return max;
    }

    public Patient addPatient(String name, int age, String gender, String phone, String condition) {
        String id = nextId("PT", patientCounter++);
        Patient patient = new Patient(id, name, age, gender, phone, condition);
        patients.put(id, patient);
        people.add(patient);
        saveAll();
        return patient;
    }

    public Doctor addDoctor(String name, String phone, String specialty) {
        String id = nextId("DR", doctorCounter++);
        Doctor doctor = new Doctor(id, name, phone, specialty);
        doctors.put(id, doctor);
        people.add(doctor);
        saveAll();
        return doctor;
    }

    public Appointment addAppointment(String patientId, String doctorId, String date, String time, String notes) {
        if (!patients.containsKey(patientId)) {
            throw new IllegalArgumentException("Patient ID not found");
        }
        if (!doctors.containsKey(doctorId)) {
            throw new IllegalArgumentException("Doctor ID not found");
        }
        String id = nextId("AP", appointmentCounter++);
        Appointment appointment = new Appointment(id, patientId, doctorId, date, time, notes, "Scheduled");
        appointments.put(id, appointment);
        saveAll();
        return appointment;
    }

    public Prescription addPrescription(String appointmentId, String medicine, String dosage, String instructions) {
        if (!appointments.containsKey(appointmentId)) {
            throw new IllegalArgumentException("Appointment ID not found");
        }
        String id = nextId("PR", prescriptionCounter++);
        Prescription prescription = new Prescription(id, appointmentId, medicine, dosage, instructions);
        prescriptions.put(id, prescription);
        saveAll();
        return prescription;
    }

    public boolean deletePatient(String id) {
        Patient removed = patients.remove(id);
        if (removed != null) {
            people.removeIf(p -> p.getId().equals(id));
            appointments.values().removeIf(a -> a.getPatientId().equals(id));
            saveAll();
            return true;
        }
        return false;
    }

    public boolean deleteDoctor(String id) {
        Doctor removed = doctors.remove(id);
        if (removed != null) {
            people.removeIf(p -> p.getId().equals(id));
            appointments.values().removeIf(a -> a.getDoctorId().equals(id));
            saveAll();
            return true;
        }
        return false;
    }

    public boolean deleteAppointment(String id) {
        Appointment removed = appointments.remove(id);
        if (removed != null) {
            prescriptions.values().removeIf(p -> p.getAppointmentId().equals(id));
            saveAll();
            return true;
        }
        return false;
    }

    public boolean deletePrescription(String id) {
        Prescription removed = prescriptions.remove(id);
        if (removed != null) {
            saveAll();
            return true;
        }
        return false;
    }

    public Collection<Patient> getPatients() { return patients.values(); }
    public Collection<Doctor> getDoctors() { return doctors.values(); }
    public Collection<Appointment> getAppointments() { return appointments.values(); }
    public Collection<Prescription> getPrescriptions() { return prescriptions.values(); }
   public List<String> getPeopleInfo() {
    List<String> info = new ArrayList<>();

    for (Person person : people) {
        info.add(person.displayInfo());
    }

    return info;
}

    public Patient getPatient(String id) { return patients.get(id); }
    public Doctor getDoctor(String id) { return doctors.get(id); }
    public Appointment getAppointment(String id) { return appointments.get(id); }

    public String[] patientComboItems() {
        return patients.values().stream().map(p -> p.getId() + " - " + p.getName()).toArray(String[]::new);
    }

    public String[] doctorComboItems() {
        return doctors.values().stream().map(d -> d.getId() + " - " + d.getName()).toArray(String[]::new);
    }

    public String[] appointmentComboItems() {
        return appointments.values().stream().map(a -> a.getId() + " - " + getAppointmentLabel(a)).toArray(String[]::new);
    }

    public String getAppointmentLabel(Appointment a) {
        Patient p = getPatient(a.getPatientId());
        Doctor d = getDoctor(a.getDoctorId());
        return (p != null ? p.getName() : a.getPatientId()) + " with " + (d != null ? d.getName() : a.getDoctorId()) + " on " + a.getDate();
    }

    public void loadAll() {
        patients.clear();
        doctors.clear();
        appointments.clear();
        prescriptions.clear();
        people.clear();

        for (Patient p : fileManager.loadPatients()) {
            patients.put(p.getId(), p);
            people.add(p);
        }
        for (Doctor d : fileManager.loadDoctors()) {
            doctors.put(d.getId(), d);
            people.add(d);
        }
        for (Appointment a : fileManager.loadAppointments()) {
            appointments.put(a.getId(), a);
        }
        for (Prescription p : fileManager.loadPrescriptions()) {
            prescriptions.put(p.getId(), p);
        }
        refreshCounters();
    }

    public void saveAll() {
        fileManager.savePatients(patients.values());
        fileManager.saveDoctors(doctors.values());
        fileManager.saveAppointments(appointments.values());
        fileManager.savePrescriptions(prescriptions.values());
    }

    public String statistics() {
        return String.format("Patients: %d | Doctors: %d | Appointments: %d | Prescriptions: %d",
                patients.size(), doctors.size(), appointments.size(), prescriptions.size());
    }
}
