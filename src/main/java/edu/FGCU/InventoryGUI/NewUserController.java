package edu.FGCU.InventoryGUI;

import edu.FGCU.InventoryClasses.Drink;
import edu.FGCU.InventoryClasses.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class NewUserController {

    @FXML
    public PasswordField password;
    @FXML
    public PasswordField confirmPassword;
    @FXML
    public Button register;
    @FXML
    public Button goBackButton;
    @FXML
    public Label warningLabel;
    @FXML
    public TextField userId;
    @FXML
    public TextField fullName;
    @FXML
    public ComboBox position;


    public void goBackButtonOnAction(ActionEvent event) throws IOException {
        Parent adminMenuParent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene adminMenuScene = new Scene(adminMenuParent);
        Stage window = (Stage) goBackButton.getScene().getWindow();
        window.setScene(adminMenuScene);
        window.show();
    }

    /**
     * Registers a new user while validating the passwords they input
     * and automatically assigning them the next id in the database
     */
    public void registerOnAction(){
        try{
            if (fullName.getText().length() > 0 && password.getText().length()>0 & confirmPassword.getText().length() > 0 && position.getSelectionModel().getSelectedItem().toString().length() > 0){
                if(Main.checkPassword(password.getText()) == true){
                    warningLabel.setText("Make sure password has at least 1 uppercase, 1 lowercase,\n 1 number and at least 8 characters.\n Acceptable Special Characters: '!','@','.',',','#'");
                }
                else{
                    if(!password.getText().equalsIgnoreCase(confirmPassword.getText())){
                        warningLabel.setText("Passwords do not match.");
                    }
                    else{
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            Statement selectStmt = conn.createStatement();
                            int id = selectStmt.executeQuery("SELECT employeeId FROM Employee ORDER BY employeeId DESC LIMIT 1;")
                                    .getInt("employeeId") + 1;
                            String query = "INSERT INTO Employee(employeeId,employeeName,employeePosition,password) VALUES (?,?,?,?);";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setInt(1,id);
                            preparedStatement.setString(2,fullName.getText());
                            preparedStatement.setString(3,position.getSelectionModel().getSelectedItem().toString().toLowerCase());
                            preparedStatement.setString(4,password.getText());
                            preparedStatement.executeUpdate();
                            Main.employeeInventory.employeeInventory.add(new Employee(id,fullName.getText().toLowerCase(),position.getSelectionModel().getSelectedItem().toString().toLowerCase(),"hidden"));
                            warningLabel.setText("Remember your ID to Login. Your ID is:");
                            userId.setVisible(true);
                            userId.setText(Integer.toString(id));
                            register.setVisible(false);
                        }catch (SQLException ex){
                            System.out.println("Failed to register user or connect to database.");
                        }

                    }
                }
            }
            else{
                warningLabel.setText("Make sure to fill out all the fields.");
            }
        }
        catch (NullPointerException ex){
            warningLabel.setText("One or more fields empty");
        }

    }
}
