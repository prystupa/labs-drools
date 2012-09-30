package com.prystupa;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 9/29/12
 * Time: 7:00 PM
 */
public class FireAlarmTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testFireAlarm() {

        logger.info("Works...");

        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(ResourceFactory.newClassPathResource("fireAlarm.drl", App.class), ResourceType.DRL);

        assertFalse(knowledgeBuilder.hasErrors());

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());

        StatelessKnowledgeSession session = knowledgeBase.newStatelessKnowledgeSession();

        Room room = new Room();
        Fire fire = new Fire(room);
        Sprinkler sprinkler = new Sprinkler(room, false);
        session.execute(Arrays.asList(room, fire, sprinkler));

        assertTrue(sprinkler.isOn());
    }
}
