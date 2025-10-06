require('dotenv').config();
const express = require('express');
const session = require('express-session');
const passport = require('passport');
const cors = require('cors');
const bodyParser = require('body-parser');

// Import routes
const authRoutes = require('./src/routes/auth');
const syncRoutes = require('./src/routes/sync');

// Import passport configuration
require('./src/auth/google');
require('./src/auth/microsoft');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors({
  origin: process.env.CORS_ORIGIN || '*',
  credentials: true
}));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Session configuration
app.use(session({
  secret: process.env.SESSION_SECRET || 'your-secret-key-change-in-production',
  resave: false,
  saveUninitialized: false,
  cookie: {
    secure: process.env.NODE_ENV === 'production',
    maxAge: 24 * 60 * 60 * 1000 // 24 hours
  }
}));

// Initialize Passport
app.use(passport.initialize());
app.use(passport.session());

// Routes
app.get('/', (req, res) => {
  res.json({
    message: 'Cloud Sync Application API',
    version: '1.0.0',
    endpoints: {
      authentication: {
        google: '/auth/google',
        microsoft: '/auth/microsoft',
        status: '/auth/status',
        logout: '/auth/logout'
      },
      sync: {
        upload: 'POST /api/sync/upload',
        download: 'GET /api/sync/download',
        list: 'GET /api/sync/list',
        delete: 'DELETE /api/sync/delete/:fileId'
      }
    }
  });
});

app.use('/auth', authRoutes);
app.use('/api/sync', syncRoutes);

// Error handling middleware
app.use((err, req, res, next) => {
  console.error('Error:', err);
  res.status(err.status || 500).json({
    error: {
      message: err.message || 'Internal Server Error',
      status: err.status || 500
    }
  });
});

// 404 handler
app.use((req, res) => {
  res.status(404).json({
    error: {
      message: 'Route not found',
      status: 404
    }
  });
});

// Start server
app.listen(PORT, () => {
  console.log(`\n🚀 Cloud Sync Application running on port ${PORT}`);
  console.log(`📝 Environment: ${process.env.NODE_ENV || 'development'}`);
  console.log(`\n📚 API Documentation:`);
  console.log(`   Main: http://localhost:${PORT}/`);
  console.log(`   Google Auth: http://localhost:${PORT}/auth/google`);
  console.log(`   Microsoft Auth: http://localhost:${PORT}/auth/microsoft`);
  console.log(`\n✨ Ready to accept requests!\n`);
});

module.exports = app;
