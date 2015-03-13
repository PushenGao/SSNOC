var request = require('request');
var rest_api = require('../../config/rest_api');

function Call(){
  this.local = {};
}

Call.storeCall = function(user_name, other_user, callback) {
  var options = {
      url : rest_api.call + user_name + '/' + other_user + '/'
    };
    request.post(options, function(err, res, body) {
      if (err || res.statusCode !== 200) {
        callback(false, res);
        return;
      }
      callback(true, res.body);
    });
};

Call.getLog = function(user_name, callback) {
  var options = {
      url : rest_api.call + user_name + '/' 
    };
    request.get(options, function(err, res, body) {
      if (err || res.statusCode !== 200) {
        callback(false, res);
        return;
      }
      callback(true, res.body);
    });
}

module.exports = Call;
