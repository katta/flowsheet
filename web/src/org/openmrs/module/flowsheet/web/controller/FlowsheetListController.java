package org.openmrs.module.flowsheet.web.controller;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.Obs;
import org.openmrs.api.EncounterService;
import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class FlowsheetListController extends SimpleFormController {
	
    /** Logger for this class and subclasses */
    protected final Log log = LogFactory.getLog(getClass());
    
	/**
	 * 
	 * This is called prior to displaying a form for the first time.  It tells Spring
	 *   the form/command object to load into the request
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
    protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		List<Encounter> encounters = null;
		
		if (Context.isAuthenticated()) {
			EncounterService es = Context.getEncounterService();
			String patientId = request.getParameter("patientId");
	    	if (patientId != null) {
	    		encounters = es.getEncountersByPatientId(Integer.valueOf(patientId), false);
	    	}
		}
		
		if (encounters == null)
			encounters = new Vector<Encounter>();
    	
        return encounters;
    }

	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object obj, Errors error) throws Exception {
		
		List<Encounter> encounters = (List<Encounter>) obj;
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<Integer, List<Obs>> observationsMap = new HashMap<Integer, List<Obs>>();
		Map<Integer, Map<Integer, FormField>> obsMapMap = new HashMap<Integer, Map<Integer, FormField>>();
		Map<Integer, List<Integer>> editedObsMap = new HashMap<Integer, List<Integer>>();
		Map<Integer, Map<Integer, List<Obs>>> obsGroupsMap = new HashMap<Integer, Map<Integer, List<Obs>>>();
		
		for (Encounter encounter : encounters) {
			
			Integer encounterId = encounter.getEncounterId();
			
			List<Integer> editedObs = new Vector<Integer>();
	
			// The map returned to the form
			Map<Integer, FormField> obsMap = new HashMap<Integer, FormField>();
			// Used for sorting
			Map<Obs, FormField> obsMapTemp = new HashMap<Obs, FormField>();
			
			// temporary list to hold the sorted obs
			List<FormField> formFields = new Vector<FormField>();
			
			// stores a map from obs group id to all obs in that group
			Map<Integer, List<Obs>> obsGroups = new HashMap<Integer, List<Obs>>();
			
			// actual list of observations to loop over on display
			List<Obs> observations = new Vector<Obs>();
			
			Form form = encounter.getForm();
			
			if (Context.isAuthenticated()) {
				FormService fs = Context.getFormService();
				
				// loop over the encounter's observations to find the edited obs
				String reason = "";
				if (encounter.getObs() != null && !encounter.getObs().isEmpty()) {
					for (Obs o : encounter.getObs()) {
						// only the voided obs have been edited
						if (o.isVoided()){
							// assumes format of: ".* (new obsId: \d*)"
							reason = o.getVoidReason();
							int start = reason.lastIndexOf(" ") + 1;
							int end = reason.length() - 1;
							try {
								reason = reason.substring(start, end);
								editedObs.add(Integer.valueOf(reason));
							} catch (Exception e) {}
						}
						
						FormField ff = fs.getFormField(form, o.getConcept());
						if (ff == null) ff = new FormField();
						FormField parent = ff.getParent();
						
						Integer groupId = o.getObsGroupId();
						
						if (groupId == null && parent != null) {
							// if the obs wasn't marked as a group but the parent concept in the form is a set, treat as a grouped obs 
							Concept fieldConcept = null;
							if ((fieldConcept = parent.getField().getConcept()) != null && fieldConcept.isSet()) {
								groupId = o.getObsId();
								o.setObsGroupId(groupId);
							}
						}
						
						if (groupId != null) {
							
							if (!obsGroups.containsKey(groupId)) {
								obsGroups.put(groupId, new Vector<Obs>());
								
								// if this is the first in the group, add the parent FormField as its FormField 
								if (parent == null)
									log.error("Parent should not be null for obs with a group id obs id: " + o.getObsId() + " form field id: " + ff.getFormFieldId());
								
								formFields.add(parent);
								obsMap.put(o.getObsId(), parent);
								obsMapTemp.put(o, parent);
							}
							
							obsGroups.get(groupId).add(o);
							
						}
						else {
							// populate the obs map so we can 
							//  1) sort the obs according to FormField
							//  2) look up the formField by the obs object
							formFields.add(ff);
							obsMap.put(o.getObsId(), ff);
							obsMapTemp.put(o, ff);
						}
					}
					
					try {
						// sort the temp list according the the FormFields.compare() method
						Collections.sort(formFields, new FormFieldNameComparator());
					}
					catch (Exception e) {
						log.error("Error while sorting obs for encounter: " + encounter, e);
					}
					
					// loop over the sorted formFields to add the corresponding
					//  obs to the returned obs list
					for (FormField f : formFields) {
						Obs o = popObsFromMap(obsMapTemp, f);
						if (o != null)
							observations.add(o);
					}
				}
			}
			
			observationsMap.put(encounterId, observations);
			obsMapMap.put(encounterId, obsMap);
			editedObsMap.put(encounterId, editedObs);
			obsGroupsMap.put(encounterId, obsGroups);
		}
		
		map.put("observationsMap", observationsMap);
		map.put("obsMapMap", obsMapMap);
		map.put("editedObsMap", editedObsMap);
		map.put("obsGroupsMap", obsGroupsMap);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(OpenmrsConstants.OPENMRS_LOCALE_DATE_PATTERNS().get(Context.getLocale().toString().toLowerCase()), Context.getLocale());
		map.put("datePattern", dateFormat.toLocalizedPattern().toLowerCase());
		map.put("locale", Context.getLocale());
		
		return map;
	}
    
	/**
	 * Searches the given map for the given FormField
	 * 
	 * @param map
	 * @param f
	 * @return
	 */
	private Obs popObsFromMap(Map<Obs, FormField> map, FormField f) {
		for (Map.Entry<Obs, FormField> entry : map.entrySet()) {
			if (entry.getValue() == f) {
				Obs o = entry.getKey();
				map.remove(o);
				return o;
			}
		}
		
		return null;
	}
	
	/**
	 * Internal class used to sort FormField by number/part/name
	 */
	private class FormFieldNameComparator implements Comparator<FormField> {
		public int compare(FormField ff1, FormField ff2) {
			if (ff1.getFieldNumber() != null || ff2.getFieldNumber() != null) {
				if (ff1.getFieldNumber() == null)
					return -1;
				if (ff2.getFieldNumber() == null)
					return 1;
				int c = ff1.getFieldNumber().compareTo(ff2.getFieldNumber());
				if (c != 0)
					return c;
			}
			if (ff1.getFieldPart() != null || ff2.getFieldPart() != null) {
				if (ff1.getFieldPart() == null)
					return -1;
				if (ff2.getFieldPart() == null)
					return 1;
				int c = ff1.getFieldPart().compareTo(ff2.getFieldPart());
				if (c != 0)
					return c;
			}
			if (ff1.getField() != null && ff2.getField() != null) {
				int c = ff1.getField().getName().compareTo(ff2.getField().getName());
				if (c != 0)
					return c;
			}
			if (ff1.getFormFieldId() == null && ff2.getFormFieldId() != null)
				return -1;
			if (ff1.getFormFieldId() != null && ff2.getFormFieldId() == null)
				return 1;
			if (ff1.getFormFieldId() == null && ff2.getFormFieldId() == null)
				return 1;
			
			return ff1.getFormFieldId().compareTo(ff2.getFormFieldId());
		}
	}
	
}
	