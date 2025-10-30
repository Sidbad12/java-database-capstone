# Smart Clinic — Database Schema Design

## Overview
This document defines the database schema design for the **Smart Clinic Management System**.  
The system uses a **hybrid database approach**:
- **MySQL** for structured, relational data (patients, doctors, appointments, admin).  
- **MongoDB** for flexible, document-oriented data (e.g., prescriptions, feedback, logs).

---

## 🧩 MySQL Database Design

### 1. Table: patients
| Column Name | Data Type | Constraints | Description |
|--------------|------------|-------------|--------------|
| patient_id | INT AUTO_INCREMENT | PRIMARY KEY | Unique patient identifier |
| name | VARCHAR(100) | NOT NULL | Patient’s full name |
| email | VARCHAR(100) | UNIQUE NOT NULL | Patient’s email address |
| date_of_birth | DATE |  | Patient’s birth date |
| contact_number | VARCHAR(15) |  | Contact phone number |

---

### 2. Table: doctors
| Column Name | Data Type | Constraints | Description |
|--------------|------------|-------------|--------------|
| doctor_id | INT AUTO_INCREMENT | PRIMARY KEY | Unique doctor identifier |
| name | VARCHAR(100) | NOT NULL | Doctor’s full name |
| specialization | VARCHAR(100) | NOT NULL | Doctor’s field of expertise |
| email | VARCHAR(100) | UNIQUE NOT NULL | Doctor’s email address |

---

### 3. Table: appointments
| Column Name | Data Type | Constraints | Description |
|--------------|------------|-------------|--------------|
| appointment_id | INT AUTO_INCREMENT | PRIMARY KEY | Unique appointment ID |
| patient_id | INT | FOREIGN KEY REFERENCES patients(patient_id) | Linked patient |
| doctor_id | INT | FOREIGN KEY REFERENCES doctors(doctor_id) | Linked doctor |
| appointment_date | DATETIME | NOT NULL | Date and time of appointment |
| status | ENUM('Scheduled','Cancelled','Completed') | DEFAULT 'Scheduled' | Appointment status |

---

### 4. Table: admin
| Column Name | Data Type | Constraints | Description |
|--------------|------------|-------------|--------------|
| admin_id | INT AUTO_INCREMENT | PRIMARY KEY | Unique admin ID |
| username | VARCHAR(50) | UNIQUE NOT NULL | Admin login username |
| password_hash | VARCHAR(255) | NOT NULL | Securely stored password hash |

---

> 💡 *Design Rationale:*  
> - The relational structure enforces data integrity and supports queries like “show all appointments for a patient” or “list all doctors by specialization.”  
> - Foreign keys maintain referential integrity between patients, doctors, and appointments.  

---

## 🌿 MongoDB Collection Design

### Collection: prescriptions
Example document:
```json
{
  "_id": "64fdb4e983bcf112d4b112a7",
  "patient_id": 23,
  "doctor_id": 7,
  "date_issued": "2025-10-30T10:30:00Z",
  "medicines": [
    { "name": "Amoxicillin", "dosage": "500mg", "frequency": "3 times a day" },
    { "name": "Paracetamol", "dosage": "650mg", "frequency": "2 times a day" }
  ],
  "instructions": "Take after meals",
  "follow_up_date": "2025-11-05T10:00:00Z"
}
