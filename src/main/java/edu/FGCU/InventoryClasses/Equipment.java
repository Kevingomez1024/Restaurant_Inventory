package edu.FGCU.InventoryClasses;

public class Equipment extends Inventory {
    private String description;

    public Equipment(){}

    public Equipment(String inventoryType, int itemNumber, String itemName, String supplierName, int price, int quantity,String houseType,String description) {
        super(inventoryType,itemNumber, itemName, supplierName, price, quantity,houseType);
        this.description = description;
    }

    public Equipment(String[] equipmentInventoryObject) {
        super(equipmentInventoryObject);
        this.description = equipmentInventoryObject[7];
    }

    @Override
    public String toString(){
        return (getInventoryType() +"," + getItemNumber() + "," + getItemName() + "," +getSupplierName() +"," + getPrice()+ "," + getQuantity() + "," + getHouseType() + "," + description).toLowerCase();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}




