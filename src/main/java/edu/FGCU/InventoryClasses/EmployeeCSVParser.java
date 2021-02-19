package edu.FGCU.InventoryClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeCSVParser implements ResourceCSVParser{
    public String employeeFilePath = "./data/employeeInventory.csv";
    public ArrayList<Employee> employeeInventory = new ArrayList<>();




    @Override
    public void loadCSV() throws FileNotFoundException {
        FileReader reader = new FileReader(employeeFilePath);
        try(BufferedReader bEquipmentReader = new BufferedReader(reader)){
            String line;
            int lineNumber = 0;
            while((line = bEquipmentReader.readLine()) != null){

                if(lineNumber == 0){
                    lineNumber++;
                }
                else{
                    employeeInventory.add(new Employee(line.split(delimiter)));
                }

            }

        } catch (IOException e) {
            System.out.println("Could not load file <" + employeeFilePath + ">\n" + e);
        }
    }

    @Override
    public List getInventory() {
        return employeeInventory;
    }

    @Override
    public void writeCSV() throws IOException {
        File outFile = new File(employeeFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        FileReader reader = new FileReader(employeeFilePath);
        try(BufferedReader bEquipmentReader = new BufferedReader(reader)){
            String line;
            if((line = bEquipmentReader.readLine()) != getHeaders()){
                writer.write(getHeaders());
            }

        } catch (IOException e) {
            System.out.println("Could not load file <" + employeeFilePath + ">\n" + e);
        }

        for(Employee inventory : employeeInventory){
            writer.newLine();
            writer.write(inventory.toString());
        }
        writer.close();

    }

    @Override
    public void printCSV() {
        if (employeeInventory.size() <= 0) {
            System.out.println("No employees found\n");
        }
        else {
            for (Employee inventory : employeeInventory) {
                System.out.println(inventory.toString());
            }

        }
        System.out.println();
    }

    @Override
    public String getHeaders() {
        return "employeeId"+delimiter+"employeeName"+delimiter+"employeePosition"+delimiter+"password";
    }

    @Override
    public void appendCSV() throws IOException {
        File outFile = new File(employeeFilePath);
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile,true));
        if(employeeInventory.size() != 0){
            writer.newLine();
            writer.write(employeeInventory.get((employeeInventory.size())-1).toString());

        }
        writer.flush();
        writer.close();

    }

    @Override
    public void appendCSV(String line) throws IOException {
        File outFile = new File(employeeFilePath);
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
