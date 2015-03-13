var Memory = require('../models/MemoryRest');

function format_date(epoch) {
  d = new Date(0); 
  d.setUTCSeconds(epoch)
  var m_names = new Array("Jan", "Feb", "Mar", 
      "Apr", "May", "Jun", "Jul", "Aug", "Sep", 
      "Oct", "Nov", "Dec");

  var date = d.getDate();
  var month = d.getMonth();
  var min = d.getMinutes();
  min = (new Array(2 + 1 - min.toString().length)).join('0') + min;
  var hour = d.getHours();

  return date + " " + m_names[month] + " " + hour + ":" + min;
}


module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {

    getMeasureMemory : function(req, res) {
      res.render('memory', {recording: Memory.recording});
    },

    startMeasuring : function(req, res) {
      Memory.startMeasuring(function(done, data){
        if (!done) {
          res.json(500, data);
        }
        res.json(200, data)
      });
    },

    stopMeasuring : function(req, res) {
      Memory.stopMeasuring(function(done, data){
        if (!done) {
          res.json(500, data);
        }
        res.json(200, data)
      });
    },

    getMeasurements : function(req, res) {
      var meas_array = Memory.getMeasurements(function(done, data) {
        if (!done) {
          res.json(500, data.body);
        }
        results = data.body;
        for (i=0; i<results.length;i++) {
          results[i].createdAt = format_date(results[i].createdAt);
        }
        res.render('mem_view', {results: data.body})
      });
    },


  };
};
