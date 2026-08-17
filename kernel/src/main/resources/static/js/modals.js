/**
 * Custom confirmation modal functionality.
 * Replaces native confirm() with Bootstrap modals.
 */
(function () {
    'use strict';

    window.showConfirmModal = function (title, message, onConfirm) {
        var modalEl = document.getElementById('confirmModal');
        if (!modalEl) {
            if (onConfirm) onConfirm();
            return;
        }

        var titleEl = document.getElementById('confirmModalTitle');
        var bodyEl = document.getElementById('confirmModalBody');
        var actionBtn = document.getElementById('confirmModalAction');

        if (titleEl) titleEl.textContent = title || 'Confirm';
        if (bodyEl) bodyEl.innerHTML = message || 'Are you sure?';
        if (actionBtn) actionBtn.className = 'btn btn-danger';

        var bsModal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        bsModal.show();

        var confirmed = false;
        var handler = function () {
            if (!confirmed) {
                confirmed = true;
                if (onConfirm) onConfirm();
            }
        };

        actionBtn.addEventListener('click', handler);
        
        var cleanup = function () {
            actionBtn.removeEventListener('click', handler);
            modalEl.removeEventListener('hidden.bs.modal', cleanup);
        };
        
        modalEl.addEventListener('hidden.bs.modal', cleanup);
    };

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('form[data-confirm-title]').forEach(function (form) {
            var submitHandler = function (e) {
                e.preventDefault();
                var title = form.getAttribute('data-confirm-title') || 'Confirm';
                var message = form.getAttribute('data-confirm-message') || 'Are you sure?';
                window.showConfirmModal(title, message, function () {
                    form.removeEventListener('submit', submitHandler);
                    form.submit();
                });
            };
            form.addEventListener('submit', submitHandler);
        });
    });
})();
