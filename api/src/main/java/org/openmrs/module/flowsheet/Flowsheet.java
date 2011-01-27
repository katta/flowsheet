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

    public Flowsheet(List<FlowsheetEntry> flowsheetEntries) {
        this.entries = flowsheetEntries;
        this.conceptMap = computeConceptMap(flowsheetEntries);
    }

    public Flowsheet(List<FlowsheetEntry> flowsheetEntries, List<String> conceptClasses, List<Date> obsDates) {
        this(flowsheetEntries);
        this.conceptClasses = conceptClasses;
        for (Date obsDate : obsDates) {
            this.obsDates.add(Context.getDateFormat().format(obsDate));
        }
    }

    public List<FlowsheetEntry> getEntries() {
        return entries;
    }

    public Map<String, ConceptInfo> getConceptMap() {
        return conceptMap;
    }

    private Map<String, ConceptInfo> computeConceptMap(List<FlowsheetEntry> flowsheetEntries) {

        Map<String, ConceptInfo> conceptMap = new HashMap<String, ConceptInfo>();

        for (FlowsheetEntry entry : flowsheetEntries) {
            if (!conceptMap.containsKey(entry.getConceptId())) {
                conceptMap.put(entry.getConceptId().toString(), new ConceptInfo(entry.returnObs()));
            }
        }
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
