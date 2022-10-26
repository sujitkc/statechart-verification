package constabl.automaton;

import java.util.*;
public class CFA {
    List<Location> locations;
    List<Edge> edges;
    List<CFA> next;
    public List<CFA> getNext() {
        return next;
    }
    public void setNext(List<CFA> next) {
        this.next = next;
    }
    public List<Location> getLocations() {
        return locations;
    }
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    public List<Edge> getEdges() {
        return edges;
    }
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
    public Location getNextLocation(Location loc){
        Location nextLocation=null;
        for(Edge e:this.edges){
            if(e.getSource()==loc){
                nextLocation = e.getDest();
                break;
            }
        }
        return nextLocation;
    }
    
}
