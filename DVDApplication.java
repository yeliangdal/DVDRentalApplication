/* 
   DVD Rental Store Application
   Randy Herritt, Ye Liang
   CSCI 2141
   Dalhousie University
   29 Oct 2014
   
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;
import java.text.*; 
import com.jcraft.jsch.JSch;  
import com.jcraft.jsch.Session;  
import java.util.Scanner;
import java.io.*;

public class DVDApplication extends JFrame
{
   private JButton searchButton;
   private JButton rentMovie;
   private JButton returnMovie;
   private JButton customerInfo;
   private JButton createCustomer;
   private JButton updateCustomer;
   
   private JButton searchDVD_title;
   private JButton searchDVD_genre;
   private JButton searchDVD_year;
   
   private JPanel outputPanel;
   private JTable table;
   
   private JScrollPane scrollPane;
   private static Connection con;
   
   private Object[][] data;
   private String[] columnNames;
   
   public DVDApplication()
   {
      //user interface
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new BorderLayout());
      setSize(1100, 600);
      //setResizable(false);
      
      //titlebar
      ImageIcon titleBarImage = new ImageIcon("Assets/titleBar.png");
      JButton titleBar = new JButton();
      titleBar.setIcon(titleBarImage);
      titleBar.setDisabledIcon(titleBarImage);
      titleBar.setEnabled(false);
      titleBar.setBorder(null);
      add(titleBar, BorderLayout.NORTH);
      
      //menu Buttons
      JPanel menuPanel = new JPanel();
      menuPanel.setLayout(new GridLayout(6,1));  
      
      searchButton = new JButton("Search DVDs");
      rentMovie = new JButton("Rent");
      returnMovie = new JButton("Returns");
      customerInfo = new JButton("Customer Info");
      createCustomer = new JButton("Create Customer");
      updateCustomer = new JButton("Update Customer");
      
      searchButton.addActionListener(new selectButton());
      rentMovie.addActionListener(new selectButton());
      returnMovie.addActionListener(new selectButton());
      customerInfo.addActionListener(new selectButton());
      createCustomer.addActionListener(new selectButton());
      updateCustomer.addActionListener(new selectButton());
      
      menuPanel.add(rentMovie);
      menuPanel.add(returnMovie);
      menuPanel.add(searchButton);
      menuPanel.add(customerInfo);
      menuPanel.add(createCustomer);
      menuPanel.add(updateCustomer);
      
      add(menuPanel, BorderLayout.WEST);   
      outputPanel = new JPanel(new GridLayout(1,1));
      add(outputPanel, BorderLayout.CENTER);
   
      setVisible(true);
      setAlwaysOnTop(false);
   }
   
   
   /*
    *  Rent a DVD
    * If applicaple, do:
    * 1. add a record in table rent_record, returnDate is set to null by default
    * 2. update the inStock status to false in dvd
    */
   public void rentMovie() 
   {
      
       //prepare the output panel
      outputPanel.removeAll();
      outputPanel.setLayout(new GridLayout(1,1));
      validate();
      repaint();
      
      //get the max rentID number to determine the rentID number
      int rentID = 1; 
      try 
      {
         Statement stmt = con.createStatement();
         String SQL = "SELECT MAX(recordID) AS max from rent_record;";
         ResultSet rs = stmt.executeQuery( SQL );
         rs.next();
         rentID = rs.getInt("max");
         rentID++;
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
      
      // user input interface       
      JPanel inputPanel = new JPanel();
      inputPanel.setLayout(new GridLayout(6,2));
      
      JTextField dvdTitle = new JTextField(30);
      JTextField dvdYear = new JTextField(30);
      
      JTextField customerID = new JTextField(30);
      JTextField rentDate = new JTextField(10);
      
      String[] genres = { "Action", "Animated", "Classic", "Comedy", "Drama", "Horror", "Musical", "Science Fiction", "Suspense" };
      
      JComboBox dvdGenre = new JComboBox(genres);
      
      inputPanel.add(new JLabel("DVD title:"));
      inputPanel.add(dvdTitle);
      
      inputPanel.add(new JLabel("DVD year:"));
      inputPanel.add(dvdYear);
      
      inputPanel.add(new JLabel("DVD genre:"));
      inputPanel.add(dvdGenre);
      
      inputPanel.add(new JLabel("Customer ID:"));
      inputPanel.add(customerID);
      
      inputPanel.add(new JLabel("Date(YYYYMMDD):"));
      inputPanel.add(rentDate);
      
      boolean dvdValid = false;
      boolean customerValid=false;
      String dTitle="",  dGenre= "", dateInString="";
      int dYear = 0, dID = 0, cID = 0; 
      int result = JOptionPane.showConfirmDialog(outputPanel, inputPanel, 
                     "Enter rental information: ", JOptionPane.OK_CANCEL_OPTION);
      
      boolean valid = false;
      
      //read in customerid, dvdid and rentdate 
      if (result== JOptionPane.OK_OPTION)
      {
         try
         {
            dTitle = dvdTitle.getText();
            dYear = Integer.parseInt(dvdYear.getText());
            dGenre = (String)dvdGenre.getSelectedItem();
            cID = Integer.parseInt(customerID.getText());
            dateInString = rentDate.getText();
         }
         catch (NumberFormatException e) 
         {
            JOptionPane.showMessageDialog(null, "Error, invalid input. Please try again.");
         }  
         if(!dTitle.equals("") && dYear>0 && !dGenre.equals("") && cID>0 && !dateInString.equals(""))
            valid = true;
      }
      
      boolean checkDVDID= false;
      boolean checkCID = false;
      boolean checkDate = false;
      
      //check whether dvdid and customerid are valid
      if(valid)
      {
         try 
         {
            Statement stmt = con.createStatement();
            String SQL = "SELECT COUNT(*) AS  countD, dvdID AS ID from inventory i, movies m WHERE i.dvdID = m.movieID and m.title= '"+dTitle + "' and m.year ="+dYear +" and m.genre = '"+dGenre+"' and i.inStock=true;";
            ResultSet rs = stmt.executeQuery( SQL );
            rs.next();
            dID= rs.getInt("ID");
            checkDVDID = (rs.getInt("countD")>0);
            
            System.out.println(dID+" "+checkDVDID );
            
            SQL = "SELECT count(*) AS custID from customer WHERE customer_id="+cID+";";
            rs = stmt.executeQuery( SQL );
            rs.next();
            checkCID = (rs.getInt("custID")==1);
            System.out.println(checkCID);
            
         }
         catch (SQLException e) 
         {
            e.printStackTrace();
         }
      }
      
      
      
      //check if date is valid
      SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
      ft.setLenient(false);
      Date t; 
   
      if (valid)
      {
         try 
         { 
            t = ft.parse(dateInString); 
            checkDate = true;  
         } 
         catch (ParseException e) 
         { 
            JOptionPane.showMessageDialog(null, "Error, invalid time format.");
            checkDate = false; 
         }
      }
      
      System.out.println(checkDate);
      
      
      
      //when dvdid, customerid and date are all valid,  
      //create a record in table rent_record, update the dvd inStock information in table dvd
      if(checkDVDID && checkCID && checkDate)  
      {
         try 
         {
            Statement stmt = con.createStatement();
            String SQL = "INSERT INTO rent_record values("+rentID +"," + dID +"," + cID + ", '" +dateInString+"',null);";
            stmt.executeUpdate(SQL);
         }
         catch (SQLException e) 
         {
            System.out.println(e.getMessage());
         }
         
         try 
         {
            Statement stmt = con.createStatement();
            String SQL = "UPDATE inventory set inStock=FALSE where dvdID="+dID+";";
            stmt.executeUpdate(SQL);
            JOptionPane.showMessageDialog(null, "Transaction done.");
         }
         catch (SQLException e) 
         {
            System.out.println(e.getMessage());
         }
      }
      if(!checkDVDID&&valid)
      {
         JOptionPane.showMessageDialog(null, "Error, DVD not in stock. ");
      }
      if(!checkCID&&valid)
      {
         JOptionPane.showMessageDialog(null, "Customer not in database.");
      }
   
   }
   
   /*
    *  Return a DVD
    * If applicaple, do:
    * 1. Update table rent_record: set returnDate 
    * 2. Update table dvd: set inStock to true
    */
   public void returnMovie() 
   {
    //prepare the output panel
      outputPanel.removeAll();
      outputPanel.setLayout(new GridLayout(1,1));
      validate();
      repaint();
      
      // user input interface       
      JPanel inputPanel = new JPanel();
      inputPanel.setLayout(new GridLayout(4,2));
      
      JTextField dvdID = new JTextField(30);
      JTextField returnDate = new JTextField(30);
      
      inputPanel.add(new JLabel("DVD ID:"));
      inputPanel.add(dvdID);
      
      inputPanel.add(new JLabel("Return date(YYYYMMDD):"));
      inputPanel.add(returnDate);
      
      boolean dvdValid = false;
      boolean dateValid=false;
      String dateInString="";
      int dID = 0;
      int result = JOptionPane.showConfirmDialog(outputPanel, inputPanel, 
                     "Enter return information: ", JOptionPane.OK_CANCEL_OPTION);
      boolean valid = false;
      
      //read in dvdID, and returnDate 
      if (result== JOptionPane.OK_OPTION)
      {
         try
         {
            dID = Integer.parseInt(dvdID.getText());
            dateInString = returnDate.getText();
            if(!dateInString.equals("") && dID>0)
               valid = true;
            else
               JOptionPane.showMessageDialog(null, "Error, invalid input. Please try again.");
         }
         catch (NumberFormatException e) 
         {
            JOptionPane.showMessageDialog(null, "Error, invalid input. Please try again.");
         }  
      }
      System.out.println(valid);
      
      boolean checkDVDID= false;
      boolean checkDate = false;
      Date rentDate = null;
      
      //check whether dvdid is valid
      if(valid)
      {
         try 
         {
            Statement stmt = con.createStatement();
            String SQL = "SELECT COUNT(*) AS count, rentDate as rentDate from rent_record WHERE returnDate is null and dvdID="+dID;
            ResultSet rs = stmt.executeQuery( SQL );
            rs.next();
            rentDate=rs.getDate("rentDate");
            checkDVDID = (rs.getInt("count")==1);
         }
         catch (SQLException e) 
         {
            e.printStackTrace();
         }
      }
      
      System.out.println("rent date: "+ rentDate+" Checkdvdid "+checkDVDID);
       
      //check whether the format of returndate is valid
      SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
      ft.setLenient(false);
      Date retDate=null; 
   
      if(valid)
      {
         try 
         { 
            retDate = ft.parse(dateInString); 
            checkDate = true;  
         } 
         catch (ParseException e) 
         { 
            JOptionPane.showMessageDialog(null, "Error, invalid time format.");
            checkDate = false; 
         }
      }
      System.out.println("return date: "+ retDate+" Checkdate "+checkDate);
      
      //check if return date is after rentDate
      if(checkDate&&rentDate!=null)
      {
         if(retDate.compareTo(rentDate)<0)
         {
            checkDate = false;
            JOptionPane.showMessageDialog(null, "Error, return date cannot be before rental date.");
         }
      }
      System.out.println(" Checkdate "+checkDate);
      
      
      //when dvdid, and date are both valid,  
      //update table rent_record and dvd
      if(checkDVDID && checkDate)  
      {
         boolean check1=true;
         try 
         {
            Statement stmt = con.createStatement();
            String SQL = "UPDATE rent_record SET returnDate='"+dateInString+"' WHERE dvdID="+dID+";";
            stmt.executeUpdate(SQL);
         }
         catch (SQLException e) 
         {
            System.out.println(e.getMessage());
            check1=false;
         }
         
         try 
         {
            Statement stmt = con.createStatement();
            String SQL = "UPDATE inventory set inStock=true where dvdID="+dID+";";
            stmt.executeUpdate(SQL);
            
         }
         catch (SQLException e) 
         {
            System.out.println(e.getMessage());
            check1=false;
         }
         if(check1)
            JOptionPane.showMessageDialog(null, "DVD ID "+dID+" returned.");
            
      }
      if(!checkDVDID&&valid)
      {
         JOptionPane.showMessageDialog(null, "Error, wrong dvd ID. ");
      }  
   }
   
   public void searchDVD_title()
   {
      JTextField firstName = new JTextField(30);
      JPanel inputPanel = new JPanel();
      inputPanel.add(firstName);
      String text = "";
   
      int result;
      result = JOptionPane.showConfirmDialog(outputPanel, inputPanel, 
                     "Enter in Title", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION)     
         text = firstName.getText();          
      
      searchMovie("SELECT m.title,m.year,m.genre, count(*) as quantity FROM inventory i, movies m where i.movieID = m.movieID and m.title like '%" + text + "%' group by m.title");
   }
   
   public void searchDVD_genre()
   {
      
      JTextField firstName = new JTextField(30);
      JPanel inputPanel = new JPanel();
      inputPanel.add(firstName);
   
      int result;
      
      String[] genres = { "Action", "Animated", "Classic", "Comedy", "Drama", "Horror", "Musical", "Science Fiction", "Suspense" };
      
      String text = (String) JOptionPane.showInputDialog(null, 
         "Select Genre",
         "Genre",
         JOptionPane.QUESTION_MESSAGE, 
         null, 
         genres, 
         genres[0]);
   
      
      repaint();
      validate();
      
      searchMovie("SELECT m.title,m.year,m.genre, count(*) as quantity FROM inventory i, movies m where i.movieID = m.movieID and m.genre like '%" + text + "%' group by m.title");
   }

   
   public void searchDVD_year()
   {
      JTextField firstName = new JTextField(30);
      JPanel inputPanel = new JPanel();
      inputPanel.add(firstName);
      String text = "";
   
      int result;
      result = JOptionPane.showConfirmDialog(outputPanel, inputPanel, 
                     "Enter in Year", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION)     
         text = firstName.getText();          
      
      searchMovie("SELECT m.title,m.year,m.genre,count(*) as quantity FROM inventory i, movies m where i.movieID = m.movieID and m.year like '%" + text + "%' group by m.title");
   
   }
   
   public void searchMovie(String query) {
      //prepare the output panel
      outputPanel.removeAll();
      outputPanel.setLayout(new BorderLayout());
      
      //add the search button
      searchDVD_title = new JButton("Search by Title");
      searchDVD_genre = new JButton("Search by Genre");
      searchDVD_year = new JButton("Search by Year");
      
      //action listeners
      searchDVD_title.addActionListener(new selectButton());
      searchDVD_year.addActionListener(new selectButton());
      searchDVD_genre.addActionListener(new selectButton());
      
      
      JPanel smenuPanel = new JPanel(new GridLayout(1,3));/*********modified name of menuPanel****************/
      smenuPanel.add(searchDVD_title);
      smenuPanel.add(searchDVD_genre);
      smenuPanel.add(searchDVD_year);
      
      outputPanel.add(smenuPanel, BorderLayout.NORTH);
      
      //get the count of movies to determine size of table
      
      int count = 0;
      try {
         Statement stmt = con.createStatement();
         String SQL = "";
         
         if (query == null)
            SQL = "Select COUNT(distinct movieID) as quantity FROM inventory";
         else
            SQL = "SELECT COUNT(title) as quantity FROM (" + query + ") as derived";   
            
         ResultSet rs = stmt.executeQuery(SQL);
         rs.next();
         count = rs.getInt("quantity");    
         
      }
      catch (SQLException e) {
         e.printStackTrace();
      }  
      
      //just in case
      if (count == 0) count = 10;
   
      data = new Object[count][4];
      try {
         Statement stmt = con.createStatement();
         
         String SQL = "";
         if (query == null)
            SQL = "SELECT m.title,m.year,m.genre,count(*) as quantity FROM inventory i, movies m WHERE i.movieID = m.movieID and i.inStock=true group by m.title";
         else
            SQL = query;
        
         ResultSet rs = stmt.executeQuery( SQL );
         
         int counter = 0;
      
         while (rs.next()) {
            String title = rs.getString("title");
            int year = rs.getInt("year");
            String genre = rs.getString("genre");
            int quantity = rs.getInt("quantity");
            
            data[counter][0] = title;    
            data[counter][1] = year;
            data[counter][2] = genre;
            data[counter][3] = quantity;
         
            counter++;       
         }   
      } 
      catch (SQLException e) {
         e.printStackTrace();
      }
   
      columnNames = new String[4];
      columnNames[0] = "Title";
      columnNames[1] = "Year";
      columnNames[2] = "Genre";
      columnNames[3] = "Quantity";
                        
      table = new JTable();
      table.setGridColor(Color.BLACK);
      MyTableModel myModel = new MyTableModel();
      table.setModel(myModel);
      table.setRowSorter(new TableRowSorter(myModel));
            
   	// Add the table to a scrolling pane
      scrollPane = new JScrollPane( table );
      outputPanel.add(scrollPane);
      
      validate();
      repaint();
      
   }

   public void customerInfo() throws SQLException {
      //prepare the output panel
      outputPanel.removeAll();
      outputPanel.setLayout(new GridLayout(1,1));
   
      //get the count of customers to determine size of table
      int count = 0; 
      try {
         Statement stmt = con.createStatement();
         String SQL = "SELECT COUNT(*) as count FROM customer";
         ResultSet rs = stmt.executeQuery( SQL );
         rs.next();
         count = rs.getInt("count");
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
      
      //just in case
      if (count == 0) count = 10;
   
      //run a query
      data = new Object[count][8];
      try {
         Statement stmt = con.createStatement();
         String SQL = "SELECT * FROM customer";
         ResultSet rs = stmt.executeQuery( SQL );
         
         int counter = 0;
      
         while (rs.next()) {
            int customer_id = rs.getInt("customer_id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            int street_number = rs.getInt("street_number");
            String street_name = rs.getString("street_name");
            String city = rs.getString("city");
            int area_code= rs.getInt("area_code");
            int telephone = rs.getInt("telephone_number");
            
            Customer newCustomer = new Customer(customer_id, 
                                                first_name, 
                                                last_name,
                                                street_number,
                                                street_name,
                                                city,
                                                area_code, 
                                                telephone);
            
            data[counter][0] = newCustomer.getCustomerID();    
            data[counter][1] = newCustomer.getFirstName();
            data[counter][2] = newCustomer.getLastName();
            data[counter][3] = newCustomer.getStreetNumber();
            data[counter][4] = newCustomer.getStreetName();
            data[counter][5] = newCustomer.getCity();
            data[counter][6] = newCustomer.getAreaCode();   
            data[counter][7] = newCustomer.getTelephone();
         
            counter++;       
         }   
      } 
      catch (SQLException e) {
         e.printStackTrace();
      }
   
      columnNames = new String[8];
      columnNames[0] = "ID";
      columnNames[1] = "First Name";
      columnNames[2] = "Last Name";
      columnNames[3] = "Street Number";
      columnNames[4] = "Street";
      columnNames[5] = "City";
      columnNames[6] = "Area Code";
      columnNames[7] = "Phone Number";
                        
      table = new JTable();
      table.setGridColor(Color.BLACK);
      MyTableModel myModel = new MyTableModel();
      table.setModel(myModel);
      table.setRowSorter(new TableRowSorter(myModel));
            
   	// Add the table to a scrolling pane
      scrollPane = new JScrollPane( table );
      outputPanel.add(scrollPane);
   
      validate();
   
   }
   
   
   public void createCustomer() throws SQLException {
      System.out.println("CREATE CUSTOMER");
       //prepare the output panel
      outputPanel.removeAll();
      outputPanel.setLayout(new GridLayout(1,1));
      validate();
      repaint();
   
      //get the max customer number to determine the new customer number
      int customerID = 1; 
      try {
         Statement stmt = con.createStatement();
         String SQL = "SELECT MAX(customer_id) AS max from customer;";
         ResultSet rs = stmt.executeQuery( SQL );
         rs.next();
         customerID = rs.getInt("max");
         customerID++;
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
      System.out.println(customerID);
      
      JPanel inputPanel = new JPanel();
      inputPanel.setLayout(new GridLayout(8,2));
      
      JTextField firstName = new JTextField(30);
      JTextField lastName = new JTextField(30);
      JTextField streetNumber = new JTextField(10);
      JTextField streetName = new JTextField(30);
      JTextField cityName = new JTextField(30);
      JTextField areaCode = new JTextField(30); 
      JTextField phoneNum = new JTextField(30);
      
      inputPanel.add(new JLabel("First Name:"));
      inputPanel.add(firstName);
      
      inputPanel.add(new JLabel("Last Name:"));
      inputPanel.add(lastName);
      
      inputPanel.add(new JLabel("Street Number:"));
      inputPanel.add(streetNumber);
      
      inputPanel.add(new JLabel("Street:"));
      inputPanel.add(streetName);
      
      inputPanel.add(new JLabel("City:"));
      inputPanel.add(cityName);
      
      inputPanel.add(new JLabel("Area Code:"));
      inputPanel.add(areaCode);
      
      inputPanel.add(new JLabel("Phone Number:"));
      inputPanel.add(phoneNum);
      
      boolean valid = false;
      String first_name = "";
      String last_name = "";
      int street_number = 0;
      String street_name = "";
      String city = "";
      int area_code = 0;
      int telephone = 0;
      int result = 0;
      while (!valid && result != 2 && result != -1)
      {
         try {
            result = JOptionPane.showConfirmDialog(outputPanel, inputPanel, 
                     "Enter in customer information", JOptionPane.OK_CANCEL_OPTION);
            System.out.println("result: " + result);         
         
            if (result == JOptionPane.OK_OPTION) {      
               first_name = firstName.getText();
               last_name = lastName.getText();
               if (!streetNumber.getText().equals(""))
                  street_number = Integer.parseInt(streetNumber.getText()); 
               street_name = streetName.getText();
               city = cityName.getText();
               if (!areaCode.getText().equals(""))
                  area_code = Integer.parseInt(areaCode.getText());
               if (!phoneNum.getText().equals(""))  
                  telephone = Integer.parseInt(phoneNum.getText());
                  
               if (area_code > 0 && telephone > 0 && street_number > 0
                  && first_name.length() > 0 && last_name.length() > 0
                  && street_name.length()> 0 && city.length() > 0
               )
                  valid = true;
            }
         }
         catch (NumberFormatException e) {
            JOptionPane.showConfirmDialog(null, "Error, not a number. Please try again.");
         }   
      }   
      if (valid && result != 2 && result !=-1) //cancelled
      {
         //check if there is a customer with that name already
         int check = 0;
         try {
            Statement stmt = con.createStatement();
            String SQL = "SELECT COUNT(*) as count from customer WHERE first_name like '" + first_name +"' and last_name like '" + last_name + "';";
            ResultSet rs = stmt.executeQuery( SQL );
            rs.next();
            check = rs.getInt("count");
            if (check > 0) {
               //give error dialog box
               JOptionPane.showMessageDialog(null, "Error. Customer already exists.");
                          
            }
            else {         
               //create customer
               boolean customerInputed = false;
               try {
                  stmt = con.createStatement();
                  SQL = "INSERT INTO customer values(" +
                        customerID + "," + "'" +
                        first_name + "','" +
                        last_name + "'," +
                        street_number + ",'" +
                        street_name + "','" +
                        city + "'," +
                        area_code + "," +
                        telephone + ");";
                  stmt.executeUpdate(SQL);
                  customerInputed = true;
               }
               catch (SQLException err) {
                  System.out.println(err.getMessage());
                  customerInputed = false;
               }
               
               if (customerInputed)
               {
                  //give dialog saying everything is ok
                  JOptionPane.showMessageDialog(null, "Customer inputed with ID number: " + customerID);
               }
               else 
               {
                  //let user know an error occured
                  JOptionPane.showMessageDialog(null, "An Error occured trying to update database!");
               }
            }
         }
         catch (SQLException err) {
            System.out.println(err.getMessage());
         }     
      }
   }
   
   public void updateCustomer() {
      System.out.println("UPDATE CUSTOMER");
      
      //prepare the output panel
      outputPanel.removeAll();
      outputPanel.setLayout(new GridLayout(1,1));
      validate();
      repaint();
      
      //input interface
      //ask user to input customer ID   
      JPanel inputPanel = new JPanel();
      inputPanel.setLayout(new GridLayout(3,2));
      
      JTextField customerID = new JTextField(30);
      inputPanel.add(new JLabel("Customer ID:"));
      inputPanel.add(customerID);
      
      int cID = 0;
      boolean valid = false;
      int result = JOptionPane.showConfirmDialog(outputPanel, inputPanel, 
                     "Enter Customer ID that needs to be updated: ", JOptionPane.OK_CANCEL_OPTION);
      
      //check whether input is valid, i.e. customer id should be an integer larger than 0               
      if (result== JOptionPane.OK_OPTION)
      {
         try
         {
            cID = Integer.parseInt(customerID.getText());
            if(cID>0)
               valid = true;
            else
               JOptionPane.showMessageDialog(null, "Error, invalid input. Please try again.");
         }
         catch (NumberFormatException e) 
         {
            JOptionPane.showMessageDialog(null, "Error, invalid input. Please try again.");
         }  
      }
      System.out.println("valid: "+valid+" cID: "+cID);
      
      //if user input is valid, check whether the customer id is in the database
      boolean checkCID=false;
      Statement stmt= null;
      ResultSet rs= null;
      
      if(valid)
      {
         try 
         {
            stmt = con.createStatement();
            String SQL = "SELECT COUNT(*) AS count from customer WHERE customer_id="+cID;
            rs = stmt.executeQuery(SQL);
            rs.next();
            checkCID = (rs.getInt("count")==1);
         }
         catch (SQLException e) 
         {
            e.printStackTrace();
         }
      }
      System.out.println("checkCID: "+checkCID);
      
      //if cID is not in database, quite the method
      if(!checkCID&&valid)
      {
         JOptionPane.showMessageDialog(null, "Error, customer ID "+cID+" does not exist.");
         return;
      }
      
      //if CID is in database, prepare input dialog to enter customer information  
      if(checkCID)
      { 
         JPanel inputPanel2 = new JPanel();
         inputPanel2.setLayout(new GridLayout(8,2));
      
         String SQL = "SELECT * FROM customer WHERE customer_id="+cID;
         try {
            rs = stmt.executeQuery( SQL );
         
            int counter = 0;
            rs.next();
         
            int customer_id = rs.getInt("customer_id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            int street_number = rs.getInt("street_number");
            String street_name = rs.getString("street_name");
            String city = rs.getString("city");
            int area_code= rs.getInt("area_code");
            int telephone = rs.getInt("telephone_number");
         
            JTextField firstName = new JTextField(first_name, 30);
            JTextField lastName = new JTextField(last_name, 30);
            JTextField streetNumber = new JTextField("" +street_number, 30);
            JTextField streetName = new JTextField(street_name, 30);
            JTextField cityName = new JTextField(city, 30);
            JTextField areaCode = new JTextField("" + area_code, 30);
            JTextField telephoneNumber = new JTextField("" + telephone, 30);
         
            inputPanel2.add(new JLabel("Customer ID"));
            inputPanel2.add(new JLabel(Integer.toString(cID)));
            inputPanel2.add(new JLabel("First name:"));
            inputPanel2.add(firstName);
            inputPanel2.add(new JLabel("Last name:"));
            inputPanel2.add(lastName);
            inputPanel2.add(new JLabel("Street number:"));
            inputPanel2.add(streetNumber);
            inputPanel2.add(new JLabel("Street name:"));
            inputPanel2.add(streetName);
            inputPanel2.add(new JLabel("City:"));
            inputPanel2.add(cityName);
            inputPanel2.add(new JLabel("Area code:"));
            inputPanel2.add(areaCode);
            inputPanel2.add(new JLabel("Telephone:"));
            inputPanel2.add(telephoneNumber);
         
            result = JOptionPane.showConfirmDialog(outputPanel, inputPanel2, 
                        "Update Customer information", JOptionPane.OK_CANCEL_OPTION);
         
            boolean check=false;
         
         //read in customer info
            if (result == JOptionPane.OK_OPTION)
            {
               check=true;
               try
               {
                  first_name = firstName.getText();
                  last_name = lastName.getText();
                  if (!streetNumber.getText().equals(""))
                     street_number = Integer.parseInt(streetNumber.getText()); 
                  street_name = streetName.getText();
                  city = cityName.getText();
                  if (!areaCode.getText().equals(""))
                     area_code = Integer.parseInt(areaCode.getText());
                  if (!telephoneNumber.getText().equals(""))  
                     telephone = Integer.parseInt(telephoneNumber.getText());
               }
               catch (NumberFormatException e) 
               {
                  check=false;
                  JOptionPane.showConfirmDialog(null,"Only numbers are allowed in Street number, Area code and Telephone.","Error",JOptionPane.OK_CANCEL_OPTION);
               }
            }
         //execute sql to update database
            boolean updateDone=true;
            if(check&&checkCID)
            {
               try 
               {
                  stmt = con.createStatement();
                  SQL = "UPDATE customer SET first_name='"+ first_name + "', last_name='" +
                      last_name + "',street_number=" +street_number + ",street_name='" + street_name 
                      + "',city='" +city + "',area_code=" + area_code + ",telephone_number=" + telephone 
                      + " WHERE customer_id="+cID+";";
                  stmt.executeUpdate(SQL);
               }
               catch (SQLException err) 
               {
                  System.out.println(err.getMessage());
                  updateDone=false;
               }
            }
         
            if (updateDone&&check)
            {
               JOptionPane.showMessageDialog(null, "Customer ID " + cID+" updated.");
            }
            else if(!updateDone&&check)
            {
               JOptionPane.showMessageDialog(null, "An Error occured when trying to update database!");
            }
         
         
         }
         catch (SQLException e) 
         {
            e.printStackTrace();
         }
      }
   }
   
   /*
   public static void main(String[] args) throws SQLException
   {
      //connect to the database
      try {
         String host = "jdbc:mysql://localhost:3306/project";
         String usrName = "randy";
         String password = "Candace85";
         con = DriverManager.getConnection(host,usrName,password);    
      }
      catch ( SQLException err ) {
         System.out.println( err.getMessage( ) );
      }
      
      DVDApplication app = new DVDApplication();
      
   }
   */
   public static void main(String[] args) throws SQLException, IOException
   {
      //connect to the database
      int lport=4321;  
      String host="bluenose.cs.dal.ca";   
      String rhost="db.cs.dal.ca";  
     
      int rport=3306;
     
     //get info for login
      Scanner keyboard = new Scanner(System.in);
      String filename = "password1.txt";
      File file = new File(filename);
      Scanner inputFile = new Scanner(file, "UTF-8");
   
      String user = inputFile.nextLine();
      String password = inputFile.nextLine();
      String dbuserName = inputFile.nextLine();
      String dbpassword = inputFile.nextLine();
   
      inputFile.close();
        
        
      String url = "jdbc:mysql://localhost:4321/yliang";  
      String driverName="com.mysql.jdbc.Driver";  
      Session session= null;  
          
      try{  
            //Set StrictHostKeyChecking property to no to avoid UnknownHostKey issue  
         java.util.Properties config = new java.util.Properties();  
         config.put("StrictHostKeyChecking", "no");  
         JSch jsch = new JSch();  
         session=jsch.getSession(user, host, 22);  
         session.setPassword(password);  
         session.setConfig(config);  
         session.connect();  
         System.out.println("Connected");  
         int assinged_port=session.setPortForwardingL(lport, rhost, rport);  
         System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);  
         System.out.println("Port Forwarded");  
      
            //mysql database connectivity  
         Class.forName(driverName);  
         con  = DriverManager.getConnection (url, dbuserName, dbpassword);  
              
         System.out.println ("Database connection established");  
         System.out.println("DONE"); 
      }
      catch(Exception e){  
         System.out.println( e.getMessage( ) );
      }
      
      DVDApplication app = new DVDApplication();
      
   }  


   private class selectButton implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if (e.getSource() == rentMovie)
         {
            rentMovie();
         }
         if (e.getSource() == returnMovie)
         {
            returnMovie();
         }
         if (e.getSource() == searchButton)
         {
            searchMovie(null);
         }
         if (e.getSource() == customerInfo)
         {
            try {
               customerInfo();
            }
            catch (SQLException err) {
               System.out.println(err.getMessage());
            }
         }
         if (e.getSource() == createCustomer)
         {
            try {
               createCustomer();
            }
            catch (SQLException err) {
               System.out.println(err.getMessage());  
            }    
         }
         if (e.getSource() == updateCustomer)
         {
            updateCustomer();
         }
         if (e.getSource() == searchDVD_title)
         {
            searchDVD_title();
         }
         if (e.getSource() == searchDVD_year)
         {
            searchDVD_year();
         }
         if (e.getSource() == searchDVD_genre)
         {
            searchDVD_genre();
         }      
            
      }
   }
   
   public class MyTableModel extends DefaultTableModel {
      
      public MyTableModel()
      {
         super(data, columnNames);
      }
      
      public boolean isCellEditable(int row, int column) {
           //all cells false
         return false;
      }
   }  
   
   public class Customer {
   
      private int customer_id;
      private String first_name;
      private String last_name;
      private int street_number;
      private String street_name;
      private String city;
      private int area_code;
      private int telephone;
   
      public Customer(int customer_id, String first_name, String last_name, int street_number,
                   String street_name, String city, int area_code, int telephone)
      {
         this.customer_id = customer_id;
         this.first_name = first_name;
         this.last_name = last_name;
         this.street_number = street_number;
         this.street_name = street_name;
         this.city = city;
         this.area_code = area_code;
         this.telephone = telephone;                   
      }
   
   //setters
      public void setCustomerID(int customer_id) {
         this.customer_id = customer_id;
      }   
      public void setFirstName(String first_name) {
         this.first_name = first_name;
      }
      public void setLastName(String last_name) {
         this.last_name = last_name;
      } 
      public void setStreetNumber(int street_number) {
         this.street_number = street_number;
      }      
      public void setStreetName(String street_name) {
         this.street_name = street_name;
      }            
      public void setCity(String city) {      
         this.city = city;
      }
      public void setAreaCode(int area_code) {
         this.area_code = area_code;
      }
      public void setTelephone(int telephone) {
         this.telephone = telephone;
      }
   
   //getters
      public int getCustomerID() {
         return customer_id;
      }   
      public String getFirstName() {
         return first_name;
      }
      public String getLastName() {
         return last_name;
      } 
      public int getStreetNumber() {
         return street_number;
      } 
      public String getStreetName() {
         return street_name;
      }                 
      public String getCity() {      
         return city;
      }
      public int getAreaCode() {
         return area_code;
      }
      public int getTelephone() {
         return telephone;
      }    
   
   //toString
      public String toString() {
         return first_name + "\t" + last_name;
      }
   }                
}


   
   

