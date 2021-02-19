package edu.FGCU.InventoryClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentCSVParser implements ResourceCSVParser {
    public String equipmentFilePath = "./data/equipmentInventory.csv";
    public List<Equipment> equipmentInventory = new ArrayList<>();

    public EquipmentCSVParser(){}




    @Override
    public void loadCSV() throws FileNotFoundException {
        FileReader reader = new FileReader(equipmentFilePath);
        try(BufferedReader bEquipmentReader = new BufferedReader(reader)){
            String line;
            int lineNumber = 0;
            while((line = bEquipmentReader.readLine()) != null){

                if(lineNumber == 0){
                    lineNumber++;
                }
                else{
                    equipmentInventory.add(new Equipment(line.split(delimiter)));
                }

            }

        } catch (IOException e) {
            File outFile = new File(equipmentFilePath);
            System.out.println("Could not load file <" + equipmentFilePath + ">\n" + e);
        }

    }

    @Override
    public List getInventory() {
        return equipmentInventory;
    }

    @Override
    public void writeCSV() throws IOException {
        File outFile = new File(equipmentFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        FileReader reader = new FileReader(equipmentFilePath);
        try(BufferedReader bEquipmentReader = new BufferedReader(reader)){
            String line;
            if((line = bEquipmentReader.readLine()) != getHeaders()){
                writer.write(getHeaders());
            }

        } catch (IOException e) {
            System.out.println("Could not load file <" + equipmentFilePath + ">\n" + e);
        }

        for(Equipment inventory : equipmentInventory){
            writer.newLine();
            writer.write(inventory.toString());
        }
        writer.close();


    }

    @Override
    public void printCSV() {
        if (equipmentInventory.size() <= 0) {
            System.out.println("No equipment items found\n");
        }
        else {
            for (Equipment inventory : equipmentInventory) {
                System.out.println(inventory.toString());
            }

        }
        System.out.println();
    }

    @Override
    public String getHeaders() {
        return "inventoryType"+delimiter+"itemNumber"+delimiter+"itemName"+
                delimiter+"supplierName"+delimiter+"price"+delimiter+"quantity"+delimiter+
                "houseType"+delimiter+"description";

    }

    @Override
    public void appendCSV() throws IOException {
        File outFile = new File(equipmentFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile,true));
        if(equipmentInventory.size() != 0){
            writer.newLine();
            writer.write(equipmentInventory.get((equipmentInventory.size())-1).toString());

        }
        writer.flush();
        writer.close();

    }

    @Override
    public void appendCSV(String line) throws IOException {
        File outFile = new File(equipmentFilePath);
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
