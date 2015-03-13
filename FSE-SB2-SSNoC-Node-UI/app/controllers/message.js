var Message = require('../models/MessageRest');

module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {

    getWall : function(req, res) {
      res.render('wall');
    },

    getPrivateChat : function(req, res) {
      var target_user = req.param("target_user");
      res.render('private_chat', {target_user: target_user});
    },


    postToWall : function(req, res) {
      var user_name = req.session.passport.user.user_name;
      message = Object.keys(req.body)[0];  
      Message.postToWall(message, user_name, function(done, data){
        if (!done) {
          res.json(500, data);
        }
        res.json(200, data)
      });
    },


    postToPrivate : function(req, res) {
      var sender = req.session.passport.user.user_name;
      var receiver = req.param("receiver");

      message = Object.keys(req.body)[0];   
      Message.postToPrivate(message, sender, receiver, function(done, data){
        if (!done) {
          res.json(500, data);
        }
        res.json(200, data)
      });
    },

    getWallPosts : function(req, res) {
      Message.getWallPosts(function(done, data) {
        if (!done) {
          res.json(500, data.body);
        }
        res.json(200, data.body);
      });
    },

    getPrivatePosts : function(req, res) {
      var this_user = req.session.passport.user.user_name;
      var other_user = req.param("with");
      Message.getPrivatePosts(this_user, other_user, function(done, data) {
        if (!done) {
          res.json(500, data.body);
        }
        res.json(200, data.body);
      });
    },

    loadAnnouncementPage : function(req, res) {
      res.render('announcement');
    },

    getAnnouncements : function(req, res) {
      Message.getAnnouncements(function(done, data) {
        if (!done) {
          res.json(500, data.body);
        }
        res.json(200, data.body);
      });
    },

    postAnnouncement : function(req, res) {
      var user_name = req.session.passport.user.user_name;
      message = Object.keys(req.body)[0];   //TODO: var?
      Message.postAnnouncement(message, user_name, function(done, data){
        if (!done) {
          res.json(500, data);
        }
        res.json(200, data)
      });
    }

  };
};
