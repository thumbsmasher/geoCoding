import java.util.ArrayList;
import java.util.HashMap;


public class Address {

	private static final boolean DEBUG = false;
	
	private String AddressLine01;
	private String AddressLine02;
	private String City;
	private String PostalCode;
	private String StateCode;
	private String CountryCode;
	
	public int matchQuality;
	public int matchesFound;
	public int errorCode;
	public String errorMessage;
	public ArrayList<HashMap> matchLocations; 
	
	public Address(String myAddressLine01, String myAddressLine02, String myCity, String myStateCode, String myPostalCode, String myCountryCode) {
		// Default Constructor
		AddressLine01 = myAddressLine01;
		AddressLine02 = myAddressLine02;
		City = myCity;
		PostalCode = myPostalCode;
		StateCode = myStateCode;
		CountryCode = myCountryCode;
		matchQuality=0;
		matchesFound=0;
		errorCode=-1;
		errorMessage="";
		matchLocations = new ArrayList<HashMap>();
	}
	
	public int validateAddress() {
		int ErrorCode = 0;
		
		AddressValidator myAV = new AddressValidator();
		System.out.println("Run validateAddress");
		
		//if (myAV.validateAddressYahoo(AddressLine01, AddressLine02, City, StateCode, CountryCode)==1) {
		if (myAV.validateAddressGoogle(AddressLine01, AddressLine02, City, StateCode, CountryCode)==1) {
			try {
				matchQuality = Integer.parseInt(myAV.matchQuality);
				matchesFound = Integer.parseInt(myAV.matchesFound);
				errorCode = Integer.parseInt(myAV.ErrorCode);
				errorMessage = myAV.ErrorMessage;
				
				matchLocations = myAV.matchLocations;
			} catch (Exception ex) {
				System.out.println("ERROR parsing address validation : " + ex.getMessage());
				ErrorCode = -999;
			}
		}
		
		myAV = null;
		
		return ErrorCode;
	}

}
