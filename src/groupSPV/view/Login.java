package groupSPV.view;
/*
 * Copyright (c) 2012, 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */


import javafx.application.Application;
import static javafx.geometry.HPos.RIGHT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.HashMap;

import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.BalanceType;

import groupSPV.controller.WalletController;

public class Login extends Application {
   private Stage window;
   private Scene loginWindow, infoWindow;
   private String name;
   private WalletController wc = new WalletController (new Wallet(TestNet3Params.get()));
   private HashMap <String,String> users = new HashMap<String, String>();
    
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        name = "";
        
        //sets up the layout for the login screen
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //sets up the scene title for the login screen
        Text loginTitle = new Text("Please enter your username and password");
        loginTitle.setId("welcome-text");
        grid.add(loginTitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        //sign in button
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        
      //register button
        Button registerBtn = new Button("Register an account");
        HBox hbRegBtn = new HBox(10);
        hbRegBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbRegBtn.getChildren().add(registerBtn);
        grid.add(hbRegBtn, 1, 5);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6);
        actiontarget.setId("actiontarget");
        GridPane.setColumnSpan(actiontarget, 2);
        GridPane.setHalignment(actiontarget, RIGHT);
        actiontarget.setId("actiontarget");
        
      //sets up the layout for the info page
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(25, 25, 25, 25));
        
        //allows the user to return to the login page
        Button returnBtn = new Button("Return to Login");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(returnBtn);
        grid2.add(hbBtn2, 1, 5);
        
        //sets up the scene title for the info screen
        Text infoTitle = new Text("Welcome Back" + name);
        infoTitle.setId("welcome-text");
        grid2.add(infoTitle, 0, 0, 2, 1);
        
        //Label that will be next to the users current balance
        Label currentBalance = new Label("Current Balance:");
        grid2.add(currentBalance, 0, 1);
        
        String balance = ""+ wc.getBalance(BalanceType.AVAILABLE_SPENDABLE).toFriendlyString();
        Label userBalance = new Label(balance);
        grid2.add(userBalance, 1, 1);
        
        //Label that will be next to the first pending transaction
        Label pendingTrans = new Label("Pending Transactions:");
        grid2.add(pendingTrans, 0, 3);
        
        ScrollPane transactionPane = new ScrollPane();
        transactionPane.setPrefSize(120, 120);
        grid2.add(transactionPane, 1, 3);
        
        Label userKeys = new Label("Keys:");
        grid2.add(userKeys, 0, 2);
        
        ScrollPane keyPane = new ScrollPane();
        keyPane.setPrefSize(120, 120);
        grid2.add(keyPane, 1, 2); 
        
        //Label keys = new Label("" + wc.getKeys());
        //grid2.add(keys, 1, 3);
        
        Label recentTrans = new Label("Recent Transactions:");
        grid2.add(recentTrans, 0, 4);
        
        ScrollPane recentPane = new ScrollPane();
        recentPane.setPrefSize(120, 120);
        grid2.add(recentPane, 1, 4); 
        
        
        
        //sets up the layout for the registration page
        GridPane grid3 = new GridPane();
        grid3.setAlignment(Pos.CENTER);
        grid3.setHgap(10);
        grid3.setVgap(10);
        grid3.setPadding(new Insets(25, 25, 25, 25));
        
        Text registerTitle = new Text("Create a new account");
        registerTitle.setId("welcome-text");
        grid3.add(registerTitle, 0, 0, 2, 1);

        Label createUserName = new Label("Choose a userName:");
        grid3.add(createUserName, 0, 1);

        TextField createuserTextField = new TextField();
        grid3.add(createuserTextField, 1, 1);

        Label createPw = new Label("Password:");
        grid3.add(createPw, 0, 2);

        PasswordField createPwBox = new PasswordField();
        grid3.add(createPwBox, 1, 2);
        
        Button confirmBtn = new Button("Register an account");
        HBox hbComfirmBtn = new HBox(10);
        hbComfirmBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbComfirmBtn.getChildren().add(confirmBtn);
        grid3.add(hbComfirmBtn, 1, 4);
        
        Button cancelBtn = new Button("Cancel");
        HBox hbcancel = new HBox(10);
        hbcancel.setAlignment(Pos.BOTTOM_LEFT);
        hbcancel.getChildren().add(cancelBtn);
        grid3.add(hbcancel, 1, 5);
        
        
        Button test= new Button("Test");
        HBox hbtest = new HBox(10);
        hbtest.setAlignment(Pos.BOTTOM_LEFT);
        hbtest.getChildren().add(test);
        grid.add(hbtest, 0, 7);
        
        test.setOnAction(e ->{
            System.out.println("");
        });
        
        Scene registerWindow = new Scene(grid3, 325, 250);
    	registerWindow.getStylesheets().add(Login.class.getResource("Login.css").toExternalForm());
        
        //when the login button is pressed on the login screen it will take you to the info screen if there is  a username and password entered
        btn.setOnAction( e ->{
        	if(users.containsKey(pwBox.getText()) && users.containsValue(userTextField.getText()))
        	{
        		name = userTextField.getText();
        		window.setScene(infoWindow);
        		userTextField.clear();
        		pwBox.clear();
        	}
        	else if(userTextField.getText().equals("") || pwBox.getText().equals(""))
            {
                AlertBox.display("UserName or Password Error", "A username or password has not been entered.");
            }
        	else if(users.containsValue(userTextField.getText()) == false)
        	{
        		AlertBox.display("UserName Error", "Invalid UserName");
        	}
            else
            {
            	AlertBox.display("Password Error", "The password you have entered is incorrect or misspelled.");
            }
        });
        
        registerBtn.setOnAction(e ->window.setScene(registerWindow));
        
        cancelBtn.setOnAction(e ->{
        	window.setScene(loginWindow);
        	createPwBox.clear();
    		createuserTextField.clear();
        });
        
        confirmBtn.setOnAction(e->{
        	if(users.containsKey(createPwBox.getText()))
        	{
        		AlertBox.display("Password Error", "Somebody is already using that password please enter a different one");
        	}
        	else if(users.containsValue(createuserTextField.getText()))
        	{
        		AlertBox.display("UserName Error", "Somebody is already using that username please enter a different one");
        	}
        	else
        	{
        		users.put(createPwBox.getText(), createuserTextField.getText());
        		window.setScene(loginWindow);
        		createPwBox.clear();
        		createuserTextField.clear();
        	}
        });
        
        returnBtn.setOnAction(e -> window.setScene(loginWindow));

        //login Layout
        loginWindow = new Scene(grid, 325, 250);
        loginWindow.getStylesheets().add(Login.class.getResource("Login.css").toExternalForm());
        
        //info layout
        infoWindow = new Scene(grid2, 300, 275);
        infoWindow.getStylesheets().add(Login.class.getResource("Login.css").toExternalForm());
        
        //sets the starting window to be the login screen
         window.setTitle("BitCoin Wallet");
        window.setScene( loginWindow);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
