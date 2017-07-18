package structPatterns;

import org.jgrapht.experimental.equivalence.EquivalenceComparator;


public class UniformEdgeComparator<E, C>
    implements EquivalenceComparator<E, C>
{
    

    /**
     * Always returns true.
     *
     * @see EquivalenceComparator#equivalenceCompare(Object, Object, Object,
     * Object)
     */
    public boolean equivalenceCompare(
        E arg1,
        E arg2,
        C context1,
        C context2)
    {
        return true;
    }

    /**
     * Always returns 0.
     *
     * @see EquivalenceComparator#equivalenceHashcode(Object, Object)
     */
    public int equivalenceHashcode(E arg1, C context)
    {
        return 0;
    }
}

// End UniformEquivalenceComparator.java
