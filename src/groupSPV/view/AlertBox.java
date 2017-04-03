/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupSPV.view;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 *
 * @author Spencer
 */
public class AlertBox {
	// TODO Remove and fix all these warnings.
	@SuppressWarnings("restriction")
	public static void display(String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(300);

		Label popupMsg = new Label();
		popupMsg.setText(message);
		Button closeWindow = new Button("Close");
		closeWindow.setOnAction(e -> window.close());

		VBox layout = new VBox(10);
		layout.getChildren().addAll(popupMsg, closeWindow);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}