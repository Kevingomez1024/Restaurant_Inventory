package edu.FGCU.InventoryGUI;

import edu.FGCU.InventoryClasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class EditItemController {
    private static ObservableList<String> tempValues = FXCollections.observableArrayList(ViewInventoryController.getLineToEdit().split(","));
    private int indexToChange = -1;
    private String inventory = "";


    @FXML
    private Button viewButton;
    @FXML
    private Button categoriesButton;
    @FXML
    private Button goBackButton;
    @FXML
    private ComboBox<String> editCategory;
    @FXML
    private Label currentValue;
    @FXML
    private TextField newValue;
    @FXML
    private Button updateButton;
    @FXML
    private Label warningLabel;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;
    @FXML
    private Button deleteItem;

    public void goBackButtonOnAction(ActionEvent event) throws IOException {
        Parent adminMenuParent = FXMLLoader.load(getClass().getResource("viewInventory.fxml"));
        Scene adminMenuScene = new Scene(adminMenuParent);
        Stage window = (Stage) goBackButton.getScene().getWindow();
        window.setScene(adminMenuScene);
        window.show();
    }

    public void setCategories(ActionEvent event){
        categoriesButton.setVisible(false);
        viewButton.setVisible(true);
        if(tempValues.get(0).equalsIgnoreCase("equipment")){
            inventory = "equipment";
            deleteItem.setVisible(true);
            ObservableList<String> tempCategories = FXCollections.observableArrayList(Main.equipmentInventory.getHeaders().split(","));
            for(String name: tempCategories){
                editCategory.getItems().add(name);
            }
        }else if(tempValues.get(0).equalsIgnoreCase("drink")){
            inventory = "drink";
            deleteItem.setVisible(true);
            ObservableList<String> tempCategories = FXCollections.observableArrayList(Main.drinkInventory.getHeaders().split(","));
            for(String name: tempCategories){
                editCategory.getItems().add(name);
            }
        }
        else if(tempValues.get(0).equalsIgnoreCase("food")){
            inventory = "food";
            deleteItem.setVisible(true);
            ObservableList<String> tempCategories = FXCollections.observableArrayList(Main.foodInventory.getHeaders().split(","));
            for(String name: tempCategories){
                editCategory.getItems().add(name);
            }
        }
        else{
            inventory = "employee";
            deleteItem.setVisible(true);
            ObservableList<String> tempCategories = FXCollections.observableArrayList(Main.employeeInventory.getHeaders().split(","));
            for(String name: tempCategories){
                editCategory.getItems().add(name);
            }
        }


    }

    /**
     * Loads the item categories to edit into the combo box
     * @param event
     */
    public void viewButtonOnAction(ActionEvent event){
        try{
            warningLabel.setText("");
            if(tempValues.get(0).equalsIgnoreCase("equipment")) {
                ObservableList<String> tempCategories = FXCollections.observableArrayList(Main.equipmentInventory.getHeaders().split(","));
                currentValue.setText("Current Value:" + tempValues.get(tempCategories.indexOf(editCategory.getValue())));
                indexToChange = tempCategories.indexOf(editCategory.getValue());
                if(indexToChange == 0){
                    warningLabel.setText("This field cannot be changed");
                    updateButton.setVisible(false);
                }else{
                    updateButton.setVisible(true);
                }


            }else if(tempValues.get(0).equalsIgnoreCase("drink")){
                ObservableList<String> tempCategories = FXCollections.observableArrayList(Main.drinkInventory.getHeaders().split(","));
                currentValue.setText("Current Value:"+tempValues.get(tempCategories.indexOf(editCategory.getValue())));
                indexToChange = tempCategories.indexOf(editCategory.getValue());
                if(indexToChange == 0){
                    warningLabel.setText("This field cannot be changed");
                    updateButton.setVisible(false);
                }else{
                    updateButton.setVisible(true);
                }
            }
            else if(tempValues.get(0).equalsIgnoreCase("food")){
                ObservableList<String> tempCategories = FXCollections.observableArrayList(Main.foodInventory.getHeaders().split(","));
                currentValue.setText("Current Value:"+ tempValues.get(tempCategories.indexOf(editCategory.getValue())));
                indexToChange = tempCategories.indexOf(editCategory.getValue());
                if(indexToChange == 0){
                    warningLabel.setText("This field cannot be changed");
                    updateButton.setVisible(false);
                }else{
                    updateButton.setVisible(true);
                }
            }
            else{
                ObservableList<String> tempCategories = FXCollections.observableArrayList(Main.employeeInventory.getHeaders().split(","));
                currentValue.setText("Current Value:"+tempValues.get(tempCategories.indexOf(editCategory.getValue())));
                updateButton.setVisible(true);
                indexToChange = tempCategories.indexOf(editCategory.getValue());
                if(indexToChange == 3){
                    warningLabel.setText("This field cannot be changed");
                    updateButton.setVisible(false);
                }else{
                    updateButton.setVisible(true);
                }
            }
        }catch(NullPointerException | IndexOutOfBoundsException e){
            warningLabel.setText("Please enter a category to see.");
        }
    }

    /**
     * Gets the value typed into the newvalue text field and updates it in the database
     * and into the item in the arraylist
     * @param event
     */
    public void updateButtonOnAction(ActionEvent event){
        //handles employee updates
        if(inventory.equalsIgnoreCase("employee")){
            if(indexToChange == 0){
                try{
                    if(Main.checkID(Integer.parseInt(newValue.getText())) == true){
                        warningLabel.setText("ID number already in use please try again!");
                    }
                    else{
                        tempValues.set(indexToChange, newValue.getText());
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Employee SET employeeId = ? WHERE employeeId = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                            preparedStatement.setInt(2,Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).getEmployeeId());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).setEmployeeId(Integer.parseInt(newValue.getText()));

                            try {
                                Main.employeeInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                }
                catch(NumberFormatException | NullPointerException ex){
                    warningLabel.setText("Make sure field is not empty or contains spaces and/or letters.");
                }
            }
            else if(indexToChange == 1 ){
                tempValues.set(indexToChange, newValue.getText());
                Connection conn = null;
                try{
                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                    String query = "UPDATE Employee SET employeeName = ? WHERE employeeName = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1,newValue.getText());
                    preparedStatement.setString(2,Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).getEmployeeName());
                    preparedStatement.executeUpdate();
                    conn.close();
                    Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).setEmployeeId(Integer.parseInt(newValue.getText()));

                    try {
                        Main.employeeInventory.writeCSV();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    warningLabel.setText("Changes Updated!");
                }catch (SQLException ex){
                    System.out.println("Failed to connect to database or make changes.");
                }
            }
            else if(indexToChange == 2 ){
                if(newValue.getText().equalsIgnoreCase("admin") || newValue.getText().equalsIgnoreCase("front") || newValue.getText().equalsIgnoreCase("back")){
                    tempValues.set(indexToChange, newValue.getText());
                    Connection conn = null;
                    try{
                        conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                        String query = "UPDATE Employee SET employeePosition = ? WHERE employeePosition = ?";
                        PreparedStatement preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1,newValue.getText());
                        preparedStatement.setString(2,Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).getEmployeeName());
                        preparedStatement.executeUpdate();
                        conn.close();
                        Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).setEmployeeId(Integer.parseInt(newValue.getText()));

                        try {
                            Main.employeeInventory.writeCSV();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        warningLabel.setText("Changes Updated!");
                    }catch (SQLException ex){
                        System.out.println("Failed to connect to database or make changes.");
                    }
                }
                else{
                    warningLabel.setText("Position can only be set to 'admin','front',or 'back'.");
                }
            }
        }
        //handles inventory updates
        else{
            if(indexToChange == 1){
                updateButton.setVisible(true);
                try{
                    if( Main.checkEquipmentItemNumber(Integer.parseInt(newValue.getText())) == true
                        || Main.checkFoodItemNumber(Integer.parseInt(newValue.getText())) == true
                        || Main.checkDrinkItemNumber(Integer.parseInt(newValue.getText())) == true){

                        warningLabel.setText("ID number already in use please try again!");
                    }
                    else{
                        tempValues.set(indexToChange,newValue.getText());
                        if(inventory.equalsIgnoreCase("equipment")){
                            Connection conn = null;
                            try{
                                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                String query = "UPDATE Equipment SET itemNumber = ? WHERE itemNumber = ?";
                                PreparedStatement preparedStatement = conn.prepareStatement(query);
                                preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                                preparedStatement.setInt(2,Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                                preparedStatement.executeUpdate();
                                conn.close();
                                Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).setItemNumber(Integer.parseInt(newValue.getText()));
                                try {
                                    Main.equipmentInventory.writeCSV();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                warningLabel.setText("Changes Updated!");
                            }catch (SQLException ex){
                                System.out.println("Failed to connect to database or make changes.");
                            }
                        }
                        else if(inventory.equalsIgnoreCase("food")){
                            Connection conn = null;
                            try{
                                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                String query = "UPDATE Food SET itemNumber = ? WHERE itemNumber = ?";
                                PreparedStatement preparedStatement = conn.prepareStatement(query);
                                preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                                preparedStatement.setInt(2,Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                                preparedStatement.executeUpdate();
                                conn.close();
                                Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).setItemNumber(Integer.parseInt(newValue.getText()));
                                try {
                                    Main.foodInventory.writeCSV();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                warningLabel.setText("Changes Updated!");
                            }catch (SQLException ex){
                                System.out.println("Failed to connect to database or make changes.");
                            }
                        }
                        else if(inventory.equalsIgnoreCase("drink")){
                            Connection conn = null;
                            try{
                                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                String query = "UPDATE Drink SET itemNumber = ? WHERE itemNumber = ?";
                                PreparedStatement preparedStatement = conn.prepareStatement(query);
                                preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                                preparedStatement.setInt(2,Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                                preparedStatement.executeUpdate();
                                conn.close();
                                Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).setItemNumber(Integer.parseInt(newValue.getText()));
                                try {
                                    Main.drinkInventory.writeCSV();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                warningLabel.setText("Changes Updated!");
                            }catch (SQLException ex){
                                System.out.println("Failed to connect to database or make changes.");
                            }
                        }
                    }
                }
                catch(NumberFormatException | NullPointerException ex){
                    warningLabel.setText("Make sure field is not empty or contains spaces and/or letters.");
                }
            }
            else if(indexToChange == 2){
                updateButton.setVisible(true);
                try{
                    tempValues.set(indexToChange,newValue.getText().toLowerCase());
                    if(inventory.equalsIgnoreCase("equipment")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Equipment SET itemName = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1,newValue.getText().toLowerCase());
                            preparedStatement.setInt(2,Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).setItemName(newValue.getText().toLowerCase());
                            try {
                                Main.equipmentInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("food")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Food SET itemName = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1,newValue.getText().toLowerCase());
                            preparedStatement.setInt(2,Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).setItemName(newValue.getText().toLowerCase());
                            try {
                                Main.foodInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("drink")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Drink SET itemName = ? WHERE itemName = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1,newValue.getText().toLowerCase());
                            preparedStatement.setInt(2,Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).setItemName(newValue.getText().toLowerCase());
                            try {
                                Main.drinkInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                }
                catch(NumberFormatException | NullPointerException ex){
                    warningLabel.setText("Make sure field is not empty or contains spaces and/or letters.");
                }
            }
            else if(indexToChange == 3){
                try{
                    updateButton.setVisible(true);
                    tempValues.set(indexToChange,newValue.getText().toLowerCase());
                    if(inventory.equalsIgnoreCase("equipment")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Equipment SET supplierName = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1,newValue.getText().toLowerCase());
                            preparedStatement.setInt(2,Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).setSupplierName(newValue.getText().toLowerCase());
                            try {
                                Main.equipmentInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("food")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Food SET supplierName = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1,newValue.getText().toLowerCase());
                            preparedStatement.setInt(2,Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).setSupplierName(newValue.getText().toLowerCase());
                            try {
                                Main.foodInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("drink")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Drink SET supplierName = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1,newValue.getText().toLowerCase());
                            preparedStatement.setInt(2,Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).setSupplierName(newValue.getText().toLowerCase());
                            try {
                                Main.drinkInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                }
                catch(NumberFormatException | NullPointerException ex){
                    warningLabel.setText("Make sure field is not empty or contains spaces and/or letters.");
                }
            }
            else if(indexToChange == 4){
                try{
                    updateButton.setVisible(true);
                    tempValues.set(indexToChange,newValue.getText());
                    if(inventory.equalsIgnoreCase("equipment")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Equipment SET price = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                            preparedStatement.setInt(2,Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).setPrice(Integer.parseInt(newValue.getText()));
                            try {
                                Main.equipmentInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("food")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Food SET price = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                            preparedStatement.setInt(2,Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).setPrice(Integer.parseInt(newValue.getText()));
                            try {
                                Main.foodInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("drink")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Drink SET price = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                            preparedStatement.setInt(2,Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).setPrice(Integer.parseInt(newValue.getText()));
                            try {
                                Main.drinkInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                }
                catch(NumberFormatException | NullPointerException ex){
                    warningLabel.setText("Make sure field is not empty or contains spaces and/or letters.");
                }
            }
            else if(indexToChange == 5){
                try{
                    updateButton.setVisible(true);
                    tempValues.set(indexToChange,newValue.getText());
                    if(inventory.equalsIgnoreCase("equipment")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Equipment SET quantity = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                            preparedStatement.setInt(2,Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).setQuantity(Integer.parseInt(newValue.getText()));
                            try {
                                Main.equipmentInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("food")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Food SET quantity = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                            preparedStatement.setInt(2,Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).setQuantity(Integer.parseInt(newValue.getText()));
                            try {
                                Main.foodInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("drink")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Drink SET quantity = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setInt(1,Integer.parseInt(newValue.getText()));
                            preparedStatement.setInt(2,Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).setQuantity(Integer.parseInt(newValue.getText()));
                            try {
                                Main.drinkInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                }
                catch(NumberFormatException | NullPointerException ex){
                    warningLabel.setText("Make sure field is not empty or contains spaces and/or letters.");
                }
            }
            else if(indexToChange == 6){
                if(Main.employeePosition.equals("admin")){
                    updateButton.setVisible(true);
                    if(newValue.getText().equalsIgnoreCase("both") || newValue.getText().equalsIgnoreCase("back") || newValue.getText().equalsIgnoreCase("front")){
                        try{

                            tempValues.set(indexToChange,newValue.getText().toLowerCase());
                            if(inventory.equalsIgnoreCase("equipment")){
                                Connection conn = null;
                                try{
                                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                    String query = "UPDATE Equipment SET houseType = ? WHERE itemNumber = ?";
                                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                                    preparedStatement.setString(1,newValue.getText().toLowerCase());
                                    preparedStatement.setInt(2,Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());                            preparedStatement.executeUpdate();
                                    conn.close();
                                    Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).setHouseType(newValue.getText().toLowerCase());
                                    try {
                                        Main.equipmentInventory.writeCSV();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    warningLabel.setText("Changes Updated!");
                                }catch (SQLException ex){
                                    System.out.println("Failed to connect to database or make changes.");
                                }
                            }
                            else if(inventory.equalsIgnoreCase("food")){
                                Connection conn = null;
                                try{
                                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                    String query = "UPDATE Food SET houseType = ? WHERE itemNumber = ?";
                                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                                    preparedStatement.setString(1,newValue.getText().toLowerCase());
                                    preparedStatement.setInt(2,Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                                    preparedStatement.executeUpdate();
                                    conn.close();
                                    Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).setHouseType(newValue.getText().toLowerCase());
                                    try {
                                        Main.foodInventory.writeCSV();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    warningLabel.setText("Changes Updated!");
                                }catch (SQLException ex){
                                    System.out.println("Failed to connect to database or make changes.");
                                }
                            }
                            else if(inventory.equalsIgnoreCase("drink")){
                                Connection conn = null;
                                try{
                                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                    String query = "UPDATE Drink SET houseType = ? WHERE itemNumber = ?";
                                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                                    preparedStatement.setString(1,newValue.getText().toLowerCase());
                                    preparedStatement.setInt(2,Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                                    preparedStatement.executeUpdate();
                                    conn.close();
                                    Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).setHouseType(newValue.getText());
                                    try {
                                        Main.drinkInventory.writeCSV();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    warningLabel.setText("Changes Updated!");
                                }catch (SQLException ex){
                                    System.out.println("Failed to connect to database or make changes.");
                                }
                            }
                        }
                        catch(NumberFormatException | NullPointerException ex){
                            warningLabel.setText("Make sure field is not empty or contains spaces and/or letters.");
                        }
                    }
                    else{
                        warningLabel.setText("The only values allowed are 'both','front',or 'back'.");
                    }
                }
                else{
                    warningLabel.setText("You are not authorized to change this");
                    updateButton.setVisible(false);
                }



            }
            else if(indexToChange == 7){
                try{
                    updateButton.setVisible(true);
                    tempValues.set(indexToChange,newValue.getText().toLowerCase());
                    if(inventory.equalsIgnoreCase("equipment")){
                        Connection conn = null;
                        try{
                            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                            String query = "UPDATE Equipment SET description = ? WHERE itemNumber = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1,newValue.getText().toLowerCase());
                            preparedStatement.setInt(2,Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                            preparedStatement.executeUpdate();
                            conn.close();
                            Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).setDescription(newValue.getText().toLowerCase());
                            try {
                                Main.equipmentInventory.writeCSV();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            warningLabel.setText("Changes Updated!");
                        }catch (SQLException ex){
                            System.out.println("Failed to connect to database or make changes.");
                        }
                    }
                    else if(inventory.equalsIgnoreCase("food")){
                        if(!newValue.getText().equalsIgnoreCase("none") && Main.checkExpirationDate(newValue.getText()) == true){
                            warningLabel.setText("Invalid expiration date.");
                        }
                        else{
                            Connection conn = null;
                            try{
                                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                String query = "UPDATE Food SET expirationDate = ? WHERE itemNumber = ?";
                                PreparedStatement preparedStatement = conn.prepareStatement(query);
                                preparedStatement.setString(1,newValue.getText().toLowerCase());
                                preparedStatement.setInt(2,Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                                preparedStatement.executeUpdate();
                                conn.close();
                                Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).setExpirationDate(newValue.getText());
                                try {
                                    Main.foodInventory.writeCSV();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                warningLabel.setText("Changes Updated!");
                            }catch (SQLException ex){
                                System.out.println("Failed to connect to database or make changes.");
                            }
                        }
                    }

                    else if(inventory.equalsIgnoreCase("drink")){
                        if(!newValue.getText().equalsIgnoreCase("none") && Main.checkExpirationDate(newValue.getText()) == true){
                            warningLabel.setText("Invalid expiration date.");
                        }
                        else{
                            Connection conn = null;
                            try{
                                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                String query = "UPDATE Drink SET expirationDate = ? WHERE itemNumber = ?";
                                PreparedStatement preparedStatement = conn.prepareStatement(query);
                                preparedStatement.setString(1,newValue.getText().toLowerCase());
                                preparedStatement.setInt(2,Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                                preparedStatement.executeUpdate();
                                conn.close();
                                Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).setExpirationDate(newValue.getText());
                                try {
                                    Main.drinkInventory.writeCSV();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                warningLabel.setText("Changes Updated!");
                            }catch (SQLException ex){
                                System.out.println("Failed to connect to database or make changes.");
                            }
                        }

                    }
                }
                catch(NumberFormatException | NullPointerException ex){
                    warningLabel.setText("Make sure field is not empty or contains spaces and/or letters.");
                }

            }

        }


    }

    public void deleteItemButtonOnAction(ActionEvent event){
        if(inventory.equalsIgnoreCase("employee")){
            if(Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).getEmployeeId() == Main.employeeInventory.employeeInventory.get(LoginController.userIndexInList).getEmployeeId()){
                warningLabel.setText("YOU ARE ABOUT TO DELETE YOURSELF AND BE LOGGED OUT AFTER THIS. CONTINUE?");
                yesButton.setVisible(true);
                noButton.setVisible(true);
            }
            else {
                warningLabel.setText("ARE YOU SURE YOU WANT TO DELETE ITEM? CANNOT BE UNDONE!");
                yesButton.setVisible(true);
                noButton.setVisible(true);
            }
        }
        else {
            warningLabel.setText("ARE YOU SURE YOU WANT TO DELETE ITEM? CANNOT BE UNDONE!");
            yesButton.setVisible(true);
            noButton.setVisible(true);
        }

    }

    /**
     * Handles if the yes button is pressed after the delete button is pressed
     * to delete the item being edited at the moment.
     * If a user deletes himself the login scene will show.
     * @param event
     * @throws IOException
     */
    public void yesButtonOnAction(ActionEvent event) throws IOException {
        if(inventory.equalsIgnoreCase("employee")){
            Connection conn = null;
            try{
                if(Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).getEmployeeId() == Main.employeeInventory.employeeInventory.get(LoginController.userIndexInList).getEmployeeId()){
                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                    String query = "DELETE FROM Employee WHERE employeeId = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).getEmployeeId());
                    preparedStatement.executeUpdate();
                    conn.close();
                    Main.employeeInventory.employeeInventory.remove(ViewInventoryController.getInventoryIndex());
                    Parent adminMenuParent = FXMLLoader.load(getClass().getResource("login.fxml"));
                    Scene adminMenuScene = new Scene(adminMenuParent);
                    Stage window = (Stage) goBackButton.getScene().getWindow();
                    window.setScene(adminMenuScene);
                    window.show();
                }
                else{
                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                    String query = "DELETE FROM Employee WHERE employeeId = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,Main.employeeInventory.employeeInventory.get(ViewInventoryController.getInventoryIndex()).getEmployeeId());
                    preparedStatement.executeUpdate();
                    conn.close();
                    Main.employeeInventory.employeeInventory.remove(ViewInventoryController.getInventoryIndex());
                    Parent adminMenuParent = FXMLLoader.load(getClass().getResource("viewInventory.fxml"));
                    Scene adminMenuScene = new Scene(adminMenuParent);
                    Stage window = (Stage) goBackButton.getScene().getWindow();
                    window.setScene(adminMenuScene);
                    window.show();
                }


            }catch (SQLException ex){
                warningLabel.setText("Failed to connect to database or delete item.");
            }
        }
        else{
            if(inventory.equalsIgnoreCase("equipment")){
                Connection conn = null;
                try{
                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                    String query = "DELETE FROM Equipment WHERE itemNumber = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,Main.equipmentInventory.equipmentInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                    preparedStatement.executeUpdate();
                    conn.close();
                    Main.equipmentInventory.equipmentInventory.remove(ViewInventoryController.getInventoryIndex());
                    Parent adminMenuParent = FXMLLoader.load(getClass().getResource("viewInventory.fxml"));
                    Scene adminMenuScene = new Scene(adminMenuParent);
                    Stage window = (Stage) goBackButton.getScene().getWindow();
                    window.setScene(adminMenuScene);
                    window.show();

                }catch (SQLException ex){
                    warningLabel.setText("Failed to connect to database or delete item.");
                }
            }
            else if(inventory.equalsIgnoreCase("food")){
                Connection conn = null;
                try{
                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                    String query = "DELETE FROM Food WHERE itemNumber = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,Main.foodInventory.foodInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                    preparedStatement.executeUpdate();
                    conn.close();
                    Main.foodInventory.foodInventory.remove(ViewInventoryController.getInventoryIndex());
                    Parent adminMenuParent = FXMLLoader.load(getClass().getResource("viewInventory.fxml"));
                    Scene adminMenuScene = new Scene(adminMenuParent);
                    Stage window = (Stage) goBackButton.getScene().getWindow();
                    window.setScene(adminMenuScene);
                    window.show();

                }catch (SQLException ex){
                    warningLabel.setText("Failed to connect to database or delete item.");
                }
            }
            else if(inventory.equalsIgnoreCase("drink")){
                Connection conn = null;
                try{
                    conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                    String query = "DELETE FROM Drink WHERE itemNumber = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,Main.drinkInventory.drinkInventory.get(ViewInventoryController.getInventoryIndex()).getItemNumber());
                    preparedStatement.executeUpdate();
                    conn.close();
                    Main.drinkInventory.drinkInventory.remove(ViewInventoryController.getInventoryIndex());
                    Parent adminMenuParent = FXMLLoader.load(getClass().getResource("viewInventory.fxml"));
                    Scene adminMenuScene = new Scene(adminMenuParent);
                    Stage window = (Stage) goBackButton.getScene().getWindow();
                    window.setScene(adminMenuScene);
                    window.show();

                }catch (SQLException ex){
                    warningLabel.setText("Failed to connect to database or delete item.");
                }
            }
        }

    }

    public void noButtonOnAction(ActionEvent event){
        warningLabel.setText("");
        yesButton.setVisible(true);
        noButton.setVisible(false);
    }

}
