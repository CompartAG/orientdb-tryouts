package com.compart.tec.orientdb;

import org.junit.Test;

import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

/**
 * Created by fc7 on 14/04/2016.
 */
public class GraphShutdownTest {

    @Test (expected = ODatabaseException.class)
    public void testShutdownGraph_graphCommitAfterShutdown() {
        OrientGraphFactory factory = new OrientGraphFactory("memory:test");
        OrientGraph graph1 = factory.getTx();
        OrientGraph graph2 = factory.getTx();
        graph2.shutdown(true); // in 2.2 this will not close the database because graph1 is still active in the pool
        graph2.commit(); // this should fail
    }
}
