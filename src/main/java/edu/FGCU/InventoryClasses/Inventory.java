package edu.FGCU.InventoryClasses;

public abstract class Inventory{

    private String inventoryType;
    private int itemNumber;
    private String itemName;
    private String supplierName;
    private int price;
    private int quantity;
    private String houseType;

    public Inventory(){}

    public Inventory(String inventoryType, int itemNumber, String itemName,String supplierName,int price, int quantity,String houseType) {
        this.itemNumber = itemNumber;
        this.itemName = itemName;
        this.supplierName = supplierName;
        this.price = price;
        this.quantity = quantity;
        this.houseType = houseType;

    }

    public Inventory(String[] someInventory) {
        inventoryType = someInventory[0];
        itemNumber = Integer.parseInt(someInventory[1]);
        itemName = someInventory[2];
        supplierName = someInventory[3];
        price = Integer.parseInt(someInventory[4]);
        quantity = Integer.parseInt(someInventory[5]);
        houseType = someInventory[6];

    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSupplierName(){
        return supplierName;
    }

    public void setSupplierName(String supplierName){
        this.supplierName = supplierName;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }
}
