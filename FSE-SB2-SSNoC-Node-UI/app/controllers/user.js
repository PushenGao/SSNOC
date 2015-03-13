var User = require('../models/UserRest');


module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {
    getLogin : function(req, res) {
      res.render("join", {message: req.flash('loginMessage')});
    },

    getLogout : function(req, res) {
      req.logout();
      res.redirect('/');
    },

    getSignup : function(req, res) {
      res.render('signup', {message: req.flash('signupMessage')});
    },

    getUser : function(req, res) {
      var user_name = req.session.passport.user.user_name;
      User.getUser(user_name, function(err, user) {
        if (user !== null) {
          res.json(200, {name:user.local.name, statusCode:user.local.statusCode});
        }
      });
    },

    updateUser : function(req, res) {
      old_user = req.body['old_user'];
      user = req.body['user'];
      password = req.body['password'];
      statusCode = req.body['status'];
      role = req.body['role'];
      activity = req.body['activity'];

      User.updateUser(old_user, user, password, statusCode, role, activity, function(err, user) {
        if (user !== null) {
          res.redirect('/ssnoc/');
        } 
      });
    },


    updateStatus : function(req, res) {
      var user_name = req.session.passport.user.user_name;
      var new_status = req.param("status");
      User.updateStatus(user_name, new_status, function(err, crumb) {
        if (crumb !== null) {
          res.json(200, {});
        }
      })
    },


    postSignup : function(req, res, next) {
      passport.authenticate('local-signup', function(err, user, info) {
        if (err)
          return next(err);
        if (!user)
          return res.redirect('/signup');
        req.logIn(user, function(err) {
          if (err)
            return next(err);
          participants.all.push({'userName' : user.local.name});
          return res.redirect('/welcome');
        });
      })(req, res, next);
    },


    getWelcome : function(req, res) {
      res.render('welcome', {title: "Hello " + req.session.passport.user.user_name + " !!"} );
    }
  };
};
