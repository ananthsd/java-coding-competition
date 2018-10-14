package com.codingcompetition.statefarm;

import com.codingcompetition.statefarm.model.PointOfInterest;
import com.codingcompetition.statefarm.utility.PointOfInterestParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
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

    /**
     *
     * @return
     */
    @Override
    public List<PointOfInterest> interpret() {
        return this.points;
    }

    private boolean fitsCriteria(PointOfInterest p, SearchCriteria c) {
        if (c.getCat().name().equals("NAMESTARTSWITH")) {
            Collection<String> descriptorValues = p.getDescriptors().values();
            for (String dV: descriptorValues) {
                if (dV.startsWith(c.getValue())) {
                    return true;
                }
            }

        } else if (c.getCat().name().equals("NAMEENDSWITH")) {
            Collection<String> descriptorValues = p.getDescriptors().values();
            for (String dV: descriptorValues) {
                if (dV.endsWith(c.getValue())) {
                    return true;
                }
            }

        } else {
            String value = p.getDescriptors().get(c.getCat().name().toLowerCase());
            if (c.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<PointOfInterest> interpret(SearchCriteria criteria) {
        List<PointOfInterest> results = new ArrayList<>();
        if (criteria == null) {
            return results;
        }

        for (PointOfInterest p: this.points) {
            if (fitsCriteria(p, criteria)) {
                results.add(p);
            }

        }

        return results;
    }

    @Override
    public List<PointOfInterest> interpret(Map<Integer, SearchCriteria> prioritizedCriteria) {
        List<PointOfInterest> results = new ArrayList<>();
        if (prioritizedCriteria == null) {
            return results;
        }

        Collection<SearchCriteria> criterias = prioritizedCriteria.values();

        for (PointOfInterest p: this.points) {
            for (SearchCriteria criteria: criterias) {
                if (fitsCriteria(p, criteria)) {
                    results.add(p);
                    break;
                }
            }

        }

        return results;
    }

    @Override
    public List<PointOfInterest> findByCriterias(List<SearchCriteria> criterias) {
        List<PointOfInterest> results = new ArrayList<>();
        if (criterias == null) {
            return results;
        }

        for (PointOfInterest p: this.points) {
            for (SearchCriteria criteria: criterias) {
                if (fitsCriteria(p, criteria)) {
                    results.add(p);
                }
            }

        }

        return results;
    }

}
