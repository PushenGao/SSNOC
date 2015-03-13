var request = require('request');
var rest_api = require('../../config/rest_api');

function Sna() {
  this.local = {};
}

Sna.analyzeRange = function(start, end, callback) {
  var options = {
    url : rest_api.sna_range + start + '/' + end,
    body : {},
    json: true
  };
  request.get(options, function(err, res, body) {
    if (err) {
      callback(err, null);
      return;
    }
    if (res.statusCode !== 200) {
      err = new Error("Status code was " + res.statusCode.toString());
      callback(err, null);
    }
    callback(null, body);
  });
};

module.exports = Sna;
