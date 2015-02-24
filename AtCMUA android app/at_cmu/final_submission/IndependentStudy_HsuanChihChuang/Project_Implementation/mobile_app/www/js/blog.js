/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var blog = null;

function Blog() {

    var self = this;

    this.resetBlogContent = resetBlogContent;
    this.getBlog = getBlog;
    this.loadBlog = loadBlog;

    function resetBlogContent() {

        $("#member-content-basic").html("");
        $("#member-content-detail").html("");
    }

    function getBlog(guid, owner, title) {

        var params = new Object();
        params.guid = guid;

        var text = "text";

        var call =
                {
                    service: 'blog.get_blog',
                    type: 'GET',
                    opt: params,
                    callback: function(response) {

                        var jsonString = $.parseJSON(response.responseText);
                        var result = jsonString.result;

                        loadBlog(result, owner, title);
                    }
                };

        webService(call);
    }

    function loadBlog(result, owner, title) {

        var header = "<div class='blog-post-header'><img id='blog-post-avatar' src=" + result.avatar + "/><h4 id='blog-post-owner'>" + owner + "</h4></div>";
        $("#blog-content-basic").html(header);

        var desc = "<div><h3>" + title + "</h3></div>";
        desc += "<p>" + removeAnchor(result.content) + "</p>";

        $("#blog-content-detail").html(desc);

    }

}

function removeAnchor(str) {
    
    var start = 0;
    var index = str.indexOf('<a');
    var output = '';
    
    while (index != -1) {
        
        output += str.substring(start, index);
        str = str.substring(index, str.length);
        var tag = str.indexOf('>') + 1;
        var text = str.indexOf('</a>');
        output += str.substring(tag, text);
        str = str.substring(text+4, str.length);
        index = str.indexOf('<a');
    }
    
    output += str;
    return output;
}
