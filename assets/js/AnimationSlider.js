// colab
class ResponsiveSlider {
    constructor() {
        this.desktopSlider = document.getElementById('desktopSlider');
        this.mobileSlider = document.getElementById('mobileSlider');
        this.desktopDots = document.getElementById('desktopDots');
        this.mobileDots = document.getElementById('mobileDots');

        this.desktopCurrentIndex = 0;
        this.mobileCurrentIndex = 0;
        this.totalItems = 4;
        this.mobileAutoInterval = null;

        // Touch/Swipe properties
        this.touchStartX = 0;
        this.touchEndX = 0;
        this.touchStartY = 0;
        this.touchEndY = 0;
        this.minSwipeDistance = 50; // Minimum distance for swipe
        this.maxVerticalDistance = 100; // Maximum vertical movement for horizontal swipe

        this.init();
    }

    init() {
        this.createDots();
        this.updateDesktopSlider();
        this.updateMobileSlider();
        this.startMobileAutoSlide();
        this.handleResize();
        this.initTouchEvents();

        window.addEventListener('resize', () => this.handleResize());
    }

    initTouchEvents() {
        // Add touch events for mobile slider
        const mobileSliderContainer = document.querySelector('.sliderMobile__container');
        if (mobileSliderContainer) {
            mobileSliderContainer.addEventListener('touchstart', (e) => this.handleTouchStart(e), { passive: true });
            mobileSliderContainer.addEventListener('touchmove', (e) => this.handleTouchMove(e), { passive: false });
            mobileSliderContainer.addEventListener('touchend', (e) => this.handleTouchEnd(e), { passive: true });
        }

        // Add touch events for desktop slider (for tablet use)
        const desktopSliderContainer = document.querySelector('.slider__container');
        if (desktopSliderContainer) {
            desktopSliderContainer.addEventListener('touchstart', (e) => this.handleTouchStart(e), { passive: true });
            desktopSliderContainer.addEventListener('touchmove', (e) => this.handleTouchMove(e), { passive: false });
            desktopSliderContainer.addEventListener('touchend', (e) => this.handleTouchEndDesktop(e), { passive: true });
        }
    }

    handleTouchStart(e) {
        this.touchStartX = e.touches[0].clientX;
        this.touchStartY = e.touches[0].clientY;

        // Stop auto-slide when user starts touching
        this.stopMobileAutoSlide();
    }

    handleTouchMove(e) {
        // Prevent default scrolling if it's a horizontal swipe
        const touchCurrentX = e.touches[0].clientX;
        const touchCurrentY = e.touches[0].clientY;

        const deltaX = Math.abs(touchCurrentX - this.touchStartX);
        const deltaY = Math.abs(touchCurrentY - this.touchStartY);

        // If horizontal movement is greater than vertical, prevent default
        if (deltaX > deltaY) {
            e.preventDefault();
        }
    }

    handleTouchEnd(e) {
        this.touchEndX = e.changedTouches[0].clientX;
        this.touchEndY = e.changedTouches[0].clientY;

        this.handleSwipe('mobile');

        // Restart auto-slide after touch ends
        this.restartMobileAutoSlide();
    }

    handleTouchEndDesktop(e) {
        this.touchEndX = e.changedTouches[0].clientX;
        this.touchEndY = e.changedTouches[0].clientY;

        this.handleSwipe('desktop');
    }

    handleSwipe(sliderType) {
        const deltaX = this.touchEndX - this.touchStartX;
        const deltaY = Math.abs(this.touchEndY - this.touchStartY);

        // Check if it's a valid horizontal swipe
        if (Math.abs(deltaX) > this.minSwipeDistance && deltaY < this.maxVerticalDistance) {
            if (sliderType === 'mobile') {
                if (deltaX > 0) {
                    // Swipe right - go to previous slide
                    this.prevMobileSlide();
                } else {
                    // Swipe left - go to next slide
                    this.nextMobileSlide();
                }
            } else if (sliderType === 'desktop') {
                if (deltaX > 0) {
                    // Swipe right - go to previous slide
                    this.changeDesktopSlide(-1);
                } else {
                    // Swipe left - go to next slide
                    this.changeDesktopSlide(1);
                }
            }
        }
    }

    createDots() {
        // Desktop dots (2 dots for 4 items showing 3 at a time)
        this.desktopDots.innerHTML = '';
        for (let i = 0; i < 2; i++) {
            const dot = document.createElement('div');
            dot.className = 'slider__dot';
            dot.addEventListener('click', () => this.goToDesktopSlide(i));
            this.desktopDots.appendChild(dot);
        }

        // Mobile dots (4 dots for 4 items)
        this.mobileDots.innerHTML = '';
        for (let i = 0; i < this.totalItems; i++) {
            const dot = document.createElement('div');
            dot.className = 'slider__dot';
            dot.addEventListener('click', () => this.goToMobileSlide(i));
            this.mobileDots.appendChild(dot);
        }
    }

    updateDesktopSlider() {
        const translateX = -(this.desktopCurrentIndex * 33.333);
        this.desktopSlider.style.transform = `translateX(${translateX}%)`;

        // Update dots
        const dots = this.desktopDots.querySelectorAll('.slider__dot');
        dots.forEach((dot, index) => {
            dot.classList.toggle('active', index === this.desktopCurrentIndex);
        });
    }

    updateMobileSlider() {
        const translateX = -(this.mobileCurrentIndex * 100);
        this.mobileSlider.style.transform = `translateX(${translateX}%)`;

        // Update dots
        const dots = this.mobileDots.querySelectorAll('.slider__dot');
        dots.forEach((dot, index) => {
            dot.classList.toggle('active', index === this.mobileCurrentIndex);
        });
    }

    changeDesktopSlide(direction) {
        if (direction === 1 && this.desktopCurrentIndex < 1) {
            this.desktopCurrentIndex++;
        } else if (direction === -1 && this.desktopCurrentIndex > 0) {
            this.desktopCurrentIndex--;
        }
        this.updateDesktopSlider();
    }

    goToDesktopSlide(index) {
        this.desktopCurrentIndex = index;
        this.updateDesktopSlider();
    }

    goToMobileSlide(index) {
        this.mobileCurrentIndex = index;
        this.updateMobileSlider();
        this.restartMobileAutoSlide();
    }

    nextMobileSlide() {
        this.mobileCurrentIndex = (this.mobileCurrentIndex + 1) % this.totalItems;
        this.updateMobileSlider();
    }

    prevMobileSlide() {
        this.mobileCurrentIndex = (this.mobileCurrentIndex - 1 + this.totalItems) % this.totalItems;
        this.updateMobileSlider();
    }

    startMobileAutoSlide() {
        this.mobileAutoInterval = setInterval(() => {
            this.nextMobileSlide();
        }, 3000);
    }

    stopMobileAutoSlide() {
        if (this.mobileAutoInterval) {
            clearInterval(this.mobileAutoInterval);
            this.mobileAutoInterval = null;
        }
    }

    restartMobileAutoSlide() {
        this.stopMobileAutoSlide();
        this.startMobileAutoSlide();
    }

    handleResize() {
        // Reset positions on resize
        this.updateDesktopSlider();
        this.updateMobileSlider();
    }
}

// Initialize slider
const slider = new ResponsiveSlider();

// Pause auto-slide on hover for mobile
document.addEventListener('DOMContentLoaded', function () {
    const mobileSliderContainer = document.querySelector('.sliderMobile__container');
    if (mobileSliderContainer) {
        mobileSliderContainer.addEventListener('mouseenter', () => {
            slider.stopMobileAutoSlide();
        });

        mobileSliderContainer.addEventListener('mouseleave', () => {
            slider.startMobileAutoSlide();
        });
    }
});

// product
class ProductCarousel {
    constructor() {
        this.row = document.getElementById('product-row');
        this.prevBtn = document.getElementById('prev-btn');
        this.nextBtn = document.getElementById('next-btn');
        this.items = Array.from(document.querySelectorAll('.container__block'));
        this.itemsPerView = 4;
        this.itemWidth = 0;
        this.currentIndex = 0;
        this.autoScrollInterval = null;
        this.autoScrollDelay = 3000;
        this.isTransitioning = false;

        this.setupClones(); // clone đầu/cuối để tạo loop
        this.init();
        this.enableDragScroll();
        this.enableTouchScroll();
        this.startAutoScroll();
    }

    setupClones() {
        const firstItems = this.items.slice(0, this.itemsPerView).map(el => el.cloneNode(true));
        const lastItems = this.items.slice(-this.itemsPerView).map(el => el.cloneNode(true));

        firstItems.forEach(el => this.row.appendChild(el));
        lastItems.reverse().forEach(el => this.row.insertBefore(el, this.row.firstChild));

        // Update items after cloning
        this.items = Array.from(this.row.querySelectorAll('.container__block'));
        this.currentIndex = this.itemsPerView; // Start from the real first item
    }

    init() {
        this.calculateDimensions();
        this.setupRowStyles();
        this.bindEvents();
        this.goTo(this.currentIndex, false);
    }

    setupRowStyles() {
        this.row.style.display = 'flex';
        this.row.style.transition = 'transform 1.5s ease';
        this.row.style.overflow = 'visible';
        this.row.style.scrollBehavior = 'auto';
    }

    bindEvents() {
        this.nextBtn.addEventListener('click', () => {
            this.next();
            this.resetAutoScroll();
        });

        this.prevBtn.addEventListener('click', () => {
            this.prev();
            this.resetAutoScroll();
        });

        window.addEventListener('resize', () => this.handleResize());

        this.row.addEventListener('mouseenter', () => this.stopAutoScroll());
        this.row.addEventListener('mouseleave', () => this.startAutoScroll());
    }

    calculateDimensions() {
        const containerWidth = this.row.parentElement.offsetWidth;
        const itemRect = this.items[0].getBoundingClientRect();
        const gap = 16;

        this.itemWidth = itemRect.width + gap;

        if (containerWidth <= 768) this.itemsPerView = 1;
        else if (containerWidth <= 1024) this.itemsPerView = 2;
        else this.itemsPerView = 4;
    }

    goTo(index, animate = true) {
        if (!animate) this.row.style.transition = 'none';
        else this.row.style.transition = 'transform 1.5s ease';

        const translateX = -index * this.itemWidth;
        this.row.style.transform = `translateX(${translateX}px)`;
        this.currentIndex = index;
    }

    next() {
        if (this.isTransitioning) return;
        this.isTransitioning = true;
        this.goTo(this.currentIndex + 1);

        setTimeout(() => {
            if (this.currentIndex === this.items.length - this.itemsPerView) {
                // Jump back to real first
                this.goTo(this.itemsPerView, false);
            }
            this.isTransitioning = false;
        }, 350);
    }

    prev() {
        if (this.isTransitioning) return;
        this.isTransitioning = true;
        this.goTo(this.currentIndex - 1);

        setTimeout(() => {
            if (this.currentIndex === 0) {
                // Jump to real last
                const lastIndex = this.items.length - this.itemsPerView * 2;
                this.goTo(lastIndex, false);
            }
            this.isTransitioning = false;
        }, 350);
    }

    handleResize() {
        this.calculateDimensions();
        this.goTo(this.currentIndex, false);
    }

    startAutoScroll() {
        this.stopAutoScroll();
        this.autoScrollInterval = setInterval(() => this.next(), this.autoScrollDelay);
    }

    stopAutoScroll() {
        if (this.autoScrollInterval) {
            clearInterval(this.autoScrollInterval);
            this.autoScrollInterval = null;
        }
    }

    resetAutoScroll() {
        this.stopAutoScroll();
        setTimeout(() => this.startAutoScroll(), 1000);
    }

    // Touch drag
    enableTouchScroll() {
        let startX = 0, moved = 0;
        this.row.addEventListener('touchstart', e => {
            startX = e.touches[0].clientX;
            this.stopAutoScroll();
        });

        this.row.addEventListener('touchmove', e => {
            moved = e.touches[0].clientX - startX;
        });

        this.row.addEventListener('touchend', () => {
            if (moved > 50) this.prev();
            else if (moved < -50) this.next();
            this.resetAutoScroll();
        });
    }

    // PC drag
    enableDragScroll() {
        let isDown = false, startX = 0, moved = 0;

        this.row.addEventListener('mousedown', e => {
            isDown = true;
            startX = e.pageX;
            moved = 0;
            this.row.style.cursor = 'grabbing';
            this.stopAutoScroll();
        });

        document.addEventListener('mousemove', e => {
            if (!isDown) return;
            moved = e.pageX - startX;
        });

        document.addEventListener('mouseup', () => {
            if (!isDown) return;
            isDown = false;
            this.row.style.cursor = 'grab';
            if (moved > 50) this.prev();
            else if (moved < -50) this.next();
            this.resetAutoScroll();
        });

        this.row.addEventListener('dragstart', e => e.preventDefault());
    }

    destroy() {
        this.stopAutoScroll();
    }
}

// Tạo function global để gọi từ bên ngoài
window.initProductCarousel = function() {
    // Kiểm tra xem có sản phẩm không trước khi tạo carousel
    const products = document.querySelectorAll('#product-row .container__block');
    
    if (products.length > 0) {
        return new ProductCarousel();
    }
    return null;
};