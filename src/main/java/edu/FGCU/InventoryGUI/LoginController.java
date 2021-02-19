package edu.FGCU.InventoryGUI;

import edu.FGCU.InventoryClasses.Employee;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public static int userIndexInList = -1;

    @FXML
    public Button newUserButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameIdText;
    @FXML
    private PasswordField loginPasswordText;


    /**
     * Closes the stage of the current scene
     * @param event
     */
    public void exitButtonOnAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Loads the scene to register as a new user if they click the new user button
     * @param event
     * @throws IOException
     */
    public void newUserButtonOnAction(ActionEvent event) throws IOException {
        Parent adminMenuParent = FXMLLoader.load(getClass().getResource("newUser.fxml"));
        Scene adminMenuScene = new Scene(adminMenuParent);
        Stage window = (Stage) loginButton.getScene().getWindow();
        window.setScene(adminMenuScene);
        window.show();
    }

    /**
     * Validates the login and password of what the users enter in the text fields.
     * Also sets the userIndexInList field with the index of the employee in the employee arraylist for later use.
     * @param event
     * @throws IOException
     */
    public void loginButtonOnAction(ActionEvent event) throws IOException {
        try{
            if(Main.checkID(Integer.parseInt(usernameIdText.getText())) == true){
                if(Main.checklogin(Integer.parseInt(usernameIdText.getText()),loginPasswordText.getText()) == true){
                        for(Employee employee: Main.employeeInventory.employeeInventory){
                            if(employee.getEmployeeId() == Integer.parseInt(usernameIdText.getText())){
                                userIndexInList = Main.employeeInventory.employeeInventory.indexOf(employee);
                            }
                        }
                        Parent adminMenuParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
                        Scene adminMenuScene = new Scene(adminMenuParent);
                        Stage window = (Stage) loginButton.getScene().getWindow();
                        window.setScene(adminMenuScene);
                        window.show();
                }
                else{
                    loginMessageLabel.setText("Incorrect login.Please try again. ");
                }
            }


        }
        catch(NullPointerException | NumberFormatException e){
            loginMessageLabel.setText("One or two fields empty. Only put numbers for user ID.");
        }





    }
}
