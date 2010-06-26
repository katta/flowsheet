package org.openmrs.module.flowsheet.gwt.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Flowsheet implements EntryPoint {
	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label resultLabel = new Label();
	private FlexTable table = new FlexTable();
	private FormPanel subPanel = new FormPanel();
	private VerticalPanel topPanel = new VerticalPanel();
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private Label topLabel = new Label();
	DateField startDatePicker = new DateField();
	final Label text = new Label();
	DateField endDatePicker = new DateField();
	Label label1 = new Label("Select Start Date");
	Label label2 = new Label("Select End Date");
	ListBox conceptList = new ListBox(true);
	private Integer[] patientConcepts;
	CheckBoxGroup conceptGroup = new CheckBoxGroup();
	CheckBox conceptCheckBox = new CheckBox();
	FormPanel formPanel = new FormPanel();
	FormData formData = new FormData();
	private String[] patientConceptNames;
	private List<Integer> selectedIds = new ArrayList<Integer>();
	private boolean endDate = false;
	private boolean startDate = false;
	private int sendConceptCount = 0;
	private int currentConceptCount = 0;

	public void onModuleLoad() {
		// Top Panel
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
		// End of Top Panel
		formPanel = new FormPanel();
		formPanel.setFrame(true);
		formPanel.setHeading("Filtering Options");
		formPanel.setLabelWidth(100);
		// Left Panel
		startDatePicker.setFieldLabel("Select Start Date");
		formPanel.add(startDatePicker);
		endDatePicker.setFieldLabel("Select End Date");
		formPanel.add(endDatePicker);
		startDatePicker.getDatePicker().addListener(Events.Select,
				new Listener<ComponentEvent>() {

					public void handleEvent(ComponentEvent be) {
						startDate = true;
						if (endDate) {
							getResponse(startDatePicker.getDatePicker()
									.getValue(), endDatePicker.getDatePicker()
									.getValue(), null);
						} else {
							getResponse(startDatePicker.getDatePicker()
									.getValue(), null, null);

						}
					}

				});
		endDatePicker.getDatePicker().addListener(Events.Select,
				new Listener<ComponentEvent>() {

					public void handleEvent(ComponentEvent be) {
						endDate = true;
						if (startDate) {
							getResponse(startDatePicker.getDatePicker()
									.getValue(), endDatePicker.getDatePicker()
									.getValue(), null);
						} else {
							getResponse(null, endDatePicker.getDatePicker()
									.getValue(), null);

						}
					}

				});

		formPanel.add(new Label("Filter By Concept"));
		conceptList.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				selectedIds
						.add(patientConcepts[conceptList.getSelectedIndex()]);
				getResponse(null, null, selectedIds);
			}

		});

		formPanel.add(conceptList);
		Button selectAllConceptsButton = new Button("Select All");

		selectAllConceptsButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				int totItems = conceptList.getItemCount();
				selectedIds = new ArrayList<Integer>();
				for (int index = 0; index < totItems; index++) {
					conceptList.setItemSelected(index, true);
					// selectedIds.add(patientConcepts[index]);
				}
				getResponse(null, null, null);
			}

		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(selectAllConceptsButton);
		Button conceptFilterClearButton = new Button("Clear");
		conceptFilterClearButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				selectedIds = new ArrayList<Integer>();
			}

		});
		buttonPanel.setSpacing(10);
		buttonPanel.add(conceptFilterClearButton);
		formPanel.add(buttonPanel);
		leftPanel.add(formPanel);
		leftPanel.setHeight("100%");
		leftPanel.setStyleName("leftPanel");
		leftPanel.add(resultLabel);
		// bottomPanel.add(leftPanel);
		String patientId = com.google.gwt.user.client.Window.Location
				.getParameter("patientId");
		FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
		AsyncCallback<Date[]> callback = new AsyncCallback<Date[]>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			public void onSuccess(Date[] range) {
				if (range[0] != null) {
					startDatePicker.setMaxValue(range[0]);
					endDatePicker.setMaxValue(range[0]);
					endDatePicker.setValue(range[0]);

				}
				if (range[1] != null) {
					startDatePicker.setMinValue(range[1]);
					endDatePicker.setMinValue(range[1]);
					startDatePicker.setValue(range[1]);
				}
			}
		};
		serviceAsync.getDateRange(patientId, callback);
		AsyncCallback<Map<Integer, String>> getListCallback = new AsyncCallback<Map<Integer, String>>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			public void onSuccess(Map<Integer, String> concepts) {
				int index = 0;
				patientConcepts = new Integer[concepts.size()];
				patientConceptNames = new String[concepts.size()];
				for (Integer id : concepts.keySet()) {
					conceptList.addItem(concepts.get(id));
					patientConceptNames[index] = concepts.get(id);
					patientConcepts[index++] = id;

				}

			}
		};
		serviceAsync.getConceptList(patientId, getListCallback);
		bottomPanel.setWidth("100%");
		mainPanel.add(bottomPanel);
		mainPanel.setWidth("100%");
		RootPanel.get("webapp").add(mainPanel);
		getResponse(null, null, null);
	}

	private void getResponse(final Date startDate, final Date endDate,
			final List<Integer> conceptId) {
		String patientId = com.google.gwt.user.client.Window.Location
				.getParameter("patientId");
		if (conceptId != null) {
			sendConceptCount = conceptId.size();
		}
		FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
		AsyncCallback<List<UIObs>> callback = new AsyncCallback<List<UIObs>>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			public void onSuccess(List<UIObs> result) {
				currentConceptCount = selectedIds.size();
				if (conceptId == null
						|| sendConceptCount == currentConceptCount) {
					showData(result);
				} else {
					getResponse(startDate, endDate, selectedIds);
				}
			}
		};
		serviceAsync.getPatientObsData(patientId, startDate, endDate,
				conceptId, callback);
	}

	private void showData(List<UIObs> data) {
		int index = 0;
		Date obsDate = null;
		Date dateObsStarted = null;
		if (bottomPanel.getWidgetIndex(rightPanel) >= 0) {
			bottomPanel.remove(rightPanel);
		}
		rightPanel = new VerticalPanel();
		VerticalPanel inPanel = new VerticalPanel();
		for (UIObs obs : data) {

			if (obsDate == null
					|| !(obsDate.toString().equals(obs.getObsDateTime()
							.toString()))) {
				table = new FlexTable();
				inPanel = new VerticalPanel();
				subPanel = new FormPanel();
				subPanel.setWidth("100%");
				subPanel.setHeading(obs.getObsDateTime().toString().substring(
						0, 10));

				index = 0;
			}

			if (obs.getConcepts() != null
					&& obs.getConcepts().getDisplayName() != null) {
				String conceptName = obs.getConcepts().getDisplayName()
						.toLowerCase();
				char newChar = (char) (conceptName.charAt(0) - 32);
				conceptName = newChar + conceptName.substring(1);
				if (obs.getNumericValue() != null) {
					String unit = obs.getConcepts().getUnits();
					String text = conceptName + " - "
							+ obs.getNumericValue().toString() + " " + unit;

					if (obs.getConcepts().getHiNormal() != null
							&& obs.getNumericValue() >= obs.getConcepts()
									.getHiNormal()) {
						text = text + "            ***Greater than Hi-Normal";
						Label highLabel = new Label();
						highLabel.setStyleName("hiNormal");
						highLabel.setText(text);
						table.getCellFormatter().addStyleName(index, 0,
								"hiNormal");
						table.setText(index++, 0, text);
					} else {
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
				if (obs.getConceptDescription() != null) {
					table.setText(index++, 0, obs.getConceptDescription());
				}

			}
			if (obsDate == null
					|| !(obsDate.toString().equals(obs.getObsDateTime()
							.toString()))) {
				inPanel.add(table);
				subPanel.add(inPanel);
				subPanel.setWidth("100%");
				rightPanel.setWidth("100%");
				rightPanel.add(subPanel);

			}
			obsDate = obs.getObsDateTime();

		}
		dateObsStarted = obsDate;
		bottomPanel.add(leftPanel);
		bottomPanel.add(rightPanel);
	}

}
