
The web_service folder contains the implementation of the @CMU web services.


The directory structure of this folder is as follows:

web_services
      |
      |
      |------ patches
      |
      |------ plugins
                 |
                 |------- apiadmin
                 |  
                 |------- atcmu_web_services




Installation instructions:

1. 
The "patches" directory contains a patch which needs to applied to the elgg engine library to fix API authentication errors.
Copy "web_services.php" under "<host_document_root>/elgg/engine/lib/" (ie, /var/www/elgg/engine/lib/) to replace the original 
"web_services.php" (you might want to back up the original file instead of simply replacing it).


2. 
The "plugins" directory contains 2 plugins:


"apiadmin":
	 
- The "apiadmin" plugin is required to generate an API key for the web service client.
	Copy the "apiadmin" folder under "<host_document_root>/elgg/mod/" (ie, /var/www/elgg/mod/), and activate the plugin 
	in the "Administration -> Configure -> Plugins" page of @CMU. Detail instructions on how to do this is can be found 
	in section 2.3 ELGG Plugin Development of the project report.
	
- Once the plugin is activated, the site administration can generate API keys under "Administer -> Utilities -> API Key Admin" tab.
  The generated API key is required by the web service client in order to access web services provided by @CMU. To do this, copy
	the API key to line 23 & line 39 of "mobile_app/www/js/webservice.js" file. The "mobile_app" folder is where the source code for
	the @CMU mobile application is located. Rebuild the source code and run the mobile application to test that the API is valid.
		 


"atcmu_web_services":
		 
- The "atcmu_web_services" folder contains the implementation of the @CMU web services plugin.
	Copy the "atcmu_web_services" folder under "<host_document_root>/elgg/mod/" (ie, /var/www/elgg/mod/), and activate the plugin
	in the "Administration -> Configure -> Plugins" page of @CMU. Detail instructions on how to do this is can be found
	in section 2.3 ELGG Plugin Development of the project report.