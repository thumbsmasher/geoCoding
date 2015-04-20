import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AddressValidator {

	/* *
	 * The Yahoo!Geocode API has been deprecated. Replace this service call w/ the Google Geocoding API
	 * private final String _Uri = "http://where.yahooapis.com/geocode?appid=" + _AppKey + "&location=";
	 * private final String _outputType = "xml"; 			// Available options: csv, xml, kml, json
     */
	
	/* GOOGLE Geocoding API - https://developers.google.com/maps/documentation/geocoding/  	 */
	private final String _UriGoogle = "http://maps.googleapis.com/maps/api/geocode/xml?address=";
	
    private static final boolean DEBUG = true;
    
    public String ErrorCode;
    public String ErrorMessage;
    public String Locale;
    public String matchQuality;
    public String matchesFound;
    public ArrayList<HashMap> matchLocations;
    
	public AddressValidator() {
		// Default constructor
		if (DEBUG) {System.out.println("Initializing AddressValidator");}
		matchLocations = new ArrayList<HashMap>();
	}
	
	public int validateAddressYahoo(String addressLine1, String addressLine2, String city, String stateCode, String countryCode) {
        int IsValidAddress = 1;
		try {
        	URI uri = new URI(_UriGoogle + GetEncodedString(addressLine1 + " " + addressLine2 + " " + city  + " " + stateCode + " " + countryCode));
        	if (DEBUG) {System.out.println("URL: " + uri.toString());}
        	
        	System.setProperty("http.proxyHost", "proxy-na.idexcorpnet.com");
        	
        	DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance(); 
        	DocumentBuilder dBuilder = dFactory.newDocumentBuilder(); 

        	Document doc = dBuilder.parse(new URL(uri.toString()).openStream()); 
        	Element documentRoot = doc.getDocumentElement();
        	
        	ErrorCode = doc.getElementsByTagName("Error").item(0).getTextContent();
        	ErrorMessage = doc.getElementsByTagName("ErrorMessage").item(0).getTextContent();
        	Locale = doc.getElementsByTagName("Locale").item(0).getTextContent();
        	matchQuality = doc.getElementsByTagName("Quality").item(0).getTextContent();
        	matchesFound = doc.getElementsByTagName("Found").item(0).getTextContent();
        	
        	for(int locations = 0;locations<Integer.parseInt(matchesFound);locations++) {
        		NodeList nodes = documentRoot.getChildNodes(); 
            	nodes = doc.getElementsByTagName("Result").item(locations).getChildNodes();
            	HashMap<String, String> locationProperties = new HashMap<String, String>();
            	
            	for(int index = 0; index < nodes.getLength(); index++){ 
            	    //System.out.println(nodes.item(index).getNodeName() + " = " + nodes.item(index).getTextContent()); 
            	    locationProperties.put(nodes.item(index).getNodeName(), nodes.item(index).getTextContent());
            	}
            	matchLocations.add(locationProperties);
            	//locationProperties = null;
        	}
        } catch (Exception ex) {
        	System.out.println("ERROR - AddressValidator - validateAddress: " + ex.getMessage());
        	IsValidAddress = 0;
        }
        return IsValidAddress;
	}
	
	public int validateAddressGoogle(String addressLine1, String addressLine2, String city, String stateCode, String countryCode) {
		if (DEBUG) {System.out.println("Starting validateAddressGoogle");}
		
		int IsValidAddress = 1;
		try {
        	URI uri = new URI(_UriGoogle + GetEncodedString(addressLine1 + " " + addressLine2 + " " + city  + " " + stateCode + " " + countryCode));
        	if (DEBUG) {System.out.println("URL: " + uri.toString());}
        	
        	System.setProperty("http.proxyHost", "proxy-na.idexcorpnet.com");
        	
        	DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance(); 
        	DocumentBuilder dBuilder = dFactory.newDocumentBuilder(); 

        	Document doc = dBuilder.parse(new URL(uri.toString()).openStream()); 
        	Element documentRoot = doc.getDocumentElement();
        	
        	System.out.println("==> STATUS: " + doc.getElementsByTagName("status").item(0).getTextContent());
        	
        	ErrorCode = doc.getElementsByTagName("status").item(0).getTextContent();
        	ErrorMessage = doc.getElementsByTagName("ErrorMessage").item(0).getTextContent();
        	Locale = doc.getElementsByTagName("Locale").item(0).getTextContent();
        	matchQuality = doc.getElementsByTagName("Quality").item(0).getTextContent();
        	matchesFound = doc.getElementsByTagName("Found").item(0).getTextContent();
        	
        	for(int locations = 0;locations<Integer.parseInt(matchesFound);locations++) {
        		NodeList nodes = documentRoot.getChildNodes(); 
            	nodes = doc.getElementsByTagName("Result").item(locations).getChildNodes();
            	HashMap<String, String> locationProperties = new HashMap<String, String>();
            	
            	for(int index = 0; index < nodes.getLength(); index++){ 
            	    //System.out.println(nodes.item(index).getNodeName() + " = " + nodes.item(index).getTextContent()); 
            	    locationProperties.put(nodes.item(index).getNodeName(), nodes.item(index).getTextContent());
            	}
            	matchLocations.add(locationProperties);
            	//locationProperties = null;
        	}
        } catch (Exception ex) {
        	System.out.println("ERROR - AddressValidator - validateAddress: " + ex.getMessage());
        	IsValidAddress = 0;
        }
        return IsValidAddress;
	}
	
	private String GetEncodedString(String address) throws URISyntaxException {
    	URI myURI = null;
    	
    	if (DEBUG) {System.out.println("Input String: " + address);}
    	
    	try {
    		myURI = new URI(URLEncoder.encode(address,"UTF-8"));
    	} catch (Exception ex) {
    		System.out.println("ERROR - AddressValidator - GetEncodedString: " + ex.getMessage());
    	}
    	if (DEBUG) {System.out.println("GetGeocodeUri (encoded): " + myURI);}
    	return myURI.toString();
    }


}
