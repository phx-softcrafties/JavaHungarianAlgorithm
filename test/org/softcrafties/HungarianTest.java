package org.softcrafties;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class HungarianTest {

    private SituationFactory factory;

    @Before
    public void before() {
        factory = new SituationFactory();
    }
    
    @Test
    public void testFindUnassignedResources() {
        HungarianAlgorithm hungarian = factory.createWithOneMatch();
        Set<Resource> freeResources = hungarian.getUnassignedResources();
        Set<Resource> expect = new HashSet<Resource>();
        expect.add(factory.alan);
        expect.add(factory.steve);
        assertEquals(expect, freeResources);
    }

    @Test
    public void testFindUnassignedTasks() {
        HungarianAlgorithm hungarian = factory.createWithOneMatch();
        Set<Task> freeTasks = hungarian.getUnassignedTasks();
        Set<Task> expect = new HashSet<Task>();
        expect.add(factory.floors);
        expect.add(factory.windows);
        assertEquals(expect, freeTasks);
    }

    @Test
    public void testReachableTasksWithOneTightBid() {
        HungarianAlgorithm hungarian = factory.createWithOneTightBid();
        hungarian.visitFromFreeTasks();
        Set<Task> reachableTasks = hungarian.getReachableTasks();
        Set<Task> expect = new HashSet<Task>();
        expect.add(factory.bathroom);
        expect.add(factory.floors);
        expect.add(factory.windows);
        assertEquals(expect, reachableTasks);
    }
    
    @Test
    public void testReachableResourcesWithOneTightBid() {
        HungarianAlgorithm hungarian = factory.createWithOneTightBid();
        hungarian.visitFromFreeTasks();
        Set<Resource> reachableResources = hungarian.getReachableResources();
        Set<Resource> expect = new HashSet<Resource>();
        expect.add(factory.jim);
        assertEquals(expect, reachableResources);
    }
    
    @Test
    public void testReachableTasksWithOneMatch() {
        HungarianAlgorithm hungarian = factory.createWithOneMatch();
        hungarian.visitFromFreeTasks();
        Set<Task> reachableTasks = hungarian.getReachableTasks();
        Set<Task> expect = new HashSet<Task>();
        expect.add(factory.floors);
        expect.add(factory.windows);
        assertEquals(expect, reachableTasks);
    }
    
    @Test
    public void testReachableResourcesWithOneMatch() {
        HungarianAlgorithm hungarian = factory.createWithOneMatch();
        hungarian.visitFromFreeTasks();
        Set<Resource> reachableResources = hungarian.getReachableResources();
        Set<Resource> expect = new HashSet<Resource>();
        assertEquals(expect, reachableResources);
    }

}
