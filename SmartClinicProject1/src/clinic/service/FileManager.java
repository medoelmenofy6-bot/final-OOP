package clinic.service;

import clinic.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Handles file saving and loading for the clinic system.
 */
public class FileManager {
    private final Path dataDir;

    public FileManager(Path dataDir) {
        this.dataDir = dataDir;
    }

    public void ensureDirectory() {
        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create data folder: " + dataDir, e);
        }
    }

    private Path file(String name) {
        return dataDir.resolve(name);
    }

    private String safe(String value) {
        return value == null ? "" : value.replace("|", "/").replace("\n", " ").trim();
    }

    public void savePatients(Collection<Patient> patients) {
        writeLines(file("patients.csv"), patients.stream()
                .map(p -> String.join("|",
                        safe(p.getId()), safe(p.getName()), String.valueOf(p.getAge()),
                        safe(p.getGender()), safe(p.getPhone()), safe(p.getCondition())))
                .toList());
    }

    public void saveDoctors(Collection<Doctor> doctors) {
        writeLines(file("doctors.csv"), doctors.stream()
                .map(d -> String.join("|",
                        safe(d.getId()), safe(d.getName()), safe(d.getPhone()), safe(d.getSpecialty())))
                .toList());
    }

    public void saveAppointments(Collection<Appointment> appointments) {
        writeLines(file("appointments.csv"), appointments.stream()
                .map(a -> String.join("|",
                        safe(a.getId()), safe(a.getPatientId()), safe(a.getDoctorId()),
                        safe(a.getDate()), safe(a.getTime()), safe(a.getNotes()), safe(a.getStatus())))
                .toList());
    }

    public void savePrescriptions(Collection<Prescription> prescriptions) {
        writeLines(file("prescriptions.csv"), prescriptions.stream()
                .map(p -> String.join("|",
                        safe(p.getId()), safe(p.getAppointmentId()), safe(p.getMedicine()),
                        safe(p.getDosage()), safe(p.getInstructions())))
                .toList());
    }

    private void writeLines(Path path, List<String> lines) {
        ensureDirectory();
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + path.getFileName(), e);
        }
    }

    public List<Patient> loadPatients() {
        List<Patient> list = new ArrayList<>();
        for (String line : readLines(file("patients.csv"))) {
            String[] p = split(line, 6);
            list.add(new Patient(p[0], p[1], parseIntSafe(p[2]), p[3], p[4], p[5]));
        }
        return list;
    }

    public List<Doctor> loadDoctors() {
        List<Doctor> list = new ArrayList<>();
        for (String line : readLines(file("doctors.csv"))) {
            String[] p = split(line, 4);
            list.add(new Doctor(p[0], p[1], p[2], p[3]));
        }
        return list;
    }

    public List<Appointment> loadAppointments() {
        List<Appointment> list = new ArrayList<>();
        for (String line : readLines(file("appointments.csv"))) {
            String[] p = split(line, 7);
            list.add(new Appointment(p[0], p[1], p[2], p[3], p[4], p[5], p[6]));
        }
        return list;
    }

    public List<Prescription> loadPrescriptions() {
        List<Prescription> list = new ArrayList<>();
        for (String line : readLines(file("prescriptions.csv"))) {
            String[] p = split(line, 5);
            list.add(new Prescription(p[0], p[1], p[2], p[3], p[4]));
        }
        return list;
    }

    private List<String> readLines(Path path) {
        if (!Files.exists(path)) return Collections.emptyList();
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + path.getFileName(), e);
        }
    }

    private String[] split(String line, int expected) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < expected) {
            String[] fixed = new String[expected];
            for (int i = 0; i < expected; i++) {
                fixed[i] = i < parts.length ? parts[i] : "";
            }
            return fixed;
        }
        return parts;
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
