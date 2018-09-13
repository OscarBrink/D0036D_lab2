package model;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class Handler extends DefaultHandler {
    
    @Override
    public void endDocument() {
        System.out.println("eof");
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        String infoStr = qName + "    " + Integer.toString(attributes.getLength());
        for (int i = 0; i < attributes.getLength(); i++) {
            infoStr += " " + attributes.getQName(i) + " " + attributes.getValue(i);
        }
        System.out.println(infoStr);
    }
}

