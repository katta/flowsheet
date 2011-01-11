package org.openmrs.module.flowsheet;

import java.text.SimpleDateFormat;
import java.util.*;

import org.openmrs.api.context.Context;

public class Flowsheet {

	private final List<FlowsheetEntry> entries;

    private final Map<Integer,ConceptInfo> conceptMap;
    private List<String> conceptClasses = null;
    private final List<String> obsDates = new ArrayList<String>();
    private SimpleDateFormat format = Context.getDateFormat();
    private String datePattern;
	public Flowsheet(List<FlowsheetEntry> flowsheetEntries) {
        this.entries = flowsheetEntries;
        this.conceptMap = computeConceptMap(flowsheetEntries);
        this.datePattern=format.toPattern();
    }

    public Flowsheet(List<FlowsheetEntry> flowsheetEntries,List<String> conceptClasses,List<Date> obsDates) {
        this(flowsheetEntries);
        this.conceptClasses = conceptClasses;
        for(Date obsDate :obsDates){
            this.obsDates.add(format.format(obsDate));
        }
    }

	public List<FlowsheetEntry> getEntries() {
		return entries;
	}

    public Map<Integer, ConceptInfo> getConceptMap(){
        return conceptMap;
    }

    private Map<Integer,ConceptInfo> computeConceptMap(List<FlowsheetEntry> flowsheetEntries){

       Map<Integer, ConceptInfo> conceptMap = new HashMap<Integer, ConceptInfo>();

       for(FlowsheetEntry entry : flowsheetEntries){
           if(!conceptMap.containsKey(entry.getConceptId())){
               conceptMap.put(entry.getConceptId(),new ConceptInfo(entry.returnObs()));
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

    public String getDatePattern(){
        return datePattern;
    }

}
