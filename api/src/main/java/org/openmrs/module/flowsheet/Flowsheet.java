package org.openmrs.module.flowsheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.api.context.Context;

public class Flowsheet {

    private List<FlowsheetEntry> entries;

    private Map<String, ConceptInfo> conceptMap;
    private List<String> conceptClasses = null;
    private List<String> obsDates = new ArrayList<String>();
    private String datePattern = Context.getDateFormat().toPattern();

    public Flowsheet() {
        this.entries = new ArrayList<FlowsheetEntry>();
        this.conceptMap = new HashMap<String, ConceptInfo>();
    }

    public Flowsheet(List<FlowsheetEntry> flowsheetEntries, Map<String, ConceptInfo> conceptMap) {
        this.entries = flowsheetEntries;
        this.conceptMap =conceptMap;
    }

    public Flowsheet(List<FlowsheetEntry> flowsheetEntries, List<String> conceptClasses, List<String> obsDates,Map<String, ConceptInfo> conceptMap) {
        this(flowsheetEntries,conceptMap);
        this.conceptClasses = conceptClasses;
        this.obsDates = obsDates;
        
    }

    public List<FlowsheetEntry> getEntries() {
        return entries;
    }

    public Map<String, ConceptInfo> getConceptMap() {
        return conceptMap;
    }


    public List<String> getConceptClasses() {
        return conceptClasses;
    }

    public List<String> getObsDates() {
        return obsDates;
    }

    public String getDatePattern() {
        return datePattern;
    }
}
