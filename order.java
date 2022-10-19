import java.time.LocalDate;
import java.sql.*;

/**@author
 * Nick, Aadith, Ismat, Nebiyou
*/

public class order {
    private entree orderEntree;
    private drink orderDrink;
    private starter orderStarter;

    private double total;
    private int entreeId;

    public order() {// called if no entree is requested
        orderEntree = new entree(0);
        total = 0;
    }
    
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

    public int getNumToppings() {
        return orderEntree.getNumToppings();
    }

    public void addTopping(int t) {
        orderEntree.addTopping(t);
    }

    public void addDressing(int d) {
        orderEntree.setDressing(d);
    }

    public void addDrink(int flavorId) {
        orderDrink = new drink(flavorId);
        total += 1.5;
    }

    public void removeDrink() { // *** should only be able to remove the drink after it has been added ***
        if (orderDrink.getFlavorId() == 0) {
            return;
        } else {
            orderDrink.setFlavor(0);
            total -= 1.5;
        }
    }

    public void addStarter(int starterId) {
        orderStarter = new starter(starterId);
        total += orderStarter.getPrice();
    }

    public void removeStarter() {
        total -= orderStarter.getPrice();
        addStarter(0);
    }

    public double getTotal() {
        return total;
    }

    public String toString() {// use for tesing implicitly called by print
        return "total amount: " + getTotal() + ", EntreeID: " + entreeId + " " + orderEntree.toInputString()
                + " Starter IDs: " + orderStarter.toInputString() + " Drinks IDs: " + orderDrink.toInputString();
    }

    // "Insert INTO \"Order\" (\"Date\", \"Server ID\", \"Total Amount\", \"Entree
    // ID\", \"Topping IDs\", \"Dressing ID\", \"Starter ID\", \"Drinks ID\") values
    // ('22-08-2022', 1, 22.46, 1, ARRAY[1,2,3,5], 1,2,4);";
    public String sqlQuery(int employeeID) {
        String sqlInput = "INSERT INTO \"Order\" (\"Date\", \"Server ID\", \"Total Amount\", \"Entree ID\", \"Topping IDs\", \"Dressing ID\", \"Starter ID\", \"Drinks ID\" ) values ('"
                + LocalDate.now() + "', "+ employeeID +"," + getTotal() + "," + entreeId + ", " + orderEntree.toInputString()
                + orderStarter.toInputString() + orderDrink.toInputString() + ")" + ";";
        // System.out.println(sqlInput); //remove once we can send to table!
        return sqlInput;
    }

    public static void main(String[] args) {
    }
}
