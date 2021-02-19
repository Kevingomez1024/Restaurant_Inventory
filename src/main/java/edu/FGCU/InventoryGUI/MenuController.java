package edu.FGCU.InventoryGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button exitButton;
    @FXML
    private Button viewInventory;
    @FXML
    private Button logoutButton;
    @FXML
    private Button addInventory;
    @FXML
    private Button changePassword;


    public void exitButtonOnAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void viewInventoryOnAction(ActionEvent event) throws IOException {
        Parent viewInventoryParent = FXMLLoader.load(getClass().getResource("viewInventory.fxml"));
        Scene viewInventoryScene = new Scene(viewInventoryParent);
        Stage window = (Stage) viewInventory.getScene().getWindow();
        window.setScene(viewInventoryScene);
        window.show();
    }

    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        Parent menuParent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene menuScene = new Scene(menuParent);
        Stage window = (Stage) logoutButton.getScene().getWindow();
        window.setScene(menuScene);
        window.show();
    }
    public void addInventoryOnAction(ActionEvent event) throws IOException {
        Parent addInventoryParent = FXMLLoader.load(getClass().getResource("addInventory.fxml"));
        Scene addInventoryScene = new Scene(addInventoryParent);
        Stage window = (Stage) addInventory.getScene().getWindow();
        window.setScene(addInventoryScene);
        window.show();
    }

    public void changePasswordOnAction(ActionEvent event) throws IOException {
        Parent orderItemsParent = FXMLLoader.load(getClass().getResource("changePassword.fxml"));
        Scene orderItemsScene = new Scene(orderItemsParent);
        Stage window = (Stage) changePassword.getScene().getWindow();
        window.setScene(orderItemsScene);
        window.show();
    }





}
