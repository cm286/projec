require('dotenv').config();
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const connectDB = require('./src/database/connection');
const mongoRoutes = require('./src/routes/mongoRoutes');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Development Content Security Policy helper
// Allows dev tools and local API connections while developing.
app.use((req, res, next) => {
  if ((process.env.NODE_ENV || 'development') === 'development') {
    res.setHeader('Content-Security-Policy',
      "default-src 'self'; " +
      "connect-src 'self' http://localhost:5000 http://127.0.0.1:5000; " +
      "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
      "style-src 'self' 'unsafe-inline'; " +
      "img-src 'self' data:;"
    );
  }
  next();
});

// Serve small dev helper for Chrome DevTools and similar requests
// This will respond to GET /.well-known/appspecific/com.chrome.devtools.json
app.use('/.well-known/appspecific', express.static(path.join(__dirname, 'public', '.well-known', 'appspecific')));

// Connect to MongoDB
connectDB().then(() => {
  console.log('✓ MongoDB connected and ready to use');
}).catch((err) => {
  console.error('✗ Failed to connect MongoDB:', err.message);
  // In production, fail fast. In development, continue so static endpoints
  // (like /.well-known) can still be served for debugging/devtools.
  if ((process.env.NODE_ENV || 'development') === 'production') {
    process.exit(1);
  } else {
    console.warn('Continuing without database connection (development mode).');
  }
});

// Routes
app.use('/api', mongoRoutes);

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'OK', database: 'MongoDB' });
});

// Error handling middleware
app.use((err, req, res, next) => {
  console.error('Error:', err);
  res.status(500).json({ success: false, error: 'Internal server error' });
});

// Start server
app.listen(PORT, () => {
  console.log(`\n🚀 Server is running on http://localhost:${PORT}`);
  console.log(`📦 Environment: ${process.env.NODE_ENV || 'development'}`);
  console.log(`💾 Database: MongoDB Atlas\n`);
});

process.on('SIGINT', () => {
  console.log('\n\n📌 Shutting down gracefully...');
  process.exit(0);
});
