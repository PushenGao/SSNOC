function loadMemory() {
  $.ajax({
      url:  '/mem',
      type: 'GET',
      dataType: 'html'
    }).done(function(data) {
      updateParticipants(cur_participants);
      $('#hook_point').html(data);
      private_with = ''; // TODO: change
      updateRecordButton();
    });
  return false;
}

function startRecordMemory() {
  $.ajax({
      url:  '/mem/start',
      type: 'GET',
      dataType: 'html'
    }).done(function(data) {
      $('#recording').text('true');
      updateRecordButton();
    });
}

function stopRecordMemory() {
  $.ajax({
      url:  '/mem/stop',
      type: 'GET',
      dataType: 'html'
    }).done(function(data) {
      $('#recording').text('false');
      updateRecordButton();
    });
}

function displayMemory() {
  $.ajax({
      url:  '/mem/view',
      type: 'GET',
      dataType: 'html'
    }).done(function(data) {
      $('#results').html(data);
    });

}

function updateRecordButton() {
  if ($('#recording').text() !== 'true'){
    $('#recordBtn').html("Start Recording Memory");
    $('#recordBtn').unbind('click').click(function() {
      startRecordMemory();
    });
  } else {
    $('#recordBtn').html("Stop Recording Memory");
    $('#recordBtn').unbind('click').click(function() {
      stopRecordMemory();
    });
  }
}
