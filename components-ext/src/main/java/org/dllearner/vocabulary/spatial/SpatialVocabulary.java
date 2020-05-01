package org.dllearner.vocabulary.spatial;

import com.github.davidmoten.guavamini.Sets;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

public class SpatialVocabulary {
    private static final OWLDataFactory df = OWLManager.getOWLDataFactory();
    public static final String prefix = "http://dl-learner.org/ont/spatial#";

    // ---- atomic classes ----
    public static final OWLClass SpatialFeature =
            df.getOWLClass(IRI.create("http://www.opengis.net/ont/geosparql#Feature"));

    public static final Set<OWLClass> spatialClasses = Sets.newHashSet(SpatialFeature);

    // ---- object properties ----
    public static final OWLObjectProperty isConnectedWith =
            df.getOWLObjectProperty(IRI.create(prefix + "isConnectedWith"));
    public static final OWLObjectProperty overlapsWith =
            df.getOWLObjectProperty(IRI.create(prefix + "overlapsWith"));

    public static final Set<OWLObjectProperty> spatialObjectProperties = Sets.newHashSet(
            isConnectedWith,
            overlapsWith);

    // ---- data properties ----
    public static Set<OWLDataProperty> spatialDataProperties = Sets.newHashSet();
}
