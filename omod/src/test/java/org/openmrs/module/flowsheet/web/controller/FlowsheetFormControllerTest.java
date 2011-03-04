package org.openmrs.module.flowsheet.web.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.flowsheet.Flowsheet;
import org.openmrs.module.flowsheet.web.controller.FlowsheetFormController;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.ui.ModelMap;


public class FlowsheetFormControllerTest extends BaseModuleContextSensitiveTest {

    protected static final String INITIAL_DATA_XML = "org/openmrs/module/flowsheet/web/controller/flowsheetFormControllerTestDataSet.xml";

    @Before
    public void setUp() throws Exception {
        executeDataSet(INITIAL_DATA_XML);
    }

    @Test
    public void shouldReturnFlowsheetForPatient() {
        ModelMap map = new ModelMap();
        new FlowsheetFormController().loadFlowsheet(7, map);
        Flowsheet flowsheet = (Flowsheet) map.get("flowsheet");

        Assert.assertEquals(9, flowsheet.getEntries().size());
    }

    @Test
    public void shouldReturnFlowsheetSnapshotForPatient() {
        ModelMap map = new ModelMap();
        new FlowsheetFormController().loadFlowsheetSnapshot(7, map);
        Flowsheet flowsheet = (Flowsheet) map.get("flowsheet");

        Assert.assertEquals(7, flowsheet.getEntries().size());
    }

    @Test
    public void shouldReturnFlowsheetSnapshotForPatientForLastTwoDates() {
        ModelMap map = new ModelMap();
        new FlowsheetFormController().loadFlowsheetSnapshot(8, map);
        String flowsheetJson = (String) map.get("flowsheetJson");
        Assert.assertEquals(
                "{\"flowsheet\":{\"conceptClasses\":[\"Test\"],\"conceptMap\":{\"5090\":{\"classType\":\"Test\"" +
                        ",\"dataType\":\"Complex\",\"desc\":null,\"imageId\":\"22\",\"name\":\"Leg Xray\",\"numeric\":nul" +
                        "l,\"shortName\":\"\",\"synonyms\":[]}},\"datePattern\":\"dd/MM/yyyy\",\"entries\":[{\"comment" +
                        "\":\"SampleComment\",\"conceptId\":5090,\"date\":\"19/08/2008\",\"rowNumber\":1,\"value\":\"jp" +
                        "eg image\"},{\"comment\":\"SampleComment\",\"conceptId\":5090,\"date\":\"18/08/2008\",\"rowN" +
                        "umber\":2,\"value\":\"jpeg image\"}],\"obsDates\":[\"17/08/2008\",\"18/08/2008\",\"19/08/200" +
                        "8\"]}}", flowsheetJson);
    }

    @Test
    public void shouldReturnFlowsheetJsonForPatient() {
        ModelMap map = new ModelMap();
        new FlowsheetFormController().loadFlowsheet(8, map);
        String flowsheetJson = (String) map.get("flowsheetJson");
        Assert.assertEquals(
                "{\"flowsheet\":{\"conceptClasses\":null,\"conceptMap\":{\"5090\":{\"classType\":\"Test\",\"da" +
                        "taType\":\"Complex\",\"desc\":null,\"imageId\":\"23\",\"name\":\"Leg Xray\",\"numeric\":null,\"s" +
                        "hortName\":\"\",\"synonyms\":[]}},\"datePattern\":\"dd/MM/yyyy\",\"entries\":[{\"comment\":\"S" +
                        "ampleComment\",\"conceptId\":5090,\"date\":\"19/08/2008\",\"rowNumber\":1,\"value\":\"jpeg " +
                        "image\"},{\"comment\":\"SampleComment\",\"conceptId\":5090,\"date\":\"18/08/2008\",\"rowNumbe" +
                        "r\":2,\"value\":\"jpeg image\"},{\"comment\":\"SampleComment\",\"conceptId\":5090,\"date\":\"1" +
                        "7/08/2008\",\"rowNumber\":3,\"value\":\"jpeg image\"}],\"obsDates\":[]}}", flowsheetJson);


    }
}
