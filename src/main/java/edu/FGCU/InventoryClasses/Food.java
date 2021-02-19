package edu.FGCU.InventoryClasses;

public class Food extends Consumables {

    public Food(String inventoryType,int itemNumber, String itemName, String supplierName, int price, int quantity, String houseType, String expirationDate) {
        super(inventoryType,itemNumber, itemName, supplierName, price, quantity,houseType, expirationDate);
    }

    public Food(String[] foodInventoryObject) {
        super(foodInventoryObject);
    }

    @Override
    public String toString(){
        return (getInventoryType() +"," + getItemNumber() + "," + getItemName() + "," +getSupplierName() +"," + getPrice()+ "," + getQuantity()+ "," + getHouseType() + "," + getExpirationDate()).toLowerCase();
    }

}
