// index.js
import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";

// Define API endpoints
const ADMIN_API = API_BASE_URL + "/admin";
const DOCTOR_API = API_BASE_URL + "/doctor/login";

// Ensure script runs after DOM loads
window.onload = function () {
  const adminBtn = document.getElementById("adminLogin");
  const doctorBtn = document.getElementById("doctorLogin");

  if (adminBtn) {
    adminBtn.addEventListener("click", () => {
      openModal("adminLogin");
    });
  }

  if (doctorBtn) {
    doctorBtn.addEventListener("click", () => {
      openModal("doctorLogin");
    });
  }
};

// ========== ADMIN LOGIN HANDLER ==========
window.adminLoginHandler = async function () {
  const username = document.getElementById("adminUsername").value;
  const password = document.getElementById("adminPassword").value;

  const admin = { username, password };

  try {
    const response = await fetch(`${ADMIN_API}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(admin),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      selectRole("admin");
    } else {
      alert("Invalid credentials!");
    }
  } catch (error) {
    console.error("Error during admin login:", error);
    alert("Something went wrong! Please try again.");
  }
};

// ========== DOCTOR LOGIN HANDLER ==========
window.doctorLoginHandler = async function () {
  const email = document.getElementById("doctorEmail").value;
  const password = document.getElementById("doctorPassword").value;

  const doctor = { email, password };

  try {
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      selectRole("doctor");
    } else {
      alert("Invalid credentials!");
    }
  } catch (error) {
    console.error("Error during doctor login:", error);
    alert("Something went wrong! Please try again.");
  }
};

// ========== ROLE SELECTOR ==========
function selectRole(role) {
  localStorage.setItem("userRole", role);
  if (role === "admin") {
    window.location.href = "/templates/admin/adminDashboard.html";
  } else if (role === "doctor") {
    window.location.href = "/templates/doctor/doctorDashboard.html";
  }
}
