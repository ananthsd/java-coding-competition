package com.codingcompetition.statefarm.utility;

import com.codingcompetition.statefarm.model.PointOfInterest;
import org.hamcrest.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;

public class PointOfInterestParserTest {

    @Test
    public void canInterpretLatitudeFromDataSource() throws Exception {

        PointOfInterestParser parser = new PointOfInterestParser();
        List<PointOfInterest> interpretedData = parser.parseFile("/small-metro.xml");
        MatcherAssert.assertThat(interpretedData.size(), equalTo(78848));
        final List<PointOfInterest> collect = interpretedData.stream().filter(aPointOfInterest -> aPointOfInterest.getLatitude() == null).collect(Collectors.toList());
        MatcherAssert.assertThat(collect.size(), equalTo(0));
    }

    @Test
    public void canInterpretLongitudeFromDataSource() throws Exception {

        PointOfInterestParser parser = new PointOfInterestParser();
        List<PointOfInterest> interpretedData = parser.parseFile("/small-metro.xml");
        MatcherAssert.assertThat(interpretedData.size(), equalTo(78848));
        final List<PointOfInterest> collect = interpretedData.stream().filter(aPointOfInterest -> aPointOfInterest.getLongitude() == null).collect(Collectors.toList());
        MatcherAssert.assertThat(collect.size(), equalTo(0));
    }

    @Test
    public void canInterpretTagsWithinNodes() throws Exception {
        PointOfInterestParser parser = new PointOfInterestParser();
        List<PointOfInterest> interpretedData = parser.parseFile("/small-metro.xml");
        final List<PointOfInterest> collect = interpretedData.stream().filter(aPointOfInterest -> aPointOfInterest.getDescriptors().size() != 0).collect(Collectors.toList());
        MatcherAssert.assertThat(collect.size(), not(equalTo(0)));

    }

    @Test
    public void canInterpretLineByLine() throws Exception {
        PointOfInterestParser parser = new PointOfInterestParser();
        FileReader smallMetroDataFile = new FileReader("src/main/resources/small-metro.xml");
        BufferedReader in = new BufferedReader(smallMetroDataFile);

        String line;
        parser.startParseLine();
        while ((line = in.readLine()) != null) {
            parser.parseLine(line);

        }
        List<PointOfInterest> interpretedData = parser.endParseLine();
        MatcherAssert.assertThat(interpretedData.size(), equalTo(78848));
    }

}
