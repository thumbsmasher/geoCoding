import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class geocodingMain{
	protected static final PrintStream o = System.out;
	
     public static void main(String[] args) throws Exception
       {
          //sqlTest myDbTest = new sqlTest();
          //myDbTest.displayDbProperties();
          
          //connectionTest();
    	 
    	 //testAddressValidation(31774573);
    	 
    	 runAddressValidation();
    	 System.out.println("Completed");
            
       }
     
     public static void connectionTest()
     {
        o.println("Starting main");

        String url = null;
        String userid = null;
        String passwd = null;
        
        try
        {
          url = "jdbc:JdbcProgress:T:EPIS0000:6150:mfgsys";
          userid= "sysprogress";
          passwd= "sysprogress";

          // Load the driver
          Class.forName ("com.progress.sql.jdbc.JdbcProgressDriver");
          o.println("Driver loaded");
          
          // We have to add any other options as additional properties in the prop argument.
          // e.g., prop.put\("Caller", "ProgressTest"\);
          
          Connection con = DriverManager.getConnection(url, userid, passwd);
          o.println(con.isClosed());
          
          // If we were unable to connect, an exception
          // would have been thrown. So, if we get here,
          // we are successfully connected to the URL
          o.println("Connection established successfully");
  	
  		// Check for, and display and warnings generated
  		// by the connect.
  		//checkForWarning (con.getWarnings ());
  		o.println("\nConnected to " + url);
  		con.close();
  		o.println(con.isClosed());
        }
        catch (Exception ex) {
        	o.println("\nERROR " + ex.getMessage());
        }
     }
     private static void runAddressValidation() {
    	 Connection  con = null;
    	 ResultSet rs = null;
    	 
    	 sqlTest myAddressList = new sqlTest();
         
         myAddressList.setServername("dw.idexcorpnet.com");
         myAddressList.setDatabasename("IDEX_KPI");
         myAddressList.setUser("ODI_Admin");
         myAddressList.setPwd("Qwerty1");
         
         try {
             con = myAddressList.getConnection();
             
             if (con!=null) {
                 String myQuery_getAddressList = "SELECT top 30000 SourceID, Source, CompanyID, SourceAddressNumber, rtrim(SoldToName) as SoldToName, ";
                 myQuery_getAddressList += "rtrim(SourceAddressLine01) as SourceAddressLine01, rtrim(SourceAddressLine02) as SourceAddressLine02, ";
                 myQuery_getAddressList += "rtrim(SourcePostalCode) as SourcePostalCode, rtrim(SourceCity) as SourceCity, rtrim(SourceStateCode) as SourceStateCode, ";
                 myQuery_getAddressList += "rtrim(SourceCountryCode) as SourceCountryCode ";
                 myQuery_getAddressList += "FROM IDEX_KPI.dbo.MDM_CustomerAddressValidation WHERE (1=0 and ErrorCode=-1) or SourceAddressNumber=5652";
                 
                 Statement getAddressList = con.createStatement(); 
                 rs = getAddressList.executeQuery(myQuery_getAddressList);
            	 System.out.println("test::runAddressValidation");
            	 
                 while(rs.next()){
                 	Statement updateMasterAddress = con.createStatement();
                 	
                 	Address myAddress = new Address(rs.getString("SourceAddressLine01"), rs.getString("SourceAddressLine02"), rs.getString("SourceCity"), rs.getString("SourceStateCode"), rs.getString("SourcePostalCode"), rs.getString("SourceCountryCode"));
                 	myAddress.validateAddress();
                 	
                 	if (myAddress.matchesFound==1) {
                 		updateMasterAddress(con, myAddress, rs.getInt("SourceID"), rs.getString("CompanyID"), rs.getInt("SourceAddressNumber"));
                 		
                 	} else {
                 		System.out.println("Multiple matches found -> " + rs.getString("SourceAddressNumber") + " - " + rs.getString("SoldToName"));
                 		updateMasterAddress(con, myAddress, rs.getInt("SourceID"), rs.getString("CompanyID"), rs.getInt("SourceAddressNumber"));
                 		
                 	}
                 	
                 	updateMasterAddress = null;
                 } 
                 rs.close();
                 getAddressList = null;
                 rs = null;
                 
                 myAddressList.closeConnection();
             } else System.out.println("Error: No active Connection");
             
        } catch(Exception e) {
             e.printStackTrace();
        }

     }
     
     private static void testAddressValidation(int myAddressNumber) {
    	 Connection  con = null;
    	 ResultSet rs = null;
    	 
    	 sqlTest myAddressList = new sqlTest();
         
         myAddressList.setServername("dw.idexcorpnet.com");
         myAddressList.setDatabasename("IDEX_KPI");
         myAddressList.setUser("ODI_Admin");
         myAddressList.setPwd("Qwerty1");
         
         try {
             con = myAddressList.getConnection();
             
             if (con!=null) {
                 String myQuery_getAddressList = "SELECT SourceID, Source, CompanyID, SourceAddressNumber, rtrim(SoldToName) as SoldToName, ";
                 myQuery_getAddressList += "rtrim(SourceAddressLine01) as SourceAddressLine01, rtrim(SourceAddressLine02) as SourceAddressLine02, ";
                 myQuery_getAddressList += "rtrim(SourcePostalCode) as SourcePostalCode, rtrim(SourceCity) as SourceCity, rtrim(SourceStateCode) as SourceStateCode, ";
                 myQuery_getAddressList += "rtrim(SourceCountryCode) as SourceCountryCode ";
                 myQuery_getAddressList += "FROM IDEX_KPI.dbo.MDM_CustomerAddressValidation WHERE SourceAddressNumber="+myAddressNumber;
                 
                 Statement getAddressList = con.createStatement(); 
                 rs = getAddressList.executeQuery(myQuery_getAddressList);
            	 System.out.println("test::testAddressValidation");
            	 
                 while(rs.next()){
                 	Statement updateMasterAddress = con.createStatement();
                 	
                 	Address myAddress = new Address(rs.getString("SourceAddressLine01"), rs.getString("SourceAddressLine02"), rs.getString("SourceCity"), rs.getString("SourceStateCode"), rs.getString("SourcePostalCode"), rs.getString("SourceCountryCode"));
                 	myAddress.validateAddress();
                 	
                 	if (myAddress.matchesFound==1) {
                 		updateMasterAddress(con, myAddress, rs.getInt("SourceID"), rs.getString("CompanyID"), rs.getInt("SourceAddressNumber"));
                 		
                 	} else {
                 		System.out.println("Multiple matches found -> " + rs.getString("SourceAddressNumber") + " - " + rs.getString("SoldToName"));
                 		updateMasterAddress(con, myAddress, rs.getInt("SourceID"), rs.getString("CompanyID"), rs.getInt("SourceAddressNumber"));
                 		
                 	}
                 	
                 	updateMasterAddress = null;
                 } 
                 rs.close();
                 getAddressList = null;
                 rs = null;
                 
                 myAddressList.closeConnection();
             } else System.out.println("Error: No active Connection");
             
        } catch(Exception e) {
             e.printStackTrace();
        }

     }
     private static void updateMasterAddress(Connection con, Address myAddress, int mySourceID, String myCompanyID, int myAddressNumber) {
    	 int recordsAffected = 0;
    	 //System.out.println("Updating Master Address: " + myAddressNumber);
    	 
    	 String myQuery_updateMasterAddress = "UPDATE IDEX_KPI.dbo.MDM_CustomerAddressValidation ";
    	 myQuery_updateMasterAddress += "SET MasterAddressLine01=?, MasterAddressLine02=?, MasterCity=?, MasterStateCode=?, ";
    	 myQuery_updateMasterAddress +=     "MasterCounty=left(?, 25), MasterPostalCode=?, MasterPostalCode_zip4=?, MasterCountryCode=?, ";
    	 myQuery_updateMasterAddress +=     "Latitude=?, Longitude=?, MatchQuality=?, NumberOfMatches=?, ";
    	 myQuery_updateMasterAddress +=     "ErrorCode=?, ErrorDescription=?, MasterLastUpdated=GetDate() ";
    	 myQuery_updateMasterAddress += "WHERE SourceID=? AND CompanyID=? and SourceAddressNumber=?";
    	 
    	 try {
    		 PreparedStatement updateMasterAddress = con.prepareStatement(myQuery_updateMasterAddress);
    		 updateMasterAddress.setString(1, myAddress.matchLocations.get(0).get("line1").toString());			//AddressLine01
    		 updateMasterAddress.setString(2, myAddress.matchLocations.get(0).get("line2").toString());			//AddressLine02
    		 updateMasterAddress.setString(3, myAddress.matchLocations.get(0).get("city").toString());			//City
    		 updateMasterAddress.setString(4, myAddress.matchLocations.get(0).get("statecode").toString());		//StateCode
    		 updateMasterAddress.setString(5, myAddress.matchLocations.get(0).get("county").toString());		//County
    		 updateMasterAddress.setString(6, myAddress.matchLocations.get(0).get("uzip").toString());			//PostalCode
    		 updateMasterAddress.setString(7, myAddress.matchLocations.get(0).get("postal").toString());		//Zip+4
    		 updateMasterAddress.setString(8, myAddress.matchLocations.get(0).get("countrycode").toString());	//CountryCode
    		 
    		 updateMasterAddress.setDouble(9, Double.parseDouble(myAddress.matchLocations.get(0).get("latitude").toString()));		//Latitude
    		 updateMasterAddress.setDouble(10, Double.parseDouble(myAddress.matchLocations.get(0).get("longitude").toString()));		//Longitude
    		 updateMasterAddress.setInt(11, myAddress.matchQuality);	//MatchQuality
    		 updateMasterAddress.setInt(12, myAddress.matchesFound);	//Number of Matches
    		 
    		 //updateMasterAddress.setInt(13, -1);		// Use this flag to process the master address again
    		 updateMasterAddress.setInt(13, myAddress.errorCode);		
    		 updateMasterAddress.setString(14, myAddress.errorMessage);
    		 
    		 updateMasterAddress.setInt(15, mySourceID);
    		 updateMasterAddress.setString(16, myCompanyID);
    		 updateMasterAddress.setInt(17, myAddressNumber);
    		 
    		 recordsAffected = updateMasterAddress.executeUpdate();

    	 } catch (Exception ex) {
    		 System.out.println("ERROR (updateMasterAddress) : " + myAddressNumber + " : " + ex.getMessage());
    		 
    		 String myQuery_updateMasterAddressError = "UPDATE IDEX_KPI.dbo.MDM_CustomerAddressValidation ";
    		 myQuery_updateMasterAddressError += "SET ErrorCode=?, ErrorDescription=?, MasterLastUpdated=GetDate() ";
    		 myQuery_updateMasterAddressError += "WHERE SourceID=? AND CompanyID=? and SourceAddressNumber=?";
        	 
        	 try {
        		 PreparedStatement stmt_updateMasterAddressError = con.prepareStatement(myQuery_updateMasterAddressError);
        		 stmt_updateMasterAddressError.setInt(1, 999);							//ErrorCode
        		 stmt_updateMasterAddressError.setString(2, ex.getMessage());			//ErrorMessage
        		 
        		 stmt_updateMasterAddressError.setInt(3, mySourceID);
        		 stmt_updateMasterAddressError.setString(4, myCompanyID);
        		 stmt_updateMasterAddressError.setInt(5, myAddressNumber);
        		 
        		 recordsAffected = stmt_updateMasterAddressError.executeUpdate();
        		 System.out.println("ERROR (updateMasterAddress) : flagged record as error");
        		 
        	 } catch (Exception exError) {
        		 System.out.println("ERROR (updateMasterAddressError) : " + myAddressNumber + " : " + ex.getMessage());
        	 }
    	 }
     }
}