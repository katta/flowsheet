<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:htmlInclude file="/moduleResources/flowsheet/grid.locale-en.js"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/jquery.jqGrid.min.js"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/date.js"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/flowsheet.js"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/ui.multiselect.js"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/fcbkcomplete_2_75.js"/>


<openmrs:htmlInclude file="/moduleResources/flowsheet/ui.jqgrid.css"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/fcbkStyles_2_75.css"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/jslider.css"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/uicustom.css"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/jquery.dependClass.js"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/jquery.slider-min.js"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/excanvas.js"/>
<openmrs:htmlInclude file="/moduleResources/flowsheet/jquery.blockUI.js"/>


<input type="hidden" id="patientId" name="patientId" value='<request:parameter name="patientId" />'/>
<div id="loading" class="loading" loaded="false"><img src="moduleResources/flowsheet/loading.gif" alt="Loading..."/></div>
<table class="table_group" id="table_group" style="display:none">

	<tr>
        <td class="flowsheet_left_panel">
            <table>
                <tr>
                    <td>
                        <div id="dateRangeText" class="slider_title"> Date Range</div>
                        <div id="dateFrom" class="slider_info"> From : <label id="sliderInfoFrom"></label></div>
                        <div id="dateTo" class="slider_info"> To : <label id="sliderInfoTo"></label></div>

                        <div class="layout-slider">
                             <span class="slider">
                            <input id="Slider1" type="slider" name="price"/></span>
                        </div>
                    </td>

                </tr>
                <tr>
                    <td>

                        <div id="classTypeList">
                            <div class="slider_title">Result Types</div>
                            <a href="#" id="selectAll">Select All </a>
                            &nbsp;
                            <a href="#" id="clearAll">Clear All </a>
                        </div>

                    </td>
                </tr>
                <tr align="left">
                    <td>
                        <div class="searchPanel">
                            <div class="slider_title">Multiple Concept Name Search</div>
                            <select id="conceptSelect" name="conceptSelect"> </select>
                        </div>
                    </td>
                </tr>
            </table>

        </td>
        <td class="flowsheet_grid" id="flowsheet_grid">
			<table id="flowsheet_grid_div" class="flowsheet">
            </table>
            <div id="pageScroller" style="display:none;"></div>
		</td>
	</tr>
</table>
<div id="obsInfoDialog" class="">
    <div id="obsInfo" class="obsInfoPanel">
        <div id="maximizeIcon" class="maximizeIcon ui-icon ui-icon-arrowthick-2-ne-sw"></div>
        <div id="obsInfoLabel" class="obsInfoLabel"></div>
        <div id="conceptDesc" class="conceptDesc"></div>
        <div id="numericObsGraph" class="obsGraph"></div>
        <div id="numericObsGraphLegend" class="obsGraphLegend"></div>
        <div id="obsInfoGrid" class="obsInfoGrid">
        </div>
    </div>
</div>
    <div id="imageDialog" title="Image">
    </div>


<script type="text/javascript">
    
    jQuery("#imageDialog").dialog({
             bgiframe: true, autoOpen: false, width:'auto', height:'auto', modal: true
           });

      var loadImage = function(imgPath){
           jQuery("#imageDialog").html("<img src='"+imgPath+"'/>");
           jQuery('#imageDialog').dialog('open');
    }


     jQuery(window).load(function(){
        var patientIdValue = $j('#patientId').val();
        var jsondata = {
            patientId : patientIdValue
        };

        var flowsheetObj = new Flowsheet("flowsheet_grid_div","#pageScroller");
        var data = {};
        var classes = new ConceptClass("#classTypeList");
        var obsInfo = new ObsInfo("#obsInfo", "#obsInfoGrid", "#numericObsGraph",
                "#numericObsGraphLegend", "#obsInfoLabel", "#maximizeIcon", "#obsInfoDialog");

        var filter = function() {
            var from = jQuery('#sliderInfoFrom').text();
            var to = jQuery('#sliderInfoTo').text();
            var list = getSearchEntries();
            var range = new DateObject(from, to);
            var entries = data.filter(range, classes.getSelected(), list);
            conceptNameSearch.render(data.entries, filter);
            flowsheetObj.reload(entries);
            flowsheetObj.createErrorMessage(entries);
        }

        var dateRange = new DateRange(jQuery("#Slider1"), filter);
        var conceptNameSearch ;


        var onClickHandlerForGrid = function(rowid, iCol, cellcontent, e) {
            e.stopPropagation();
            var conceptId = jQuery("#flowsheet_grid_div").find("#"+rowid).find('td:nth-child(5)').html();
            if(data.isConceptComplex(conceptId)==null){
            jQuery('#obsInfoDialog').show();
            var searchResult = data.searchForConceptId(conceptId);
            obsInfo.reload(searchResult,jQuery("#flowsheet_grid_div").find("#"+rowid),data.datePattern);
            obsInfo.setConceptDesc("#conceptDesc",data.getConceptDesc(conceptId));
            }else{
                jQuery('#obsInfoDialog').hide();
            }
        }

        jQuery("#obsInfoDialog").click(function(){return false});

         jQuery("body").click(function() {
            obsInfo.hide();
        });


        jQuery("#maximizeIcon").click(function(e) {
            e.stopPropagation();
            var conceptId = jQuery("#maximizeIcon").attr("conceptId");
            var searchResult = data.searchForConceptId(conceptId);
            obsInfo.reloadInExpandedMode(searchResult);
        });


        renderflowsheet = function(json) {
            data = new FlowsheetData(json);
            jQuery("#loading").attr("loaded","true")
            flowsheetObj.render(data.entries, onClickHandlerForGrid, data.datePattern);
            classes.render(data.getConceptClasses());
            classes.change(filter);
            classes.attachSelectClearAll(filter);
            dateRange.render(data.getDateRange());
            jQuery('#table_group').show();
         	conceptNameSearch = new ConceptNameSearch(jQuery("#conceptSelect"));
            conceptNameSearch.render(data.entries, filter);
			jQuery('#loading').hide();
            waitMsg('#flowsheet_grid_div');
            setTimeout(fetchFlowsheetCompleteData, 10);

        };
        
        renderflowsheetComplete = function(json){
        	data.updateData(json);
        	flowsheetObj.reload(data.entries);
            conceptNameSearch.render(data.entries, filter);
            stopWaiting('#flowsheet_grid_div');
        }

        var fetchFlowsheetCompleteData = function(){
            $j.ajax({
                url : "flowsheet.json",
                data : jsondata,
                success : renderflowsheetComplete,
                dataType : "json"
            });
        	
        }

      var fetchFlowsheetSnapShot = function(){
                  $j.ajax({
                        url : "flowsheetSnapshot.json",
                        data : jsondata,
                        success : renderflowsheet,
                        dataType : "json"
                    });

            }

          //start up call - calls on refresh/on load
          if((jQuery('#loading').is(':visible')) && (jQuery("#loading").attr("loaded")=="false")){
                fetchFlowsheetSnapShot();
        }

        //the Ajax call has to be made on tab click also
        jQuery("#flowsheetTab").click(function(){
        var loaded=jQuery("#loading").attr("loaded")
        if(loaded=="false"){
                  fetchFlowsheetSnapShot();
             }
           });

        var getSearchEntries = function() {
            var list = []
            jQuery(".holder").children('li').each(function(index) {
                var text = jQuery(this).text();
                if (text.length > 0) {
                    list.push(text);
                }
            });
            return list
        }
    });
</script>

