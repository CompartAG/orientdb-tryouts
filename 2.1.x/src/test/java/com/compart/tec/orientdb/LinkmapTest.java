package com.compart.tec.orientdb;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 * Created by fc7 on 18/04/2016.
 */
public class LinkmapTest {

    @Test
    public void testLinkmap() {

        // setup
        OrientGraphFactory factory = new OrientGraphFactory("memory:testlinkmap");

        OrientGraph graph = factory.getTx();
        OrientVertex a = graph.addVertex("A");
        OrientVertex b = graph.addVertex("B");
        graph.commit();

        // exercice
        OrientVertex c = graph.addVertex("C");
        ODocument bDoc = b.getRecord();
        Map<String, OrientVertex> linkmap = getLinkMap(bDoc);
        linkmap.put(a.getId().toString(), c);
        bDoc.save();
        graph.commit(); // in 2.2 this mutates the Map<String, OrientVertex> into a Map<String, ODocument> !!!

        // verify
        Map<String, OrientVertex> map = getLinkMap(b.getRecord());

        Assertions.assertNotNull(map, "linkmap cannot be null");
        MatcherAssert.assertThat(map.get(a.getId().toString()), CoreMatchers.instanceOf(OrientVertex.class));

    }

    private Map<String, OrientVertex> getLinkMap(ODocument document) {

        if (!document.containsField("linkmap")) {
            document.field("linkmap", new HashMap<String, OrientVertex>(), OType.LINKMAP);
        }

        return document.field("linkmap", OType.LINKMAP);
    }
}
