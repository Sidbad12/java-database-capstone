// doctorServices.js
import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = API_BASE_URL + "/doctor";

// ✅ Get all doctors
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);
    if (!response.ok) throw new Error("Failed to fetch doctors");
    const doctors = await response.json();
    return doctors || [];
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return [];
  }
}

// ✅ Delete a doctor (Admin only)
export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Failed to delete doctor");
    }

    const result = await response.json();
    return { success: true, message: result.message || "Doctor deleted successfully" };
  } catch (error) {
    console.error("Error deleting doctor:", error);
    return { success: false, message: error.message };
  }
}

// ✅ Save (Add) a new doctor
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(doctor),
    });

    if (!response.ok) throw new Error("Failed to save doctor");

    const result = await response.json();
    return { success: true, message: result.message || "Doctor added successfully" };
  } catch (error) {
    console.error("Error saving doctor:", error);
    return { success: false, message: error.message };
  }
}

// ✅ Filter doctors
export async function filterDoctors(name = "", time = "", specialty = "") {
  try {
    const response = await fetch(
      `${DOCTOR_API}/filter?name=${name}&time=${time}&specialty=${specialty}`
    );
    if (!response.ok) throw new Error("Failed to filter doctors");

    const doctors = await response.json();
    return doctors || [];
  } catch (error) {
    console.error("Error filtering doctors:", error);
    return [];
  }
}
