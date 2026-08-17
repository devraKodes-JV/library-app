/* =========================================================================
   works-form.js - form validation for the Work create/edit form.
   ========================================================================= */

(function () {
    function initWorksForm() {
        var form = document.querySelector('form[novalidate]');
        if (!form) return;
        form.addEventListener('submit', function (e) {
            var valid = true;
            var title = document.getElementById('title');
            var subtitle = document.getElementById('subtitle');
            var summary = document.getElementById('summary');
            var alphaRegex = /^[a-zA-Z0-9\s'-]+$/;

            if (!title.value.trim()) {
                title.classList.add('is-invalid');
                title.classList.remove('is-valid');
                valid = false;
            } else if (!alphaRegex.test(title.value.trim())) {
                title.classList.add('is-invalid');
                title.classList.remove('is-valid');
                valid = false;
            } else {
                title.classList.remove('is-invalid');
                title.classList.add('is-valid');
            }

            if (subtitle.value.length > 500) {
                subtitle.classList.add('is-invalid');
                subtitle.classList.remove('is-valid');
                valid = false;
            } else if (subtitle.value.trim() && !alphaRegex.test(subtitle.value.trim())) {
                subtitle.classList.add('is-invalid');
                subtitle.classList.remove('is-valid');
                valid = false;
            } else if (subtitle.value.trim()) {
                subtitle.classList.remove('is-invalid');
                subtitle.classList.add('is-valid');
            }

            if (summary.value.length > 2000) {
                summary.classList.add('is-invalid');
                summary.classList.remove('is-valid');
                valid = false;
            } else if (summary.value.trim()) {
                summary.classList.remove('is-invalid');
                summary.classList.add('is-valid');
            }

            var authorCheckboxes = document.querySelectorAll('input[name="authorIds"]');
            var authorChecked = Array.from(authorCheckboxes).some(function(cb) { return cb.checked; });

            if (!authorChecked) {
                valid = false;
                var authorTable = document.querySelector('input[name="authorIds"]');
                if (authorTable) {
                    authorTable.closest('.card').classList.add('border-danger');
                }
            } else {
                var authorTableEl = document.querySelector('input[name="authorIds"]');
                if (authorTableEl && authorTableEl.closest('.card')) {
                    authorTableEl.closest('.card').classList.remove('border-danger');
                }
            }

            if (!valid) {
                e.preventDefault();
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initWorksForm);
    } else {
        initWorksForm();
    }
})();
