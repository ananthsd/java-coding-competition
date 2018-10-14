package com.codingcompetition.statefarm;

import com.codingcompetition.statefarm.model.PointOfInterest;
import com.codingcompetition.statefarm.utility.PointOfInterestParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreetMapDataInterpreter implements Interpreter {

    private String filename;
    private List<PointOfInterest> points;

    public StreetMapDataInterpreter(String s) {
        this.filename = s;
        PointOfInterestParser parser = new PointOfInterestParser();
        try {
            this.points = parser.parse(this.filename);
        } catch (Exception e) {
            this.points = null;
        }
    }

    @Override
    public List<PointOfInterest> interpret() {
        return this.points;
    }

    @Override
    public List<PointOfInterest> interpret(SearchCriteria criteria) {

        return null;
    }

    @Override
    public List<PointOfInterest> interpret(Map<Integer, SearchCriteria> prioritizedCriteria) {
        return null;
    }

    @Override
    public List<PointOfInterest> findByCriterias(List<SearchCriteria> criterias) {
        return null;
    }
}
