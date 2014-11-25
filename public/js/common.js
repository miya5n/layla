$(document).ready(function(){
    $("#acMenu dt").on("click", function() {
    	if ($(this).next().height() == 0) {
	        var count=$(this).next().find('a').length;
	        $(this).next().height(30 * count);
    	} else {
    		$(this).next().height(0);
    	}
        $(this).next().slideToggle();
    });
    $("#link a").on("click", function() {
    	$("article h1").text($(this).text());
        $("iframe#dispFrame").attr({
            src: $(this).attr('href')
        });
        return false;
    });
    $("#container").slideDown(1000);
});
