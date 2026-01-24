$(() => {
  // ===== Check quyền ADMIN =====
  let userInfo = JSON.parse(localStorage.getItem('userInfo'));
  if (!userInfo || userInfo.role !== 'ADMIN') {
    alert('Bạn không có quyền truy cập!');
    window.location.href = '../../login.html';
    return;
  }

  // ===== Load Sidebar =====
  $('#sidenav').load('./assets/layout/SiderBarAdmin.html', function () {
    let currentPath = window.location.pathname.split('/').pop();
    let activeText = '';

    $('#sidenav .nav-link').each(function () {
      let linkPath = $(this).attr('href').split('/').pop();
      if (linkPath === currentPath) {
        $(this)
          .addClass('active bg-gradient-dark text-white')
          .removeClass('text-dark');
        activeText = $(this).find('.nav-link-text').text();
      } else {
        $(this)
          .removeClass('active bg-gradient-dark text-white')
          .addClass('text-dark');
      }
    });

    if (activeText) {
      localStorage.setItem('breadcrumbTitle', activeText);
    }

    $('#sidenav').on('click', '.nav-link', function () {
      $('#sidenav .nav-link')
        .removeClass('active bg-gradient-dark text-white')
        .addClass('text-dark');

      $(this)
        .addClass('active bg-gradient-dark text-white')
        .removeClass('text-dark');

      let clickedText = $(this).find('.nav-link-text').text();
      localStorage.setItem('breadcrumbTitle', clickedText);
    });

    // ===== Logout =====
    $(document).on('click', '#logoutBtn', function (e) {
      e.preventDefault();
      localStorage.removeItem('userInfo');
      localStorage.removeItem('userId');
      localStorage.removeItem('auth');
      window.location.href = '../../login.html';
    });
  });

  // ===== Load Footer =====
  $('#footer').load('./assets/layout/FooterAdmin.html');

  // ===== Load Navbar =====
  $('#navbarBlur').load('./assets/layout/Navbar.html', function () {
    // Gán avatar từ localStorage
    if (userInfo.avatar) {
      $('#navbarBlur img').attr('src', userInfo.avatar);
    }

    // Gán breadcrumb
    let savedBreadcrumb = localStorage.getItem('breadcrumbTitle');
    if (savedBreadcrumb) {
      $('.breadcrumb .active').text(savedBreadcrumb);
    }

    // Toggle sidenav
    $(document).on('click', '#iconNavbarSidenav', function () {
      $('#sidenav-main').toggleClass('show-sidenav');
    });
  });
});
