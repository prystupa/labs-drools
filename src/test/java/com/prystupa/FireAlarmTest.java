package com.prystupa;

import org.drools.ClassObjectFilter;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 9/29/12
 * Time: 7:00 PM
 */
public class FireAlarmTest {

    private Room room;
    private Fire fire;
    private Sprinkler sprinkler;
    private StatelessKnowledgeSession session;
    private StatefulKnowledgeSession statefulSession;

    @Before
    public void setUp() {

        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(ResourceFactory.newClassPathResource("fireAlarm.drl", App.class), ResourceType.DRL);

        assertFalse(knowledgeBuilder.getErrors().toString(), knowledgeBuilder.hasErrors());

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());

        room = new Room("office");
        fire = new Fire(room);
        sprinkler = new Sprinkler(room, false);

        session = knowledgeBase.newStatelessKnowledgeSession();
        statefulSession = knowledgeBase.newStatefulKnowledgeSession();
    }

    @Test
    public void testSprinklerTurnsOnWhenFire() {

        // Arrange

        // Act
        session.execute(Arrays.asList(room, fire, sprinkler));

        // Assert
        assertTrue(sprinkler.isOn());
    }

    @Test
    public void testSprinklerTurnsOffWhenFireGone() {

        // Arrange
        session.execute(Arrays.asList(room, fire, sprinkler));

        // Act
        session.execute(Arrays.asList(room, sprinkler));

        // Assert
        assertFalse(sprinkler.isOn());
    }

    @Test
    public void testFireAlarmCycle() {

        // Setup
        Room kitchen = new Room("kitchen");
        Room bedroom = new Room("bedroom");
        Room office = new Room("office");

        for (Room room : Arrays.asList(kitchen, bedroom, office)) {
            Sprinkler sprinkler = new Sprinkler(room, false);

            statefulSession.insert(room);
            statefulSession.insert(sprinkler);
        }

        statefulSession.fireAllRules();

        // Alarm should not be active
        Collection alarms = statefulSession.getFactHandles(new ClassObjectFilter(Alarm.class));
        assertEquals(0, alarms.size());

        // Set office on fire
        Fire officeFire = new Fire(office);
        statefulSession.insert(officeFire);
        statefulSession.fireAllRules();
        assertEquals("Alarm should have activated", 1, statefulSession.getFactHandles(new ClassObjectFilter(Alarm.class)).size());

        // Set bedroom on fire
        Fire bedroomFire = new Fire(bedroom);
        statefulSession.insert(bedroomFire);
        statefulSession.fireAllRules();
        assertEquals("Alarm should still be activated", 1, statefulSession.getFactHandles(new ClassObjectFilter(Alarm.class)).size());

        // Combat the fire
        statefulSession.retract(statefulSession.getFactHandle(officeFire));
        statefulSession.retract(statefulSession.getFactHandle(bedroomFire));
        statefulSession.fireAllRules();
        assertEquals("Alarm should have deactivated", 0, statefulSession.getFactHandles(new ClassObjectFilter(Alarm.class)).size());
    }
}
