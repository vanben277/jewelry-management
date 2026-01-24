/**
 * Utility functions để quản lý CartMini
 */
window.CartMiniUtils = {
    /**
     * Khởi tạo CartMini sau khi header load xong
     */
    initAfterHeaderLoad: function() {
        if (window.cartMini) {
            window.cartMini.waitForHeaderAndRender();
        }
    },

    /**
     * Refresh CartMini (dùng trong refreshUserUI)
     */
    refresh: function() {
        if (window.cartMini) {
            // Đợi một chút để DOM render xong
            setTimeout(() => {
                window.cartMini.renderMiniCart();
            }, 50);
        }
    },

    /**
     * Thêm sản phẩm vào giỏ hàng với validation
     */
    addProduct: function(productData) {
        if (!window.cartMini) {
            console.error('CartMini chưa được khởi tạo');
            return false;
        }

        // Validation cơ bản
        if (!productData.productId || !productData.name || !productData.price) {
            console.error('Thiếu thông tin sản phẩm cần thiết');
            return false;
        }

        try {
            const result = window.cartMini.addItem(productData);
            return result;
        } catch (error) {
            console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
            return false;
        }
    },

    /**
     * Lấy thông tin tổng quan về giỏ hàng
     */
    getCartSummary: function() {
        if (!window.cartMini) {
            return {
                items: [],
                totalQuantity: 0,
                totalPrice: 0,
                isEmpty: true
            };
        }

        return {
            items: window.cartMini.getCart(),
            totalQuantity: window.cartMini.getTotalQuantity(),
            totalPrice: window.cartMini.getTotalPrice(),
            isEmpty: window.cartMini.isEmpty()
        };
    },

    /**
     * Xử lý callback khi header load xong
     */
    onHeaderLoaded: function(callback) {
        $(document).on('headerLoaded', function() {
            if (window.cartMini) {
                window.cartMini.renderMiniCart();
            }
            if (typeof callback === 'function') {
                callback();
            }
        });
    },

    /**
     * Xử lý callback khi cart được update
     */
    onCartUpdated: function(callback) {
        $(document).on('cartUpdated', function(event, cart) {
            if (typeof callback === 'function') {
                callback(cart, window.CartMiniUtils.getCartSummary());
            }
        });
    }
};