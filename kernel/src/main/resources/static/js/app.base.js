/* =========================================================================
   app.js - custom application JavaScript for the Library management system.
   ========================================================================= */

function initApp() {
    document.querySelectorAll('.alert').forEach(function (alert) {
        setTimeout(function () {
            if (alert.parentElement) {
                alert.remove();
            }
        }, 5000);
    });

    var sections = document.querySelectorAll('.nav-section');
    sections.forEach(function (section) {
        var title = section.querySelector('.nav-section-title');
        var items = section.querySelector('.nav-section-items');
        if (!title || !items) return;
        title.addEventListener('click', function () {
            var isOpen = items.style.display === 'block';
            sections.forEach(function (s) {
                var si = s.querySelector('.nav-section-items');
                if (si) si.style.display = 'none';
                s.classList.remove('open');
            });
            if (!isOpen) {
                items.style.display = 'block';
                section.classList.add('open');
            }
        });
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initApp);
} else {
    initApp();
}
