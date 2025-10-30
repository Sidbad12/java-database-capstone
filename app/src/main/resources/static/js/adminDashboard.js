import { openModal } from "../components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

document.addEventListener("DOMContentLoaded", () => {
  const addBtn = document.getElementById("addDocBtn");
  if (addBtn) {
    addBtn.addEventListener("click", () => openModal("addDoctor"));
  }

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
    console.error(error);
    contentDiv.innerHTML = "<p>Failed to load doctors.</p>";
  }
}

async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar").value.trim();
  const time = document.getElementById("filterTime").value;
  const specialty = document.getElementById("filterSpecialty").value;

  try {
    const doctors = await filterDoctors(name, time, specialty);
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error filtering doctors:", error);
  }
}

function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  if (!doctors || doctors.length === 0) {
    contentDiv.innerHTML = "<p>No doctors found.</p>";
    return;
  }

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

window.adminAddDoctor = async function () {
  const name = document.getElementById("docName").value.trim();
  const email = document.getElementById("docEmail").value.trim();
  const password = document.getElementById("docPassword").value.trim();
  const mobile = document.getElementById("docMobile").value.trim();
  const specialty = document.getElementById("docSpecialty").value;
  const availability = Array.from(document.querySelectorAll(".availability:checked")).map(
    (input) => input.value
  );
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Unauthorized. Please log in again.");
    return;
  }

  const newDoctor = { name, email, password, mobile, specialty, availability };

  try {
    const response = await saveDoctor(newDoctor, token);
    if (response && response.success !== false) {
      alert("Doctor added successfully!");
      window.location.reload();
    } else {
      alert("Failed to add doctor.");
    }
  } catch (error) {
    console.error(error);
    alert("Error adding doctor.");
  }
};
