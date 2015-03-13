var User = require('../models/UserRest.js')

module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {
    getPeople: function(req, res) {
      var this_user = req.session.passport.user.user_name;
      User.getUser(this_user, function(err,user) {
        if (user !== null) {
          res.render("people", {userId: req.session.userId, title:"People", 
              user_name:this_user, statusCode: user.local.statusCode, 
              privilege: user.local.privilege});
        }
      });
    },

    getBase: function(req, res) {
      refreshAllUsers(participants, function() {
        var this_user = req.session.passport.user.user_name;
        User.getUser(this_user, function(err, user) {
          if (user !== null) {
            res.render("base", {privilege: user.local.privilege}); 
          }
        });
      });
    },

    getUsersPage: function(req, res) {
      res.render("users", {});
    },

    getUserProfile: function(req, res) {
      var profile_username = req.param("profile_username");
      res.render("user_profile", {profile_username: profile_username});
    },

    getUser : function(req, res) {
      var user_name = req.param("username");
      User.getUser(user_name, function(err, user) {
        if (user !== null) {
          res.json(200, {name:user.local.name, statusCode:user.local.statusCode, status:user.local.status, privilege:user.local.privilege});
        }
      });
    },

    getUsers : function(req, res) {
      User.getAllUsers(function(done, data) {
        if (!done) {
          res.json(500, data);
        }
        res.json(200, data);
      });
    }

  };
};
