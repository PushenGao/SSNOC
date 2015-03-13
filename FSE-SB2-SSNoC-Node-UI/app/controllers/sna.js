var Sna = require('../models/SnaRest');

module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {
    getSna : function(req, res) {
      res.render('sna');
    },
    postSna : function(req, res) {
      Sna.analyzeRange(req.body.start, req.body.end, function(err, data){
        if (!err) {
          res.send(data);
        }
      });
    }
  };
}
