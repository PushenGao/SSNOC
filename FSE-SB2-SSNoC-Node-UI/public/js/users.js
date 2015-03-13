function loadUsers() {
  $.ajax({
    url: '/users',
    type: 'GET',
    dataType: 'html'
  }).done(function(data) {
    $('#hook_point').html(data);
    populate_users();
  });
}

function populate_users() {
  if ($('#users').length == 0) {
    return false;
  }
  $.ajax({
    url:  '/get_users',
    type: 'GET',
    dataType: 'json'
  }).done(function(data) {
    if ($('#users').length !== 0) {
      var users = '';
      data.forEach(function(object) {
        users += format_user(object);
      });
      $('#users').html(users);
    }
    jQuery(document).ready(function($) {
      $(".clickableRow").click(function() {
        username = $(this).attr("username");
        populate_user(username);
      });
    });
  });
  return false;
}

function format_user(object) {
  var user_status = object.local.statusCode;
  if (user_status == "yellow") {
    user_status = "#EBA133";
  } else if (user_status == undefined) {
    user_status = "grey";
  }

  str = '<tr class=\'clickableRow btn-default\' style=\"color: ' + user_status +
    '\" username=\"' + object.local.name + '\">' +
    '<td>' + object.local.name + '</td>' +
    '<td>' + object.local.statusCode + '</td>' +
    '<td>' + object.local.privilege + '</td>' +
    '<td>' + object.local.status + '</td></tr>';
  return str;
}

function populate_user(username) {
  $.ajax({
    url: '/get_user_profile?profile_username=' + username,
    type: 'GET',
    dataType: 'html'
  }).done(function(data) {
    $('#hook_point').html(data);
    populate_fields(username);
  });
}

function populate_fields(username) {
  $.ajax({
    url: '/get_user_info?username=' + username,
    type: 'GET',
    dataType: 'json'
  }).done(function (data) {
    $('#old_user').val(data.name);
    $('#username').val(data.name);
    $('#statuscode').val(data.statusCode);
    $('#password').val(data.password);
    $('#role').val(data.privilege);
    $('#activity').val(data.status);
  });
}

