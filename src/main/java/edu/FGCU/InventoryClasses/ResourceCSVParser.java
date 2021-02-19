package edu.FGCU.InventoryClasses;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

interface ResourceCSVParser {
    String delimiter = ",";

    //Methods for loading, writing, & printing the CSV

    /**
     * Loads lines from csv file into arraylist of objects
     * @throws FileNotFoundException
     */
    void loadCSV() throws FileNotFoundException;

    /**
     *
     * @return List of objects loaded csv file
     */
    List getInventory();

    /**
     * Creates csv file if it doesn't exist or writes headers and writes every object in arraylist to a new line
     * @throws IOException
     */
    void writeCSV() throws IOException;

    /**
     * Prints every arraylist object's toString() method
     */
    void printCSV();

    /**
     * Gets the headers of the csv file
     * @return
     */
    String getHeaders();

    /**
     * Appends to last item added to an arraylist to the end of the csv file
     * @throws IOException
     */
    void appendCSV() throws IOException;

    /**
     * Appends string parameter to the end of the csv file
     * @param line
     * @throws IOException
     */
    void appendCSV(String line) throws IOException;

    }

