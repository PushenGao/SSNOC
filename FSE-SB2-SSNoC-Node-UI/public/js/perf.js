var time_handler = null;

function loadPerf() {
    $.ajax({
        url:  '/perf', 
        type: 'GET',
        dataType: 'html'
    }).done(function(data) {
        $('#hook_point').html(data);
        if ($('#recordingPerf').text() !== 'true'){
          recordButtonStart();
        } else {
          recordButtonStop();
        }
    });
    return false;
}

function stopRecordPerformance() {
  $.ajax({
        url:  '/stop_perf', 
        type: 'POST',
        dataType: 'application/json'
    }).done(function(data) {
    });
    $('#recordingPerf').text('false');
    recordButtonStart();
    getPerfResults();
    clearTimeout(time_handler);
}

function startRecordPerformance() {
  $('#recordingPerf').text('true');
  recordButtonStop();
  time = $('#rec_time').val();
  time_handler = setTimeout(getPerfResults, time * 1000);
  $('#results').html('<img src=/img/loading.gif></img>');
  $.ajax({
        url:  '/post_perf', 
        type: 'POST',
        dataType: 'application/json',
        data: time
  }).done(function(data) {
  });

}

function getPerfResults() {
  $.ajax({
        url:  '/perf_results', 
        type: 'GET',
        dataType: 'html',
  }).done(function(data) {
      $('#results').html(data);
      $('#recordBtnPerf').hide();
  });
  return false;
}

function recordButtonStart(){
  $('#recordBtnPerf').html("Start Recording Performance")
  $('#recordBtnPerf').unbind('click').click(function() {startRecordPerformance();});

}

function recordButtonStop(){
  $('#recordBtnPerf').html("Stop Recording Performance")
  $('#recordBtnPerf').unbind('click').click(function() {stopRecordPerformance();});
}

function updateRecordButtonPerf() {
    if ($('#recordingPerf').text() !== 'true'){
        $('#recordBtnPerf').html("Start Recording Performance")
        $('#recordBtnPerf').click(function() {startRecordPerformance();});
    } else {
        $('#recordBtnPerf').html("Stop Recording Performance")
        $('#recordBtnPerf').click(function() {stopRecordPerformance();});
    }
}
