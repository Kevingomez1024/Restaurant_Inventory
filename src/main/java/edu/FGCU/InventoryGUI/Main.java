package edu.FGCU.InventoryGUI;

import edu.FGCU.InventoryClasses.*;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;



public class Main extends Application {

    static EquipmentCSVParser equipmentInventory = new EquipmentCSVParser();
    static FoodCSVParser foodInventory = new FoodCSVParser();
    static DrinkCSVParser drinkInventory = new DrinkCSVParser();
    static EmployeeCSVParser employeeInventory = new EmployeeCSVParser();
    public static String employeePosition = "";

    /**
     * Connects to database,creates tables if they don't exist, loads respective data into their arraylists
     * and loads the first scene which is the login screen
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");

            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Employee ("
                    + " employeeId  INTEGER PRIMARY KEY,"
                    + "employeeName VARCHAR,"
                    + "employeePosition  VARCHAR,"
                    + "password     VARCHAR"
                    + "  );");

            stmt.execute("CREATE TABLE IF NOT EXISTS Equipment ("
                    + "       inventoryType VARCHAR       PRIMARY KEY,"
                    + "       itemNumber    INTEGER ,"
                    + "       itemName      VARCHAR,"
                    + "       supplierName  VARCHAR,"
                    + "       price         INTEGER,"
                    + "       quantity      INTEGER,"
                    + "       houseType     VARCHAR,"
                    + "       description   VARCHAR"
                    + " );");

            stmt.execute("CREATE TABLE IF NOT EXISTS Food ("
                    + "       inventoryType   VARCHAR,"
                    + "       itemNumber      INTEGER   PRIMARY KEY,"
                    + "       itemName        VARCHAR,"
                    + "       supplierName    VARCHAR,"
                    + "       price           INTEGER,"
                    + "       quantity        INTEGER,"
                    + "       houseType       VARCHAR,"
                    + "       expirationDate  VARCHAR"
                    + " );");

            stmt.execute("CREATE TABLE IF NOT EXISTS Drink ("
                    + "       inventoryType   VARCHAR,"
                    + "       itemNumber      INTEGER   PRIMARY KEY,"
                    + "       itemName        VARCHAR,"
                    + "       supplierName    VARCHAR,"
                    + "       price           INTEGER,"
                    + "       quantity        INTEGER,"
                    + "       houseType       VARCHAR,"
                    + "       expirationDate  VARCHAR"
                    + " );");

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Could not connect to database or create tables");
            e.printStackTrace();
        }


        try{
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
            try{
                Statement selectStmt = conn.createStatement();
                ResultSet resultSet = selectStmt.executeQuery("SELECT expirationDate, itemNumber FROM Food");
                while(resultSet.next()){
                    if(!resultSet.getString("expirationDate").equalsIgnoreCase("none")){
                        if(checkExpirationDate(resultSet.getString("expirationDate")) == true){
                            editExpiredQuantity(resultSet.getInt("itemNumber"),"Food");
                        }
                    }
                }

                resultSet = selectStmt.executeQuery("SELECT expirationDate, itemNumber FROM Drink");
                while(resultSet.next()){
                    if(!resultSet.getString("expirationDate").equalsIgnoreCase("none")){
                        if(checkExpirationDate(resultSet.getString("expirationDate")) == true){
                            editExpiredQuantity(resultSet.getInt("itemNumber"),"Drink");
                        }
                    }
                }

            }
            catch (SQLException ex){
                System.out.println("Could not edit item quantities or connect to database.");
            }



            //Load data from database into csv files
            try{
                Statement stmt = conn.createStatement();
                //Executing the query
                //stmt.executeQuery("use Inventory");
                ResultSet rs = stmt.executeQuery("SELECT * FROM Equipment;");
                String equipmentData[] = new String[8];
                while(rs.next()) {
                    equipmentData[0] = rs.getString("inventoryType");
                    equipmentData[1] = Integer.toString(rs.getInt("itemNumber"));
                    equipmentData[2] = rs.getString("itemName");
                    equipmentData[3] = rs.getString("supplierName");
                    equipmentData[4] = Integer.toString(rs.getInt("price"));
                    equipmentData[5] = Integer.toString(rs.getInt("quantity"));
                    equipmentData[6] = rs.getString("houseType");
                    equipmentData[7] = rs.getString("description");
                    equipmentInventory.getInventory().add(new Equipment(equipmentData));
                }
                equipmentInventory.writeCSV();
                System.out.println("Equipment Data Loaded");
            }
            catch(Exception e){
                System.out.println("Could not load equipment data from database.");
            }
            try{
                Statement stmt = conn.createStatement();
                //Executing the query
                ResultSet rs = stmt.executeQuery("SELECT * FROM Food;");
                String foodData[] = new String[8];
                while(rs.next()) {
                    foodData[0] = rs.getString("inventoryType");
                    foodData[1] = Integer.toString(rs.getInt("itemNumber"));
                    foodData[2] = rs.getString("itemName");
                    foodData[3] = rs.getString("supplierName");
                    foodData[4] = Integer.toString(rs.getInt("price"));
                    foodData[5] = Integer.toString(rs.getInt("quantity"));
                    foodData[6] = rs.getString("houseType");
                    foodData[7] = rs.getString("expirationDate");
                    foodInventory.getInventory().add(new Food(foodData));
                }
                foodInventory.writeCSV();
                System.out.println("Food Data Loaded");
            }
            catch(Exception e){
                System.out.println("Could not load food data from database.");
            }
            try{
                Statement stmt = conn.createStatement();
                //Executing the query
                //stmt.executeQuery("use Inventory");
                ResultSet rs = stmt.executeQuery("SELECT * FROM Drink;");
                String drinkData[] = new String[8];
                while(rs.next()) {
                    drinkData[0] = rs.getString("inventoryType");
                    drinkData[1] = Integer.toString(rs.getInt("itemNumber"));
                    drinkData[2] = rs.getString("itemName");
                    drinkData[3] = rs.getString("supplierName");
                    drinkData[4] = Integer.toString(rs.getInt("price"));
                    drinkData[5] = Integer.toString(rs.getInt("quantity"));
                    drinkData[6] = rs.getString("houseType");
                    drinkData[7] = rs.getString("expirationDate");
                    drinkInventory.getInventory().add(new Drink(drinkData));
                }
                drinkInventory.writeCSV();
                System.out.println("Drink Data Loaded");
            }
            catch(Exception e){
                System.out.println("Could not load drink data from database.");
            }
            try{
                Statement stmt = conn.createStatement();
                //Executing the query
                ResultSet rs = stmt.executeQuery("SELECT * FROM Employee;");
                String empData[] = new String[4];
                while(rs.next()) {
                    empData[0] = Integer.toString(rs.getInt("employeeId"));
                    empData[1] = rs.getString("employeeName");
                    empData[2] = rs.getString("employeePosition");
                    empData[3] = rs.getString("password");
                    empData[3] = "hidden";
                    //Do not copy passwords to csv
                    employeeInventory.getInventory().add(new Employee(empData));
                }
                employeeInventory.writeCSV();
                System.out.println("Employee Data Loaded");
            }
            catch(Exception e){
                System.out.println("Could not load employee data from database.");
            }

            conn.close();

        }catch(SQLException ex){
            System.out.println("Could not load or close database.");
        }


        Parent root = FXMLLoader.load((getClass().getResource("login.fxml")));
        primaryStage.setTitle("Restaurant Inventory");
        primaryStage.setScene(new Scene(root, 600, 480));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Takes a userID, checks if its present in the database using the checkID() method,
     * then checks if the password parameter matches the password
     * in the database with the corresponding ID.
     * @param userId
     * @param password
     * @return Returns true or false if login is succesful.
     */
    public static boolean checklogin(int userId, String password){
        boolean loginSuccesful = false;
        Connection conn = null;
        if(checkID(userId) == true){
            try{
                conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
                String query = "SELECT * FROM EMPLOYEE WHERE employeeId = ? AND password = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1,userId);
                preparedStatement.setString(2,password);
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    loginSuccesful = true;
                    employeePosition = resultSet.getString("employeePosition");
                }
                conn.close();
            }catch (SQLException ex){
                System.out.println("Failed to connect to database.");
            }
        }

        return loginSuccesful;
    }

    /**
     * Checks if the user Id is present in the database
     * @param Id
     * @return Returns true or false if the ID is found in the database
     */
    public static boolean checkID(int Id){
        boolean idFound = false;
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
            String query = "SELECT * FROM EMPLOYEE WHERE employeeId = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,Id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                idFound = true;
            }
            conn.close();
        }catch (SQLException ex){
            System.out.println("Failed to connect to database.");
        }

        return idFound;
    }

    /**
     * Checks if equipment item number is present in the database
     * @param equipmentItemNumber
     * @return Returns true or false if equipment item number is found
     */
    public static boolean checkEquipmentItemNumber(int equipmentItemNumber){
        boolean itemNumberFound = false;
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
            String query = "SELECT * FROM Equipment WHERE itemNumber = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,equipmentItemNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                itemNumberFound = true;
            }
            conn.close();
        }catch (SQLException ex){
            System.out.println("Failed to connect to database.");
        }

        return itemNumberFound;
    }

    /**
     * Checks if drink item number is present in the database
     * @param drinkItemNumber
     * @return Returns true or false if drink item number is found
     */
    public static boolean checkDrinkItemNumber(int drinkItemNumber){
        boolean itemNumberFound = false;
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
            String query = "SELECT * FROM Drink WHERE itemNumber = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,drinkItemNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                itemNumberFound = true;
            }
            conn.close();
        }catch (SQLException ex){
            System.out.println("Failed to connect to database.");
        }

        return itemNumberFound;
    }

    /**
     * Checks if food item number is present in the database
     * @param foodItemNumber
     * @return Returns true or false if food item number is found
     */
    public static boolean checkFoodItemNumber(int foodItemNumber){
        boolean itemNumberFound = false;
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
            String query = "SELECT * FROM Food WHERE itemNumber = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,foodItemNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                itemNumberFound = true;
            }
            conn.close();
        }catch (SQLException ex){
            System.out.println("Failed to connect to database.");
        }

        return itemNumberFound;
    }

    /**
     * Uses ascii values to check the password requirements of 8+ characters,and
     *  at least 1 uppercase, 1 lowercase, 1 number. Also checks the only special
     *  characters used are !,@,#,.,,
     * @param password
     * @return Returns true or false is password is bad
     */
    public static boolean checkPassword(String password){
        boolean badPassword = true;
        int lowercaseCount = 0;
        int upperCaseCount = 0;
        int specialCount = 0;
        int numCount = 0;
        int invalidCount = 0;

        for(int i = 0; i <= password.length()-1 ; i++){

            try{
                int checkForNum = Integer.parseInt(Character.toString(password.charAt(i)));
                if(checkForNum >= 0 && checkForNum <= 9 ){
                    numCount++;
                }
            }
            catch(NumberFormatException ex){
                int castAscii = (int)password.charAt(i);
                if(castAscii >= 65 && castAscii <= 90){
                    upperCaseCount ++;
                }
                else if(castAscii >= 97 && castAscii <= 122){
                    lowercaseCount++;
                }
                else if(castAscii == 33 || castAscii == 35 || castAscii == 46 || castAscii == 44 || castAscii == 64){
                    specialCount++;
                }
                else{
                    invalidCount++;
                    break;
                }
            }
        }
        if(invalidCount == 0){
            if(lowercaseCount != 0 && upperCaseCount != 0 && numCount != 0 && password.length() >= 8){
                badPassword = false;
            }
        }
        return badPassword;
    }

    /**
     * Takes in a date of the format MM/dd/yyyy and compares it with today's date.
     * @param date
     * @return Returns true or false if date parameter is expired
     */
    public static boolean checkExpirationDate(String date){
        boolean isExpired = true;
        LocalDate today = LocalDate.now();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");


        try{
            Date todayDate = formatter.parse(today.format(dateTimeFormatter));
            Date expirationDate = formatter.parse(date);
            if(todayDate.compareTo(expirationDate) < 0){
                isExpired = false;
            }
        }
        catch(ParseException ex){
            isExpired = true;
        }


        return isExpired;
    }

    /**
     * Method is called if item is expired. Will set the quantity of the item to 0 in the database
     * @param itemNumber
     * @param inventoryName
     */
    public static void editExpiredQuantity(int itemNumber,String inventoryName){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Inventory.db");
            String query = "UPDATE ? SET quantity = ? WHERE itemNumber = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,inventoryName);
            preparedStatement.setInt(2,0);
            preparedStatement.setInt(3,itemNumber);
            preparedStatement.executeUpdate();
            conn.close();
        }catch (SQLException ex){
            System.out.println("Failed to connect to database.");
        }
    }

}
