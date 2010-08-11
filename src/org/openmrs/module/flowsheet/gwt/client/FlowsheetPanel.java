package org.openmrs.module.flowsheet.gwt.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.module.flowsheet.gwt.client.model.UIDetailedData;
import org.openmrs.module.flowsheet.gwt.client.model.UIEncounter;
import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.ToolTip;
import com.extjs.gxt.charts.client.model.ToolTip.MouseStyle;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.LineChart;
import com.extjs.gxt.charts.client.model.charts.dots.SolidDot;
import com.extjs.gxt.charts.client.model.charts.dots.Star;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Composite;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.HeaderGroupConfig;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class handles the generation of the flowsheet UI
 * 
 * @author umashanthi
 *
 */

public class FlowsheetPanel extends LayoutContainer {
	
 
	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label resultLabel = new Label();
	private VerticalPanel subPanel = new VerticalPanel();
	private DockPanel bottomPanel = new DockPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private FormPanel filtersPaneFormPanel = new FormPanel();
	private UIDetailedData[] numericData = null;
	private LayoutContainer lchart = null;
	private Slider2Bar slider = null;
	private Label selectedDatesLabel = new Label();
	private String patientId;
	private Set<Integer> selectedConnceptTypes = new HashSet<Integer>();
	private boolean isClearSelected = false;
	private boolean isSelectAllSelected = false;
	private FlowsheetServiceAsync rpcService = null;
	private String[] patientObsDetailArray = null;
	private Integer totalObsCount = 0;
	private Integer sentStartIndex = 0;
	private Integer obsPerReq = 25;
	private Integer sentLastIndex = obsPerReq;
	private List<UIObs> patientObsList = new ArrayList<UIObs>();
	private GroupingStore<ObsDataModel> obsDataGroupStore;
	private StoreFilterField<ObsDataModel> filterField;
	private int sendConceptCount;
	private long startTime;
	
	public FlowsheetPanel(String patientId){
		this.patientId=patientId;
	}
	/**
	 * This method generates the UI of the Flowsheet module
	 * The flowsheet module page for the given patient will be rendered when this method is called from an EntryPoint class
	 * @param patientId - The patientId of the Patient whose flowsheet page is required to be shown
	 */
	 @Override
	 protected void onRender(Element target, int index) {
	  super.onRender(target, index);			
		
		/* Left side Form Panel - Filtering Options Pane */
		filtersPaneFormPanel = new FormPanel();
		filtersPaneFormPanel.setFrame(true);
		filtersPaneFormPanel.setHeading("Filtering Options");
		filtersPaneFormPanel.setCollapsible(false);
		filtersPaneFormPanel.setLabelWidth(100);
		
		/* Display the header label for the date range slider */		
		Label dateRangeLabel = new Label("Result Dates");
		dateRangeLabel.setStyleName("header_label");
		filtersPaneFormPanel.add(dateRangeLabel);
		
		/* Showing the selected dates in the date range slider */
		HorizontalPanel dateLablesPanel = new HorizontalPanel();
		dateLablesPanel.setSpacing(5);
		selectedDatesLabel.setStyleName("text_label");
		dateLablesPanel.add(selectedDatesLabel);
		filtersPaneFormPanel.add(dateLablesPanel);
		
		/* Creating the FlowsheetServiceAsync object */		
		rpcService = GWT.create(FlowsheetService.class);
		AsyncCallback<String[][]> conceptClassListCallback = new AsyncCallback<String[][]>() {
			/* onFailure method  for the FlowsheetService.getConceptClassList() */
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}
			/* onSuccess method  for the FlowsheetService.getConceptClassList() */
			public void onSuccess(String[][] result) {
				// when the Concept Name list is retrieved successfully, show it in the left filtering pane
				final CheckBoxGroup group = new CheckBoxGroup();
				group.setOrientation(Orientation.VERTICAL);
				group.setHideLabel(true);
				int index = 0;
				for (String entry : result[0]) {
					CheckBox conceptClassCheckBox = new CheckBox();
					conceptClassCheckBox.setBoxLabel(result[1][index++]);
					conceptClassCheckBox.setLabelStyle("checkboxLabel");
					conceptClassCheckBox.setValue(true);
					conceptClassCheckBox.setValueAttribute((result[0][index - 1]).toString());
					selectedConnceptTypes.add(Integer.valueOf(conceptClassCheckBox
							.getValueAttribute()));
					// Adding listener to the check boxes to listen to selection & deselection of check boxes 
					conceptClassCheckBox.addListener(Events.Change, new Listener<BaseEvent>() {
						// According to the selection, filter the observations
						@Override
						public void handleEvent(BaseEvent be) {
							CheckBox option = (CheckBox) be.getSource();
							if (option.getValue()) {
								selectedConnceptTypes.add(Integer
										.valueOf(option.getValueAttribute()));
								if (!(isClearSelected || isSelectAllSelected)) {
									rightPanel.clear();
									RootPanel.get("loading").setVisible(true);
									getResponse(slider.getCurrent1DateValue(),
											slider.getCurrent2DateValue(),
											selectedConnceptTypes);
								}
							} else {
								selectedConnceptTypes.remove(Integer
										.valueOf(option.getValueAttribute()));
								if (!(isClearSelected || isSelectAllSelected)) {
									rightPanel.clear();
									RootPanel.get("loading").setVisible(true);
									getResponse(slider.getCurrent1DateValue(),
											slider.getCurrent2DateValue(),
											selectedConnceptTypes);
								}
							}
						}

					});
					group.add(conceptClassCheckBox);
				}
				Label resultTypeLabel = new Label("Result Types");
				resultTypeLabel.setStyleName("header_label");
				filtersPaneFormPanel.add(resultTypeLabel);
				// Select All label to select all check boxes at once
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
						getResponse(slider.getCurrent1DateValue(), slider
								.getCurrent2DateValue(), selectedConnceptTypes);
						isSelectAllSelected = false;
					}

				});
				// Clear label to clear all check boxes at once
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
				/* Display the Select All and Clear anchor labels */
				HorizontalPanel anchorPanel = new HorizontalPanel();
				anchorPanel.setSpacing(5);
				anchorPanel.add(selectAllAnchor);
				anchorPanel.add(new Label(" | "));
				anchorPanel.add(clearAnchor);
				filtersPaneFormPanel.add(anchorPanel);
				filtersPaneFormPanel.add(group);
			}
		};
		rpcService.getConceptClassList(patientId, null, null, conceptClassListCallback);
		leftPanel.add(filtersPaneFormPanel); // Adding filter form panel to the leftPanel of the main page
		leftPanel.setHeight("100%");
		leftPanel.add(resultLabel);

		
		AsyncCallback<Date[]> callback = new AsyncCallback<Date[]>() {
			/* onFailure method  for the FlowsheetService.getDateRange() */
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}
			/* onSuccess method  for the FlowsheetService.getDateRange() */
			public void onSuccess(Date[] range) {
				/* Creating the date range slider and setting properties */
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
				/* Adding mouse handler to the knob to listen to mouseUp events*/
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
				/* Adding mouse handler to the knob to listen to mouseMove events*/
				slider.getKnob1Image().addMouseMoveHandler(
						new MouseMoveHandler() {
							
							@Override
							public void onMouseMove(MouseMoveEvent event) {
								// When the knob is moved, show the date value according to knob position
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
								// When the knob is moved, show the date value according to knob position
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

				filtersPaneFormPanel.insert(slider, 1);
			}
		};
		rpcService.getDateRange(patientId, callback);
		bottomPanel.setWidth("100%");
		mainPanel.add(bottomPanel);
		mainPanel.setWidth("100%");
		getResponse(null, null, null);
	}

	/**
	 * This method gets the data according to the passed parameters
	 * Handles filtering according to the passed parameters
	 * This method is called initially when start retrieving data
	 * Thereafter the getObs() is called consecutively until all observation data is retrieved
	 * 
	 * @param startDate The start date of the observations
	 * @param endDate The end date of the observations
	 * @param conceptIdSet The set of conceptClass types selected for filtering
	 */
	private void getResponse(final Date startDate, final Date endDate,
			final Set<Integer> conceptIdSet) {
		startTime=System.currentTimeMillis();
		patientObsList = new ArrayList<UIObs>();

		if (conceptIdSet != null && conceptIdSet.size() == 0) {
			// No concept filtering option selected
			rightPanel.clear();
		}
		if (conceptIdSet != null) {
			sendConceptCount=conceptIdSet.size();
		}
		FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
		AsyncCallback<Integer> obsCountCallback = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(Integer result) {
				totalObsCount = result;
				if (totalObsCount > obsPerReq) {
					sentStartIndex = 0;
					sentLastIndex = obsPerReq;
				} else {
					sentLastIndex = totalObsCount;
				}
				getObs(startDate, endDate, conceptIdSet, 0, sentLastIndex);

			}

		};
		serviceAsync.getObsCount(patientId, startDate, endDate, conceptIdSet,
				obsCountCallback);

	}
	/**
	 * This method retrieves observation data chuck by chuck accroding to the stratIndex and lastIndex specified
	 * 
	 * @param startDate  The start date of observations
	 * @param endDate   The end date of observations
	 * @param conceptIdSet  The set of conceptIds of the conceptClasses selected for filtering
	 * @param startIndex  The index of the first observation entry to fetch
	 * @param lastIndex   The index of the last observation entry to fetch
	 */
	private void getObs(final Date startDate, final Date endDate,
			final Set<Integer> conceptIdSet, Integer startIndex, Integer lastIndex) {

		FlowsheetServiceAsync serviceAsync = GWT.create(FlowsheetService.class);
		AsyncCallback<List<UIObs>> callback = new AsyncCallback<List<UIObs>>() {
			public void onFailure(Throwable caught) {
				resultLabel.setText(caught.getMessage());
			}

			public void onSuccess(List<UIObs> result) {
				//If all the observation data is retrieved, renders data; otherwise, retrieves the next chunk of data
				patientObsList.addAll(result);
				selectedConnceptTypes.size();
				/*
				 * if (conceptId == null || sendConceptCount ==
				 * currentConceptCount) { showData(result); long
				 * renderFinishTime = System.currentTimeMillis(); //
				 * resultLabel.setText("Time delay " + (endTime - startTime) //
				 * + " ms" + " Render Time " // + (renderFinishTime - endTime) +
				 * " ms"); RootPanel.get("loading").setVisible(false);
				 * RootPanel.get("webapp").add(mainPanel); } else {
				 * getResponse(startDate, endDate, selectedConnceptTypes); }
				 */
				if (sentLastIndex >= totalObsCount) { // all obs are requested
					long endTime = System.currentTimeMillis();
					showData(patientObsList);
					long renderFinishTime = System.currentTimeMillis();
					/*
					 * resultLabel.setText("Time delay " + (endTime - startTime)
					 * + " ms" + " Render Time " + (renderFinishTime - endTime)
					 * + " ms");
					 */
					RootPanel.get("loading").setVisible(false);
					RootPanel.get("webapp").add(mainPanel);
				} else {
					sentStartIndex = sentLastIndex;
					sentLastIndex = sentLastIndex + obsPerReq;
					getObs(startDate, endDate, conceptIdSet, sentStartIndex,
							sentLastIndex);
				}
			}
		};

		serviceAsync.getPatientObsData(patientId, startDate, endDate,
				conceptIdSet, startIndex, lastIndex, callback);
	}

	/**
	 * This method renders the observations details in the left panel
	 * 
	 * @param data The list of observation data
	 */
	
	private void showData(List<UIObs> data) {
		
		if (data == null) {
			rightPanel.clear();
		}
		Date obsDate = null;
		if (bottomPanel.getWidgetIndex(rightPanel) >= 0) {
			bottomPanel.remove(rightPanel);
		}
		rightPanel = new VerticalPanel();
		final FormPanel inPanel = new FormPanel();
		/* Creating the store for observations */
		obsDataGroupStore = new GroupingStore<ObsDataModel>();
		obsDataGroupStore.groupBy("obsDate");
		obsDataGroupStore.sort("obsDate", SortDir.DESC);
		for (UIObs obs : data) {

			if (obsDate == null
					|| !(obsDate.toString().equals(obs.getObsDateTime()
							.toString()))) {
				inPanel.clear();
				inPanel.setCollapsible(false);
				subPanel = new VerticalPanel();
				subPanel.setWidth("100%");
				inPanel.setHeading("Patient Observations");
			}
			/* Creating the data model for the grid */
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
				obsDataGroupStore.add(obsData);
			}

			obsDate = obs.getObsDateTime();

		}

		
		// FilterField - search box to filter observations by concept name
		filterField = new StoreFilterField<ObsDataModel>() {

			@Override
			protected boolean doSelect(Store<ObsDataModel> store,
					ObsDataModel parent, ObsDataModel record, String property,
					String filter) {
				String conceptName = record.get("conceptName");
				conceptName = conceptName.toLowerCase();
				if (conceptName.startsWith(filter.toLowerCase())) {
					return true;
				}
				return false;
			}

		};
		filterField.bind(obsDataGroupStore);
		filterField.setFieldLabel("Filter by Concept Name");
		GridFilters filters = new GridFilters();
		filters.setLocal(true);
		StringFilter conceptFilter = new StringFilter("conceptName");
		filters.addFilter(conceptFilter);
		/* Column configurations for the grid */
		List<ColumnConfig> col = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId("obsDate");
		column.setHeader("Date of Observation");
		column.setWidth(200);
		col.add(column);
		column = new ColumnConfig();
		column.setId("conceptName");
		column.setHeader("Observation");
		column.setWidth(400);
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
		cm.addHeaderGroup(0, 1, new HeaderGroupConfig("Filter Observation by Concept Name", 1, 1)); 
		filterField.setWidth(240);
		cm.addHeaderGroup(0, 2, new HeaderGroupConfig(filterField, 1, 1));  
		/* Creation of grid to display observations */
		final Grid<ObsDataModel> grid = new Grid<ObsDataModel>(
				obsDataGroupStore, cm);
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.getView().setForceFit(true);
		GridSelectionModel<ObsDataModel> gsm = grid.getSelectionModel();
		gsm.setSelectionMode(SelectionMode.SINGLE);
		grid.setView(view);
		grid.addPlugin(filters);
		grid.addListener(Events.ViewReady, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent be) {
				grid.getStore().addListener(Store.Add,
						new Listener<StoreEvent<ObsDataModel>>() {
							public void handleEvent(StoreEvent<ObsDataModel> be) {
								if (grid.isViewReady()) {
									grid.getView().getScroller()
											.setStyleAttribute("overflowY",
													"hidden");
									inPanel.setHeight((grid.getView().getBody()
											.isScrollableX() ? 19 : 0)
											+ grid.el().getFrameWidth("tb")
											+ grid.getView().getHeader()
													.getHeight()
											+ inPanel.getFrameHeight()
											+ grid.getView().getBody()
													.firstChild().getHeight());
								}
							}
						});
				if (grid.isViewReady()) {
					grid.getView().getScroller().setStyleAttribute("overflowY",
							"hidden");
					inPanel
							.setHeight((grid.getView().getBody()
									.isScrollableX() ? 19 : 0)
									+ grid.el().getFrameWidth("tb")
									+ grid.getView().getHeader().getHeight()
									+ inPanel.getFrameHeight()
									+ grid.getView().getBody().firstChild()
											.getHeight());
				}
			}
		});
		// Adding listener to the grid to listen for cell click events
		grid.addListener(Events.CellClick,
				new Listener<GridEvent<ObsDataModel>>() {

					@Override
					public void handleEvent(final GridEvent<ObsDataModel> be) {
						final ContentPanel panel = new ContentPanel();
						final Window window = new Window();
						window.setOnEsc(true);
						window.setSize(900, 500);
						window.setPlain(true);
						window.setModal(true);
						window.setBlinkModal(true);
						window.setLayout(new FitLayout());
						window.setMaximizable(true);
						final FormPanel form = new FormPanel();
						form.setScrollMode(Scroll.AUTO);
						String patientIdValue = patientId;
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
								lchart = getGraph(be.getModel().getConceptId(),
										be.getModel().getObsId());
								panel.setLayout(new BorderLayout());
								BorderLayoutData lay = new BorderLayoutData(
										LayoutRegion.NORTH, 50);
								panel.setHeaderVisible(false);
								window
										.setHeading(patientObsDetailArray[2]
												+ " for "
												+ patientObsDetailArray[0]
												+ " ("
												+ patientObsDetailArray[1]
												+ ")");
								form.add(new Text(patientObsDetailArray[3]));
								if (isNumeric) {
									lay = new BorderLayoutData(
											LayoutRegion.NORTH, 80);
									if (numericData[0].getHiNormal() != null
											&& numericData[0].getLowNormal() != null) {
										form.add(new Text("Normal Range: "
												+ numericData[0].getLowNormal()
												+ " - "
												+ numericData[0].getHiNormal()
												+ " "
												+ numericData[0].getUnit()));
									} else if (numericData[0].getHiNormal() != null) {
										form.add(new Text("Normal Range: < "
												+ numericData[0].getHiNormal()
												+ " "
												+ numericData[0].getUnit()));
									} else if (numericData[0].getLowNormal() != null) {
										form.add(new Text("Normal Range: > "
												+ numericData[0].getLowNormal()
												+ " "
												+ numericData[0].getUnit()));
									}

									if (numericData[0].getHiCritical() != null
											&& numericData[0].getLowCritical() != null) {
										form.add(new Text("Critical Range: "
												+ numericData[0]
														.getLowCritical()
												+ " - "
												+ numericData[0]
														.getHiCritical() + " "
												+ numericData[0].getUnit()));
									} else if (numericData[0].getHiCritical() != null) {
										form.add(new Text("Critical Range: > "
												+ numericData[0]
														.getHiCritical() + " "
												+ numericData[0].getUnit()));
									} else if (numericData[0].getLowCritical() != null) {
										form.add(new Text("Critical Range:  < "
												+ numericData[0]
														.getLowCritical() + " "
												+ numericData[0].getUnit()));
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
		inPanel.setSize(750, (int) (obsDataGroupStore.getCount() * 23.5));// TODO
		inPanel.setWidth(700);
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

	/** 
	 * This methods creates the flowsheet for the observation type and returns is
	 * @param isNumeric true if the concept is a type of numeric
	 * @param selectObsId obsId of the selected observation
	 * @return the flowsheet for the selected observation
	 */
	private ContentPanel getFlowsheet(boolean isNumeric,
			final Integer selectObsId) {

		ListStore<ObsDataModel> store = new ListStore<ObsDataModel>();
		store.add(getPatientObsData(isNumeric));
		/* Highlighting the selected observation in the flowsheet */
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

				return "<span>" + (String) model.get(property) + "</span>";
			}
		};
		/* Handling abnormal flags */
		GridCellRenderer<ObsDataModel> valueCellRenderer = new GridCellRenderer<ObsDataModel>() {
			@Override
			public String render(ObsDataModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ObsDataModel> store, Grid<ObsDataModel> grid) {
				String color = null;
				int valueRange = (Integer) model.get("valueRange");
				switch (valueRange) {
				case 1:
					color = "blue";
					break;
				case 2:
					color = "yellow";
					break;
				case 3:
					color = "green";
					break;
				case 4:
					color = "red";
					break;
				}
				if (color == null) {
					return "<span>" + (String) model.get(property) + "</span>";
				}
				return "<span style='font-weight: bold;color:" + color + "'>"
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
		column.setRenderer(valueCellRenderer);
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
		// Adding listener to the grid to listen for cell click events
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
								window.setOnEsc(true);
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
									if(result.getEncounter()!=null){
										UIEncounter encounter=result.getEncounter();
										form.add(new Text("Encounter Date: "+((encounter.getEncounterDateTime()!=null)?encounter.getEncounterDateTime().toString().substring(0,10):" -- empty -- ")));
										form.add(new Text("Encounter Type: "+((encounter.getEncounterType()!=null)?encounter.getEncounterType():" -- empty -- ")));
										form.add(new Text("Encounter Created Date: "+((encounter.getDateCreated()!=null)?encounter.getDateCreated().toString().substring(0,10):" -- empty -- ")));
										form.add(new Text("Encounter Creator: "+((encounter.getCreator()!=null)?encounter.getCreator():" -- empty -- ")));
									}
									else{
										form.add(new Text("Encounter Date: -- empty --"));
										form.add(new Text("Encounter Type: -- empty --"));
										form.add(new Text("Encounter Created Date: -- empty --"));
										form.add(new Text("Encounter Creator: -- empty --"));
									}
									form.add(new Text("Comments: "+((result.getComment()!=null)?result.getComment():" -- empty -- ")));
								}
								form.setHeaderVisible(false);
								panel.add(form, lay);
								window.add(panel);
								window.setSize(600, 300);
								window.setPlain(true);
								window.setModal(true);
								window.setBlinkModal(true);
								window.setHeading("Details for Concept :"
										+ patientObsDetailArray[2]);
								window.setLayout(new FitLayout());
								window.setMaximizable(true);
								window.show();
							}
						};
						serviceAsync.getObsDetails(be.getModel().getObsId(),
								callback);
					}
				});
		// Resource panel in the flowsheet
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
		Label showDetailLabel = new Label("The result you clicked");
		showDetailLabel.getElement().getStyle().setBackgroundColor("yellow");
		showDetailLabel.setWidth("50");
		bottomComponent.add(showDetailLabel);
		bottomComponent.getElement().getStyle().setBorderColor("#99bbe8");
		cPanel.setBottomComponent(bottomComponent);
		cPanel.setHeaderVisible(false);
		cPanel.setCollapsible(false);
		return cPanel;
	}

	/**
	 * This method generated the graph for the given numeric type observation
	 * 
	 * @param conceptId The conceptId of the Concept the observation is made
	 * @param selectedObsId The id of the observation selected from the main page
	 * @return A LayoutContainer object with the generated line chart
	 */
	private LayoutContainer getGraph(final Integer conceptId,
			final Integer selectedObsId) {

		String url = "/openmrs/moduleResources/flowsheet/chart/open-flash-chart.swf";
		final Chart chart = new Chart(url);
		chart
				.setChartModel(getHorizontalBarChartModel(conceptId,
						selectedObsId));
		FieldSet fs = new FieldSet();
		fs.setLayout(new FitLayout());
		fs.add(chart, new FitData(0, 0, 20, 0));
		fs.setCollapsible(false);
		fs.getElement().getStyle().setBorderColor("#99bbe8");
		return fs;
	}
	
	/**
	 * This method creates the chartModel for the line chart
	 * @param conceptId The conceptId of the Concept the observation is made
	 * @param selectedObsId The id of the observation to which the graph is to be generated
	 * @return
	 */

	public ChartModel getHorizontalBarChartModel(Integer conceptId,
			Integer selectedObsId) {

		// Create a ChartModel with the Chart Title and some style attributes
		ChartModel cm = new ChartModel(numericData[0].getConceptName(),
				"font-size: 14px; font-family:      Verdana; text-align: center;");
		cm.setBackgroundColour("ffdef5");

		XAxis xa = new XAxis();
		// set the maximum, minimum and the step value for the X- axis
		xa.setColour("c799e8");
		YAxis ya = new YAxis();
		ya.setGridColour("81aeea");
		// Finding the min, max values of the results for the patient
		double minObsValue, maxObsValue;
		double[] obsValue = new double[numericData.length];
		int count = 0;
		for (int index = 0; index < numericData.length; index++) {
			if (numericData[index].getObsValue() != null)
				obsValue[count++] = (numericData[index].getObsValue())
						.doubleValue();

		}
		Arrays.sort(obsValue);
		minObsValue = obsValue[0];
		maxObsValue = obsValue[count - 1];
		int interval = (int) ((maxObsValue * 1.1 - minObsValue * 0.9) / 10);
		// Add the labels to the Y axis
		ya.setRange(Math.floor(minObsValue * 0.9),
				Math.ceil(maxObsValue * 1.1), Math.abs(interval));

		cm.setXAxis(xa);
		LineChart lchart = new LineChart();
		lchart.setTooltip("#val#" + numericData[0].getConceptName());
		for (int index = 1; index <= numericData.length; index++) {
			xa.addLabels(numericData[numericData.length - index].getObsDate()
					.toString().substring(0, 10));
			if (selectedObsId.intValue() == numericData[numericData.length
					- index].getObsId().intValue()) {
				lchart.addDots(new Star(numericData[numericData.length - index]
						.getObsValue()));
			} else {
				lchart.addDots(new SolidDot(numericData[numericData.length
						- index].getObsValue()));
			}
		}
		xa.setOffset(true);
		cm.setYAxis(ya);
		lchart.setColour("e73585");
		cm.addChartConfig(lchart);
		cm.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));
		return cm;
	}
	
	/**
	 * This method returns the data model list
	 * @param isNumeric true if the observation is of numeric type
	 * @return the data model list 
	 */
	private List<ObsDataModel> getPatientObsData(boolean isNumeric) {
		List<ObsDataModel> data = new ArrayList<ObsDataModel>();
		for (int index = 0; index < numericData.length; index++) {
			if (numericData[index] != null) {
				ObsDataModel dataEntry = null;
				if (isNumeric) {
					dataEntry = new ObsDataModel(numericData[index]
							.getObsDate().toString().substring(0, 10),
							numericData[index].getObsValue().toString(),
							getObsValueRange(numericData[index]));
				} else {
					dataEntry = new ObsDataModel(numericData[index]
							.getObsDate().toString().substring(0, 10),
							numericData[index].getStringValue(), 0);
				}
				dataEntry.set("obsId", numericData[index].getObsId());
				data.add(dataEntry);
			}
		}
		return data;
	}
	/**
	 * This method returns the range of the observation value according to the predefined values 
	 * @param data The data object
	 * @return 0 - none
	 * 	       1 - observation value is between low-normal value and low-critical value
	 *         2 - observation value is less than low-critical value
	 *         3 - observation value is between hi-normal value and hi-critical value
	 *         4 - observation value is greater than hi-normal value
	 */
	private int getObsValueRange(UIDetailedData data) {
		int result = 0;
		if (data.getHiCritical() != null
				&& data.getObsValue() > data.getHiCritical()) {
			return 4;
		}
		if (data.getHiNormal() != null
				&& data.getObsValue() > data.getHiNormal()) {
			return 3;
		}
		if (data.getLowCritical() != null
				&& data.getObsValue() < data.getLowCritical()) {
			return 2;
		}
		if (data.getLowNormal() != null
				&& data.getObsValue() < data.getLowNormal()) {
			return 1;
		}
		return result;
	}
}

/**
 * This class models the patien observation as a subclass of the BaseModel class
 * @author umashanthi
 *
 */
class ObsDataModel extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	/* Constructors */
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

	public ObsDataModel(String obsDate, String value) {
		set("obsDate", obsDate);
		set("obsValue", value);

	}

	public ObsDataModel(String obsDate, String value, int valueRange) {
		set("obsDate", obsDate);
		set("obsValue", value);
		set("valueRange", valueRange);
	}
	
	/* Getter methods */
	
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

	int getValueRange() {
		return (Integer) get("valueRange");
	}
}