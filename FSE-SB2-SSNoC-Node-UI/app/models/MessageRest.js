var request = require('request');
var rest_api = require('../../config/rest_api');

function Message(){

  this.local = {
    asd:asd
  };
}


Message.postToWall = function(message, user_name, callback) {
    var options = {
      url : rest_api.message + user_name,
      body : {content: message, postedAt: Date.now().toString()},
      json: true
    };
    request.post(options, function(err, res, body) {
      if (err || res.statusCode !== 200) {
        callback(false, res);
        return;
      }
      callback(true, res);
    });
  };


Message.postToPrivate = function(message, sender, receiver, callback) {
    var options = {
      url : rest_api.message + sender + '/' + receiver,
      body : {content: message, postedAt: Date.now().toString()},
      json: true
    };
    console.log(options);
    request.post(options, function(err, res, body) {
      if (err || res.statusCode !== 200) {
        callback(false, res);
        return;
      }
      callback(true, res);
    });
  };


Message.getWallPosts = function(callback) {
  var options = {
    url : rest_api.getWall,
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


Message.getPrivatePosts = function(user1, user2,callback) {
var options = {
      url : rest_api.private + user1 + '/' + user2 + '/visible',
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

Message.getAnnouncements = function(callback) {
  var options = {
    url : rest_api.announcements,
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

Message.postAnnouncement = function(message, user_name, callback) {
  var options = {
    url : rest_api.post_announcement,
    body : {content: message, author: user_name, postedAt: Date.now().toString()},
    json: true
  };
  request.post(options, function(err, res, body) {
    if (err || res.statusCode !== 200) {
      callback(false, res);
      return;
    }
    callback(true, res);
  });
};

module.exports = Message;
