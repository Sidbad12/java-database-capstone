cat <<'EOF' > footer.js
function renderFooter() {
  const footer = document.getElementById("footer");
  if (!footer) return;

  footer.innerHTML = `
    <footer class="footer">
      <div class="footer-logo">
        <img src="/static/assets/images/logo/logo.png" alt="Clinic Logo" width="60" />
        <p>© 2025 Smart Clinic. All rights reserved.</p>
      </div>
      <div class="footer-columns">
        <div class="footer-column">
          <h4>Company</h4>
          <a href="#">About</a>
          <a href="#">Careers</a>
          <a href="#">Press</a>
        </div>
        <div class="footer-column">
          <h4>Support</h4>
          <a href="#">Account</a>
          <a href="#">Help Center</a>
          <a href="#">Contact</a>
        </div>
        <div class="footer-column">
          <h4>Legal</h4>
          <a href="#">Terms</a>
          <a href="#">Privacy Policy</a>
          <a href="#">Licensing</a>
        </div>
      </div>
    </footer>`;
}

renderFooter();
EOF
