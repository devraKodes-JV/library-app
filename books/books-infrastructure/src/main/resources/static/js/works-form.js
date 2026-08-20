/* =========================================================================
   works-form.js - form validation for the Work create/edit form.
   ========================================================================= */

(function () {
    function initWorksForm() {
        var form = document.querySelector('form[novalidate]');
        if (!form) return;
        
        var title = document.getElementById('title');
        var subtitle = document.getElementById('subtitle');
        var summary = document.getElementById('summary');
        var titleError = document.getElementById('titleError');
        var subtitleError = document.getElementById('subtitleError');
        var summaryError = document.getElementById('summaryError');
        var authorsError = document.getElementById('authorsError');
        var submitBtn = form.querySelector('button[type="submit"]');
        var alphaRegex = /^[a-zA-ZÀ-ÿ0-9\s'-]+$/;

        function isTitleValid() {
            if (!title.value.trim()) return false;
            if (!alphaRegex.test(title.value.trim())) return false;
            if (title.value.trim().length > 200) return false;
            return true;
        }

        function isSubtitleValid() {
            if (!subtitle.value.trim()) return true;
            if (subtitle.value.trim().length > 500) return false;
            if (!alphaRegex.test(subtitle.value.trim())) return false;
            return true;
        }

        function isSummaryValid() {
            if (!summary.value.trim()) return true;
            if (summary.value.trim().length > 2000) return false;
            return true;
        }

        function isAuthorsValid() {
            var authorCheckboxes = document.querySelectorAll('input[name="authorIds"]');
            var authorChecked = Array.from(authorCheckboxes).some(function(cb) { return cb.checked; });
            if (!authorChecked) return false;
            var missingRole = false;
            authorCheckboxes.forEach(function(cb) {
                if (cb.checked) {
                    var roleSelect = document.querySelector('select[name="authorRoleId_' + cb.value + '"]');
                    if (roleSelect && !roleSelect.value) missingRole = true;
                }
            });
            if (missingRole) return false;
            return true;
        }

        function validateTitle() {
            if (!isTitleValid()) {
                if (!title.value.trim()) {
                    titleError.textContent = 'Title is required.';
                } else if (!alphaRegex.test(title.value.trim())) {
                    titleError.textContent = 'Title must contain only letters, numbers, spaces, hyphens or apostrophes.';
                } else {
                    titleError.textContent = 'Title must be 200 characters or less.';
                }
                title.classList.add('is-invalid');
                title.classList.remove('is-valid');
                return false;
            }
            titleError.textContent = '';
            title.classList.remove('is-invalid');
            title.classList.add('is-valid');
            return true;
        }

        function validateSubtitle() {
            if (!isSubtitleValid()) {
                if (subtitle.value.trim().length > 500) {
                    subtitleError.textContent = 'Subtitle must be 500 characters or less.';
                } else {
                    subtitleError.textContent = 'Subtitle must contain only letters, numbers, hyphens or apostrophes.';
                }
                subtitle.classList.add('is-invalid');
                subtitle.classList.remove('is-valid');
                return false;
            }
            subtitleError.textContent = '';
            subtitle.classList.remove('is-invalid');
            if (subtitle.value.trim()) subtitle.classList.add('is-valid');
            return true;
        }

        function validateSummary() {
            if (!isSummaryValid()) {
                summaryError.textContent = 'Summary must be 2000 characters or less.';
                summary.classList.add('is-invalid');
                summary.classList.remove('is-valid');
                return false;
            }
            summaryError.textContent = '';
            summary.classList.remove('is-invalid');
            if (summary.value.trim()) summary.classList.add('is-valid');
            return true;
        }

        function validateAuthors() {
            var authorCheckboxes = document.querySelectorAll('input[name="authorIds"]');
            var authorChecked = Array.from(authorCheckboxes).some(function(cb) { return cb.checked; });
            
            if (!authorChecked) {
                authorsError.textContent = 'At least one author is required.';
                var authorTable = document.querySelector('input[name="authorIds"]');
                if (authorTable) {
                    authorTable.closest('.card').classList.add('border-danger');
                }
                return false;
            }
            
            var authorTableEl = document.querySelector('input[name="authorIds"]');
            if (authorTableEl && authorTableEl.closest('.card')) {
                authorTableEl.closest('.card').classList.remove('border-danger');
            }
            
            var missingRole = false;
            authorCheckboxes.forEach(function(cb) {
                if (cb.checked) {
                    var roleSelect = document.querySelector('select[name="authorRoleId_' + cb.value + '"]');
                    if (roleSelect && !roleSelect.value) {
                        missingRole = true;
                        roleSelect.classList.add('is-invalid');
                    } else if (roleSelect) {
                        roleSelect.classList.remove('is-invalid');
                    }
                }
            });
            
            if (missingRole) {
                authorsError.textContent = 'Role is required for each selected author.';
                return false;
            }
            
            authorsError.textContent = '';
            return true;
        }

        function updateSubmitButton() {
            var valid = isTitleValid() && isSubtitleValid() && isSummaryValid() && isAuthorsValid();
            submitBtn.disabled = !valid;
            return valid;
        }

        title.addEventListener('input', function() {
            validateTitle();
            updateSubmitButton();
        });
        title.addEventListener('blur', function() {
            validateTitle();
            updateSubmitButton();
        });
        
        subtitle.addEventListener('input', function() {
            validateSubtitle();
            updateSubmitButton();
        });
        subtitle.addEventListener('blur', function() {
            validateSubtitle();
            updateSubmitButton();
        });
        
        summary.addEventListener('input', function() {
            validateSummary();
            updateSubmitButton();
        });
        summary.addEventListener('blur', function() {
            validateSummary();
            updateSubmitButton();
        });
        
        document.querySelectorAll('input[name="authorIds"]').forEach(function(cb) {
            cb.addEventListener('change', function() {
                validateAuthors();
                updateSubmitButton();
            });
        });
        
        document.querySelectorAll('select[name^="authorRoleId_"]').forEach(function(select) {
            select.addEventListener('change', function() {
                validateAuthors();
                updateSubmitButton();
            });
        });

        form.addEventListener('submit', function(e) {
            updateSubmitButton();
            if (submitBtn.disabled) {
                e.preventDefault();
            }
        });
        
        updateSubmitButton();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initWorksForm);
    } else {
        initWorksForm();
    }
})();
