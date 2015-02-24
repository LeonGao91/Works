/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var feed = null;

function Feed() {

    var self = this;

    this.index = 0;
    this.loadBlock = 8;

    this.resetFeed = resetFeed;
    this.getFeed = getFeed;
    this.loadFeed = loadFeed;

    function resetFeed() {

        self.index = 0;
        $("#feed-listview").html("");
    }

    function getFeed() {

        var params = new Object();
        params.offset = self.index;
        params.limit = self.loadBlock;

        var call =
                {
                    service: 'blog.get_latest_posts',
                    type: 'GET',
                    opt: params,
                    callback: function(response) {
                        var jsonString = $.parseJSON(response.responseText);

                        if (jsonString.status == '-1') {

                            if (jsonString.message == 'pam_auth_userpass:failed') {

                                invalidateAuthToken();
                                self.getFeed();
                            }

                        } else {

                            var result = jsonString.result;
                            //alert(response.responseText);
                            self.index += self.loadFeed(result);
                        }
                        
                    }
                };

        webService(call);
    }

    function loadFeed(result) {

        var html = "";
        var count = 0;
        $.each(result, function() {


            var guid = encodeURIComponent(this.blog_guid);
            var owner = encodeURIComponent(this.name);
            var title = encodeURIComponent(this.title);

            html += "<li>";
            html += "<a href='#blog?guid=" + guid + "&owner=" + owner + "&title=" + title + "' data-transition='slide'>";
            html += "<h3 class='feed-entry-title'>" + this.title + "</h3>";
            html += "<p>" + this.name + "</p>";
            html += "</a>";
            html += "</li>";
            count++;

        });
        $("#feed-listview").append(html);
        $("#feed-listview").listview("refresh");
        return count;
    }
}
