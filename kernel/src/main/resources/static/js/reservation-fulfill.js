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

    async function postForm(url, form) {
        var resp = await fetch(url, {
            method: 'POST',
            body: new FormData(form),
            credentials: 'same-origin',
            headers: csrfHeaders({ 'Accept': 'application/json' })
        });
        if (!resp.ok) {
            var t = await resp.text().catch(function() { return ''; });
            throw new Error('HTTP ' + resp.status + (t ? ': ' + t.substring(0, 200) : ''));
        }
        return resp;
    }

    function bindWizard(opts) {
        var modal = new window.bootstrap.Modal(opts.modalEl);
        var errorBox = opts.errorBox;
        var stepper = opts.stepper;
        var stepEls = opts.modalEl.querySelectorAll('.fulfill-step');
        var prevBtn = opts.prevBtn;
        var nextBtn = opts.nextBtn;
        var confirmBtn = opts.confirmBtn;
        var currentStep = 1;
        var reservationId = opts.reservationId;
        var confirmedCheckbox = opts.confirmedCheckbox;

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
            if (nextBtn) nextBtn.classList.toggle('d-none', currentStep === opts.totalSteps);
            if (confirmBtn) confirmBtn.classList.toggle('d-none', currentStep !== opts.totalSteps);
        }
        function refreshConfirmState() {
            if (!confirmBtn) return;
            var checked = confirmedCheckbox && confirmedCheckbox.checked;
            confirmBtn.disabled = currentStep !== opts.totalSteps || !checked;
        }

        function validateStep() {
            if (currentStep === 1 && opts.needsClient) {
                var nameEl = document.getElementById('clientFullName');
                var name = nameEl ? nameEl.value.trim() : '';
                console.log('[fulfill] validate step1 needsClient=true name=', name);
                if (!name) { showError('Full name is required.'); return false; }
            } else if (currentStep === 2) {
                var amtEl = document.getElementById('depositAmount');
                var amt = amtEl ? (amtEl.getAttribute('data-raw') || '').trim() : '';
                var methodEl = document.getElementById('paymentMethod');
                var method = methodEl ? methodEl.value.trim() : '';
                console.log('[fulfill] validate step2 amt=', amt, 'method=', method);
                if (amt === '' || isNaN(parseFloat(amt)) || parseFloat(amt) < 0) {
                    showError('Deposit amount is required.'); return false;
                }
                if (!method) { showError('Select a payment method.'); return false; }
            }
            return true;
        }
        function populateConfirm() {
            var cf = document.getElementById('confirmClientName');
            var ca = document.getElementById('confirmAmount');
            var cm = document.getElementById('confirmMethod');
            if (opts.needsClient && cf) cf.textContent = document.getElementById('clientFullName').value.trim();
            if (!opts.needsClient && cf) cf.textContent = '— (already on file) —';
            if (ca) ca.textContent = parseFloat(document.getElementById('depositAmount').value).toFixed(2);
            if (cm) cm.textContent = document.getElementById('paymentMethod').value;
        }

        if (nextBtn) nextBtn.addEventListener('click', function() {
            clearError();
            if (!validateStep()) return;
            if (currentStep === opts.totalSteps - 1) {
                populateConfirm();
            }
            if (currentStep < opts.totalSteps) { currentStep++; refreshNav(); refreshConfirmState(); }
        });
        if (prevBtn) prevBtn.addEventListener('click', function() {
            clearError();
            if (currentStep > 1) { currentStep--; refreshNav(); refreshConfirmState(); }
        });
        if (confirmedCheckbox) confirmedCheckbox.addEventListener('change', refreshConfirmState);

        opts.btn.addEventListener('click', function() {
            clearError();
            currentStep = 1;
            refreshNav();
            refreshConfirmState();
            modal.show();
        });
        opts.modalEl.addEventListener('hidden.bs.modal', function() {
            currentStep = 1;
            refreshNav();
            refreshConfirmState();
            clearError();
        });

        refreshNav();
        refreshConfirmState();
        if (opts.shouldAutoOpen && opts.shouldAutoOpen()) modal.show();

        return {
            submit: async function(formPayload) {
                clearError();
                if (confirmBtn) confirmBtn.disabled = true;
                if (nextBtn) nextBtn.disabled = true;
                if (prevBtn) prevBtn.disabled = true;
                try {
                    await formPayload();
                    window.location.href = '/reservations';
                } catch (err) {
                    showError(err.message || String(err));
                    if (confirmBtn) confirmBtn.disabled = false;
                    if (nextBtn) nextBtn.disabled = false;
                    if (prevBtn) prevBtn.disabled = false;
                    refreshConfirmState();
                }
            }
        };
    }

    ready(function() {
        var btn = document.getElementById('fulfillBtn');
        var modalEl = document.getElementById('fulfillWizardModal');
        if (!btn || !modalEl) return;
        if (!window.bootstrap || !window.bootstrap.Modal) {
            console.error('[fulfill] Bootstrap Modal not available');
            return;
        }

        var reservationId = btn.getAttribute('data-reservation-id');
        var clientId = btn.getAttribute('data-client-id');
        var needsClient = btn.getAttribute('data-needs-client') === 'true';

        var wizard = bindWizard({
            btn: btn,
            modalEl: modalEl,
            errorBox: document.getElementById('fulfillError'),
            stepper: document.getElementById('fulfillStepper'),
            prevBtn: document.getElementById('wizardPrev'),
            nextBtn: document.getElementById('wizardNext'),
            confirmBtn: document.getElementById('wizardConfirm'),
            confirmedCheckbox: document.getElementById('fulfillPaymentConfirmed'),
            reservationId: reservationId,
            needsClient: needsClient,
            totalSteps: 3,
            shouldAutoOpen: function() {
                return new URLSearchParams(window.location.search).get('wizard') === 'fulfill';
            }
        });

        var confirmBtn = document.getElementById('wizardConfirm');
        if (confirmBtn) {
            confirmBtn.addEventListener('click', function() {
                wizard.submit(async function() {
                    if (needsClient) {
                        var clientForm = document.getElementById('clientDataForm');
                        await postForm('/api/clients/' + clientForm.clientId.value + '/update', clientForm);
                    }
                    var payForm = document.getElementById('paymentForm');
                    var fd = new FormData();
                    fd.append('paymentMethod', payForm.paymentMethod.value);
                    fd.append('paymentConfirmed', 'true');
                    var resp = await fetch('/reservations/' + reservationId + '/fulfill', {
                        method: 'POST',
                        body: fd,
                        credentials: 'same-origin',
                        headers: csrfHeaders({ 'Accept': 'application/json', 'X-Requested-With': 'XMLHttpRequest' })
                    });
                    if (!resp.ok) {
                        var t = await resp.text().catch(function() { return ''; });
                        var msg = 'Fulfill failed: HTTP ' + resp.status;
                        try {
                            var j = JSON.parse(t);
                            if (j && j.error) msg = j.error;
                        } catch (e) { if (t) msg += ' — ' + t.substring(0, 200); }
                        throw new Error(msg);
                    }
                });
            });
        }
    });
})();
