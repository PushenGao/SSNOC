var request = require('request');
var rest_api = require('../../config/rest_api');
var Message = require('../models/MessageRest')

function Perf(){

  this.local = {
    asd:asd
  };
}

Perf.recording = false;
Perf.posts = 0;
Perf.gets = 0;
Perf.timeout = null;

Perf.doTest = function(user_name, callback) {
  if (Perf.recording) {
    Message.postToWall("12345678901234567890", user_name, function(asd, asd){
      Perf.posts++;
      Message.getWallPosts(function(asd, asd){
        Perf.gets++;
        Perf.doTest(user_name, callback);
      });
    });
  } else {
    callback();
  }
}

Perf.setup = function(callback) {
  var options = {
    url : rest_api.perf_setup,
    body : {},
    json: true
  };
  request.post(options, function(err, res, body) {
    if (err) {
      callback(err);
    }
    if (res.statusCode !== 200) {
      err = new Error("statusCode was " + res.statusCode.toString());
      callback(err);
    }
    callback(null);
  });
  Perf.recording = true;
}

Perf.tearDown = function(callback) {
  var options = {
    url : rest_api.perf_teardown,
    body : {},
    json: true
  };
  request.post(options, function(err, res, body) {
    if (err) {
      callback(err);
    }
    if (res.statusCode !== 200) {
      err = new Error("statusCode was " + res.statusCode.toString());
      callback(err);
    }
    callback(null);
  });
  Perf.recording = false;
}

module.exports = Perf;
