/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function setLoginCredential(username, password) {
    window.localStorage.setItem("username", username);
    window.localStorage.setItem("password", sjcl.encrypt("password", password));
}

function getLoginCredential() {

    var credential = new Object();
    credential.username = window.localStorage.getItem("username");
    if (credential.username != null) {
        var password = window.localStorage.getItem("password");
        if (password != null) {
            credential.password = sjcl.decrypt("password", password);
            return credential;
        }
    }
    return null;
}

function clearUserCredential() {
    window.localStorage.clear();
}

function setToken(token) {
    window.localStorage.setItem("token", token);
}

function getToken() {
    return (window.localStorage.getItem("token"));
}

function invalidateAuthToken() {
    window.localStorage.removeItem("token");
}

function getAuthToken(params, onComplete) {

    var authToken = null;
    var credential = getLoginCredential();

    /* the user is not logged in, fetch token for login validation,
     otherwise the user is assumed logged in, fetch token for web service */
    if (credential == null) {
        credential = params;
    }

    $.ajax({
        url: 'http://elgg.cmu.edu.au/elgg/services/api/rest/?method=auth.gettoken',
        type: 'POST',
        dataType: "text",
        //dataType: 'json',
        data: {
            username: credential.username,
            password: credential.password
        },
        complete: function(response) {
            if (response.status == 0) {
                //ok(false, '0 status - browser could be on offline mode');
                return null;
            } else if (response.status == 404) {
                //ok(false, '404 error');
                return null;
            } else {

                var xmlDoc = $.parseXML(response.responseText);
                var $xml = $(xmlDoc);
                authToken = $xml.find("result").text();

                onComplete(authToken, params);

            }
        }
    });

}

function logout() {

    clearUserCredential();
    $.mobile.changePage("#user-login");
}

function validateLogin() {

    var credential = new Object();
    credential.username = $('input#username').val();
    credential.password = $('input#password').val();

    getAuthToken(credential, function(authToken, params) {

        if (authToken != '') {

            setLoginCredential(params.username, params.password);
            setToken(authToken);

            $.mobile.changePage('#feed');

        } else {
            alert('Invalid Login');
        }

    });

}
