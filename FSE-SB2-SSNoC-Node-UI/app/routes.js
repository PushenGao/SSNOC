var User = require('./models/UserRest');


module.exports = function(app, _, io, participants, passport) {
  var user_controller = require('./controllers/user')(_, io, participants, passport, refreshAllUsers);
  var people_controller = require('./controllers/people')(_, io, participants, passport, refreshAllUsers);
  var message_controller = require('./controllers/message')(_, io, participants, passport);
  var memory_controller = require('./controllers/memory')(_, io, participants, passport);
  var perf_controller = require('./controllers/perf')(_, io, participants, passport);
  var sna_controller = require('./controllers/sna')(_, io, participants, passport);
  var search_controller = require('./controllers/search')(_, io, participants, passport);
  var call_controller = require('./controllers/call')(_, io, participants, passport);

  app.get("/", user_controller.getLogin);

  app.post("/signup", user_controller.postSignup);

  app.get("/welcome", isLoggedIn, user_controller.getWelcome);

  app.get("/user", isLoggedIn, user_controller.getUser);
  app.get('/signup', user_controller.getSignup);
  app.get("/logout", isLoggedIn, user_controller.getLogout);

  app.post("/login", passport.authenticate('local-login', {
    successRedirect : '/ssnoc/#people',
    failureRedirect : '/',
    failureFlash: true
  }));


  app.get("/ssnoc/", isLoggedIn, people_controller.getBase);
  app.get("/people_data", isLoggedIn, people_controller.getPeople);

  app.get("/status", isLoggedIn, user_controller.updateStatus);
  app.get("/wall", isLoggedIn, message_controller.getWall);
  app.get("/wall_posts", isLoggedIn, message_controller.getWallPosts);
  app.post("/post_to_wall", isLoggedIn, message_controller.postToWall);

  app.get("/private", isLoggedIn, message_controller.getPrivateChat);
  app.get("/private_posts", isLoggedIn, message_controller.getPrivatePosts);
  app.post("/post_to_private_chat", isLoggedIn, message_controller.postToPrivate);

  app.get("/mem", isLoggedIn, memory_controller.getMeasureMemory);
  app.get("/mem/start", isLoggedIn, memory_controller.startMeasuring);
  app.get("/mem/stop", isLoggedIn, memory_controller.stopMeasuring);
  app.get("/mem/view", isLoggedIn, memory_controller.getMeasurements);

  app.get("/perf", isLoggedIn, perf_controller.getPerf);
  app.get("/perf_results", perf_controller.getPerfResults);
  app.post("/post_perf", isLoggedIn, perf_controller.postPerf);
  app.post("/stop_perf", perf_controller.stopPerf);

  app.get("/sna", isLoggedIn, sna_controller.getSna);
  app.post("/sna", isLoggedIn, sna_controller.postSna);

  app.get("/announcement", isLoggedIn, message_controller.loadAnnouncementPage);
  app.get("/announcements", isLoggedIn, message_controller.getAnnouncements);
  app.post("/post_announcement", isLoggedIn, message_controller.postAnnouncement);

//  app.get("/search", isLoggedIn, search_controller.getSearch);
  app.post("/search/results", isLoggedIn, search_controller.postSearchQuery);

  app.get("/users", isLoggedIn, people_controller.getUsersPage);
  app.get("/get_users", isLoggedIn, people_controller.getUsers);
  app.get("/get_user_profile", isLoggedIn, people_controller.getUserProfile);
  app.get("/get_user_info", isLoggedIn, people_controller.getUser);
  app.post("/update_user", isLoggedIn, user_controller.updateUser);

  app.get("/call_store", isLoggedIn, call_controller.storeCall);
  app.get("/call_log", isLoggedIn, call_controller.getCalls);
};


function isLoggedIn(req, res, next) {
  if (req.isAuthenticated())
    return next();

  res.redirect('/');
}

function refreshAllUsers(participants, callback) {
  participants.all = [];
  User.getVisibleUsers(function(err, users) {
    users.forEach(function(user) {
      participants.all.push({'userName' : user.local.name});
    });
    callback();
  });
}
