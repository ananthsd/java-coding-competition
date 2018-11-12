package com.codingcompetition.statefarm.utility;

import com.sothawo.mapjfx.Coordinate;
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

    public Coordinate parse(String city, String state) throws Exception {

        city = city.toLowerCase().replace(' ', '-');
        state = state.toLowerCase();

        SAXParser saxParser = this.saxParserFactory.newSAXParser();
        PlaceHandler handler = new PlaceHandler();

        try {
            saxParser.parse(new File(String.format("cache/places/%s-%s.xml", city, state)), handler);
        } catch (IOException io) {
            throw new SAXException(io);
        }

        return new Coordinate(handler.getLatitude(), handler.getLongitude());
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
