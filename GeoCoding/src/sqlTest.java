
public class sqlTest{
     private java.sql.Connection  con = null;
     private final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
     private final String url = "jdbc:sqlserver://";
     private String serverName= "SCIDXDWS07\\DWS07";
     private String portNumber = "1433";
     private String databaseName= "DWCR_811";
     private String userName = "DMirror";
     private String password = "ts_dm_53";
     // Informs the driver to use server a side-cursor, 
     // which permits more than one active statement 
     // on a connection.
     private final String selectMethod = "cursor"; 
     
     // Constructor
     public void Test(){}
     
     private String getConnectionUrl(){
          return url+serverName+":"+portNumber+";databaseName="+databaseName+";selectMethod="+selectMethod+";";
     }
     
     public java.sql.Connection getConnection(){
          try{
               Class.forName(driver); 
               con = java.sql.DriverManager.getConnection(getConnectionUrl(),userName,password);
               if(con!=null) System.out.println("Connection Successful!");
          }catch(Exception e){
               e.printStackTrace();
               System.out.println("Error Trace in getConnection() : " + e.getMessage());
         }
          return con;
      }

     /*
          Display the driver properties, database details 
     */ 

     public void displayDbProperties(){
          java.sql.DatabaseMetaData dm = null;
          java.sql.ResultSet rs = null;
          try{
               con= this.getConnection();
               if(con!=null){
                    dm = con.getMetaData();
                    System.out.println("Driver Information");
                    System.out.println("\tDriver Name: "+ dm.getDriverName());
                    System.out.println("\tDriver Version: "+ dm.getDriverVersion ());
                    System.out.println("\nDatabase Information ");
                    System.out.println("\tDatabase Name: "+ dm.getDatabaseProductName());
                    System.out.println("\tDatabase Version: "+ dm.getDatabaseProductVersion());
                    System.out.println("Avalilable Catalogs ");
                    rs = dm.getCatalogs();
                    while(rs.next()){
                         System.out.println("\tcatalog: "+ rs.getString(1));
                    } 
                    rs.close();
                    rs = null;
                    closeConnection();
               }else System.out.println("Error: No active Connection");
          }catch(Exception e){
               e.printStackTrace();
          }
          dm=null;
     }     
     
     public void closeConnection(){
          try{
               if(con!=null)
                    con.close();
               con=null;
          }catch(Exception e){
               e.printStackTrace();
          }
     }
     
     public void setServername(String value) {
    	 serverName = value;
     }
     public void setPortNumber(String value) {
    	 portNumber = value;
     }
     public void setDatabasename(String value) {
    	 databaseName = value;
     }
     public void setUser(String value) {
    	 userName = value;
     }
     public void setPwd(String value) {
    	 password = value;
     }
     
}