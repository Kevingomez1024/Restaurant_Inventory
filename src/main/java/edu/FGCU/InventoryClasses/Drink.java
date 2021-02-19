package edu.FGCU.InventoryClasses;

public class Drink extends Consumables{


    public Drink(String inventoryType,int itemNumber, String itemName, String supplierName, int price, int quantity,String houseType, String expirationDate) {
        super(inventoryType,itemNumber, itemName, supplierName, price, quantity, houseType, expirationDate);
    }

    public Drink(String[] drinkInventoryObject) {
        super(drinkInventoryObject);
    }

    @Override
    public String toString(){
        return (getInventoryType() +"," + getItemNumber() + "," + getItemName() + "," +getSupplierName() +"," + getPrice()+ "," + getQuantity()+ "," + getHouseType() + "," + getExpirationDate()).toLowerCase();
    }

}
