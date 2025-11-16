const { db } = require('../database/db');
const { v4: uuidv4 } = require('uuid');

// ==================== SUPPLIER OPERATIONS ====================

const searchSuppliers = (searchTerm) => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM suppliers WHERE name LIKE ? ORDER BY name`;
    db.all(query, [`%${searchTerm}%`], (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

const getSupplierById = (id) => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM suppliers WHERE id = ?`;
    db.get(query, [id], (err, row) => {
      if (err) reject(err);
      else resolve(row);
    });
  });
};

const createSupplier = (code, name, address, phone) => {
  return new Promise((resolve, reject) => {
    const id = uuidv4();
    const query = `INSERT INTO suppliers (id, code, name, address, phone) VALUES (?, ?, ?, ?, ?)`;
    db.run(query, [id, code, name, address, phone], function(err) {
      if (err) reject(err);
      else resolve({ id, code, name, address, phone });
    });
  });
};

// ==================== ITEM OPERATIONS ====================

const searchItems = (searchTerm) => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM items WHERE name LIKE ? ORDER BY name`;
    db.all(query, [`%${searchTerm}%`], (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

const getItemById = (id) => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM items WHERE id = ?`;
    db.get(query, [id], (err, row) => {
      if (err) reject(err);
      else resolve(row);
    });
  });
};

const getAllItems = () => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM items ORDER BY name`;
    db.all(query, (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

const createItem = (code, name, description = '') => {
  return new Promise((resolve, reject) => {
    const id = uuidv4();
    const query = `INSERT INTO items (id, code, name, description) VALUES (?, ?, ?, ?)`;
    db.run(query, [id, code, name, description], function(err) {
      if (err) reject(err);
      else resolve({ id, code, name, description, current_quantity: 0 });
    });
  });
};

const updateItemQuantity = (item_id, quantity) => {
  return new Promise((resolve, reject) => {
    const query = `UPDATE items SET current_quantity = current_quantity + ? WHERE id = ?`;
    db.run(query, [quantity, item_id], function(err) {
      if (err) reject(err);
      else resolve();
    });
  });
};

// ==================== IMPORT RECEIPT OPERATIONS ====================

const createImportReceipt = (supplier_id, import_date = new Date()) => {
  return new Promise((resolve, reject) => {
    const id = uuidv4();
    const code = `IMP-${Date.now()}`;
    const query = `INSERT INTO import_receipts (id, code, supplier_id, total_amount, import_date) VALUES (?, ?, ?, ?, ?)`;
    db.run(query, [id, code, supplier_id, 0, import_date], function(err) {
      if (err) reject(err);
      else resolve({ id, code });
    });
  });
};

const addImportReceiptDetail = (import_receipt_id, item_id, quantity, unit_price) => {
  return new Promise((resolve, reject) => {
    const id = uuidv4();
    const total_amount = quantity * unit_price;
    const query = `INSERT INTO import_receipt_details (id, import_receipt_id, item_id, quantity, unit_price, total_amount) VALUES (?, ?, ?, ?, ?, ?)`;
    db.run(query, [id, import_receipt_id, item_id, quantity, unit_price, total_amount], function(err) {
      if (err) reject(err);
      else resolve();
    });
  });
};

const finalizeImportReceipt = (import_receipt_id) => {
  return new Promise((resolve, reject) => {
    const sumQuery = `SELECT SUM(total_amount) as total FROM import_receipt_details WHERE import_receipt_id = ?`;
    db.get(sumQuery, [import_receipt_id], (err, result) => {
      if (err) {
        reject(err);
        return;
      }

      const total = result.total || 0;
      const updateQuery = `UPDATE import_receipts SET total_amount = ? WHERE id = ?`;
      db.run(updateQuery, [total, import_receipt_id], function(err) {
        if (err) reject(err);
        else resolve();
      });
    });
  });
};

const getImportReceiptById = (id) => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM import_receipts WHERE id = ?`;
    db.get(query, [id], (err, receipt) => {
      if (err) {
        reject(err);
        return;
      }

      const detailQuery = `
        SELECT ird.*, i.name as item_name, i.code as item_code
        FROM import_receipt_details ird
        JOIN items i ON ird.item_id = i.id
        WHERE ird.import_receipt_id = ?
      `;
      db.all(detailQuery, [id], (err, details) => {
        if (err) reject(err);
        else resolve({ ...receipt, details: details || [] });
      });
    });
  });
};

const getAllImportReceipts = () => {
  return new Promise((resolve, reject) => {
    const query = `
      SELECT ir.*, s.name as supplier_name, s.code as supplier_code
      FROM import_receipts ir
      JOIN suppliers s ON ir.supplier_id = s.id
      ORDER BY ir.import_date DESC
    `;
    db.all(query, (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

// ==================== EXPORT RECEIPT OPERATIONS ====================

const createExportReceipt = (distributor_id, export_date = new Date()) => {
  return new Promise((resolve, reject) => {
    const id = uuidv4();
    const code = `EXP-${Date.now()}`;
    const query = `INSERT INTO export_receipts (id, code, distributor_id, total_amount, export_date) VALUES (?, ?, ?, ?, ?)`;
    db.run(query, [id, code, distributor_id, 0, export_date], function(err) {
      if (err) reject(err);
      else resolve({ id, code });
    });
  });
};

const addExportReceiptDetail = (export_receipt_id, item_id, quantity, unit_price) => {
  return new Promise((resolve, reject) => {
    const id = uuidv4();
    const total_amount = quantity * unit_price;
    const query = `INSERT INTO export_receipt_details (id, export_receipt_id, item_id, quantity, unit_price, total_amount) VALUES (?, ?, ?, ?, ?, ?)`;
    db.run(query, [id, export_receipt_id, item_id, quantity, unit_price, total_amount], function(err) {
      if (err) reject(err);
      else resolve();
    });
  });
};

const finalizeExportReceipt = (export_receipt_id) => {
  return new Promise((resolve, reject) => {
    const sumQuery = `SELECT SUM(total_amount) as total FROM export_receipt_details WHERE export_receipt_id = ?`;
    db.get(sumQuery, [export_receipt_id], (err, result) => {
      if (err) {
        reject(err);
        return;
      }

      const total = result.total || 0;
      const updateQuery = `UPDATE export_receipts SET total_amount = ? WHERE id = ?`;
      db.run(updateQuery, [total, export_receipt_id], function(err) {
        if (err) reject(err);
        else resolve();
      });
    });
  });
};

const getExportReceiptById = (id) => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM export_receipts WHERE id = ?`;
    db.get(query, [id], (err, receipt) => {
      if (err) {
        reject(err);
        return;
      }

      const detailQuery = `
        SELECT erd.*, i.name as item_name, i.code as item_code
        FROM export_receipt_details erd
        JOIN items i ON erd.item_id = i.id
        WHERE erd.export_receipt_id = ?
      `;
      db.all(detailQuery, [id], (err, details) => {
        if (err) reject(err);
        else resolve({ ...receipt, details: details || [] });
      });
    });
  });
};

const getAllExportReceipts = () => {
  return new Promise((resolve, reject) => {
    const query = `
      SELECT er.*, d.brand_name as distributor_name, d.code as distributor_code
      FROM export_receipts er
      JOIN distributors d ON er.distributor_id = d.id
      ORDER BY er.export_date DESC
    `;
    db.all(query, (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

// ==================== DISTRIBUTOR OPERATIONS ====================

const searchDistributors = (searchTerm) => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM distributors WHERE brand_name LIKE ? ORDER BY brand_name`;
    db.all(query, [`%${searchTerm}%`], (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

const getDistributorById = (id) => {
  return new Promise((resolve, reject) => {
    const query = `SELECT * FROM distributors WHERE id = ?`;
    db.get(query, [id], (err, row) => {
      if (err) reject(err);
      else resolve(row);
    });
  });
};

const createDistributor = (code, brand_name, address, phone) => {
  return new Promise((resolve, reject) => {
    const id = uuidv4();
    const query = `INSERT INTO distributors (id, code, brand_name, address, phone) VALUES (?, ?, ?, ?, ?)`;
    db.run(query, [id, code, brand_name, address, phone], function(err) {
      if (err) reject(err);
      else resolve({ id, code, brand_name, address, phone });
    });
  });
};

// ==================== STATISTICS OPERATIONS ====================

const getItemStatistics = (startDate, endDate) => {
  return new Promise((resolve, reject) => {
    const query = `
      SELECT 
        i.id,
        i.code,
        i.name,
        SUM(erd.quantity) as total_sold,
        SUM(erd.total_amount) as total_revenue
      FROM items i
      LEFT JOIN export_receipt_details erd ON i.id = erd.item_id
      LEFT JOIN export_receipts er ON erd.export_receipt_id = er.id
      WHERE er.export_date BETWEEN ? AND ? OR er.export_date IS NULL
      GROUP BY i.id
      ORDER BY total_revenue DESC NULLS LAST
    `;
    db.all(query, [startDate, endDate], (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

const getItemDetailStatistics = (itemId, startDate, endDate) => {
  return new Promise((resolve, reject) => {
    const query = `
      SELECT 
        er.id as receipt_id,
        er.code as receipt_code,
        er.export_date,
        d.code as distributor_code,
        d.brand_name as distributor_name,
        erd.quantity,
        erd.unit_price,
        erd.total_amount
      FROM export_receipt_details erd
      JOIN export_receipts er ON erd.export_receipt_id = er.id
      JOIN distributors d ON er.distributor_id = d.id
      WHERE erd.item_id = ? AND er.export_date BETWEEN ? AND ?
      ORDER BY er.export_date DESC
    `;
    db.all(query, [itemId, startDate, endDate], (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

const getDistributorStatistics = (startDate, endDate) => {
  return new Promise((resolve, reject) => {
    const query = `
      SELECT 
        d.id,
        d.code,
        d.brand_name,
        SUM(er.total_amount) as total_revenue
      FROM distributors d
      LEFT JOIN export_receipts er ON d.id = er.distributor_id
      WHERE er.export_date BETWEEN ? AND ? OR er.export_date IS NULL
      GROUP BY d.id
      ORDER BY total_revenue DESC NULLS LAST
    `;
    db.all(query, [startDate, endDate], (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

const getDistributorDetailStatistics = (distributorId, startDate, endDate) => {
  return new Promise((resolve, reject) => {
    const query = `
      SELECT 
        er.id as receipt_id,
        er.code as receipt_code,
        er.export_date,
        COUNT(DISTINCT erd.item_id) as total_items,
        er.total_amount
      FROM export_receipts er
      LEFT JOIN export_receipt_details erd ON er.id = erd.export_receipt_id
      WHERE er.distributor_id = ? AND er.export_date BETWEEN ? AND ?
      GROUP BY er.id
      ORDER BY er.export_date DESC
    `;
    db.all(query, [distributorId, startDate, endDate], (err, rows) => {
      if (err) reject(err);
      else resolve(rows || []);
    });
  });
};

module.exports = {
  // Supplier
  searchSuppliers,
  getSupplierById,
  createSupplier,
  // Item
  searchItems,
  getItemById,
  getAllItems,
  createItem,
  updateItemQuantity,
  // Import Receipt
  createImportReceipt,
  addImportReceiptDetail,
  finalizeImportReceipt,
  getImportReceiptById,
  getAllImportReceipts,
  // Export Receipt
  createExportReceipt,
  addExportReceiptDetail,
  finalizeExportReceipt,
  getExportReceiptById,
  getAllExportReceipts,
  // Distributor
  searchDistributors,
  getDistributorById,
  createDistributor,
  // Statistics
  getItemStatistics,
  getItemDetailStatistics,
  getDistributorStatistics,
  getDistributorDetailStatistics
};
