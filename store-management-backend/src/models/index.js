const { v4: uuidv4 } = require('uuid');

class Supplier {
  constructor(code, name, address, phone) {
    this.id = uuidv4();
    this.code = code;
    this.name = name;
    this.address = address;
    this.phone = phone;
  }
}

class Item {
  constructor(code, name, description = '') {
    this.id = uuidv4();
    this.code = code;
    this.name = name;
    this.description = description;
    this.current_quantity = 0;
  }
}

class ImportReceipt {
  constructor(supplier_id, import_date = new Date()) {
    this.id = uuidv4();
    this.code = `IMP-${Date.now()}`;
    this.supplier_id = supplier_id;
    this.details = [];
    this.import_date = import_date;
  }

  addItem(item_id, quantity, unit_price) {
    this.details.push({
      item_id,
      quantity,
      unit_price,
      total_amount: quantity * unit_price
    });
  }

  getTotalAmount() {
    return this.details.reduce((sum, detail) => sum + detail.total_amount, 0);
  }
}

class ExportReceipt {
  constructor(distributor_id, export_date = new Date()) {
    this.id = uuidv4();
    this.code = `EXP-${Date.now()}`;
    this.distributor_id = distributor_id;
    this.details = [];
    this.export_date = export_date;
  }

  addItem(item_id, quantity, unit_price) {
    this.details.push({
      item_id,
      quantity,
      unit_price,
      total_amount: quantity * unit_price
    });
  }

  getTotalAmount() {
    return this.details.reduce((sum, detail) => sum + detail.total_amount, 0);
  }
}

class Distributor {
  constructor(code, brand_name, address, phone) {
    this.id = uuidv4();
    this.code = code;
    this.brand_name = brand_name;
    this.address = address;
    this.phone = phone;
  }
}

module.exports = {
  Supplier,
  Item,
  ImportReceipt,
  ExportReceipt,
  Distributor
};
