package org.apache.jena.permissions.model;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Seq;

public class SeqTest {

    public static void main(String[] args) {
        Model m = ModelFactory.createDefaultModel();

        Seq seq = m.createSeq();

        seq.add( 1L);
        seq.add(2, 2L);
        m.write(System.out, "TURTLE");
        seq.set(1, 1L);
        m.write(System.out, "TURTLE");
    }

}
