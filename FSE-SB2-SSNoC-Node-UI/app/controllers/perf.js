var Perf = require('../models/PerfRest')
var rest_api = require('../../config/rest_api');



module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {
    getPerf : function(req, res) {
      res.render('perf', {recordingPerf: Perf.recording});
    },

    postPerf : function(req, res) {
      var duration = Object.keys(req.body)[0];  
      var user_name = req.session.passport.user.user_name;

      Perf.posts = 0;
      Perf.gets = 0;

      Perf.recording = true;
      Perf.timeout = setTimeout(function() {Perf.recording = false;}, duration * 1000);
      Perf.setup(function(){
          Perf.doTest(user_name, function() {
            Perf.tearDown(function(){});
          });
        });
    },

    getPerfResults : function(req, res) {
      res.render('perf_view', {gets: Perf.gets, posts: Perf.posts});
    },
    
    stopPerf : function(req, res) {
      Perf.recording = false;
      clearTimeout(Perf.timeout);
      res.json('200');
    },


  };

};
