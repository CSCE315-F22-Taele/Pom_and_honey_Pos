import java.util.Arrays;

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

    public entree(int inType) { // entree orders should start with type, rest should be modified with sets as
                                // added
        setType(inType);
        numToppings = 0;
    }

    public void setType(int inType) {
        typeId = inType;
        type = types[typeId];
    }

    public int getTypeId() {
        return typeId;
    }

    public String getType() {
        return type;
    }

    public void setProtein(int protein) {
        if(protein<0){
            this.proteins="Seasonal";
        }
        else{
            this.proteins = proteinTypes[protein];
        }
    }

    public String getProtein() {
        return proteins;
    }

    public void addTopping(int toppingId) {
        if (numToppings == 4) {
            // System.out.println("Error too many toppings");
            demo.displayMessage("Error: Too Many Toppings!");
        } else {
            toppings[numToppings] = toppingId;
            numToppings++;
        }

    }

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

    public void setDressing(int dressing) {
        this.dressing = dressing;
    }

    public int getDressing() {
        return dressing;
    }

    public double getPrice() {
        if (typeId == 0) {
            return 0;
        }
        return 7.69;
    }

    public String toString() {// sysout.println calls tostring so it would make sense to use this for testing
        return type + " with " + proteins + ", " + dressing + " and topped with " + getDressing();
    }

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
