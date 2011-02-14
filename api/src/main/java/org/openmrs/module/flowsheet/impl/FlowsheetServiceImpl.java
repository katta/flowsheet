package org.openmrs.module.flowsheet.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.flowsheet.ConceptInfo;
import org.openmrs.module.flowsheet.Flowsheet;
import org.openmrs.module.flowsheet.FlowsheetEntry;

public class FlowsheetServiceImpl implements FlowsheetService {
	private SessionFactory factory;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	

	@SuppressWarnings("unchecked")
	public Flowsheet getFlowsheet(int personId) {
		Map<String,ConceptInfo> conceptMap =new HashMap<String,ConceptInfo>();
		List<Obs> obsList = Context.getObsService().getObservationsByPerson(
				new Person(personId));
		List<FlowsheetEntry> flowsheetEntries = new ArrayList<FlowsheetEntry>();
		int rowNumber = 0;
		for (Obs obs : obsList) {
			conceptMap.put(obs.getConcept().getConceptId().toString(),new ConceptInfo(obs));
			flowsheetEntries.add(new FlowsheetEntry(++rowNumber, obs));
		}
		return new Flowsheet(flowsheetEntries,conceptMap);
	}

	public Flowsheet getFlowsheetSnapshot(int personId) {

		Map<String,ConceptInfo> conceptMap =new HashMap<String,ConceptInfo>(); 
		List<String> conceptClasses = getConceptClassess(personId);
		List<Date> obsDates = getObsDates(personId);
		List<FlowsheetEntry> flowsheetEntries = new ArrayList<FlowsheetEntry>();
		String firstDate = obsDates.size() > 0?"'" + format.format(obsDates.get(obsDates.size() - 1))+ "'":"''";
		String secondDate = obsDates.size() > 1?"'" + format.format(obsDates.get(obsDates.size() - 2))+ "'":"''";
		List<Obs> obsList = getObsList(personId, firstDate, secondDate);
		int rowNumber = 0;
		for (Obs obs : obsList) {
			flowsheetEntries.add(new FlowsheetEntry(++rowNumber, obs));
			conceptMap.put(obs.getConcept().getConceptId().toString(),new ConceptInfo(obs));
		}
		return new Flowsheet(flowsheetEntries, conceptClasses, formatObservationDates(obsDates),conceptMap);
	}

	private List<String> formatObservationDates(List<Date> obsDates) {
		List<String> obsDateStrings = new ArrayList<String>();
		for (Date obsDate : obsDates) {
			obsDateStrings.add(Context.getDateFormat().format(obsDate));
        }
		return obsDateStrings;
	}

	private List<Obs> getObsList(int personId, String firstDate,
			String secondDate) {
		Query query2 = factory.getCurrentSession().createQuery(
				"select o1 from Obs o1 where o1.personId = :id and o1.voided = 0 "
						+ "and o1.obsDatetime =" + firstDate
						+ " or o1.obsDatetime =" + secondDate + "");
		query2.setInteger("id", personId);
		return query2.list();
	}

	private List<Date> getObsDates(int personId) {
		Query query1 = factory
				.getCurrentSession()
				.createQuery(
						"select distinct o.obsDatetime from Obs o "
								+ "where o.personId = :id and o.voided = 0 order by o.obsDatetime asc");//
		query1.setInteger("id", personId);
		return query1.list();
	}

	private List<String> getConceptClassess(int personId) {
		Query query = factory.getCurrentSession().createQuery(
				"select distinct c1.conceptClass.name from Concept c1, Obs o1 "
						+ "where " + "c1.conceptId = o1.concept.conceptId and "
						+ "o1.personId = :id and " + "o1.voided = 0 ");
		query.setInteger("id", personId);
		return query.list();
	}

	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}
}
