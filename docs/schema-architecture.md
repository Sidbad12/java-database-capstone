# Smart Clinic — Architecture Design

## Section 1: Architecture Summary

The **Smart Clinic Management System** is built as a **three-tier web application** that cleanly separates concerns into **Presentation**, **Application (service)**, and **Data** layers.  

- The **Presentation layer** supports both server-rendered HTML dashboards (Thymeleaf + Spring MVC controllers) and a programmatic surface of RESTful APIs for single-page apps or external clients.  
- The **Application layer (Spring Boot)** hosts MVC controllers, REST controllers, services, and business logic; it also handles authentication, validation, and DTO mapping.  
- The **Data layer** uses two databases side-by-side:  
  - A **relational MySQL store** accessed via Spring Data JPA for strongly-typed, transactional entities (users, doctors, appointments).  
  - A **document-oriented MongoDB** accessed via Spring Data MongoDB for flexible, evolving documents (prescriptions, audit trails).  

This hybrid approach lets the system keep transactional integrity where it matters while providing flexible storage for semi-structured clinical artifacts. The app is designed to be **modular, testable, and ready for containerization and CI/CD**.

---

## Section 2: Numbered Flow — Request / Response Cycle

1. **User action (browser/mobile/API client)**  
   A user (admin, doctor, or patient) initiates an action from the UI (Thymeleaf page) or an external client calling a REST endpoint.

2. **HTTP request reaches Spring Boot**  
   The request arrives at the embedded web server (Tomcat/Jetty) and is routed by Spring MVC.

3. **Routing to Controller**  
   - For web pages: a Spring MVC controller handles the request, prepares a Model, and returns a Thymeleaf template.  
   - For APIs: a `@RestController` endpoint handles the request and accepts/returns JSON.

4. **Authentication & Authorization**  
   - JWT or session-based authentication middleware verifies identity.  
   - Role checks (admin/doctor/patient) are enforced before business logic executes.

5. **Request validation & DTO mapping**  
   Incoming payloads are validated (Bean Validation). DTOs are mapped to domain entities.

6. **Service layer invocation**  
   Controller delegates to a service class that orchestrates business logic, transactions, and interactions with repositories.

7. **Repository/Data access**  
   - **Relational data**: Services call Spring Data JPA repositories to perform CRUD on MySQL entities (Users, Appointments, Schedules). Transactions are managed here.  
   - **Document data**: Services call Spring Data MongoDB repositories for flexible documents (Prescriptions, VisitNotes, Audit logs).

8. **Cross-data operations & consistency**  
   When an operation spans both stores (e.g., create appointment + prescription document), the service coordinates actions, applies compensation logic or eventual consistency patterns, and logs the outcome.

9. **Business response assembly**  
   Service returns domain results to the controller (entities, DTOs, status).  
   - For web requests: the controller adds model attributes for Thymeleaf.  
   - For APIs: a JSON response is prepared.

10. **Serialization & HTTP response**  
    Response is serialized (HTML or JSON) and sent back to the client with appropriate HTTP status codes.

11. **Client updates UI**  
    The client updates the UI (page update or SPA state change) based on the response; follow-up asynchronous work (notifications, background jobs) may be triggered.

12. **Monitoring & persistence**  
    Application logs, metrics, and audit entries (in MongoDB or a separate store) capture the transaction for observability.

## Architecture Diagram (Mermaid)

# Smart Clinic — Architecture Diagram (Mermaid)

```mermaid
%%{init: {
  "theme": "default",
  "themeVariables": {
    "background": "#ffffff",
    "primaryColor": "#e8f5e9",
    "primaryTextColor": "#000000",
    "secondaryColor": "#f3e5f5",
    "tertiaryColor": "#bbdefb",
    "edgeLabelBackground":"#ffffff"
  }
}}%%

flowchart TD

%% ====== UI Layer ======
subgraph UI["Dashboards / REST Modules"]
  AD["AdminDashboard"]
  DD["DoctorDashboard"]
  AP["Appointments"]
  PD["PatientDashboard"]
end

AD -->|Uses| TC["Thymeleaf Controllers"]
DD -->|Uses| TC
AP -->|Uses JSON API| RC["REST Controllers"]
PD -->|Uses JSON API| RC

%% ====== Application Layer ======
subgraph APP["Spring Boot Application"]
  TC -->|Calls| SL["Service Layer"]
  RC -->|Calls| SL
  SL -->|Uses| MR["MySQL Repository"]
  SL -->|Uses| MGR["MongoDB Repository"]
end

%% ====== MySQL Layer ======
subgraph MYSQL["MySQL Data Layer"]
  MR -->|Accesses| DB["MySQL Database"]
  DB -->|Contains| PAT["Patient (JPA Entity)"]
  DB --> DOC["Doctor (JPA Entity)"]
  DB --> APPT["Appointment (JPA Entity)"]
  DB --> ADM["Admin (JPA Entity)"]
end

%% ====== MongoDB Layer ======
subgraph MONGO["MongoDB Data Layer"]
  MGR -->|Accesses| MDB["MongoDB Database"]
  MDB --> PRES["Prescription (Document)"]
end

%% ====== Styles ======
classDef ui fill:#C8E6C9,stroke:#2E7D32,stroke-width:2px,color:#000;
classDef app fill:#D1C4E9,stroke:#512DA8,stroke-width:2px,color:#000;
classDef db fill:#B3E5FC,stroke:#0277BD,stroke-width:2px,color:#000;
linkStyle default stroke:#9E9E9E,stroke-width:2px;


class UI ui;
class APP app;
class MYSQL,MONGO db;
