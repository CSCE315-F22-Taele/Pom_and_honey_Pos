/**
 * @author
 * Nick, Aadith, Ismat, Nebiyou
*/
public class drink {

    private String flavor;
    private static int flavorId;
    private String[] flavorOptions = { "None", "Bottled Water", "Bottled Soda", "Fountain Soda" };
    private int[] drinksInventoryList = { 0, 0, 0 };

    private String seasonalFlavor="";
    // private double seasonal price=0;
    
    public drink(int flavorId) { // itemId shold be enough to set name and price
        setFlavor(flavorId);// dry coding :)
        this.flavorId = flavorId;
    }

    public void setFlavor(int flavorId) { // we should only need to use this to change everything
        this.flavorId = flavorId;
        if (flavorId == 1) {
            this.flavor = flavorOptions[1];
        } else if (flavorId == 2) {
            this.flavor = flavorOptions[2];
        } else if (flavorId == 3) {
            this.flavor = flavorOptions[3];
        }
    }

    public void setDrinksInventoryList() {
        // grab bottled water count w/ SELECT "Drink Inventory" from "Drinks" where
        // "Drink ID" = 1;
        // grab bottled soda count w/ SELECT "Drink Inventory" from "Drinks" where
        // "Drink ID" = 2;
        // grab fountain sode count w/ SELECT "Drink Inventory" from "Drinks" where
        // "Drink ID" = 3;
    }

    public void updateDrinksInventoryList() {
        if (flavorId == 1) {
            drinksInventoryList[0] = drinksInventoryList[0] - 1;
            // UPDATE "Drinks" SET "Drink Inventory" = drinksInventoryList[0] WHERE
            // "Dressing ID" = 1;
        } else if (flavorId == 2) {
            drinksInventoryList[1] = drinksInventoryList[1] - 1;
            // UPDATE "Drinks" SET "Drink Inventory" = drinksInventoryList[1] WHERE
            // "Dressing ID" = 2;
        } else if (flavorId == 3) {
            drinksInventoryList[2] = drinksInventoryList[2] - 1;
            // UPDATE "Drinks" SET "Drink Inventory" = drinksInventoryList[2] WHERE
            // "Dressing ID" = 3;
        }
    }

    public String getFlavor() {
        return flavor;
    }

    public int getFlavorId() {
        return flavorId;
    }

    public String toString() {// use for testing implicitly called by print
        return "Drink flavor: " + flavor + ", Drink id: " + flavorId;
    }

    public String toInputString() { // u se to prepare to add order to sql
        return flavorId + "";
    }
    // public static void main(String[] args) {
    // drink newDrink = new drink(1);
    // System.out.println(newDrink.toInputString());
    // }

}
