const express = require('express');
const router = express.Router();
const controller = require('../controllers/mongoController');

// ==================== SUPPLIER ROUTES ====================
router.get('/suppliers/search', controller.searchSuppliers);
router.get('/suppliers/:id', controller.getSupplier);
router.post('/suppliers', controller.createSupplier);

// ==================== ITEM ROUTES ====================
router.get('/items/search', controller.searchItems);
router.get('/items/all', controller.getAllItems);
router.get('/items/:id', controller.getItem);
router.post('/items', controller.createItem);

// ==================== IMPORT RECEIPT ROUTES ====================
router.post('/import-receipts', controller.createImportReceipt);
router.get('/import-receipts/:id', controller.getImportReceipt);
router.get('/import-receipts', controller.getAllImportReceipts);

// ==================== EXPORT RECEIPT ROUTES ====================
router.post('/export-receipts', controller.createExportReceipt);
router.get('/export-receipts/:id', controller.getExportReceipt);
router.get('/export-receipts', controller.getAllExportReceipts);

// ==================== DISTRIBUTOR ROUTES ====================
router.get('/distributors/search', controller.searchDistributors);
router.get('/distributors/:id', controller.getDistributor);
router.post('/distributors', controller.createDistributor);

// ==================== STATISTICS ROUTES ====================
router.get('/statistics/items', controller.getItemStatistics);
router.get('/statistics/items/:itemId/details', controller.getItemDetailStatistics);
router.get('/statistics/distributors', controller.getDistributorStatistics);
router.get('/statistics/distributors/:distributorId/details', controller.getDistributorDetailStatistics);

// Legacy / alternative endpoints (aliases) to support frontend variants
router.get('/statistics/distributors/:distributorId/exports', controller.getDistributorDetailStatistics);
router.get('/statistics/items/:itemId/imports', controller.getItemDetailStatistics);

module.exports = router;
