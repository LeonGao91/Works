/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function webService(call) {

	var authToken = getToken();
	
	if (authToken != null) {
		// test if token is expired
		callWebService(authToken, call);
	} else {
		getAuthToken(call, callWebService);
	}
	
}

function callWebService(authToken, call) {
	
	var data = {
		api_key : '67316cc2468f62b15ce35797e643195588c45b36',
		auth_token : authToken,
	};
	
	var opt = call.opt;
	$.each(opt, function(key, value) {
		data[key] = value;
	});
	
	/* hsuanchc DEBUG 
	$.each(data, function(key, value) {
		alert("key:" + key + "   value:" + value);
	});
	*/
        var baseURL = 'http://elgg.cmu.edu.au/elgg/services/api/rest/json/';
	baseURL = baseURL.concat('?method=' + call.service);
	baseURL = baseURL.concat('&api_key=' + '67316cc2468f62b15ce35797e643195588c45b36');
	baseURL = baseURL.concat('&auth_token=' + authToken);
	
	$.ajax({
		url : baseURL,
		type : call.type,
		dataType : "application/json",
		data : data,
		complete : function(response) {
			if (response.status == 0) {
				//ok(false, '0 status - browser could be on offline mode');
				return null;
			} else if (response.status == 404) {
				//ok(false, '404 error');
				return null;
			} else {
				call.callback(response);
			}
		}
	});

}
