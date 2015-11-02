package application;

import java.io.IOException;

import controller.SerialPortDataVisualiserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Book Service");

		initVisualiserLayout();
	}

	public void initVisualiserLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainwindow.fxml"));
			BorderPane visualiserLayout = (BorderPane) loader.load();

			Scene scene = new Scene(visualiserLayout);
			// scene.getStylesheets()
			// .add(getClass().getResource("view/css/standard.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.show();

			SerialPortDataVisualiserController controller = loader.getController();
			// controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
