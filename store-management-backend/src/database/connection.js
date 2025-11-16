const mongoose = require('mongoose');

const connectDB = async () => {
  try {
    const mongoUri = process.env.MONGODB_URI;
    
    if (!mongoUri) {
      throw new Error('MONGODB_URI is not defined in .env file');
    }

    await mongoose.connect(mongoUri, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    });

    console.log('✅ MongoDB connected successfully!');
    console.log(`📊 Connected to database: ${mongoose.connection.name}`);
    return mongoose.connection;
  } catch (error) {
    console.error('MongoDB connection error:', error.message);
    // Throw the error so callers (index.js) can decide how to handle it
    throw error;
  }
};

module.exports = connectDB;
