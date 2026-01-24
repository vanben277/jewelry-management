// Mobile panel functionality
const overlay = document.getElementById('overlay');
const filterPanel = document.getElementById('filterPanel');
const sortPanel = document.getElementById('sortPanel');
const openFilterBtn = document.getElementById('openFilterBtn');
const openSortBtn = document.getElementById('openSortBtn');
const closeFilterBtn = document.getElementById('closeFilterBtn');
const closeSortBtn = document.getElementById('closeSortBtn');
const mobileFilterButtons = document.querySelector('.mobile-filter-buttons');

// Sticky filter buttons functionality
function handleScroll() {
    if (window.innerWidth <= 768) {
        const headerHeight = document.querySelector('#header')?.offsetHeight || 0;
        const tabletHeaderHeight = document.querySelector('#header_tablet')?.offsetHeight || 0;
        const totalHeaderHeight = headerHeight + tabletHeaderHeight;

        if (window.scrollY > totalHeaderHeight) {
            mobileFilterButtons.classList.add('sticky');
        } else {
            mobileFilterButtons.classList.remove('sticky');
        }
    }
}

// Add scroll event listener
window.addEventListener('scroll', handleScroll);
window.addEventListener('resize', handleScroll);

function openPanel(panel) {
    overlay.classList.add('active');
    panel.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closePanel() {
    overlay.classList.remove('active');
    filterPanel.classList.remove('active');
    sortPanel.classList.remove('active');
    document.body.style.overflow = 'auto';
}

openFilterBtn.addEventListener('click', () => openPanel(filterPanel));
openSortBtn.addEventListener('click', () => openPanel(sortPanel));
closeFilterBtn.addEventListener('click', closePanel);
closeSortBtn.addEventListener('click', closePanel);
overlay.addEventListener('click', closePanel);

// Handle reset and apply buttons
document.querySelectorAll('.btn-reset').forEach(btn => {
    btn.addEventListener('click', () => {
        const panel = btn.closest('.mobile-panel');
        panel.querySelectorAll('select').forEach(select => {
            select.selectedIndex = 0;
        });
        panel.querySelectorAll('input[type="radio"]').forEach(radio => {
            radio.checked = radio.value === 'newest';
        });
    });
});

document.querySelectorAll('.btn-apply').forEach(btn => {
    btn.addEventListener('click', () => {
        // Here you would typically apply the filters/sorting
        console.log('Apply filters/sorting');
        closePanel();
    });
});