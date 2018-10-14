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
        List<PointOfInterest> results = new ArrayList<>();
        if (criteria == null) {
            return results;
        }

        for (PointOfInterest p: this.points) {
            String value = p.getDescriptors().get(criteria.getCat().name().toLowerCase());
            if (value != null && value.equals(criteria.getValue())) {
                results.add(p);
            }

        }

        return results;
    }

    @Override
    public List<PointOfInterest> interpret(Map<Integer, SearchCriteria> prioritizedCriteria) {
        if (prioritizedCriteria == null) {
            return new ArrayList<>();
        }
        return null;
    }

    @Override
    public List<PointOfInterest> findByCriterias(List<SearchCriteria> criterias) {
        List<PointOfInterest> results = new ArrayList<>();
        if (criterias == null) {
            return results;
        }

        for (PointOfInterest p: this.points) {
            for (SearchCriteria criteria: criterias) {
                String value = p.getDescriptors().get(criteria.getCat().name().toLowerCase());
                if (value != null && value.equals(criteria.getValue())) {
                    results.add(p);
                    break;
                }
            }

        }

        return results;
    }
}
