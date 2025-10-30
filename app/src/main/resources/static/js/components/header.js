cat <<'EOF' > header.js
function renderHeader() {
  const headerDiv = document.getElementById("header");
  if (!headerDiv) return;

  // Reset header for homepage
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
  }

  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  let headerContent = `<header><h2>Smart Clinic</h2><nav>`;

  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn">Add Doctor</button>
      <a href="#" id="logoutLink">Logout</a>`;
  } else if (role === "doctor") {
    headerContent += `
      <a href="/pages/doctorDashboard.html">Home</a>
      <a href="#" id="logoutLink">Logout</a>`;
  } else if (role === "patient") {
    headerContent += `
      <a href="/">Login</a>
      <a href="/pages/patientDashboard.html">Sign Up</a>`;
  } else if (role === "loggedPatient") {
    headerContent += `
      <a href="/pages/patientDashboard.html">Home</a>
      <a href="/pages/patientAppointments.html">Appointments</a>
      <a href="#" id="logoutPatientLink">Logout</a>`;
  }

  headerContent += `</nav></header>`;
  headerDiv.innerHTML = headerContent;
  attachHeaderButtonListeners();
}

function attachHeaderButtonListeners() {
  const addDocBtn = document.getElementById("addDocBtn");
  if (addDocBtn) addDocBtn.addEventListener("click", () => openModal("addDoctor"));

  const logoutLink = document.getElementById("logoutLink");
  if (logoutLink) logoutLink.addEventListener("click", logout);

  const logoutPatientLink = document.getElementById("logoutPatientLink");
  if (logoutPatientLink) logoutPatientLink.addEventListener("click", logoutPatient);
}

function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("userRole");
  window.location.href = "/";
}

function logoutPatient() {
  localStorage.removeItem("token");
  localStorage.setItem("userRole", "patient");
  window.location.href = "/pages/patientDashboard.html";
}

renderHeader();
EOF
