/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var contacts = null;

function Contacts() {

    var self = this;

    this.getMembersByProgram = getMembersByProgram;
    this.loadMembers = loadMembers;

    this.resetMemberProfile = resetMemberProfile;
    this.getMemberProfile = getMemberProfile;
    this.loadMemberProfile = loadMemberProfile;

    /*
     * call web service to fetch @CMU members by program
     */
    function getMembersByProgram(prog) {

        var params = new Object();

        params.program = prog;

        var call =
                {
                    service: 'member.get_members_by_program',
                    type: 'GET',
                    opt: params,
                    callback: function(response) {
                        var jsonString = $.parseJSON(response.responseText);

                        if (jsonString.status == '-1') {

                            alert("member call failed");

                            if (jsonString.message == 'pam_auth_userpass:failed') {

                                invalidateAuthToken();
                                self.getMembersByProgram(program);
                            }

                        } else {

                            var result = jsonString.result;
                            //alert(response.responseText);
                            self.loadMembers(result);
                        }

                    }
                };

        webService(call);
    }

    /*
     * loads the members list to the listview
     */
    function loadMembers(result) {

        /* hsuanchc (uncomment this to test)
         $.each(result, function(key, value) {
         alert(key + ":" + value); 
         });
         */

        var html = "";

        for (var count = 0; count < result.count; count++) {
            html += "<li>";

            var username = encodeURIComponent(result[count].username);
            var avatar = encodeURIComponent(result[count].avatar);
            var name = encodeURIComponent(result[count].name);

            html += "<a href='#member?username=" + username + "&avatar=" + avatar + "&name=" + name + "'" + " data-transition='slide'>";

            // hsuanchc V2
            if (result[count].avatar) {
                html += "<img src=" + result[count].avatar + "/>";
            } else {
                html += "<img src='img/spacer2.jpg'/>";
            }

            html += "<h3>" + result[count].name + "</h3>";
            html += "</a>";
            html += "</li>";
        }

        $("#contacts-listview").html(html);
        $("#contacts-listview").listview("refresh");
    }


    /*
     * call web service to fetch @CMU member profile
     */
    function getMemberProfile(username, avatar, name) {

        var params = new Object();
        params.username = username;

        var call =
                {
                    service: 'member.get_profile',
                    type: 'GET',
                    opt: params,
                    callback: function(response) {

                        //alert(response.responseText);

                        var jsonString = $.parseJSON(response.responseText);
                        var result = jsonString.result;

                        self.loadMemberProfile(result, avatar, name);

                    }
                };

        webService(call);
    }

    /*
     * loads the member profile to the detail view
     */
    function loadMemberProfile(result, avatar, name) {

        /* hsuanchc (uncomment this to test)
         $.each(result, function(key, value) {
         alert(key + ":" + value);
         });
         */
        if (avatar == null) {
            avatar = "img/spacer2.jpg";
        }
        var header = "<div class='member-profile-header'><img id='member-profile-avatar' src=" + avatar + "/><h4 id='member-profile-name'>" + name + "</h4></div>";
        $("#member-content-basic").html(header);

        var html = '';

        if (result.admin_defined_profile_1 != '') {
            html += "<br/><div id='about-me'><h4 style='padding-left:15px;'>About Me</h4><div class='member-info' id='about-me-content'>";
            html += result.admin_defined_profile_1.replace(/^[\r\n]+|\.|[\r\n]+$/g, "") + "</div></div><br/>";
        }
        if (result.admin_defined_profile_2 != '') {
            html += "<div id='description'><h4 style='padding-left:15px;'>Description</h4><div class='member-info' id='description-content'>";
            html += "<p>" + result.admin_defined_profile_2 + "</p></div></div><br/>";
        }
        if (result.admin_defined_profile_3 != '') {
            html += "<div id='email'><h4 style='padding-left:15px;'>Email</h4><div class='member-info' id='email-content'>";
            html += "<p>" + result.admin_defined_profile_3 + "</p></div></div><br/>";
        }
        if (result.admin_defined_profile_4 != '') {
            html += "<div id='telephone'><h4 style='padding-left:15px;'>Telephone</h4><div class='member-info' id='telephone-content'>";
            html += "<p>" + result.admin_defined_profile_4 + "</p></div></div><br/>";
        }
        if (result.admin_defined_profile_5 != '') {
            html += "<div id='mobile'><h4 style='padding-left:15px;'>Mobile Phone</h4><div class='member-info' id='mobile-content'>";
            html += "<p>" + result.admin_defined_profile_5 + "</p></div></div><br/>";
        }
        if (result.admin_defined_profile_7 != '') {
            html += "<div id='location'><h4 style='padding-left:15px;'>Location</h4><div class='member-info' id='location-content'>";
            html += "<p>" + result.admin_defined_profile_7 + "</p></div></div><br/>";
        }
        if (result.admin_defined_profile_8 != '') {
            html += "<div id='interests'><h4 style='padding-left:15px;'>Interests</h4><div class='member-info' id='interests-content'>";
            html += "<p>" + result.admin_defined_profile_8 + "</p></div></div><br/>";
        }
        if (result.admin_defined_profile_9 != '') {
            html += "<div id='skills'><h4 style='padding-left:15px;'>Skills</h4><div class='member-info' id='skills-content'>";
            html += "<p>" + result.admin_defined_profile_9 + "</p></div></div><br/>";
        }
        if (result.admin_defined_profile_10 != '') {
            html += "<div id='twitter'><h4 style='padding-left:15px;'>Twitter</h4><div class='member-info' id='twitter-content'>";
            html += "<p>" + result.admin_defined_profile_10 + "</p></div></div><br/>";
        }

        $("#member-content-detail").html(html);
    }

    /*
     * resets the content of the member profile page
     */
    function resetMemberProfile() {

        $("#member-content-basic").html("");
        $("#member-content-detail").html("");
    }
}

function filterByType(type) {

    contacts.getMembersByProgram(type);
}