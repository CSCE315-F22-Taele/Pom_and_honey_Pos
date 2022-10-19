import java.sql.*;
import java.util.*;
import java.io.File;

/** 
 * A class to store Employee data
 * @author Nick, Aadith, Ismat, Nebiyou
 */
public class employee { //this was originally called server in our plans but using an
    private int empId;  // employee class for both server and managers makes more sense imo
    private String name;
    private String password;
    private boolean manager; //if true then has more permissions
    
    /**
    * A constructor that that returns the employee object
    * @param name a string that supposed to be set to employee 
     */
    public employee(String name){//should only be called if trying to create a new one
    
    
    }
    /** Creates an employee object
    * @param empId the employee's ID 
    * @param inputPass the password for the employee to use the POS
    * 
    *
     */
    public employee(int empId, String inputPass){
        this.empId=empId;
        //should test if employee id is real before continuing
        //then pull password from our sql table and verfiy 
        verifyPass(inputPass);
        //if verification passed then pull the rest from sql;

    }
    
    /** Verifies password with the database
    *@param password a password tp be checked if valid
    *  @return a boolean whether or not the password matches the employee's password
     */
    public boolean verifyPass(String password){
        return password.equals(this.password);
    }

    /**
     * Getter for Employee num
     * @return return an int of the Employee
     * 
     */
    public int getEmpNum(){
        return empId;
    }
    
    /**
     * Setter for Employee num
     * @param id id to be set to
     * 
     */
    public void setEmpNum(int id){
        empId=id;
    }
    
    /**
     * Setter for Employee password
     * @param password password to be set to
     * 
     */
    public void setPass(String password){
        this.password=password;
    }

    /**
     * Getter for Employee password
     * @return String containing the password
     * 
     */
    public String getPass(){
        return password;
    }
    
}
