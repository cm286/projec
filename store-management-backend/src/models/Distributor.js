const mongoose = require('mongoose');

const distributorSchema = new mongoose.Schema({
  code: { type: String, required: true, unique: true },
  brand_name: { type: String, required: true },
  address: { type: String, required: true },
  phone: { type: String, required: true },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Distributor', distributorSchema);
