const sqlite3 = require('sqlite3').verbose();
const path = require('path');

const dbPath = path.join(__dirname, '../../store-management.db');
const db = new sqlite3.Database(dbPath, (err) => {
  if (err) {
    console.error('Error opening database:', err);
  }
});

// Initialize tables
const initializeDatabase = () => {
  db.serialize(() => {
    // Suppliers table
    db.run(`
      CREATE TABLE IF NOT EXISTS suppliers (
        id TEXT PRIMARY KEY,
        code TEXT UNIQUE NOT NULL,
        name TEXT NOT NULL,
        address TEXT NOT NULL,
        phone TEXT NOT NULL,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
      )
    `);

    // Items table
    db.run(`
      CREATE TABLE IF NOT EXISTS items (
        id TEXT PRIMARY KEY,
        code TEXT UNIQUE NOT NULL,
        name TEXT NOT NULL,
        description TEXT,
        current_quantity INTEGER DEFAULT 0,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
      )
    `);

    // Import Receipts table
    db.run(`
      CREATE TABLE IF NOT EXISTS import_receipts (
        id TEXT PRIMARY KEY,
        code TEXT UNIQUE NOT NULL,
        supplier_id TEXT NOT NULL,
        total_amount REAL NOT NULL,
        import_date DATETIME DEFAULT CURRENT_TIMESTAMP,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY(supplier_id) REFERENCES suppliers(id)
      )
    `);

    // Import Receipt Details table
    db.run(`
      CREATE TABLE IF NOT EXISTS import_receipt_details (
        id TEXT PRIMARY KEY,
        import_receipt_id TEXT NOT NULL,
        item_id TEXT NOT NULL,
        quantity INTEGER NOT NULL,
        unit_price REAL NOT NULL,
        total_amount REAL NOT NULL,
        FOREIGN KEY(import_receipt_id) REFERENCES import_receipts(id),
        FOREIGN KEY(item_id) REFERENCES items(id)
      )
    `);

    // Distributors table
    db.run(`
      CREATE TABLE IF NOT EXISTS distributors (
        id TEXT PRIMARY KEY,
        code TEXT UNIQUE NOT NULL,
        brand_name TEXT NOT NULL,
        address TEXT NOT NULL,
        phone TEXT NOT NULL,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
      )
    `);

    // Export Receipts table
    db.run(`
      CREATE TABLE IF NOT EXISTS export_receipts (
        id TEXT PRIMARY KEY,
        code TEXT UNIQUE NOT NULL,
        distributor_id TEXT NOT NULL,
        total_amount REAL NOT NULL,
        export_date DATETIME DEFAULT CURRENT_TIMESTAMP,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY(distributor_id) REFERENCES distributors(id)
      )
    `);

    // Export Receipt Details table
    db.run(`
      CREATE TABLE IF NOT EXISTS export_receipt_details (
        id TEXT PRIMARY KEY,
        export_receipt_id TEXT NOT NULL,
        item_id TEXT NOT NULL,
        quantity INTEGER NOT NULL,
        unit_price REAL NOT NULL,
        total_amount REAL NOT NULL,
        FOREIGN KEY(export_receipt_id) REFERENCES export_receipts(id),
        FOREIGN KEY(item_id) REFERENCES items(id)
      )
    `);
  });
};

module.exports = { db, initializeDatabase };
