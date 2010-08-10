package org.openmrs.module.flowsheet.gwt.client;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.module.flowsheet.gwt.client.model.UIDetailedData;
import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This is the Asynchronous service interface for the Flowsheet Service
 * 
 * @author umashanthi
 * 
 */
public interface FlowsheetServiceAsync {
	/**
	 * 
	 * @param patientId
	 *            PatientId of the patient whose history is to be retrieved
	 * @param startDate
	 *            Start date of the observation to retrieve
	 * @param endDate
	 *            End date of the observation to retrieve
	 * @param conceptIds
	 *            Set of conceptClass types
	 * @param startIndex
	 *            Start index of the observations to return
	 * @param endIndex
	 *            End index of the observations to return
	 * @param callback
	 *            Asynchrnous callback object
	 * @return A list of UIObs according to the passed in parameters
	 */

	public void getPatientObsData(String patientId, Date startDate,
			Date endDate, Set<Integer> conceptIds, Integer startIndex,
			Integer endIndex, AsyncCallback<List<UIObs>> callback);

	/**
	 * 
	 * @param patientId
	 *            PatientId of the patient whose history is to be retrieved
	 * @param startDate
	 *            Start date of the observation to count
	 * @param endDate
	 *            End date of the observation to count
	 * @param conceptClassIds
	 *            Set of conceptClass types
	 * @param callback
	 *            Asynchrnous callback object
	 * @return The number of observations for the give patientId matching the
	 *         filter parameters
	 */

	public void getObsCount(String patientId, Date startDate, Date endDate,
			Set<Integer> conceptClassIds, AsyncCallback<Integer> callback);

	/**
	 * 
	 * @param patiendId
	 *            PatientId of the patient whose data is to be retrieved
	 * 
	 * @param callback
	 *            Asynchrnous callback object
	 * @return returns the start and end dates of the observation history of a
	 *         patient
	 */

	public void getDateRange(String patientId, AsyncCallback<Date[]> callback);

	/**
	 * 
	 * @param conceptId
	 *            Id of the Concept the observation relates to
	 * @param callback
	 *            Asynchrnous callback object
	 * @return The array of conceptName, unit, MinValue and MaxValue
	 */

	public void getDataForNumericValueHistory(Integer conceptId,
			AsyncCallback<String[]> callback);

	/**
	 * 
	 * @param patientId
	 *            PatientId of the patient whose data is to be retrieved
	 * @param conceptId
	 *            The conceptId of the Concept the Observation belongs to
	 * @param startDate
	 *            Start date of the observation to retrieve
	 * @param endDate
	 *            End date of the observation to retrieve
	 * @param callback
	 *            Asynchrnous callback object
	 * 
	 * @return
	 */

	public void getDetailedHistory(String patientId, Integer conceptId,
			Date startDate, Date endDate,
			AsyncCallback<UIDetailedData[]> callback);

	/**
	 * 
	 * @param patientId
	 *            PatientId of the patient whose data is to be retrieved
	 * @param date
	 *            Date of the observation
	 * @param conceptId
	 *            Id of the Concept the observation relates to
	 * @param callback
	 *            Asynchrnous callback object
	 * @return The patiendIdentifier, concpetName and conceptDescription of the
	 *         Observation for the passed parameters
	 */

	public void getPatientObsDetails(String patientId, Date date,
			Integer conceptId, AsyncCallback<String[]> callback);

	/**
	 * 
	 * @param patientId
	 *            PatientId of the patient whose data is to be retrieved
	 * @param startDate
	 *            Start date of the observation
	 * @param endDate
	 *            End date of the observation
	 * @param callback
	 *            Asynchrnous callback object
	 * @return List of ConceptClass and their ids
	 */
	public void getConceptClassList(String patientId, Date startDate,
			Date endDate, AsyncCallback<String[][]> callback);

	/**
	 * 
	 * @param obsId
	 *            Id of the observation to return
	 * @param callback
	 *            Asynchrnous callback object
	 * @return The observations for the obsId as a UIObs objcet
	 */

	public void getObsDetails(Integer obsId, AsyncCallback<UIObs> callback);
}
