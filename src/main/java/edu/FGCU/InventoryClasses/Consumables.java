package edu.FGCU.InventoryClasses;

public abstract class Consumables extends Inventory {
    protected String expirationDate;


    public Consumables(String inventoryType, int itemNumber, String itemName, String supplierName, int price, int quantity, String houseType, String expirationDate) {
        super(inventoryType, itemNumber, itemName, supplierName, price, quantity, houseType);
        this.expirationDate = expirationDate;
    }

    public Consumables(String[] someInventory) {
        super(someInventory);
        this.expirationDate = someInventory[7];
    }


    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

}

