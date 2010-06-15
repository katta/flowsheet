package org.openmrs.module.flowsheet.gwt.client;

import java.util.Date;
import java.util.List;

import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Flowsheet implements EntryPoint {
	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label resultLabel = new Label();
	private FlexTable table = new FlexTable();
	private VerticalPanel subPanel = new VerticalPanel();

	private Label dateLabel = new Label();
	private VerticalPanel topPanel = new VerticalPanel();
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private Label topLabel = new Label();
	private Label topLabelBottom = new Label();

	public void onModuleLoad() {
		topLabel.setText("Patient History");
		topLabel.setStyleName("topLabel");
		topLabel.setWidth("100%");
		topPanel.add(topLabel);
		topPanel.getElement().getStyle().setBorderColor("#8FABC7");
		// topPanel.setBorderWidth(1);
		topPanel.setWidth("100%");
		topPanel.setStyleName("topPanel");
		topPanel.setWidth("100%");
		mainPanel.add(topPanel);
		DatePicker datepicker = new DatePicker();
		leftPanel.add(datepicker);
		leftPanel.setHeight("100%");
		leftPanel.setWidth("30%");
		leftPanel.setStyleName("leftPanel");
		bottomPanel.add(leftPanel);
		bottomPanel.setWidth("100%");
		getResponse();
		rightPanel.setWidth("70%");
		bottomPanel.add(rightPanel);
		bottomPanel.setBorderWidth(1);
		mainPanel.add(bottomPanel);
		mainPanel.setWidth("100%");
		RootPanel.get("webapp").add(mainPanel);

	}

	private void getResponse() {
		String patientId = com.google.gwt.user.client.Window.Location
				.getParameter("patientId");
		FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
		AsyncCallback<List<UIObs>> callback = new AsyncCallback<List<UIObs>>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			// public void onSuccess(PatientObsCollection[] result) {
			// // obsList.add(getObsData(result));
			// populateData(result);
			//				
			// }

			public void onSuccess(List<UIObs> result) {
				// TODO Auto-generated method stub
				showData(result);
			}
		};
		serviceAsync.getPatientObsData(patientId, callback);
	}

	private void showData(List<UIObs> data) {
		int index = 0;
		Date obsDate = null;
		for (UIObs obs : data) {
			index = 0;
			table = new FlexTable();
			subPanel = new VerticalPanel();

			table.setWidth("100%");

			// table.setStyleName("table1");
			// table.setBorderWidth(0);
			if (obsDate == null
					|| !(obsDate.toString().equals(obs.getObsDateTime()
							.toString()))) {
				dateLabel = new Label();
				dateLabel.setText(obs.getObsDateTime().toString().substring(0,
						10));
				// dateLabel.setWidth("100%");
				dateLabel.getElement().getStyle().setBackgroundColor("#8FABC7");
				dateLabel.getElement().getStyle().setFontSize(12, Unit.PT);
				dateLabel.getElement().getStyle()
						.setFontStyle(FontStyle.NORMAL);
				subPanel.add(dateLabel);
			}

			// table.setText(index++, 0, obs.getObsId().toString());
			// table.setText(index++, 0, obs.getConcepts().getDisplayName());
			// table.setText(index++,0,obs.getCodedValue());
			// if(obs.getLocation()!=null){
			// table.setText(index++,0,obs.getLocation());
			// }
			if (obs.getConcepts() != null
					&& obs.getConcepts().getDisplayName() != null) {
				String conceptName = obs.getConcepts().getDisplayName()
						.toLowerCase();
				char newChar = (char) (conceptName.charAt(0) - 32);
				conceptName = newChar + conceptName.substring(1);
				if (obs.getNumericValue() != null) {
					String unit=obs.getConcepts().getUnits();
					String text=conceptName + " - "+ obs.getNumericValue().toString()+" "+unit;
					
					if(obs.getConcepts().getHiNormal()!=null && obs.getNumericValue()>=obs.getConcepts().getHiNormal()){
						text=text+"            ***Greater than Hi-Normal";
						Label highLabel=new Label();
						highLabel.setStyleName("hiNormal");
						highLabel.setText(text);
						table.getCellFormatter().addStyleName(index, 0, "hiNormal");
						table.setText(index++, 0, text);
					}
					else{
						table.setText(index++, 0, text);
					}
					
					
					
				}
				
				if (obs.getBooleanValue() != null) {
					if (obs.getBooleanValue()) {
						table.setText(index++, 0, conceptName + " - Yes");
					} else {
						table.setText(index++, 0, conceptName + " - No");

					}
				}
			}
			
			if (obs.getConceptDescription() != null) {
				table.setText(index++, 0, obs.getConceptDescription());
			}
			// if(obs.getConcepts().getAnswers()!=null){
			// for(String ans:obs.getConcepts().getAnswers()){
			// if(ans!=null){
			// table.setText(index++,0,ans);
			// }
			// }
			// }

			subPanel.add(table);
			// subPanel.setBorderWidth(1);
			subPanel.setWidth("100%");
			rightPanel.setWidth("100%");
			rightPanel.add(subPanel);
			obsDate = obs.getObsDateTime();
		}

	}

	private void populateData(PatientObsCollection[] data) {

		int index = 1;
		for (PatientObsCollection s : data) {
			table = new FlexTable();
			// table.getElement().getStyle().setBackgroundColor("#E0E0F0");
			table.setStyleName("table1");
			// table.setBorderWidth(0);
			dateLabel = new Label();
			dateLabel.setText(s.getEncounterDateTime().toString().substring(0,
					10));
			dateLabel.getElement().getStyle().setBackgroundColor("#8FABC7");
			dateLabel.getElement().getStyle().setFontSize(12, Unit.PT);
			dateLabel.getElement().getStyle().setFontStyle(FontStyle.NORMAL);

			// table.setText(0, 0, s.getEncounterDateTime().toString());
			index = 0;
			if (s.getQuestions() != null && s.getQuestions().length > 0) {
				table.setText(index++, 0, "Questions");
				for (PatientObs obs : s.getQuestions()) {
					table.setText(index++, 0, obs.getConceptName() + "  "
							+ obs.getObsValue());
				}
			}
			if (s.getTests() != null && s.getTests().length > 0) {
				// table.getWidget(index,
				// 0).getElement().getStyle().setBackgroundColor("silver");
				table.setText(index++, 0, "Tests");
				for (PatientObs obs : s.getTests()) {
					table.setText(index++, 0, obs.getConceptName() + "  "
							+ obs.getObsValue());
				}
			}
			if (s.getDrugs() != null && s.getDrugs().length > 0) {
				table.setText(index++, 0, "Drugs");
				for (PatientObs obs : s.getDrugs()) {
					table.setText(index++, 0, obs.getConceptName() + "  "
							+ obs.getObsValue());
					for (String drug : obs.getDrugs()) {
						table.setText(index++, 0, drug);
					}
				}
			}
			if (s.getMisc() != null && s.getMisc().length > 0) {
				table.setText(index++, 0, "Tests");
				for (PatientObs obs : s.getMisc()) {
					table.setText(index++, 0, obs.getConceptName() + "  "
							+ obs.getObsValue());
				}
			}
			subPanel = new VerticalPanel();
			dateLabel.setWidth("100%");
			table.setWidth("100%");
			subPanel.add(dateLabel);
			subPanel.add(table);
			// subPanel.setBorderWidth(1);
			subPanel.setWidth("100%");
			rightPanel.setWidth("100%");
			rightPanel.add(subPanel);

		}
	}

}
