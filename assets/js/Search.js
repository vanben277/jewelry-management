$(document).ready(() => {
    // Dùng event delegation - sẽ hoạt động với elements được load sau
    $(document).on('input', '.outline-none', function () {
        const query = this.value.trim();
        console.log('Input event triggered, query:', query);
        if (query.length > 0) {
            searchProducts(query);
        } else {
            const suggestionBox = document.querySelector('.header__boxsearch--listproduct');
            if (suggestionBox) suggestionBox.innerHTML = '';
        }
    });

    $(document).on('keypress', '.outline-none', function (event) {
        if (event.key === 'Enter') {
            const query = this.value.trim();
            if (query) {
                window.location.href = `containers.html?search=${encodeURIComponent(query)}`;
            }
        }
    });

    $(document).on('click', '#button-search', function () {
        const searchInput = document.querySelector('.outline-none');
        const query = searchInput ? searchInput.value.trim() : '';
        if (query) {
            window.location.href = `containers.html?search=${encodeURIComponent(query)}`;
        }
    });

    // Escape key để đóng search
    $(document).on('keydown', function (event) {
        if (event.key === 'Escape') {
            window.closeSearchBox && window.closeSearchBox();
        }
    });

    // Prevent click inside search box from closing overlay
    $(document).on('click', '.header__boxsearch', function (event) {
        event.stopPropagation();
    });

    // Global functions
    window.openSearchBox = function () {
        const overlay = document.getElementById('searchOverlay');
        if (!overlay) return;
        overlay.classList.add('active');
        document.body.style.overflow = 'hidden';
        setTimeout(() => {
            const searchInput = document.querySelector('.outline-none');
            if (searchInput) searchInput.focus();
        }, 300);
    };

    window.closeSearchBox = function () {
        const overlay = document.getElementById('searchOverlay');
        if (!overlay) return;
        overlay.classList.remove('active');
        document.body.style.overflow = 'auto';
        const suggestionBox = document.querySelector('.header__boxsearch--listproduct');
        if (suggestionBox) suggestionBox.innerHTML = '';
    };

    window.selectProduct = function (productId) {
        window.location.href = `product-detailed.html?id=${productId}`;
        window.closeSearchBox();
    };

    function searchProducts(query) {
        $.ajax({
            url: `http://localhost:8080/api/v1/product/search?name=${encodeURIComponent(query)}&pageNumber=0&pageSize=5`,
            method: 'GET',
            success: function (res) {
                if (res.data && res.data.content && res.data.content.length > 0) {
                    renderSuggestions(res.data.content);
                } else {
                    renderNoResults();
                }
            },
            error: function () {
                console.error('API error:', error, xhr.responseText);
                renderNoResults();
            }
        });
    }

    function renderSuggestions(products) {
        const suggestionBox = document.querySelector('.header__boxsearch--listproduct');
        if (!suggestionBox) return;

        suggestionBox.innerHTML = '';


        products.forEach(product => {
            const formattedPrice = product.price ? new Intl.NumberFormat('vi-VN').format(product.price) + ' đ' : 'Liên hệ';
            const primaryImage = product.primaryImageUrl || 'https://via.placeholder.com/300x300?text=No+Image';
            const soldQuantity = product.soldQuantity || 0;
            const soldHtml = soldQuantity > 0 ? `<label>${soldQuantity + ' đã bán'}</label>` : '';

            const itemHtml = `
                <a class="header__boxsearch--item" onclick="selectProduct('${product.id}')">
                    <img src="${primaryImage}" alt="${product.name}">
                    <div class="header__boxsearch--right">
                        <span>${product.displayName || product.name}</span>
                        <div class="header__boxsearch--number">
                            <span>${formattedPrice}</span>
                            <div class="vote-star">
                                <i class="fa-solid fa-star"></i>
                                <h5>${product.rating || 0}</h5>
                            </div>
                            ${soldHtml}
                        </div>
                    </div>
                </a>
            `;
            suggestionBox.insertAdjacentHTML('beforeend', itemHtml);
        });
    }

    function renderNoResults() {
        const suggestionBox = document.querySelector('.header__boxsearch--listproduct');
        if (!suggestionBox) return;

        suggestionBox.innerHTML = `
            <div class="no__result">
                <img src="https://cdn.pnj.io/images/2025/rebuild/a60759ad1dabe909c46a817ecbf71878.png?1740973018637" alt="Không có kết quả">
                <h3>Không tìm thấy kết quả</h3>
            </div>
        `;
    }
});