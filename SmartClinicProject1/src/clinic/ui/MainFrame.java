package clinic.ui;

import clinic.model.*;
import clinic.service.ClinicManager;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Main GUI window with modern design.
 */
public class MainFrame extends JFrame {
    private final ClinicManager manager;

    // Dashboard stats
    private JLabel patientCountLabel, doctorCountLabel, appointmentCountLabel, prescriptionCountLabel;
    private JTable upcomingTable;

    // Tables
    private JTable patientTable, doctorTable, appointmentTable, prescriptionTable;

    // Patient form
    private JTextField patientNameField, patientAgeField, patientPhoneField, patientConditionField;
    private JComboBox<String> patientGenderCombo;

    // Doctor form
    private JTextField doctorNameField, doctorPhoneField, doctorSpecialtyField;

    // Appointment form
    private JComboBox<String> appointmentPatientCombo, appointmentDoctorCombo;
    private JTextField appointmentDateField, appointmentTimeField, appointmentNotesField;

    // Prescription form
    private JComboBox<String> prescriptionAppointmentCombo;
    private JTextField prescriptionMedicineField, prescriptionDosageField, prescriptionInstructionsField;

    private JTabbedPane mainTabs;

    public MainFrame(ClinicManager manager) {
        this.manager = manager;
        setTitle("🏥 Smart Clinic Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        root.setBackground(new Color(240, 245, 250));
        setContentPane(root);

        // ---- HEADER PANEL (Title) ----
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 60, 120)); // dark blue background
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("🏥 Smart Clinic Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE); // white text on dark blue
        headerPanel.add(title, BorderLayout.CENTER);

        root.add(headerPanel, BorderLayout.NORTH);

        // ---- TABS ----
        mainTabs = new JTabbedPane();
        mainTabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainTabs.addTab("📊 Dashboard", buildDashboardPanel());
        mainTabs.addTab("👤 Patients", buildPatientsPanel());
        mainTabs.addTab("👨‍⚕️ Doctors", buildDoctorsPanel());
        mainTabs.addTab("📅 Appointments", buildAppointmentsPanel());
        mainTabs.addTab("💊 Prescriptions", buildPrescriptionsPanel());

        root.add(mainTabs, BorderLayout.CENTER);

        refreshAll();
    }

    // ================ DASHBOARD ================

    private JPanel buildDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBackground(new Color(240, 245, 250));

        // Top action bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topBar.setBackground(new Color(240, 245, 250));
        JButton newPatient = createStyledButton("➕ New Patient", new Color(52, 152, 219));
        JButton newAppointment = createStyledButton("📅 New Appointment", new Color(46, 204, 113));
        JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(155, 89, 182));
        JButton saveBtn = createStyledButton("💾 Save Now", new Color(52, 73, 94));

        newPatient.addActionListener(e -> mainTabs.setSelectedIndex(1));
        newAppointment.addActionListener(e -> mainTabs.setSelectedIndex(3));
        refreshBtn.addActionListener(e -> refreshAll());
        saveBtn.addActionListener(e -> { manager.saveAll(); JOptionPane.showMessageDialog(this, "Saved!"); });

        topBar.add(newPatient);
        topBar.add(newAppointment);
        topBar.add(refreshBtn);
        topBar.add(saveBtn);
        p.add(topBar, BorderLayout.NORTH);

        // Stats cards – create and store count labels
        JPanel cards = new JPanel(new GridLayout(1, 4, 20, 0));
        cards.setBackground(new Color(240, 245, 250));

        JPanel patientCard = createStatsCard("👨‍⚕️ Patients", 0, new Color(52, 152, 219));
        patientCountLabel = (JLabel) patientCard.getComponent(1); // count is at CENTER (index 1)
        cards.add(patientCard);

        JPanel doctorCard = createStatsCard("👨‍🏫 Doctors", 0, new Color(46, 204, 113));
        doctorCountLabel = (JLabel) doctorCard.getComponent(1);
        cards.add(doctorCard);

        JPanel appointmentCard = createStatsCard("📋 Appointments", 0, new Color(241, 196, 15));
        appointmentCountLabel = (JLabel) appointmentCard.getComponent(1);
        cards.add(appointmentCard);

        JPanel prescriptionCard = createStatsCard("💊 Prescriptions", 0, new Color(231, 76, 60));
        prescriptionCountLabel = (JLabel) prescriptionCard.getComponent(1);
        cards.add(prescriptionCard);

        p.add(cards, BorderLayout.CENTER);

        // Upcoming table
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 60, 120), 2),
                " 📅 Upcoming Appointments (Next 5) ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(0, 60, 120)
        ));
        upcomingTable = new JTable();
        upcomingTable.setRowHeight(28);
        upcomingTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        upcomingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        upcomingTable.getTableHeader().setBackground(new Color(52, 152, 219));
        upcomingTable.getTableHeader().setForeground(Color.WHITE);
        upcomingTable.setShowGrid(true);
        upcomingTable.setGridColor(new Color(200, 200, 200));
        bottom.add(new JScrollPane(upcomingTable), BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    private JPanel createStatsCard(String title, int count, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        JLabel titleLbl = new JLabel(title, JLabel.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(Color.WHITE);
        JLabel countLbl = new JLabel(String.valueOf(count), JLabel.CENTER);
        countLbl.setFont(new Font("Segoe UI", Font.BOLD, 36));
        countLbl.setForeground(Color.WHITE);
        card.add(titleLbl, BorderLayout.NORTH);
        card.add(countLbl, BorderLayout.CENTER);
        return card;
    }

    // ================ COMMON BUILDERS ================

    private JPanel buildPatientsPanel() {
        return buildGenericPanel("Patient", buildPatientForm(), patientTable = new JTable(), this::addPatient, this::deleteSelectedPatient);
    }
    private JPanel buildDoctorsPanel() {
        return buildGenericPanel("Doctor", buildDoctorForm(), doctorTable = new JTable(), this::addDoctor, this::deleteSelectedDoctor);
    }
    private JPanel buildAppointmentsPanel() {
        return buildGenericPanel("Appointment", buildAppointmentForm(), appointmentTable = new JTable(), this::addAppointment, this::deleteSelectedAppointment);
    }
    private JPanel buildPrescriptionsPanel() {
        return buildGenericPanel("Prescription", buildPrescriptionForm(), prescriptionTable = new JTable(), this::addPrescription, this::deleteSelectedPrescription);
    }

    private JPanel buildGenericPanel(String entity, JPanel form, JTable table, Runnable addAction, Runnable deleteAction) {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(form, BorderLayout.NORTH);

        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        JButton addBtn = createStyledButton("➕ Add " + entity, new Color(46, 204, 113));
        JButton delBtn = createStyledButton("🗑️ Delete Selected", new Color(231, 76, 60));
        addBtn.addActionListener(e -> addAction.run());
        delBtn.addActionListener(e -> deleteAction.run());
        btnPanel.add(addBtn);
        btnPanel.add(delBtn);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    // ================ FORMS ================

    private JPanel buildPatientForm() {
        JPanel form = new JPanel(new GridLayout(2, 5, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                " Patient Details ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 152, 219)
        ));
        patientNameField = new JTextField();
        patientAgeField = new JTextField();
        patientGenderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        patientPhoneField = new JTextField();
        patientConditionField = new JTextField();

        String[] labels = {"Name", "Age", "Gender", "Phone", "Condition"};
        JComponent[] fields = {patientNameField, patientAgeField, patientGenderCombo, patientPhoneField, patientConditionField};
        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }
        return form;
    }

    private JPanel buildDoctorForm() {
        JPanel form = new JPanel(new GridLayout(2, 3, 10, 10));
        form.setBorder(createTitledBorder(" Doctor Details "));
        doctorNameField = new JTextField();
        doctorPhoneField = new JTextField();
        doctorSpecialtyField = new JTextField();
        String[] labels = {"Name", "Phone", "Specialty"};
        JComponent[] fields = {doctorNameField, doctorPhoneField, doctorSpecialtyField};
        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }
        return form;
    }

    private JPanel buildAppointmentForm() {
        JPanel form = new JPanel(new GridLayout(2, 5, 10, 10));
        form.setBorder(createTitledBorder(" Appointment Details "));
        appointmentPatientCombo = new JComboBox<>();
        appointmentDoctorCombo = new JComboBox<>();
        appointmentDateField = new JTextField();
        appointmentTimeField = new JTextField();
        appointmentNotesField = new JTextField();
        String[] labels = {"Patient", "Doctor", "Date (YYYY-MM-DD)", "Time (HH:mm)", "Notes"};
        JComponent[] fields = {appointmentPatientCombo, appointmentDoctorCombo, appointmentDateField, appointmentTimeField, appointmentNotesField};
        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }
        return form;
    }

    private JPanel buildPrescriptionForm() {
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBorder(createTitledBorder(" Prescription Details "));
        prescriptionAppointmentCombo = new JComboBox<>();
        prescriptionMedicineField = new JTextField();
        prescriptionDosageField = new JTextField();
        prescriptionInstructionsField = new JTextField();
        String[] labels = {"Appointment", "Medicine", "Dosage", "Instructions"};
        JComponent[] fields = {prescriptionAppointmentCombo, prescriptionMedicineField, prescriptionDosageField, prescriptionInstructionsField};
        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }
        return form;
    }

    private TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                title,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 152, 219)
        );
    }

    // ================ STYLED BUTTON ================

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ================ ADD METHODS ================

    private void addPatient() {
        try {
            String name = patientNameField.getText().trim();
            String ageText = patientAgeField.getText().trim();
            String gender = (String) patientGenderCombo.getSelectedItem();
            String phone = patientPhoneField.getText().trim();
            String condition = patientConditionField.getText().trim();

            if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!isValidName(name)) { JOptionPane.showMessageDialog(this, "Name must contain only letters and spaces.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (ageText.isEmpty()) { JOptionPane.showMessageDialog(this, "Age is required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            int age = Integer.parseInt(ageText);
            if (age < 0 || age > 150) { JOptionPane.showMessageDialog(this, "Age must be 0-150.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (gender == null) { JOptionPane.showMessageDialog(this, "Please select gender.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!phone.isEmpty() && !isValidPhone(phone)) { JOptionPane.showMessageDialog(this, "Invalid phone format.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            manager.addPatient(name, age, gender, phone, condition);
            clearPatientFields();
            refreshAll();
            JOptionPane.showMessageDialog(this, "Patient added.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDoctor() {
        try {
            String name = doctorNameField.getText().trim();
            String phone = doctorPhoneField.getText().trim();
            String specialty = doctorSpecialtyField.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Doctor name required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!isValidName(name)) { JOptionPane.showMessageDialog(this, "Name must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!phone.isEmpty() && !isValidPhone(phone)) { JOptionPane.showMessageDialog(this, "Invalid phone.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!specialty.isEmpty() && !isValidName(specialty)) { JOptionPane.showMessageDialog(this, "Specialty must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            manager.addDoctor(name, phone, specialty);
            clearDoctorFields();
            refreshAll();
            JOptionPane.showMessageDialog(this, "Doctor added.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addAppointment() {
        try {
            String patientId = extractId((String) appointmentPatientCombo.getSelectedItem());
            String doctorId = extractId((String) appointmentDoctorCombo.getSelectedItem());
            String date = appointmentDateField.getText().trim();
            String time = appointmentTimeField.getText().trim();
            String notes = appointmentNotesField.getText().trim();

            if (patientId.isEmpty() || doctorId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select patient and doctor.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (date.isEmpty() || !isValidDate(date)) {
                JOptionPane.showMessageDialog(this, "Invalid date (use YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (time.isEmpty() || !isValidTime(time)) {
                JOptionPane.showMessageDialog(this, "Invalid time (use HH:mm).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidAppointmentDateTime(date, time)) {
                JOptionPane.showMessageDialog(this, "Appointment must be in the future.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            manager.addAppointment(patientId, doctorId, date, time, notes);
            clearAppointmentFields();
            refreshAll();
            JOptionPane.showMessageDialog(this, "Appointment added.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPrescription() {
        try {
            String appointmentId = extractId((String) prescriptionAppointmentCombo.getSelectedItem());
            String medicine = prescriptionMedicineField.getText().trim();
            String dosage = prescriptionDosageField.getText().trim();
            String instructions = prescriptionInstructionsField.getText().trim();
            if (appointmentId.isEmpty()) { JOptionPane.showMessageDialog(this, "Select appointment.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (medicine.isEmpty() || !isValidName(medicine)) { JOptionPane.showMessageDialog(this, "Medicine name must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (dosage.isEmpty() || !isValidDosage(dosage)) { JOptionPane.showMessageDialog(this, "Dosage must contain a number.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            manager.addPrescription(appointmentId, medicine, dosage, instructions);
            clearPrescriptionFields();
            refreshAll();
            JOptionPane.showMessageDialog(this, "Prescription added.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================ DELETE METHODS ================

    private void deleteSelectedPatient() { deleteFromTable(patientTable, manager::deletePatient); }
    private void deleteSelectedDoctor() { deleteFromTable(doctorTable, manager::deleteDoctor); }
    private void deleteSelectedAppointment() { deleteFromTable(appointmentTable, manager::deleteAppointment); }
    private void deleteSelectedPrescription() { deleteFromTable(prescriptionTable, manager::deletePrescription); }

    private void deleteFromTable(JTable table, java.util.function.Function<String, Boolean> deleter) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) table.getValueAt(row, 0);
            if (deleter.apply(id)) refreshAll();
        }
    }

    // ================ REFRESH ================

    private void refreshAll() {
        refreshStats();
        refreshPatientTable();
        refreshDoctorTable();
        refreshAppointmentTable();
        refreshPrescriptionTable();
        refreshCombos();
        refreshUpcomingTable();
    }

    private void refreshStats() {
        patientCountLabel.setText(String.valueOf(manager.getPatients().size()));
        doctorCountLabel.setText(String.valueOf(manager.getDoctors().size()));
        appointmentCountLabel.setText(String.valueOf(manager.getAppointments().size()));
        prescriptionCountLabel.setText(String.valueOf(manager.getPrescriptions().size()));
    }

    private void refreshCombos() {
        appointmentPatientCombo.removeAllItems();
        for (String s : manager.patientComboItems()) appointmentPatientCombo.addItem(s);
        appointmentDoctorCombo.removeAllItems();
        for (String s : manager.doctorComboItems()) appointmentDoctorCombo.addItem(s);
        prescriptionAppointmentCombo.removeAllItems();
        for (String s : manager.appointmentComboItems()) prescriptionAppointmentCombo.addItem(s);
    }

    private void refreshPatientTable() {
        refreshTable(patientTable, new String[]{"ID","Name","Age","Gender","Phone","Condition"},
                manager.getPatients().stream()
                        .map(p -> new Object[]{p.getId(), p.getName(), p.getAge(), p.getGender(), p.getPhone(), p.getCondition()})
                        .toArray(Object[][]::new));
    }

    private void refreshDoctorTable() {
        refreshTable(doctorTable, new String[]{"ID","Name","Phone","Specialty"},
                manager.getDoctors().stream()
                        .map(d -> new Object[]{d.getId(), d.getName(), d.getPhone(), d.getSpecialty()})
                        .toArray(Object[][]::new));
    }

    private void refreshAppointmentTable() {
        String[] cols = {"ID","Patient","Doctor","Date","Time","Status","Notes"};
        Object[][] data = manager.getAppointments().stream().map(a -> {
            Patient p = manager.getPatient(a.getPatientId());
            Doctor d = manager.getDoctor(a.getDoctorId());
            return new Object[]{a.getId(), p==null?a.getPatientId():p.getName(), d==null?a.getDoctorId():d.getName(), a.getDate(), a.getTime(), a.getStatus(), a.getNotes()};
        }).toArray(Object[][]::new);
        refreshTable(appointmentTable, cols, data);
    }

    private void refreshPrescriptionTable() {
        String[] cols = {"ID","Appointment","Medicine","Dosage","Instructions"};
        Object[][] data = manager.getPrescriptions().stream().map(p -> {
            Appointment a = manager.getAppointment(p.getAppointmentId());
            return new Object[]{p.getId(), a==null?p.getAppointmentId():manager.getAppointmentLabel(a), p.getMedicine(), p.getDosage(), p.getInstructions()};
        }).toArray(Object[][]::new);
        refreshTable(prescriptionTable, cols, data);
    }

    private void refreshTable(JTable table, String[] cols, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Object[] row : data) model.addRow(row);
        table.setModel(model);
    }

    private void refreshUpcomingTable() {
        String[] cols = {"Date","Time","Patient","Doctor","Status"};
        java.util.List<Appointment> list = new java.util.ArrayList<>(manager.getAppointments());
        list.sort(java.util.Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTime));
        int limit = Math.min(list.size(), 5);
        Object[][] data = new Object[limit][5];
        for (int i = 0; i < limit; i++) {
            Appointment a = list.get(i);
            Patient p = manager.getPatient(a.getPatientId());
            Doctor d = manager.getDoctor(a.getDoctorId());
            data[i] = new Object[]{a.getDate(), a.getTime(), p==null?a.getPatientId():p.getName(), d==null?a.getDoctorId():d.getName(), a.getStatus()};
        }
        refreshTable(upcomingTable, cols, data);
    }

    // ================ UTILITY ================

    private String extractId(String comboText) {
        if (comboText == null || comboText.isBlank()) return "";
        int idx = comboText.indexOf(" - ");
        return idx > 0 ? comboText.substring(0, idx).trim() : comboText.trim();
    }

    private void clearPatientFields() {
        patientNameField.setText("");
        patientAgeField.setText("");
        patientGenderCombo.setSelectedIndex(0);
        patientPhoneField.setText("");
        patientConditionField.setText("");
    }
    private void clearDoctorFields() {
        doctorNameField.setText("");
        doctorPhoneField.setText("");
        doctorSpecialtyField.setText("");
    }
    private void clearAppointmentFields() {
        appointmentDateField.setText("");
        appointmentTimeField.setText("");
        appointmentNotesField.setText("");
    }
    private void clearPrescriptionFields() {
        prescriptionMedicineField.setText("");
        prescriptionDosageField.setText("");
        prescriptionInstructionsField.setText("");
    }

    // ================ VALIDATION ================

    private boolean isValidName(String s) { return s.matches("^[a-zA-Z\\s]+$"); }
    private boolean isValidPhone(String s) { return s.matches("^[0-9\\s\\-+()]+$"); }
    private boolean isValidDate(String s) { try { LocalDate.parse(s); return true; } catch (Exception e) { return false; } }
    private boolean isValidTime(String s) { try { LocalTime.parse(s); return true; } catch (Exception e) { return false; } }
    private boolean isValidDosage(String s) { return s.matches("^(?=.*[0-9])[\\w\\s]+$"); }
    private boolean isValidAppointmentDateTime(String date, String time) {
        try {
            java.time.LocalDateTime dt = java.time.LocalDateTime.parse(date + "T" + time);
            return !dt.isBefore(java.time.LocalDateTime.now());
        } catch (Exception e) { return false; }
    }
}