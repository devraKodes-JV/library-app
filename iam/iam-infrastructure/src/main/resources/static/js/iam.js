/* =========================================================================
   iam.js - feature-specific JavaScript for the IAM module.
   Handles: DataTables initialization, permission filters on the role form,
   and the real-time notification stream (SSE) for admin pages.
   ========================================================================= */

(function () {
    function initIam() {
        document.querySelectorAll('.alert').forEach(function (alert) {
            setTimeout(function () {
                if (alert.parentElement) {
                    alert.remove();
                }
            }, 5000);
        });

        var dataTables = document.querySelectorAll('table.data-table');
        if (window.jQuery && jQuery.fn && jQuery.fn.DataTable && dataTables.length) {
            dataTables.forEach(function (table) {
                initializeDataTable(table);
            });
        }

        var permForm = document.getElementById('permissionFilterForm');
        if (permForm) {
            initPermissionFilters(permForm);
        }

        var hasToastContainer = document.getElementById('notificationToast') !== null;
        if (hasToastContainer && 'EventSource' in window) {
            initNotificationStream();
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initIam);
    } else {
        initIam();
    }
})();

/**
 * Initializes a single table as a DataTable using sensible defaults.
 *
 * @param {HTMLElement} table the table element
 */
function initializeDataTable(table) {
    if (!table) return;
    if (jQuery.fn.DataTable.isDataTable(table)) {
        return;
    }
    var options = {
        paging: true,
        pageLength: 10,
        searching: true,
        ordering: true,
        info: true,
        responsive: true,
        language: {
            search: "Search:",
            lengthMenu: "Show _MENU_ entries",
            info: "Showing _START_ to _END_ of _TOTAL_ entries",
            infoEmpty: "Showing 0 to 0 of 0 entries",
            infoFiltered: "(filtered from _MAX_ total entries)",
            emptyTable: "No data available in table",
            zeroRecords: "No matching records found",
            paginate: {
                first: "First",
                previous: "Prev",
                next: "Next",
                last: "Last"
            }
        }
    };
    jQuery(table).DataTable(options);
}

/**
 * Sets up the module filter select on the role-permissions form.
 * Supports both the DataTable-based layout and the legacy card layout.
 *
 * @param {HTMLElement} form the form containing the filter controls
 */
function initPermissionFilters(form) {
    var moduleSelect = document.getElementById('filterModule');
    var table = document.getElementById('permissionsTable');

    if (!moduleSelect || !table) {
        return;
    }

    var dataTable = null;
    if (window.jQuery && jQuery.fn && jQuery.fn.DataTable) {
        if (jQuery.fn.DataTable.isDataTable(table)) {
            dataTable = jQuery(table).DataTable();
        } else {
            dataTable = jQuery(table).DataTable({
                paging: true,
                pageLength: 10,
                searching: true,
                ordering: true,
                info: true,
                responsive: true,
                columnDefs: [
                    { orderable: false, targets: 0 },
                    { searchable: true, targets: 3 }
                ],
                language: {
                    search: "Search:",
                    lengthMenu: "Show _MENU_ entries",
                    info: "Showing _START_ to _END_ of _TOTAL_ entries",
                    infoEmpty: "Showing 0 to 0 of 0 entries",
                    infoFiltered: "(filtered from _MAX_ total entries)",
                    emptyTable: "No permissions available",
                    zeroRecords: "No matching permissions found",
                    paginate: {
                        first: "First",
                        previous: "Prev",
                        next: "Next",
                        last: "Last"
                    }
                }
            });
        }

        moduleSelect.addEventListener('change', function () {
            var selectedModule = moduleSelect.value;
            dataTable.column(3).search(selectedModule ? '^' + selectedModule + '$' : '', true, false).draw();
        });

        var selectAll = document.getElementById('selectAllPermissions');
        if (selectAll) {
            selectAll.addEventListener('change', function () {
                var checked = selectAll.checked;
                table.querySelectorAll('.permission-checkbox').forEach(function (cb) {
                    cb.checked = checked;
                });
            });
        }

        table.addEventListener('change', function (e) {
            if (e.target.classList.contains('permission-checkbox')) {
                var checkboxes = table.querySelectorAll('.permission-checkbox');
                var allChecked = checkboxes.length > 0 && Array.prototype.every.call(checkboxes, function (cb) {
                    return cb.checked;
                });
                selectAll.checked = allChecked;
            }
        });

        return;
    }

    // --- Legacy card mode -------------------------------------------------
    var groups = Array.prototype.slice.call(form.querySelectorAll('.permission-group'));

    if (!moduleSelect) {
        return;
    }

    function applyFilters() {
        var selectedModule = moduleSelect.value;

        groups.forEach(function (group) {
            var moduleKey = group.getAttribute('data-module');
            var show = !selectedModule || moduleKey === selectedModule;
            group.style.display = show ? '' : 'none';
        });
    }

    moduleSelect.addEventListener('change', applyFilters);
}

/**
 * Opens a Server-Sent Events connection to the notification stream and shows
 * toasts for incoming events. Only authenticated users with the
 * notifications.stream permission reach this point (server enforces it).
 */
function initNotificationStream() {
    var source = new EventSource('/api/notifications/stream');
    source.onmessage = function (event) {
        var data;
        try {
            data = JSON.parse(event.data);
        } catch (e) {
            return;
        }
        showNotificationToast(data.message || 'Notification received');
    };
    source.onerror = function () {
        // The server closes the stream when the session ends; the browser will
        // reconnect automatically. We just log and keep the page usable.
        console.warn('Notification stream connection lost; will retry.');
    };
}

/**
 * Shows a Bootstrap toast with the given message.
 *
 * @param {string} message the notification text
 */
function showNotificationToast(message) {
    var container = document.getElementById('notificationToast');
    if (!container) {
        return;
    }
    var toast = document.createElement('div');
    toast.className = 'toast align-items-center text-bg-primary border-0 show';
    toast.setAttribute('role', 'alert');
    toast.innerHTML =
        '<div class="d-flex">' +
        '  <div class="toast-body">' + message + '</div>' +
        '  <button type="button" class="btn-close btn-close-white me-2 m-auto" ' +
        'data-bs-dismiss="toast" aria-label="Close"></button>' +
        '</div>';
    container.appendChild(toast);
    if (window.bootstrap && bootstrap.Toast) {
        var bsToast = new bootstrap.Toast(toast, { delay: 5000 });
        bsToast.show();
    }
    // Remove the DOM element after the toast hides.
    toast.addEventListener('hidden.bs.toast', function () {
        toast.remove();
    });
}
