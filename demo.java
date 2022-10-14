import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

/**
 * @author Nick, Ismat, Nebiyou, Aadith
 */
public class demo extends JFrame {
    public static int employeeID;
    public static int type;
    public static int protein;
    public static order theOrder = new order();

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
        takeOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entree_screen();
                frame.setVisible(false);

            }
        });
        takeOrder.setBounds(25, 50, 300, 100);

        JButton addItem = new JButton("Add Seasonal Item");
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add seasonal item screen;
                frame.setVisible(false);

            }
        });
        addItem.setBounds(675, 50, 300, 100);
        
        JButton viewInventory = new JButton("View Inventory");
        viewInventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // see inventory screen;
                view_inventory();
                frame.setVisible(false);
                
            }
        });
        viewInventory.setBounds(350, 50, 300, 100);

        JButton exit = new JButton("Exit to Main Screen");
        exit.setBounds(25, 700, 425, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome();
                frame.setVisible(false);
            }
        });

        frame.add(takeOrder);
        // frame.add(addItem);
        frame.add(viewInventory);
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
        takeOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entree_screen();
                frame.setVisible(false);

            }
        });
        takeOrder.setBounds(100, 100, 100, 100);

        JButton exit = new JButton("Exit to Main Screen");
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
        int[] partitions = new int[4];
        int line = 0;
        try {
            String sqlQuery = "select * from \"Entrees\"";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlQuery);
            while (result.next()) {
                Vector<String> entry = new Vector<>();
                entry.add(result.getString("Entree Items"));
                entry.add(result.getString("Entree Inventory"));
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
        exit.setBounds(0, 800, 500, 100);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager_view();
                frame.setVisible(false);
            }
        });
        JButton update = new JButton("Update inventory changes");
        update.setBounds(500, 800, 500, 100);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println("Update");
                update_inventory(changes);
            }
        });

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Item");
        columnNames.add("Stock");

        DefaultTableModel tableModel = new DefaultTableModel(inventoryTable, columnNames);
        tableModel.addTableModelListener(new TableModelListener() {
    
            public void tableChanged(TableModelEvent e) {
                if (e.getType() != e.UPDATE) {
                    return;
                }

                int colChanged = e.getColumn();
                if (colChanged == 0) {
                    return;
                }
                
                int rowChanged = e.getFirstRow();
                String tblName = "";
                if (rowChanged >= partitions[3]) {
                    tblName = "Toppings";
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

                String sqlQuery = "";
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
                    sqlQuery = "UPDATE \"Entrees\" SET \"Entree Inventory\"=" + stock + " WHERE \"Entree Items\"='" + item + "'";
                } else if (tblName.equals("Dressings")) {
                    sqlQuery = "UPDATE \"Dressings\" SET \"Dressing Inventory\"=" + stock + " WHERE \"Dressing Item\"='" + item + "'";
                } else if (tblName.equals("Drinks")) {
                    sqlQuery = "UPDATE \"Drinks\" SET \"Drink Inventory\"=" + stock + " WHERE \"Drink Item\"='" + item + "'";
                } else if (tblName.equals("Starters")) {
                    sqlQuery = "UPDATE \"Starters\" SET \"Starter Inventory\"=" + stock + " WHERE \"Starter Item\"='" + item + "'";
                } else if (tblName.equals("Toppings")) {
                    sqlQuery = "UPDATE \"Toppings\" SET \"Topping Inventory\"=" + stock + " WHERE \"Topping Item\"='" + item + "'";
                }
                
                ResultSet result = stmt.executeQuery(sqlQuery);
                // System.out.println(sqlQuery);
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
     * Loads the entree screen
     */
    public static void entree_screen() {
        // int entreeId;

        JFrame frame = new JFrame("ORDER: ENTREES");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel label1 = new JLabel("Choose An Entree");
        label1.setBounds(250, 0, 825, 100);
        label1.setFont(new Font("Arial", Font.BOLD, 40));

        JLabel label2 = new JLabel("Or Add The Combo");
        label2.setBounds(250, 350, 825, 100);
        label2.setFont(new Font("Arial", Font.BOLD, 40));

        JButton bowlOption = new JButton("Grain Bowl"); // set it to where some entree has to have been selected
        bowlOption.setBounds(75, 150, 150, 50);// x axis, y axis, width, height
        bowlOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 0; // index 0
            }
        });

        JButton saladOption = new JButton("Salad"); // set it to where some entree has to have been selected
        saladOption.setBounds(275, 150, 150, 50);
        saladOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 5;
            }
        });

        JButton pitaOption = new JButton("Pita"); // set it to where some entree has to have been selected
        pitaOption.setBounds(475, 150, 150, 50);
        pitaOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 10; // index 0
            }
        });
        JButton greenOption = new JButton("Green & Grains"); // set it to where some entree has to have been selected
        greenOption.setBounds(675, 150, 150, 50);
        greenOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 15; // index 0
            }
        });

        /* 
         * 
        */

        JButton gyroPro = new JButton("Gyro"); // set it to where some entree has to have been selected
        gyroPro.setBounds(25, 250, 150, 50);// x axis, y axis, width, height
        gyroPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 0; // index 0
            }
        });
        JButton falPro = new JButton("Falafel"); // set it to where some entree has to have been selected
        falPro.setBounds(200, 250, 150, 50);
        falPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 1; // index 0
            }
        });
        JButton vegMedPro = new JButton("Vegetable Medley"); // set it to where some entree has to have been selected
        vegMedPro.setBounds(375, 250, 150, 50);
        vegMedPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 2; // index 0
            }
        });

        JButton mBallsPro = new JButton("Meat Ball"); // set it to where some entree has to have been selected
        mBallsPro.setBounds(550, 250, 150, 50);
        mBallsPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 3; // index 0
            }
        });

        JButton chknPro = new JButton("Chicken"); // set it to where some entree has to have been selected
        chknPro.setBounds(725, 250, 150, 50);
        chknPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protein = 4; // index 0
            }
        });

        JButton gyroCombo = new JButton("Gyro Combo"); // set it to where some entree has to have been selected
        gyroCombo.setBounds(100, 500, 700, 100);
        gyroCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 20;
                protein = 0; // index 0
            }
        });

        JButton toppings = new JButton("Continue to Toppings & Dressings"); // set it to where some entree has to have
                                                                            // been selected
        toppings.setBounds(475, 700, 425, 100);
        toppings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {// assumint that stuff is chosen correctly to start

                theOrder = new order(type + protein);
                toppings_screen();
                frame.setVisible(false);
            }
        });

        JButton exit = new JButton("Exit to Main Screen");
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
     * shows the topping choice menu
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
                theOrder.addTopping(0);
            }
        });

        JButton pickOnionTop = new JButton("Pickled Onions"); // set it to where some entree has to have been selected
        pickOnionTop.setBounds(220, 150, 164, 50);
        pickOnionTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addTopping(1);
            }
        });

        JButton diceCucumberTop = new JButton("Diced Cucumber");
        diceCucumberTop.setBounds(412, 150, 164, 50);
        diceCucumberTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addTopping(2);
            }
        });

        JButton citCousTop = new JButton("Citrius Couscous");
        citCousTop.setBounds(604, 150, 164, 50);
        citCousTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addTopping(3);
            }
        });

        JButton roastCauliTop = new JButton("Roasted Cauliflower");
        roastCauliTop.setBounds(796, 150, 164, 50);
        roastCauliTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addTopping(4);
            }
        });

        JButton tomOnTop = new JButton("Tomato-onion Salad");
        tomOnTop.setBounds(130, 228, 164, 50);
        tomOnTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addTopping(5);
            }
        });

        JButton kalamataTop = new JButton("Kalamatas");
        kalamataTop.setBounds(322, 228, 164, 50);
        kalamataTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addTopping(6);
            }
        });

        JButton roastPeppTop = new JButton("Roasted Peppers");
        roastPeppTop.setBounds(514, 228, 164, 50);
        roastPeppTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addTopping(7);
            }
        });

        JButton redCabbTop = new JButton("Red Cabbage Slaw");
        redCabbTop.setBounds(706, 228, 164, 50);
        redCabbTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addTopping(8);
            }
        });

        JLabel dressingsLabel = new JLabel("Choose Dressings: ");
        dressingsLabel.setBounds(250, 350, 825, 100);
        dressingsLabel.setFont(new Font("Arial", Font.BOLD, 40));

        JButton noDress = new JButton("No Dressing");
        noDress.setBounds(28, 500, 164, 50);
        noDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(0);
            }
        });

        JButton hummusDress = new JButton("Hummus");
        hummusDress.setBounds(220, 500, 164, 50);
        hummusDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(1);
            }
        });

        JButton redPeppHummusDress = new JButton("Red Pepper Hummus");
        redPeppHummusDress.setBounds(412, 500, 164, 50);
        redPeppHummusDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(2);
            }
        });

        JButton jalapFetaDress = new JButton("Jalapeno Feta");
        jalapFetaDress.setBounds(604, 500, 164, 50);
        jalapFetaDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(3);
            }
        });

        JButton tzatzikiDress = new JButton("Tzatziki");
        tzatzikiDress.setBounds(796, 150, 164, 50);
        tzatzikiDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(4);
            }
        });

        JButton greekVinaDress = new JButton("Greek Vinaigrette");
        greekVinaDress.setBounds(130, 578, 164, 50);
        greekVinaDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(5);
            }
        });

        JButton harissYogDress = new JButton("Harissa Yogurt");
        harissYogDress.setBounds(322, 578, 164, 50);
        harissYogDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(6);
            }
        });

        JButton lemonHerbTahiDress = new JButton("Lemon Herb Tahini");
        lemonHerbTahiDress.setBounds(514, 578, 164, 50);
        lemonHerbTahiDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(7);
            }
        });

        JButton yogDillDress = new JButton("Yogurt Dill");
        yogDillDress.setBounds(706, 578, 164, 50);
        yogDillDress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDressing(8);
            }
        });

        JButton starter = new JButton("Continue to Starters & Drinks");
        starter.setBounds(475, 700, 425, 100);
        starter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                starter_menu();
                frame.setVisible(false);
            }
        });

        JButton entree = new JButton("Return to Entrees");
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
        noStarter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(0);
            }
        });
        JButton falStart = new JButton("Falafels");
        falStart.setBounds(130, 150, 164, 50);
        falStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(1);
            }
        });
        JButton hummus = new JButton("Hummus & Pita");
        hummus.setBounds(322, 150, 164, 50);
        hummus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(1);
            }
        });
        JButton vegan = new JButton("Vegan Box");
        vegan.setBounds(514, 150, 164, 50);
        vegan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(3);
            }
        });
        JButton fries = new JButton("Garlic Fries");
        fries.setBounds(706, 150, 164, 50);
        fries.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addStarter(3);
            }
        });
        JButton drinks = new JButton("Continue to Drinks");// go to drink menu
        drinks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
            }
        });

        JLabel drinksLabel = new JLabel("Select your drink:");
        drinksLabel.setBounds(250, 350, 825, 100);
        drinksLabel.setFont(new Font("Arial", Font.BOLD, 40));

        JButton bottledWaterDrink = new JButton("Bottled Water");// if pressed pos displays menu_screen
        bottledWaterDrink.setBounds(220, 500, 164, 50);
        // bottledWaterDrink.setFont(new Font("Arial", Font.BOLD, 40));
        bottledWaterDrink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDrink(1);
            }
        });
        Color c1 = new Color(0, 255, 0);
        // bottledWaterDrink.setBackground(c1);

        JButton bottledSodaDrink = new JButton("Bottled Soda");// if pressed pos displays menu_screen
        bottledSodaDrink.setBounds(412, 500, 164, 50);
        // bottledSodaDrink.setFont(new Font("Arial", Font.BOLD, 40));
        bottledSodaDrink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDrink(2);
            }
        });
        // bottledSodaDrink.setBackground(c1);

        JButton fountainSodaDrink = new JButton("Fountain Soda");// if pressed pos displays menu_screen
        fountainSodaDrink.setBounds(604, 500, 164, 50);
        // fountainSodaDrink.setFont(new Font("Arial", Font.BOLD, 40));
        fountainSodaDrink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theOrder.addDrink(3);
            }
        });

        JButton payment = new JButton("Continue to Payment");
        payment.setBounds(475, 700, 425, 100);
        payment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payment_page();
                frame.setVisible(false);
            }
        });

        JButton toppings = new JButton("Return to Toppings & Drinks");
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
        placeOrder.setBackground(c1);

        frame.add(placeOrder);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true); // making the frame visible
    }

    public static void main(String[] args) {
        // JFrame frame;
        demo.welcome();
        // frame.setVisible(true);
        // demo.menu_screen();
        // frame.setVisible(true);
    }
}