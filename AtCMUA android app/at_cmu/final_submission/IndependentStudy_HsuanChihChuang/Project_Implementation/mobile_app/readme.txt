
@CMU Mobile Application


Installation instruction:

1.
Setup the Apache Cordova build environment. The setup instrcution is available here:
http://cordova.apache.org/docs/en/3.3.0/guide_cli_index.md.html#The%20Command-Line%20Interface


2.
Create a Cordova application project, and replace the "www" folder under the project root with the 
"www" folder here. This application requires no additional Cordova plugins.


3.
For test purposes, the current mobile application is accessing web services provided by the 
"elgg.cmu.edu.au/elgg/" test instance (this is the fake @CMU portal created for test purposes). 
The official @CMU instance is "at.cmu.edu.au/elgg/". This means that once the web services plugin is
deployed onto the official @CMU instance, 2 changes need to be made to the mobile application source code:

  - Replace "elgg.cmu.edu.au/elgg/" with "at.cmu.edu.au/elgg/" in line 54 of file "www/js/authentication.js".
	
	- Replace "elgg.cmu.edu.au/elgg/" with "at.cmu.edu.au/elgg/" in line 37 of file "www/js/webservice.js".
	
	
4.
Build the mobile application code and run on device/emulator.