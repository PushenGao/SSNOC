module.exports = function(_, io, participants) {
  io.on("connection", function(socket){
    socket.on("newUser", function(data) {
      participants.online[data.id] = {'userName' : data.name, 'statusCode': data.statusCode, 'status': data.status};
      io.sockets.emit("newConnection", {participants: participants});
    });

        socket.on("disconnect", function() {
      delete participants.online[socket.id];
      io.sockets.emit("userDisconnected", {id: socket.id, sender:"system", participants:participants});
    });

    socket.on("newWallPost", function(data) {
      io.sockets.emit("newWallPost", {content: data.message, author: data.user_name, postedAt: Date.now().toString()});
    });

    socket.on("newPrivatePost", function(data) {
      io.sockets.emit("newPrivatePost", {content: data.message, author: data.user_name, 
          receiver: data.receiver, postedAt: Date.now().toString()});
    });

    socket.on("newAnnouncement", function(data) {
      io.sockets.emit("newAnnouncement", {content: data.message, author: data.user_name, postedAt: Date.now().toString()});
    });

    socket.on("locationRequest", function(data) {
      io.sockets.emit("locationRequest", {});
    });

    socket.on("location", function(data) {
      io.sockets.emit("location", {name: data.name, longitude: data.longitude, latitude: data.latitude});
    });

  });
};

