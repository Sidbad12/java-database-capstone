import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

let selectedDate = new Date().toISOString().split("T")[0];
let token = localStorage.getItem("token");
let patientName = null;

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("searchBar")?.addEventListener("input", (e) => {
    patientName = e.target.value.trim() || "null";
    loadAppointments();
  });

  document.getElementById("todayButton")?.addEventListener("click", () => {
    selectedDate = new Date().toISOString().split("T")[0];
    document.getElementById("datePicker").value = selectedDate;
    loadAppointments();
  });

  document.getElementById("datePicker")?.addEventListener("change", (e) => {
    selectedDate = e.target.value;
    loadAppointments();
  });

  loadAppointments();
});

async function loadAppointments() {
  const tableBody = document.getElementById("patientTableBody");
  tableBody.innerHTML = "<tr><td colspan='5'>Loading...</td></tr>";

  try {
    const appointments = await getAllAppointments(selectedDate, patientName, token);

    tableBody.innerHTML = "";

    if (!appointments || appointments.length === 0) {
      tableBody.innerHTML = "<tr><td colspan='5'>No Appointments found for today</td></tr>";
      return;
    }

    appointments.forEach((appointment) => {
      const row = createPatientRow(appointment);
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error(error);
    tableBody.innerHTML =
      "<tr><td colspan='5'>Error fetching appointments. Please try again.</td></tr>";
  }
}
