package groupSPV.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.geometry.HPos.RIGHT;

import org.bitcoinj.wallet.Wallet.BalanceType;

import groupSPV.controller.WalletController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login extends Application {
    static Stage window;
    Scene loginWindow, infoWindow;
    private static WalletController wc;
    
    public static void setWalletController(WalletController w){
    	wc = w;
    }

    @Override
    public void start(Stage primaryStage) {
    	window = primaryStage;
        
        //sets up the layout for the login screen
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        //sets up the layout for the info 
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(25, 25, 25, 25));

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
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        
        //allows the user to return to the login page
        Button returnBtn = new Button("Return to Login");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(returnBtn);
        grid2.add(hbBtn2, 1, 4);
        
        //sets up the scene title for the info screen
        Text infoTitle = new Text("Welcome Back");
        infoTitle.setId("welcome-text");
        grid2.add(infoTitle, 0, 0, 2, 1);
        
        //Label that will be next to the users current balance
        Label currentBalance = new Label(wc.getBalance(BalanceType.AVAILABLE).toFriendlyString());
        grid2.add(currentBalance, 0, 1);
        
        //Label that will be next to the first pending transactiosn
        Label pendingTrans = new Label("Pending Transactions:");
        grid2.add(pendingTrans, 0, 2);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6);
        actiontarget.setId("actiontarget");
        GridPane.setColumnSpan(actiontarget, 2);
        GridPane.setHalignment(actiontarget, RIGHT);
        actiontarget.setId("actiontarget");
        
        //when the login button is pressed on the login screen it will take you to the info screen if there is  a username and password entered
        btn.setOnAction( e ->{
            if(userTextField.getText().equals("") || pwBox.getText().equals(""))
            {
                AlertBox.display("UserName or Password Error", "A username or password has not been entered.");
            }
            else
            {
                window.setScene(infoWindow); 
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
