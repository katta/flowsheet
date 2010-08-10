package org.openmrs.module.flowsheet.gwt.client;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.module.flowsheet.gwt.client.model.UIDetailedData;
import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface is the Service interface for the Flowsheet RPC service
 * 
 * @author umashanthi
 * 
 */

@RemoteServiceRelativePath("../../../moduleServlet/flowsheet/flowsheetService")
public interface FlowsheetService extends RemoteService {
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
	 * @return A list of UIObs according to the passed in parameters
	 */
	List<UIObs> getPatientObsData(String patientId, Date startDate,
			Date endDate, Set<Integer> conceptIds, Integer startIndex,
			Integer endIndex);

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
	 * @return The number of observations for the give patientId matching the
	 *         filter parameters
	 */
	Integer getObsCount(String patientId, Date startDate, Date endDate,
			Set<Integer> conceptClassIds);

	/**
	 * 
	 * @param patiendId
	 *            PatientId of the patient whose data is to be retrieved
	 * @return returns the start and end dates of the observation history of a
	 *         patient
	 */
	Date[] getDateRange(String patiendId);

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
	 * @return
	 */
	UIDetailedData[] getDetailedHistory(String patientId, Integer conceptId,
			Date startDate, Date endDate);

	/**
	 * 
	 * @param patientId
	 *            PatientId of the patient whose data is to be retrieved
	 * @param date
	 *            Date of the observation
	 * @param conceptId
	 *            Id of the Concept the observation relates to
	 * @return The patiendIdentifier, concpetName and conceptDescription of the
	 *         Observation for the passed parameters
	 */
	String[] getPatientObsDetails(String patientId, Date date, Integer conceptId);

	/**
	 * 
	 * @param conceptId
	 *            Id of the Concept the observation relates to
	 * @return The array of conceptName, unit, MinValue and MaxValue
	 */
	String[] getDataForNumericValueHistory(Integer conceptId);

	/**
	 * 
	 * @param patientId
	 *            PatientId of the patient whose data is to be retrieved
	 * @param startDate
	 *            Start date of the observation
	 * @param endDate
	 *            End date of the observation
	 * @return List of ConceptClass and their ids
	 */
	String[][] getConceptClassList(String patientId, Date startDate,
			Date endDate);

	/**
	 * 
	 * @param obsId
	 *            Id of the observation to return
	 * @return The observations for the obsId as a UIObs objcet
	 */
	UIObs getObsDetails(Integer obsId);
}
