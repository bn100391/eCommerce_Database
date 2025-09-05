import java.sql.*;
import java.util.*;


public class storeAccess {

    public static void addOrder(Connection c){                
        Scanner scan = new Scanner(System.in); 
        System.out.println("Please enter order I.D."); 
        int orderId = scan.nextInt(); 

        System.out.println("Please enter your Customer I.D."); 
        int custId = scan.nextInt(); 

        System.out.println("Please enter the date you want the order to be delivered"); 
        System.out.print("Year [YYYY]: ");
        String year = scan.next();
        System.out.print("Month [MM]: ");
        String month = scan.next();
        System.out.print("Day [DD]: "); 
        String day = scan.next(); 
 

        try{
            String query = "INSERT INTO [Order] (O_ID, Customer_ID, OrderDate) VALUES(?, ?, ?)"; 
            PreparedStatement p = c.prepareStatement(query); 
            p.setInt(1, orderId); 
            p.setInt(2, custId); 
            p.setString(3, year+month+day); 
  

            p.executeUpdate(); 

            }catch(SQLException e){
                e.printStackTrace(); 
            }
    }

    public static void updateDeliveryDate(Connection c){                
        Scanner scan = new Scanner(System.in); 
        System.out.println("Please enter order I.D."); 
        int orderId = scan.nextInt(); 
        System.out.println("Please enter new delivery date"); 
        
        System.out.print("Year [YYYY]: ");
        String year = scan.next();
        System.out.print("Month [MM]: ");
        String month = scan.next();
        System.out.print("Day [DD]: "); 
        String day = scan.next(); 


        try{
            String query = "UPDATE [Order] SET OrderDate = ? WHERE O_ID = ? "; 
            PreparedStatement p = c.prepareStatement(query); 
            p.setString(1, year+month+day);  
            p.setInt(2, orderId); 

            p.executeUpdate(); 
            }catch(SQLException e){
                e.printStackTrace(); 
            }
    }

    public static void deleteOrder(Connection c){                
        Scanner scan = new Scanner(System.in); 
        System.out.println("Please enter order I.D."); 
        int orderId = scan.nextInt(); 


        try{
            String query = "DELETE FROM [Order] WHERE O_ID = ? "; 
            PreparedStatement p = c.prepareStatement(query); 
            p.setInt(1, orderId); 

            p.executeUpdate(); 
            }catch(SQLException e){
                e.printStackTrace(); 
            }
    }

    public static void group1(Connection c){                
        try{
            Statement s = c.createStatement(); 
            String query = 
            "SELECT Product.\"ProductName \", Product.Price " + 
                "FROM Product " + 
                "ORDER BY Product.Price " + 
                "DESC LIMIT 1 " ; 

            ResultSet result = s.executeQuery(query);  
            System.out.printf("Item: %s\nPrice: $%.2f\n", result.getString(1), result.getDouble(2)); 


            }catch(SQLException e){
                e.printStackTrace(); 
            }
    }


    public static void group2(Connection c){                
        try{
            System.out.println("Please Enter Your Customer ID"); 
            Scanner scan = new Scanner(System.in); 
            int custId = scan.nextInt(); 
            System.out.println("Please Enter Price Cutoff, Round to nearest dollar"); 
            int priceCuttoff = scan.nextInt(); 

            String query = 
            "SELECT Product.\"ProductName \", Product.Price " +
                "FROM Customer JOIN 'Order' JOIN Purchases JOIN Product " +
                "ON Customer.Customer_ID = 'Order'.Customer_ID " + 
                "AND 'Order'.O_ID = Purchases.O_ID " + 
                "AND Purchases.Product_ID = Product.Product_ID " + 
                "WHERE Customer.Customer_ID = ? " +
                "AND Product.Price >= ? " + 
                "GROUP BY Product.Product_ID "; 
            
            PreparedStatement p = c.prepareStatement(query); 
            p.setInt(1, custId); 
            p.setInt(2, priceCuttoff); 
            
            ResultSet resultSet = p.executeQuery(); 

            ResultSetMetaData data = resultSet.getMetaData(); 
            int numColumns = data.getColumnCount(); 
            System.out.println(); 
            while(resultSet.next()){
                for(int i = 1; i< numColumns; i+=2){
                    System.out.printf("%s $%.2f\n", resultSet.getString(i), resultSet.getDouble(i+1)); 
                }
            }
            System.out.println(); 

            }catch(SQLException e){
                e.printStackTrace(); 
            }
    }

    public static void group3(Connection c){                
        try{
        
        Scanner scan = new Scanner(System.in); 
        System.out.println("Please enter the data for which you would like to see the total sales..."); 
        System.out.print("Year [YYYY]: ");
        String year = scan.next();
        System.out.print("Month [MM]: ");
        String month = scan.next();
        System.out.print("Day [DD]: "); 
        String day = scan.next(); 


            String query = 
            "SELECT SUM(Product.Price) AS Total_Sales\n" +
            "    FROM Purchases JOIN Product \n" +
            "    ON Purchases.Product_ID = Product.Product_ID\n" +
            "    WHERE O_ID = (" +
            "        SELECT O_ID " +
            "        FROM \"Order\"" +
            "        WHERE OrderDate = ?" +
            "        );"; 
            
            PreparedStatement p = c.prepareStatement(query);  
            p.setString(1, year+month+day);
            
            ResultSet resultSet = p.executeQuery();
            System.out.printf("Total Sales: $%.2f\n", resultSet.getDouble(1)); 

            }catch(SQLException e){
                e.printStackTrace(); 
            }
    }

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:store.db");
            System.out.println("_________________________________________________________________________________________________________"); 
            System.out.println("\nWELCOME! You are now connected to the online store. Type in your option's number followed by the ENTER key...\n\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("1.) ADD Order"); 
        System.out.println("2.) UPDATE Delivery Date"); 
        System.out.println("3.) REMOVE Order"); 
        System.out.println();  
        System.out.println("4.) Find Most Expensive Item"); 
        System.out.println("5.) A Customers Purchases Above A Price Threshold"); 
        System.out.println("6.) Total Sales On A Given Day");
        System.out.println(); 
        
        Scanner scan = new Scanner(System.in); 
        int option = scan.nextInt(); 
        System.out.println(); 

        switch (option){
            case 1: 
                addOrder(conn); 
                break; 
            case 2: 
                updateDeliveryDate(conn); 
                break; 
            case 3: 
                deleteOrder(conn); 
                break; 
            case 4: 
                group1(conn);  
                break; 
            case 5: 
                group2(conn); 
                break; 
            case 6: 
                group3(conn); 
                break; 
        }

        scan.close(); 

    }
}
