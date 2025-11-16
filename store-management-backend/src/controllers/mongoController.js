const Supplier = require('../models/Supplier');
const Item = require('../models/Item');
const ImportReceipt = require('../models/ImportReceipt');
const ExportReceipt = require('../models/ExportReceipt');
const Distributor = require('../models/Distributor');

// Helper to accept either snake_case or camelCase fields from frontend
const getField = (obj = {}, snake, camel) => {
  if (obj == null) return undefined;
  if (Object.prototype.hasOwnProperty.call(obj, snake)) return obj[snake];
  return obj[camel];
};

// ==================== SUPPLIER CONTROLLER ====================

exports.searchSuppliers = async (req, res) => {
  try {
    const searchTerm = req.query.searchTerm ?? req.query.name ?? '';
    const suppliers = await Supplier.find({
      $or: [
        { name: { $regex: searchTerm, $options: 'i' } },
        { code: { $regex: searchTerm, $options: 'i' } }
      ]
    });
    res.json({ success: true, data: suppliers });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getSupplier = async (req, res) => {
  try {
    const { id } = req.params;
    const supplier = await Supplier.findById(id);
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
    const code = getField(req.body, 'code', 'code');
    const name = getField(req.body, 'name', 'name');
    const address = getField(req.body, 'address', 'address');
    const phone = getField(req.body, 'phone', 'phoneNumber');

    if (!code || !name || !address || !phone) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }

    const existingSupplier = await Supplier.findOne({ code });
    if (existingSupplier) {
      return res.status(400).json({ success: false, error: 'Supplier code already exists' });
    }

    const supplier = new Supplier({ code, name, address, phone });
    await supplier.save();
    res.status(201).json({ success: true, data: supplier });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== ITEM CONTROLLER ====================

exports.searchItems = async (req, res) => {
  try {
    const searchTerm = req.query.searchTerm ?? req.query.name ?? '';
    const items = await Item.find({
      $or: [
        { name: { $regex: searchTerm, $options: 'i' } },
        { code: { $regex: searchTerm, $options: 'i' } }
      ]
    });
    res.json({ success: true, data: items });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getAllItems = async (req, res) => {
  try {
    const items = await Item.find();
    res.json({ success: true, data: items });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getItem = async (req, res) => {
  try {
    const { id } = req.params;
    const item = await Item.findById(id);
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
    const code = getField(req.body, 'code', 'code');
    const name = getField(req.body, 'name', 'name');
    const description = getField(req.body, 'description', 'description') ?? '';
    if (!code || !name) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }

    const existingItem = await Item.findOne({ code });
    if (existingItem) {
      return res.status(400).json({ success: false, error: 'Item code already exists' });
    }

    const item = new Item({ code, name, description });
    await item.save();
    res.status(201).json({ success: true, data: item });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== IMPORT RECEIPT CONTROLLER ====================

exports.createImportReceipt = async (req, res) => {
  try {
    const supplier_id = getField(req.body, 'supplier_id', 'supplierId');
    const items = req.body.items || [];
    const import_date = getField(req.body, 'import_date', 'importDate') || getField(req.body, 'importDate', 'import_date');

    if (!supplier_id || !items || items.length === 0) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }

    // Verify supplier exists
    const supplier = await Supplier.findById(supplier_id);
    if (!supplier) {
      return res.status(404).json({ success: false, error: 'Supplier not found' });
    }

    // Process items and update inventory
    let details = [];
    let totalAmount = 0;

    for (const item of items) {
      const itemId = getField(item, 'item_id', 'itemId');
      const quantity = getField(item, 'quantity', 'quantity');
      const unit_price = getField(item, 'unit_price', 'unitPrice');

      const itemDoc = await Item.findById(itemId);
      if (!itemDoc) {
        return res.status(404).json({ success: false, error: `Item ${itemId} not found` });
      }

      const itemTotal = quantity * unit_price;
      details.push({
        item_id: itemId,
        quantity,
        unit_price,
        total_amount: itemTotal
      });
      totalAmount += itemTotal;

      // Update item quantity
      itemDoc.current_quantity += quantity;
      await itemDoc.save();
    }

    const code = `IMP-${Date.now()}`;
    const receipt = new ImportReceipt({
      code,
      supplier_id,
      details,
      total_amount: totalAmount,
      import_date: import_date || new Date()
    });

    await receipt.save();
    const populatedReceipt = await receipt.populate('supplier_id');

    res.status(201).json({ success: true, data: populatedReceipt });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getImportReceipt = async (req, res) => {
  try {
    const { id } = req.params;
    const receipt = await ImportReceipt.findById(id)
      .populate('supplier_id')
      .populate('details.item_id');
    
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
    const receipts = await ImportReceipt.find()
      .populate('supplier_id')
      .sort({ import_date: -1 });
    
    res.json({ success: true, data: receipts });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== EXPORT RECEIPT CONTROLLER ====================

exports.createExportReceipt = async (req, res) => {
  try {
    const distributor_id = getField(req.body, 'distributor_id', 'distributorId');
    const items = req.body.items || [];
    const export_date = getField(req.body, 'export_date', 'exportDate') || getField(req.body, 'exportDate', 'export_date');

    if (!distributor_id || !items || items.length === 0) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }

    // Verify distributor exists
    const distributor = await Distributor.findById(distributor_id);
    if (!distributor) {
      return res.status(404).json({ success: false, error: 'Distributor not found' });
    }

    // Validate and update inventory
    let details = [];
    let totalAmount = 0;

    for (const item of items) {
      const itemId = getField(item, 'item_id', 'itemId');
      const quantity = getField(item, 'quantity', 'quantity');
      const unit_price = getField(item, 'unit_price', 'unitPrice');

      const itemDoc = await Item.findById(itemId);
      if (!itemDoc) {
        return res.status(404).json({ success: false, error: `Item ${itemId} not found` });
      }

      if (itemDoc.current_quantity < quantity) {
        return res.status(400).json({ 
          success: false, 
          error: `Insufficient quantity for item: ${itemDoc.name}` 
        });
      }

      const itemTotal = quantity * unit_price;
      details.push({
        item_id: itemId,
        quantity,
        unit_price,
        total_amount: itemTotal
      });
      totalAmount += itemTotal;

      // Update item quantity
      itemDoc.current_quantity -= quantity;
      await itemDoc.save();
    }

    const code = `EXP-${Date.now()}`;
    const receipt = new ExportReceipt({
      code,
      distributor_id,
      details,
      total_amount: totalAmount,
      export_date: export_date || new Date()
    });

    await receipt.save();
    const populatedReceipt = await receipt.populate('distributor_id');

    res.status(201).json({ success: true, data: populatedReceipt });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getExportReceipt = async (req, res) => {
  try {
    const { id } = req.params;
    const receipt = await ExportReceipt.findById(id)
      .populate('distributor_id')
      .populate('details.item_id');
    
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
    const receipts = await ExportReceipt.find()
      .populate('distributor_id')
      .sort({ export_date: -1 });
    
    res.json({ success: true, data: receipts });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

// ==================== DISTRIBUTOR CONTROLLER ====================

exports.searchDistributors = async (req, res) => {
  try {
    const searchTerm = req.query.searchTerm ?? req.query.name ?? '';
    const distributors = await Distributor.find({
      $or: [
        { brand_name: { $regex: searchTerm, $options: 'i' } },
        { code: { $regex: searchTerm, $options: 'i' } }
      ]
    });
    res.json({ success: true, data: distributors });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

exports.getDistributor = async (req, res) => {
  try {
    const { id } = req.params;
    const distributor = await Distributor.findById(id);
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
    const code = getField(req.body, 'code', 'code');
    const brand_name = getField(req.body, 'brand_name', 'brandName');
    const address = getField(req.body, 'address', 'address');
    const phone = getField(req.body, 'phone', 'phoneNumber');
    if (!code || !brand_name || !address || !phone) {
      return res.status(400).json({ success: false, error: 'Missing required fields' });
    }

    const existingDistributor = await Distributor.findOne({ code });
    if (existingDistributor) {
      return res.status(400).json({ success: false, error: 'Distributor code already exists' });
    }

    const distributor = new Distributor({ code, brand_name, address, phone });
    await distributor.save();
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

    const stats = await ExportReceipt.aggregate([
      {
        $match: {
          export_date: {
            $gte: new Date(startDate),
            $lte: new Date(endDate)
          }
        }
      },
      { $unwind: '$details' },
      {
        $group: {
          _id: '$details.item_id',
          total_sold: { $sum: '$details.quantity' },
          total_revenue: { $sum: '$details.total_amount' }
        }
      },
      { $sort: { total_revenue: -1 } }
    ]);

    // Fetch item details
    const itemIds = stats.map(s => s._id);
    const items = await Item.find({ _id: { $in: itemIds } });
    
    const result = stats.map(stat => {
      const item = items.find(i => i._id.toString() === stat._id.toString());
      return {
        id: stat._id,
        code: item?.code,
        name: item?.name,
        total_sold: stat.total_sold,
        total_revenue: stat.total_revenue
      };
    });

    res.json({ success: true, data: result });
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

    const stats = await ExportReceipt.find({
      export_date: {
        $gte: new Date(startDate),
        $lte: new Date(endDate)
      },
      'details.item_id': itemId
    }).populate('distributor_id');

    const result = [];
    stats.forEach(receipt => {
      receipt.details.forEach(detail => {
        if (detail.item_id.toString() === itemId) {
          result.push({
            receipt_id: receipt._id,
            receipt_code: receipt.code,
            export_date: receipt.export_date,
            distributor_code: receipt.distributor_id.code,
            distributor_name: receipt.distributor_id.brand_name,
            quantity: detail.quantity,
            unit_price: detail.unit_price,
            total_amount: detail.total_amount
          });
        }
      });
    });

    res.json({ success: true, data: result });
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

    const stats = await ExportReceipt.aggregate([
      {
        $match: {
          export_date: {
            $gte: new Date(startDate),
            $lte: new Date(endDate)
          }
        }
      },
      {
        $group: {
          _id: '$distributor_id',
          total_revenue: { $sum: '$total_amount' }
        }
      },
      { $sort: { total_revenue: -1 } }
    ]);

    // Fetch distributor details
    const distributorIds = stats.map(s => s._id);
    const distributors = await Distributor.find({ _id: { $in: distributorIds } });
    
    const result = stats.map(stat => {
      const distributor = distributors.find(d => d._id.toString() === stat._id.toString());
      return {
        id: stat._id,
        code: distributor?.code,
        brand_name: distributor?.brand_name,
        total_revenue: stat.total_revenue
      };
    });

    res.json({ success: true, data: result });
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

    const receipts = await ExportReceipt.find({
      distributor_id: distributorId,
      export_date: {
        $gte: new Date(startDate),
        $lte: new Date(endDate)
      }
    });

    const result = receipts.map(receipt => ({
      receipt_id: receipt._id,
      receipt_code: receipt.code,
      export_date: receipt.export_date,
      total_items: receipt.details.length,
      total_amount: receipt.total_amount
    }));

    res.json({ success: true, data: result });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};
