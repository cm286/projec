const ops = require('../utils/operations');

// ==================== SUPPLIER CONTROLLER ====================

exports.searchSuppliers = async (req, res) => {
  try {
    const { searchTerm = '' } = req.query;
    const suppliers = await ops.searchSuppliers(searchTerm);
    res.json({ success: true, data: suppliers });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getSupplier = async (req, res) => {
  try {
    const { id } = req.params;
    const supplier = await ops.getSupplierById(id);
    if (!supplier) {
      return res.status(404).json({ success: false, error: 'Supplier not found' });
    }
    res.json({ success: true, data: supplier });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.createSupplier = async (req, res) => {
  try {
    const { code, name, address, phone } = req.body;
    if (!code || !name || !address || !phone) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }
    const supplier = await ops.createSupplier(code, name, address, phone);
    res.status(201).json({ success: true, data: supplier });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== ITEM CONTROLLER ====================

exports.searchItems = async (req, res) => {
  try {
    const { searchTerm = '' } = req.query;
    const items = await ops.searchItems(searchTerm);
    res.json({ success: true, data: items });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getAllItems = async (req, res) => {
  try {
    const items = await ops.getAllItems();
    res.json({ success: true, data: items });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getItem = async (req, res) => {
  try {
    const { id } = req.params;
    const item = await ops.getItemById(id);
    if (!item) {
      return res.status(404).json({ success: false, error: 'Item not found' });
    }
    res.json({ success: true, data: item });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.createItem = async (req, res) => {
  try {
    const { code, name, description = '' } = req.body;
    if (!code || !name) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }
    const item = await ops.createItem(code, name, description);
    res.status(201).json({ success: true, data: item });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== IMPORT RECEIPT CONTROLLER ====================

exports.createImportReceipt = async (req, res) => {
  try {
    const { supplier_id, items, import_date } = req.body;
    if (!supplier_id || !items || items.length === 0) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }

    // Create receipt
    const receipt = await ops.createImportReceipt(supplier_id, import_date || new Date());

    // Add items and update quantities
    for (const item of items) {
      await ops.addImportReceiptDetail(receipt.id, item.item_id, item.quantity, item.unit_price);
      await ops.updateItemQuantity(item.item_id, item.quantity);
    }

    // Finalize receipt (calculate total)
    await ops.finalizeImportReceipt(receipt.id);

    const fullReceipt = await ops.getImportReceiptById(receipt.id);
    res.status(201).json({ success: true, data: fullReceipt });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getImportReceipt = async (req, res) => {
  try {
    const { id } = req.params;
    const receipt = await ops.getImportReceiptById(id);
    if (!receipt) {
      return res.status(404).json({ success: false, error: 'Import receipt not found' });
    }
    res.json({ success: true, data: receipt });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getAllImportReceipts = async (req, res) => {
  try {
    const receipts = await ops.getAllImportReceipts();
    res.json({ success: true, data: receipts });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== EXPORT RECEIPT CONTROLLER ====================

exports.createExportReceipt = async (req, res) => {
  try {
    const { distributor_id, items, export_date } = req.body;
    if (!distributor_id || !items || items.length === 0) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }

    // Validate quantities
    for (const item of items) {
      const itemData = await ops.getItemById(item.item_id);
      if (!itemData || itemData.current_quantity < item.quantity) {
        return res.status(400).json({ 
          success: false, 
          error: `Insufficient quantity for item: ${item.item_id}` 
        });
      }
    }

    // Create receipt
    const receipt = await ops.createExportReceipt(distributor_id, export_date || new Date());

    // Add items and update quantities
    for (const item of items) {
      await ops.addExportReceiptDetail(receipt.id, item.item_id, item.quantity, item.unit_price);
      await ops.updateItemQuantity(item.item_id, -item.quantity);
    }

    // Finalize receipt
    await ops.finalizeExportReceipt(receipt.id);

    const fullReceipt = await ops.getExportReceiptById(receipt.id);
    res.status(201).json({ success: true, data: fullReceipt });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getExportReceipt = async (req, res) => {
  try {
    const { id } = req.params;
    const receipt = await ops.getExportReceiptById(id);
    if (!receipt) {
      return res.status(404).json({ success: false, error: 'Export receipt not found' });
    }
    res.json({ success: true, data: receipt });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getAllExportReceipts = async (req, res) => {
  try {
    const receipts = await ops.getAllExportReceipts();
    res.json({ success: true, data: receipts });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== DISTRIBUTOR CONTROLLER ====================

exports.searchDistributors = async (req, res) => {
  try {
    const { searchTerm = '' } = req.query;
    const distributors = await ops.searchDistributors(searchTerm);
    res.json({ success: true, data: distributors });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getDistributor = async (req, res) => {
  try {
    const { id } = req.params;
    const distributor = await ops.getDistributorById(id);
    if (!distributor) {
      return res.status(404).json({ success: false, error: 'Distributor not found' });
    }
    res.json({ success: true, data: distributor });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.createDistributor = async (req, res) => {
  try {
    const { code, brand_name, address, phone } = req.body;
    if (!code || !brand_name || !address || !phone) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }
    const distributor = await ops.createDistributor(code, brand_name, address, phone);
    res.status(201).json({ success: true, data: distributor });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== STATISTICS CONTROLLER ====================

exports.getItemStatistics = async (req, res) => {
  try {
    const { startDate, endDate } = req.query;
    if (!startDate || !endDate) {
      return res.status(400).json({ success: false, error: 'startDate and endDate are required' });
    }
    const stats = await ops.getItemStatistics(startDate, endDate);
    res.json({ success: true, data: stats });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getItemDetailStatistics = async (req, res) => {
  try {
    const { itemId } = req.params;
    const { startDate, endDate } = req.query;
    if (!startDate || !endDate) {
      return res.status(400).json({ success: false, error: 'startDate and endDate are required' });
    }
    const stats = await ops.getItemDetailStatistics(itemId, startDate, endDate);
    res.json({ success: true, data: stats });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getDistributorStatistics = async (req, res) => {
  try {
    const { startDate, endDate } = req.query;
    if (!startDate || !endDate) {
      return res.status(400).json({ success: false, error: 'startDate and endDate are required' });
    }
    const stats = await ops.getDistributorStatistics(startDate, endDate);
    res.json({ success: true, data: stats });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getDistributorDetailStatistics = async (req, res) => {
  try {
    const { distributorId } = req.params;
    const { startDate, endDate } = req.query;
    if (!startDate || !endDate) {
      return res.status(400).json({ success: false, error: 'startDate and endDate are required' });
    }
    const stats = await ops.getDistributorDetailStatistics(distributorId, startDate, endDate);
    res.json({ success: true, data: stats });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};
