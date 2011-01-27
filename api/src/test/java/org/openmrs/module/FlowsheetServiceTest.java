package org.openmrs.module;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.flowsheet.ConceptInfo;
import org.openmrs.module.flowsheet.Flowsheet;
import org.openmrs.module.flowsheet.FlowsheetEntry;
import org.openmrs.module.flowsheet.impl.FlowsheetService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class FlowsheetServiceTest extends BaseModuleContextSensitiveTest {

    private FlowsheetService service;
    private FlowsheetEntry entry;
    Flowsheet flowsheet;
     protected static final String INITIAL_DATA_XML = "org/openmrs/module/flowsheetTestDataSet.xml";

	private List<FlowsheetEntry> getFlowSheetEntry(int personId) {
		return service.getFlowsheet(personId).getEntries();
	}

	@Before
	public void setUp() throws Exception {
        executeDataSet(INITIAL_DATA_XML);
		service = Context.getService(FlowsheetService.class);
		entry = getFlowSheetEntry(7).get(0);
        flowsheet = service.getFlowsheet(7);
        Context.setLocale(Locale.ENGLISH);
	}

    @Test
    public void shouldReturnObservationsForPerson() {
        Assert.assertEquals(0, getFlowSheetEntry(1).size());
        Assert.assertEquals(13, getFlowSheetEntry(7).size());
    }

    @Test
    public void shouldReturnConceptShortNameForEachObservation() {
        Assert.assertEquals("WT", flowsheet.getConceptMap().get("5089").getShortName());
    }

    @Test
    public void shouldReturnConceptNameForEachObservation() {
        Assert.assertEquals("WEIGHT (KG)", flowsheet.getConceptMap().get("5089").getName());
    }

    @Test
    public void shouldReturnSynonymsForEachObservation() {
        Assert.assertEquals(0, flowsheet.getConceptMap().get("5089").getSynonyms().size());
    }

	@Test
	public void shouldReturnValueForEachObservation() {
		Assert.assertEquals("61.0", entry.getValue());
	}

	@Test
	public void shouldReturnDataTypeForEachObservation() {
		Assert.assertEquals("Numeric", flowsheet.getConceptMap().get("5089").getDataType());
	}

	@Test
	public void shouldReturnClassTypeForEachObservation() {
		Assert.assertEquals("Test", flowsheet.getConceptMap().get("5089").getClassType());
	}

    @Test
    public void shouldReturnDateForEachObservation() {
        SimpleDateFormat targetformat=Context.getDateFormat();
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = format.parse("2008-08-19");
            Assert.assertEquals(targetformat.format(date), entry.getDate());
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

	@Test
	public void shouldReturnUnitForEachObservation() throws Exception {
		Assert.assertEquals("kg", flowsheet.getConceptMap().get("5089").getNumeric().getUnit());
	}

	@Test
	public void shouldReturnCommentObservation() throws Exception {
		FlowsheetEntry entry = getFlowSheetEntry(7).get(12);
		Assert.assertEquals("SampleComment", entry.getComment());
	}

    @Test
    @Ignore("Add additional data set to test this")
    public void shouldReturnHiValueEachObservation() throws Exception {
        Assert.assertEquals("250.0", flowsheet.getConceptMap().get("5089").getNumeric().getHi().toString());
    }

    @Test
    @Ignore("Add additional data set to test this")
    public void shouldReturnHiLowAsEmpty() throws Exception {
        Assert.assertEquals("250.0", flowsheet.getConceptMap().get("5089").getNumeric().getHi());
        Assert.assertEquals("", flowsheet.getConceptMap().get("5089").getNumeric().getLow());
    }

    @Test
    public void shouldReturnConceptDesc() {
        Map<String, ConceptInfo> conceptMap = flowsheet.getConceptMap();
        Assert.assertEquals("Patient's weight in kilograms.", conceptMap.get("5089").getDesc());
        Assert.assertEquals("Measure of CD4 (T-helper cells) in blood", conceptMap.get("5497").getDesc());
    }

    @Test
    public void shouldReturnImageIdForComplexType() {
        Map<String, ConceptInfo> conceptMap = flowsheet.getConceptMap();
        Assert.assertEquals("17", conceptMap.get("5090").getImageId());
        Assert.assertEquals(null, conceptMap.get("5089").getImageId());
    }
}
