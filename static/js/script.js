// Simple script for client-side functionality

document.addEventListener('DOMContentLoaded', function() {
    // Password confirmation validation for registration
    const registerForm = document.querySelector('form[action*="register"]');
    if (registerForm) {
        const password = document.getElementById('password');
        const confirmPassword = document.getElementById('confirm-password');

        registerForm.addEventListener('submit', function(e) {
            if (password.value !== confirmPassword.value) {
                e.preventDefault();
                alert('Passwords do not match!');
            }
        });
    }

    // Confirm vote submission
    const voteForms = document.querySelectorAll('form[action*="vote"]');
    voteForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const candidateName = this.closest('.candidate-card').querySelector('h3').textContent;
            if (!confirm(`Are you sure you want to vote for ${candidateName}? You cannot change your vote later.`)) {
                e.preventDefault();
            }
        });
    });

    // Add some interactivity to admin page
    const adminTable = document.querySelector('.admin-table');
    if (adminTable) {
        const rows = adminTable.querySelectorAll('tbody tr');
        rows.forEach(row => {
            row.addEventListener('click', function() {
                this.classList.toggle('selected');
            });
        });
    }

    // Add smooth scrolling for navigation
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetElement = document.querySelector(targetId);

            if (targetElement) {
                window.scrollTo({
                    top: targetElement.offsetTop - 100,
                    behavior: 'smooth'
                });
            }
        });
    });
});

// Function to show notification
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;

    document.body.appendChild(notification);

    // Auto-dismiss after 3 seconds
    setTimeout(() => {
        notification.classList.add('hide');
        setTimeout(() => {
            notification.remove();
        }, 500);
    }, 3000);
}