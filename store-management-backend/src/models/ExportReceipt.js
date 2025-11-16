const mongoose = require('mongoose');

const exportReceiptSchema = new mongoose.Schema({
  code: { type: String, required: true, unique: true },
  distributor_id: { type: mongoose.Schema.Types.ObjectId, ref: 'Distributor', required: true },
  details: [{
    item_id: { type: mongoose.Schema.Types.ObjectId, ref: 'Item', required: true },
    quantity: { type: Number, required: true },
    unit_price: { type: Number, required: true },
    total_amount: { type: Number, required: true }
  }],
  total_amount: { type: Number, default: 0 },
  export_date: { type: Date, default: Date.now },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('ExportReceipt', exportReceiptSchema);
