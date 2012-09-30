package com.prystupa;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AppTest {

    @Test
    public void testApp() {

        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(ResourceFactory.newClassPathResource("licenseApplication.drl", App.class), ResourceType.DRL);

        if (knowledgeBuilder.hasErrors()) {
            System.err.println(knowledgeBuilder.getErrors().toString());
        }

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());

        StatelessKnowledgeSession session = knowledgeBase.newStatelessKnowledgeSession();

        Applicant applicant = new Applicant("Mr John Smith", 16);
        Application application = new Application();

        assertTrue(application.isValid());

        session.execute(Arrays.asList(applicant, application));
        assertFalse(application.isValid());
    }
}
