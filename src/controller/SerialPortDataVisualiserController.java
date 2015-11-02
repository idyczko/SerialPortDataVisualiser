package controller;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.Main;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import model.Measurement;

public class SerialPortDataVisualiserController {
	Random random = new Random();
	@FXML
	private Button addMeasurement;
	@FXML
	private LineChart<Number, Number> chart;
	@FXML
	private NumberAxis xAxis;
	
	private Series series;
	private int xSeriesData = 0;
	private ConcurrentLinkedQueue<Measurement> dataQ = new ConcurrentLinkedQueue<Measurement>();
	private ExecutorService executor;
	private AddToQueue addToQueue;
	private Timeline timeline2;
	long date = new Date().getTime();
	long time = 0;
	
	private void prepareTimeline() {
		// Every frame to take any data from queue and add to chart
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				addDataToSeries();
			}
		}.start();
	}
	
	private void addDataToSeries() {
		for (int i = 0; i < 20; i++) { // -- add 20 numbers to the plot+
			if (dataQ.isEmpty())
				break;
			//date = new Date().getTime();
			Measurement mes = dataQ.remove();
			time =mes.getTime().getTime()-date;
			series.getData().add(new LineChart.Data(time, mes.getValue()));
		}
		// remove points to keep us at no more than MAX_DATA_POINTS
//		if (series.getData().size() > MAX_DATA_POINTS) {
//			series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
//		}
		// update
		xAxis.setLowerBound(0>time-5000?0:time-5000);//xSeriesData - MAX_DATA_POINTS);
		xAxis.setUpperBound(time - 1);
	}

	private class AddToQueue implements Runnable {
		public void run() {
			try {
				// add a item of random data to queue
				dataQ.add(new Measurement(random.nextFloat()));
				Thread.sleep(1000);
				executor.execute(this);
			} catch (InterruptedException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	@FXML
	private void addNewMeasurement(ActionEvent event) {
		try {
			Task<Measurement> backgroundTask = new Task<Measurement>() {

				@Override
				protected Measurement call() throws Exception {

					Measurement measurement = new Measurement(random.nextFloat());
					dataQ.add(measurement);
					return measurement;
				}
			};

			new Thread(backgroundTask).start();
		} catch (Exception e) {

		}
	}

	
	@FXML
	private void initialize() {

		xAxis = new NumberAxis();
		xAxis.setForceZeroInRange(false);
		xAxis.setAutoRanging(false);

		NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(true);
		
		chart= new LineChart<Number, Number>(xAxis, yAxis) {
			// Override to remove symbols on each data point
			@Override
			protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
			}
		};
		chart.setAnimated(false);
		chart.setId("liveAreaChart");
		chart.setTitle("Animated Area Chart");

		// -- Chart Series
		series = new LineChart.Series<Number, Number>();
		series.setName("Area Chart Series");
		series.getData().add(new LineChart.Data(123, 90));
		series.getData().add(new LineChart.Data(0, 90));
		chart.getData().add(series);
		
		executor = Executors.newCachedThreadPool();
//		addToQueue = new AddToQueue();
//		executor.execute(addToQueue);
		// -- Prepare Timeline
		prepareTimeline();

	}

}