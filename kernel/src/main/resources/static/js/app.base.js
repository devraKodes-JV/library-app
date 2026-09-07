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

    initHtmxCsp();
    initNotificationStream();
    loadNotifications();

    var bell = document.getElementById('notificationBell');
    if (bell) {
        bell.addEventListener('click', function() {
            fetch('/api/notifications/read', { method: 'POST', credentials: 'same-origin' })
                .then(function() {
                    var badge = document.getElementById('notificationBadge');
                    if (badge) badge.classList.add('d-none');
                });
        });
    }
}

function initHtmxCsp() {
    var nonce = document.querySelector('meta[name="csp-nonce"]')?.getAttribute('content');
    if (!nonce) {
        var script = document.querySelector('script[nonce]');
        nonce = script?.getAttribute('nonce');
    }
    if (!nonce) return;

    document.addEventListener('htmx:afterSettle', function (evt) {
        var target = evt.detail.target;
        if (!target) return;
        var styles = target.querySelectorAll('style:not([nonce])');
        styles.forEach(function (style) {
            style.setAttribute('nonce', nonce);
        });
    });

    var observer = new MutationObserver(function (mutations) {
        mutations.forEach(function (mutation) {
            mutation.addedNodes.forEach(function (node) {
                if (node.nodeName === 'STYLE' && !node.getAttribute('nonce')) {
                    node.setAttribute('nonce', nonce);
                }
            });
        });
    });
    observer.observe(document.documentElement, { childList: true, subtree: true });
}

function initNotificationStream() {
    if (typeof EventSource === 'undefined') {
        console.warn('EventSource not supported');
        return;
    }

    console.log('Connecting to notification stream...');
    var source = new EventSource('/api/notifications/stream', { withCredentials: true });

    source.onopen = function () {
        console.log('Notification stream connected');
    };

    source.onmessage = function (event) {
        console.log('Notification received:', event.data);
        try {
            var data = JSON.parse(event.data);
            showNotificationToast(data.message || 'Notification received');
            loadNotifications();
        } catch (e) {
            console.error('Failed to parse notification:', e);
        }
    };

    source.onerror = function (e) {
        console.warn('Notification stream error:', e);
        source.close();
        setTimeout(initNotificationStream, 5000);
    };
}

function loadNotifications() {
    fetch('/api/notifications', { credentials: 'same-origin' })
        .then(function(response) { return response.json(); })
        .then(function(notifications) {
            var badge = document.getElementById('notificationBadge');
            var list = document.getElementById('notificationList');
            if (!badge || !list) return;

            var unread = notifications.filter(function(n) { return !n.read; });
            if (unread.length > 0) {
                badge.textContent = unread.length;
                badge.classList.remove('d-none');
            } else {
                badge.classList.add('d-none');
            }

            if (notifications.length === 0) {
                list.innerHTML = '<p class="text-muted text-center py-2 mb-0">No new notifications</p>';
            } else {
                list.innerHTML = notifications.map(function(n) {
                    var icon = 'bi-bell';
                    if (n.type && n.type.startsWith('reservation.')) icon = 'bi-calendar-check';
                    var link = n.link || '#';
                    var notifId = n.id || '';
                    var onclickAttr = ' onclick="handleNotificationClick(\'' + notifId + '\', \'' + link + '\');"';
                    return '<div class="dropdown-item-text text-light py-1 border-success border-secondary" style="cursor: pointer;"' +
                        onclickAttr + '>' +
                        '<i class="bi ' + icon + ' me-2"></i>' + n.message +
                        '<br><small class="text-muted">' + new Date(n.timestamp).toLocaleString() + '</small>' +
                        '</div>';
                }).join('');
            }
        })
        .catch(function(e) { console.error('Failed to load notifications:', e); });
}

function handleNotificationClick(notifId, link) {
    fetch('/api/notifications/read?id=' + encodeURIComponent(notifId), {
        method: 'POST',
        credentials: 'same-origin'
    }).then(function() {
        fetch('/api/notifications?id=' + encodeURIComponent(notifId), {
            method: 'DELETE',
            credentials: 'same-origin'
        }).then(function() {
            if (link && link !== '#') {
                window.location.href = link;
            } else {
                loadNotifications();
            }
        });
    });
}

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
    toast.addEventListener('hidden.bs.toast', function () {
        toast.remove();
    });
}

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
    toast.addEventListener('hidden.bs.toast', function () {
        toast.remove();
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initApp);
} else {
    initApp();
}
