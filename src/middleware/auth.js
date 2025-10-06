/**
 * Middleware to ensure user is authenticated
 */
function ensureAuthenticated(req, res, next) {
  if (req.isAuthenticated()) {
    return next();
  }
  
  res.status(401).json({
    error: {
      message: 'Authentication required',
      status: 401
    }
  });
}

/**
 * Middleware to ensure user is authenticated with Google
 */
function ensureGoogleAuth(req, res, next) {
  if (req.isAuthenticated() && req.user.provider === 'google') {
    return next();
  }
  
  res.status(401).json({
    error: {
      message: 'Google authentication required',
      status: 401
    }
  });
}

/**
 * Middleware to ensure user is authenticated with Microsoft
 */
function ensureMicrosoftAuth(req, res, next) {
  if (req.isAuthenticated() && req.user.provider === 'microsoft') {
    return next();
  }
  
  res.status(401).json({
    error: {
      message: 'Microsoft authentication required',
      status: 401
    }
  });
}

module.exports = {
  ensureAuthenticated,
  ensureGoogleAuth,
  ensureMicrosoftAuth
};
