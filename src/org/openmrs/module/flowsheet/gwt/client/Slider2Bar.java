package org.openmrs.module.flowsheet.gwt.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.util.DateWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.SimplePanel;

public class Slider2Bar extends Composite implements HasMouseUpHandlers,
		HasMouseMoveHandlers {

	private AbsolutePanel composite = null;
	private SimplePanel knobsLine = null;
	private Image knob1Image = null;
	private Image knob2Image = null;
	private List labelsLst = new ArrayList();
	private List ticksLst = new ArrayList();

	private double sliderMinValue = 0;
	private double sliderMaxValue = 1;
	private double sliderCurrent1Value = sliderMinValue;
	private double sliderCurrent2Value = sliderMaxValue;
	private double sliderStepSize = 1;
	private int sliderNumLabels = 2;
	private int sliderNumTicks = 2;

	private int horizontalOffset = 15;
	private int sliderLineWidth = 400;
	private int sliderLineHeight = 4;
	private int sliderLabelsHeight = 8; // TODO:Umashanthi
	private int sliderTicksHeight = 0;

	private boolean slidingMouse1 = false;
	private boolean slidingMouse2 = false;

	private boolean sliderHas2Knobs = true;
	private boolean sliderNotifyOnMouseUp = true;
	private boolean sliderNotifyOnMouseMove = true;

	private SliderBarImageBundle sliderImageBundle = null;
	private LabelFormatter sliderLabelFormatter = null;
	private MouseUpHandler mouseUpHandler = null;

	private EventPreview eventPreviewer = null;

	public void init() {
		// Create the line
		knobsLine = new SimplePanel();
		knobsLine.addStyleName("gwt-SliderBar-line");

		// Create the knobs
		if (sliderImageBundle == null)
			sliderImageBundle = (SliderBarImageBundle) GWT
					.create(SliderBarImageBundle.class);

		knob1Image = new Image() {
			public void onBrowserEvent(Event event) {
				super.onBrowserEvent(event);
				switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEDOWN:
					slidingMouse1 = true;
					DOM.setCapture(getElement());
					DOM.eventPreventDefault(event);
					startSliding1();
					break;
				case Event.ONMOUSEMOVE:
					if (slidingMouse1)
						slideKnob1(event, sliderNotifyOnMouseMove);
					break;
				case Event.ONMOUSEUP:
					if (slidingMouse1) {
						DOM.releaseCapture(getElement());
						slideKnob1(event, sliderNotifyOnMouseUp);
						stopSliding1();
						slidingMouse1 = false;
					}
					break;
				}
			}
		};
		knob1Image.addStyleName("gwt-SliderBar-knob");

		if (sliderHas2Knobs) {
			knob2Image = new Image() {
				public void onBrowserEvent(Event event) {
					super.onBrowserEvent(event);
					switch (DOM.eventGetType(event)) {
					case Event.ONMOUSEDOWN:
						slidingMouse2 = true;
						DOM.setCapture(getElement());
						DOM.eventPreventDefault(event);
						startSliding2();
						break;
					case Event.ONMOUSEMOVE:
						if (slidingMouse2) {
							slideKnob2(event, sliderNotifyOnMouseMove);
						}
						break;
					case Event.ONMOUSEUP:
						if (slidingMouse2) {
							DOM.releaseCapture(getElement());
							slidingMouse2 = false;
							slideKnob2(event, sliderNotifyOnMouseUp);
							stopSliding2();
						}
						break;
					}
				}
			};
			knob2Image.addStyleName("gwt-SliderBar-knob");
		}

		// Create the outer shell
		composite = new AbsolutePanel() {
			protected void onLoad() {

				DeferredCommand.addPause();
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						drawAll();
					}
				});
			}
		};
		composite.setStyleName("gwt-SliderBar-shell");
		composite.add(knobsLine);
		composite.add(knob1Image);
		if (sliderHas2Knobs)
			composite.add(knob2Image);
		initWidget(composite);
		setCurrent1Value(sliderMinValue);
		if (sliderHas2Knobs)
			setCurrent2Value(sliderMaxValue);
		else
			sliderCurrent2Value = sliderMaxValue + sliderStepSize;
	}

	public Slider2Bar() {
		init();
	}

	public Slider2Bar(double minValue, double maxValue, double stepSize,
			boolean isDouble) {
		sliderMinValue = minValue;
		sliderMaxValue = maxValue;
		sliderStepSize = stepSize;
		sliderHas2Knobs = isDouble;
		init();
	}

	public Slider2Bar(double minValue, double maxValue, double stepSize,
			int lineWidth, int numLabels, int numTicks, boolean isDouble,
			LabelFormatter labelFormatter) {

		sliderMinValue = minValue;
		sliderMaxValue = maxValue;
		sliderStepSize = stepSize;
		sliderLineWidth = lineWidth;
		sliderNumLabels = numLabels;
		sliderNumTicks = numTicks;
		sliderHas2Knobs = isDouble;
		sliderLabelFormatter = labelFormatter;
		init();
	}

	public Slider2Bar(double minValue, double maxValue, double stepSize,
			int lineWidth, int numLabels, int numTicks, boolean isDouble,
			LabelFormatter labelFormatter, SliderBarImageBundle imageBundle) {
		sliderMinValue = minValue;
		sliderMaxValue = maxValue;
		sliderStepSize = stepSize;
		sliderLineWidth = lineWidth;
		sliderNumLabels = numLabels;
		sliderNumTicks = numTicks;
		sliderHas2Knobs = isDouble;
		sliderLabelFormatter = labelFormatter;
		sliderImageBundle = imageBundle;
		init();
	}

	public void setLineWidth(int lineWidth) {
		sliderLineWidth = lineWidth;
		drawAll();
	}

	public void drawAll() {

		knobsLine.setWidth(sliderLineWidth + "px");
		knobsLine.setHeight(sliderLineHeight + "px");

		drawLabels();
		drawTicks();

		composite.setWidgetPosition(knobsLine, horizontalOffset,
				sliderLabelsHeight + sliderTicksHeight + 2);
		composite
				.setWidth((knobsLine.getOffsetWidth() + (horizontalOffset * 2))
						+ "px");
		composite.setStyleName(".x-form-text, textarea.x-form-field ");
		composite.setHeight((sliderLabelsHeight + sliderTicksHeight + 2
				+ knobsLine.getOffsetHeight() + 10)
				+ "px");

		sliderImageBundle.knobNormal().applyTo(knob1Image);
		drawKnob1();
		if (sliderHas2Knobs) {
			sliderImageBundle.knobNormal().applyTo(knob2Image);
			drawKnob2();
		}

	}

	private void drawLabels() {

		// Create the labels or make them visible
		if (sliderNumLabels == 0)
			return;
		for (int cii = 0; cii <= sliderNumLabels; cii++) {
			HTML label = null;
			if (cii < labelsLst.size()) {
				label = (HTML) labelsLst.get(cii);
			} else {
				label = new HTML();
				label.addStyleName("gwt-SliderBar-label");
				composite.add(label);
				labelsLst.add(label);
			}

			label.setHTML(formatLabel(sliderMinValue
					+ (getTotalRange() * cii / sliderNumLabels)));

			// Position the label
			int lineWidth = knobsLine.getOffsetWidth();
			int labelLeftOffset = horizontalOffset
					+ (lineWidth * cii / sliderNumLabels)
					- (label.getOffsetWidth() / 2);
			composite.setWidgetPosition(label, labelLeftOffset + 4, 0);
			sliderLabelsHeight = Math.max(sliderLabelsHeight, label
					.getOffsetHeight());
		}

		// remove unused labels
		for (int cii = (sliderNumLabels + 1); cii < labelsLst.size(); cii++)
			composite.remove((HTML) labelsLst.get(cii));
	}

	protected String formatLabel(double value) {
		if (sliderLabelFormatter != null) {
			return sliderLabelFormatter.formatLabel(this, value);
		} else {
			String s = getEndOfDay(new Date((long) (value))).toString();
			// adds nothing
			s = "";
			return s;
		}
	}

	private void drawTicks() {

		// Create the ticks or make them visible
		if (sliderNumTicks == 0)
			return;
		for (int cii = 0; cii <= sliderNumTicks; cii++) {
			SimplePanel tick = null;
			if (cii < ticksLst.size()) {
				tick = (SimplePanel) ticksLst.get(cii);
			} else {
				tick = new SimplePanel();
				tick.addStyleName("gwt-SliderBar-tick");
				composite.add(tick);
				ticksLst.add(tick);
			}

			// Position the tick and make it visible
			int lineWidth = knobsLine.getOffsetWidth();
			int tickWidth = tick.getOffsetWidth();
			int tickLeftOffset = horizontalOffset
					+ (lineWidth * cii / sliderNumTicks) - (tickWidth / 2);
			composite.setWidgetPosition(tick, tickLeftOffset,
					sliderLabelsHeight);
			sliderTicksHeight = Math.max(sliderTicksHeight, tick
					.getOffsetHeight());

		}

		// remove unused ticks
		for (int cii = (sliderNumTicks + 1); cii < ticksLst.size(); cii++)
			composite.remove((SimplePanel) ticksLst.get(cii));
	}

	private void dimmOuterElements() {
		int lineWidth = knobsLine.getOffsetWidth();
		int knobWidth = knob1Image.getOffsetWidth();
		int knob1LeftOffset = (int) (horizontalOffset
				+ (getKnob1Percent() * lineWidth) - (knobWidth / 2));
		knob1LeftOffset = Math.min(knob1LeftOffset, horizontalOffset
				+ lineWidth - (knobWidth / 2));
		int knob2LeftOffset = (int) (horizontalOffset
				+ (getKnob2Percent() * lineWidth) - (knobWidth / 2));
		knob2LeftOffset = Math.min(knob2LeftOffset, horizontalOffset
				+ lineWidth - (knobWidth / 2));

		knob1LeftOffset += knobWidth / 2;
		knob2LeftOffset += knobWidth / 2 + 1;

		for (int cii = 0; cii < labelsLst.size(); cii++) {
			HTML label = (HTML) labelsLst.get(cii);
			int labelLeftOffset = composite.getWidgetLeft(label)
					+ label.getOffsetWidth() / 2;
			if (labelLeftOffset < knob1LeftOffset
					|| labelLeftOffset > knob2LeftOffset)
				label.addStyleName("dimm");
			else
				label.removeStyleName("dimm");
		}
		for (int cii = 0; cii < ticksLst.size(); cii++) {
			SimplePanel tick = (SimplePanel) ticksLst.get(cii);
			int tickLeftOffset = composite.getWidgetLeft(tick)
					+ tick.getOffsetWidth() / 2;
			if (tickLeftOffset < knob1LeftOffset
					|| tickLeftOffset > knob2LeftOffset)
				tick.addStyleName("dimm");
			else
				tick.removeStyleName("dimm");
		}

	}

	private void drawKnob(double knobPercent, Image knobImage) {
		int lineWidth = knobsLine.getOffsetWidth();
		int knobWidth = knobImage.getOffsetWidth();
		int knobLeftOffset = (int) (horizontalOffset
				+ (knobPercent * lineWidth) - (knobWidth / 2));
		knobLeftOffset = Math.min(knobLeftOffset, horizontalOffset + lineWidth
				- (knobWidth / 2));

		composite.setWidgetPosition(knobImage, knobLeftOffset,
				sliderLabelsHeight + sliderTicksHeight + 2
						- knobImage.getOffsetHeight() / 2);

		dimmOuterElements();
	}

	private void drawKnob1() {

		drawKnob(getKnob1Percent(), knob1Image);
	}

	private void drawKnob2() {

		drawKnob(getKnob2Percent(), knob2Image);
	}

	public double getCurrent1Value() {
		return sliderCurrent1Value;
	}

	public double getCurrent2Value() {
		if (sliderCurrent2Value + 3600000 > sliderMaxValue)
			return sliderMaxValue;
		return sliderCurrent2Value;
	}

	public Date getCurrent1DateValue() {
		Date date = new Date((long) getCurrent1Value());
		return getEndOfDay(date);
	}

	public Date getCurrent2DateValue() {
		Date date = new Date((long) getCurrent2Value());
		return getEndOfDay(date);
	}

	private Date getEndOfDay(Date day) {
		DateWrapper wrapper = new DateWrapper(day);
		wrapper = wrapper.clearTime();
		return wrapper.asDate();
	}

	public double getMinValue() {
		return sliderMinValue;
	}

	public double getMaxValue() {
		return sliderMaxValue;
	}

	public int getNumLabels() {
		return sliderNumLabels;
	}

	public int getNumTicks() {
		return sliderNumTicks;
	}

	public double getStepSize() {
		return sliderStepSize;
	}

	public LabelFormatter getLabelFormatter() {
		return sliderLabelFormatter;
	}

	public boolean getHas2Knobs() {
		return sliderHas2Knobs;
	}

	public boolean getNotifyOnMouseUp() {
		return sliderNotifyOnMouseUp;
	}

	public boolean getNotifyOnMouseMove() {
		return sliderNotifyOnMouseMove;
	}

	/**
	 * Return the total range between the minimum and maximum values.
	 * 
	 * @return the total range
	 */
	public double getTotalRange() {
		if (sliderMinValue > sliderMaxValue) {
			return 0;
		} else {
			return sliderMaxValue - sliderMinValue;
		}
	}

	/**
	 * Get the percentage of the knob's position relative to the size of the
	 * line. The return value will be between 0.0 and 1.0.
	 * 
	 * @return the current percent complete
	 */
	protected double getKnob1Percent() {
		// If we have no range
		if (sliderMaxValue <= sliderMinValue)
			return 0;

		// Calculate the relative progress
		double percent = (sliderCurrent1Value - sliderMinValue)
				/ (sliderMaxValue - sliderMinValue);
		return Math.max(0.0, Math.min(1.0, percent));
	}

	protected double getKnob2Percent() {
		// If we have no range
		if (sliderMaxValue <= sliderMinValue)
			return 0;

		// Calculate the relative progress
		double percent = (sliderCurrent2Value - sliderMinValue)
				/ (sliderMaxValue - sliderMinValue);
		return Math.max(0.0, Math.min(1.0, percent));
	}

	public void setCurrent1Value(double current1Value) {
		setCurrent1Value(current1Value, true);
	}

	public void setCurrent1Value(double current1Value, boolean fireEvent) {
		// Confine the value to the range
		sliderCurrent1Value = Math.max(sliderMinValue, Math.min(sliderMaxValue,
				current1Value));
		double remainder = (sliderCurrent1Value - sliderMinValue)
				% sliderStepSize;
		sliderCurrent1Value -= remainder;

		// Go to next step if more than halfway there
		if (remainder > (sliderStepSize / 2)
				&& (sliderCurrent1Value + sliderStepSize) <= sliderMaxValue)
			sliderCurrent1Value += sliderStepSize;

		drawKnob1();

		// if (fireEvent && _changeListeners != null)
		// _changeListeners.fireChange(this);

	}

	public void setCurrent2Value(double current2Value) {
		setCurrent2Value(current2Value, true);
	}

	public void setCurrent2Value(double current2Value, boolean fireEvent) {
		// Confine the value to the range
		sliderCurrent2Value = Math.max(sliderMinValue, Math.min(sliderMaxValue,
				current2Value));
		double remainder = (sliderCurrent2Value - sliderMinValue)
				% sliderStepSize;
		sliderCurrent2Value -= remainder;

		// Go to next step if more than halfway there
		if (remainder > (sliderStepSize / 2)
				&& (sliderCurrent2Value + sliderStepSize) <= sliderMaxValue)
			sliderCurrent2Value += sliderStepSize;

		drawKnob2();

	}

	public void setMinValue(double _minValue) {
		_minValue = _minValue;
		drawAll();
	}

	public void setMaxValue(double _maxValue) {
		_maxValue = _maxValue;
		drawAll();
	}

	public void setStepSize(double stepSize) {
		sliderStepSize = stepSize;
		drawAll();
	}

	public void setNumLabels(int numLabels) {
		sliderNumLabels = numLabels;
		drawAll();
	}

	public void setNumTicks(int numTicks) {
		sliderNumTicks = numTicks;
		drawAll();
	}

	public void setLabelFormatter(LabelFormatter labelFormatter) {
		sliderLabelFormatter = labelFormatter;
		drawAll();
	}

	public void setHas2Knobs(boolean has2Knobs) {
		sliderHas2Knobs = has2Knobs;
	}

	public void setNotifyOnMouseUp(boolean notifyOnMouseUp) {
		sliderNotifyOnMouseUp = notifyOnMouseUp;
	}

	public void setNotifyOnMouseMove(boolean notifyOnMouseMove) {
		sliderNotifyOnMouseMove = notifyOnMouseMove;
	}

	private void slideKnob1(Event event, boolean update) {
		int x = DOM.eventGetClientX(event);
		if (x > 0) {
			int lineWidth = knobsLine.getOffsetWidth();
			int lineLeft = knobsLine.getAbsoluteLeft();
			double percent = (double) (x - lineLeft) / lineWidth * 1.0;
			setCurrent1Value(Math.min(getCurrent2Value() - sliderStepSize,
					getTotalRange() * percent + sliderMinValue), update);
		}
	}

	private void slideKnob2(Event event, boolean update) {
		int x = DOM.eventGetClientX(event);
		if (x > 0) {
			int lineWidth = knobsLine.getOffsetWidth();
			int lineLeft = knobsLine.getAbsoluteLeft();
			double percent = (double) (x - lineLeft) / lineWidth * 1.0;
			setCurrent2Value(Math.max(getCurrent1Value() + sliderStepSize,
					getTotalRange() * percent + sliderMinValue), update);
		}
	}

	private void startSliding1() {
		knobsLine.addStyleName("gwt-SliderBar-line-sliding");
		knob1Image.addStyleName("gwt-SliderBar-knob-sliding");
		sliderImageBundle.knobClicked().applyTo(knob1Image);
	}

	private void startSliding2() {
		knobsLine.addStyleName("gwt-SliderBar-line-sliding");
		knob2Image.addStyleName("gwt-SliderBar-knob-sliding");
		sliderImageBundle.knobClicked().applyTo(knob2Image);
	}

	private void stopSliding1() {
		knobsLine.removeStyleName("gwt-SliderBar-line-sliding");

		knob1Image.removeStyleName("gwt-SliderBar-knob-sliding");
		sliderImageBundle.knobNormal().applyTo(knob1Image);

	}

	private void stopSliding2() {
		knobsLine.removeStyleName("gwt-SliderBar-line-sliding");

		knob2Image.removeStyleName("gwt-SliderBar-knob-sliding");
		sliderImageBundle.knobNormal().applyTo(knob2Image);
	}

	public static interface SliderBarImageBundle extends ImageBundle {
		/**
		 * An image used for the sliding knob.
		 * 
		 * @return a prototype of this image
		 */
		AbstractImagePrototype knobNormal();

		/**
		 * An image used for the sliding knob while sliding.
		 * 
		 * @return a prototype of this image
		 */
		AbstractImagePrototype knobClicked();
	}

	public static interface LabelFormatter {

		public abstract String formatLabel(Slider2Bar slider, double value);
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	public Image getKnob1Image() {
		return knob1Image;
	}

	public Image getKnob2Image() {
		return knob2Image;
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

}
