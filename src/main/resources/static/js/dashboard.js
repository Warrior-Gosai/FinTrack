function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('show');
}

function closeSidebar() {
    if (window.innerWidth <= 768) {
        document.getElementById('sidebar').classList.remove('show');
    }
}