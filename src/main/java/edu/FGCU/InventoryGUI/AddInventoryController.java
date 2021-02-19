package edu.FGCU.InventoryGUI;

import edu.FGCU.InventoryClasses.Drink;
import edu.FGCU.InventoryClasses.Equipment;
import edu.FGCU.InventoryClasses.Food;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AddInventoryController {
    @FXML
    public MenuButton menuButton;
    @FXML
    public MenuItem Equipment;
    @FXML
    public MenuItem Food;
    @FXML
    public MenuItem Drink;
    @FXML
    public Label descripOrExpiration;
    @FXML
    public TextField inventoryType;
    @FXML
    public TextField itemNumber;
    @FXML
    public TextField itemName;
    @FXML
    public TextField supplierName;
    @FXML
    public TextField price;
    @FXML
    public TextField quantity;
    @FXML
    public TextField lastTextField;
    @FXML
    public ComboBox houseType;
    @FXML
    public Button addItem;
    @FXML
    public Label warningLabel;
    @FXML
    public Button goBackButton;

    public void goBackButtonOnAction(ActionEvent event) throws IOException {
        Parent adminMenuParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene adminMenuScene = new Scene(adminMenuParent);
        Stage window = (Stage) goBackButton.getScene().getWindow();
        window.setScene(adminMenuScene);
        window.show();
    }

    public void equipmentOnAction(ActionEvent event){
        addItem.setVisible(true);
        inventoryType.setText("equipment");
        menuButton.setText("equipment");
        descripOrExpiration.setText("Description");
        lastTextField.setPromptText("Description");
    }
    public void foodOnAction(ActionEvent event){
        addItem.setVisible(true);
        inventoryType.setText("food");
        menuButton.setText("food");
        descripOrExpiration.setText("Expiration Date");
        lastTextField.setPromptText("MM/dd/yyyy");
    }
    public void drinkOnAction(ActionEvent event){
        addItem.setVisible(true);
        inventoryType.setText("drink");
        menuButton.setText("drink");
        descripOrExpiration.setText("Expiration Date");
        lastTextField.setPromptText("MM/dd/yyyy");
    }

    /**
     * Validates all fields aren't empty, adds the new item to the database,
     * and also adds the new item to the respective array list for later viewing.
     * @param event
     */
    public void addItemOnAction(ActionEvent event){
        try{
            if(itemNumber.getText().length() > 0 && itemName.getText().length()>0 && supplierName.getText().length() > 0
                    && price.getText().length()>0 && quantity.getText().length()>0 && lastTextField.getText().length()>0 && houseType.getSelectionModel().getSelectedItem().toString().length() > 0) {
                if((Main.employeePosition.equals("front") && houseType.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("back"))
                    || (Main.employeePosition.equals("back") && houseType.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("front"))){
                    warningLabel.setText("You can only add items that are \nboth house types or your position.");
                }
                else{
                    if(inventoryType.getText().equalsIgnoreCase("equipment")){
                        if(Main.checkEquipmentItemNumber(Integer.parseInt(itemNumber.getText())) == true ||
                                Main.checkFoodItemNumber(Integer.parseInt(itemNumber.getText())) == true ||
                                Main.checkDrinkItemNumber(Integer.parseInt(itemNumber.getText())) == true){
                            warningLabel.setText("Item number already in use.");
                        }
                        else{
                            Connection conn = null;
                            try{
                                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                String query = "INSERT INTO Equipment (inventoryType,itemNumber,itemName,supplierName,price,quantity,houseType,description)"
                                        + "VALUES(?,?,?,?,?,?,?,?);";
                                PreparedStatement preparedStatement = conn.prepareStatement(query);
                                preparedStatement.setString(1,"equipment");
                                preparedStatement.setInt(2,Integer.parseInt(itemNumber.getText()));
                                preparedStatement.setString(3,itemName.getText().toLowerCase());
                                preparedStatement.setString(4,supplierName.getText().toLowerCase());
                                preparedStatement.setInt(5,Integer.parseInt(price.getText()));
                                preparedStatement.setInt(6,Integer.parseInt(quantity.getText()));
                                preparedStatement.setString(7,houseType.getSelectionModel().getSelectedItem().toString().toLowerCase());
                                preparedStatement.setString(8,lastTextField.getText().toLowerCase());
                                preparedStatement.executeUpdate();
                                conn.close();
                                Main.equipmentInventory.equipmentInventory.add(new Equipment("equipment",Integer.parseInt(itemNumber.getText()),itemName.getText().toLowerCase(),supplierName.getText().toLowerCase(),Integer.parseInt(price.getText()),Integer.parseInt(quantity.getText()),houseType.getSelectionModel().getSelectedItem().toString().toLowerCase(),lastTextField.getText().toLowerCase()));
                                warningLabel.setText("Equipment Item added!");
                            }catch (SQLException ex){
                                System.out.println("Failed to add item or connect to database.");
                            }

                        }
                    }
                    else if(inventoryType.getText().equalsIgnoreCase("food")){
                        if(Main.checkEquipmentItemNumber(Integer.parseInt(itemNumber.getText())) == true ||
                                Main.checkFoodItemNumber(Integer.parseInt(itemNumber.getText())) == true ||
                                Main.checkDrinkItemNumber(Integer.parseInt(itemNumber.getText())) == true){
                            warningLabel.setText("Item number already in use.");
                        }
                        else if(!lastTextField.getText().equalsIgnoreCase("none") && Main.checkExpirationDate(lastTextField.getText()) == true){
                            warningLabel.setText("Date expired or wrong format.\n Use MM/dd/yyyy");
                        }
                        else{
                            Connection conn = null;
                            try{
                                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                String query = "INSERT INTO Food (inventoryType,itemNumber,itemName,supplierName,price,quantity,houseType,expirationDate)"
                                        + "VALUES(?,?,?,?,?,?,?,?);";
                                PreparedStatement preparedStatement = conn.prepareStatement(query);
                                preparedStatement.setString(1,"food");
                                preparedStatement.setInt(2,Integer.parseInt(itemNumber.getText()));
                                preparedStatement.setString(3,itemName.getText().toLowerCase());
                                preparedStatement.setString(4,supplierName.getText().toLowerCase());
                                preparedStatement.setInt(5,Integer.parseInt(price.getText()));
                                preparedStatement.setInt(6,Integer.parseInt(quantity.getText()));
                                preparedStatement.setString(7,houseType.getSelectionModel().getSelectedItem().toString().toLowerCase());
                                preparedStatement.setString(8,lastTextField.getText().toLowerCase());
                                preparedStatement.executeUpdate();
                                conn.close();
                                Main.foodInventory.foodInventory.add(new Food("food",Integer.parseInt(itemNumber.getText()),itemName.getText().toLowerCase(),supplierName.getText().toLowerCase(),Integer.parseInt(price.getText()),Integer.parseInt(quantity.getText()),houseType.getSelectionModel().getSelectedItem().toString().toLowerCase(),lastTextField.getText().toLowerCase()));
                                warningLabel.setText("Food Item added!");
                            }catch (SQLException ex){
                                System.out.println("Failed to add item or connect to database.");
                            }

                        }
                    }
                    else if(inventoryType.getText().equalsIgnoreCase("drink")){
                        if(Main.checkEquipmentItemNumber(Integer.parseInt(itemNumber.getText())) == true ||
                                Main.checkFoodItemNumber(Integer.parseInt(itemNumber.getText())) == true ||
                                Main.checkDrinkItemNumber(Integer.parseInt(itemNumber.getText())) == true){
                            warningLabel.setText("Item number already in use.");
                        }
                        else if(!lastTextField.getText().equalsIgnoreCase("none") && Main.checkExpirationDate(lastTextField.getText()) == true){
                            warningLabel.setText("Date expired or wrong format. Use MM/dd/yyyy");
                        }
                        else{
                            Connection conn = null;
                            try{
                                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                                String query = "INSERT INTO Drink (inventoryType,itemNumber,itemName,supplierName,price,quantity,houseType,expirationDate)"
                                        + "VALUES(?,?,?,?,?,?,?,?);";
                                PreparedStatement preparedStatement = conn.prepareStatement(query);
                                preparedStatement.setString(1,"drink");
                                preparedStatement.setInt(2,Integer.parseInt(itemNumber.getText()));
                                preparedStatement.setString(3,itemName.getText().toLowerCase());
                                preparedStatement.setString(4,supplierName.getText().toLowerCase());
                                preparedStatement.setInt(5,Integer.parseInt(price.getText()));
                                preparedStatement.setInt(6,Integer.parseInt(quantity.getText()));
                                preparedStatement.setString(7,houseType.getSelectionModel().getSelectedItem().toString().toLowerCase());
                                preparedStatement.setString(8,lastTextField.getText().toLowerCase());
                                preparedStatement.executeUpdate();
                                conn.close();
                                Main.drinkInventory.drinkInventory.add(new Drink("drink",Integer.parseInt(itemNumber.getText()),itemName.getText().toLowerCase(),supplierName.getText().toLowerCase(),Integer.parseInt(price.getText()),Integer.parseInt(quantity.getText()),houseType.getSelectionModel().getSelectedItem().toString().toLowerCase(),lastTextField.getText().toLowerCase()));
                                warningLabel.setText("Drink Item added!");
                            }catch (SQLException ex){
                                System.out.println("Failed to add item or connect to database.");
                            }

                        }
                    }
                }

            }
            else{
                warningLabel.setText("One or more fields empty");
            }
        }
        catch(NumberFormatException| NullPointerException ex){
            warningLabel.setText("Make sure to put price in cents and put quantity and item number as whole numbers.");
        }



    }
}
