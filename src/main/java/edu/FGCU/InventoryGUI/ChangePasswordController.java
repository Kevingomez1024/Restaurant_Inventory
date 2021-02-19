package edu.FGCU.InventoryGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePasswordController {
    @FXML
    public Label errorLabel;
    @FXML
    public PasswordField oldPassword;
    @FXML
    public PasswordField newPassword;
    @FXML
    public PasswordField confirmNewPassword;
    @FXML
    public Button goBackButton;
    @FXML
    private Button updateButton;

    /**
     * Goes back to the menu scene.
     * @param event
     * @throws IOException
     */
    public void goBackButtonOnAction(ActionEvent event) throws IOException {
        Parent adminMenuParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene adminMenuScene = new Scene(adminMenuParent);
        Stage window = (Stage) goBackButton.getScene().getWindow();
        window.setScene(adminMenuScene);
        window.show();
    }

    /**
     * When the user clicks the update button, the Old password will be validated,
     * along with confirming that the new password is valid, and that the confirm password matches
     * the new password.
     * @param event
     */
    public void updatePasswordButtonOnAction(ActionEvent event){
        int id = Main.employeeInventory.employeeInventory.get(LoginController.userIndexInList).getEmployeeId();
        try{
            if(Main.checklogin(id,oldPassword.getText()) == true){
                if(Main.checkPassword(newPassword.getText()) == false){
                    if(newPassword.getText().equals(confirmNewPassword.getText())){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Employee SET password = ? WHERE employeeId = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1,newPassword.getText());
                            preparedStatement.setInt(2,Main.employeeInventory.employeeInventory.get(LoginController.userIndexInList).getEmployeeId());
                            preparedStatement.executeUpdate();
                            conn.close();
                            errorLabel.setText("Password Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else{
                        errorLabel.setText("New passwords do not match.");
                    }
                }
                else {
                    errorLabel.setText("Invalid. Make sure password has at least 1 lowercase,1 uppercase, 1 number\n" +
                            ", and/or uses only the special characters '!','@','.',',' .");
                }
            }
            else {
                errorLabel.setText("Incorrect Password");
            }
        }
        catch(NullPointerException e){
            errorLabel.setText("One or more fields empty.");
        }
    }
}
