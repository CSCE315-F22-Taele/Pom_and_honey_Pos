 import java.util.Arrays;

/**
 * A helper class to used to create and process entree orders
 * @author Nick, Aadith, Ismat, Nebiyou
 */
public class entree {
    private String type;
    private int typeId;
    private String proteins;
    private int[] toppings = {0, 0, 0, 0};
    private int numToppings;
    private int dressing;

    private String[] toppingOptions = { "None", "Pickled Onions", "Diced Cucumber", "Citrius Couscous",
            "Roasted Cauliflower", "Tomato-onion Salad", "Kalamata Olives", "Roasted Peppers", "Red Cabbage Slaw" };
    private String[] types = { "Grain Bowl", "Salad", "Pita", "Greens and Grains", "Gyro" };
    private String[] proteinTypes = { "Gyro", "Falafel", "Vegetable Medley", "Meatballs", "Chicken" };
    
    private String seasonalProtein="";
    private String seasonalType="";

    /**
    * Returns an entree object to be used in the order.java file
    * @param inType each entree is assigned a particular entree in the database
     */
    public entree(int inType) { // entree orders should start with type, rest should be modified with sets as
                                // added
        setType(inType);
        numToppings = 0;
    }

    /**
    * Sets the entree type for the entree object
    * @param inType the entree integer is assigned from the database
     */
    public void setType(int inType) {
        typeId = inType;
        type = types[typeId];
    }
    
    /** A getter for Entree type
    * @return entreeID
     */
    public int getTypeId() {
        return typeId;
    }
    
    /**
    * Returns the the name associated with the entree's inType
    * @return the name associated with the entree's inType
     */
    public String getType() {
        return type;
    }

    /**
    * Returns the number of toppings that have been selected for the entree
    * @return the number of toppings currently selected for the entree
    * */
    public int getNumToppings() {
        return numToppings;
    }

    /**
    * Sets the protein of the entree with the database-assigned integer for the particular protein
    * @param protein the database-assigned protein integer
     */
    public void setProtein(int protein) {
        if(protein<0){
            this.proteins="Seasonal";
        }
        else{
            this.proteins = proteinTypes[protein];
        }
    }

    /**
    * Returns the name associated with the protein
    * @return the name associated with the protein
    * */
    public String getProtein() {
        return proteins;
    }

    /**
    * Adds toppings to the entree and provides some logic for the limitation of toppings
    * @param toppingId the database-assigned integer to the topping
    */
    public void addTopping(int toppingId) {
        if (numToppings == 4) {
            // System.out.println("Error too many toppings");
            demo.displayMessage("Error: Too Many Toppings!");
        } else {
            toppings[numToppings] = toppingId;
            numToppings++;
        }

    }

    /**
    * Returns the SQL query implementation for the toppings in generating the order
    * @return the SQL query implementation for the toppings in the order object
     */
    public String getToppings() {
        // String toppingStr="[";
        // for(int i=0;i<numToppings;i++){
        // toppingStr+=toppingOptions[toppings[i]];
        // if(i<numToppings-1){
        // toppingStr+=" ";
        // }
        // }
        // return toppingStr + "]";
        return "ARRAY" + Arrays.toString(toppings);
    }

    /**
    * Sets the dressing using the database-assigned integer for the dressing
    * @param dressing the database-assigned integer for the dressing
     */
    public void setDressing(int dressing) {
        this.dressing = dressing;
    }

    /**
    * Returns the dressing's database-assigned integer
    * @return the database-assigned integer for the dressing
    */
    public int getDressing() {
        return dressing;
    }

    /**
    * Returns the price of the base entree
    * @return returns the price of the base entree
    * */
    public double getPrice() {
        if (typeId == 0) {
            return 0;
        }
        return 7.69;
    }

    /**
    * the toString() implementation for the entree object
    * @return the String version of the entree object
     */
    public String toString() {// sysout.println calls tostring so it would make sense to use this for testing
        return type + " with " + proteins + ", " + dressing + " and topped with " + getDressing();
    }

    /**
    * A part of the SQL query used to generate a new order in order.java
    * @return part of the SQL query used to generate a new order in order.java
     */
    public String toInputString() { // we should use this to return a string ready to be added to order and sent
                                    // into the data base
        // Entree ID, Topping ids, Dressing Ids taken care of here
        return "ARRAY" + Arrays.toString(toppings) + "," + getDressing() + ","; // entree,toppings, getdressing,
    }
    // public static void main(String[] args) {
    // entree newEntree = new entree(4);
    // newEntree.setProtein(4);
    // newEntree.setDressing(7);
    // newEntree.addTopping(2);
    // newEntree.addTopping(1);
    // newEntree.addTopping(5);
    // newEntree.addTopping(6);

    // System.out.println(newEntree.toInputString());
    // }
}
