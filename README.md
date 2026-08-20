# final-OOP
# Smart Clinic Management System

## Project Overview

Smart Clinic Management System is a Java-based desktop application designed to help manage the main activities of a clinic.

The system provides a simple graphical user interface for managing patients, doctors, appointments, and prescriptions. It uses Object-Oriented Programming principles and file handling to store and retrieve clinic data.

## Sustainable Development Goal

This project is related to:

**SDG 3 – Good Health and Well-being**

The system supports better healthcare management by organizing patient information, doctor information, appointments, and prescriptions in one application.

## Main Features

* Manage patient records
* Manage doctor records
* Create and manage appointments
* Manage prescriptions
* Search and display clinic information
* Validate entered information
* Store data using CSV files
* Load saved data when the application starts
* Display clinic information through a graphical user interface
* Provide basic clinic statistics and information

## Technologies Used

* Java
* Java Swing
* Object-Oriented Programming (OOP)
* Collections
* File I/O
* CSV data storage

## OOP Concepts Used

The project demonstrates the main Object-Oriented Programming concepts:

### Encapsulation

Classes use private/protected attributes with appropriate getters and setters to control access to data.

### Inheritance

The `Patient` and `Doctor` classes inherit from the abstract `Person` class.

### Abstraction

`Person` is an abstract class that defines common behavior for different types of people in the clinic.

### Polymorphism

The project uses method overriding through methods such as `getRole()` and `displayInfo()` in the subclasses.

### Collections

Collections are used to store and manage clinic objects such as patients, doctors, appointments, and prescriptions.

## Project Structure

```text
SmartClinicProject1/
│
├── src/
│   └── clinic/
│       ├── Main.java
│       │
│       ├── model/
│       │   ├── Person.java
│       │   ├── Patient.java
│       │   ├── Doctor.java
│       │   ├── Appointment.java
│       │   └── Prescription.java
│       │
│       ├── service/
│       │   ├── ClinicManager.java
│       │   └── FileManager.java
│       │
│       └── ui/
│           └── MainFrame.java
│
├── data/
│   ├── patients.csv
│   ├── doctors.csv
│   ├── appointments.csv
│   └── prescriptions.csv
│
├── UML.txt
├── README.md
└── .gitignore
```

## Data Storage

The system uses CSV files for persistent data storage:

* `patients.csv` – stores patient information
* `doctors.csv` – stores doctor information
* `appointments.csv` – stores appointment information
* `prescriptions.csv` – stores prescription information

The `FileManager` class is responsible for reading and writing the data files.

## Main Classes

| Class           | Purpose                                               |
| --------------- | ----------------------------------------------------- |
| `Person`        | Abstract parent class for people in the clinic        |
| `Patient`       | Stores patient information                            |
| `Doctor`        | Stores doctor information                             |
| `Appointment`   | Stores appointment information                        |
| `Prescription`  | Stores prescription information                       |
| `ClinicManager` | Handles the main clinic operations and business logic |
| `FileManager`   | Handles CSV file reading and writing                  |
| `MainFrame`     | Provides the graphical user interface                 |
| `Main`          | Starts the application                                |

## Group Members

Add the project group members and student IDs here.

## How to Run

1. Open the project in a Java IDE such as IntelliJ IDEA.
2. Make sure the project uses a compatible Java SDK.
3. Keep the `data` folder in the project directory.
4. Run `Main.java`.
5. The Smart Clinic graphical user interface will open.

## Project Purpose

The main purpose of this project is to demonstrate how Object-Oriented Programming concepts can be applied to a real-world healthcare management system while supporting SDG 3: Good Health and Well-being.
