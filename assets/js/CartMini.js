/**
 * CartMini - Component quản lý giỏ hàng mini
 */
class CartMini {
    constructor() {
        this.cart = this.getCart();
    }

    /**
     * Khởi tạo CartMini với retry mechanism
     */
    init() {
        this.renderMiniCart();
    }

    /**
     * Đợi cho đến khi header được load xong rồi mới render
     */
    waitForHeaderAndRender(maxRetries = 10, currentRetry = 0) {
        const $cartList = $('.cart__list');
        
        if ($cartList.length > 0) {
            // Header đã load, có thể render
            this.renderMiniCart();
            return;
        }
        
        if (currentRetry >= maxRetries) {
            console.warn('CartMini: Không thể tìm thấy element .cart__list sau', maxRetries, 'lần thử');
            return;
        }
        
        // Thử lại sau 200ms
        setTimeout(() => {
            this.waitForHeaderAndRender(maxRetries, currentRetry + 1);
        }, 200);
    }

    /**
     * Lấy dữ liệu giỏ hàng từ localStorage
     */
    getCart() {
        return JSON.parse(localStorage.getItem('cart') || '[]');
    }

    /**
     * Lưu giỏ hàng vào localStorage
     */
    saveCart(cart) {
        localStorage.setItem('cart', JSON.stringify(cart));
        this.cart = cart;
    }

    /**
     * Render giỏ hàng mini
     */
    renderMiniCart() {
        const cart = this.getCart();
        const $cartList = $('.cart__list');
        const $cartCount = $('.header__nav--icon');
        const $cartTotal = $('.cart__total--price');

        // Kiểm tra xem các element có tồn tại không
        if (!$cartList.length) {
            console.warn('CartMini: Không tìm thấy element .cart__list - Header có thể chưa được load');
            // Thử lại sau 100ms
            setTimeout(() => {
                if ($('.cart__list').length > 0) {
                    this.renderMiniCart();
                }
            }, 100);
            return;
        }

        $cartList.empty();

        if (!cart || cart.length === 0) {
            $cartList.append('<p>Giỏ hàng trống</p>');
            $cartCount.text('0');
            $cartTotal.text('0 ₫');
            return;
        }

        let total = 0;
        cart.forEach((item, index) => {
            const price = item.price || 0;
            const quantity = item.quantity || 1;
            const subtotal = price * quantity;
            total += subtotal;

            const cartItem = this.createCartItemHTML(item, index, price, quantity);
            $cartList.append(cartItem);
        });

        // Cập nhật tổng số lượng và tổng tiền
        const totalQuantity = cart.reduce((sum, item) => sum + (item.quantity || 1), 0);
        $cartCount.text(totalQuantity);
        $cartTotal.text(total.toLocaleString('vi-VN') + ' ₫');

        // Gắn event listeners
        this.attachEventListeners();
    }

    /**
     * Tạo HTML cho một item trong giỏ hàng mini
     */
    createCartItemHTML(item, index, price, quantity) {
        return `
            <div class="cart__cont" data-index="${index}">
                <div class="cart__img">
                    <img src="${item.image || './assets/img/default-product.jpg'}" alt="${item.name}">
                </div>
                <div class="cart__name">
                    <div class="cart__block">
                        <span class="cart__desc">${item.name}</span>
                        <div class="cart__icon">
                            <i class="fa-solid fa-delete-left cart-mini-remove" data-index="${index}"></i>
                        </div>
                    </div>
                    <div class="cart__size" style="display: ${item.goldType ? 'block' : 'none'}">
                        <span>Chất liệu: ${item.goldType || ''}</span>
                    </div>
                    <div class="cart__size" style="display: ${item.size ? 'block' : 'none'}">
                        <span>Size: ${item.size || ''}</span>
                    </div>
                    <div class="cart__num">
                        <span class="cart__quantity">Số lượng: ${quantity}</span>
                        <div class="cart__price">${price.toLocaleString('vi-VN')} ₫</div>
                    </div>
                </div>
            </div>
        `;
    }

    /**
     * Gắn event listeners cho các nút trong giỏ hàng mini
     */
    attachEventListeners() {
        // Xóa event listeners cũ để tránh duplicate
        $(document).off('click', '.cart-mini-remove');
        
        // Gắn event listener mới
        $(document).on('click', '.cart-mini-remove', (e) => {
            const index = parseInt($(e.target).data('index'));
            this.removeItem(index);
        });
    }

    /**
     * Xóa một item khỏi giỏ hàng mini
     */
    removeItem(index) {
        let cart = this.getCart();
        if (cart[index]) {
            cart.splice(index, 1);
            this.saveCart(cart);
            this.renderMiniCart();
            
            // Trigger custom event để các component khác có thể lắng nghe
            $(document).trigger('cartUpdated', [cart]);
        }
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    addItem(product) {
        let cart = this.getCart();
        const existingItemIndex = cart.findIndex(item => 
            item.productId === product.productId && 
            item.size === product.size &&
            item.goldType === product.goldType
        );

        if (existingItemIndex !== -1) {
            // Nếu sản phẩm đã tồn tại, tăng số lượng
            cart[existingItemIndex].quantity = (cart[existingItemIndex].quantity || 1) + (product.quantity || 1);
        } else {
            // Nếu sản phẩm chưa tồn tại, thêm mới
            cart.push({
                productId: product.productId,
                name: product.name,
                price: product.price,
                image: product.image,
                sku: product.sku,
                size: product.size,
                goldType: product.goldType,
                quantity: product.quantity || 1
            });
        }

        this.saveCart(cart);
        this.renderMiniCart();
        
        // Trigger custom event
        $(document).trigger('cartUpdated', [cart]);
        
        return cart;
    }

    /**
     * Cập nhật số lượng của một item
     */
    updateQuantity(index, newQuantity) {
        let cart = this.getCart();
        if (cart[index] && newQuantity > 0) {
            cart[index].quantity = newQuantity;
            this.saveCart(cart);
            this.renderMiniCart();
            
            // Trigger custom event
            $(document).trigger('cartUpdated', [cart]);
        }
    }

    /**
     * Lấy tổng số lượng sản phẩm trong giỏ hàng
     */
    getTotalQuantity() {
        const cart = this.getCart();
        return cart.reduce((sum, item) => sum + (item.quantity || 1), 0);
    }

    /**
     * Lấy tổng tiền của giỏ hàng
     */
    getTotalPrice() {
        const cart = this.getCart();
        return cart.reduce((sum, item) => sum + ((item.price || 0) * (item.quantity || 1)), 0);
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    clearCart() {
        this.saveCart([]);
        this.renderMiniCart();
        
        // Trigger custom event
        $(document).trigger('cartUpdated', [[]]);
    }

    /**
     * Kiểm tra giỏ hàng có rỗng không
     */
    isEmpty() {
        const cart = this.getCart();
        return !cart || cart.length === 0;
    }

    /**
     * Tìm kiếm sản phẩm trong giỏ hàng
     */
    findItem(productId, size = null, goldType = null) {
        const cart = this.getCart();
        return cart.find(item => 
            item.productId === productId && 
            item.size === size &&
            item.goldType === goldType
        );
    }

    /**
     * Cập nhật thông tin sản phẩm trong giỏ hàng
     */
    updateItem(index, updates) {
        let cart = this.getCart();
        if (cart[index]) {
            cart[index] = { ...cart[index], ...updates };
            this.saveCart(cart);
            this.renderMiniCart();
            
            // Trigger custom event
            $(document).trigger('cartUpdated', [cart]);
        }
    }
}

// Tạo instance global
window.cartMini = new CartMini();

// Auto-render khi DOM ready - sử dụng waitForHeaderAndRender
$(document).ready(function() {
    if (window.cartMini) {
        window.cartMini.waitForHeaderAndRender();
    }
});

// Lắng nghe sự kiện header loaded
$(document).on('headerLoaded', function() {
    if (window.cartMini) {
        window.cartMini.renderMiniCart();
    }
});

// Export cho module system nếu cần
if (typeof module !== 'undefined' && module.exports) {
    module.exports = CartMini;
}