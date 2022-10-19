/**
 * A helper class that provides functionality for drinks in the entree and overall order
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
    
    /**
    * Returns a drink object that can then be implemented in the order.java file
    * @param flavorId the database assigned integer given to the drink
     */
    public drink(int flavorId) { // itemId shold be enough to set name and price
        setFlavor(flavorId);// dry coding :)
        this.flavorId = flavorId;
    }
    /**
    * A setter function for a flavodID
    * @param flavorId the flavorId that needs to be assigned
    */
    public void setFlavor(int flavorId) { // we should only need to use this to change everything
        this.flavorId = flavorId;
        if(flavorId==-1){
            this.flavor="Seasonal";
        }
        else if (flavorId == 1) {
            this.flavor = flavorOptions[1];
        } else if (flavorId == 2) {
            this.flavor = flavorOptions[2];
        } else if (flavorId == 3) {
            this.flavor = flavorOptions[3];
        }
    }

    /**
    * Updates the drinks database after an order including the drink has been paid for
    * */
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

    /**
    * Calls the flavor in String form
    * @return the drink ordered
     */
    public String getFlavor() {
        return flavor;
    }
    /**
    * A getter function for flavorID
    * @return the flavorId
     */
    public int getFlavorId() {
        return flavorId;
    }
    
    /**
    * the toString implementation for the drink object
    * @return the String representing the drink object
     */
    public String toString() {// use for testing implicitly called by print
        return "Drink flavor: " + flavor + ", Drink id: " + flavorId;
    }

    /*
    * A function that prepares a string to be passed to SQL Database
    * @return string that should be passed to the SQL Database
    * 
    */
    public String toInputString() { // use to prepare to add order to sql
        return flavorId + "";
    }
    // public static void main(String[] args) {
    // drink newDrink = new drink(1);
    // System.out.println(newDrink.toInputString());
    // }

}
