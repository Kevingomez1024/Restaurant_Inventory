package edu.FGCU.InventoryGUI;
import edu.FGCU.InventoryClasses.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;


public class ViewInventoryController {

    private static String lineToEdit = "";
    private static int inventoryIndex = -1;

    @FXML
    private ComboBox viewChoice;
    @FXML
    private ListView<String> listView;
    @FXML
    private Label listTitle;
    @FXML
    private Button goBackButton;
    @FXML
    private Label editLabel;
    @FXML
    private Button editButton;



    public void showEquipment(){
        listTitle.setText("Equipment Inventory");
        listView.getItems().clear();
        if (Main.employeeInventory.getInventory().size() > 0) {
            listView.getItems().add(Main.equipmentInventory.getHeaders());
            if(Main.employeePosition.equalsIgnoreCase("admin")){
                for (Equipment equipment: Main.equipmentInventory.equipmentInventory) {
                    listView.getItems().add(equipment.toString());
                }
            }else if(Main.employeePosition.equalsIgnoreCase("front")){
                for (Equipment equipment: Main.equipmentInventory.equipmentInventory) {
                    if(equipment.getHouseType().equalsIgnoreCase("front") || equipment.getHouseType().equalsIgnoreCase("both")){
                        listView.getItems().add(equipment.toString());
                    }
                }
            }
            else if(Main.employeePosition.equalsIgnoreCase("back")){
                for (Equipment equipment: Main.equipmentInventory.equipmentInventory) {
                    if(equipment.getHouseType().equalsIgnoreCase("back") || equipment.getHouseType().equalsIgnoreCase("both")){
                        listView.getItems().add(equipment.toString());
                    }
                }
            }
        }
        else{
            listView.getItems().add("No equipment found");
        }
    }
    public void showFood(){
        listTitle.setText("Food Inventory");
        listView.getItems().clear();
        if (Main.foodInventory.getInventory().size() > 0) {
            listView.getItems().add(Main.foodInventory.getHeaders());
            if(Main.employeePosition.equalsIgnoreCase("admin")){
                for (Food food: Main.foodInventory.foodInventory) {
                    listView.getItems().add(food.toString());
                }
            }else if(Main.employeePosition.equalsIgnoreCase("front")){
                for (Food food: Main.foodInventory.foodInventory) {
                    if(food.getHouseType().equalsIgnoreCase("front") || food.getHouseType().equalsIgnoreCase("both")){
                        listView.getItems().add(food.toString());
                    }
                }
            }
            else if(Main.employeePosition.equalsIgnoreCase("back")){
                for (Food food: Main.foodInventory.foodInventory) {
                    if(food.getHouseType().equalsIgnoreCase("back") || food.getHouseType().equalsIgnoreCase("both")){
                        listView.getItems().add(food.toString());
                    }
                }
            }
        }
        else{
            listView.getItems().add("No foods found");
        }
    }

    public void showDrink(){
        listTitle.setText("Drink Inventory");
        listView.getItems().clear();
        if (Main.drinkInventory.getInventory().size() > 0) {
            listView.getItems().add(Main.drinkInventory.getHeaders());
            if(Main.employeePosition.equalsIgnoreCase("admin")){
                for (Drink drink: Main.drinkInventory.drinkInventory) {
                    listView.getItems().add(drink.toString());
                }
            }else if(Main.employeePosition.equalsIgnoreCase("front")){
                for (Drink drink : Main.drinkInventory.drinkInventory) {
                    if(drink.getHouseType().equalsIgnoreCase("front") || drink.getHouseType().equalsIgnoreCase("both")){
                        listView.getItems().add(drink.toString());
                    }
                }
            }
            else if(Main.employeePosition.equalsIgnoreCase("back")){
                for (Drink drink: Main.drinkInventory.drinkInventory) {
                    if(drink.getHouseType().equalsIgnoreCase("back") || drink.getHouseType().equalsIgnoreCase("both")){
                        listView.getItems().add(drink.toString());
                    }
                }
            }

        }
        else{
            listView.getItems().add("No drinks found");
        }
    }
    public void showEmployee() {
        listTitle.setText("Employee List");
        listView.getItems().clear();
        if(Main.employeePosition.equalsIgnoreCase("admin")){
            if (Main.employeeInventory.getInventory().size() > 0) {
                listView.getItems().add(Main.employeeInventory.getHeaders());
                for (Employee employee : Main.employeeInventory.employeeInventory) {
                    listView.getItems().add(employee.toString());
                }
            }
            else{
                listView.getItems().add("No employees found");
            }
        }
        else{
            listView.getItems().add("You are not authorized to view this.");
        }

    }

    /**
     * Loads the arraylists from the respective inventories into the list view with the headers
     * @param event
     */
    public void showList(ActionEvent event) {
        try{
            if(viewChoice.getValue().toString().equalsIgnoreCase("Equipment Items")){
                showEquipment();
            }else if(viewChoice.getValue().toString().equalsIgnoreCase("Food Items")){
                showFood();
            }
            else if(viewChoice.getValue().toString().equalsIgnoreCase("Drink Items")){
                showDrink();
            }
            else if(viewChoice.getValue().toString().equalsIgnoreCase("Employee List")){
                showEmployee();
            }
        }
        catch(NullPointerException ex){
            listTitle.setText("Choose An Inventory");
        }


    }

    public void goBackButtonOnAction(ActionEvent event) throws IOException {
        Parent adminMenuParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene adminMenuScene = new Scene(adminMenuParent);
        Stage window = (Stage) goBackButton.getScene().getWindow();
        window.setScene(adminMenuScene);
        window.show();
    }

    /**
     * Takes the line clicked in the listview to the edit scene.
     * @param event
     * @throws IOException
     */
    public void editButtonOnAction(ActionEvent event) throws IOException{
        try{
            if(    listView.getSelectionModel().getSelectedItem().equalsIgnoreCase(Main.employeeInventory.getHeaders())
                || listView.getSelectionModel().getSelectedItem().equalsIgnoreCase(Main.foodInventory.getHeaders())
                || listView.getSelectionModel().getSelectedItem().equalsIgnoreCase(Main.drinkInventory.getHeaders())
                || listView.getSelectionModel().getSelectedItem().equalsIgnoreCase(Main.equipmentInventory.getHeaders())
                || listView.getSelectionModel().getSelectedItem().equalsIgnoreCase("You are not authorized to view this.")){
                editLabel.setText("You cannot edit the headers.");
            }
            else{
                lineToEdit = listView.getSelectionModel().getSelectedItem();
                if(listView.getSelectionModel().getSelectedItem().split(",").length == 4){
                    for(Employee employee : Main.employeeInventory.employeeInventory){
                        if(Integer.parseInt(listView.getSelectionModel().getSelectedItem().split(",")[0]) == employee.getEmployeeId()){
                            inventoryIndex = Main.employeeInventory.employeeInventory.indexOf(employee);
                            break;
                        }
                    }
                }
                else if(listView.getSelectionModel().getSelectedItem().split(",")[0].equalsIgnoreCase("equipment")){
                    for(Equipment equipment : Main.equipmentInventory.equipmentInventory){
                        if(Integer.parseInt(listView.getSelectionModel().getSelectedItem().split(",")[1]) == equipment.getItemNumber()){
                            inventoryIndex = Main.equipmentInventory.equipmentInventory.indexOf(equipment);
                            break;
                        }
                    }
                }
                else if(listView.getSelectionModel().getSelectedItem().split(",")[0].equalsIgnoreCase("food")){
                    for(Food food : Main.foodInventory.foodInventory){
                        if(Integer.parseInt(listView.getSelectionModel().getSelectedItem().split(",")[1]) == food.getItemNumber()){
                            inventoryIndex = Main.foodInventory.foodInventory.indexOf(food);
                            break;
                        }
                    }
                }
                else if(listView.getSelectionModel().getSelectedItem().split(",")[0].equalsIgnoreCase("drink")){
                    for(Drink drink : Main.drinkInventory.drinkInventory){
                        if(Integer.parseInt(listView.getSelectionModel().getSelectedItem().split(",")[1]) == drink.getItemNumber()){
                            inventoryIndex = Main.drinkInventory.drinkInventory.indexOf(drink);
                            break;
                        }
                    }
                }

                Parent editItemParent = FXMLLoader.load(getClass().getResource("editItem.fxml"));
                Scene editItemScene = new Scene(editItemParent);
                Stage window = (Stage) editButton.getScene().getWindow();
                window.setScene(editItemScene);
                window.show();
            }

        }catch(NullPointerException ex){
            editLabel.setText("Click an item if you want to edit it.");
        }

    }

    public static String getLineToEdit() {
        return lineToEdit;
    }

    public static int getInventoryIndex() {
        return inventoryIndex;
    }
}
