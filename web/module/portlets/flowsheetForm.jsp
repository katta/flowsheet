<!doctype html>
<!-- The DOCTYPE declaration above will set the    -->
<!-- browser's rendering engine into               -->
<!-- "Standards Mode". Replacing this declaration  -->
<!-- with a "Quirks Mode" doctype may lead to some -->
<!-- differences in layout.                        -->

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">

    <!--                                                               -->
    <!-- Consider inlining CSS to reduce the number of requested files -->
    <!--           
                                                        -->
   <link rel="stylesheet" type="text/css" href="/openmrs/moduleResources/flowsheet/gxt-all.css" />
    <link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/flowsheet/Sample.css">
   
    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>Web Application Starter Project</title>
    
    <!--                                           -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                -->
    <!--                                           -->
    <script type="text/javascript" language="javascript" src="/openmrs/moduleResources/flowsheet/mywebapp/mywebapp.nocache.js"></script>
        <script language='javascript' src='/openmrs/moduleResources/flowsheet/flash/swfobject.js'></script>  
  </head>

  <!--                                           -->
  <!-- The body can have arbitrary html, or      -->
  <!-- you can leave the body empty if you want  -->
  <!-- to create a completely dynamic UI.        -->
  <!--                                           -->
  <body>
 <openmrs:require privilege="View Patients" otherwise="/login.htm" redirect="/admin/index.htm" />
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>
	
<!--    <h2>~Patient Data</h2>-->
<div id="loading" align="center"> 
       <h2 style="color: #000080;">Loading.....</h2>
       <img alt="" src="/openmrs/moduleResources/flowsheet/loading.gif">
    </div> 
    <div id="webapp" align="center"></div>
<!--	<table align="center" style="table">-->
<!--      <tr>-->
<!--        <td colspan="2" style="font-weight:bold;"></td>        -->
<!--      </tr>-->
<!--      <tr>-->
<!--        <td id="webapp"></td>-->
<!--      </tr>-->
<!--      <tr>-->
<!--        <td colspan="2" style="color:red;" id="errorLabelContainer"></td>-->
<!--      </tr>-->
<!--    </table>-->
    
  </body>
</html>