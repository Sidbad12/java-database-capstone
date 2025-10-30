import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { patientLogin, patientSignup } from "./services/patientServices.js";

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("patientSignup")?.addEventListener("click", () => openModal("patientSignup"));
  document.getElementById("patientLogin")?.addEventListener("click", () => openModal("patientLogin"));

  document.getElementById("searchBar")?.addEventListener("input", filterDoctorsOnChange);
  document.getElementById("filterTime")?.addEventListener("change", filterDoctorsOnChange);
  document.getElementById("filterSpecialty")?.addEventListener("change", filterDoctorsOnChange);

  loadDoctorCards();
});

async function loadDoctorCards() {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "<p>Loading doctors...</p>";

  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    contentDiv.innerHTML = "<p>Failed to load doctors.</p>";
  }
}

async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar").value.trim();
  const time = document.getElementById("filterTime").value;
  const specialty = document.getElementById("filterSpecialty").value;

  const doctors = await filterDoctors(name, time, specialty);
  renderDoctorCards(doctors);
}

function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  if (!doctors || doctors.length === 0) {
    contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
    return;
  }

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

window.signupPatient = async function () {
  const name = document.getElementById("signupName").value.trim();
  const email = document.getElementById("signupEmail").value.trim();
  const password = document.getElementById("signupPassword").value.trim();
  const phone = document.getElementById("signupPhone").value.trim();
  const address = document.getElementById("signupAddress").value.trim();

  try {
    const result = await patientSignup({ name, email, password, phone, address });
    if (result.success) {
      alert("Signup successful!");
      window.location.reload();
    } else {
      alert("Signup failed.");
    }
  } catch (error) {
    alert("Error signing up.");
  }
};

window.loginPatient = async function () {
  const email = document.getElementById("loginEmail").value.trim();
  const password = document.getElementById("loginPassword").value.trim();

  try {
    const result = await patientLogin({ email, password });
    if (result.token) {
      localStorage.setItem("token", result.token);
      alert("Login successful!");
      window.location.href = "loggedPatientDashboard.html";
    } else {
      alert("Invalid credentials.");
    }
  } catch (error) {
    alert("Error logging in.");
  }
};
