//function loadSearch() {
//  $.ajax({
//      url:  '/search',
//      type: 'GET',
//      dataType: 'html'
//    }).done(function(data) {
//      updateParticipants(cur_participants);
//      $('#hook_point').html(data);
//      private_with = ''; // TODO: change
//      configure_enter_key('search');
//      $('#results').hide();
//    });
//}

function doSearch() {
    query = $('#query').val();
    btnVal = $('#searchBtn').text().substring(7);
    if (btnVal == "") search_param = "everything";
    else search_param = btnVal.toLowerCase();

    $.ajax({
      url:  '/search/results',
      type: 'POST',
      dataType: 'html',
      data: {search_param: search_param, query: query}
    }).done(function(data) {
      $('#hook_point').html(data);
      $('#usernames').dataTable();
      $('#statuses').dataTable();
      $('#announcements').dataTable();
      $('#wallposts').dataTable();
      $('#privates').dataTable();
    });
}

function handleKeyPress(e){
  var key = e.which;
  if (key==13){
    doSearch();
  }
}


