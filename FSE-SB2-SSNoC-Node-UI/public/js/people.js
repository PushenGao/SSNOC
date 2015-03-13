var socket = io.connect(document.domain);
var cur_participants;
var sessionId = '';
window.my_name = '';
var statuses = {};

function loadBase(callback, arg) {
    $.ajax({
        url:  '/people_data',
        type: 'GET',
        dataType: 'html'
      }).done(function(data) {
        $('#hook_point').html(data);
        callback(arg);
      });
  }

function loadPeople() {
  loadBase(updateParticipants, cur_participants);
  private_with = '';  //change to invisible element
}

function pageLoad() {
  loadBase(function(){return;});
  init();
}


function changeStatus(statusColor) {
    statuses[window.my_name] = statusColor;
    $.ajax({
        url:  '/status?status=' + statusColor,
        type: 'GET',
        dataType: 'html'
      }).done(function(data) {

        // Post status change to wall
        var message = "Status changed to " + statusColor;
        $.ajax({
          url:  '/post_to_wall',
          type: 'POST',
          dataType: 'text',
          data: message
        }).done(function(data) {
          socket.emit('newWallPost', {message: message, user_name: window.my_name});

        });

        socket.emit('newUser', {id: sessionId, name: window.my_name, statusCode: statusColor});
        loadPeople();
      });
}


function render_detail_ele(name, online) {
  var detail_ele = '<div class="row user-info ' + name + '">'
    + '<a class="btn btn-info col-xs-6 col-sm-3 col-md-6 col-lg-6 '
    + 'col-xs-offset-3 col-sm-offset-3 col-md-offset-3 col-lg-offset-3" '
    + 'onClick=\"private_chat(\'' + name + '\')\">Private Message</a><hr/>';
  if (online) {
    detail_ele += '<a class="btn btn-info col-xs-6 col-sm-3 col-md-6 col-lg-6 '
    + 'col-xs-offset-3 col-sm-offset-3 col-md-offset-3 col-lg-offset-3" '
    + 'onClick=\"call_user(\'' + name + '\')\">Make a Call</a><hr/>';
  }
  detail_ele += '</div></div>';
  return detail_ele;

}

function getCallLog() {
  var res = null;
  jQuery.ajax({
    url: "/call_log",
    success: function(data) {
      res = JSON.parse(data);
    },
    async: false
  });          
  return res;
}


function updateParticipants(participants) {

    
    $('#participants').html('');
    var map = {};
    var map2 = {};
    statuses = {};
    var userName = '';
    var userEle = '';


    for (var sId in participants.online){
      userName = participants.online[sId].userName;

      if (map[userName] == undefined || map[userName] !== sessionId){
        map[userName] = {sId:sId};
      }

      statuses[userName] = participants.online[sId].statusCode;
    }

    /* Get the order of previously contacted users */
    var contacted = getCallLog();
    var order = [];

    /* Make a copy of the Map */
    var newMap = {};
    for (var i in map)
       newMap[i] = map[i];

    /* Use the copy to iterate contacted users and remove values */
    for (var id in contacted) {
      var user = contacted[id];
      if (user in newMap) {
        order.push(user);
        delete newMap[user];
      }
    }

    if ($('#participants').length == 0) {
      return false;
    }

    /* Get an array from the remaining keys of the map copy */
    keys = Object.keys(newMap);
    keys.sort();

    /* Concatenate the two arrays */
    keys = order.concat(keys);

    for (var i = 0; i < keys.length; i++) {
      var name = keys[i];
      var user_status = statuses[name];
      
      var status_photo = 'green-dot.png'
      if (user_status == 'yellow') {
        status_photo = 'yellow-dot.png'
      } else if (user_status == 'red') {
        status_photo = 'red-dot.png'
      }

      if ($.inArray(name, order) >= 0) {
        var img_ele = '<img src="/img/photo4-blue.png" height=40/>';
      } else {
        var img_ele = '<img src="/img/photo4.png" height=40/>';
      }
      var photo_ele = '<div class="col-xs-3 col-sm-2 col-md-1 col-lg-1"><img src="/img/' + status_photo + '" height=10/><br/>'+ img_ele + '</div>';
      var name_ele = '<div class="col-xs-8 col-sm-9 col-md-10 col-lg-10"><strong>' + name + '</strong></div>';
      var dropdown_symbol = map[name].sId === sessionId ? '':'<i class="glyphicon glyphicon-chevron-down text-muted"></i>';
      var dropdown_ele = '<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 dropdown-user" data-for=".' + name + '">' + dropdown_symbol + '</div>';

      var info_ele = '<div class="row user-row search_item">' + photo_ele + name_ele + dropdown_ele + '</div>';
      var detail_ele = render_detail_ele(name, true);
      if (map[name].sId === sessionId || name === window.my_name) {
        // this statement is stupid but I'm gonna keep it here just to show how horrible the skeleton code was
      } else {
        $('#participants').append(info_ele);
        $('#participants').append(detail_ele);
      }
    }

    map2 = {}
    participants.all.forEach(function(userObj) {
      if (map[userObj.userName] == undefined) {
        map2[userObj.userName] = 1;  // set implementation
      }
    });
    keys2 = Object.keys(map2);
    keys2.sort();

    for (var i = 0; i < keys2.length; i++) {
        var img_ele = '<img class="img-circle" src="/img/photo4.png" height=40/>';
        var photo_ele = '<div class="offline col-xs-3 col-sm-2 col-md-1 col-lg-1"><img src="/img/grey-dot.png" height=10/><br/>'+img_ele + '</div>';
        var name_ele = '<div class="offline col-xs-8 col-sm-9 col-md-10 col-lg-10"><strong>' + keys2[i] + '</strong><br/></div>';
        var dropdown_ele = '<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 dropdown-user" data-for=".' + keys2[i] + '"><i class="glyphicon glyphicon-chevron-down text-muted"></i></div>';
        var info_ele = '<div class="row user-row search_item">' + photo_ele + name_ele + dropdown_ele + '</div>';
        var detail_ele = render_detail_ele(keys2[i], false);
        $('#participants').append(info_ele);
        $('#participants').append(detail_ele);
    }

    $('.user-info').hide();
    $('.dropdown-user').click(function() {
      var dataFor = $(this).attr('data-for');
      var idFor = $(dataFor);
      var currentButton = $(this);
      idFor.slideToggle(400, function() {
        if(idFor.is(':visible'))
          {
            currentButton.html('<i class="glyphicon glyphicon-chevron-up text-muted"></i>');
          }
          else
            {
              currentButton.html('<i class="glyphicon glyphicon-chevron-down"></i>');
            }
      })
    });
  }


function loadScript(url)
{
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;

    // Fire the loading
    head.appendChild(script);
}


function init() {

  socket.on('connect', function () {
    sessionId = socket.socket.sessionid;
    $.ajax({
      url:  '/user',
      type: 'GET',
      dataType: 'json'
    }).done(function(data) {
      var name = data.name;
      var statusCode = data.statusCode;
      window.my_name = data.name;
      loadScript('/js/call.js');
      socket.emit('newUser', {id: sessionId, name: name, statusCode: statusCode});
    });
  });

  socket.on('newConnection', function (data) {
    cur_participants = data.participants;
    updateParticipants(data.participants);
    populate_chatbox();

  });

  socket.on('userDisconnected', function(data) {
    cur_participants = data.participants;
    updateParticipants(data.participants);
    populate_chatbox();

  });

  socket.on('error', function (reason) {
    console.log('Unable to connect to server', reason);
  });

  var panels = $('.user-info');
  panels.hide();
  $('.dropdown-user').click(function() {
    var dataFor = $(this).attr('data-for');
    var idFor = $(dataFor);
    var currentButton = $(this);
    idFor.slideToggle(400, function() {
      if(idFor.is(':visible'))
        {
          currentButton.html('<i class="glyphicon glyphicon-chevron-up text-muted"></i>');
        }
        else
        {
          currentButton.html('<i class="glyphicon glyphicon-chevron-down text-muted"></i>');
        }
    })
  });
}

$(document).on('ready', pageLoad);
