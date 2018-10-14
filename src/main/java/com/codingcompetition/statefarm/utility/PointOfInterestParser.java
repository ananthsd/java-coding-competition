package com.codingcompetition.statefarm.utility;

import com.codingcompetition.statefarm.model.PointOfInterest;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PointOfInterestParser {


    // Stacks for storing the elements and objects.
    private Stack<String> elements = new Stack<String>();
    private Stack<PointOfInterest> objects = new Stack<PointOfInterest>();


    /**
     * Parses XML file and returns list of PointOfInterest
     * @param fileName XML file
     * @return list of points of interest
     * @throws IOException file not found
     * @throws SAXException XML parsing exception
     */
    public List<PointOfInterest> parse(String fileName) throws IOException, SAXException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            PointHandler handler = new PointHandler();
            saxParser.parse(new File("src/main/resources" + fileName), handler);
            return handler.getPoints();
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
    }

    //Handler to parse data with SAX
    private class PointHandler extends DefaultHandler {
        private List<PointOfInterest> pointsOfInterest;
        private PointOfInterest currentPoint;

        /**
         * Starts at beginning of XML
         * @throws SAXException XML parsing exception
         */
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            pointsOfInterest = new ArrayList<>();
        }

        /**
         * Beginning of XML element
         * @param uri url
         * @param localName name
         * @param qName qName
         * @param attributes attributes
         * @throws SAXException XML parsing exception
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.toLowerCase().equals("node")) {
                currentPoint = new PointOfInterest();
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getQName(i).equals("lat")) {
                        currentPoint.setLatitude(attributes.getValue(i));
                    } else if (attributes.getQName(i).equals("lon")) {
                        currentPoint.setLongitude(attributes.getValue(i));
                    } else {
                        currentPoint.addDescriptor(attributes.getQName(i), attributes.getValue(i));
                    }
                }
            }
            if (qName.toLowerCase().equals("tag")) {
                currentPoint.addDescriptor(attributes.getValue(0),attributes.getValue(1));
            }
        }

        /**
         * End of XML element
         * @param uri url
         * @param localName localName
         * @param qName qName
         * @throws SAXException XML parsing exception
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.toLowerCase().equals("node")) {
                pointsOfInterest.add(currentPoint);
            }
        }

        /**
         * Getter for pointsOfInterest
         * @return pointsOfInterest
         */
        public List<PointOfInterest> getPoints() {
            return pointsOfInterest;
        }
    }
}
