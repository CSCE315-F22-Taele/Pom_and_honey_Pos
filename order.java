import java.time.LocalDate;
import java.sql.*;

/**
 * A helper class to store all information about an order
 * @author Nick, Aadith, Ismat, Nebiyou
*/

public class order {
    private entree orderEntree;
    private drink orderDrink;
    private starter orderStarter;

    private double total;
    private int entreeId;

    /**
    * The default constructor for the order object that acts as a placeholder in the GUI before the order has been modified
     */
    public order() {// called if no entree is requested
        orderEntree = new entree(0);
        total = 0;
    }
    
    /**
    * An order object is created using the database-assigned integer for the entree of the order
    * @param entreeId the database-assigned integer for the respective entree of the order
     */
    public order(int entreeId) {
        this.entreeId = entreeId;
        if(entreeId<0){
            entreeId*=-1;
            orderEntree=new entree((entreeId-1)/5);
            orderEntree.setProtein(-1);
        }
        else if (entreeId < 20) {
            orderEntree = new entree(entreeId / 5);
            orderEntree.setProtein(entreeId % 5);
        } else if (entreeId == 20) {
            orderEntree = new entree(4);
            orderEntree.setProtein(0);
        } else {
            //System.out.println("Error invalid entree");
        }
        total += 7.69;
        orderDrink = new drink(0);
        orderStarter = new starter(0);
    }

    /**
    * Returns the number of toppings for the entree referenced by the order object
    * @return the number of toppings selected for the entree
     */
    public int getNumToppings() {
        return orderEntree.getNumToppings();
    }
    
    /**
    * Adds a topping to the order object's entree object
    * @param t the database-assigned integer for the topping
     */
    public void addTopping(int t) {
        orderEntree.addTopping(t);
    }

    /**
    * Adds a dressing to the order object's entree object
    * @param d the database-assigned integer for the dressing
     */
    public void addDressing(int d) {
        orderEntree.setDressing(d);
    }

    /**
    * Sets the drink flavor for the order object's drink object
    * @param flavorId the database-assigned integer for the flavorId
     */
    public void addDrink(int flavorId) {
        orderDrink = new drink(flavorId);
        total += 1.5;
    }

    /**
    * Removes a drink added by the GUI to the order
     */
    public void removeDrink() { // *** should only be able to remove the drink after it has been added ***
        if (orderDrink.getFlavorId() == 0) {
            return;
        } else {
            orderDrink.setFlavor(0);
            total -= 1.5;
        }
    }

    /**
     * Adds the starter to the order according to the given starterID
     * Adjusts the price of the order according to the starter
     * @param starterId ID of the starter to be added according to the database
     * 
     */
    public void addStarter(int starterId) {
        orderStarter = new starter(starterId);
        total += orderStarter.getPrice();
    }
    /**
    * A function that removes starter from total when server unselects it from GUI
     */
    public void removeStarter() {
        total -= orderStarter.getPrice();
        addStarter(0);
    }
    
    /**
    * Returns the total price for the order
    * @return the price of the order
     */
    public double getTotal() {
        return total;
    }

    /**
    * toString() implementation for the order
    * @return the String version of the order object
     */
    public String toString() {// use for tesing implicitly called by print
        return "total amount: " + getTotal() + ", EntreeID: " + entreeId + " " + orderEntree.toInputString()
                + " Starter IDs: " + orderStarter.toInputString() + " Drinks IDs: " + orderDrink.toInputString();
    }

    // "Insert INTO \"Order\" (\"Date\", \"Server ID\", \"Total Amount\", \"Entree
    // ID\", \"Topping IDs\", \"Dressing ID\", \"Starter ID\", \"Drinks ID\") values
    // ('22-08-2022', 1, 22.46, 1, ARRAY[1,2,3,5], 1,2,4);";
    
    /**
     * Creates and returns a string that contains a SQL update query to add the order to the database
     * @param employeeID the employee's associated ID on the database
     * @return a query string that is passed in to the database to populat "Order" table
     * 
     */
    public String sqlQuery(int employeeID) {
        String sqlInput = "INSERT INTO \"Order\" (\"Date\", \"Server ID\", \"Total Amount\", \"Entree ID\", \"Topping IDs\", \"Dressing ID\", \"Starter ID\", \"Drinks ID\" ) values ('"
                + LocalDate.now() + "', "+ employeeID +"," + getTotal() + "," + entreeId + ", " + orderEntree.toInputString()
                + orderStarter.toInputString() + orderDrink.toInputString() + ")" + ";";
        // System.out.println(sqlInput); //remove once we can send to table!
        return sqlInput;
    }

    // public static void main(String[] args) {
    // }
}
