package model;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class Handler extends DefaultHandler {
    
    public String parameterName;
    private boolean nxtElem = false;
    
    @Override
    public void endDocument() {
        System.out.println("eof");
    }

    public void setParameter(String parameterName) {
        this.parameterName = parameterName;
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        //String infoStr = qName + "    " + Integer.toString(attributes.getLength());
        String infoStr = "";
        if (nxtElem) {
            for (int i = 0; i < attributes.getLength(); i++) { 
                infoStr += attributes.getQName(i) + " " + attributes.getValue(i) + " ";
            }
            System.out.println(infoStr);
        }

        nxtElem = this.parameterName.equals(attributes.getValue(0));
    }
}

