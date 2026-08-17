/* =========================================================================
   works-cards.js - card grid with search and pagination for Works list.
   ========================================================================= */

(function () {
    const PAGE_SIZE = 9;
    let works = [];
    let currentPage = 1;
    let query = '';

    function collectWorks() {
        const grid = document.getElementById('worksGrid');
        if (!grid) return [];
        return Array.from(grid.querySelectorAll('.work-card')).map(function (card) {
            return {
                el: card,
                title: (card.getAttribute('data-title') || '').toLowerCase(),
                subtitle: (card.getAttribute('data-subtitle') || '').toLowerCase(),
                language: (card.getAttribute('data-language') || '').toLowerCase(),
                category: (card.getAttribute('data-category') || '').toLowerCase()
            };
        });
    }

    function matches(work) {
        if (!query) return true;
        return work.title.includes(query) ||
            work.subtitle.includes(query) ||
            work.language.includes(query) ||
            work.category.includes(query);
    }

    function render() {
        const filtered = works.filter(matches);
        const totalPages = Math.max(1, Math.ceil(filtered.length / PAGE_SIZE));
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        const start = (currentPage - 1) * PAGE_SIZE;
        const pageItems = filtered.slice(start, start + PAGE_SIZE);

        works.forEach(function (work) {
            work.el.style.display = 'none';
        });

        pageItems.forEach(function (work) {
            work.el.style.display = '';
        });

        const noResults = document.getElementById('noWorksResults');
        if (noResults) {
            noResults.style.display = filtered.length === 0 ? '' : 'none';
        }

        renderPagination(totalPages);
    }

    function renderPagination(totalPages) {
        const pagination = document.getElementById('worksPagination');
        if (!pagination) return;
        pagination.innerHTML = '';

        if (totalPages <= 1) {
            return;
        }

        const createItem = function (page, label, isActive, isDisabled) {
            const li = document.createElement('li');
            li.className = 'page-item' + (isActive ? ' active' : '') + (isDisabled ? ' disabled' : '');
            const button = document.createElement('button');
            button.className = 'page-link bg-dark border-secondary text-light';
            button.innerHTML = label;
            button.disabled = isDisabled;
            button.addEventListener('click', function () {
                currentPage = page;
                render();
            });
            li.appendChild(button);
            return li;
        };

        pagination.appendChild(createItem(currentPage - 1, 'Previous', false, currentPage === 1));

        for (let i = 1; i <= totalPages; i++) {
            pagination.appendChild(createItem(i, String(i), i === currentPage, false));
        }

        pagination.appendChild(createItem(currentPage + 1, 'Next', false, currentPage === totalPages));
    }

    function init() {
        works = collectWorks();
        const searchInput = document.getElementById('workSearch');
        if (searchInput) {
            searchInput.addEventListener('input', function () {
                query = this.value.toLowerCase().trim();
                currentPage = 1;
                render();
            });
        }
        render();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
