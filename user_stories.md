# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

## Admin User Stories

### 1. Admin Login
_As an admin, I want to log into the portal with my username and password so that I can manage the platform securely._

**Acceptance Criteria:**  
1. Login form validates credentials against the database.  
2. Successful login redirects to admin dashboard.  
3. Invalid credentials show an error message.  

**Priority:** High  
**Story Points:** 3  
**Notes:** Use JWT or Spring Security for authentication.

---

### 2. Admin Logout
_As an admin, I want to log out of the portal so that system access is protected when I leave._

**Acceptance Criteria:**  
1. Logout button invalidates the active session or JWT.  
2. User is redirected to the login page.  
3. No cached admin pages should remain accessible.  

**Priority:** High  
**Story Points:** 2  
**Notes:** Ensure secure session invalidation.

---

### 3. Add Doctor Profile
_As an admin, I want to add new doctors to the portal so that they can start managing appointments._

**Acceptance Criteria:**  
1. Admin can open an “Add Doctor” form and input details.  
2. System validates required fields (name, specialization, email).  
3. A new doctor record is saved to MySQL.  

**Priority:** High  
**Story Points:** 3  
**Notes:** Trigger a welcome email to the doctor.

---

### 4. Delete Doctor Profile
_As an admin, I want to delete a doctor's profile from the portal so that inactive accounts can be removed._

**Acceptance Criteria:**  
1. Admin can select a doctor and confirm deletion.  
2. Record is removed from the database.  
3. System logs the deletion action.  

**Priority:** Medium  
**Story Points:** 2  
**Notes:** Use soft delete if audit trail is required.

---

### 5. Generate Monthly Appointment Report
_As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month so that I can track usage statistics._

**Acceptance Criteria:**  
1. Stored procedure executes successfully and returns appointment counts per month.  
2. Results can be displayed or exported from the dashboard.  
3. Only admins have permission to execute the procedure.  

**Priority:** Medium  
**Story Points:** 3  
**Notes:** Verify stored procedure performance with sample data.

## Doctor User Stories

### User Story 1
**Title:** Create Doctor Profile  
**As a** doctor, **I want** to create my profile, **so that** patients can find me and learn about my expertise and availability.  
**Acceptance Criteria:**
1. The system allows a doctor to input their name, specialization, and contact details.
2. The profile is visible to patients searching for doctors.
3. The doctor can edit their profile information anytime.  
**Priority:** High  
**Story Points:** 5  
**Notes:** Ensure profile data validation for required fields.

---

### User Story 2
**Title:** Secure Login for Doctor  
**As a** doctor, **I want** to log into the portal, **so that** I can securely access my appointments and update my schedule.  
**Acceptance Criteria:**
1. The system verifies doctor credentials before granting access.
2. Login session is maintained securely using authentication tokens.
3. The doctor is redirected to their dashboard after successful login.  
**Priority:** High  
**Story Points:** 3  
**Notes:** Use password hashing for security.

---

### User Story 3
**Title:** Manage Availability  
**As a** doctor, **I want** to set and update my available time slots, **so that** patients can book appointments based on my availability.  
**Acceptance Criteria:**
1. The doctor can add, edit, or delete available time slots.
2. The system prevents overlapping or duplicate slots.
3. Availability updates reflect in the patient’s booking view.  
**Priority:** High  
**Story Points:** 4  
**Notes:** Time slots should follow a 24-hour format.

---

### User Story 4
**Title:** View Appointments  
**As a** doctor, **I want** to view a list of my upcoming appointments, **so that** I can prepare for each consultation.  
**Acceptance Criteria:**
1. The system displays upcoming appointments with patient names and times.
2. Past appointments are shown separately or archived.
3. The doctor can filter appointments by date or patient name.  
**Priority:** Medium  
**Story Points:** 3  
**Notes:** Include a “Today’s Appointments” quick view section.

---

### User Story 5
**Title:** Reschedule or Cancel Appointments  
**As a** doctor, **I want** to cancel or reschedule appointments, **so that** I can manage unexpected schedule changes efficiently.  
**Acceptance Criteria:**
1. The doctor can cancel or change the time of an existing appointment.
2. Patients are automatically notified about the change.
3. The system logs all changes for audit purposes.  
**Priority:** Medium  
**Story Points:** 4  
**Notes:** Rescheduling must respect appointment duration and slot availability.

## Doctor User Stories

### User Story 1
**Title:** Doctor Login  
**As a** doctor, **I want** to log into the portal, **so that** I can securely manage my appointments.  
**Acceptance Criteria:**
1. The doctor can log in using valid credentials.  
2. The system validates login securely with encrypted passwords.  
3. Successful login redirects the doctor to the dashboard.  
**Priority:** High  
**Story Points:** 3  
**Notes:** Use two-factor authentication for enhanced security.

---

### User Story 2
**Title:** Doctor Logout  
**As a** doctor, **I want** to log out of the portal, **so that** my data and sessions remain secure.  
**Acceptance Criteria:**
1. The system ends the current session securely.  
2. All cookies and tokens are cleared upon logout.  
3. The doctor is redirected to the login screen.  
**Priority:** Medium  
**Story Points:** 2  
**Notes:** Ensure proper session management to prevent unauthorized access.

---

### User Story 3
**Title:** View Appointment Calendar  
**As a** doctor, **I want** to view my appointment calendar, **so that** I can stay organized and manage my schedule efficiently.  
**Acceptance Criteria:**
1. The system displays a calendar view of all appointments.  
2. Each appointment shows patient name, time, and consultation type.  
3. The doctor can filter appointments by date or week.  
**Priority:** High  
**Story Points:** 4  
**Notes:** Calendar should auto-refresh when new bookings are added.

---

### User Story 4
**Title:** Mark Unavailability  
**As a** doctor, **I want** to mark my unavailability, **so that** patients can only book available slots.  
**Acceptance Criteria:**
1. The doctor can mark unavailable days or hours.  
2. The system prevents patients from booking during unavailable times.  
3. The updated availability reflects instantly on the patient booking page.  
**Priority:** High  
**Story Points:** 5  
**Notes:** Add color-coded indicators for available/unavailable slots.

---

### User Story 5
**Title:** Update Profile Information  
**As a** doctor, **I want** to update my profile with specialization and contact information, **so that** patients have up-to-date details.  
**Acceptance Criteria:**
1. The doctor can edit specialization, contact info, and bio.  
2. Changes are reflected immediately in the public doctor list.  
3. Profile data is validated before saving.  
**Priority:** Medium  
**Story Points:** 3  
**Notes:** Add a profile completion progress bar.

---

### User Story 6
**Title:** View Patient Details for Appointments  
**As a** doctor, **I want** to view patient details for upcoming appointments, **so that** I can be prepared for consultations.  
**Acceptance Criteria:**
1. The system displays patient name, age, and medical notes.  
2. Patient information is accessible only to the assigned doctor.  
3. Data access is logged for security compliance.  
**Priority:** High  
**Story Points:** 4  
**Notes:** Display limited patient history for quick reference.
