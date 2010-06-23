package org.openmrs.module.flowsheet.gwt.client;

import java.util.Date;
import java.util.List;

import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	DateField startDatePicker = new DateField();
	final Label text = new Label();
	DateField endDatePicker = new DateField();
	Label label1 = new Label("Select Start Date");
	Label label2 = new Label("Select End Date");
	FlexTable conceptList = new FlexTable();
	
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
		// Left Panel
		leftPanel.add(label1);
		leftPanel.add(startDatePicker);
		leftPanel.add(label2);
		leftPanel.add(endDatePicker);
		Button filter = new Button("Filter");
		filter.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				bottomPanel.remove(rightPanel);
				rightPanel = new VerticalPanel();
				Date startDate = startDatePicker.getValue();
				Date endDate = endDatePicker.getValue();
				if (startDate != null && endDate != null) {
					getResponse(startDate, endDate);
				} else {
					getResponse(null, null);
				}

			}

		});
		
		leftPanel.add(conceptList);
		leftPanel.add(filter);
		leftPanel.setHeight("100%");
		leftPanel.setWidth("10%");
		leftPanel.setStyleName("leftPanel");
		leftPanel.add(resultLabel);
		bottomPanel.add(leftPanel);
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
//		AsyncCallback<Map<Integer, String>> getListCallback = new AsyncCallback<Map<Integer, String>>() {
//			public void onFailure(Throwable caught) {
//				resultLabel.setText(caught.getMessage());
//			}
//
//			public void onSuccess(Map<Integer, String> concepts) {
//				int index=0;
//				for (Integer id : concepts.keySet()) {
//					conceptList.setText(index++, 0, concepts.get(id));
//				}
//
//			}
//		};
//		serviceAsync.getConceptList(getListCallback);

		endDatePicker.addListener(Events.Change,
				new Listener<ComponentEvent>() {

					public void handleEvent(ComponentEvent be) {
						String d = DateTimeFormat.getShortDateFormat().format(
								endDatePicker.getValue());
						getResponse(null, endDatePicker.getValue());
					}

				});
		startDatePicker.addListener(Events.KeyPress,
				new Listener<ComponentEvent>() {

					public void handleEvent(ComponentEvent be) {
						String d = DateTimeFormat.getShortDateFormat().format(
								startDatePicker.getValue());
						getResponse(startDatePicker.getValue(), null);
					}

				});

		bottomPanel.setWidth("100%");
		rightPanel.setWidth("70%");
		// bottomPanel.add(rightPanel);
		bottomPanel.setBorderWidth(1);
		mainPanel.add(bottomPanel);
		mainPanel.setWidth("100%");
		RootPanel.get("webapp").add(mainPanel);
		getResponse(null, null);
	}

	private void getResponse(Date startDate, Date endDate) {
		String patientId = com.google.gwt.user.client.Window.Location
				.getParameter("patientId");
		FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
		AsyncCallback<List<UIObs>> callback = new AsyncCallback<List<UIObs>>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			public void onSuccess(List<UIObs> result) {
				showData(result);
			}
		};
		serviceAsync.getPatientObsData(patientId, startDate, endDate, callback);
	}

	private void showData(List<UIObs> data) {
		int index = 0;
		Date obsDate = null;
		Date dateOfLastObs = data.get(0).getObsDateTime();
		Date dateObsStarted = null;
		if (bottomPanel.getWidgetIndex(rightPanel) >= 0) {
			bottomPanel.remove(rightPanel);
		}
		rightPanel = new VerticalPanel();
		for (UIObs obs : data) {
			index = 0;

			table = new FlexTable();
			subPanel = new VerticalPanel();

			table.setWidth("100%");
			if (obsDate == null
					|| !(obsDate.toString().equals(obs.getObsDateTime()
							.toString()))) {
				dateLabel = new Label();
				dateLabel.setText(obs.getObsDateTime().toString().substring(0,
						10));

				dateLabel.getElement().getStyle().setBackgroundColor("#8FABC7");
				dateLabel.getElement().getStyle().setFontSize(12, Unit.PT);
				dateLabel.getElement().getStyle()
						.setFontStyle(FontStyle.NORMAL);
				subPanel.add(dateLabel);
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
			}

			if (obs.getConceptDescription() != null) {
				table.setText(index++, 0, obs.getConceptDescription());
			}

			subPanel.add(table);
			// subPanel.setBorderWidth(1);
			subPanel.setWidth("100%");
			rightPanel.setWidth("100%");
			rightPanel.add(subPanel);
			obsDate = obs.getObsDateTime();
		}
		dateObsStarted = obsDate;
		bottomPanel.add(rightPanel);
	}

}
