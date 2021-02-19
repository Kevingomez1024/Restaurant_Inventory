package edu.FGCU.InventoryClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FoodCSVParser implements ResourceCSVParser{
    public String foodFilePath = "./data/foodInventory.csv";
    public List<Food> foodInventory = new ArrayList<>();



    @Override
    public void loadCSV() throws FileNotFoundException {
        FileReader reader = new FileReader(foodFilePath);
        try(BufferedReader bEquipmentReader = new BufferedReader(reader)){
            String line;
            int lineNumber = 0;
            while((line = bEquipmentReader.readLine()) != null){

                if(lineNumber == 0){
                    lineNumber++;
                }
                else{
                    foodInventory.add(new Food(line.split(delimiter)));
                }

            }

        } catch (IOException e) {
            System.out.println("Could not load file <" + foodFilePath + ">\n" + e);
        }

    }


    @Override
    public List getInventory() {
        return foodInventory;
    }

    @Override
    public void writeCSV() throws IOException {
        File outFile = new File(foodFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        FileReader reader = new FileReader(foodFilePath);
        try(BufferedReader bEquipmentReader = new BufferedReader(reader)){
            String line;
            if((line = bEquipmentReader.readLine()) != getHeaders()){
                writer.write(getHeaders());
            }

        } catch (IOException e) {
            System.out.println("Could not load file <" + foodFilePath + ">\n" + e);
        }

        for(Food inventory : foodInventory){
            writer.newLine();
            writer.write(inventory.toString());
        }
        writer.close();
    }

    @Override
    public void printCSV() {
        if (foodInventory.size() <= 0) {
            System.out.println("No food items found\n");
        }
        else {
            for (Food inventory : foodInventory) {
                System.out.println(inventory.toString());
            }

        }
        System.out.println();
    }


    @Override
    public String getHeaders() {
        return "inventoryType"+delimiter+"itemNumber"+delimiter+"itemName"+delimiter+"supplierName"+delimiter+"price"+
                delimiter+"quantity"+delimiter+"houseType"+delimiter+ "expirationDate";

    }

    @Override
    public void appendCSV() throws IOException {
        File outFile = new File(foodFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile,true));
        if(foodInventory.size() != 0){
            writer.newLine();
            writer.write(foodInventory.get((foodInventory.size())-1).toString());

        }
        writer.flush();
        writer.close();
    }

    @Override
    public void appendCSV(String line) throws IOException {
        File outFile = new File(foodFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile,true));
        writer.newLine();
        writer.write(line);
        writer.flush();
        writer.close();
    }

}
