var bcrypt = require('bcrypt-nodejs');
var request = require('request');
var rest_api = require('../../config/rest_api');

function User(user_name, password, statusCode, status, privilege){
  this.local = {
    name : user_name,
    password : password,
    statusCode : statusCode,
    status : status,
    privilege : privilege
  };
}

User.generateHash = function(password) {
  return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
};

User.prototype.isValidPassword = function(password, callback) {
  request.post(rest_api.is_password_valid + this.local.name + '/authenticate', {json:true, body:{password:password}}, function(err, res, body) {
    if (err || res.statusCode !== 200){
      callback(false);
      return;
    }

    callback(true);
  });
};

User.getUser = function(user_name, callback) {
  request(rest_api.get_user + user_name, {json:true}, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode === 200) {
      var user = new User(body.userName, body.password, body.lastStatusCode, body.status, body.privilege);
      callback(null, user);
      return;
    }
    if (res.statusCode !== 200) {
      callback(null, null);
      return;
    }
  });
};

User.getAllUsers = function(callback) {
  request(rest_api.get_all_users, {json:true}, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode === 200) {
      var users = body.map(function(item, idx, arr){
        return new User(item.userName, item.password, item.lastStatusCode, item.status, item.privilege);
      });

      users.sort(function(a,b) {
        return a.userName > b.userName;
      });

      console.log("@@@@@ in User.getAllUser succeed users :" + JSON.stringify(users));
      callback(null, users);
      return;
    }
    if (res.statusCode !== 200) {
        callback(null, null);
      return;
    }
  });
};

User.getVisibleUsers = function(callback) {
  request(rest_api.get_visible_users, {json:true}, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode === 200) {
      var users = body.map(function(item, idx, arr){
        return new User(item.userName, item.password, item.lastStatusCode, item.status, item.privilege);
      });

      users.sort(function(a,b) {
        return a.userName > b.userName;
      });

      console.log("@@@@@ in User.getAllUser succeed users :" + JSON.stringify(users));
      callback(null, users);
      return;
    }
    if (res.statusCode !== 200) {
        callback(null, null);
      return;
    }
  });
};

User.saveNewUser = function(user_name, password, callback) {
  var options = {
    url : rest_api.post_new_user,
    body : {userName: user_name, password: password, createdAt: Date.now().toString()},
    json: true
  };

  request.post(options, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode !== 200 && res.statusCode !== 201) {
      callback(res.body, null);
      return;
    }
    var new_user = new User(body.userName, password, undefined, 'citizen', 'active');
    callback(null, new_user);
    return;
  });
};

User.updateUser = function(old_user_name, user_name, password, statusCode, role, activity, callback) {
  var options = {
    url : rest_api.update_user + old_user_name,
    body : {userName: user_name, lastStatusCode: statusCode, password: password, status: activity, privilege: role, modifiedAt: Date.now().toString()},
    json: true
  };

  request.put(options, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode !== 200 && res.statusCode !== 201) {
      callback(res.body, null);
      return;
    }
    var new_user = new User(body.userName, password, undefined);
    callback(null, new_user);
    return;
  });
}


User.updateStatus = function(user_name, new_status, callback) {
  var options = {
    url : rest_api.post_status + user_name,
    body : {statusCode: new_status, updatedAt: Date.now().toString()},
    json: true
  };

  request.post(options, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode !== 201) {
      callback(res.body, null);
      return;
    }
    callback(null, res.body);    // crumb is contained in response body
  });
};

module.exports = User;

