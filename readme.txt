1. GWT-SDK
	Compiling this modules requires the GWT-SDK (Google Web Toolkit).
	You can get the latest version of GWT-SDK for your Operating System here:
	http://code.google.com/webtoolkit/download.html
	**Download the SDK and extract it to a convenient place
	**Give the extracted directory path to the gwt.sdk property in the build.properties 
	Currently this module uses GWT 2.0.3

2. Building the .omod file
   Follow the steps below to build the .omod file (the module)
  * Invoke the 'clean' Ant task
  * Invoke the 'gwtc' Ant task
  * This will create a directory named 'mywebapp' under flowsheet/war
  * Copy this 'mywebapp' directory to  flowsheet/web/module/resources
  * Then invoke the 'package-module' Ant task
  * Now the module will be packged under flowsheet/dist with the name 'flowsheet-1.0.omod'
  * You can deploy this module in the OpenMRS deployment
