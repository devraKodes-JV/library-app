/* =========================================================================
   books.js - feature-specific JavaScript for the Books module.
   Handles: DataTables initialization for book entity tables.
   ========================================================================= */

(function () {
    function initBooks() {
        if (window.jQuery && jQuery.fn && jQuery.fn.DataTable) {
            var dataTables = document.querySelectorAll('table.data-table');
            dataTables.forEach(function (table) {
                initializeDataTable(table);
            });
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initBooks);
    } else {
        initBooks();
    }
})();

function initializeDataTable(table) {
    if (!table) return;
    if (jQuery.fn.DataTable.isDataTable(table)) {
        return;
    }
    var isCompact = table.classList.contains('no-search-info-length');
    var isPagination = table.classList.contains('compact-pagination');
    var emptyMessage = table.getAttribute('data-empty-message') || 'No data available in table';
    var options = {
        paging: true,
        pageLength: isPagination ? 3 : 10,
        searching: !isCompact,
        ordering: true,
        info: !isPagination,
        lengthChange: !isPagination,
        responsive: true,
        language: {
            search: "Search:",
            lengthMenu: !isPagination ? "Show _MENU_ entries" : "",
            info: !isPagination ? "Showing _START_ to _END_ of _TOTAL_ entries" : "",
            infoEmpty: !isPagination ? "Showing 0 to 0 of 0 entries" : "",
            infoFiltered: !isPagination ? "(filtered from _MAX_ total entries)" : "",
            emptyTable: emptyMessage,
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

    if (isPagination || isCompact) {
        var style = document.createElement('style');
        style.textContent = '.compact-pagination .dataTables_paginate .paginate_button, .no-search-info-length .dataTables_paginate .paginate_button { color: #fff !important; } .compact-pagination .dataTables_paginate .paginate_button:hover, .no-search-info-length .dataTables_paginate .paginate_button:hover { color: #fff !important; }';
        document.head.appendChild(style);
    }
}
