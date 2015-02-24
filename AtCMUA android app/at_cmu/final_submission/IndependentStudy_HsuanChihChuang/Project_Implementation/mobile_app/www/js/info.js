/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var info = null;

function Info()
{

    var self = this;

    this.categoryList = ["handbook", "internship", "career", "events", "accommodation", "orientation", "IT support", "printer", "wireless"];
    this.index = 0;
    this.loadBlock = 8;

    this.loadCategoryList = loadCategoryList;
    this.resetPostsByCategory = resetPostsByCategory;
    this.getPostsByCategory = getPostsByCategory;
    this.loadPosts = loadPosts;

    function loadCategoryList() {

        var html = "";
        var count = 0;

        $("#info-listview").html(html);

        $.each(self.categoryList, function() {

            html += "<li>";
            html += "<a href='#category?category=" + encodeURIComponent(self.categoryList[count]) + "' data-transition='slide'>";
            html += "<h3 class='category-title'>" + self.categoryList[count] + "</h3>";
            html += "</a>";
            html += "</li>";
            count++;

        });
        $("#info-listview").append(html);
        $("#info-listview").listview("refresh");
    }

    function resetPostsByCategory() {

        self.index = 0;
        $("#category-listview").html("");
    }

    function getPostsByCategory(category) {

        var params = new Object();
        params.offset = self.index;
        params.limit = self.loadBlock;
        params.tag = category;

        var call =
                {
                    service: 'blog.get_posts_by_tag',
                    type: 'GET',
                    opt: params,
                    callback: function(response) {
                        var jsonString = $.parseJSON(response.responseText);

                        if (jsonString.status == '-1') {

                            if (jsonString.message == 'pam_auth_userpass:failed') {

                                invalidateAuthToken();
                                getPostsByCategory(category);
                            }

                        } else {

                            //alert(response.responseText);
                            var result = jsonString.result;
                            self.index += self.loadPosts(result, category);
                        }

                    }
                };

        webService(call);
    }


    function loadPosts(result, category) {

        var html = "";
        
        $("#category-title").html(category);

        for (var i = 0; i < result.count; i++)
        {
            var guid =  encodeURIComponent(result[i].blog_guid);
            var owner = encodeURIComponent(result[i].name);
            var title = encodeURIComponent(result[i].title);

            html += "<li>";
            html += "<a href='#blog?guid=" + guid + "&owner=" + owner + "&title=" + title + "' data-transition='slide'>";
            html += "<h3 class='feed-entry-title'>" + result[i].title + "</h3>";
            html += "<p>" + result[i].name + "</p>";
            html += "</a>";
            html += "</li>";
        }
       
        $("#category-listview").append(html);
        $("#category-listview").listview("refresh");
        
        result.count;
    }
}
