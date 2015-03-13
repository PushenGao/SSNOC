var Search = require('../models/SearchRest');

function format_date(epoch) {
  d = new Date(0); 
  d.setUTCSeconds(epoch/1000)
  var m_names = new Array("Jan", "Feb", "Mar", 
      "Apr", "May", "Jun", "Jul", "Aug", "Sep", 
      "Oct", "Nov", "Dec");

  var date = d.getDate();
  var month = d.getMonth();
  var min = d.getMinutes();
  min = (new Array(2 + 1 - min.toString().length)).join('0') + min;
  var hour = d.getHours();

  return date + " " + m_names[month] + " at " + hour + ":" + min;
}

function format_messageList(list) {
  var length = list.length;
  for (var i = 0; i < length; i++) {
    list[i].postedAt = format_date(list[i].postedAt)
  }
  return list
}

module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {

//    getSearch: function(req, res) {
//      res.render('search');
//    },

    postSearchQuery : function(req, res) {
      var info = req.body;
      search_param = info['search_param'];
      query = info['query'];
      username = req.session.passport.user.user_name;
      var usernames = [];
      var statuses = [];
      var announcements = [];
      var wallposts = [];
      var privates = [];

      switch(search_param) {
        case 'users':
          Search.nameQuery(query, function (data) {
            res.render('search_results', {usernames: data});
          });
          break;

        case 'statuses':
          Search.statusQuery(query, function (data) {
            res.render('search_results', {statuses: data});
          });
          break;

        case 'announcements':
          Search.announcementQuery(query, function (data) {
            announcements = format_messageList(data);
            res.render('search_results', {announcements: announcements});
          });
          break;

        case 'wallposts':
          Search.wallQuery(query, function (data) {
            wallposts = format_messageList(data);
            res.render('search_results', {wallposts: wallposts});
          });
          break;

        case 'privates':
          Search.privateQuery(username, query, function (data) {
            privates = format_messageList(data);
            res.render('search_results', {privates: privates});
          });
          break;

        default:
          Search.nameQuery(query, function (data) {
            usernames = data;
            Search.statusQuery(query, function (data) {
              statuses = data;
              Search.announcementQuery(query, function (data) {
                announcements = format_messageList(data);
                Search.wallQuery(query, function (data) {
                  wallposts = format_messageList(data);
                  Search.privateQuery(username, query, function (data) {
                    privates = format_messageList(data);
                    res.render('search_results', {usernames: usernames,
                      statuses: statuses,
                      announcements: announcements,
                      wallposts: wallposts,
                      privates: privates });
                  });
                });
              });
            });
          });
          break;
      }
    }

  };
};
