var host_url = "http://localhost:1234/ssnoc";

module.exports = {
  'get_visible_users' : host_url + '/users/active',
  'get_all_users' : host_url + '/users',
  'is_password_valid' : host_url + '/user/',
  'get_user' : host_url + '/user/',
  'post_new_user' : host_url + '/user/signup',
  'post_status' : host_url + '/status/',
  'message' : host_url + '/message/',
  'getWall' : host_url + '/messages/wall/visible',
  'private' : host_url + '/messages/',
  'sna_range' : host_url + '/usergroups/unconnected/',
  'perf_setup' : host_url + '/performance/setup',
  'perf_teardown' : host_url + '/performance/teardown',
  'memory' : host_url + '/memory/',
  'announcements' : host_url + '/messages/announcement/visible',
  'post_announcement': host_url + '/message/announcement',
  'update_user': host_url + '/user/',
  'search': host_url + '/search/',
  'call': host_url + '/calllog/'
};
