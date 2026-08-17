/**
 * Flash messages toast functionality.
 * Shows toast notifications and auto-dismisses after 5 seconds.
 */
(function () {
    'use strict';

    function showToast(toastEl) {
        var toast = new bootstrap.Toast(toastEl, {
            autohide: true,
            delay: 5000
        });
        toast.show();
    }

    document.addEventListener('DOMContentLoaded', function () {
        var toastContainer = document.getElementById('notificationToast');
        if (!toastContainer) return;

        var toasts = document.querySelectorAll('.toast');
        toasts.forEach(function (toastEl) {
            toastContainer.appendChild(toastEl);
            showToast(toastEl);
        });
    });
})();
