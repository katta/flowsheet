package org.openmrs.module.flowsheet.gwt.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import org.openmrs.module.flowsheet.gwt.client.model.UIDetailedData;
import org.openmrs.module.flowsheet.gwt.client.model.UIObs;
import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.ToolTip;
import com.extjs.gxt.charts.client.model.ToolTip.MouseStyle;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.LineChart;
import com.extjs.gxt.charts.client.model.charts.dots.Star;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Flowsheet implements EntryPoint {
	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label resultLabel = new Label();
	private Grid<ObsDataModel> table = null;
	private VerticalPanel subPanel = new VerticalPanel();
	private VerticalPanel topPanel = new VerticalPanel();
	private DockPanel bottomPanel = new DockPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private Label topLabel = new Label();
	private CheckBoxGroup conceptGroup = new CheckBoxGroup();
	private CheckBox conceptCheckBox = new CheckBox();
	private FormPanel formPanel = new FormPanel();
	private FormData formData = new FormData();
	private int sendConceptCount = 0;
	private int currentConceptCount = 0;
	private UIDetailedData[] numericData = null;
	private LayoutContainer lchart = null;
	private Slider2Bar slider = null;
	private Label selectedDatesLabel = new Label();
	private String[][] conceptTypeData = null;
	private String patientId;
	private Set<Integer> selectedConnceptTypes = new HashSet<Integer>();
	private boolean isClearSelected = false;
	private boolean isSelectAllSelected = false;
	private FlowsheetServiceAsync service = null;
	private String[] patientObsDetailArray = null;

	public void onModuleLoad() {
		
		
		patientId = com.google.gwt.user.client.Window.Location
				.getParameter("patientId");
		// Top Panel
		topLabel.setText("Patient History");
		topLabel.setStyleName("topLabel");
		topLabel.setWidth("100%");
		topPanel.add(topLabel);
		topPanel.getElement().getStyle().setBorderColor("#8FABC7");
		topPanel.setWidth("100%");
		topPanel.setStyleName("topPanel");
		topPanel.setWidth("100%");
		mainPanel.add(topPanel);
		// End of Top Panel
		// Left side Form Panel
		formPanel = new FormPanel();
		formPanel.setFrame(true);
		formPanel.setHeading("Filtering Options");
		formPanel.setCollapsible(false);
		formPanel.setLabelWidth(100);
		Label dateRangeLabel = new Label("Result Dates");
		dateRangeLabel.setStyleName("label1");
		formPanel.add(dateRangeLabel);
		HorizontalPanel dateLablesPanel = new HorizontalPanel();
		dateLablesPanel.setSpacing(5);
		selectedDatesLabel.setStyleName("label2");
		dateLablesPanel.add(selectedDatesLabel);
		formPanel.add(dateLablesPanel);
		service = GWT.create(FlowsheetService.class);
		AsyncCallback<String[][]> callback1 = new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			public void onSuccess(String[][] result) {
				conceptTypeData = result;
				final CheckBoxGroup group = new CheckBoxGroup();
				group.setOrientation(Orientation.VERTICAL);
				group.setHideLabel(true);
				int index = 0;
				for (String entry : result[0]) {
					CheckBox c1 = new CheckBox();
					c1.setBoxLabel(result[1][index++]);
					c1.setLabelStyle("checkboxLabel");
					c1.setValue(true);
					c1.setValueAttribute((result[0][index - 1]).toString());
					selectedConnceptTypes.add(Integer.valueOf(c1
							.getValueAttribute()));
					c1.addListener(Events.Change, new Listener<BaseEvent>() {

						@Override
						public void handleEvent(BaseEvent be) {

							CheckBox option = (CheckBox) be.getSource();

							if (option.getValue()) {
								selectedConnceptTypes.add(Integer
										.valueOf(option.getValueAttribute()));
								if (!(isClearSelected || isSelectAllSelected)) {
									rightPanel.clear();
									RootPanel.get("loading").setVisible(true);
									getResponse(slider.getCurrent1DateValue(), slider.getCurrent2DateValue(),
											selectedConnceptTypes);
								}
							} else {
								selectedConnceptTypes.remove(Integer
										.valueOf(option.getValueAttribute()));
								if (!(isClearSelected || isSelectAllSelected)) {
									rightPanel.clear();
									RootPanel.get("loading").setVisible(true);
									getResponse(slider.getCurrent1DateValue(), slider.getCurrent2DateValue(),
											selectedConnceptTypes);
								}
							}
						}

					});
					group.add(c1);
				}
				Label resultTypeLabel = new Label("Result Types");
				resultTypeLabel.setStyleName("label1");
				formPanel.add(resultTypeLabel);
				Anchor selectAllAnchor = new Anchor("Select All");
				selectAllAnchor.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						isSelectAllSelected = true;
						List<Field<?>> options = (List<Field<?>>) group
								.getAll();
						for (Field<?> option : options) {
							CheckBox opt = (CheckBox) option;
							opt.setValue(true);
							selectedConnceptTypes.add(Integer.valueOf(opt
									.getValueAttribute()));
						}
						rightPanel.clear();
						RootPanel.get("loading").setVisible(true);
						getResponse(slider.getCurrent1DateValue(), slider.getCurrent2DateValue(), selectedConnceptTypes);
						isSelectAllSelected = false;
					}

				});

				Anchor clearAnchor = new Anchor("Clear");
				clearAnchor.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						isClearSelected = true;
						List<CheckBox> options = group.getValues();
						for (CheckBox option : options) {
							option.setValue(false);
							selectedConnceptTypes.remove(Integer.valueOf(option
									.getValueAttribute()));
						}
						rightPanel.clear();
						isClearSelected = false;
					}

				});

				HorizontalPanel anchorPanel = new HorizontalPanel();
				anchorPanel.setSpacing(5);
				anchorPanel.add(selectAllAnchor);
				anchorPanel.add(new Label(" | "));
				anchorPanel.add(clearAnchor);
				formPanel.add(anchorPanel);
				formPanel.add(group);
			}
		};
		service.getConceptClassList(patientId, null, null, callback1);
		leftPanel.add(formPanel);
		leftPanel.setHeight("100%");
		leftPanel.add(resultLabel);

		FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
		AsyncCallback<Date[]> callback = new AsyncCallback<Date[]>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			public void onSuccess(Date[] range) {

				slider = new Slider2Bar(range[1].getTime(), range[0].getTime(),
						(43200), true);

				slider.setCurrent1Value(range[1].getTime(), true);
				slider.setCurrent2Value(range[0].getTime(), true);
				String startDate = slider.getCurrent1DateValue().toString();
				String endDate = slider.getCurrent2DateValue().toString();
				selectedDatesLabel.setText(startDate.substring(4, 10)
						+ startDate.substring(27) + "                       "
						+ endDate.substring(4, 10) + endDate.substring(27));
				slider.setNumTicks(0);
				slider.setNumLabels(2);
				slider.setLineWidth(200);
				slider.setHeight("20px");
				slider.setWidth("500px");
				slider.setNotifyOnMouseUp(true);
				slider.getKnob2Image().addMouseUpHandler(new MouseUpHandler() {
					public void onMouseUp(MouseUpEvent arg0) {
						Slider2Bar bar = slider;
						String startDate = bar.getCurrent1DateValue()
								.toString();
						String endDate = bar.getCurrent2DateValue().toString();
						selectedDatesLabel.setText(startDate.substring(4, 10)
								+ startDate.substring(27)
								+ "                       "
								+ endDate.substring(4, 10)
								+ endDate.substring(27));
						rightPanel.clear();
						RootPanel.get("loading").setVisible(true);
						getResponse(bar.getCurrent1DateValue(), bar
								.getCurrent2DateValue(), selectedConnceptTypes);
					}
				});
				slider.getKnob1Image().addMouseUpHandler(new MouseUpHandler() {
					public void onMouseUp(MouseUpEvent arg0) {
						Slider2Bar bar = slider;
						String startDate = bar.getCurrent1DateValue()
								.toString();
						String endDate = bar.getCurrent2DateValue().toString();
						selectedDatesLabel.setText(startDate.substring(4, 10)
								+ startDate.substring(27)
								+ "                       "
								+ endDate.substring(4, 10)
								+ endDate.substring(27));
						rightPanel.clear();
						RootPanel.get("loading").setVisible(true);
						getResponse(bar.getCurrent1DateValue(), bar
								.getCurrent2DateValue(), selectedConnceptTypes);
					}
				});

				slider.getKnob1Image().addMouseMoveHandler(
						new MouseMoveHandler() {

							@Override
							public void onMouseMove(MouseMoveEvent event) {
								Slider2Bar bar = slider;
								String startDate = bar.getCurrent1DateValue()
										.toString();
								String endDate = bar.getCurrent2DateValue()
										.toString();
								selectedDatesLabel.setText(startDate.substring(
										4, 10)
										+ startDate.substring(27)
										+ "                       "
										+ endDate.substring(4, 10)
										+ endDate.substring(27));

							}

						});
				slider.getKnob2Image().addMouseMoveHandler(
						new MouseMoveHandler() {

							@Override
							public void onMouseMove(MouseMoveEvent event) {
								Slider2Bar bar = slider;
								String startDate = bar.getCurrent1DateValue()
										.toString();
								String endDate = bar.getCurrent2DateValue()
										.toString();
								selectedDatesLabel.setText(startDate.substring(
										4, 10)
										+ startDate.substring(27)
										+ "                       "
										+ endDate.substring(4, 10)
										+ endDate.substring(27));

							}

						});

				formPanel.insert(slider, 1);
			}
		};
		serviceAsync.getDateRange(patientId, callback);
		bottomPanel.setWidth("100%");
		mainPanel.add(bottomPanel);
		mainPanel.setWidth("100%");
		getResponse(null, null, null);
	}

	private void getResponse(final Date startDate, final Date endDate,
			final Set<Integer> conceptId) {
		final long startTime = System.currentTimeMillis();

		if (conceptId != null && conceptId.size() == 0) {
			// No concept filtering option selected
			rightPanel.clear();
		}
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
				long endTime = System.currentTimeMillis();
				currentConceptCount = selectedConnceptTypes.size();
				if (conceptId == null
						|| sendConceptCount == currentConceptCount) {
					showData(result);
					long renderFinishTime = System.currentTimeMillis();
//					resultLabel.setText("Time delay " + (endTime - startTime)
//							+ " ms" + " Render Time "
//							+ (renderFinishTime - endTime) + " ms");
					RootPanel.get("loading").setVisible(false);
					RootPanel.get("webapp").add(mainPanel);
				} else {
					getResponse(startDate, endDate, selectedConnceptTypes);
				}
			}
		};

		serviceAsync.getPatientObsData(patientId, startDate, endDate,
				conceptId, callback);
	}

	private void showData(List<UIObs> data) {
		
		if (data == null) {
			rightPanel.clear();
		}
		int index = 0;
		Date obsDate = null;
		if (bottomPanel.getWidgetIndex(rightPanel) >= 0) {
			bottomPanel.remove(rightPanel);
		}
		rightPanel = new VerticalPanel();
		FormPanel inPanel = new FormPanel();
		GroupingStore<ObsDataModel> store = new GroupingStore<ObsDataModel>();
		store.groupBy("obsDate");
		store.sort("obsDate", SortDir.DESC);
		for (UIObs obs : data) {

			if (obsDate == null
					|| !(obsDate.toString().equals(obs.getObsDateTime()
							.toString()))) {
				inPanel = new FormPanel();
				inPanel.setCollapsible(false);
				subPanel = new VerticalPanel();
				subPanel.setWidth("100%");
				inPanel.setHeading("Patient Observations");
				index = 1;
			}

			if (obs.getConcepts() != null
					&& obs.getConcepts().getDisplayName() != null) {

				String conceptName = obs.getConcepts().getDisplayName()
						.toLowerCase();
				char newChar = (char) (conceptName.charAt(0) - 32);
				conceptName = newChar + conceptName.substring(1);
				String text = "";
				boolean isNumeric = false;
				if (obs.getNumericValue() != null) {
					String unit = obs.getConcepts().getUnits();
					text = obs.getNumericValue().toString() + " " + unit;
					isNumeric = true;
				} else {
					text = obs.getStringValue();
					isNumeric = false;
				}
				ObsDataModel obsData = new ObsDataModel(obs.getObsDateTime()
						.toString(), conceptName, text, Integer.valueOf(obs
						.getConcepts().getConceptId()), isNumeric);
				obsData.set("dateValue", obs.getObsDateTime());
				obsData.set("obsId", obs.getObsId());
				store.add(obsData);
			}

			obsDate = obs.getObsDateTime();

		}

		List<ColumnConfig> col = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId("obsDate");
		column.setHeader("Date of Observation");
		column.setWidth(200);
		col.add(column);
		column = new ColumnConfig();
		column.setId("conceptName");
		column.setHeader("Observation");
		column.setWidth(500);
		col.add(column);
		column = new ColumnConfig();
		column.setId("obsValue");
		column.setHeader("Result / Value");
		column.setWidth(250);
		col.add(column);
		final ColumnModel cm = new ColumnModel(col);
		GroupingView view = new GroupingView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				String f = cm.getColumnById(data.field).getHeader();
				String l = data.models.size() == 1 ? "Item" : "Items";
				return f + ": " + data.group.toString().substring(0, 10) + " ("
						+ data.models.size() + " " + l + ")";
			}
		});
		Grid<ObsDataModel> grid = new Grid<ObsDataModel>(store, cm);
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.getView().setForceFit(true);
		GridSelectionModel<ObsDataModel> gsm = grid.getSelectionModel();
		gsm.setSelectionMode(SelectionMode.SINGLE);
		grid.setView(view);
		grid.addListener(Events.CellClick,
				new Listener<GridEvent<ObsDataModel>>() {

					@Override
					public void handleEvent(final GridEvent<ObsDataModel> be) {
						final ContentPanel panel = new ContentPanel();
						final Window window = new Window();
						window.setSize(900, 500);
						window.setPlain(true);
						window.setModal(true);
						window.setBlinkModal(true);
						window.setHeading("Patient Observation Details");
						window.setLayout(new FitLayout());
						window.setMaximizable(true);
						final FormPanel form = new FormPanel();
						form.setScrollMode(Scroll.AUTO);
						String patientIdValue = com.google.gwt.user.client.Window.Location
								.getParameter("patientId");
						final Integer conceptId = be.getModel().getConceptId();
						Date dateValue = be.getModel().getDateValue();
						final boolean isNumeric = be.getModel().isNumeric();

						FlowsheetServiceAsync serviceAsync = GWT
								.create(FlowsheetService.class);

						AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
							public void onFailure(Throwable caught) {
								resultLabel.setText(caught.getMessage());
							}

							public void onSuccess(String[] result) {
								patientObsDetailArray = result;

							}
						};
						serviceAsync.getPatientObsDetails(patientIdValue,
								dateValue, conceptId, callback);

						serviceAsync = GWT.create(FlowsheetService.class);

						AsyncCallback<UIDetailedData[]> newCallback = new AsyncCallback<UIDetailedData[]>() {
							public void onFailure(Throwable caught) {
								resultLabel.setText(caught.getMessage());
							}

							public void onSuccess(UIDetailedData[] result) {
								numericData = result;
								lchart = getGraph(be.getModel().getConceptId());
								panel.setLayout(new BorderLayout());
								BorderLayoutData lay = new BorderLayoutData(
										LayoutRegion.NORTH, 50);
								panel
										.setHeading(patientObsDetailArray[2]
												+ " for "
												+ patientObsDetailArray[0]
												+ " ("
												+ patientObsDetailArray[1]
												+ ")");
								form.add(new Text(patientObsDetailArray[3]));
								if(isNumeric){
									lay = new BorderLayoutData(
											LayoutRegion.NORTH, 80);
									if(numericData[0].getHiNormal()!=null && numericData[0].getLowNormal()!=null){
										form.add(new Text("Normal Range: "+numericData[0].getLowNormal()+" - "+numericData[0].getHiNormal()+" "+numericData[0].getUnit()));
									}
									else if(numericData[0].getHiNormal()!=null){
										form.add(new Text("Normal Range < "+numericData[0].getHiNormal()+" "+numericData[0].getUnit()));
									}
									else if(numericData[0].getLowNormal()!=null){
										form.add(new Text("Normal Range > "+numericData[0].getLowNormal()+" "+numericData[0].getUnit()));
									}
									
									if(numericData[0].getHiCritical()!=null && numericData[0].getLowCritical()!=null){
										form.add(new Text("Critical Range: "+numericData[0].getLowCritical()+" - "+numericData[0].getHiCritical()+" "+numericData[0].getUnit()));
									}
									else if(numericData[0].getHiCritical()!=null){
										form.add(new Text("Critical Range < "+numericData[0].getHiCritical()+" "+numericData[0].getUnit()));
									}
									else if(numericData[0].getLowCritical()!=null){
										form.add(new Text("Critical Range > "+numericData[0].getLowCritical()+" "+numericData[0].getUnit()));
									}
								}
								form.setHeaderVisible(false);
								panel.add(form, lay);
								BorderLayoutData chartLayout = new BorderLayoutData(
										LayoutRegion.WEST, 500);
								BorderLayoutData flowsheetLayout = new BorderLayoutData(
										LayoutRegion.CENTER, 400);
								if (isNumeric) {
									panel.add(lchart, chartLayout);
								} else {
									window.setSize(600, 500);
								}
								panel.add(getFlowsheet(isNumeric, be.getModel()
										.getObsId()), flowsheetLayout);
								window.add(panel);
								window.show();
							}
						};
						serviceAsync.getDetailedHistory(patientId, conceptId,
								null, null, newCallback);

					}

				});
		inPanel.setLayout(new FitLayout());
		inPanel.setSize(750, (int) (store.getCount() * 23.5));// TODO
		inPanel.add(grid);
		subPanel.add(inPanel);
		subPanel.setWidth("100%");
		rightPanel.setWidth("100%");
		rightPanel.add(subPanel);

		leftPanel.setWidth("100%");
		rightPanel.setWidth("100%");
		bottomPanel.add(leftPanel, DockPanel.WEST);
		bottomPanel.add(rightPanel, DockPanel.WEST);
		bottomPanel.setCellWidth(leftPanel, "20%");
		bottomPanel.setCellWidth(rightPanel, "80%");

	}

	private ContentPanel getFlowsheet(boolean isNumeric,
			final Integer selectObsId) {

		ListStore<ObsDataModel> store = new ListStore<ObsDataModel>();
		store.add(getPatientObsData(isNumeric));
		GridCellRenderer<ObsDataModel> change = new GridCellRenderer<ObsDataModel>() {
			@Override
			public String render(ObsDataModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ObsDataModel> store, Grid<ObsDataModel> grid) {
				
				if (selectObsId.toString().equals(
						((Integer) model.get("obsId")).toString())) {
				
					return "<span style='background-color:yellow'>"
					+ (String) model.get(property) + "</span>";
				}

				return "<span>"
						+ (String) model.get(property) + "</span>";
			}
		};
		List<ColumnConfig> col = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId("obsDate");
		column.setHeader("Date");
		column.setWidth(80);
		column.setRenderer(change);
		col.add(column);
		column = new ColumnConfig();
		column.setId("obsValue");
		String unit = numericData[0].getUnit();
		column.setHeader(isNumeric ? ("Value (" + unit + ")") : ("Value"));
		column.setWidth(60);
		column.setAlignment(isNumeric ? HorizontalAlignment.CENTER
				: HorizontalAlignment.LEFT);
		col.add(column);
		ColumnModel cm = new ColumnModel(col);
		Grid<ObsDataModel> grid = new Grid<ObsDataModel>(store, cm);
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.getView().setForceFit(true);
		GridSelectionModel<ObsDataModel> gsm = grid.getSelectionModel();
		gsm.setSelectionMode(SelectionMode.SINGLE);
		grid.addListener(Events.CellClick,
				new Listener<GridEvent<ObsDataModel>>() {

					@Override
					public void handleEvent(final GridEvent<ObsDataModel> be) {
						FlowsheetServiceAsync serviceAsync = GWT
								.create(FlowsheetService.class);

						AsyncCallback<UIObs> callback = new AsyncCallback<UIObs>() {
							public void onFailure(Throwable caught) {
								resultLabel.setText(caught.getMessage());
							}

							public void onSuccess(UIObs result) {
								// display details of this obs
								Window window = new Window();
								ContentPanel panel = new ContentPanel();
								panel.setLayout(new BorderLayout());
								FormPanel form = new FormPanel();
								form.setPadding(10);
								BorderLayoutData lay = new BorderLayoutData(
										LayoutRegion.NORTH, 400);
								panel
										.setHeading(patientObsDetailArray[0]
												+ " ("
												+ patientObsDetailArray[1]
												+ ")");
								form.add(new Text("Concept: "
										+ patientObsDetailArray[2]));
								form.add(new Text("Concept Description: "
										+ patientObsDetailArray[3]));
								if (result != null) {
									form.add(new Text("Obs Date: "
											+ result.getObsDateTime()
													.toLocaleString()
													.substring(0, 15)));
									form.add(new Text("Location: "
											+ result.getLocation()));
									form.add(new Text("Obs Result: "
											+ result.getStringValue()));									
								}
								form.setHeaderVisible(false);
								panel.add(form, lay);
								window.add(panel);
								window.setSize(400, 300);
								window.setPlain(true);
								window.setModal(true);
								window.setBlinkModal(true);
								window
										.setHeading("Patient Observation Details");
								window.setLayout(new FitLayout());
								window.setMaximizable(true);
								window.show();
							}
						};
						serviceAsync.getObsDetails(be.getModel().getObsId(),
								callback);
					}
				});
		ContentPanel cPanel = new ContentPanel();
		cPanel.setLayout(new FitLayout());
		cPanel.add(grid);
		FieldSet resourcePanel = new FieldSet();
		resourcePanel.setHeading("Online Resources");
		resourcePanel.setCollapsible(false);
		Anchor googleLink = new Anchor("Google", "http://www.google.com",
				"_blank");
		Anchor onlineLabResultLink = new Anchor("Lab Tests Online", "",
				"_blank");
		Anchor upToDateLink = new Anchor("UpToDate", "", "_blank");
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(upToDateLink);
		vPanel.add(googleLink);
		vPanel.add(onlineLabResultLink);
		resourcePanel.add(vPanel);
		resourcePanel.getElement().getStyle().setBorderColor("#99bbe8");
		FieldSet bottomComponent = new FieldSet();
		bottomComponent.add(resourcePanel);
		Label showDetailLabel = new Label(
				"The result you clicked");
		showDetailLabel.getElement().getStyle().setBackgroundColor("yellow");
		showDetailLabel.setWidth("50");
		bottomComponent.add(showDetailLabel);
		bottomComponent.getElement().getStyle().setBorderColor("#99bbe8");
		cPanel.setBottomComponent(bottomComponent);
		cPanel.setHeading("Flowsheet");
		cPanel.setCollapsible(false);
		return cPanel;
	}

	private LayoutContainer getGraph(final Integer conceptId) {

		String url = "/openmrs/moduleResources/flowsheet/chart/open-flash-chart.swf";
		final Chart chart = new Chart(url);
		chart.setChartModel(getHorizontalBarChartModel(conceptId));
		FieldSet fs = new FieldSet();
		fs.setHeading("Chart");
		fs.setLayout(new FitLayout());
		fs.add(chart, new FitData(0, 0, 20, 0));
		fs.setCollapsible(false);
		fs.getElement().getStyle().setBorderColor("#99bbe8");
		return fs;
	}

	public ChartModel getHorizontalBarChartModel(Integer conceptId) {

		// Create a ChartModel with the Chart Title and some
		// style attributes
		ChartModel cm = new ChartModel(numericData[0].getConceptName(),
				"font-size: 14px; font-family:      Verdana; text-align: center;");
		cm.setBackgroundColour("ffdef5");

		XAxis xa = new XAxis();
		// set the maximum, minimum and the step value for the X
		// axis
		xa.setColour("c799e8");
		YAxis ya = new YAxis();
		ya.setGridColour("81aeea");
		// Finding the min, max values of the results for the patient
		double minObsValue,maxObsValue;
		double[] obsValue=new double[numericData.length];
		int count=0;
		for (int index = 0; index <numericData.length; index++) {
			if(numericData[index].getObsValue()!=null)
			obsValue[count++]=(numericData[index].getObsValue()).doubleValue();
			
		}
		Arrays.sort(obsValue);
		minObsValue=obsValue[0];
		maxObsValue=obsValue[count-1];
		int interval = (int) ((maxObsValue*1.1 - minObsValue*0.9) / 10);
		// Add the labels to the Y axis
		ya.setRange(Math.floor(minObsValue*0.9),Math.ceil(maxObsValue*1.1),Math.abs(interval));

		cm.setXAxis(xa);
		LineChart lchart = new LineChart();
		lchart.setTooltip("#val#" + numericData[0].getConceptName());
		for (int index = 1; index <= numericData.length; index++) {
			// if (numericData[numericData.length-index] != null) {
			xa.addLabels(numericData[numericData.length - index].getObsDate()
					.toString().substring(0, 10));
			lchart.addDots(new Star(numericData[numericData.length - index]
					.getObsValue()));
			// }
		}
		xa.setOffset(true);
		cm.setYAxis(ya);
		lchart.setColour("e73585");
		cm.addChartConfig(lchart);
		cm.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));
		return cm;
	}

	private List<ObsDataModel> getPatientObsData(boolean isNumeric) {
		List<ObsDataModel> data = new ArrayList<ObsDataModel>();
		for (int index = 0; index < numericData.length; index++) {
			if (numericData[index] != null) {
				ObsDataModel dataEntry = null;
				if (isNumeric) {
					dataEntry = new ObsDataModel(numericData[index]
							.getObsDate().toString().substring(0, 10),
							numericData[index].getObsValue().toString());
				} else {
					dataEntry = new ObsDataModel(numericData[index]
							.getObsDate().toString().substring(0, 10),
							numericData[index].getStringValue());
				}
				dataEntry.set("obsId", numericData[index].getObsId());
				data.add(dataEntry);
			}
		}
		return data;
	}
}

class ObsDataModel extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObsDataModel(String date, String conceptName, String value,
			Integer conceptId, boolean isNumeric) {
		set("obsDate", date);
		set("conceptName", conceptName);
		set("obsValue", value);
		set("conceptId", conceptId);
		set("isNumeric", isNumeric);

	}

	public ObsDataModel(String date, String conceptName, String value) {
		set("obsDate", date);
		set("conceptName", conceptName);
		set("obsValue", value);

	}

	// public ObsDataModel(String date, String value) {
	// set("obsDate", date);
	// set("obsValue", value);
	//
	// }
	public ObsDataModel(String obsDate, String value) {
		set("obsDate", obsDate);
		set("obsValue", value);

	}

	Integer getObsId() {
		return (Integer) get("obsId");
	}

	String getObsDate() {
		return (String) get("obsDate");
	}

	String getObsTime() {
		return (String) get("obsTime");
	}

	String getObsValue() {
		return (String) get("obsValue");
	}

	String getConceptName() {
		return (String) get("conceptName");
	}

	Integer getConceptId() {
		return (Integer) get("conceptId");
	}

	boolean isNumeric() {
		return (Boolean) get("isNumeric");
	}

	Date getDateValue() {
		return (Date) get("dateValue");
	}
}