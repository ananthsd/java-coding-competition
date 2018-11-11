package com.codingcompetition.statefarm.utility;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlaceParser {
    private SAXParserFactory saxParserFactory;

    public PlaceParser() {
        this.saxParserFactory = SAXParserFactory.newInstance();
    }

    public double[] parse(String xml) throws Exception {
        FileWriter writer = new FileWriter(new File("src/main/resources/place.xml"), false);
        writer.write(xml);
        writer.close();

        SAXParser saxParser = this.saxParserFactory.newSAXParser();
        PlaceHandler handler = new PlaceHandler();

        try {
            saxParser.parse(new File("src/main/resources/place.xml"), handler);
        } catch (IOException io) {
            throw new SAXException(io);
        }

        return new double[] {handler.getLatitude(), handler.getLongitude()};
    }

    private class PlaceHandler extends DefaultHandler {

        double latitude;
        double longitude;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("place")) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getQName(i).equals("lat") && this.latitude == 0D) {
                        this.latitude = Double.parseDouble(attributes.getValue(i));
                    } else if (attributes.getQName(i).equals("lon") && longitude == 0D) {
                        this.longitude = Double.parseDouble(attributes.getValue(i));
                    }
                }
            }
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }
    }
}
