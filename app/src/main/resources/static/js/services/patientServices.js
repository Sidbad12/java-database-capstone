// patientServices.js
import { API_BASE_URL } from "../config/config.js";

const PATIENT_API = API_BASE_URL + "/patient";

// ✅ Patient signup
export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}/signup`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    const result = await response.json();
    if (!response.ok) throw new Error(result.message || "Signup failed");

    return { success: true, message: result.message || "Signup successful" };
  } catch (error) {
    console.error("Error during signup:", error);
    return { success: false, message: error.message };
  }
}

// ✅ Patient login
export async function patientLogin(data) {
  try {
    const response = await fetch(`${PATIENT_API}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    return response; // Let frontend handle token extraction
  } catch (error) {
    console.error("Error during login:", error);
    return null;
  }
}

// ✅ Get logged-in patient data
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/details`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Failed to fetch patient data");
    const patient = await response.json();
    return patient;
  } catch (error) {
    console.error("Error fetching patient data:", error);
    return null;
  }
}

// ✅ Get patient appointments
export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(`${PATIENT_API}/appointments/${id}?user=${user}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Failed to fetch appointments");
    const appointments = await response.json();
    return appointments || [];
  } catch (error) {
    console.error("Error fetching appointments:", error);
    return [];
  }
}

// ✅ Filter appointments
export async function filterAppointments(condition = "", name = "", token = "") {
  try {
    const response = await fetch(
      `${PATIENT_API}/filter?condition=${condition}&name=${name}`,
      { headers: { Authorization: `Bearer ${token}` } }
    );

    if (!response.ok) throw new Error("Failed to filter appointments");
    const appointments = await response.json();
    return appointments || [];
  } catch (error) {
    console.error("Error filtering appointments:", error);
    return [];
  }
}
