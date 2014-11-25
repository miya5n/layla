function reqAjax(pt, t, p, rt) {
//    $("#videos").text("Loading...");
    $.ajax({
        url: pt,
        type: t,
        data: p,
        jsonpCallback: 'jt',
        cache: false,
        dataType: rt,
        success: function (json) {
       		$('#sample-result').text(json.user.name);
        }
    });
}
