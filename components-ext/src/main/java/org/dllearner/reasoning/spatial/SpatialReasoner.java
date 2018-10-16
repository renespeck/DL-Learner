package org.dllearner.reasoning.spatial;

import org.dllearner.core.ReasonerComponent;
import org.semanticweb.owlapi.model.OWLIndividual;

import java.sql.ResultSet;
import java.util.stream.Stream;

/**
 * Spatial reasoner interface specifying a reasoner which is capable of
 * reasoning over implicit spatial relations like 'near', 'inside', 'along' etc.
 *
 * For now only geo:asWKT literals will be supported. So, geographic geometries
 * can be expressed by means of the following primitives: (examples taken from
 * https://en.wikipedia.org/wiki/Well-known_text)
 *
 * - POINT (long1 lat1)
 * - LINESTRING (long1 lat1, long2 lat2, long3 lat3)
 * - POLYGON ((long1 lat1, long2 lat2, long3 lat3, long4 lat4, long1 lat1))
 * - POLYGON ((long1 lat1, long2 lat2, long3 lat3, long4 lat4, long1 lat1), \
 *            (long5 lat5, long6 lat6, long7 lat7, long5 lat5))
 * - MULTIPOINT ((long1 lat1), (long2 lat2), (long3 lat3), (long4 lat4))
 * - MULTIPOINT (long1 lat1, long2 lat2, long3 lat3, long4 lat4)
 * - MULTILINESTRING ((long1 lat1, long2 lat2, long3 lat3), \
 *                    (long4 lat4, long5 lat5, long6 lat6, long7 lat7))
 * - MULTIPOLYGON (((long1 lat1, long2 lat2, long3 lat3, long4 lat4)), \
 *                 ((long5 lat5, long6 lat6, long7 lat7, long8 lat8, long9 lat9)))
 *
 * The implementation of the Region Connection Calculus relations should follow
 * the relation definitions as e.g. presented in Table 1 in 'Towards Spatial
 * Reasoning in the Semantic Web: A Hybrid Knowledge Representation System
 * Architecture' by Grüttler and Bauer-Messmer,
 * https://www.wsl.ch/fileadmin/user_upload/WSL/Projekte/dnl/Grutter_Bauer-Messmer_AGILE_2007.pdf
 */
public interface SpatialReasoner extends ReasonerComponent {
    // <RCC area feature relations>
    // See: https://en.wikipedia.org/wiki/Region_connection_calculus

    // C
    /**
     * Returns a stream of OWL individuals which are connected with the input
     * individual in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getConnectedIndividuals(OWLIndividual individual);

    /**
     * Returns `true` if the input OWL individuals are connected in terms of
     * their spatial extension and w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus), and `false`
     * otherwise.
     *
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean areConnected(OWLIndividual individual1, OWLIndividual individual2);

    // DC
    /**
     * Returns a stream of OWL individuals which are disconnected from the input
     * individual in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * Disconnected(x, y) iff. not Connected(x, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getDisconnectedIndividuals(OWLIndividual individual);

    /**
     * Returns `true` if the input OWL individuals are disconnected from each
     * other in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus),
     * and `false` otherwise.
     *
     * Disconnected(x, y) iff. not Connected(x, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean areDisconnected(OWLIndividual individual1, OWLIndividual individual2);

    // P
    /**
     * Returns a stream of OWL individuals which are part of the input OWL
     * individual in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getIndividualsWhichArePartOf(OWLIndividual individual);

    /**
     * Returns `true` if the first input OWL individual is part of the second
     * input individual in terms of their respective spatial extension and
     * w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus), and `false`
     * otherwise.
     *
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean isPartOf(OWLIndividual part, OWLIndividual whole);

    // PP
    /**
     * Returns a stream of OWL individuals which are a proper part of the input
     * OWL individual in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * ProperPartOf(x, y) iff. PartOf(x, y) and not PartOf(y, x) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getIndividualsWhichAreProperPartOf(OWLIndividual individual);

    /**
     * Returns `true` if the first input OWL individual is a proper part of the
     * second input OWL individual in terms of their respective spatial
     * extension and w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus), and `false`
     * otherwise.
     *
     * ProperPartOf(x, y) iff. PartOf(x, y) and not PartOf(y, x) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean isProperPartOf(OWLIndividual part, OWLIndividual whole);

    // EQ
    /**
     * Returns a stream of OWL individuals which are equal to the input OWL
     * individual in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus) .
     *
     * Equal(x, y) iff. PartOf(x, y) and PartOf(y, x) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getSpatiallyEqualIndividuals(OWLIndividual individual);

    /**
     * Returns `true` if the input OWL individuals are equal in terms of their
     * spatial extension and w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus) and `false`
     * otherwise.
     *
     * Equal(x, y) iff. PartOf(x, y) and PartOf(y, x) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean areSpatiallyEqual(OWLIndividual individual1, OWLIndividual individual2);

    // O
    /**
     * Returns a stream of OWL individuals which are overlapping with the input
     * OWL individual in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getOverlappingIndividuals(OWLIndividual individual);

    /**
     * Returns `true` if the input OWL individuals are overlapping in terms of
     * their spatial extension and w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus), and `false`
     * otherwise.
     *
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean areOverlapping(OWLIndividual individual1, OWLIndividual individual2);

    // DC
    /**
     * Returns a stream of OWL individuals which are discrete from the input
     * OWL individual in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * DiscreteFrom(x, y) iff. not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getIndividualsDiscreteFrom(OWLIndividual individual);

    /**
     * Returns `true` if the input OWL individuals are discrete from each other
     * in terms of their spatial extension and w.r.t. the Region Connection
     * Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus), and
     * `false` otherwise.
     *
     * DiscreteFrom(x, y) iff. not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean areDiscreteFromEachOther(OWLIndividual individual1, OWLIndividual individual2);

    // PO
    /**
     * Returns a stream of OWL individuals which are partially overlapping with
     * the input OWL individual in terms of their spatial extension and w.r.t.
     * the Region Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * PartiallyOverlaps(x, y) iff. Overlaps(x, y) and not PartOf(x, y) and
     *      not PartOf(y, x) .
     * DiscreteFrom(x, y) iff. not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getPartiallyOverlappingIndividuals(OWLIndividual individual);

    /**
     * Returns `true` if the input OWL individuals are partially overlapping in
     * terms of their spatial extension and w.r.t. the Region Connection
     * Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus), and
     * `false` otherwise.
     *
     * PartiallyOverlaps(x, y) iff. Overlaps(x, y) and not PartOf(x, y) and
     *      not PartOf(y, x) .
     * DiscreteFrom(x, y) iff. not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean arePartiallyOverlapping(OWLIndividual individual1, OWLIndividual individual2);

    // EC
    /**
     * Returns a stream of OWL individuals which are externally connected to the
     * input OWL individual in terms of their spatial extension and w.r.t. the
     * Region Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * ExternallyConnected(x, y) iff. Connected(x, y) and not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getExternallyConnectedIndividuals(OWLIndividual individual);

    /**
     * Returns `true` if the input OWL individuals are externally connected with
     * each other in terms of their spatial extension and w.r.t. the Region
     * Connection Calculus (https://en.wikipedia.org/wiki/Region_connection_calculus),
     * and `false` otherwise.
     *
     * ExternallyConnected(x, y) iff. Connected(x, y) and not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean areExternallyConnected(OWLIndividual individual1, OWLIndividual individual2);

    // TPP
    /**
     * Returns a stream of OWL individuals which are a tangential proper part
     * of the input OWL individual in terms of their spatial extension and
     * w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * TangentialProperPartOf(x, y) iff. ProperPartOf(x, y) and
     *      exists z: (ExternallyConnected(z, x) and ExternallyConnected(z, y))
     * ProperPartOf(x, y) iff. PartOf(x, y) and not PartOf(y, x) .
     * ExternallyConnected(x, y) iff. Connected(x, y) and not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getIndividualsWhichAreTangentialProperPartOf(OWLIndividual individual);

    /**
     * Returns `true` if the first input OWL individual is a tangential proper
     * part of the second input OWL individual in terms of their respective
     * spatial extension and w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus), and `false`
     * otherwise.
     *
     * TangentialProperPartOf(x, y) iff. ProperPartOf(x, y) and
     *      exists z: (ExternallyConnected(z, x) and ExternallyConnected(z, y))
     * ProperPartOf(x, y) iff. PartOf(x, y) and not PartOf(y, x) .
     * ExternallyConnected(x, y) iff. Connected(x, y) and not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean isTangentialProperPartOf(OWLIndividual part, OWLIndividual whole);

    // NTPP
    /**
     * Returns a stream of OWL individuals which are a non-tangential proper
     * part of the input OWL individual in terms of their spatial extension and
     * w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus).
     *
     * NonTangentialProperPartOf(x, y) iff. ProperPartOf(x, y) and
     *   not exists z: (ExternallyConnected(z, x) and ExternallyConnected(z, y))
     * ProperPartOf(x, y) iff. PartOf(x, y) and not PartOf(y, x) .
     * ExternallyConnected(x, y) iff. Connected(x, y) and not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    Stream<OWLIndividual> getIndividualsWhichAreNonTangentialProperPartOf(OWLIndividual individual);

    /**
     * Returns `true` if the first input OWL individual is a non-tangential
     * proper part of the second input OWL individual in terms of their
     * respective spatial extension and w.r.t. the Region Connection Calculus
     * (https://en.wikipedia.org/wiki/Region_connection_calculus), and `false`
     * otherwise.
     *
     * NonTangentialProperPartOf(x, y) iff. ProperPartOf(x, y) and
     *   not exists z: (ExternallyConnected(z, x) and ExternallyConnected(z, y))
     * ProperPartOf(x, y) iff. PartOf(x, y) and not PartOf(y, x) .
     * ExternallyConnected(x, y) iff. Connected(x, y) and not Overlaps(x, y) .
     * Overlaps(x, y) iff. exists z: PartOf(z, x) and PartOf(z, y) .
     * PartOf(x, y) iff. forall z: Connected(z, x) --> Connected(z, y) .
     * Connected(x, y) iff. x and y have at least one point in common .
     */
    boolean isNonTangentialProperPartOf(OWLIndividual part, OWLIndividual whole);
    // </RCC area feature relations>
}
