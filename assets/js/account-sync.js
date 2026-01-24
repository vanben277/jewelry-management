// user-sync.js
(function () {
    /**
     * Cập nhật thông tin user lên UI
     * @param {Object} user
     */
    function updateAccountInfo(user) {
        if (!user) return;
        // Avatar
        const avatarEl = document.querySelector('#account-image');
        if (avatarEl) avatarEl.src = user.avatar || 'https://cdn.pnj.io/images/customer/home/new_avatar_default.svg';

        // Full name
        const nameEl = document.querySelector('#account-full-name');
        if (nameEl) nameEl.textContent = (user.firstName + ' ' + user.lastName).trim() || 'Khách hàng';

        // Email
        const emailEl = document.querySelector('#account-email');
        if (emailEl) emailEl.textContent = user.email || '-';

        // Phone
        const phoneEl = document.querySelector('#account-phone');
        if (phoneEl) phoneEl.textContent = user.phone || '-';

        // Address
        const addressEl = document.querySelector('#account-address');
        if (addressEl) addressEl.textContent = user.address || '-';

        // Date of Birth
        const dobEl = document.querySelector('#account-date-of-birth');
        if (dobEl) dobEl.textContent = user.dateOfBirth
            ? new Date(user.dateOfBirth).toLocaleDateString('vi-VN')
            : '-';

        // Gender
        const genderEl = document.querySelector('#account-gender');
        if (genderEl) {
            const genderMap = { MALE: 'Nam', FEMALE: 'Nữ', OTHER: 'Khác' };
            genderEl.textContent = genderMap[user.gender] || '-';
        }


        // Header chào user
        const accountText = document.querySelectorAll('#accountText, #accountName');
        if (accountText.length > 0) {
            const prefix =
                user.gender === 'MALE'
                    ? 'Chào anh'
                    : user.gender === 'FEMALE'
                        ? 'Chào chị'
                        : 'Chào bạn';
            const fullName = (user.firstName + ' ' + user.lastName).trim();
            accountText.forEach((el) => (el.textContent = `${prefix} ${fullName}`));
        }

        const accountLinks = document.querySelectorAll('#accountNav a, #mobileAccountLink');
        accountLinks.forEach(link => link.href = 'my-home.html');

        const dashboardItem = document.getElementById("dashboard");
        if (dashboardItem) {
            if (user.role === "ADMIN") {
                dashboardItem.style.display = "block"; 
            } else {
                dashboardItem.style.display = "none";  
            }
        }
    }

    function renderCategoryMenu(data, containerId, isDropdown = false) {
        const container = $(containerId);
        container.empty();

        data.forEach(cat => {
            if (cat.children && cat.children.length > 0) {
                const childrenHtml = cat.children.map(
                    child => `<li>
                    <a href="containers.html?category=${child.id}" 
                       data-id="${child.id}" 
                       data-name="${child.name}" 
                       data-banner="${child.bannerUrl || ''}" 
                       class="${isDropdown ? 'header__narbar--item' : 'navbar__tablet--link'}">
                       ${child.name}
                    </a>
                </li>`
                ).join('');

                const li = `
                <li class="${isDropdown ? 'header__narbar--item' : 'navbar__tablet--link has-children'}">
                    <span>${cat.name}</span>
                    <i class="fa-solid fa-angle-left"></i>
                    <ul class="tablet__submenu">
                        ${childrenHtml}
                    </ul>
                </li>
            `;
                container.append(li);
            } else {
                const li = `
                <li>
                    <a href="containers.html?category=${cat.id}" 
                       data-id="${cat.id}" 
                       data-name="${cat.name}" 
                       data-banner="${cat.bannerUrl || ''}" 
                       class="${isDropdown ? 'header__narbar--item' : 'navbar__tablet--link'}">
                       ${cat.name}
                    </a>
                </li>
            `;
                container.append(li);
            }
        });

        // Lưu thông tin khi click
        container.find('a[data-id]').on('click', function () {
            const catData = {
                id: $(this).data('id'),
                name: $(this).data('name'),
                bannerUrl: $(this).data('banner')
            };
            localStorage.setItem('selectedCategory', JSON.stringify(catData));
        });
    }

    function loadCategories() {
        $.ajax({
            url: 'http://localhost:8080/api/v1/category/tree',
            method: 'GET',
            success: function (res) {
                if (res.data && res.data.length > 0) {
                    // PC menu
                    renderCategoryMenu(res.data, '#category-menu', true);

                    // Tablet menu
                    renderCategoryMenu(res.data, '#tablet-category-menu', false);

                    // Append mục tĩnh (blog, khuyến mãi, hotline...) cho tablet
                    $('#tablet-category-menu').append(`
                        <li><a href="./blog.html" class="navbar__tablet--link">Blog</a></li>
                        <li><a href="./exception.html" class="navbar__tablet--link color">Khuyến mãi</a></li>
                        <li><a href="./shop.html" class="navbar__tablet--link">Hệ thống cửa hàng</a></li>
                        <li><a href="./DSCD.html" class="navbar__tablet--link">Quan hệ cổ đông (IR)</a></li>
                        <li><a href="./exception.html" class="navbar__tablet--link">Lịch sử đơn hàng</a></li>
                        <li class="navbar__tablet--link cursor">Hotline: 0748187 (Miễn phí)</li>
                    `);
                }
            },
            error: function (xhr) {
                let errorMessage = 'Tải danh danh mục thất bại.'
                switch (xhr.status) {
                    case 500:
                        errorMessage = 'Lỗi máy chủ. Vui lòng thử lại sau.'
                        window.location.href = 'exception.html?code=500';
                        break;
                }
                alert(errorMessage);
            }
        });
    }

    // Cập nhật thông tin user từ localStorage
    const currentUser = JSON.parse(localStorage.getItem('userInfo') || 'null');
    updateAccountInfo(currentUser);

    // Đồng bộ realtime giữa các tab / khi update localStorage
    window.addEventListener('storage', function (e) {
        if (e.key === 'userInfo') {
            const newUser = JSON.parse(e.newValue);
            updateAccountInfo(newUser);
        }
    });

    // Public để gọi thủ công nếu cần
    window.UserSync = {
        updateAccountInfo,
        renderCategoryMenu,
        loadCategories
    };
})();





    