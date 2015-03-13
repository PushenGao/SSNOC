var private_with = '';
var notificationInterval = null;


function loadWall() {
  $.ajax({
      url:  '/wall',
      type: 'GET',
      dataType: 'html'
    }).done(function(data) {
      updateParticipants(cur_participants);
      $('#hook_point').html(data);
      populate_chatbox();
      configure_enter_key("wall");
      private_with = ''; //change to invisible element
    });
  return false;
}

function loadAnnouncementPage() {
  $.ajax({
    url:  '/announcement',
    type: 'GET',
    dataType: 'html'
  }).done(function(data) {
    updateParticipants(cur_participants);
    private_with = ''; //change to invisible element
    $('#hook_point').html(data);
    populate_announcement_box();
    can_post = (($('#privilege').html() == 'administrator') || 
               ($('#privilege').html() == 'coordinator'));
    if (can_post) {
      configure_enter_key("announcement");
    } else {
      $('#message').hide()
      $('button.btn.btn-primary').hide()
    }
  });
  return false;
}

function populate_announcement_box() {
  if ($('#announcementbox').length == 0) {
    return false;
  }
  $.ajax({
    url:  '/announcements',
    type: 'GET',
    dataType: 'json'
  }).done(function(data) {
    if ($('#announcementbox').length !== 0) {
      var announcements = '';

      data.forEach( function(object) {
        announcements += format_linestring(object);
      });
      $('#announcementbox').html(announcements);
      scroll_to_bottom("announcementbox");
    }
  });
  return false;
}

function configure_enter_key(page, private_user) {
  $("#message").keyup(function(event){
      if(event.keyCode == 13){
        if (page == "wall") {
          postToWall();
        } else if (page == "announcement") {
            postAnnouncement();
        } else if (page == "search") {
            doSearch();
        } else {
          postPrivateMessage(private_user);
        }
      }
  });
}

function private_chat(user_name) {
  $.ajax({
      url:  '/private?target_user=' + user_name,
      type: 'GET',
      dataType: 'html'
    }).done(function(data) {
      $('#hook_point').html(data);
      populate_private_chatbox(user_name);
      configure_enter_key("private", user_name);
      private_with = user_name;
      $('#note').html('');
    });
  return false;
}


function populate_private_chatbox(user_name) {
  if ($('#privatebox').length == 0) {
    return false;
  }
  $.ajax({
      url:  '/private_posts?with=' + user_name,
      type: 'GET',
      dataType: 'json'
    }).done(function(data) {
      if ($('#privatebox').length !== 0) {
        var chat_logs = '';

        data.forEach( function(object) {
          chat_logs += format_linestring(object);
        });
        $('#privatebox').html(chat_logs);
        scroll_to_bottom("privatebox");
      }
    });
  return false;
}


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

function format_linestring(object) {
      var user_status = statuses[object.author];
      if (user_status == "yellow") {
        user_status = "#EBA133";
      } else if (user_status == undefined) {
        user_status = "grey";
      }

      str = '<li>(' + ' ' + format_date(object.postedAt) + 
          ') <a href=\"/ssnoc/#private\" onclick=\"private_chat(' + object.author + 
          ');\" style=\"color: ' + user_status + ';\">' + object.author + 
          '</a>: ' + object.content + '</li>';
  return str;
}


function populate_chatbox() {
  if ($('#chatbox').length == 0) {
    return false;
  }
  $.ajax({
      url:  '/wall_posts',
      type: 'GET',
      dataType: 'json'
    }).done(function(data) {
      if ($('#chatbox').length !== 0) {
        var chat_logs = '';

        data.forEach( function(object) {
          chat_logs += format_linestring(object);
        });
        $('#chatbox').html(chat_logs);
        scroll_to_bottom("chatbox");
      }
    });
  return false;
}

function scroll_to_bottom(element) {
    var elem = document.getElementById(element);
    elem.scrollTop = elem.scrollHeight;
}

function add_to_chatbox(element, data) {
    if (element !== null) {
      var chat_logs = format_linestring(data)
      $('#' + element).append(chat_logs);
      scroll_to_bottom(element);
    }
}

function postAnnouncement() {
  var new_message = document.getElementsByName('message')[0].value;
  if (new_message == "") {
    return false;
  }

  $.ajax({
    url:  '/post_announcement',
    type: 'POST',
    dataType: 'text',
    data: new_message
  }).done(function() {
    document.getElementsByName('message')[0].value = '';
    socket.emit('newAnnouncement', {message: new_message, user_name: window.my_name});
  });

}

function postPrivateMessage(target_user) {
  var new_message = document.getElementsByName('message')[0].value;
  if (new_message == "") {
    return false;
  }

  $.ajax({
      url:  '/post_to_private_chat?receiver=' + target_user,
      type: 'POST',
      dataType: 'text',
      data: new_message
    }).done(function(data) {
      document.getElementsByName('message')[0].value = '';
      socket.emit('newPrivatePost', {message: new_message, user_name: window.my_name, receiver: target_user });
    });
}


function postToWall() {
  var new_message = document.getElementsByName('message')[0].value;
  if (new_message == "") {
    return false;
  }

  $.ajax({
      url:  '/post_to_wall',
      type: 'POST',
      dataType: 'text',
      data: new_message
    }).done(function(data) {
      document.getElementsByName('message')[0].value = '';
      socket.emit('newWallPost', {message: new_message, user_name: window.my_name});
    });

}

// Private Message Notification 
function newNotification(author) {
  if ($('#note').html() == '') {
    if (notificationInterval != null) {
      clearInterval(notificationInterval);
      notificationInterval = null;
    }
    notificationInterval = setInterval(function () {$('#note').html('');}, 7000);
    var notification = '<a href="/ssnoc/#private" onClick="private_chat(\'' + 
                      author + '\');">New message from ' + author + '</a>';
    $('#note').html(notification);
  }
}


// Announcement Notification
function newAnnouncement() {
  if ($('#note').html() == '') {
    if (notificationInterval != null) {
      clearInterval(notificationInterval);
      notificationInterval = null;
    }
    notificationInterval = setInterval(function () {$('#note').html('');}, 7000);
    var notification = '<a href="/ssnoc/#private" onClick="loadAnnouncementPage(); ' +
      '$(\'#note\').html(\'\');">New Announcement Posted</a>';
    $('#note').html(notification);
  }
}



socket.on('newAnnouncement', function(data){
  newAnnouncement();
  add_to_chatbox('announcementbox', data);
});

socket.on('newWallPost', function(data){
  add_to_chatbox('chatbox', data);
});


socket.on('newPrivatePost', function(data){
  if ((data.receiver == window.my_name && data.author == private_with) ||
        (data.receiver == private_with && data.author == window.my_name)) {
    add_to_chatbox('privatebox', data);
  } else if (data.receiver == window.my_name) {
    newNotification(data.author);
  }
});

