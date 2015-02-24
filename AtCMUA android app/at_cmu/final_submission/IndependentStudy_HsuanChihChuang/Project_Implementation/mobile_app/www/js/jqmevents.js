/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function registerJQMEvents() {


    /*
     * Feed Tab list/detail pages transition logic
     */
    $(document).on('pageshow', '#feed', function(event, data) {

        if (feed == null) {
            feed = new Feed();

            $("#feed").bind('scrollstart', function() {
                if (isAtBottom()) {
                    feed.getFeed();
                }
            });
        }

        if (data.prevPage.attr("id") != "blog") {
            feed.resetFeed();
            feed.getFeed();
        }
        
    });

    $(document).on('pageshow', '#blog', function(event, data) {

        if (blog == null) {
            blog = new Blog();
        }
        
        var guid = decodeURIComponent($.mobile.pageData.guid);
        var owner = decodeURIComponent($.mobile.pageData.owner);
        var title = decodeURIComponent($.mobile.pageData.title);
        
        blog.resetBlogContent();
        blog.getBlog(guid, owner, title);
    });


    /*
     * Contacts Tab list/detail pages transition logic
     */
    $(document).on('pageshow', '#contacts', function(event, data) {

        if (contacts == null) {
            contacts = new Contacts();
        }
        contacts.getMembersByProgram("all");

    });

    $(document).on('pageshow', '#member', function(event, data) {

        var avatar = decodeURIComponent($.mobile.pageData.avatar);
        var name = decodeURIComponent($.mobile.pageData.name);
        var username = decodeURIComponent($.mobile.pageData.username);

        contacts.resetMemberProfile();
        contacts.getMemberProfile(username, avatar, name);
    });


    /*
     * Info Page list/detail pages transition logic
     */
    $(document).on('pageshow', '#info', function(event, data) {

        if (info == null) {
            info = new Info();
        }
        info.loadCategoryList();
    });

    $(document).on('pageshow', '#category', function(event, data) {

        if (data.prevPage.attr("id") == "info") {

            var category = decodeURIComponent($.mobile.pageData.category);
            info.resetPostsByCategory();
            info.getPostsByCategory(category);
            
            $("#category").bind('scrollstart', function() {
                if (isAtBottom()) {
                    info.getPostsByCategory(category);
                }
            });
        }

    });

}

function isAtBottom() {

    var contentOffsetTop, visibleHeight, totalHeight;

    if (document.documentElement.scrollTop) {
        contentOffsetTop = document.documentElement.scrollTop;
    } else {
        contentOffsetTop = document.body.scrollTop;
    }

    visibleHeight = document.documentElement.clientHeight;
    totalHeight = document.body.scrollHeight;

    return (totalHeight <= contentOffsetTop + visibleHeight);
}