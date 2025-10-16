document.addEventListener('DOMContentLoaded', () => {
    let index = 1;
    const addBtn = document.getElementById('addProduct');
    const container = document.getElementById('products');
    const totalSpan = document.getElementById('totalAmount');

    // Store a clean template row
    const templateRow = document.querySelector('.product-row').cloneNode(true);

    // Recalculate total
    function recalculateTotal() {
        let total = 0;
        document.querySelectorAll('.product-row').forEach(row => {
            const select = row.querySelector('select');
            const qty = parseInt(row.querySelector('.quantity').value) || 0;
            const opt = select.options[select.selectedIndex];
            const price = parseFloat(opt?.dataset.price || 0);
            const subtotal = price * qty;
            row.querySelector('.subtotal').textContent = `₹${subtotal.toFixed(2)}`;
            total += subtotal;
        });
        totalSpan.textContent = `₹${total.toFixed(2)}`;
    }

    // Update product info
    function updateInfo(row) {
        const select = row.querySelector('select');
        const info = row.querySelector('.product-info');
        const opt = select.options[select.selectedIndex];
        if (opt && opt.value) {
            info.textContent = `Price: ₹${opt.dataset.price} | Stock: ${opt.dataset.quantity}`;
        } else {
            info.textContent = '';
        }
        recalculateTotal();
    }

    // Function to handle adding a new row
    function addProductRow() {
        const newRow = templateRow.cloneNode(true);

        // Update form field names for correct Spring binding
        const select = newRow.querySelector('select');
        const quantityInput = newRow.querySelector('.quantity');
        const removeBtn = newRow.querySelector('.remove-product');

        select.name = `productIdQuantities[${index}].productId`;
        quantityInput.name = `productIdQuantities[${index}].quantity`;
        quantityInput.value = 1;
        select.selectedIndex = 0;
        newRow.querySelector('.product-info').textContent = '';
        newRow.querySelector('.subtotal').textContent = '₹0.00';

        // Enable remove button for new rows
        removeBtn.style.display = 'inline-block';
        removeBtn.addEventListener('click', () => {
            newRow.remove();
            recalculateTotal();
        });

        // Add event listeners
        select.addEventListener('change', () => updateInfo(newRow));
        quantityInput.addEventListener('input', () => recalculateTotal());

        container.appendChild(newRow);
        index++;
        recalculateTotal();
    }

    // Fix: clone button to prevent multiple event listeners
    const newAddBtn = addBtn.cloneNode(true);
    addBtn.replaceWith(newAddBtn);
    newAddBtn.addEventListener('click', addProductRow);

    // Setup first/default row
    const firstRow = document.querySelector('.product-row');
    const firstSelect = firstRow.querySelector('select');
    const firstQuantity = firstRow.querySelector('.quantity');
    const firstRemoveBtn = firstRow.querySelector('.remove-product');

    firstSelect.name = `productIdQuantities[0].productId`;
    firstQuantity.name = `productIdQuantities[0].quantity`;

    firstSelect.addEventListener('change', () => updateInfo(firstRow));
    firstQuantity.addEventListener('input', () => recalculateTotal());
    firstRemoveBtn.style.display = 'none';

    recalculateTotal();
});
