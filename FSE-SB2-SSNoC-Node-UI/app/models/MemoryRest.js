var request = require('request');
var rest_api = require('../../config/rest_api');


function Memory(){
  this.local = {};
}

Memory.recording = false;

Memory.startMeasuring = function(callback) {
  var options = {
    url : rest_api.memory + 'start',
    body : {},
    json: true
  };
  request.post(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      callback(false, res);
      return;
    }
    callback(true, res);
  });

  Memory.recording = true;
};


Memory.stopMeasuring = function(callback) {
  var options = {
    url : rest_api.memory + 'stop',
    body : {},
    json: true
  };
  request.post(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      callback(false, res);
      return;
    }
    callback(true, res);
  });

  Memory.recording = false;
};


Memory.getMeasurements = function (callback) {
  var options = {
    url : rest_api.memory,
    body : {},
    json: true
  };
  request.get(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      callback(false, res);
      return;
    }
    callback(true, res);
  });
};

    
module.exports = Memory;
