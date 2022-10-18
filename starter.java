/**@author
 * Nick, Aadith, Ismat, Nebiyou
**/
import java.sql.*;
public class starter {
    private String itemName;
    private int itemId;
    private double price;
    // add arrays of names and prices where index corresponds to itemId
    private String[] starterOptions = { "None", "2 Felafels", "Hummus & Pita", "Vegan Box", "Garlic Fries" };
    private double[] prices = { 0, 2.85, 3.5, 6.49, 1.99 };
    private String seasonalStarter ="";
    private double seasonalPrice =0;

    public starter(int itemId) { // itemId shold be enough to set name and price
        setItem(itemId);// dry coding :)
        setItemName(itemId);
        setprice(itemId);
    }

    public void setItem(int newId) { // we should only need to use this to change everything
        itemId = newId;
    }

    public void setItemName(int itemId) {
        if(itemId==-1){
            itemName="Seasonal";
        }
        else{
            itemName = starterOptions[itemId];
        }
    }

    public void setprice(int itemId) {
        if(itemId<0){
            getSeasonalPrice();
            return;
        }
        price = prices[itemId];
    }
    public void getSeasonalPrice(){
        Connection conn = null;
        String teamNumber = "14";
        String sectionNumber = "912";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        // Connecting to the database
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        try {        
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM \"PromotionalItem\"");
            rs.next();
            price=rs.getDouble("Item Price");

        } catch (Exception e) {
            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        try {
            conn.close();
            // System.out.println("Connection Closed.");
        } catch (Exception e) {
            // System.out.println("Connection NOT Closed.");
        }
    }

    public String getItem() {
        return itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public double getPrice() {
        return price;
    }

    public String toString() {// use for tesing implicitly called by print
        return "Name: " + itemName + " ID: " + itemId + " Price: " + price;
    }

    public String toInputString() {// use to prepare to add order to sql
        return itemId + ", ";
    }
    // public static void main(String[] args) {
    // starter newStarter = new starter(1);
    // System.out.println(newStarter.toInputString());
    // System.out.println(newStarter.toString());
    // }

}
