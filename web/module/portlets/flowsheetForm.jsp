<%@page import="org.openmrs.api.ObsService"%>
<%@page import="org.openmrs.api.context.Context"%>
<%@page import="java.util.*"%>
<%@page import="org.openmrs.Obs"%>
<%@page import="org.openmrs.Patient"%>



<%
	String patientId = request.getParameter("patientId");
	ObsService obsService = Context.getObsService();
	List<Person> patient = new ArrayList<Person>();
	patient.add(new Patient(Integer.valueOf(patientId)));
	List<String> sortOder = new ArrayList<String>();
	sortOder.add("obsId");
	List<Obs> observations = obsService.getObservations(patient, null,
			null, null, null, null, sortOder, null, null, null, null,
			true);
%>

<%@page import="org.openmrs.Person"%><table>
	<tr>
		<th>Obs Id</th>
		<th>Created Date</th>
		<th>Concept Name</th>
		<th>Comment</th>
	</tr>
	<%
		for (Obs obs : observations) {
	%>
	<tr>
		<td align="center">
		<%
			out.println(obs.getId().toString());
		%>
		</td>
		<td>
		<%
			out.println(obs.getDateCreated().toString());
		%>
		</td>
		<td>
		<%
			out.println(obs.getConcept().getName());
		%>
		</td>

		<td>
		<%
			out.println(obs.getComment());
		%>
		</td>
	</tr>
	<%
		}
	%>


</table>