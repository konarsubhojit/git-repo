const express = require('express');
const passport = require('passport');
const router = express.Router();

// Google authentication routes
router.get('/google', 
  passport.authenticate('google', { 
    scope: [
      'https://www.googleapis.com/auth/userinfo.profile',
      'https://www.googleapis.com/auth/userinfo.email',
      'https://www.googleapis.com/auth/drive.file'
    ]
  })
);

router.get('/google/callback',
  passport.authenticate('google', { failureRedirect: '/auth/failure' }),
  (req, res) => {
    // Successful authentication
    res.json({
      success: true,
      message: 'Successfully authenticated with Google',
      user: {
        id: req.user.id,
        provider: req.user.provider,
        displayName: req.user.displayName,
        email: req.user.email
      }
    });
  }
);

// Microsoft authentication routes
router.get('/microsoft',
  passport.authenticate('microsoft', {
    scope: ['user.read', 'files.readwrite']
  })
);

router.get('/microsoft/callback',
  passport.authenticate('microsoft', { failureRedirect: '/auth/failure' }),
  (req, res) => {
    // Successful authentication
    res.json({
      success: true,
      message: 'Successfully authenticated with Microsoft',
      user: {
        id: req.user.id,
        provider: req.user.provider,
        displayName: req.user.displayName,
        email: req.user.email
      }
    });
  }
);

// Authentication status
router.get('/status', (req, res) => {
  if (req.isAuthenticated()) {
    res.json({
      authenticated: true,
      user: {
        id: req.user.id,
        provider: req.user.provider,
        displayName: req.user.displayName,
        email: req.user.email
      }
    });
  } else {
    res.json({
      authenticated: false
    });
  }
});

// Logout
router.get('/logout', (req, res) => {
  req.logout((err) => {
    if (err) {
      return res.status(500).json({
        error: {
          message: 'Error during logout',
          status: 500
        }
      });
    }
    res.json({
      success: true,
      message: 'Successfully logged out'
    });
  });
});

// Authentication failure
router.get('/failure', (req, res) => {
  res.status(401).json({
    error: {
      message: 'Authentication failed',
      status: 401
    }
  });
});

module.exports = router;
