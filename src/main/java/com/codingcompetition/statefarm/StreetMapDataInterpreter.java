package com.codingcompetition.statefarm;

import com.codingcompetition.statefarm.model.PointOfInterest;
import com.codingcompetition.statefarm.utility.PointOfInterestParser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class StreetMapDataInterpreter implements Interpreter {

    private String filename;
    private List<PointOfInterest> points;

    /**
     * Creates object by which to query.
     * @param s filename
     */
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
     * Gets List of all points
     * @return points
     */
    @Override
    public List<PointOfInterest> interpret() {
        return this.points;
    }

    /**
     * Returns if a point meets a criteria
     * @param p point
     * @param c criteria
     * @return does it fit
     */
    private boolean fitsCriteria(PointOfInterest p, SearchCriteria c) {
        if (c.getCat().name().equals("NAMESTARTSWITH")) {
            String name = p.getDescriptors().get("name");
            if (name != null && name.startsWith(c.getValue().toLowerCase())) {
                return true;
            }

            return false;

        } else if (c.getCat().name().equals("NAMEENDSWITH")) {
            String name = p.getDescriptors().get("name");
            if (name != null && name.endsWith(c.getValue().toLowerCase())) {
                return true;
            }

            return false;

        } else {
            String value = p.getDescriptors().get(c.getCat().name().toLowerCase());
            if (c.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns list of points that meet a single criteria
     * @param criteria criteria to look for
     * @return points that meet criteria
     */
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

    /**
     * Searches through list of points in order of priority and
     * returns list of points that match all criteria.
     * @param prioritizedCriteria critieria prioritized
     * @return list that matches all criteria
     */
    @Override
    public List<PointOfInterest> interpret(Map<Integer, SearchCriteria> prioritizedCriteria) {
        List<PointOfInterest> results = new ArrayList<>();
        if (prioritizedCriteria == null) {
            return results;
        }

        Collection<SearchCriteria> criterias = prioritizedCriteria.values();

        for (PointOfInterest p: this.points) {
            boolean perfectFit = true;
            for (SearchCriteria criteria: criterias) {
                if (!fitsCriteria(p, criteria)) {
                    perfectFit = false;
                    break;
                }
            }

            if (perfectFit) {
                results.add(p);
            }

        }

        return results;
    }

    /**
     * Returns list of points that matches any of the criteria.
     * @param criterias list of criteria
     * @return list of points that fits any of the criteria
     */
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
                    break;
                }
            }

        }

        return results;
    }

}
