package org.softcrafties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HungarianAlgorithm {

    private Set<Resource> resources;
    private Set<Resource> freeResources;
    private Set<Resource> resourcesToVisit;
    private Map<Resource,Double> resourcePotential;
    private Set<Task> tasks;
    private Set<Task> freeTasks;
    private Set<Task> tasksToVisit;
    private Map<Task,Double> taskPotential;
    private Set<Bid> looseBids;
    private Set<Bid> matchedBids;
    private Set<Bid> tightBids;
    private HashSet<Resource> reachableResources;
    private HashSet<Task> reachableTasks;

    public HungarianAlgorithm(Set<Resource> resources, Set<Task> tasks, Set<Bid> bids) {
        this.resources = resources;
        freeResources = new HashSet<Resource>(resources);
        reachableResources = new HashSet<Resource>(resources.size());
        resourcesToVisit = new HashSet<Resource>(resources.size());
        resourcePotential = new HashMap<Resource, Double>(resources.size());
        for (Resource resource : resources) {
            resourcePotential.put(resource, 0.0);
        }
        
        this.tasks = tasks;
        freeTasks = new HashSet<Task>(tasks);
        reachableTasks = new HashSet<Task>(tasks.size());
        tasksToVisit = new HashSet<Task>(tasks.size());
        taskPotential = new HashMap<Task, Double>(tasks.size());
        for (Task task : tasks) {
            taskPotential.put(task, 0.0);
        }
        
        looseBids = new HashSet<Bid>(bids);
        matchedBids = new HashSet<Bid>(bids.size());
        tightBids = new HashSet<Bid>(bids.size());
        for (Bid bid : bids) {
            if (bid.isTight(0.0, 0.0)) {
                tightBids.add(bid);
                looseBids.remove(bid);
            }
        }
    }

    public void match(Bid bid) {
        if (matchedBids.contains(bid)) {
            return;
        }
        if (!looseBids.contains(bid) && !tightBids.contains(bid)) {
            throw new AssertionError("Unknown key: " + bid);
        }
        Resource resource = bid.getResource();
        if (!freeResources.contains(resource)) {
            throw new AssertionError("Resource already matched: " + resource);
        }
        Task task = bid.getTask();
        if (!freeTasks.contains(task)) {
            throw new AssertionError("Task already matched: " + task);
        }
        matchedBids.add(bid);
        looseBids.remove(bid);
        tightBids.remove(bid);
        freeResources.remove(resource);
        freeTasks.remove(task);
    }

    public void free(Bid bid) {
        if (looseBids.contains(bid)) {
            return;
        }
        if (!matchedBids.contains(bid)) {
            throw new AssertionError("Unknown key: " + bid);
        }
        Resource resource = bid.getResource();
        Task task = bid.getTask();
        if (bid.isTight(
                resourcePotential.get(resource), 
                taskPotential.get(task))) {
            tightBids.add(bid);
        } else {
            looseBids.add(bid);
        }
        matchedBids.remove(bid);
        freeResources.add(resource);
        freeTasks.add(task);
    }

    public void visitFromFreeTasks() {
        reachableResources.clear();
        reachableTasks.clear();
        reachableTasks.addAll(freeTasks);
    }

    public Set<Resource> getUnassignedResources() {
        return freeResources;
    }

    public Set<Task> getUnassignedTasks() {
        return freeTasks;
    }

    public Set<Task> getReachableTasks() {
        return reachableTasks;
    }
    
}
