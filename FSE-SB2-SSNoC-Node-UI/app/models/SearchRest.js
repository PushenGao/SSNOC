var request = require('request');
var rest_api = require('../../config/rest_api');


function Search(){
  this.local = {};
}


Search.nameQuery = function (query, callback) {
  var options = {
    url : rest_api.search + 'name/' + query,
    body : {},
    json: true
  };
  request.get(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      return;
    }
    return callback(res.body);
  });
};

Search.statusQuery = function (query, callback) {
  var options = {
    url : rest_api.search + 'status/' + query,
    body : {},
    json: true
  };
  request.get(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      return;
    }
    return callback(res.body);
  });
};

Search.announcementQuery = function (query, callback) {
  var options = {
    url : rest_api.search + 'announcement/' + query,
    body : {},
    json: true
  };
  request.get(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      return;
    }
    return callback(res.body);
  });
};

Search.wallQuery = function (query, callback) {
  var options = {
    url : rest_api.search + 'wall/' + query,
    body : {},
    json: true
  };
  request.get(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      return;
    }
    return callback(res.body);
  });
};

Search.privateQuery = function (username, query, callback) {
  var options = {
    url : rest_api.search + 'chat/' + username +'/' + query,
    body : {},
    json: true
  };
  request.get(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      return;
    }
    return callback(res.body);
  });
};


    
module.exports = Search;
