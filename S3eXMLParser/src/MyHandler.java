import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


	public class MyHandler extends DefaultHandler {
		public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
			if (qName.equalsIgnoreCase("MCS")) {   System.out.println("MyHandler MCS: " + attributes.getValue("Nombre")); }
			if (qName.equalsIgnoreCase("CSE")) {   System.out.println("MyHandler CSE" + attributes.getValue("Nombre")); }
			if (qName.equalsIgnoreCase("ED")) {    System.out.println("MyHandler ED: " + attributes.getValue("Nombre")); }
			if (qName.equalsIgnoreCase("SD")) {    System.out.println("MyHandler SD: " + attributes.getValue("Nombre")); }
		}		
	}