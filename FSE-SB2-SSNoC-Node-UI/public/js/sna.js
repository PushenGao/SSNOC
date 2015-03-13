var sna_sample_result = [{"group":0,"members":"Zyra,Ahri"},{"group":1,"members":"Jax,Ahri"},{"group":2,"members":"Leona,Ahri"},{"group":3,"members":"Nasus,Ahri"},{"group":4,"members":"Pushen,Alex"},{"group":5,"members":"Daniel,Alex"},{"group":6,"members":"Hill,Alex"},{"group":7,"members":"Zyra,Jax"},{"group":8,"members":"Zyra,Leona"},{"group":9,"members":"Zyra,Nasus"},{"group":10,"members":"Leona,Jax"},{"group":11,"members":"Nasus,Jax"},{"group":12,"members":"Pushen,Navnika"},{"group":13,"members":"Navnika,Daniel"},{"group":14,"members":"Navnika,Hill"},{"group":15,"members":"Pushen,Daniel"},{"group":16,"members":"Nasus,Leona"}];

function loadSna() {
  $.ajax({
      url:  '/sna',
      type: 'GET',
      dataType: 'html'
  }).done(function(data) {
    updateParticipants(cur_participants);
    $('#hook_point').html(data);
    private_with = ''; // TODO: change
  });
  return false;
}

function showSnaResult(data) {
  result = [];
  result.push('<ul>');
  for (var i = 0; i < data.length; i++) {
    result.push('<li>');
    result.push(data[i].join(', '));
    result.push('</li>');
  }
  result.push('</ul>');
  $('.sna-result').html(result.join(''));
}

function startSna() {
  var start = '0';
  var end = '9223372036854775807';
  var s1 = $('form')[0].date1.value;
  var h1 = $('form')[0].hour1.value;
  if (s1.length === 10) {
    start = Date.parse(s1 + ' ' + h1 + ':00:00').toString();
  }
  var s2 = $('form')[0].date2.value;
  var h2 = $('form')[0].hour2.value;
  if (s2.length === 10) {
    end = Date.parse(s2 + ' ' + h2 + ':00:00').toString();
  }
  var data = {
    'start' : start,
    'end' : end
  }
  $.ajax({
    url: '/sna',
    type: 'POST',
    dataType: 'json',
    data: data
  }).done(showSnaResult);
  return false;
}
