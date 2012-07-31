package org.softcrafties;

import java.util.HashSet;
import java.util.Set;

public class SituationFactory {

    public Set<Resource> resources;
    public Set<Task> tasks;
    public Set<Bid> bids;
    
    public final Resource jim = new Resource("Jim");
    public final Resource steve = new Resource("Steve");
    public final Resource alan = new Resource("Alan");
    
    public final Task bathroom = new Task("Clean bathroom");
    public final Task floors = new Task("Sweep floors");
    public final Task windows = new Task("Wash windows");
    
    public final Bid jimBathroomBid = new Bid(jim, bathroom, 1.0);
    public final Bid jimFloorsBid = new Bid(jim, floors, 2.0);
    public final Bid jimWindowsBid = new Bid(jim, windows, 3.0);
    public final Bid steveBathroomBid = new Bid(steve, bathroom, 3.0);
    public final Bid steveFloorsBid = new Bid(steve, floors, 3.0);
    public final Bid steveWindowsBid = new Bid(steve, windows, 3.0);
    public final Bid alanBathroomBid = new Bid(alan, bathroom, 3.0);
    public final Bid alanFloorsBid = new Bid(alan, floors, 3.0);
    public final Bid alanWindowsBid= new Bid(alan, windows, 2.0);
    
    public SituationFactory() {
        setResources();
        setTasks();
        createBids();
    }
    
    public HungarianAlgorithm createInitialState() {
        HungarianAlgorithm hungarian =
                new HungarianAlgorithm(resources, tasks, bids);
        return hungarian;
    }

    public HungarianAlgorithm createWithOneMatch() {
        HungarianAlgorithm hungarian =
                new HungarianAlgorithm(resources, tasks, bids);
        hungarian.increasePotential(jim, 1.0);
        hungarian.match(jimBathroomBid);
        return hungarian;
    }

    private void setResources() {
        resources = new HashSet<Resource>();
        resources.add(jim);
        resources.add(steve);
        resources.add(alan);
    }

    private void setTasks() {
        tasks = new HashSet<Task>();
        tasks.add(bathroom);
        tasks.add(floors);
        tasks.add(windows);
        
    }

    private void createBids() {
        bids = new HashSet<Bid>();
        bids.add(jimBathroomBid);
        bids.add(jimFloorsBid);
        bids.add(jimWindowsBid);
        bids.add(steveBathroomBid);
        bids.add(steveFloorsBid);
        bids.add(steveWindowsBid);
        bids.add(alanBathroomBid);
        bids.add(alanFloorsBid);
        bids.add(alanWindowsBid); 
    }
}
