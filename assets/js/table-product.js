var win = navigator.platform.indexOf("Win") > -1;
if (win && document.querySelector("#sidenav-scrollbar")) {
    var options = { damping: "0.5" };
    Scrollbar.init(document.querySelector("#sidenav-scrollbar"), options);
}

// Constants
const API_BASE_URL = "http://localhost:8080/api/v1";
let sizeCounter = 0;
let selectedImages = [];
let currentProducts = [];
let currentPage = 0;
let pageSize = 10;
let totalPages = 0;
let selectedProductIds = [];
let editingProductId = null; // Biến để lưu ID sản phẩm đang chỉnh sửa

// Authentication and Authorization Check
function checkAuth() {
    const userInfo = localStorage.getItem("userInfo");
    const userId = localStorage.getItem("userId");
    const auth = localStorage.getItem("auth");

    if (!userInfo || !userId || !auth) {
        alert("Vui lòng đăng nhập");
        window.location.href = "/login.html";
        return false;
    }

    const user = JSON.parse(userInfo);
    if (user.role !== "ADMIN") {
        alert("Bạn không có quyền truy cập trang này");
        window.location.href = "/index.html";
        return false;
    }

    return true;
}

// API Helper Functions
function getAuthHeaders() {
    let authHeader = localStorage.getItem("auth");

    // Tạo Basic Auth từ userInfo nếu cần
    try {
        const userInfo = JSON.parse(localStorage.getItem("userInfo"));
        if (userInfo.username && userInfo.password) {
            authHeader = btoa(`${userInfo.username}:${userInfo.password}`);
        }
    } catch (e) {
        console.error("Lỗi khi tạo Basic Auth:", e);
    }

    return {
        Authorization: `Basic ${authHeader}`,
        "Content-Type": "application/json",
    };
}

function getAuthHeadersForFormData() {
    let authHeader = localStorage.getItem("auth");

    try {
        const userInfo = JSON.parse(localStorage.getItem("userInfo"));
        if (userInfo.username && userInfo.password) {
            authHeader = btoa(`${userInfo.username}:${userInfo.password}`);
        }
    } catch (e) {
        console.error("Lỗi khi tạo Basic Auth:", e);
    }

    return {
        Authorization: `Basic ${authHeader}`,
    };
}

function validateForm(data, productType) {
    let isValid = true;
    const errorMessages = document.querySelectorAll(".error-message");
    errorMessages.forEach((error) => {
        error.style.display = "none";
        error.textContent = "";
    });

    // Validate SKU
    if (!data.sku || data.sku.trim() === "") {
        showError("skuError", "Vui lòng nhập mã sản phẩm");
        isValid = false;
    } else if (data.sku.length > 50) {
        showError("skuError", "Mã sản phẩm không được vượt quá 50 ký tự");
        isValid = false;
    }

    // Validate Name
    if (!data.name || data.name.trim() === "") {
        showError("nameError", "Vui lòng nhập tên sản phẩm");
        isValid = false;
    } else if (data.name.length > 50) {
        showError("nameError", "Tên sản phẩm không được vượt quá 50 ký tự");
        isValid = false;
    }

    // Validate Price
    if (!data.price || isNaN(data.price) || data.price <= 0) {
        showError("priceError", "Vui lòng nhập giá hợp lệ");
        isValid = false;
    }

    // Validate Quantity
    if (productType === "no-size") {
        if (!data.quantity || isNaN(data.quantity) || data.quantity < 0) {
            showError("quantityError", "Vui lòng nhập số lượng hợp lệ");
            isValid = false;
        }
    } else {
        if (data.sizes.length === 0) {
            showError("sizeSection", "Vui lòng thêm ít nhất một kích cỡ");
            isValid = false;
        } else {
            data.sizes.forEach((size, index) => {
                if (!size.size || isNaN(size.size) || size.size <= 0) {
                    showError(
                        `size${index + 1}Error`,
                        "Vui lòng nhập kích cỡ hợp lệ"
                    );
                    isValid = false;
                }
                if (!size.quantity || isNaN(size.quantity) || size.quantity < 0) {
                    showError(
                        `sizeQuantity${index + 1}Error`,
                        "Vui lòng nhập số lượng hợp lệ"
                    );
                    isValid = false;
                }
            });
        }
    }

    // Validate Images
    if (selectedImages.length === 0) {
        showError("imagesError", "Vui lòng chọn ít nhất một ảnh sản phẩm");
        isValid = false;
    }

    // Validate Description
    if (data.description && data.description.length > 255) {
        showError("descriptionError", "Mô tả không được vượt quá 255 ký tự");
        isValid = false;
    }

    if (!data.categoryId || data.categoryId.trim() === "") {
        showError("categoryIdError", "Vui lòng chọn danh mục");
        isValid = false;
    }

    if (!data.dateOfEntry || data.dateOfEntry.trim() === "") {
        showError("dateOfEntryError", "Vui lòng chọn ngày nhập");
        isValid = false;
    }


    return isValid;
}

function showError(elementId, message) {
    const errorElement =
        document.getElementById(elementId) || createErrorElement(elementId);
    errorElement.textContent = message;
    errorElement.style.display = "block";
}

function createErrorElement(id) {
    const errorDiv = document.createElement("div");
    errorDiv.id = id;
    errorDiv.className = "error-message";
    // Find appropriate element to append error message
    const relatedInput = document.querySelector(
        `[name*="${id.replace("Error", "")}"]`
    );
    if (relatedInput) {
        relatedInput.parentElement.appendChild(errorDiv);
    } else {
        document.getElementById("sizeSection").appendChild(errorDiv);
    }
    return errorDiv;
}

function renderImagePreviews() {
    const container = document.getElementById("imagePreview");
    container.innerHTML = "";

    selectedImages.forEach((imageData, index) => {
        const previewDiv = document.createElement("div");
        previewDiv.className = "image-preview";

        previewDiv.innerHTML = `
                    <img src="${imageData.url}" alt="Preview ${index + 1}">
                    <button type="button" class="remove-image" onclick="removeImage(${index})">
                        <i class="fas fa-times"></i>
                    </button>
                    ${imageData.isPrimary
                ? '<div class="primary-badge">Chính</div>'
                : ""
            }
                `;

        // Thêm sự kiện click để set làm ảnh chính
        if (!imageData.isPrimary) {
            previewDiv.style.cursor = "pointer";
            previewDiv.title = "Click để đặt làm ảnh chính";
            previewDiv.addEventListener("click", () => setPrimaryImage(index));
        }

        container.appendChild(previewDiv);
    });
}

function removeImage(index) {
    selectedImages.splice(index, 1);

    // Nếu xóa ảnh chính và còn ảnh khác, đặt ảnh đầu tiên làm chính
    if (selectedImages.length > 0) {
        const hasPrimary = selectedImages.some((img) => img.isPrimary);
        if (!hasPrimary) {
            selectedImages[0].isPrimary = true;
        }
    }

    renderImagePreviews();
}

function setPrimaryImage(index) {
    // Bỏ primary của tất cả ảnh
    selectedImages.forEach((img) => (img.isPrimary = false));
    // Đặt ảnh được chọn làm primary
    selectedImages[index].isPrimary = true;
    renderImagePreviews();
}

// API Calls
async function fetchCategories() {
    try {
        const response = await fetch(
            `${API_BASE_URL}/category/name-not-parent`,
            {
                method: "GET",
                headers: getAuthHeaders(),
            }
        );

        if (!response.ok) throw new Error("Không thể tải danh mục");

        const result = await response.json();
        return result.data || [];
    } catch (error) {
        console.error("Lỗi khi tải danh mục:", error);
        return [];
    }
}

async function fetchStatuses() {
    try {
        const response = await fetch(`${API_BASE_URL}/product/status`, {
            method: "GET",
            headers: getAuthHeaders(),
        });

        if (!response.ok) throw new Error("Không thể tải trạng thái");

        const result = await response.json();
        return result.data || [];
    } catch (error) {
        console.error("Lỗi khi tải trạng thái:", error);
        return [];
    }
}

async function fetchGoldTypes() {
    try {
        const response = await fetch(`${API_BASE_URL}/product/gold-type`, {
            method: "GET",
            headers: getAuthHeaders(),
        });

        if (!response.ok) throw new Error("Không thể tải loại vàng");

        const result = await response.json();
        return result.data || [];
    } catch (error) {
        console.error("Lỗi khi tải loại vàng:", error);
        return [];
    }
}

async function fetchProducts(filters = {}) {
    try {
        const params = new URLSearchParams();

        if (filters.name) params.append("name", filters.name);
        if (filters.categoryId)
            params.append("categoryId", filters.categoryId);
        if (filters.status) params.append("status", filters.status);
        if (filters.goldType) params.append("goldType", filters.goldType);
        if (filters.isDeleted !== undefined && filters.isDeleted !== "")
            params.append("isDeleted", filters.isDeleted);

        params.append("pageSize", pageSize);
        params.append("pageNumber", currentPage);

        const response = await fetch(
            `${API_BASE_URL}/product/filter?${params.toString()}`,
            {
                method: "GET",
                headers: getAuthHeaders(),
            }
        );

        if (!response.ok) throw new Error("Không thể tải danh sách sản phẩm");

        const result = await response.json();
        return (
            result.data || { content: [], totalElements: 0, totalPages: 0 }
        );
    } catch (error) {
        console.error("Lỗi khi tải sản phẩm:", error);
        return { content: [], totalElements: 0, totalPages: 0 };
    }
}

async function createProduct(formData) {
    try {
        const response = await fetch(`${API_BASE_URL}/product`, {
            method: "POST",
            headers: getAuthHeadersForFormData(),
            body: formData,
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.errorMessage || "Không thể tạo sản phẩm");
        }

        return await response.json();
    } catch (error) {
        console.error("Lỗi khi tạo sản phẩm:", error);
        throw error;
    }
}

async function updateProduct(productId, formData) {
    try {
        const response = await fetch(`${API_BASE_URL}/product/${productId}`, {
            method: "PUT",
            headers: getAuthHeadersForFormData(),
            body: formData,
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(
                errorData.errorMessage || "Không thể cập nhật sản phẩm"
            );
        }

        return await response.json();
    } catch (error) {
        console.error("Lỗi khi cập nhật sản phẩm:", error);
        throw error;
    }
}

async function getProductDetail(productId) {
    try {
        const response = await fetch(`${API_BASE_URL}/product/${productId}`, {
            method: "GET",
            headers: getAuthHeaders(),
        });

        if (!response.ok) throw new Error("Không thể tải chi tiết sản phẩm");

        const responseJson = await response.json();
        return responseJson.data;
    } catch (error) {
        console.error("Lỗi khi tải chi tiết sản phẩm:", error);
        throw error;
    }
}

async function updateProductStatus(productId, status) {
    try {
        const response = await fetch(
            `${API_BASE_URL}/product/status/${productId}`,
            {
                method: "PUT",
                headers: getAuthHeaders(),
                body: JSON.stringify({ status }),
            }
        );

        if (!response.ok)
            throw new Error("Không thể cập nhật trạng thái sản phẩm");

        return await response.json();
    } catch (error) {
        console.error("Lỗi khi cập nhật trạng thái sản phẩm:", error);
        throw error;
    }
}

async function softDeleteProducts(productIds) {
    try {
        const response = await fetch(`${API_BASE_URL}/product/soft-deleted`, {
            method: "DELETE",
            headers: getAuthHeaders(),
            body: JSON.stringify(productIds),
        });

        if (!response.ok) throw new Error("Không thể ẩn sản phẩm");

        return await response.json();
    } catch (error) {
        console.error("Lỗi khi ẩn sản phẩm:", error);
        throw error;
    }
}

async function restoreProducts(productIds) {
    try {
        const response = await fetch(
            `${API_BASE_URL}/product/restore-deleted`,
            {
                method: "PUT",
                headers: getAuthHeaders(),
                body: JSON.stringify(productIds),
            }
        );

        if (!response.ok) throw new Error("Không thể khôi phục sản phẩm");

        return await response.json();
    } catch (error) {
        console.error("Lỗi khi khôi phục sản phẩm:", error);
        throw error;
    }
}

async function deleteProductsPermanently(productIds) {
    try {
        const response = await fetch(
            `${API_BASE_URL}/product/hard-delete`,
            {
                method: "DELETE",
                headers: getAuthHeaders(),
                body: JSON.stringify(productIds),
            }
        );

        if (!response.ok) throw new Error("Không thể xóa sản phẩm");

        return await response.json();
    } catch (error) {
        console.error("Lỗi khi xóa sản phẩm:", error);
        throw error;
    }
}

// Initialize Page
async function initializePage() {
    if (!checkAuth()) return;

    try {
        await Promise.all([
            populateCategories(),
            populateStatuses(),
            populateGoldTypes(),
            populateCreateFormDropdowns(),
            loadProducts(),
        ]);

        setupEventListeners();
    } catch (error) {
        console.error("Lỗi khởi tạo trang:", error);
        alert("Có lỗi xảy ra khi tải trang");
    }
}

// Populate dropdown data
async function populateCategories() {
    const categories = await fetchCategories();
    const categorySelect = document.getElementById("selectCategoryId");
    const createCategorySelect = document.querySelector(
        '#createProductModal select[name="categoryId"]'
    );

    if (categorySelect) {
        categorySelect.innerHTML = '<option value="">Tất cả</option>';
        categories.forEach((category) => {
            const option = document.createElement("option");
            option.value = category.id;
            option.textContent = category.name;
            categorySelect.appendChild(option);
        });
    }

    if (createCategorySelect) {
        createCategorySelect.innerHTML =
            '<option value="">Chọn danh mục</option>';
        categories.forEach((category) => {
            const option = document.createElement("option");
            option.value = category.id;
            option.textContent = category.name;
            createCategorySelect.appendChild(option);
        });
    }
}

async function populateStatuses() {
    const statuses = await fetchStatuses();
    const statusSelect = document.getElementById("selectStatus");

    if (statusSelect) {
        statusSelect.innerHTML = '<option value="">Tất cả</option>';
        statuses.forEach((status) => {
            const option = document.createElement("option");
            option.value = status;
            option.textContent =
                status === "IN_STOCK" ? "Còn hàng" : "Hết hàng";
            statusSelect.appendChild(option);
        });
    }
}

async function populateGoldTypes() {
    const goldTypes = await fetchGoldTypes();
    const goldTypeSelect = document.getElementById("selectGoldType");
    const createGoldTypeSelect = document.querySelector(
        '#createProductModal select[name="goldType"]'
    );

    if (goldTypeSelect) {
        goldTypeSelect.innerHTML = '<option value="">Tất cả</option>';
        goldTypes.forEach((goldType) => {
            const option = document.createElement("option");
            option.value = goldType;
            option.textContent = goldType.replace("GOLD_", "");
            goldTypeSelect.appendChild(option);
        });
    }

    if (createGoldTypeSelect) {
        createGoldTypeSelect.innerHTML = '<option value="">Không có</option>';
        goldTypes.forEach((goldType) => {
            const option = document.createElement("option");
            option.value = goldType;
            option.textContent = goldType.replace("GOLD_", "");
            createGoldTypeSelect.appendChild(option);
        });
    }
}

function populateCreateFormDropdowns() {
    const isDeletedSelect = document.getElementById("selectIsDeleted");
    if (isDeletedSelect) {
        isDeletedSelect.innerHTML = `
                <option value="">Tất cả</option>
                <option value="0">Hiện</option>
                <option value="1">Ẩn</option>
            `;
    }
}

// Load and display products
async function loadProducts() {
    const filters = getFilters();
    const productsData = await fetchProducts(filters);

    currentProducts = productsData.content || [];
    totalPages = productsData.totalPages || 0;

    displayProducts(currentProducts);
    updatePagination(productsData.totalElements || 0);
}

function getFilters() {
    return {
        name: document.querySelector('input[placeholder="Nhập tên sản phẩm"]')
            .value,
        categoryId: document.getElementById("selectCategoryId").value,
        status: document.getElementById("selectStatus").value,
        goldType: document.getElementById("selectGoldType").value,
        isDeleted: document.getElementById("selectIsDeleted").value,
    };
}

function displayProducts(products) {
    const tbody = document.querySelector("table tbody");
    if (tbody) {
        tbody.innerHTML = "";
        if (!products || products.length === 0) {
            tbody.innerHTML =
                '<tr><td colspan="12" class="text-center text-muted py-4"><i class="fa-solid fa-circle-exclamation me-2"></i>Không có bản ghi nào phù hợp</td></tr>';
            return;
        }

        products.forEach((product, index) => {
            const row = createProductRow(product, index);
            tbody.appendChild(row);
        });
    }
}

function createProductRow(product, index) {
    const row = document.createElement("tr");

    const formattedPrice =
        new Intl.NumberFormat("vi-VN").format(product.price) + " VND";
    const formattedDate = new Date(product.dateOfEntry).toLocaleString(
        "vi-VN"
    );

    row.innerHTML = `
                <td class="align-middle text-center">
                    <input type="checkbox" class="product-checkbox" value="${product.id
        }" title="Chọn">
                </td>
                <td class="align-middle text-center">
                    <span class="text-secondary text-xs font-weight-bold">#${product.id
        }</span>
                </td>
                <td>
                    <div class="d-flex px-2 py-1">
                        <div>
                            <img src="${product.primaryImageUrl ||
        "https://via.placeholder.com/60"
        }" 
                                style="width: 60px !important; height: 60px" 
                                class="banner banner-sm me-3 border-radius-lg" alt="product">
                        </div>
                        <div class="d-flex flex-column justify-content-center hidden-letter">
                            <h6 class="mb-0 text-sm">${product.name}</h6>
                            <p class="text-xs text-secondary mb-0">${product.sku
        }</p>
                        </div>
                    </div>
                </td>
                <td class="align-middle text-center">
                    <span class="text-secondary text-xs font-weight-bold">${formattedPrice}</span>
                </td>
                <td class="align-middle text-center">
                    <span class="text-secondary text-xs font-weight-bold">${product.quantity
        }</span>
                </td>
                <td class="align-middle text-center">
                    <span class="text-secondary text-xs font-weight-bold">${formattedDate}</span>
                </td>
                <td class="align-middle text-center">
                    <select class="is-deleted-select" onchange="handleStatusChange(${product.id
        }, this.value)">
                        <option value="IN_STOCK" ${product.status === "IN_STOCK" ? "selected" : ""
        }>Còn hàng</option>
                        <option value="SOLD_OUT" ${product.status === "SOLD_OUT" ? "selected" : ""
        }>Hết hàng</option>
                    </select>
                </td>
                <td class="align-middle text-center">
                    <select class="is-deleted-select" onchange="handleVisibilityChange(${product.id
        }, this.value)">
                        <option value="FALSE" ${!product.isDeleted ? "selected" : ""
        }>Hiện</option>
                        <option value="TRUE" ${product.isDeleted ? "selected" : ""
        }>Ẩn</option>
                    </select>
                </td>
                <td class="align-middle text-center action">
                    <a href="javascript:;" onclick="openModalView(${product.id
        })" title="Xem chi tiết">
                        <i class="fa-solid fa-eye"></i>
                    </a>
                    |
                    <a href="javascript:;" onclick="editProduct(${product.id
        })" title="Sửa">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </a>
                    |
                    <a href="javascript:;" onclick="deleteProduct(${product.id
        })" title="Xóa" style="color: #dc3545;">
                        <i class="fa-solid fa-trash"></i>
                    </a>
                </td>
            `;

    return row;
}

function updatePagination(totalElements) {
    const paginationInfo = document.querySelector(".pagination-info");
    if (paginationInfo) {
        const start = currentPage * pageSize + 1;
        const end = Math.min((currentPage + 1) * pageSize, totalElements);
        paginationInfo.textContent = `Hiển thị ${start}-${end} trong tổng số ${totalElements} sản phẩm`;
    }

    updatePaginationControls();
}

function updatePaginationControls() {
    const pagination = document.querySelector(".pagination");
    if (pagination) {
        pagination.innerHTML = "";

        const prevLi = document.createElement("li");
        prevLi.className = `page-item ${currentPage === 0 ? "disabled" : ""}`;
        prevLi.innerHTML = `
                <a class="page-link" href="#" onclick="changePage(${currentPage - 1
            })" tabindex="-1">
                    <i class="fa-solid fa-angle-left"></i>
                </a>
                `;
        pagination.appendChild(prevLi);

        const startPage = Math.max(0, currentPage - 2);
        const endPage = Math.min(totalPages - 1, currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            const pageLi = document.createElement("li");
            pageLi.className = `page-item ${i === currentPage ? "active" : ""}`;
            pageLi.innerHTML = `<a class="page-link" href="#" onclick="changePage(${i})">${i + 1
                }</a>`;
            pagination.appendChild(pageLi);
        }

        const nextLi = document.createElement("li");
        nextLi.className = `page-item ${currentPage >= totalPages - 1 ? "disabled" : ""
            }`;
        nextLi.innerHTML = `
                <a class="page-link" href="#" onclick="changePage(${currentPage + 1
            })">
                    <i class="fa-solid fa-angle-right"></i>
                </a>
                `;
        pagination.appendChild(nextLi);
    }
}

function changePage(page) {
    if (page < 0 || page >= totalPages) return;
    currentPage = page;
    loadProducts();
}

// Modal Handlers
function openModal() {
    openCreateProductModal();
}

function openCreateProductModal() {
    editingProductId = null;
    resetCreateProductForm();

    const modalTitle = document.getElementById("modalTitle");
    if (modalTitle) modalTitle.textContent = "Tạo sản phẩm";

    const submitBtn = document.querySelector(".btn-submit");
    if (submitBtn) {
        submitBtn.innerHTML =
            '<i class="fas fa-plus"></i> Tạo sản phẩm';
    }

    const modal = document.getElementById("createProductModal");
    if (modal) {
        modal.style.display = "flex";
    }
}


function closeModal() {
    closeCreateProductModal();
}

function closeCreateProductModal() {
    const modal = document.getElementById("createProductModal");
    if (modal) {
        modal.style.display = "none";
        resetCreateProductForm();
    }
}

function resetCreateProductForm() {
    const form = document.querySelector("#createProductForm");
    if (form) {
        form.reset();
        const sizeContainer = document.querySelector("#sizeContainer");
        if (sizeContainer) sizeContainer.innerHTML = "";
        const imagePreview = document.querySelector("#imagePreview");
        if (imagePreview) imagePreview.innerHTML = "";
        selectedImages = [];
        sizeCounter = 0;
        const totalQuantity = document.querySelector("#totalQuantity");
        if (totalQuantity) totalQuantity.textContent = "0";
        const noSizeRadio = document.querySelector(
            'input[name="productType"][value="no-size"]'
        );
        if (noSizeRadio) noSizeRadio.checked = true;
        toggleSizeSection(false);
    }
}

function selectProductType(type) {
    const noSizeRadio = document.getElementById("noSizeProduct");
    const withSizeRadio = document.getElementById("withSizeProduct");
    const noSizeOption = document.querySelector(
        ".product-type-option[onclick=\"selectProductType('no-size')\"]"
    );
    const withSizeOption = document.querySelector(
        ".product-type-option[onclick=\"selectProductType('with-size')\"]"
    );

    if (type === "no-size") {
        noSizeRadio.checked = true;
        noSizeOption.classList.add("active");
        withSizeOption.classList.remove("active");
        toggleSizeSection(false);
    } else {
        withSizeRadio.checked = true;
        withSizeOption.classList.add("active");
        noSizeOption.classList.remove("active");
        toggleSizeSection(true);
    }
}

async function openModalView(productId) {
    try {
        const product = await getProductDetail(productId);

        // Format giá
        const formattedPrice = new Intl.NumberFormat("vi-VN", {
            style: "currency",
            currency: "VND",
        }).format(product.price);

        // Format ngày
        const formatDate = (dateString) => {
            return dateString
                ? new Date(dateString).toLocaleDateString("vi-VN")
                : "N/A";
        };

        // Map trạng thái
        const statusMap = {
            IN_STOCK: "Còn hàng",
            SOLD_OUT: "Hết hàng",
        };

        // Map loại vàng
        const goldTypeMap = {
            GOLD_10K: "Vàng 10K",
            GOLD_14K: "Vàng 14K",
            GOLD_18K: "Vàng 18K",
            GOLD_24K: "Vàng 24K",
        };

        // Sizes
        const sizesHTML =
            product.sizes && product.sizes.length > 0
                ? `
                    <div class="sizes-grid">
                        ${product.sizes
                    .map(
                        (size) => `
                            <div class="size-card">
                                <div class="size-number">${size.size}</div>
                                <div class="size-quantity">Số lượng: ${size.quantity}</div>
                            </div>
                        `
                    )
                    .join("")}
                    </div>
                `
                : `
                    <div class="no-sizes-message">Không có thông tin kích cỡ</div>
                `;

        // Images
        const imagesHTML = `
                    <div class="product-images">
                        ${product.images
                .map(
                    (image) => `
                            <div class="product-image" onclick="openImageModal('${image.imageUrl
                        }')">
                                <img src="${image.imageUrl
                        }" alt="Product Image">
                                ${image.isPrimary
                            ? '<div class="primary-image-badge">Chính</div>'
                            : ""
                        }
                            </div>
                        `
                )
                .join("")}
                    </div>
                `;

        // Hiển thị modal
        const modal = document.getElementById("productModal");
        const productInfo = document.querySelector("#productInfo");
        console.log(productInfo);
        if (modal && productInfo) {
            productInfo.innerHTML = `
                        <div class="form-row">
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>ID:</strong>
                                    <span>${product.id}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Mã sản phẩm:</strong>
                                    <span>${product.sku}</span>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group full-width">
                                <div class="info-field">
                                    <strong>Tên sản phẩm:</strong>
                                    <span>${product.name}</span>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Giá:</strong>
                                    <span class="price-display">${formattedPrice}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Số lượng:</strong>
                                    <span>${product.quantity}</span>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Ngày nhập:</strong>
                                    <span>${formatDate(
                product.dateOfEntry
            )}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Tuổi vàng:</strong>
                                    <span>${product.goldType
                    ? `<span class="gold-type-badge">${goldTypeMap[product.goldType]
                    }</span>`
                    : "Không có"
                }</span>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Trạng thái:</strong>
                                    <span class="status-badge ${product.status === "IN_STOCK"
                    ? "status-in-stock"
                    : "status-out-of-stock"
                }">
                                        ${statusMap[product.status] ||
                product.status
                }
                                    </span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Danh mục:</strong>
                                    <span>${product.categoryName}</span>
                                </div>
                            </div>
                        </div>


                        <div class="form-row">
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Ngày tạo:</strong>
                                    <span>${formatDate(product.createAt)}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="info-field">
                                    <strong>Ngày cập nhật:</strong>
                                    <span>${formatDate(product.updateAt)}</span>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group full-width">
                                <div class="info-field">
                                    <strong>Mô tả:</strong>
                                    <span>${product.description || "Không có mô tả"
                }</span>
                                </div>
                            </div>
                        </div>

                        <!-- Kích cỡ -->
                        <div class="form-row">
                            <div class="form-group full-width">
                                <div class="info-field">
                                    <strong>Kích cỡ:</strong>
                                    <span>${sizesHTML}</span>
                                </div>
                            </div>
                        </div>

                        <!-- Ảnh sản phẩm -->
                        <div class="form-row">
                            <div class="form-group full-width">
                                <div class="info-field">
                                    <strong>Ảnh sản phẩm:</strong>
                                    <span>${imagesHTML}</span>
                                </div>
                            </div>
                        </div>
                `;
            modal.style.display = "flex";
        }
    } catch (error) {
        alert("Lỗi khi xem chi tiết sản phẩm: " + error.message);
    }
}

function openImageModal(imageUrl) {
    const modal = document.getElementById("imageModal");
    const modalImage = document.getElementById("modalImage");
    modalImage.src = imageUrl;
    modal.style.display = "block";
    document.body.style.overflow = "hidden";
}

function closeImageModal() {
    const modal = document.getElementById("imageModal");
    modal.style.display = "none";
    document.body.style.overflow = "auto";
    // Clear image source to prevent memory leaks
    document.getElementById("modalImage").src = "";
}

// Close modal when clicking outside
window.onclick = function (event) {
    const productModal = document.getElementById("productModal");
    const imageModal = document.getElementById("imageModal");
    if (event.target === productModal) {
        closeModalView();
    } else if (event.target === imageModal) {
        closeImageModal();
    }
};

// Initialize page
document.addEventListener("DOMContentLoaded", function () {
    // Ensure modals are hidden on page load
    document.getElementById("productModal").style.display = "none";
    document.getElementById("imageModal").style.display = "none";
});

function closeModalView() {
    const modal = document.getElementById("productModal");
    if (modal) modal.style.display = "none";
}

function openImageModal(imageUrl) {
    const modal = document.getElementById("imageModal");
    const modalImage = document.getElementById("modalImage");
    if (modal && modalImage) {
        modalImage.src = imageUrl;
        modal.style.display = "block";
    }
}

function closeImageModal() {
    const modal = document.getElementById("imageModal");
    if (modal) modal.style.display = "none";
}

async function editProduct(productId) {
    try {
        const product = await getProductDetail(productId);
        editingProductId = productId;

        const form = document.querySelector("#createProductForm");
        if (form) {
            form.querySelector('input[name="sku"]').value = product.sku || "";
            form.querySelector('input[name="name"]').value = product.name || "";
            form.querySelector('input[name="price"]').value =
                product.price || "";
            form.querySelector('input[name="quantity"]').value =
                product.quantity || "";
            form.querySelector('input[name="dateOfEntry"]').value =
                product.dateOfEntry ? product.dateOfEntry.split("T")[0] : "";

            const categorySelect = form.querySelector(
                'select[name="categoryId"]'
            );
            if (categorySelect) categorySelect.value = product.categoryId || "";
            const goldTypeSelect = form.querySelector(
                'select[name="goldType"]'
            );
            if (goldTypeSelect) goldTypeSelect.value = product.goldType || "";
            form.querySelector('textarea[name="description"]').value =
                product.description || "";

            const sizeContainer = document.querySelector("#sizeContainer");
            if (sizeContainer) sizeContainer.innerHTML = "";
            if (product.sizes && product.sizes.length > 0) {
                form.querySelector(
                    'input[name="productType"][value="with-size"]'
                ).checked = true;
                selectProductType("with-size");
                product.sizes.forEach((size) =>
                    addSizeItem(size.size, size.quantity)
                );
                updateTotalQuantity();
            } else {
                form.querySelector(
                    'input[name="productType"][value="no-size"]'
                ).checked = true;
                selectProductType("no-size");
            }

            selectedImages = product.images
                ? product.images.map((img) => ({
                    url: img.imageUrl,
                    isPrimary: img.isPrimary,
                }))
                : [];

            renderImagePreviews();

            const modalTitle = document.getElementById("modalTitle");
            if (modalTitle) modalTitle.textContent = "Cập nhật sản phẩm";

            const submitBtn = document.querySelector(".btn-submit");
            if (submitBtn) {
                submitBtn.innerHTML =
                    '<i class="fas fa-save"></i> Cập nhật sản phẩm';
            }

            const modal = document.getElementById("createProductModal");
            if (modal) modal.style.display = "flex";
        }
    } catch (error) {
        alert("Lỗi khi tải dữ liệu sản phẩm: " + error.message);
    }
}

async function deleteProduct(productId) {
    if (!confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) return;

    try {
        await deleteProductsPermanently([productId]);
        showSuccessMessage("Xóa sản phẩm thành công");
        loadProducts();
    } catch (error) {
        alert("Lỗi khi xóa sản phẩm: " + error.message);
    }
}

// Size Management
function addSizeItem(size = "", quantity = "") {
    const sizeContainer = document.querySelector("#sizeContainer");
    if (sizeContainer) {
        const sizeRow = document.createElement("div");
        sizeRow.className = "size-row mb-2";
        sizeRow.innerHTML = `
                    <input type="number" name="sizes[${sizeCounter}].size" placeholder="Kích cỡ" value="${size}" class="form-control d-inline-block w-25">
                    <input type="number" name="sizes[${sizeCounter}].quantity" placeholder="Số lượng" value="${quantity}" class="form-control d-inline-block w-25" oninput="updateTotalQuantity()">
                    <button type="button" class="btn btn-danger btn-sm" onclick="removeSizeRow(this)">Xóa</button>
                `;
        sizeContainer.appendChild(sizeRow);
        sizeCounter++;
        updateTotalQuantity();
    }
}

function removeSizeRow(button) {
    if (button && button.parentElement) {
        button.parentElement.remove();
        updateTotalQuantity();
    }
}

function updateTotalQuantity() {
    const quantities = document.querySelectorAll(
        'input[name*="sizes"][name$="quantity"]'
    );
    const total = Array.from(quantities).reduce(
        (sum, input) => sum + (parseInt(input.value) || 0),
        0
    );
    const totalQuantity = document.querySelector("#totalQuantity");
    if (totalQuantity) totalQuantity.textContent = total;
}

function toggleSizeSection(show) {
    const sizeSection = document.querySelector("#sizeSection");
    const quantitySection = document.querySelector(".quantity-section");
    if (sizeSection) {
        sizeSection.style.display = show ? "block" : "none";
    }
    if (quantitySection) {
        quantitySection.style.display = show ? "none" : "block";
    }
}

// Image Management
function handleImageSelection(event) {
    const files = Array.from(event.target.files);

    files.forEach((file) => {
        const reader = new FileReader();
        reader.onload = function (e) {
            selectedImages.push({
                url: e.target.result,
                file,
                isPrimary: selectedImages.length === 0,
            });
            renderImagePreviews();
        };
        reader.readAsDataURL(file);
    });
}

// Form Submission
function validateForm(data, productType) {
    let isValid = true;

    // Clear all previous error messages
    const errorMessages = document.querySelectorAll(".error-message");
    errorMessages.forEach((error) => {
        error.style.display = "none";
        error.textContent = "";
    });

    if (!data.sku || data.sku.trim() === "") {
        showError("skuError", "Vui lòng nhập mã sản phẩm");
        isValid = false;
    } else if (data.sku.trim().length > 50) {
        showError("skuError", "Mã sản phẩm không được vượt quá 50 ký tự");
        isValid = false;
    } else if (!/^[A-Za-z0-9_-]+$/.test(data.sku.trim())) {
        showError("skuError", "Mã sản phẩm chỉ được chứa chữ cái, số, dấu gạch ngang và gạch dưới");
        isValid = false;
    }

    if (!data.name || data.name.trim() === "") {
        showError("nameError", "Vui lòng nhập tên sản phẩm");
        isValid = false;
    } else if (data.name.trim().length < 2) {
        showError("nameError", "Tên sản phẩm phải có ít nhất 2 ký tự");
        isValid = false;
    }

    if (!data.price || data.price.toString().trim() === "") {
        showError("priceError", "Vui lòng nhập giá sản phẩm");
        isValid = false;
    } else {
        const price = parseFloat(data.price);
        if (isNaN(price) || price <= 0) {
            showError("priceError", "Giá sản phẩm phải là số dương hợp lệ");
            isValid = false;
        } else if (price > 999999999) {
            showError("priceError", "Giá sản phẩm không được vượt quá 999,999,999");
            isValid = false;
        }
    }

    if (!data.dateOfEntry || data.dateOfEntry.trim() === "") {
        showError("dateOfEntryError", "Vui lòng chọn ngày nhập trang sức");
        isValid = false;
    } else {
        const entryDate = new Date(data.dateOfEntry);
        const today = new Date();
        today.setHours(23, 59, 59, 999);

        if (entryDate > today) {
            showError("dateOfEntryError", "Ngày nhập không thể là ngày trong tương lai");
            isValid = false;
        }

        const minDate = new Date();
        minDate.setFullYear(minDate.getFullYear() - 10);
        if (entryDate < minDate) {
            showError("dateOfEntryError", "Ngày nhập không hợp lệ (quá xa trong quá khứ)");
            isValid = false;
        }
    }

    if (!data.categoryId || data.categoryId.trim() === "") {
        showError("categoryIdError", "Vui lòng chọn danh mục sản phẩm");
        isValid = false;
    }

    if (productType === "no-size") {
        if (!data.quantity && data.quantity !== 0) {
            showError("quantityError", "Vui lòng nhập số lượng");
            isValid = false;
        } else {
            const quantity = parseInt(data.quantity);
            if (isNaN(quantity) || quantity < 0) {
                showError("quantityError", "Số lượng phải là số nguyên không âm");
                isValid = false;
            } else if (quantity > 999999) {
                showError("quantityError", "Số lượng không được vượt quá 999,999");
                isValid = false;
            }
        }
    } else if (productType === "with-size") {
        if (!data.sizes || data.sizes.length === 0) {
            showError("sizesError", "Vui lòng thêm ít nhất một kích cỡ");
            isValid = false;
        } else {
            const sizeValues = new Set();
            let totalQuantity = 0;

            data.sizes.forEach((sizeItem, index) => {
                if (!sizeItem.size && sizeItem.size !== 0) {
                    showError(`size${index + 1}Error`, "Vui lòng nhập kích cỡ");
                    isValid = false;
                } else {
                    const sizeValue = parseFloat(sizeItem.size);
                    if (isNaN(sizeValue) || sizeValue <= 0) {
                        showError(`size${index + 1}Error`, "Kích cỡ phải là số dương hợp lệ");
                        isValid = false;
                    } else if (sizeValue > 100) {
                        showError(`size${index + 1}Error`, "Kích cỡ không được vượt quá 100");
                        isValid = false;
                    } else if (sizeValues.has(sizeValue)) {
                        showError(`size${index + 1}Error`, "Kích cỡ này đã tồn tại");
                        isValid = false;
                    } else {
                        sizeValues.add(sizeValue);
                    }
                }

                if (!sizeItem.quantity && sizeItem.quantity !== 0) {
                    showError(`sizeQuantity${index + 1}Error`, "Vui lòng nhập số lượng");
                    isValid = false;
                } else {
                    const quantity = parseInt(sizeItem.quantity);
                    if (isNaN(quantity) || quantity < 0) {
                        showError(`sizeQuantity${index + 1}Error`, "Số lượng phải là số nguyên không âm");
                        isValid = false;
                    } else if (quantity > 999999) {
                        showError(`sizeQuantity${index + 1}Error`, "Số lượng không được vượt quá 999,999");
                        isValid = false;
                    } else {
                        totalQuantity += quantity;
                    }
                }
            });

            if (totalQuantity > 999999) {
                showError("sizesError", "Tổng số lượng tất cả kích cỡ không được vượt quá 999,999");
                isValid = false;
            }
        }
    }

    if (data.description && data.description.trim().length > 255) {
        showError("descriptionError", "Mô tả không được vượt quá 255 ký tự");
        isValid = false;
    }

    if (!selectedImages || selectedImages.length === 0) {
        showError("imagesError", "Vui lòng chọn ít nhất một ảnh sản phẩm");
        isValid = false;
    } else {
        const hasPrimaryImage = selectedImages.some(img => img.isPrimary);
        if (!hasPrimaryImage) {
            showError("imagesError", "Vui lòng chọn một ảnh làm ảnh chính");
            isValid = false;
        }

        selectedImages.forEach((img, index) => {
            if (img.file) {
                const validTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
                if (!validTypes.includes(img.file.type)) {
                    showError("imagesError", `Ảnh ${index + 1}: Chỉ chấp nhận file ảnh (JPG, PNG, GIF, WebP)`);
                    isValid = false;
                }

                const maxSize = 5 * 1024 * 1024;
                if (img.file.size > maxSize) {
                    showError("imagesError", `Ảnh ${index + 1}: Dung lượng file không được vượt quá 5MB`);
                    isValid = false;
                }
            }
        });
    }

    return isValid;
}

function showError(elementId, message) {
    const errorElement = document.getElementById(elementId) || createErrorElement(elementId);
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = "block";

        if (!document.querySelector('.error-message[style*="block"]') ||
            errorElement === document.querySelector('.error-message[style*="block"]')) {
            errorElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    }
}

function createErrorElement(id) {
    const errorDiv = document.createElement("div");
    errorDiv.id = id;
    errorDiv.className = "error-message";
    errorDiv.style.display = "none";

    let targetParent = null;

    if (id.includes("size") && id.includes("Error")) {
        targetParent = document.getElementById("sizeSection");
    } else {
        const fieldName = id.replace("Error", "");
        targetParent = document.querySelector(`[name="${fieldName}"]`)?.parentElement ||
            document.getElementById(fieldName)?.parentElement ||
            document.querySelector(`#${fieldName}`)?.parentElement;
    }

    if (targetParent) {
        targetParent.appendChild(errorDiv);
    } else {
        const form = document.querySelector('form');
        if (form) {
            form.appendChild(errorDiv);
        }
    }

    return errorDiv;
}

async function handleFormSubmit(event) {
    event.preventDefault();
    const form = event.target;

    try {
        // Collect form data
        const data = collectFormData(form);

        // Get product type
        const productTypeElement = form.querySelector('input[name="productType"]:checked');
        if (!productTypeElement) {
            alert("Vui lòng chọn loại sản phẩm");
            return;
        }
        const productType = productTypeElement.value;

        // Validate form
        if (!validateForm(data, productType)) {
            return; // Stop submission if validation fails
        }

        // Prepare FormData for API
        const formData = prepareFormData(data, productType);

        // Submit form
        if (editingProductId) {
            await updateProduct(editingProductId, formData);
            showSuccessMessage("Cập nhật sản phẩm thành công");
        } else {
            await createProduct(formData);
            showSuccessMessage("Tạo sản phẩm thành công");
        }

        closeCreateProductModal();
        loadProducts();

    } catch (error) {
        console.error("Form submission error:", error);
        alert("Lỗi: " + (error.message || "Có lỗi xảy ra khi xử lý form"));
    }
}

function collectFormData(form) {
    const data = {
        sku: form.querySelector('input[name="sku"]')?.value || "",
        name: form.querySelector('input[name="name"]')?.value || "",
        price: form.querySelector('input[name="price"]')?.value || "",
        dateOfEntry: form.querySelector('input[name="dateOfEntry"]')?.value || "",
        categoryId: form.querySelector('select[name="categoryId"]')?.value || "",
        goldType: form.querySelector('select[name="goldType"]')?.value || "",
        description: form.querySelector('textarea[name="description"]')?.value || "",
        quantity: form.querySelector('input[name="quantity"]')?.value || "",
        sizes: []
    };

    // Collect size data if applicable
    const sizeRows = form.querySelectorAll(".size-row");
    sizeRows.forEach((row, index) => {
        const sizeInput = row.querySelector(`input[name="sizes[${index}].size"]`);
        const quantityInput = row.querySelector(`input[name="sizes[${index}].quantity"]`);

        if (sizeInput && quantityInput) {
            const size = sizeInput.value;
            const quantity = quantityInput.value;

            if (size && quantity) {
                data.sizes.push({
                    size: parseFloat(size),
                    quantity: parseInt(quantity)
                });
            }
        }
    });

    return data;
}

function prepareFormData(data, productType) {
    const formData = new FormData();

    // Append basic fields
    formData.append("sku", data.sku.trim());
    formData.append("name", data.name.trim());
    formData.append("price", data.price);
    formData.append("dateOfEntry", data.dateOfEntry);
    formData.append("categoryId", data.categoryId);
    formData.append("goldType", data.goldType);
    formData.append("description", data.description.trim());

    // Append quantity or sizes based on product type
    if (productType === "no-size") {
        formData.append("quantity", data.quantity);
    } else if (productType === "with-size") {
        data.sizes.forEach((sizeItem, index) => {
            formData.append(`sizes[${index}].size`, sizeItem.size);
            formData.append(`sizes[${index}].quantity`, sizeItem.quantity);
        });
    }

    // Append images
    if (selectedImages && selectedImages.length > 0) {
        selectedImages.forEach((img, index) => {
            if (img.file) {
                const key = editingProductId ? "imageFiles" : "images";
                formData.append(key, img.file);
                formData.append(`${key}[${index}].isPrimary`, img.isPrimary);
            }
        });
    }

    return formData;
}

// Event handlers
async function handleStatusChange(productId, newStatus) {
    try {
        await updateProductStatus(productId, newStatus);
        showSuccessMessage("Cập nhật trạng thái thành công");
    } catch (error) {
        alert("Lỗi khi cập nhật trạng thái: " + error.message);
        loadProducts();
    }
}

async function handleVisibilityChange(productId, isDeleted) {
    try {
        if (isDeleted === "TRUE") {
            await softDeleteProducts([productId]);
            showSuccessMessage("Ẩn sản phẩm thành công");
        } else {
            await restoreProducts([productId]);
            showSuccessMessage("Hiện sản phẩm thành công");
        }
        loadProducts();
    } catch (error) {
        alert("Lỗi khi thay đổi tình trạng: " + error.message);
        loadProducts();
    }
}

function setupEventListeners() {
    // Filter event listeners
    const nameInput = document.querySelector(
        'input[placeholder="Nhập tên sản phẩm"]'
    );
    if (nameInput)
        nameInput.addEventListener("input", debounce(loadProducts, 500));
    const categorySelect = document.getElementById("selectCategoryId");
    if (categorySelect)
        categorySelect.addEventListener("change", loadProducts);
    const statusSelect = document.getElementById("selectStatus");
    if (statusSelect) statusSelect.addEventListener("change", loadProducts);
    const goldTypeSelect = document.getElementById("selectGoldType");
    if (goldTypeSelect)
        goldTypeSelect.addEventListener("change", loadProducts);
    const isDeletedSelect = document.getElementById("selectIsDeleted");
    if (isDeletedSelect)
        isDeletedSelect.addEventListener("change", loadProducts);

    // Page size change
    const pageSizeSelect = document.querySelector(
        'select[name="pageSize"]'
    );
    if (pageSizeSelect) {
        pageSizeSelect.addEventListener("change", function () {
            pageSize = parseInt(this.value);
            currentPage = 0;
            loadProducts();
        });
    }

    // Select all checkbox
    const selectAllCheckbox = document.getElementById("selectAll");
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener("change", function () {
            const checkboxes = document.querySelectorAll(".product-checkbox");
            checkboxes.forEach((checkbox) => {
                checkbox.checked = this.checked;
            });
            updateSelectedProducts();
        });
    }

    // Individual checkbox
    const table = document.querySelector("table");
    if (table) {
        table.addEventListener("change", function (event) {
            if (event.target.classList.contains("product-checkbox")) {
                updateSelectedProducts();
            }
        });
    }

    // Delete all button
    const deleteAllBtn = document.getElementById("deleteAll");
    if (deleteAllBtn)
        deleteAllBtn.addEventListener("click", handleDeleteSelected);

    // Create product button
    const createProductBtn = document.querySelector(
        'a[onclick="openModal()"]'
    );
    if (createProductBtn)
        createProductBtn.addEventListener("click", openCreateProductModal);

    // Close modal
    document.querySelectorAll(".modal .close").forEach((btn) => {
        btn.addEventListener("click", function () {
            const modal = this.closest(".modal");
            if (modal) modal.style.display = "none";
        });
    });

    // Form submission
    const createForm = document.querySelector("#createProductForm");
    if (createForm) createForm.addEventListener("submit", handleFormSubmit);

    // Product type toggle
    document
        .querySelectorAll('input[name="productType"]')
        .forEach((input) => {
            input.addEventListener("change", function () {
                toggleSizeSection(this.value === "with-size");
            });
        });

    // Add size button
    const addSizeBtn = document.getElementById("addSizeBtn");
    if (addSizeBtn)
        addSizeBtn.addEventListener("click", () => addSizeItem());

    // Image selection
    const imageInput = document.querySelector('input[name="images"]');
    if (imageInput)
        imageInput.addEventListener("change", handleImageSelection);
}

// Utility functions
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function showSuccessMessage(message) {
    const successDiv = document.createElement("div");
    successDiv.className = "alert alert-success";
    successDiv.textContent = message;
    successDiv.style.position = "fixed";
    successDiv.style.top = "20px";
    successDiv.style.right = "20px";
    successDiv.style.zIndex = "9999";

    document.body.appendChild(successDiv);

    setTimeout(() => {
        successDiv.remove();
    }, 3000);
}

function updateSelectedProducts() {
    const checkboxes = document.querySelectorAll(
        ".product-checkbox:checked"
    );
    selectedProductIds = Array.from(checkboxes).map((cb) =>
        parseInt(cb.value)
    );
}

async function handleDeleteSelected() {
    updateSelectedProducts();

    if (selectedProductIds.length === 0) {
        alert("Vui lòng chọn ít nhất một sản phẩm");
        return;
    }

    if (
        !confirm(
            `Bạn có chắc chắn muốn xóa ${selectedProductIds.length} sản phẩm đã chọn?`
        )
    ) {
        return;
    }

    try {
        await deleteProductsPermanently(selectedProductIds);
        showSuccessMessage("Xóa sản phẩm thành công");
        loadProducts();
        selectedProductIds = [];
        const selectAllCheckbox = document.getElementById("selectAll");
        if (selectAllCheckbox) selectAllCheckbox.checked = false;
    } catch (error) {
        alert("Lỗi khi xóa sản phẩm: " + error.message);
    }
}

// Initialize page
document.addEventListener("DOMContentLoaded", initializePage);