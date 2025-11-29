# Smart Clinic Management System


A comprehensive full-stack clinic management system built with Spring Boot, MySQL, MongoDB, and modern web technologies. This enterprise-grade application streamlines healthcare operations by managing doctors, patients, appointments, and prescriptions through role-based dashboards.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![MongoDB](https://img.shields.io/badge/MongoDB-Latest-green)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

---

## 🌟 Key Features

### Multi-Role Access Control
- **Admin Dashboard**: Manage doctors, view system analytics, control user access
- **Doctor Dashboard**: View appointments, manage patient records, issue prescriptions
- **Patient Portal**: Book appointments, view history, access prescriptions

### Core Functionality
- ✅ **Appointment Management**: Real-time booking with availability checking
- ✅ **Prescription System**: Digital prescription storage and retrieval (MongoDB)
- ✅ **User Authentication**: JWT-based secure authentication with role-based access
- ✅ **Doctor Filtering**: Search by name, specialty, and available time slots
- ✅ **Appointment Tracking**: Status tracking (Scheduled, Completed, Cancelled)

---

## 🏗️ Architecture

### Three-Tier Architecture

```
┌─────────────────────────────────────────┐
│      Presentation Layer                 │
│  (Thymeleaf + REST APIs + JavaScript)   │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│      Application Layer                  │
│  (Spring Boot + Business Logic)         │
└─────────────────────────────────────────┘
                  ↓
┌──────────────────────┬──────────────────┐
│   MySQL Database     │  MongoDB         │
│  (Relational Data)   │  (Documents)     │
└──────────────────────┴──────────────────┘
```

### Hybrid Database Design

**MySQL** - Structured, transactional data:
- Users (Admin, Doctors, Patients)
- Appointments with foreign key relationships
- Doctor availability and specializations

**MongoDB** - Flexible, document-oriented data:
- Prescriptions with nested medicine arrays
- Medical notes and flexible clinical records
- Audit trails and logs

---

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.5
- **Language**: Java 17
- **Security**: Spring Security + JWT
- **Data Access**: Spring Data JPA, Spring Data MongoDB
- **Build Tool**: Maven 3.9
- **Validation**: Bean Validation (JSR-380)

### Databases
- **MySQL 8.0**: Relational data with ACID compliance
- **MongoDB**: Document store for flexible schemas

### Frontend
- **Template Engine**: Thymeleaf
- **JavaScript**: ES6 Modules
- **CSS**: Custom responsive design
- **API Communication**: Fetch API

### DevOps
- **Containerization**: Docker with multi-stage builds
- **CI/CD**: GitHub Actions (compile, lint, test workflows)
- **Code Quality**: Checkstyle, ESLint, HTMLHint
- **Container Registry**: IBM Cloud Container Registry

---

## 📁 Project Structure

```
smart-clinic/
├── app/
│   ├── src/main/java/com/project/back_end/
│   │   ├── controllers/          # REST & MVC Controllers
│   │   ├── services/              # Business Logic Layer
│   │   ├── models/                # JPA Entities & MongoDB Documents
│   │   ├── repo/                  # Data Access Repositories
│   │   ├── dto/                   # Data Transfer Objects
│   │   ├── config/                # Security & CORS Config
│   │   └── mvc/                   # Server-side Controllers
│   ├── src/main/resources/
│   │   ├── application.properties # Configuration
│   │   ├── static/                # Frontend Assets
│   │   └── templates/             # Thymeleaf Templates
│   ├── Dockerfile                 # Multi-stage build
│   └── pom.xml                    # Maven dependencies
├── .github/workflows/             # CI/CD Pipelines
├── docs/                          # Architecture Documentation
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0
- MongoDB 4.4+
- Docker (optional)

### Local Development Setup

#### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/smart-clinic.git
cd smart-clinic
```

#### 2. Configure Databases

**MySQL Setup:**
```sql
CREATE DATABASE cms;
CREATE USER 'clinic_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON cms.* TO 'clinic_user'@'localhost';
FLUSH PRIVILEGES;
```

**MongoDB Setup:**
```bash
# MongoDB runs on default port 27017
# Database 'clinic_prescriptions' will be auto-created
```

#### 3. Update Configuration

Edit `app/src/main/resources/application.properties`:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/cms
spring.datasource.username=clinic_user
spring.datasource.password=your_password

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/clinic_prescriptions

# JWT Secret (change in production)
jwt.secret=YourSecureJWTSecretKey123456789
```

#### 4. Build and Run

```bash
cd app
mvn clean install
mvn spring-boot:run
```

Application will start on: `http://localhost:8081`

---

## 🐳 Docker Deployment

### Build Docker Image
```bash
cd app
docker build -t smart-clinic-backend:latest .
```

### Run with Docker Compose
```bash
docker-compose up -d
```

This will start:
- Spring Boot application (port 8081)
- MySQL database (port 3306)
- MongoDB (port 27017)

---

## 📊 Database Schema

### MySQL Tables

**doctors**
- `id` (PK), `name`, `specialty`, `email`, `password`, `phone`
- `available_times` (JSON array)

**patients**
- `id` (PK), `name`, `email`, `password`, `phone`, `address`

**appointments**
- `id` (PK), `doctor_id` (FK), `patient_id` (FK)
- `appointment_time`, `status`

**admin**
- `id` (PK), `username`, `password_hash`

### MongoDB Collections

**prescriptions**
```json
{
  "_id": "ObjectId",
  "patient_id": 23,
  "doctor_id": 7,
  "appointment_id": 101,
  "medicines": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times daily"
    }
  ],
  "doctor_notes": "Take after meals",
  "date_issued": "2025-11-29T10:30:00Z"
}
```

---

## 🔑 API Endpoints

### Authentication
```
POST /api/admin/login          # Admin login
POST /api/doctor/login         # Doctor login
POST /api/patient/login        # Patient login
POST /api/patient              # Patient registration
```

### Doctor Management
```
GET    /api/doctor                        # List all doctors
POST   /api/doctor/{token}                # Add doctor (Admin)
PUT    /api/doctor/{token}                # Update doctor (Admin)
DELETE /api/doctor/{id}/{token}           # Delete doctor (Admin)
GET    /api/doctor/filter/{name}/{time}/{specialty}
```

### Appointments
```
GET    /appointments/{date}/{patientName}/{token}  # Get appointments
POST   /appointments/{token}                       # Book appointment
PUT    /appointments/{token}                       # Update appointment
DELETE /appointments/{id}/{token}                  # Cancel appointment
```

### Prescriptions
```
POST /api/prescription/{token}                # Create prescription
GET  /api/prescription/{appointmentId}/{token} # Get prescription
```

---

## 🧪 Testing & Quality Assurance

### Run Tests
```bash
mvn test
```

### Code Quality Checks
```bash
# Java Checkstyle
mvn checkstyle:check

# Compile check
mvn clean compile
```

### CI/CD Pipeline
GitHub Actions automatically run on every push:
- ✅ Java compilation
- ✅ Checkstyle linting
- ✅ Docker linting (Hadolint)
- ✅ Frontend linting (ESLint, HTMLHint)

---

## 🔐 Security Features

- **JWT Authentication**: Secure token-based auth with 7-day expiration
- **Password Hashing**: Bcrypt for password storage (ready to implement)
- **Role-Based Access Control**: Admin, Doctor, Patient roles
- **CORS Configuration**: Controlled cross-origin access
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries
- **Input Validation**: Bean Validation annotations

---

## 📈 Future Enhancements

- [ ] Email notifications for appointments
- [ ] Payment gateway integration
- [ ] Video consultation feature
- [ ] Mobile app (React Native)
- [ ] Advanced analytics dashboard
- [ ] Prescription PDF export
- [ ] Multi-language support
- [ ] Password reset functionality

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.


---

## 🙏 Acknowledgments

- Coursera & IBM Developer Skills Network for infrastructure support
- Spring Boot community for excellent documentation
- Open source contributors


---


<div align="center">
  <sub>Built with ❤️ using Spring Boot and modern web technologies</sub>
</div>
