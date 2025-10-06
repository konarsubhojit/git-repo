const passport = require('passport');
const MicrosoftStrategy = require('passport-microsoft').Strategy;
const config = require('../config/config');

// Configure Microsoft OAuth Strategy
passport.use(new MicrosoftStrategy({
    clientID: config.microsoft.clientID,
    clientSecret: config.microsoft.clientSecret,
    callbackURL: config.microsoft.redirectURI,
    scope: config.microsoft.scope
  },
  async (accessToken, refreshToken, profile, done) => {
    try {
      // Create user object with profile and tokens
      const user = {
        id: profile.id,
        provider: 'microsoft',
        displayName: profile.displayName,
        email: profile.emails && profile.emails[0] ? profile.emails[0].value : null,
        accessToken: accessToken,
        refreshToken: refreshToken
      };
      
      return done(null, user);
    } catch (error) {
      return done(error, null);
    }
  }
));

module.exports = passport;
