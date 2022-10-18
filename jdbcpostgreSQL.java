import java.sql.*;
import java.util.*;
import java.io.File;

/**@author
 * Nick, Aadith, Ismat, Nebiyou
**/

/*
CSCE 315
9-27-2021 Lab
*/
public class jdbcpostgreSQL {
    /**
     * 
     * @param sqlQuery
     * @return
     */

    public static int orderQuery(String sqlQuery) {
        Connection conn = null;
        String teamNumber = "14";
        String sectionNumber = "912";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        dbSetup myCredentials = new dbSetup();

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 1;
        }
        //System.out.println("Opened database successfully");

        try {
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(sqlQuery);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 2;
        }
        //System.out.println("Passed query successfully");

        try {
            conn.close();
            //System.out.println("Connection Closed.");
        } catch (Exception e) {
            //System.out.println("Connection NOT Closed.");
        } // end try catch

        return 0;
    }
    public static int seasonalQuery(String sqlQuery){
        //drop stuff from current table
        orderQuery("DELETE FROM \"PromotionalItem\"");
        return orderQuery(sqlQuery);
    }
    public static int seasonalCountQuery(){
        int count=0;
        Connection conn = null;
        String teamNumber = "14";
        String sectionNumber = "912";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        dbSetup myCredentials = new dbSetup();

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 1;
        }
        //System.out.println("Opened database successfully");

        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT COUNT(*) AS total FROM \"PromotionalItem\"");
            result.next();
            count=result.getInt("total");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 2;
        }
        //System.out.println("Passed query successfully");

        try {
            conn.close();
            //System.out.println("Connection Closed.");
        } catch (Exception e) {
            //System.out.println("Connection NOT Closed.");
        } // end try catch

        return count;
    }

    // Commands to run this script
    // This will compile all java files in this directory
    // javac *.java
    // This command tells the file where to find the postgres jar which it needs to
    // execute postgres commands, then executes the code
    // Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
    // Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

    // MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
    public static void main(String args[]) {

        // Building the connection with your credentials
        Connection conn = null;
        String teamNumber = "14";
        String sectionNumber = "912";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        dbSetup myCredentials = new dbSetup();

        // Connecting to the database
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Opened database successfully");

        // String items = "";
        // try {
        //     String sqlQuery = "select * from \"Entrees\"";

        //     Statement stmt = conn.createStatement();
        //     ResultSet result = stmt.executeQuery(sqlQuery);
        //     while (result.next()) {
        //         items += result.getString("Entree Items")+"\n";
        //     }
        // } catch (Exception e) {
        //     System.err.println(e.getClass().getName() + ": " + e.getMessage());
        //     return;
        // }
        // System.out.println(items);

        // loading Order file
        // ------------------ ORDER FILE ---------------------------
        // try{
        // Scanner scn = new Scanner(new File("order4.csv"));
        // //scn.useDelimiter(",");
        // int line = 0;
        // while(scn.hasNextLine()){
        // if(line==0){ //skipping the header since the sql DB has the datable headers
        // scn.nextLine();
        // line++;
        // continue;
        // }
        // String grabbed_row = scn.nextLine();
        // //System.out.println(grabbed_row);
        // //Order table columns =
        // // ("Order ID" SERIAL PRIMARY KEY, "Date" Text, "Server ID" Integer, "Total
        // Amount" Double PRECISION, "Entree ID" Integer, "Topping IDs" Integer[],
        // "Dressing ID" Integer, "Starter ID" Integer, "Drinks ID" INTEGER)
        // // populating: Insert INTO "Order" ("Date", "Server ID", "Total Amount",
        // "Entree ID", "Topping IDs", "Dressing ID", "Starter ID", "Drinks ID") values
        // ('22-08-2022', 1, 22.46, 1, ARRAY[1,2,3,5], 1,2,4);

        // try{
        // Scanner tokenize_row = new Scanner(grabbed_row);
        // tokenize_row.useDelimiter(",");

        // //create a statement object
        // Statement stmt = conn.createStatement();

        // //Running a query
        // //TODO: update the sql command here
        // String sqlStatement = "INSERT INTO \"Order\" (\"Date\", \"Server ID\",
        // \"Total Amount\", \"Entree ID\", \"Topping IDs\", \"Dressing ID\", \"Starter
        // ID\", \"Drinks ID\" ) values ('" +tokenize_row.next()+"'," +
        // tokenize_row.next() +"," + tokenize_row.next() +","+tokenize_row.next() +",";
        // String topping = tokenize_row.next();
        // // System.out.println(topping);

        // topping = topping.replace(" ", ",");
        // // //topping = topping.replace("]\"", "]");
        // topping = topping.substring(1,topping.length()-1);
        // // System.out.println("ARRAY"+topping);
        // sqlStatement += "ARRAY"+ topping + "," +tokenize_row.next() +","
        // +tokenize_row.next() +"," +tokenize_row.next() +");";
        // // System.out.println(sqlStatement);
        // //String sqlStatement = "Insert INTO \"Order\" (\"Date\", \"Server ID\",
        // \"Total Amount\", \"Entree ID\", \"Topping IDs\", \"Dressing ID\", \"Starter
        // ID\", \"Drinks ID\") values ('22-08-2022', 1, 22.46, 1, ARRAY[1,2,3,5],
        // 1,2,4);";
        // //String sqlStatement = "INSERT INTO \"Order\" values (" +
        // tokenize_row.next()+ tokenize_row.next()+ tokenize_row.next()+
        // tokenize_row.next()+ tokenize_row.next()+ "Array"+ tokenize_row.next()+
        // tokenize_row.next()+ tokenize_row.next()+ tokenize_row.next()+")";
        // //sqlStatement += scn.nextLine() +")";

        // //send statement to DBMS
        // //This executeQuery command is useful for data retrieval
        // //ResultSet result = stmt.executeQuery(sqlStatement);
        // //stmt.executeQuery(sqlStatement);
        // //OR
        // //This executeUpdate command is useful for updating data
        // int result = stmt.executeUpdate(sqlStatement);

        // //OUTPUT
        // //You will need to output the results differently depeninding on which
        // function you use
        // //System.out.println("--------------------Query
        // Results--------------------");
        // //while (result.next()) {
        // //System.out.println(result.getString("column_name"));
        // //}
        // //OR
        // //System.out.println(result);
        // tokenize_row.close();
        // line++;
        // //System.out.println(line);
        // } catch (Exception e){
        // e.printStackTrace();
        // System.err.println(e.getClass().getName()+": "+e.getMessage());
        // System.exit(0);
        // }
        // }
        // scn.close();
        // }catch(Exception e){}

        // ------------------- TOPPINGS FILE, Entrees File, Dressings File, Servers File
        // --------------
        // try{
        // Scanner scn = new Scanner(new File("entree.csv"));
        // //scn.useDelimiter(",");
        // int line = 0;
        // while(scn.hasNextLine()){
        // if(line==0){ //skipping the header since the sql DB has the datable headers
        // scn.nextLine();
        // line++;
        // continue;
        // }
        // String grabbed_row = scn.nextLine();

        // try{
        // Scanner tokenize_row = new Scanner(grabbed_row);
        // tokenize_row.useDelimiter(",");

        // //create a statement object
        // Statement stmt = conn.createStatement();

        // //Running a query
        // //TODO: update the sql command here
        // String sqlStatement = "INSERT INTO \"Entrees\" (\"Entree ID\", \"Entree
        // Item\", \"Entree Inventory\") values ("+ tokenize_row.next() +", '"+
        // tokenize_row.next().toString()+"', " + tokenize_row.next()+");";

        // //send statement to DBMS
        // //This executeQuery command is useful for data retrieval
        // //ResultSet result = stmt.executeQuery(sqlStatement);
        // //stmt.executeQuery(sqlStatement);
        // //OR
        // //This executeUpdate command is useful for updating data
        // int result = stmt.executeUpdate(sqlStatement);

        // //OUTPUT
        // //You will need to output the results differently depeninding on which
        // function you use
        // //System.out.println("--------------------Query
        // Results--------------------");
        // //while (result.next()) {
        // //System.out.println(result.getString("column_name"));
        // //}
        // //OR
        // //System.out.println(result);
        // tokenize_row.close();
        // line++;
        // //System.out.println(line);
        // } catch (Exception e){
        // e.printStackTrace();
        // System.err.println(e.getClass().getName()+": "+e.getMessage());
        // System.exit(0);
        // }
        // }
        // scn.close();
        // }catch(Exception e){}
        // ------------------------ Starters File, Drinks File, ---------------
        // try{
        // Scanner scn = new Scanner(new File("drinks.csv"));
        // //scn.useDelimiter(",");
        // int line = 0;
        // while(scn.hasNextLine()){
        // if(line==0){ //skipping the header since the sql DB has the datable headers
        // scn.nextLine();
        // line++;
        // continue;
        // }
        // String grabbed_row = scn.nextLine();

        // try{
        // Scanner tokenize_row = new Scanner(grabbed_row);
        // tokenize_row.useDelimiter(",");

        // //create a statement object
        // Statement stmt = conn.createStatement();

        // //Running a query
        // //TODO: update the sql command here
        // String sqlStatement = "INSERT INTO \"Drinks\" (\"Drink ID\", \"Drink Item\",
        // \"Drink Price\", \"Drink Inventory\") values ("+ tokenize_row.next() +", '"+
        // tokenize_row.next().toString()+"'," + tokenize_row.next() +",
        // "+tokenize_row.next()+");";

        // //send statement to DBMS
        // //This executeQuery command is useful for data retrieval
        // //ResultSet result = stmt.executeQuery(sqlStatement);
        // //stmt.executeQuery(sqlStatement);
        // //OR
        // //This executeUpdate command is useful for updating data
        // int result = stmt.executeUpdate(sqlStatement);

        // //OUTPUT
        // //You will need to output the results differently depeninding on which
        // function you use
        // //System.out.println("--------------------Query
        // Results--------------------");
        // //while (result.next()) {
        // //System.out.println(result.getString("column_name"));
        // //}
        // //OR
        // //System.out.println(result);
        // tokenize_row.close();
        // line++;
        // //System.out.println(line);
        // } catch (Exception e){
        // e.printStackTrace();
        // System.err.println(e.getClass().getName()+": "+e.getMessage());
        // System.exit(0);
        // }
        // }
        // scn.close();
        // }catch(Exception e){}

        // closing the connection
        try {
        conn.close();
        //System.out.println("Connection Closed.");
        } catch (Exception e) {
        //System.out.println("Connection NOT Closed.");
        } // end try catch
    }// end main
}// end Class
