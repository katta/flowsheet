package org.openmrs.module.flowsheet;

import java.text.SimpleDateFormat;

import org.openmrs.Obs;
import org.openmrs.api.context.Context;

public class FlowsheetEntry {

    private SimpleDateFormat format = Context.getDateFormat();
    private Obs obs;
    private int rowNumber;

    public FlowsheetEntry(int rowNumber, Obs obs) {
        this.rowNumber = rowNumber;
        this.obs = obs;
    }


    public String getValue() {
        return obs.getValueAsString(Context.getLocale());
    }

    public String getDate() {
        return format.format(obs.getObsDatetime());
    }

    public Integer getConceptId() {
        return obs.getConcept().getConceptId();
    }

    public Obs returnObs() {
        return obs;
    }

    public String getComment() {
        return obs.getComment() == null ? "" : obs.getComment();
    }

    public int getRowNumber() {
        return rowNumber;
    }
}