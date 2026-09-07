(function() {
    'use strict';

    function ready(fn) {
        if (document.readyState !== 'loading') fn();
        else document.addEventListener('DOMContentLoaded', fn);
    }

    function getCookie(name) {
        var match = document.cookie.match(new RegExp('(^|; )' + name + '=([^;]*)'));
        return match ? decodeURIComponent(match[2]) : null;
    }
    function csrfHeaders(extra) {
        var h = Object.assign({}, extra || {});
        var token = getCookie('XSRF-TOKEN');
        if (token) h['X-XSRF-TOKEN'] = token;
        return h;
    }

    function fmt(amount) {
        var n = Number(amount || 0);
        return '$ ' + n.toFixed(2);
    }

    ready(function() {
        var btn = document.getElementById('returnBtn');
        var modalEl = document.getElementById('returnWizardModal');
        if (!btn || !modalEl) return;
        if (!window.bootstrap || !window.bootstrap.Modal) {
            console.error('[return] Bootstrap Modal not available');
            return;
        }

        var reservationId = btn.getAttribute('data-reservation-id');
        var errorBox = document.getElementById('returnError');
        var stepper = document.getElementById('returnStepper');
        var stepEls = modalEl.querySelectorAll('.return-step');
        var prevBtn = document.getElementById('returnPrev');
        var nextBtn = document.getElementById('returnNext');
        var confirmBtn = document.getElementById('returnConfirm');
        var confirmedCheckbox = document.getElementById('returnPaymentConfirmed');
        var currentStep = 1;
        var breakdown = null;
        var modal = new window.bootstrap.Modal(modalEl);

        function showError(msg) {
            if (!errorBox) return;
            errorBox.textContent = msg;
            errorBox.classList.remove('d-none');
        }
        function clearError() {
            if (!errorBox) return;
            errorBox.textContent = '';
            errorBox.classList.add('d-none');
        }
        function refreshNav() {
            stepEls.forEach(function(s) {
                var n = parseInt(s.getAttribute('data-step'), 10);
                s.classList.toggle('d-none', n !== currentStep);
            });
            if (stepper) {
                stepper.querySelectorAll('li').forEach(function(li) {
                    var n = parseInt(li.getAttribute('data-step'), 10);
                    li.classList.toggle('active', n === currentStep);
                    li.classList.toggle('text-light', n <= currentStep);
                    li.classList.toggle('text-muted', n > currentStep);
                });
            }
            if (prevBtn) prevBtn.classList.toggle('d-none', currentStep === 1);
            if (nextBtn) nextBtn.classList.toggle('d-none', currentStep === 3);
            if (confirmBtn) confirmBtn.classList.toggle('d-none', currentStep !== 3);
            refreshConfirmState();
        }
        function refreshConfirmState() {
            var checked = confirmedCheckbox && confirmedCheckbox.checked;
            if (currentStep === 3) {
                if (confirmBtn) confirmBtn.disabled = !checked;
            } else {
                if (nextBtn) nextBtn.disabled = false;
            }
        }
        function validateStep() {
            if (currentStep === 1) {
                if (!breakdown) { showError('Breakdown not loaded yet.'); return false; }
            } else if (currentStep === 2) {
                var method = document.getElementById('returnPaymentMethod').value;
                if (!method) { showError('Select a payment method.'); return false; }
            }
            return true;
        }

        async function loadBreakdown() {
            var resp = await fetch('/reservations/' + reservationId + '/return/preview', {
                method: 'GET',
                credentials: 'same-origin',
                headers: csrfHeaders({ 'Accept': 'application/json' })
            });
            if (!resp.ok) {
                var t = await resp.text().catch(function() { return ''; });
                throw new Error('Could not load return breakdown: HTTP ' + resp.status + (t ? ': ' + t.substring(0, 200) : ''));
            }
            return resp.json();
        }

        function renderBreakdown() {
            var lateFeeEl = document.getElementById('returnLateFee');
            var depositEl = document.getElementById('returnDeposit');
            var settlementEl = document.getElementById('returnSettlement');
            var confirmLateFee = document.getElementById('confirmReturnLateFee');
            var confirmDeposit = document.getElementById('confirmReturnDeposit');
            var confirmSettlement = document.getElementById('confirmReturnSettlement');
            if (lateFeeEl) lateFeeEl.textContent = fmt(breakdown.lateFee);
            if (depositEl) depositEl.textContent = fmt(breakdown.deposit);
            if (settlementEl) settlementEl.textContent = fmt(breakdown.settlement);
            if (confirmLateFee) confirmLateFee.textContent = fmt(breakdown.lateFee);
            if (confirmDeposit) confirmDeposit.textContent = fmt(breakdown.deposit);
            if (confirmSettlement) confirmSettlement.textContent = fmt(breakdown.settlement);
        }

        if (nextBtn) nextBtn.addEventListener('click', function() {
            clearError();
            if (!validateStep()) return;
            if (currentStep === 2) {
                var method = document.getElementById('returnPaymentMethod').value;
                var confirmMethod = document.getElementById('confirmReturnMethod');
                if (confirmMethod) confirmMethod.textContent = method;
            }
            if (currentStep < 3) { currentStep++; refreshNav(); }
        });
        if (prevBtn) prevBtn.addEventListener('click', function() {
            clearError();
            if (currentStep > 1) { currentStep--; refreshNav(); }
        });
        if (confirmedCheckbox) confirmedCheckbox.addEventListener('change', refreshConfirmState);

        btn.addEventListener('click', async function() {
            clearError();
            currentStep = 1;
            refreshNav();
            modal.show();
            try {
                breakdown = await loadBreakdown();
                renderBreakdown();
                refreshNav();
            } catch (err) {
                showError(err.message || String(err));
            }
        });

        if (new URLSearchParams(window.location.search).get('wizard') === 'return') {
            currentStep = 1;
            refreshNav();
            modal.show();
            loadBreakdown().then(function(b) {
                breakdown = b;
                renderBreakdown();
                refreshNav();
            }).catch(function(err) { showError(err.message || String(err)); });
        }

        modalEl.addEventListener('hidden.bs.modal', function() {
            currentStep = 1;
            refreshNav();
            clearError();
        });

        if (confirmBtn) confirmBtn.addEventListener('click', async function() {
            clearError();
            confirmBtn.disabled = true;
            nextBtn.disabled = true;
            prevBtn.disabled = true;
            try {
                var method = document.getElementById('returnPaymentMethod').value;
                var fd = new FormData();
                fd.append('paymentMethod', method);
                fd.append('paymentConfirmed', 'true');
                var resp = await fetch('/reservations/' + reservationId + '/return', {
                    method: 'POST',
                    body: fd,
                    credentials: 'same-origin',
                    headers: csrfHeaders({ 'Accept': 'application/json', 'X-Requested-With': 'XMLHttpRequest' })
                });
                if (!resp.ok) {
                    var t = await resp.text().catch(function() { return ''; });
                    var msg = 'Return failed: HTTP ' + resp.status;
                    try {
                        var j = JSON.parse(t);
                        if (j && j.error) msg = j.error;
                    } catch (e) { if (t) msg += ' — ' + t.substring(0, 200); }
                    throw new Error(msg);
                }
                window.location.href = '/reservations';
            } catch (err) {
                showError(err.message || String(err));
                confirmBtn.disabled = false;
                nextBtn.disabled = false;
                prevBtn.disabled = false;
                refreshConfirmState();
            }
        });

        refreshNav();
    });
})();
