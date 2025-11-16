const mongoose = require('mongoose');

const importReceiptSchema = new mongoose.Schema({
  code: { type: String, required: true, unique: true },
  supplier_id: { type: mongoose.Schema.Types.ObjectId, ref: 'Supplier', required: true },
  details: [{
    item_id: { type: mongoose.Schema.Types.ObjectId, ref: 'Item', required: true },
    quantity: { type: Number, required: true },
    unit_price: { type: Number, required: true },
    total_amount: { type: Number, required: true }
  }],
  total_amount: { type: Number, default: 0 },
  import_date: { type: Date, default: Date.now },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('ImportReceipt', importReceiptSchema);
