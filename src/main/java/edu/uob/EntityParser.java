package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class EntityParser {
    private final FileReader reader;
    ArrayList<Graph> locations;
    ArrayList<Edge> paths;

    public EntityParser(File entitiesFile){
        try {
            reader = new FileReader(entitiesFile);
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("Failed to read from entities file");
        }
    }

    public void parse(){
        try{
            Parser parser = new Parser();
            parser.parse(reader);
            Graph document = parser.getGraphs().get(0);
            ArrayList<Graph> sections = document.getSubgraphs();

            //Store locations and paths in variables that can be accessed
            this.locations = sections.get(0).getSubgraphs();
            this.paths = sections.get(1).getEdges();

//            System.out.println(locations);
//            System.out.println(paths);

        }catch (ParseException pe) {
            throw new RuntimeException("Parsing failure");
        }
    }

    public void createLocations(){
        for(Graph location : locations){
            buildLocation(location);
        }
    }

    public void buildLocation(Graph location){
        Node locationDetails = location.getNodes(false).get(0);
        //System.out.println(location.getSubgraphs());
        for(Graph subgraph: location.getSubgraphs()){
            String subgraphId = subgraph.getId().getId();
            System.out.println(subgraphId);
            System.out.println("Node: " +subgraph.getNodes(false));
        }
        System.out.println("");
        //Location newLocation = new Location();
        }


}
