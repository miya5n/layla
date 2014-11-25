$(document).ready(function(){
    var laylaClass = $("[class^='layla:']");
    for (i = 0; i < laylaClass.length; i++) {
        var x = laylaClass[i].className.replace('layla:', '');
        reqAjax(x, "get", "callback=jt", "jsonp");
    }
//http://news.mynavi.jp/articles/2010/12/06/pseudo-javascript-thread-ideas/
});
