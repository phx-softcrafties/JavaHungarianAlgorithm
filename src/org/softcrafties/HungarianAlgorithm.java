package org.softcrafties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
        findTightBids();
    }

    public void findTightBids() {
        Iterator<Bid> iterator = looseBids.iterator();
        while (iterator.hasNext()) {
            Bid bid = iterator.next();
            if (bidIsTight(bid)) {
                tightBids.add(bid);
                iterator.remove();
            }
        }
    }

    public boolean bidIsTight(Bid bid) {
        double difference = bid.getCost()
                - resourcePotential.get(bid.getResource())
                - taskPotential.get(bid.getTask());
        return Math.abs(difference) < 1e-14;
    }

    public void match(Bid bid) {
        if (matchedBids.contains(bid)) {
            return;
        }
        if (!looseBids.contains(bid) && !tightBids.contains(bid)) {
            throw new AssertionError("Unknown key: " + bid);
        }
        if (!tightBids.contains(bid)) {
            
            String message = "Cannot match loose bid: " + bid;
            message += "\n  (resource potential = " +   
                    resourcePotential.get(bid.getResource());
            message += ", task potential = " +
                    taskPotential.get(bid.getTask()) + ")";
            throw new AssertionError(message);
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
        tightBids.add(bid);
        matchedBids.remove(bid);
        freeResources.add(resource);
        freeTasks.add(task);
    }

    public void visitFromFreeTasks() {
        reachableResources.clear();
        reachableTasks.clear();
        reachableTasks.addAll(freeTasks);
        tasksToVisit.addAll(freeTasks);
        visitTasks();
    }

    public void visitTasks() {
        for (Bid bid : tightBids) {
            if (tasksToVisit.contains(bid.getTask())) {
                Resource resource = bid.getResource();
                reachableResources.add(resource);
                resourcesToVisit.add(resource);
            }
        }
        tasksToVisit.clear();
        if (!resourcesToVisit.isEmpty()) {
            visitResources();
        }
    }

    private void visitResources() {
        for (Bid bid : matchedBids) {
            if (resourcesToVisit.contains(bid.getResource())) {
                Task resource = bid.getTask();
                reachableTasks.add(resource);
                tasksToVisit.add(resource);
            }
        }
        resourcesToVisit.clear();
        if (!tasksToVisit.isEmpty()) {
            visitTasks();
        }
    }

    public void increasePotential(Resource resource, double increment) {
        double value = resourcePotential.get(resource) + increment;
        resourcePotential.put(resource, value);
        findTightBids();
    }

    public void increasePotential(Task task, double increment) {
        double value = taskPotential.get(task) + increment;
        taskPotential.put(task, value);
        findTightBids();
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

    public Set<Resource> getReachableResources() {
        return reachableResources;
    }
}
