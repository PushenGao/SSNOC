var Call = require('../models/CallRest');

module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {

  storeCall : function(req, res) {
        var user_name = req.session.passport.user.user_name;
        var target_user = req.param("user");  
        console.log("storing for" + target_user);
        Call.storeCall(user_name, target_user, function(done, data) {
          if (!done) {
            res.json(500, data);
          }
          res.json(200, data)
        });
  },

  getCalls : function(req, res) {
    var user_name = req.session.passport.user.user_name;
    Call.getLog(user_name, function(done, data) {
      console.log(data);
      if (!done) {
        res.json(500, data);
      }
      res.json(200, data)
    });
  }

 };
};
