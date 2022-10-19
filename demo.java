import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFormattedTextField.AbstractFormatter;

import java.awt.Color;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;

import java.util.Vector;
import java.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Nick, Ismat, Nebiyou, Aadith
 */
public class demo extends JFrame {
    public static int employeeID;
    public static int type;
    public static int protein;
    public static order theOrder = new order();

    public static String seasonalType="";
    public static String seasonalName="";
    public static Boolean seasonalExists=false;
    
    private static Color deepRed= new Color(125,5,30);
    private static Color darkGreen= new Color(15,75,5);
    private static Color lightBlue= new Color(130,200,210);
    
    /**
    * Helper function for view/edit inventory screen that updates the inventory
     */
    public static void update_inventory(Vector<Vector<String>> changes) {
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
           
            for (int r = 0; r < changes.size(); r++) {
                String tblName = changes.get(r).get(0);
                String item = changes.get(r).get(1);
                String stock = changes.get(r).get(2);

                String sqlStatement = "";
                Statement stmt = conn.createStatement();
                
                try {
                    if (Integer.parseInt(stock) < 0) {
                        continue;
                    }
                }
                catch (NumberFormatException e) {
                    continue;
                }
                
                if (tblName.equals("Entrees")) {
                    sqlStatement = "UPDATE \"Entrees\" SET \"Entree Inventory\"=" + stock + " WHERE \"Entree Items\"='" + item + "'";
                } else if (tblName.equals("Dressings")) {
                    sqlStatement = "UPDATE \"Dressings\" SET \"Dressing Inventory\"=" + stock + " WHERE \"Dressing Item\"='" + item + "'";
                } else if (tblName.equals("Drinks")) {
                    sqlStatement = "UPDATE \"Drinks\" SET \"Drink Inventory\"=" + stock + " WHERE \"Drink Item\"='" + item + "'";
                } else if (tblName.equals("Starters")) {
                    sqlStatement = "UPDATE \"Starters\" SET \"Starter Inventory\"=" + stock + " WHERE \"Starter Item\"='" + item + "'";
                } else if (tblName.equals("Toppings")) {
                    sqlStatement = "UPDATE \"Toppings\" SET \"Topping Inventory\"=" + stock + " WHERE \"Topping Item\"='" + item + "'";
                } else if(tblName.equals("PromotionalItem")){
                    sqlStatement = "UPDATE \"PromotionalItem\" SET \"Item Inventory\"=" + stock + " WHERE \"Promotional Item Name\"='" + item + "'";
                }
                else{
                    System.out.println("Theres a typo");
                }
                
                stmt.executeUpdate(sqlStatement);
                System.out.println(sqlStatement);
            }
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
    
    /**
     * Opens welcome screen that starts our gui
     */
    public static void welcome() { // this screen is what the user is greeted with upon starting the gui
        JFrame frame = new JFrame("POS Log-In");
        Color background = new Color(47, 79, 79);
        frame.setBackground(background);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel logInLabel = new JLabel("Pom & Honey POS Login");// if pressed the pos goes to the manager screen
        logInLabel.setBounds(210, 100, 1000, 350); // x axis, y axis, width, height
        logInLabel.setFont(new Font("Arial", Font.BOLD, 40));
        Color c2 = new Color(255, 0, 0);
        logInLabel.setBackground(c2);

        JLabel label = new JLabel("Enter your ID: ");
        label.setBounds(120, 325, 150, 100);
        label.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField idField = new JTextField(0);
        idField.setBounds(285, 350, 300, 50);
        idField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '1') || (c > '6')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume(); // if it's not a number, ignore the event
                }
            }
        });

        JButton submitIDButton = new JButton("Submit");
        submitIDButton.setBackground(darkGreen);
        submitIDButton.setForeground(Color.white);
        submitIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idField.getText().isEmpty()) {
                    return;
                }
                if (idField.getText().charAt(0) == '1') {
                    employeeID = 1;
                    manager_view();
                    frame.setVisible(false);
                } else if (idField.getText().charAt(0) >= '2' || idField.getText().charAt(0) <= '6') {
                    server_view();
                    frame.setVisible(false);
                    employeeID = Integer.parseInt(idField.getText());
                } else {
                    idField.setText("");
                    return;
                }

            }
        });
        submitIDButton.setBounds(610, 350, 150, 50);

        frame.add(logInLabel);
        frame.add(idField);
        frame.add(submitIDButton);
        frame.add(label);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true); // making the frame visible
    }

    /**
     * Shows the manager screen after log-in
     */
    public static void manager_view() {
        JFrame frame = new JFrame("MANAGER VIEW");
        Color background = new Color(47, 79, 79);
        frame.setBackground(background);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton takeOrder = new JButton("Take Order");
        takeOrder.setBackground(lightBlue);
        takeOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entree_screen();
                frame.setVisible(false);

            }
        });
        takeOrder.setBounds(25, 50, 300, 100);

        JButton addItem = new JButton("Add Seasonal Item");
        addItem.setBackground(lightBlue);
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            // add seasonal item screen;
                seasonal_item();
                frame.setVisible(false);

             }
        });
        addItem.setBounds(675, 50, 300, 100);

        JButton clearItem = new JButton("Remove Seasonal Item");
        clearItem.setBackground(lightBlue);
        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            // add seasonal item screen;
                jdbcpostgreSQL.orderQuery("DELETE FROM \"PromotionalItem\"");
                displayMessage("Seasonal Item Removed Successfully!");
             }
        });
        clearItem.setBounds(675, 175, 300, 100);
        
        JButton viewInventory = new JButton("View Inventory");
        viewInventory.setBackground(lightBlue);
        viewInventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // see inventory screen;
                view_inventory();
                frame.setVisible(false);
                
            }
        });
        viewInventory.setBounds(350, 50, 300, 100);

        JButton salesReportInput = new JButton("Sales Report");
        salesReportInput.setBackground(lightBlue);
        salesReportInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //see sales input and table page
                sales_report_input();
                frame.setVisible(false);
            }
        });
        salesReportInput.setBounds(350, 175, 300, 100);

        JButton excessReportInput = new JButton("Excess Report");
        excessReportInput.setBackground(lightBlue);
        excessReportInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // see inventory screen;
                excess_report_input();
                frame.setVisible(false);
                
            }
        });
        excessReportInput.setBounds(25, 175, 300, 100);

        JButton restockReport = new JButton("Restock Report");
        restockReport.setBackground(lightBlue);
        restockReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // see restock screen;
                restock_report();
                frame.setVisible(false);
                
            }
        });
        restockReport.setBounds(25, 300, 300, 100);

        JButton exit = new JButton("Exit to Main Screen");
        exit.setBackground(deepRed);   
        exit.setForeground(Color.white);
        exit.setBounds(25, 700, 425, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome();
                frame.setVisible(false);
            }
        });

        frame.add(takeOrder);
        frame.add(viewInventory);
        frame.add(salesReportInput);
        frame.add(excessReportInput);
        frame.add(restockReport);
        frame.add(addItem);
        frame.add(clearItem);
        frame.add(exit);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true); // making the frame visible
    }

    /**
     * Shows the server view screen after log-in
     *
     */
    public static void server_view() {
        JFrame frame = new JFrame("SERVER VIEW");
        Color background = new Color(47, 79, 79);
        frame.setBackground(background);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton takeOrder = new JButton("Take Order");
        takeOrder.setBackground(lightBlue);
        takeOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entree_screen();
                frame.setVisible(false);

            }
        });
        takeOrder.setBounds(250, 250, 450, 150);

        JButton exit = new JButton("Exit to Main Screen");
        exit.setBackground(deepRed);   
        exit.setForeground(Color.white);
        exit.setBounds(25, 700, 425, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome();
                frame.setVisible(false);
            }
        });

        frame.add(takeOrder);
        frame.add(exit);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true); // making the frame visible
    }

    /**
    * Shows the view/edit inventory screen
     */
    public static int view_inventory() {
        JFrame frame = new JFrame("VIEW/EDIT INVENTORY");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Connection conn = null;
        String teamNumber = "14";
        String sectionNumber = "912";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 1;
        }
        // System.out.println("Opened database successfully");

        Vector<Vector<String>> inventoryTable = new Vector<>();
        int[] partitions = new int[5];
        int line = 0;
        try {
            String sqlQuery = "select * from \"Entrees\"";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Entree Items"));
                entry.add(result.getString("Entree Inventory"));
                // result.getString() convert to int and then check if less than amount
                inventoryTable.add(entry);
                line++;
            }
            partitions[0] = line;

            sqlQuery = "select * from \"Dressings\" where \"Dressing Item\" != 'None'";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Dressing Item"));
                entry.add(result.getString("Dressing Inventory"));
                inventoryTable.add(entry);
                line++;
            }
            partitions[1] = line;

            sqlQuery = "select * from \"Drinks\" where \"Drink Item\" != 'None'";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Drink Item"));
                entry.add(result.getString("Drink Inventory"));
                inventoryTable.add(entry);
                line++;
            }
            partitions[2] = line;

            sqlQuery = "select * from \"Starters\" where \"Starter Item\" != 'None'";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Starter Item"));
                entry.add(result.getString("Starter Inventory"));
                inventoryTable.add(entry);
                line++;
            }
            partitions[3] = line;

            sqlQuery = "select * from \"Toppings\" where \"Topping Item\" != 'None'";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Topping Item"));
                entry.add(result.getString("Topping Inventory"));
                inventoryTable.add(entry);
                line++;
            }
            partitions[4] = line;
            
            sqlQuery = "select * from \"PromotionalItem\"";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Promotional Item Name"));
                entry.add(result.getString("Item Inventory"));
                inventoryTable.add(entry);
            }
        } catch (Exception e) {
            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 2;
        }
        // System.out.println("Passed query successfully");

        try {
            conn.close();
            // System.out.println("Connection Closed.");
        } catch (Exception e) {
            // System.out.println("Connection NOT Closed.");
        }
        
        Vector<Vector<String>> changes = new Vector<>();
        JButton exit = new JButton("Exit to Manager Screen");
        exit.setBackground(deepRed);   
        exit.setForeground(Color.white);
        exit.setBounds(0, 800, 500, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_view();
                frame.setVisible(false);
            }
        });
        JButton update = new JButton("Update inventory changes");
        update.setBackground(darkGreen);
        update.setForeground(Color.white);
        update.setBounds(500, 800, 500, 100);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println("Update " + changes.size());
                update_inventory(changes);
                manager_view();
                frame.setVisible(false);
            }
        });

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Item");
        columnNames.add("Stock");

        DefaultTableModel tableModel = new DefaultTableModel(inventoryTable, columnNames);
        tableModel.addTableModelListener(new TableModelListener() {
    
            public void tableChanged(TableModelEvent e) {
                if (e.getType() != TableModelEvent.UPDATE) {
                    return;
                }

                int colChanged = e.getColumn();
                if (colChanged == 0) {
                    return;
                }
                
                int rowChanged = e.getFirstRow();
                //System.out.println(rowChanged+"");
                String tblName = "";
                if(rowChanged >= partitions[4]){
                    tblName = "PromotionalItem";
                    //System.out.println("Stored line pi " + partitions[4]);
                }else if (rowChanged >= partitions[3]) {
                    tblName = "Toppings";
                    // System.out.println("Stored line topping " + partitions[3]);
                } else if (rowChanged >= partitions[2]) {
                    tblName = "Starters";
                } else if (rowChanged >= partitions[1]) {
                    tblName = "Drinks";
                } else if (rowChanged >= partitions[0]) {
                    tblName = "Dressings";
                } else {
                    tblName = "Entrees";
                }
                Vector<String> entryChange = new Vector<>();
                String item = inventoryTable.get(rowChanged).get(0);
                String stock = inventoryTable.get(rowChanged).get(1);
                
                entryChange.add(tblName);
                entryChange.add(item);
                entryChange.add(stock);
                
                // System.out.println(tblName + " " + inventoryTable.get(rowChanged).get(0) + " " + inventoryTable.get(rowChanged).get(1));
                
                changes.add(entryChange);
            }
        }); 
        JTable table = new JTable(tableModel);
        // table.setBounds(50, 50, 1000, 1000);
        


        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0, 0, 1000, 1000);

        frame.add(exit);
        frame.add(update);
        frame.add(sp);

        frame.setSize(1000, 1000);
        frame.setLayout(null);
        frame.setVisible(true);

        return 0;
    }

    /**
    * Calls the Sales Report Input screen where a starting and ending dates are provided and then the resulting Sales Report is shown
     */
    public static void sales_report_input() {
        JFrame frame = new JFrame("SALES REPORT: INPUT");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel dateLabel1 = new JLabel("Enter in the beginning date (yyyy-mm-dd format): ");
        dateLabel1.setBounds(50, 20, 450, 200);
        dateLabel1.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField date1 = new JTextField();
        date1.setBounds(450, 110, 200, 25);
        date1.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel dateLabel2 = new JLabel("Enter in the ending date (yyyy-mm-dd format): ");
        dateLabel2.setBounds(50, 70, 450, 200);
        dateLabel2.setFont(new Font("Arial", Font.BOLD, 16));

        
        
        JTextField date2 = new JTextField();
        date2.setBounds(450, 160, 200, 25);
        date2.setFont(new Font("Arial", Font.BOLD, 16));

        JButton salesReport = new JButton("Generate Sales Report");// if pressed pos displays sales_report
        salesReport.setBackground(darkGreen);
        salesReport.setForeground(Color.white);
        salesReport.setBounds(700, 105, 225, 80);
        salesReport.setFont(new Font("Arial", Font.BOLD, 17));
        salesReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if (!verifyDates(date1.getText(), date2.getText())) {
                //     return;
                // }

                Vector<Vector<String>> inventoryTable = new Vector<>();
                
                Connection conn = null;
                String teamNumber = "14";
                String sectionNumber = "912";
                String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
                String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

                try {
                    Class.forName("org.postgresql.Driver");
                    conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
                } catch (Exception err) {
                    err.printStackTrace();
                    // System.err.println(err.getClass().getName() + ": " + err.getMessage());
                    return;
                }
                // System.out.println("Opened database successfully");

                try {
                    String sqlQuery = "SELECT  \"Entrees\".\"Entree Items\", count(\"Order ID\") From \"Order\" Inner Join \"Entrees\" on \"Order\".\"Entree ID\" = \"Entrees\".\"Entree ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' group by \"Entrees\".\"Entree Items\" order by \"Entrees\".\"Entree Items\"";
                    Statement stmt = conn.createStatement();
                    ResultSet result = stmt.executeQuery(sqlQuery);
                    while (result.next()) {
                        Vector<String> entry = new Vector<>();
                        entry.add(result.getString("Entree Items"));
                        entry.add(result.getString("count"));
                        inventoryTable.add(entry);
                    }

                    sqlQuery = "SELECT  \"Dressings\".\"Dressing Item\", count(\"Order ID\") From \"Order\" Inner Join \"Dressings\" on \"Order\".\"Dressing ID\" = \"Dressings\".\"Dressing ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' and \"Dressing Item\"!='None' group by \"Dressings\".\"Dressing Item\" order by \"Dressings\".\"Dressing Item\"";
                    stmt = conn.createStatement();
                    result = stmt.executeQuery(sqlQuery);
                    while (result.next()) {
                        Vector<String> entry = new Vector<>();
                        entry.add(result.getString("Dressing Item"));
                        entry.add(result.getString("count"));
                        inventoryTable.add(entry);
                    }

                    sqlQuery = "SELECT  \"Drinks\".\"Drink Item\", count(\"Order ID\") From \"Order\" Inner Join \"Drinks\" on \"Order\".\"Drinks ID\" = \"Drinks\".\"Drink ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' and \"Drink Item\"!='None' group by \"Drinks\".\"Drink Item\" order by \"Drinks\".\"Drink Item\"";
                    stmt = conn.createStatement();
                    result = stmt.executeQuery(sqlQuery);
                    while (result.next()) {
                        Vector<String> entry = new Vector<>();
                        entry.add(result.getString("Drink Item"));
                        entry.add(result.getString("count"));
                        inventoryTable.add(entry);
                    }

                    sqlQuery = "SELECT  \"Starters\".\"Starter Item\", count(\"Order ID\") From \"Order\" Inner Join \"Starters\" on \"Order\".\"Starter ID\" = \"Starters\".\"Starter ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' and \"Starter Item\"!='None' group by \"Starters\".\"Starter Item\" order by \"Starters\".\"Starter Item\"";
                    stmt = conn.createStatement();
                    result = stmt.executeQuery(sqlQuery);
                    while (result.next()) {
                        Vector<String> entry = new Vector<>();
                        entry.add(result.getString("Starter Item"));
                        entry.add(result.getString("count"));
                        inventoryTable.add(entry);
                    }

                    sqlQuery = "SELECT  \"Toppings\".\"Topping Item\", count(\"Order ID\") From \"Order\" Inner Join \"Toppings\" on \"Order\".\"Topping IDs\"[1] = \"Toppings\".\"Topping ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' and \"Topping Item\"!='None' group by \"Toppings\".\"Topping Item\" order by \"Toppings\".\"Topping Item\"";
                    stmt = conn.createStatement();
                    result = stmt.executeQuery(sqlQuery);
                    while (result.next()) {
                        Vector<String> entry = new Vector<>();
                        entry.add(result.getString("Topping Item"));
                        entry.add(result.getString("count"));
                        inventoryTable.add(entry);
                    }
                } catch (Exception ex) {
                    // System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                    return;
                }
                // System.out.println("Passed query successfully");
                
                try {
                    conn.close();
                    // System.out.println("Connection Closed.");
                } catch (Exception ex) {
                    // System.out.println("Connection NOT Closed.");
                    return;
                }


                Vector<String> columnNames = new Vector<>();
                columnNames.add("Menu Item");
                columnNames.add("Units Sold");

                DefaultTableModel tableModel = new DefaultTableModel(inventoryTable, columnNames);
                JTable table = new JTable(tableModel);
                JScrollPane sp = new JScrollPane(table);
                sp.setBounds(50, 200, 900, 475);
                frame.add(sp);
            }
        });

        JButton exit = new JButton("Exit to Manager View");
        exit.setBackground(deepRed);   
        exit.setForeground(Color.white);
        exit.setBounds(25, 700, 425, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_view();
                frame.setVisible(false);
            }
        });

        frame.add(dateLabel1);
        frame.add(date1);
        frame.add(dateLabel2);
        frame.add(date2);
        frame.add(salesReport);
        frame.add(exit);
        frame.setSize(1000,1000);
        frame.setLayout(null);
        frame.setVisible(true);
    }
    
    /**
     * Validates input dates by checking the SQL database for orders with both the start date and end date.
     * 
     * @param start start date in 'yyyy-mm-dd' format
     * @param end end date in 'yyyy-mm-dd' format
     * @return boolean showing whether the dates are a valid range
     * 
     */
    public static boolean verifyDates(String start, String end) {
    /*
     This function takes dates in the format 'yyyy-mm-dd'. We were thinking of checking the database for if the dates exist. 
     If they do, then the dates are valid. If not, then the dates are invalid. This allows us to skip any format validation. 
     A problem arises if there are no sales recorded in the database for the date that is entered.
    */
        Connection conn = null;
        String teamNumber = "14";
        String sectionNumber = "912";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        try {
            Class.forName("org.postgresql.Driver"); 
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception err) {
            err.printStackTrace();
            System.err.println(err.getClass().getName() + ": " + err.getMessage());
            return false; 
        }
        //System.out.println("Opened database successfully");
        try {
            String sqlQuery = "SELECT  \"Entrees\".\"Entree Items\", count(\"Order ID\") From \"Order\" Inner Join \"Entrees\" on \"Order\".\"Entree ID\" = \"Entrees\".\"Entree ID\" where \"Date\"  between '2022-08-01' And '2022-08-04' group by \"Entrees\".\"Entree Items\" order by \"Entrees\".\"Entree Items\"";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlQuery);

            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Dressing Item"));
                entry.add(result.getString("Dressing Inventory"));
            }
        } catch (Exception ex) {
            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        // System.out.println("Passed query successfully");
        
        try {
            conn.close();
            // System.out.println("Connection Closed.");
        } catch (Exception ex) {
            // System.out.println("Connection NOT Closed.");
            return false;
        }

        return false;
    }

    /**
    * Calls the Excess Report Input screen where a starting date is provided and the subsequent Excess Report is shown
     */
    public static void excess_report_input() {
        JFrame frame = new JFrame("EXCESS REPORT: INPUT");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel dateLabel1 = new JLabel("Enter in the start date (yyyy-mm-dd format): ");
        dateLabel1.setBounds(50, 20, 450, 200);
        dateLabel1.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField date1 = new JTextField();
        date1.setBounds(400, 108, 200, 27);
        date1.setFont(new Font("Arial", Font.BOLD, 16));

        JButton excessReport = new JButton("Generate Excess Report");// if pressed pos displays sales_report
        excessReport.setBackground(darkGreen);
        excessReport.setForeground(Color.white);
        excessReport.setBounds(625, 95, 220, 50);
        excessReport.setFont(new Font("Arial", Font.BOLD, 17));
        // Connection conn = null;
        // String teamNumber = "14";
        // String sectionNumber = "912";
        // String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
        // String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        // try {
        //     Class.forName("org.postgresql.Driver");
        //     conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        // } catch (Exception err) {
        //     err.printStackTrace();
        //     // System.err.println(err.getClass().getName() + ": " + err.getMessage());
        //     return;
        // }
        // // System.out.println("Opened database successfully");

        // try {
        //     String sqlQuery = "SELECT  \"Entrees\".\"Entree Items\", count(\"Order ID\") From \"Order\" Inner Join \"Entrees\" on \"Order\".\"Entree ID\" = \"Entrees\".\"Entree ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' group by \"Entrees\".\"Entree Items\" order by \"Entrees\".\"Entree Items\"";
        //     Statement stmt = conn.createStatement();
        //     ResultSet result = stmt.executeQuery(sqlQuery);
        //     while (result.next()) {
        //         Vector<String> entry = new Vector<>();
        //         entry.add(result.getString("Entree Items"));
        //         entry.add(result.getString("count"));
        //         inventoryTable.add(entry);
        //     }

        //     sqlQuery = "SELECT  \"Dressings\".\"Dressing Item\", count(\"Order ID\") From \"Order\" Inner Join \"Dressings\" on \"Order\".\"Dressing ID\" = \"Dressings\".\"Dressing ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' and \"Dressing Item\"!='None' group by \"Dressings\".\"Dressing Item\" order by \"Dressings\".\"Dressing Item\"";
        //     stmt = conn.createStatement();
        //     result = stmt.executeQuery(sqlQuery);
        //     while (result.next()) {
        //         Vector<String> entry = new Vector<>();
        //         entry.add(result.getString("Dressing Item"));
        //         entry.add(result.getString("count"));
        //         inventoryTable.add(entry);
        //     }

        //     sqlQuery = "SELECT  \"Drinks\".\"Drink Item\", count(\"Order ID\") From \"Order\" Inner Join \"Drinks\" on \"Order\".\"Drinks ID\" = \"Drinks\".\"Drink ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' and \"Drink Item\"!='None' group by \"Drinks\".\"Drink Item\" order by \"Drinks\".\"Drink Item\"";
        //     stmt = conn.createStatement();
        //     result = stmt.executeQuery(sqlQuery);
        //     while (result.next()) {
        //         Vector<String> entry = new Vector<>();
        //         entry.add(result.getString("Drink Item"));
        //         entry.add(result.getString("count"));
        //         inventoryTable.add(entry);
        //     }

        //     sqlQuery = "SELECT  \"Starters\".\"Starter Item\", count(\"Order ID\") From \"Order\" Inner Join \"Starters\" on \"Order\".\"Starter ID\" = \"Starters\".\"Starter ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' and \"Starter Item\"!='None' group by \"Starters\".\"Starter Item\" order by \"Starters\".\"Starter Item\"";
        //     stmt = conn.createStatement();
        //     result = stmt.executeQuery(sqlQuery);
        //     while (result.next()) {
        //         Vector<String> entry = new Vector<>();
        //         entry.add(result.getString("Starter Item"));
        //         entry.add(result.getString("count"));
        //         inventoryTable.add(entry);
        //     }

        //     sqlQuery = "SELECT  \"Toppings\".\"Topping Item\", count(\"Order ID\") From \"Order\" Inner Join \"Toppings\" on \"Order\".\"Topping IDs\"[1] = \"Toppings\".\"Topping ID\" where \"Date\"  between '" + date1.getText() + "' And '" + date2.getText() + "' and \"Topping Item\"!='None' group by \"Toppings\".\"Topping Item\" order by \"Toppings\".\"Topping Item\"";
        //     stmt = conn.createStatement();
        //     result = stmt.executeQuery(sqlQuery);
        //     while (result.next()) {
        //         Vector<String> entry = new Vector<>();
        //         entry.add(result.getString("Topping Item"));
        //         entry.add(result.getString("count"));
        //         inventoryTable.add(entry);
        //     }
        // } catch (Exception ex) {
        //     // System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        //     return;
        // }
        // // System.out.println("Passed query successfully");
        
        // try {
        //     conn.close();
        //     // System.out.println("Connection Closed.");
        // } catch (Exception ex) {
        //     // System.out.println("Connection NOT Closed.");
        //     return;
        // }


        JButton exit = new JButton("Exit to Manager View");
        exit.setBackground(deepRed);   
        exit.setForeground(Color.white);
        exit.setBounds(25, 700, 425, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_view();
                frame.setVisible(false);
            }
        });

        frame.add(dateLabel1);
        frame.add(date1);
        frame.add(excessReport);
        frame.add(exit);
        frame.setSize(1000,1000);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    /**
    * Calls the Restock Report Screen where all items 
     */
    public static void restock_report() {
        JFrame frame = new JFrame("RESTOCK INFO");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel restockTitle = new JLabel("Items to restock ");
        restockTitle.setBounds(375, 20, 600, 180);
        restockTitle.setFont(new Font("Arial", Font.BOLD, 25));
                
        Connection conn = null;
        String teamNumber = "14";
        String sectionNumber = "912";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }
            
        Vector<Vector<String>> inventoryTable = new Vector<>();

        try {
            String sqlQuery = "select * from \"Entrees\"";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Entree Items"));
                entry.add(result.getString("Entree Inventory"));
                int curr = Integer.parseInt(result.getString("Entree Inventory")); 
                if(curr<200){
                    inventoryTable.add(entry);
                }
            }

            sqlQuery = "select * from \"Dressings\" where \"Dressing Item\" != 'None'";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Dressing Item"));
                entry.add(result.getString("Dressing Inventory"));
                int curr = Integer.parseInt(result.getString("Dressing Inventory")); 
                if(curr<200){
                    inventoryTable.add(entry);
                }
            }

            sqlQuery = "select * from \"Drinks\" where \"Drink Item\" != 'None'";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Drink Item"));
                entry.add(result.getString("Drink Inventory"));
                int curr = Integer.parseInt(result.getString("Drink Inventory")); 
                if(curr<200){
                    inventoryTable.add(entry);
                }
            }

            sqlQuery = "select * from \"Starters\" where \"Starter Item\" != 'None'";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Starter Item"));
                entry.add(result.getString("Starter Inventory"));
                int curr = Integer.parseInt(result.getString("Starter Inventory")); 
                if(curr<200){
                    inventoryTable.add(entry);
                }
            }

            sqlQuery = "select * from \"Toppings\" where \"Topping Item\" != 'None'";
            stmt = conn.createStatement();
            result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Topping Item"));
                entry.add(result.getString("Topping Inventory"));
                int curr = Integer.parseInt(result.getString("Topping Inventory")); 
                if(curr<200){
                    inventoryTable.add(entry);
                }
            }
        } catch (Exception e) {
            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return;
        }
        // System.out.println("Passed query successfully");

        try {
            conn.close();
            // System.out.println("Connection Closed.");
        } catch (Exception e) {
            // System.out.println("Connection NOT Closed.");
        }
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Product Item");
        columnNames.add("Units left in stock");

        DefaultTableModel tableModel = new DefaultTableModel(inventoryTable, columnNames);

        JButton exit = new JButton("Exit to Manager View");
        exit.setBackground(deepRed);   
        exit.setForeground(Color.white);
        exit.setBounds(25, 825, 425, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_view();
                frame.setVisible(false);
            }
        });

        JTable table = new JTable(tableModel);
        
        JScrollPane sp = new JScrollPane(table);

        sp.setBounds(50, 200, 900, 475);
        frame.add(restockTitle);
        frame.add(exit);
        frame.add(sp);
        frame.setSize(1000,1000);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    /**
     * Loads the entree screen
     */
    public static void entree_screen() {
        // int entreeId;

        JFrame frame = new JFrame("ORDER: ENTREES");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSeasonal(); //ADDED

        JLabel label1 = new JLabel("Choose An Entree");
        label1.setBounds(250, 0, 825, 100);
        label1.setFont(new Font("Arial", Font.BOLD, 40));

        JLabel label2 = new JLabel("Or Add The Combo");
        label2.setBounds(250, 350, 825, 100);
        label2.setFont(new Font("Arial", Font.BOLD, 40));

        JButton bowlOption = new JButton("Grain Bowl"); // set it to where some entree has to have been selected
        JButton saladOption = new JButton("Salad");
        JButton pitaOption = new JButton("Pita");
        JButton greenOption = new JButton("Green & Grains");
        JButton gyroCombo = new JButton("Gyro Combo");
        
        bowlOption.setBounds(75, 150, 150, 50);// x axis, y axis, width, height
        bowlOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 0; // index 0
                bowlOption.setBackground(SystemColor.activeCaption);
                saladOption.setBackground(null);
                pitaOption.setBackground(null);
                greenOption.setBackground(null);
                gyroCombo.setBackground(null);
            }
        });

        saladOption.setBounds(275, 150, 150, 50);
        saladOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 5;
                bowlOption.setBackground(null);
                saladOption.setBackground(SystemColor.activeCaption);
                pitaOption.setBackground(null);
                greenOption.setBackground(null);
                gyroCombo.setBackground(null);
            }
        });

        pitaOption.setBounds(475, 150, 150, 50);
        pitaOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 10; // index 0
                bowlOption.setBackground(null);
                saladOption.setBackground(null);
                pitaOption.setBackground(SystemColor.activeCaption);
                greenOption.setBackground(null);
                gyroCombo.setBackground(null);
            }
        });
        greenOption.setBounds(675, 150, 150, 50);
        greenOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 15; // index 0
                bowlOption.setBackground(null);
                saladOption.setBackground(null);
                pitaOption.setBackground(null);
                greenOption.setBackground(SystemColor.activeCaption);
                gyroCombo.setBackground(null);
            }
        });

        /* 
         * 
        */

        JButton gyroPro = new JButton("Gyro");
        JButton falPro = new JButton("Falafel");
        JButton vegMedPro = new JButton("Vegetable Medley");
        JButton mBallsPro = new JButton("Meat Ball");
        JButton chknPro = new JButton("Chicken");
        
        gyroPro.setBounds(25, 250, 150, 50);// x axis, y axis, width, height
        gyroPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 0; // index 0
                gyroPro.setBackground(SystemColor.activeCaption);
                falPro.setBackground(null);
                vegMedPro.setBackground(null);
                mBallsPro.setBackground(null);
                chknPro.setBackground(null);
                gyroCombo.setBackground(null);
            }
        });
        falPro.setBounds(200, 250, 150, 50);
        falPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 1; // index 0
                gyroPro.setBackground(null);
                falPro.setBackground(SystemColor.activeCaption);
                vegMedPro.setBackground(null);
                mBallsPro.setBackground(null);
                chknPro.setBackground(null);
                gyroCombo.setBackground(null);
            }
        });
        vegMedPro.setBounds(375, 250, 150, 50);
        vegMedPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 2; // index 0
                gyroPro.setBackground(null);
                falPro.setBackground(null);
                vegMedPro.setBackground(SystemColor.activeCaption);
                mBallsPro.setBackground(null);
                chknPro.setBackground(null);
                gyroCombo.setBackground(null);
            }
        });

        JButton seasonalPro= new JButton(seasonalName);
        seasonalPro.setBounds(375, 325,150,50);
        seasonalPro.setBackground(lightBlue);
        seasonalPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = -1; // index 0
                gyroPro.setBackground(null);
                falPro.setBackground(null);
                vegMedPro.setBackground(null);
                seasonalPro.setBackground(SystemColor.activeCaption);
                mBallsPro.setBackground(null);
                chknPro.setBackground(null);
                gyroCombo.setBackground(null);
            }
        });
        if(seasonalType.equals("Protein")){
            frame.add(seasonalPro);
        }

        mBallsPro.setBounds(550, 250, 150, 50);
        mBallsPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 3; // index 0
                gyroPro.setBackground(null);
                falPro.setBackground(null);
                vegMedPro.setBackground(null);
                mBallsPro.setBackground(SystemColor.activeCaption);
                chknPro.setBackground(null);
                gyroCombo.setBackground(null);
            }
        });

        chknPro.setBounds(725, 250, 150, 50);
        chknPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 4; // index 0
                gyroPro.setBackground(null);
                falPro.setBackground(null);
                vegMedPro.setBackground(null);
                mBallsPro.setBackground(null);
                chknPro.setBackground(SystemColor.activeCaption);
                gyroCombo.setBackground(null);
            }
        });

        gyroCombo.setBounds(100, 500, 700, 100);
        gyroCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 20;
                protein = 0; // index 0
                bowlOption.setBackground(null);
                saladOption.setBackground(null);
                pitaOption.setBackground(null);
                greenOption.setBackground(null);
                gyroPro.setBackground(null);
                falPro.setBackground(null);
                vegMedPro.setBackground(null);
                mBallsPro.setBackground(null);
                chknPro.setBackground(null);
                gyroCombo.setBackground(SystemColor.activeCaption);
            }
        });

        JButton toppings = new JButton("Continue to Toppings & Dressings"); // set it to where some entree has to have
        toppings.setBackground(darkGreen);   
        toppings.setForeground(Color.white);                                                                 // been selected
        toppings.setBounds(475, 700, 425, 100);
        toppings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {// assumint that stuff is chosen correctly to start
                if(protein==-1){
                    theOrder=new order(type*-1-1);
                }
                else{
                    theOrder = new order(type + protein);
                }
                toppings_screen();
                frame.setVisible(false);
            }
        });

        JButton exit = new JButton("Exit to Main Screen");
        exit.setBackground(deepRed);   
        exit.setForeground(Color.white);
        exit.setBounds(25, 700, 425, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome();
                frame.setVisible(false);
            }
        });

        Color c = new Color(0, 255, 0);
        c = new Color(255, 0, 0);
        frame.add(label1);
        frame.add(label2);

        frame.add(saladOption);
        frame.add(bowlOption);
        frame.add(pitaOption);
        frame.add(greenOption);

        frame.add(gyroPro);
        frame.add(falPro);
        frame.add(vegMedPro);
        frame.add(mBallsPro);
        frame.add(chknPro);
        frame.add(gyroCombo);

        frame.add(toppings);
        frame.add(exit);
        c = new Color(20, 20, 20);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers

        frame.setVisible(true);
    }

    /**
     * Shows the topping choice menu
     */
    public static void toppings_screen() {
        int width = 164;
        int height = 50;
        int padding = 28;

        JFrame frame = new JFrame("ORDER: TOPPINGS");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel label1 = new JLabel("Choose Toppings: ");
        label1.setBounds(250, 0, 825, 100);
        label1.setFont(new Font("Arial", Font.BOLD, 40));

        JButton noTop = new JButton("No Topping"); // set it to where some entree has to have been selected
        noTop.setBounds(28, 150, 164, 50);
        noTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(0);
                noTop.setBackground(SystemColor.activeCaption);
            }
        });

        JButton pickOnionTop = new JButton("Pickled Onions"); // set it to where some entree has to have been selected
        pickOnionTop.setBounds(220, 150, 164, 50);
        pickOnionTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(1);
                pickOnionTop.setBackground(SystemColor.activeCaption);
            }
        });

        JButton diceCucumberTop = new JButton("Diced Cucumber");
        diceCucumberTop.setBounds(412, 150, 164, 50);
        diceCucumberTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(2);
                diceCucumberTop.setBackground(SystemColor.activeCaption);
            }
        });

        JButton citCousTop = new JButton("Citrius Couscous");
        citCousTop.setBounds(604, 150, 164, 50);
        citCousTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(3);
                citCousTop.setBackground(SystemColor.activeCaption);
            }
        });

        JButton roastCauliTop = new JButton("Roasted Cauliflower");
        roastCauliTop.setBounds(796, 150, 164, 50);
        roastCauliTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(4);
                roastCauliTop.setBackground(SystemColor.activeCaption);
            }
        });

        JButton tomOnTop = new JButton("Tomato-onion Salad");
        tomOnTop.setBounds(130, 228, 164, 50);
        tomOnTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(5);
                tomOnTop.setBackground(SystemColor.activeCaption);
            }
        });

        JButton kalamataTop = new JButton("Kalamatas");
        kalamataTop.setBounds(322, 228, 164, 50);
        kalamataTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(6);
                kalamataTop.setBackground(SystemColor.activeCaption);
            }
        });

        JButton roastPeppTop = new JButton("Roasted Peppers");
        roastPeppTop.setBounds(514, 228, 164, 50);
        roastPeppTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(7);
                roastPeppTop.setBackground(SystemColor.activeCaption);
            }
        });

        JButton redCabbTop = new JButton("Red Cabbage Slaw");
        redCabbTop.setBounds(706, 228, 164, 50);
        redCabbTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (theOrder.getNumToppings() >= 4) {
                    demo.displayMessage("Error: Too Many Toppings!");
                    return;
                }
                theOrder.addTopping(8);
                redCabbTop.setBackground(SystemColor.activeCaption);
            }
        });

        JLabel dressingsLabel = new JLabel("Choose Dressings: ");
        dressingsLabel.setBounds(250, 350, 825, 100);
        dressingsLabel.setFont(new Font("Arial", Font.BOLD, 40));

        JButton noDress = new JButton("No Dressing");
        JButton hummusDress = new JButton("Hummus");
        JButton redPeppHummusDress = new JButton("Red Pepper Hummus");
        JButton jalapFetaDress = new JButton("Jalapeno Feta");
        JButton tzatzikiDress = new JButton("Tzatziki");
        JButton greekVinaDress = new JButton("Greek Vinaigrette");
        JButton harissYogDress = new JButton("Harissa Yogurt");
        JButton lemonHerbTahiDress = new JButton("Lemon Herb Tahini");
        JButton yogDillDress = new JButton("Yogurt Dill");
        
        noDress.setBounds(28, 500, 164, 50);
        noDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(0);
                noDress.setBackground(SystemColor.activeCaption);
                hummusDress.setBackground(null);
                redPeppHummusDress.setBackground(null);
                jalapFetaDress.setBackground(null);
                tzatzikiDress.setBackground(null);
                greekVinaDress.setBackground(null);
                harissYogDress.setBackground(null);
                lemonHerbTahiDress.setBackground(null);
                yogDillDress.setBackground(null);
            }
        });

        hummusDress.setBounds(220, 500, 164, 50);
        hummusDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(1);
                noDress.setBackground(null);
                hummusDress.setBackground(SystemColor.activeCaption);
                redPeppHummusDress.setBackground(null);
                jalapFetaDress.setBackground(null);
                tzatzikiDress.setBackground(null);
                greekVinaDress.setBackground(null);
                harissYogDress.setBackground(null);
                lemonHerbTahiDress.setBackground(null);
                yogDillDress.setBackground(null);
            }
        });

        redPeppHummusDress.setBounds(412, 500, 164, 50);
        redPeppHummusDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(2);
                noDress.setBackground(null);
                hummusDress.setBackground(null);
                redPeppHummusDress.setBackground(SystemColor.activeCaption);
                jalapFetaDress.setBackground(null);
                tzatzikiDress.setBackground(null);
                greekVinaDress.setBackground(null);
                harissYogDress.setBackground(null);
                lemonHerbTahiDress.setBackground(null);
                yogDillDress.setBackground(null);
            }
        });

        jalapFetaDress.setBounds(604, 500, 164, 50);
        jalapFetaDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(3);
                noDress.setBackground(null);
                hummusDress.setBackground(null);
                redPeppHummusDress.setBackground(null);
                jalapFetaDress.setBackground(SystemColor.activeCaption);
                tzatzikiDress.setBackground(null);
                greekVinaDress.setBackground(null);
                harissYogDress.setBackground(null);
                lemonHerbTahiDress.setBackground(null);
                yogDillDress.setBackground(null);
            }
        });

        tzatzikiDress.setBounds(796, 500, 164, 50);
        tzatzikiDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(4);
                noDress.setBackground(null);
                hummusDress.setBackground(null);
                redPeppHummusDress.setBackground(null);
                jalapFetaDress.setBackground(null);
                tzatzikiDress.setBackground(SystemColor.activeCaption);
                greekVinaDress.setBackground(null);
                harissYogDress.setBackground(null);
                lemonHerbTahiDress.setBackground(null);
                yogDillDress.setBackground(null);
            }
        });

        greekVinaDress.setBounds(130, 578, 164, 50);
        greekVinaDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(5);
                noDress.setBackground(null);
                hummusDress.setBackground(null);
                redPeppHummusDress.setBackground(null);
                jalapFetaDress.setBackground(null);
                tzatzikiDress.setBackground(null);
                greekVinaDress.setBackground(SystemColor.activeCaption);
                harissYogDress.setBackground(null);
                lemonHerbTahiDress.setBackground(null);
                yogDillDress.setBackground(null);
            }
        });

        harissYogDress.setBounds(322, 578, 164, 50);
        harissYogDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(6);
                noDress.setBackground(null);
                hummusDress.setBackground(null);
                redPeppHummusDress.setBackground(null);
                jalapFetaDress.setBackground(null);
                tzatzikiDress.setBackground(null);
                greekVinaDress.setBackground(null);
                harissYogDress.setBackground(SystemColor.activeCaption);
                lemonHerbTahiDress.setBackground(null);
                yogDillDress.setBackground(null);
            }
        });

        lemonHerbTahiDress.setBounds(514, 578, 164, 50);
        lemonHerbTahiDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(7);
                noDress.setBackground(null);
                hummusDress.setBackground(null);
                redPeppHummusDress.setBackground(null);
                jalapFetaDress.setBackground(null);
                tzatzikiDress.setBackground(null);
                greekVinaDress.setBackground(null);
                harissYogDress.setBackground(null);
                lemonHerbTahiDress.setBackground(SystemColor.activeCaption);
                yogDillDress.setBackground(null);
            }
        });

        yogDillDress.setBounds(706, 578, 164, 50);
        yogDillDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(8);
                noDress.setBackground(null);
                hummusDress.setBackground(null);
                redPeppHummusDress.setBackground(null);
                jalapFetaDress.setBackground(null);
                tzatzikiDress.setBackground(null);
                greekVinaDress.setBackground(null);
                harissYogDress.setBackground(null);
                lemonHerbTahiDress.setBackground(null);
                yogDillDress.setBackground(SystemColor.activeCaption);
            }
        });

        JButton starter = new JButton("Continue to Starters & Drinks");
        starter.setBackground(darkGreen);   
        starter.setForeground(Color.white);
        starter.setBounds(475, 700, 425, 100);
        starter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                starter_menu();
                frame.setVisible(false);
            }
        });

        JButton entree = new JButton("Return to Entrees");
        entree.setBackground(deepRed);   
        entree.setForeground(Color.white);
        entree.setBounds(25, 700, 425, 100);
        entree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entree_screen();
                frame.setVisible(false);
            }
        });

        // Color c = new Color(0, 0, 255);
        // noTop.setBackground(c);
        // pickOnionTop.setBackground(c);
        // diceCucumberTop.setBackground(c);
        // citCousTop.setBackground(c);
        // roastCauliTop.setBackground(c);
        // tomOnTop.setBackground(c);
        // kalamataTop.setBackground(c);
        // roastPeppTop.setBackground(c);
        // redCabbTop.setBackground(c);

        frame.add(starter);
        frame.add(entree);

        frame.add(label1);
        frame.add(dressingsLabel);

        frame.add(noTop);
        frame.add(pickOnionTop);
        frame.add(diceCucumberTop);
        frame.add(citCousTop);
        frame.add(roastCauliTop);
        frame.add(tomOnTop);
        frame.add(kalamataTop);
        frame.add(roastPeppTop);
        frame.add(redCabbTop);

        frame.add(noDress);
        frame.add(hummusDress);
        frame.add(redPeppHummusDress);
        frame.add(jalapFetaDress);
        frame.add(tzatzikiDress);
        frame.add(greekVinaDress);
        frame.add(harissYogDress);
        frame.add(lemonHerbTahiDress);
        frame.add(yogDillDress);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true);
    }

    /**
     * Shows the starter menu
     */
    public static void starter_menu() {

        JFrame frame = new JFrame("ORDER: STARTERS & DRINKS");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel starterLabel = new JLabel("Choose Starter: ");
        starterLabel.setBounds(250, 0, 825, 100);
        starterLabel.setFont(new Font("Arial", Font.BOLD, 40));

        JButton noStarter = new JButton("None");
        JButton falStart = new JButton("Falafels");
        JButton hummus = new JButton("Hummus & Pita");
        JButton vegan = new JButton("Vegan Box");
        JButton fries = new JButton("Garlic Fries");

        noStarter.setBounds(28, 150, 164, 50);
        noStarter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(0);
                noStarter.setBackground(SystemColor.activeCaption);
                falStart.setBackground(null);
                hummus.setBackground(null);
                vegan.setBackground(null);
                fries.setBackground(null);
            }
        });
        falStart.setBounds(220, 150, 164, 50);
        falStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(1);
                noStarter.setBackground(null);
                falStart.setBackground(SystemColor.activeCaption);
                hummus.setBackground(null);
                vegan.setBackground(null);
                fries.setBackground(null);
            }
        });
        hummus.setBounds(412, 150, 164, 50);
        hummus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(2);
                noStarter.setBackground(null);
                falStart.setBackground(null);
                hummus.setBackground(SystemColor.activeCaption);
                vegan.setBackground(null);
                fries.setBackground(null);
            }
        });
        vegan.setBounds(604, 150, 164, 50);
        vegan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(3);
                noStarter.setBackground(null);
                falStart.setBackground(null);
                hummus.setBackground(null);
                vegan.setBackground(SystemColor.activeCaption);
                fries.setBackground(null);
            }
        });
        fries.setBounds(796, 150, 164, 50);
        fries.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(4);
                noStarter.setBackground(null);
                falStart.setBackground(null);
                hummus.setBackground(null);
                vegan.setBackground(null);
                fries.setBackground(SystemColor.activeCaption);
            }
        });
        JButton drinks = new JButton("Continue to Drinks");// go to drink menu
        drinks.setBackground(darkGreen);   
        drinks.setForeground(Color.white);
        drinks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
            }
        });

        JLabel drinksLabel = new JLabel("Select your drink:");
        drinksLabel.setBounds(250, 350, 825, 100);
        drinksLabel.setFont(new Font("Arial", Font.BOLD, 40));
        
        JButton bottlednone = new JButton("None");
        JButton bottledWaterDrink = new JButton("Bottled Water");
        JButton bottledSodaDrink = new JButton("Bottled Soda");
        JButton fountainSodaDrink = new JButton("Fountain Soda");

        bottlednone.setBounds(130, 500, 164, 50);
        bottlednone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDrink(0);
                bottlednone.setBackground(SystemColor.activeCaption);
                bottledSodaDrink.setBackground(null);
                fountainSodaDrink.setBackground(null);
            }
        });
        bottledWaterDrink.setBounds(322, 500, 164, 50);
        bottledWaterDrink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDrink(1);
                bottledWaterDrink.setBackground(SystemColor.activeCaption);
                bottledSodaDrink.setBackground(null);
                fountainSodaDrink.setBackground(null);
            }
        });
        bottledSodaDrink.setBounds(514, 500, 164, 50);
        bottledSodaDrink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDrink(2);
                bottledWaterDrink.setBackground(null);
                bottledSodaDrink.setBackground(SystemColor.activeCaption);
                fountainSodaDrink.setBackground(null);
            }
        });

        JButton seasonalStarter= new JButton(seasonalName);
        seasonalStarter.setBounds(412, 325,164,50);
        seasonalStarter.setBackground(lightBlue);
        seasonalStarter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(-1);
                noStarter.setBackground(null);
                falStart.setBackground(null);
                hummus.setBackground(null);
                vegan.setBackground(null);
                fries.setBackground(null);
                seasonalStarter.setBackground(SystemColor.activeCaption);
            }
        });
        if(seasonalType.equals("Starter")){
            frame.add(seasonalStarter);
        }

        JButton seasonalDrink= new JButton(seasonalName);
        seasonalDrink.setBounds(412, 575,164,50);
        seasonalDrink.setBackground(lightBlue);
        seasonalDrink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDrink(-1);
                bottledWaterDrink.setBackground(null);
                bottledSodaDrink.setBackground(null);
                fountainSodaDrink.setBackground(null);
                seasonalDrink.setBackground(SystemColor.activeCaption);
            }
        });
        if(seasonalType.equals("Drink")){
            frame.add(seasonalDrink);
        }

        fountainSodaDrink.setBounds(706, 500, 164, 50);
        fountainSodaDrink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDrink(3);
                bottledWaterDrink.setBackground(null);
                bottledSodaDrink.setBackground(null);
                fountainSodaDrink.setBackground(SystemColor.activeCaption);
            }
        });

        JButton payment = new JButton("Continue to Payment");
        payment.setBackground(darkGreen);   
        payment.setForeground(Color.white);
        payment.setBounds(475, 700, 425, 100);
        payment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payment_page();
                frame.setVisible(false);
            }
        });

        JButton toppings = new JButton("Return to Toppings & Dressings");
        toppings.setBackground(deepRed);   
        toppings.setForeground(Color.white);
        toppings.setBounds(25, 700, 425, 100);
        toppings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toppings_screen();
                frame.setVisible(false);
            }
        });

        frame.add(starterLabel);
        frame.add(drinksLabel);

        frame.add(payment);
        frame.add(toppings);

        frame.add(noStarter);
        frame.add(falStart);
        frame.add(hummus);
        frame.add(vegan);
        frame.add(fries);
        frame.add(drinks);
        frame.add(bottlednone);
        frame.add(bottledWaterDrink);
        frame.add(bottledSodaDrink);
        frame.add(fountainSodaDrink);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true); // making the frame visible
    }

    

    /**
     * Places the order and acts as the payment screen
     */
    public static void payment_page() {
        JFrame frame = new JFrame("PAYMENT");
        Color background = new Color(47, 79, 79);
        frame.setBackground(background);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton placeOrder = new JButton("Place Order!");// if pressed pos displays menu_screen
        placeOrder.setBounds(50, 50, 825, 200);
        placeOrder.setFont(new Font("Arial", Font.BOLD, 40));
        placeOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // sql input statement query
                String sqlQuery = theOrder.sqlQuery(employeeID);

                // here's where we call the jdbc page
                jdbcpostgreSQL.orderQuery(sqlQuery);

                // go back to home page
                welcome();
                frame.setVisible(false);
            }
        });
        Color c1 = new Color(0, 255, 0);
        placeOrder.setBackground(darkGreen);
        placeOrder.setForeground(Color.white);

        frame.add(placeOrder);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true); // making the frame visible
    }
    
    
    /**
    * Loads the seasonal item screen
     */
    public static void seasonal_item(){
        JFrame frame = new JFrame("ADD SEASONAL ITEM");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
        String[] typeStrings= {"Protein", "Drink", "Starter"}; 
        JLabel typeLabel = new JLabel("What type of seasonal item would you like to add?");
        typeLabel.setFont(new Font("Arial",Font.BOLD,20));
        typeLabel.setBounds(250,25,600,50);
        //@SuppressWarnings("unchecked")
        JComboBox<String> typeBox = new JComboBox<String>(typeStrings);
        typeBox.setBounds(350,100,300,50);
        
        JLabel inventoryLabel = new JLabel("How many would you like to add?");
        inventoryLabel.setFont(new Font("Arial",Font.BOLD,20));
        inventoryLabel.setBounds(340,200,350,50);
        JTextField inventoryAmnt = new JTextField(50);
        inventoryAmnt.setBounds(350,275,300,50);

        JLabel priceLabel = new JLabel("If the item is a Starter please enter a price");
        priceLabel.setFont(new Font("Arial",Font.BOLD,20));
        priceLabel.setBounds(290,350,600,50);
        JTextField price = new JTextField(20);
        price.setBounds(350,425,300,50);

        JLabel nameLabel = new JLabel("Please name this new item");
        nameLabel.setFont(new Font("Arial",Font.BOLD,20));
        nameLabel.setBounds(370,500,350,50);
        JTextField name = new JTextField(50);
        name.setBounds(350,575,300,50);

        JButton exit = new JButton("Exit to Manager Screen");
        exit.setBackground(deepRed);   
        exit.setForeground(Color.white);
        exit.setBounds(0, 800, 500, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_view();
                frame.setVisible(false);
            }
        });
        JButton update = new JButton("Add Item");
        update.setBackground(darkGreen);
        update.setForeground(Color.white);
        update.setBounds(500, 800, 500, 100);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemT=typeBox.getSelectedItem().toString();
                String inv=inventoryAmnt.getText();
                String cost=price.getText();
                String itemName=name.getText();

                int invInt=0;
                Double costDub=0.0;
                int error=0;
                if(inv.length()==0){
                    error=1;
                }
                else{
                    try{
                        Integer.parseInt(inv);
                    }
                    catch(NumberFormatException f){
                        error=1;
                    }
                    if(error==0){
                        invInt=Integer.parseInt(inv);
                        if(invInt<=0){
                            error=1;
                        }
                    }
                }
                if(error==0 && itemT.equals("Starter")){
                    try{
                        Double.parseDouble(cost);
                    }
                    catch(NumberFormatException f){
                        error=2;
                    }
                    if(error==0){
                        costDub=Double.parseDouble(cost);
                        if(costDub<0){
                            error=2;
                        }
                    }
                }
                if(error==0 && itemName.length()==0){
                    error=3;
                }
                validate_seasonal(error);//this displays error/success for input validation
                if(error==0){
                    String sqlInput = "INSERT INTO \"PromotionalItem\" (\"Promotional Item Name\", \"Item type\", \"Item Price\", \"Item inventory\" ) values ('"
                + itemName + "' , '"+ itemT +"'," + costDub + "," + invInt + ");";
                jdbcpostgreSQL.seasonalQuery(sqlInput);
                manager_view();
                frame.setVisible(false);
                }
                
            }
        });
        
        frame.add(typeLabel);
        frame.add(inventoryLabel);
        frame.add(priceLabel);
        frame.add(nameLabel);

        frame.add(typeBox);
        frame.add(inventoryAmnt);
        frame.add(price);
        frame.add(name);

        frame.add(exit);
        frame.add(update);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true);
    }

    /**
    * Helper function that provides input validation for seasonal item
     */
    public static void validate_seasonal(int error){
       // JFrame frame;
        String[] messages= {"Item added successfully", "Please enter a valid amount", "Please enter a valid price","Please enter a name","Successfuly Deleted"};
        displayMessage(messages[error]);
        /*if(error==0||error==4){
            frame=new JFrame("SUCCESS");
        }
        else{
            frame= new JFrame("ERROR!");
        }*/

        /*JLabel msgLabel = new JLabel(messages[error]);
        msgLabel.setFont(new Font("Arial",Font.BOLD,20));
        msgLabel.setBounds(25,50,450,50);

        frame.add(msgLabel);

        frame.setSize(500, 200);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true);*/
    }
    
    /**
    * Interfaces with the SQL database to produce the new seasonal item
     */
    public static void setSeasonal(){
        if(jdbcpostgreSQL.seasonalCountQuery()==0){
            seasonalExists=false;
            return;
        }
        else{
            seasonalExists=true;
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
                seasonalName=rs.getString("Promotional Item Name");
                seasonalType=rs.getString("Item type");
                

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
    }

    /**
    * General boilerplate function for displaying a message
     */
    public static void displayMessage(String msg) {
        JWindow w = new JWindow();
        JLabel l = new JLabel(msg);
        l.setBounds(35,20,280, 40);
        JButton b = new JButton("OK");
        b.setBounds(52,85, 180, 50);
        b.setBackground(lightBlue);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                w.setVisible(false);
            }
        });

        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        w.getContentPane().add(p,"Center");
        p.add(l);
        p.add(b);
        p.setLayout(null);
        
        //w.add(p);
        w.setSize(300, 150);
        w.setLocation(350, 350);
        w.setVisible(true);
        w.setLayout(null);
    }

    public static void main(String[] args) {
        demo.welcome();
        
    }
}