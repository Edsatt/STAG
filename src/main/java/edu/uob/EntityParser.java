package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.PortNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class EntityParser {
    Map map;
    private final FileReader reader;
    ArrayList<Graph> locations;
    ArrayList<Edge> paths;

    public EntityParser(Map map, File entitiesFile){
        this.map = map;
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

            this.locations = sections.get(0).getSubgraphs();
            this.paths = sections.get(1).getEdges();

        }catch (ParseException pe) {
            throw new RuntimeException("Parsing failure");
        }
    }

    public void createLocations(){
        for(Graph location : locations){
            buildLocation(location);
        }
        buildPaths();
    }

    public void buildLocation(Graph location){
        Node locationDetails = location.getNodes(false).get(0);
        String locationId = locationDetails.getId().getId();
        String locationDescription = locationDetails.getAttribute("description");
        Location newLocation = new Location(locationId);
        newLocation.setDescription(locationDescription);
        for(Graph subgraph: location.getSubgraphs()){
            String entityId = subgraph.getId().getId();
            createEntity(newLocation, entityId, subgraph.getNodes(false));
        }
        setupStartLocation(locationId);
        map.addLocation(locationId, newLocation);
        map.addSubjectKey(locationId);
    }

    private void createEntity(Location location, String entityId, ArrayList<Node> node){
        for (Node value : node) {
            String id = value.getId().getId();
            String description = value.getAttribute("description");
            if(!id.isEmpty() && !description.isEmpty()){
                location.addValuePair(entityId, id, description);
                map.addSubjectKey(id);
            }
        }
    }

    private void buildPaths(){
        Edge path;
        String source;
        String target;
        for (Edge edge : paths) {
            path = edge;
            source = getEdgeString(path.getSource());
            target = getEdgeString(path.getTarget());
            addPathToLocation(source, target);
        }
    }

    private String getEdgeString(PortNode edge){
        return edge.getNode().getId().getId();
    }

    private void addPathToLocation(String source, String target){
        map.getLocation(source).addPath(target);
    }

    public void setupStartLocation(String key){
        if(map.getLocations().isEmpty()){
            map.setStartLocationKey(key);
        }
    }
}
