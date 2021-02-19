package edu.FGCU.InventoryClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkCSVParser implements ResourceCSVParser {
    public String drinkFilePath = "./data/drinkInventory.csv";
    public List<Drink> drinkInventory = new ArrayList<>();


    @Override
    public void loadCSV() throws FileNotFoundException {
        FileReader reader = new FileReader(drinkFilePath);
        try(BufferedReader bEquipmentReader = new BufferedReader(reader)){
            String line;
            int lineNumber = 0;
            while((line = bEquipmentReader.readLine()) != null){

                if(lineNumber == 0){
                    lineNumber++;
                }
                else{
                    drinkInventory.add(new Drink(line.split(delimiter)));
                }

            }

        } catch (IOException e) {
            System.out.println("Could not load file <" + drinkFilePath + ">\n" + e);
        }

    }


    @Override
    public List getInventory() {
        return drinkInventory;
    }

    @Override
    public void writeCSV() throws IOException {
        File outFile = new File(drinkFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        FileReader reader = new FileReader(drinkFilePath);
        try(BufferedReader bEquipmentReader = new BufferedReader(reader)){
            String line;
            if((line = bEquipmentReader.readLine()) != getHeaders()){
                writer.write(getHeaders());
            }

        } catch (IOException e) {
            System.out.println("Could not load file <" + drinkFilePath + ">\n" + e);
        }

        for(Drink inventory : drinkInventory){
            writer.newLine();
            writer.write(inventory.toString());
        }
        writer.close();
    }

    @Override
    public void printCSV() {
        if (drinkInventory.size() <= 0) {
            System.out.println("No drink items found\n");
        }
        else {
            for (Drink inventory : drinkInventory) {
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
        File outFile = new File(drinkFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile,true));
        if(drinkInventory.size() != 0){
            writer.newLine();
            writer.write(drinkInventory.get((drinkInventory.size())-1).toString());

        }
        writer.flush();
        writer.close();
    }

    @Override
    public void appendCSV(String line) throws IOException {
        File outFile = new File(drinkFilePath);
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
