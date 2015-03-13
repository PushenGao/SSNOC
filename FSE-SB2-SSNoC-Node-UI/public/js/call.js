var peer = new Peer(window.my_name, 
    {host: window.location.host, port: 8000, path:'/'});
    //{host: window.location.host, port: 8000, path:'/', debug:3});

var isLocationRequestor = false;
var locations = [];
var longitude;
var latitude;
var redbutton_enabled = true;


/* Calculates distance between two geolocation points */
function distance(lon1, lat1, lon2, lat2) {
  var R = 6371; 
  var dLat = (lat2-lat1).toRad();  
  var dLon = (lon2-lon1).toRad(); 
  var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
          Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) * 
          Math.sin(dLon/2) * Math.sin(dLon/2); 
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
  var d = R * c; // Distance in km
  return d;
}

/** Converts numeric degrees to radians */
if (typeof(Number.prototype.toRad) === "undefined") {
  Number.prototype.toRad = function() {
    return this * Math.PI / 180;
  }
}


// Emergency Call function
$('#redbutton').on('click', function(){
  if (!redbutton_enabled) {
    return;
  }
  redbutton_enabled = false;
  initialize_audio_ele();
  socket.emit('locationRequest', {});
  isLocationRequestor = true;

  get_location(function(crd) {
      longitude = crd.longitude;
      latitude = crd.latitude;
  });

  setTimeout(function(){
    isLocationRequestor = false;

    // find closest user
    var min_distance = 100000;
    var min_user = null;
    for (var i=0; i < locations.length; i++) {
      if (!((locations[i].longitude == longitude) && 
          (locations[i].latitude == latitude))) {
        var dist = distance(longitude, latitude, 
                                locations[i].longitude, locations[i].latitude);
        if (dist < min_distance) {
          min_distance = dist;
          min_user = locations[i].name;
        }
      }
    }
    call_user(min_user);
  }, 4000);

});

socket.on('location', function(data) {
  if (isLocationRequestor) {
    locations.push(data)
  }

}); // append each location to a list, if user is not self


function loadCall(target_user, call, callback){
  redbutton_enabled = true;
  $('#incall').html('In Call with ' + target_user);
  $('#redbutton').html('Hang Up');
  $('#redbutton').on('click', function(){
    hangUp(call);
  });
}

function hangUp(call) {
    peer.destroy();
    call.close();
    location.reload();
}


function initialize_audio_ele() {
    var audio = document.getElementById("camera");
    audio.load();
    audio.play();
    audio.pause();
}

function call_user(user_name) {
    initialize_audio_ele()

    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
    navigator.getUserMedia({video: false, audio: true}, function(stream) {

        peer.on('error', function(err) {
          console.log("error: " + err);
        });

        var call = peer.call(user_name, stream);

        call.on('close', function() {
          hangUp(call);
        });

        call.on('stream', function(remoteStream) {
          set_video_element(remoteStream);
          loadCall(user_name, call);
        });
      }, function(err) {
        console.log('Failed to get local stream' ,err);
    });
}


peer.on('call', function(call) {
  $("#note").html('<btn class="btn btn-block" id="answer">Incoming call from ' + call.peer +'</btn>');
  $("#note").show()
  $("#answer").on('click', function() {
    $("#note").hide()
    initialize_audio_ele();
    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
    navigator.getUserMedia({video: false, audio: true}, function(stream) {
      call.answer(stream); // Answer the call with an A/V stream.

      call.on('close', function() {
        hangUp(call);
      });

      call.on('stream', function(remoteStream) {
        loadCall(call.peer, call);
        updateCallHistory(call.peer);
        set_video_element(remoteStream);
      });
    }, function(err) {
      console.log('Failed to get local stream' ,err);
    });
  });
});



function updateCallHistory(other_user) {
  console.log("storing call");
  $.ajax({
      url:  '/call_store?user='+other_user,
      type: 'GET',
    }).done(function() {});
}

function set_video_element(stream) {
  var video = document.getElementById("camera");
  video.src = window.URL.createObjectURL(stream);
  video.play();
}


function get_location(callback) {
  var options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0
  };

  function success(pos) {
    var crd = pos.coords;
  
    callback(crd);
  };

  function error(err) {
    console.warn('ERROR(' + err.code + '): ' + err.message);
  };

  navigator.geolocation.getCurrentPosition(success, error, options);
}


function mirror_location() {
  get_location(function (crd) {
    socket.emit('location', {name: window.my_name, 
                            longitude: crd.longitude, 
                            latitude: crd.latitude});
  });
}



socket.on('locationRequest', mirror_location);

$(document).ready(function () {
  get_location(function (crd) {});
});
