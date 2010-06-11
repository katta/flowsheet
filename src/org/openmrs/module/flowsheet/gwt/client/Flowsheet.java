package org.openmrs.module.flowsheet.gwt.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Flowsheet implements EntryPoint {
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label resultLabel = new Label();
	private FlexTable table = new FlexTable();
	private Slider slider = new Slider();
	private VerticalPanel subPanel = new VerticalPanel();
	GroupingStore<ObsData> obsList;

	public void onModuleLoad() {

		// // dataLabel.setText("Enter Patient Id:  ");
		// // inputPanel.add(dataLabel);
		// // inputPanel.add(newTextBox);
		// // inputPanel.add(addButton);
		// // mainPanel.add(inputPanel);
		// slider.setMaxValue(100);
		// slider.setMessage("slider");
		// mainPanel.add(slider);
		// mainPanel.add(resultLabel);
		// table.setBorderWidth(2);
		// table.getElement().getStyle().setBorderColor("blue");
		// table.getElement().getStyle().setBackgroundColor("#E0E0F0");
		// mainPanel.add(table);
		// // RootPanel.get("webapp").add(mainPanel);
		// // addButton.addClickHandler(new ClickHandler() {
		// //
		// // public void onClick(ClickEvent arg0) {
		 getResponse();
		 RootPanel.get("webapp").add(mainPanel);
		// //
		// // }
		// //
		// // });
/*
		obsList = new GroupingStore<ObsData>();
		getResponse();
		obsList.groupBy("date");
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("date");
		column.setHeader("Date");
		column.setWidth(200);
		configs.add(column);

		column = new ColumnConfig("conceptName", "Concept", 150);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);

		column = new ColumnConfig("value", "ObsValue", 150);
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column);

		column = new ColumnConfig("drug", "Drug", 100);
		column.setAlignment(HorizontalAlignment.RIGHT);
		;
		configs.add(column);

		final ColumnModel cm = new ColumnModel(configs);
		GroupingView view = new GroupingView();
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				String f = cm.getColumnById(data.field).getHeader();
				String l = data.models.size() == 1 ? "Item" : "Items";
				return f + ": " + data.group + " (" + data.models.size() + " "
						+ l + ")";
			}
		});

		Grid<ObsData> grid = new Grid<ObsData>(obsList, cm);
		grid.setView(view);
		grid.setBorders(true);
		ContentPanel cp = new ContentPanel();
		cp.setBodyBorder(false);
		cp.setHeading("Patient Data");
		cp.setButtonAlign(HorizontalAlignment.CENTER);
		cp.setLayout(new FitLayout());
		cp.setSize(700, 420);
		cp.add(grid);
		RootPanel.get("webapp").add(cp);*/

	}

	private void getResponse() {
		String patientId = com.google.gwt.user.client.Window.Location
				.getParameter("patientId");
		FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
		AsyncCallback<PatientObsCollection[]> callback = new AsyncCallback<PatientObsCollection[]>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			public void onSuccess(PatientObsCollection[] result) {
				//obsList.add(getObsData(result));
				populateData(result);
			}
		};
		serviceAsync.getObsData(patientId, callback);
	}

	private List<ObsData> getObsData(String[] result) {
		List<ObsData> res = new ArrayList<ObsData>();
		for (String s : result) {
			String[] entry = s.split("#");
			ObsData obs = new ObsData();
			obs.set("date", entry[0]);
			obs.set("conceptName", entry[1]);
			obs.set("value", entry[2]);
			obs.set("drug", entry[3]);
			res.add(obs);
		}
		return res;
	}

	private void populateData(PatientObsCollection[] data) {
		// table.setText(0, 0, "Encounter Date");
		// table.setText(0, 1, "Concept Name");
		// table.setText(0, 2, "Comment");
		// table.getWidget(0, 0).getElement().getStyle()
		// .setBackgroundColor("silver");
		// table.getWidget(0, 1).getElement().getStyle()
		// .setBackgroundColor("silver");
		// table.getWidget(0, 2).getElement().getStyle()
		// .setBackgroundColor("silver");
		int index = 1, iteration = 0;
		String date = "";
		for (PatientObsCollection s : data) {
			table=new FlexTable();
			table.setBorderWidth(0);
			table.setText(0, 0, s.getEncounterDateTime().toString());
			index=1;
			if(s.getQuestions()!=null && s.getQuestions().length>0){
				table.setText(index++, 0, "Questions");
				for(PatientObs obs:s.getQuestions()){
				table.setText(index++, 0, obs.getConceptName()+"  "+obs.getObsValue());
				}
			}
			if(s.getTests()!=null && s.getTests().length>0){
				//table.getWidget(index, 0).getElement().getStyle().setBackgroundColor("silver");
				table.setText(index++, 0, "Tests");
				for(PatientObs obs:s.getTests()){
				table.setText(index++, 0, obs.getConceptName()+"  "+obs.getObsValue());
				}
			}
			if(s.getDrugs()!=null && s.getDrugs().length>0){
				table.setText(index++, 0, "Drugs");
				for(PatientObs obs:s.getDrugs()){
				table.setText(index++, 0, obs.getConceptName()+"  "+obs.getObsValue());
				for(String drug: obs.getDrugs()){
					table.setText(index++, 0, drug);
				}
				}
			}
			if(s.getMisc()!=null && s.getMisc().length>0){
				table.setText(index++, 0, "Tests");
				for(PatientObs obs:s.getMisc()){
				table.setText(index++, 0, obs.getConceptName()+"  "+obs.getObsValue());
				}
			}
			subPanel=new VerticalPanel();
			subPanel.add(table);
			subPanel.setBorderWidth(1);
			mainPanel.add(subPanel);
			
			/*String[] entry = s.split("#");

			if (iteration == 0) {
				// first time
				table = new FlexTable();
				table.setBorderWidth(0);
				table.setText(0, 0, entry[0]);
				table.setText(index++, 0, entry[1] + "  -  " + entry[2]);
				table.setText(index++, 0, entry[3]);
				date = entry[0];
				iteration++;
			} else {
				if (date.equals(entry[0])) {
					table.setText(index++, 0, entry[1] + "  -  " + entry[2]);
					table.setText(index++, 0, entry[3]);

				} else {
					subPanel.add(table);
					subPanel.setBorderWidth(1);
					mainPanel.add(subPanel);
					subPanel = new VerticalPanel();
					date = entry[0];
					index = 1;
					iteration++;
					table = new FlexTable();
					table.setBorderWidth(0);
					table.setText(0, 0, entry[0]);
					table.setText(index++, 0, entry[1] + "  -  " + entry[2]);
					table.setText(index++, 0, entry[3]);
				}
			}*/

		}
	}

	// final Button sendButton = new Button("Send");
	// final TextBox nameField = new TextBox();
	// nameField.setText("GWT User");
	// final Label errorLabel = new Label();
	//
	// // We can add style names to widgets
	// sendButton.addStyleName("sendButton");
	//
	// // Add the nameField and sendButton to the RootPanel
	// // Use RootPanel.get() to get the entire body element
	// RootPanel.get("nameFieldContainer").add(nameField);
	// RootPanel.get("sendButtonContainer").add(sendButton);
	// RootPanel.get("errorLabelContainer").add(errorLabel);
	//
	// // Focus the cursor on the name field when the app loads
	// nameField.setFocus(true);
	// nameField.selectAll();
	//
	// // Create the popup dialog box
	// final DialogBox dialogBox = new DialogBox();
	// dialogBox.setText("Remote Procedure Call");
	// dialogBox.setAnimationEnabled(true);
	// final Button closeButton = new Button("Close");
	// // We can set the id of a widget by accessing its Element
	// closeButton.getElement().setId("closeButton");
	// final Label textToServerLabel = new Label();
	// final HTML serverResponseLabel = new HTML();
	// VerticalPanel dialogVPanel = new VerticalPanel();
	// dialogVPanel.addStyleName("dialogVPanel");
	// dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
	// dialogVPanel.add(textToServerLabel);
	// dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
	// dialogVPanel.add(serverResponseLabel);
	// dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
	// dialogVPanel.add(closeButton);
	// dialogBox.setWidget(dialogVPanel);
	//
	// // Add a handler to close the DialogBox
	// closeButton.addClickHandler(new ClickHandler() {
	// public void onClick(ClickEvent event) {
	// dialogBox.hide();
	// sendButton.setEnabled(true);
	// sendButton.setFocus(true);
	// }
	// });
	//
	// // Create a handler for the sendButton and nameField
	// class MyHandler implements ClickHandler, KeyUpHandler {
	// /**
	// * Fired when the user clicks on the sendButton.
	// */
	// public void onClick(ClickEvent event) {
	// sendNameToServer();
	// }
	//
	// /**
	// * Fired when the user types in the nameField.
	// */
	// public void onKeyUp(KeyUpEvent event) {
	// if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	// sendNameToServer();
	// }
	// }
	//
	// /**
	// * Send the name from the nameField to the server and wait for a response.
	// */
	// private void sendNameToServer() {
	// // First, we validate the input.
	// errorLabel.setText("");
	// String textToServer = nameField.getText();
	//				
	//
	// // Then, we send the input to the server.
	// sendButton.setEnabled(false);
	// textToServerLabel.setText(textToServer);
	// serverResponseLabel.setText("");
	// FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
	// final String SERVER_ERROR="server Error";
	// serviceAsync.getObsData("3",
	// new AsyncCallback<String>() {
	// public void onFailure(Throwable caught) {
	// // Show the RPC error message to the user
	// dialogBox
	// .setText("Remote Procedure Call - Failure");
	// serverResponseLabel
	// .addStyleName("serverResponseLabelError");
	// serverResponseLabel.setHTML(SERVER_ERROR);
	// dialogBox.center();
	// closeButton.setFocus(true);
	// }
	//
	// public void onSuccess(String result) {
	// dialogBox.setText("Remote Procedure Call");
	// serverResponseLabel
	// .removeStyleName("serverResponseLabelError");
	// serverResponseLabel.setHTML(result);
	// dialogBox.center();
	// closeButton.setFocus(true);
	// }
	// });
	// }
	// }
	//
	// // Add a handler to send the name to the server
	// MyHandler handler = new MyHandler();
	// sendButton.addClickHandler(handler);
	// nameField.addKeyUpHandler(handler);
}
