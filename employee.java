import java.sql.*;
import java.util.*;
import java.io.File;
/**@author
 * Nick, Aadith, Ismat, Nebiyou
**/
public class employee { //this was originally called server in our plans but using an
    private int empId;  // employee class for both server and managers makes more sense imo
    private String name;
    private String password;
    private boolean manager; //if true then has more permissions
    
    /**
    * @param name
    *
     */
    public employee(String name){//should only be called if trying to create a new one
    
    
    }
    /** Creates an employee object
    * @param empId,inputPass
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
    *@param password
    *
     */
    public boolean verifyPass(String password){
        return password.equals(this.password);
    }
    /**     
    Getter for Employee num
    */
    public int getEmpNum(){
        return empId;
    }
    /**     
    Setter for Employee num
    */
    public void setEmpNum(int id){
        empId=id;
    }
     /**     
    Setter for Employee password
    */
    public void setPass(String password){
        this.password=password;
    }
    /**     
    Getter for Employee password
    */
    public String getPass(){
        return password;
    }
    
}
