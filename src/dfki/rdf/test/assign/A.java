package dfki.rdf.test.assign;

// RDFS2Class: imports
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Iterator;
// RDFS2Class: end of imports


/** RDFS2Class: class A
  * <p>
  */
public class A
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toB **/
    java.util.HashSet m_toB = new java.util.HashSet();
    java.util.HashSet m_toB__asURI = new java.util.HashSet();

    /** RDFS2Class: putter for slot toB **/
    public void putToB (B p_toB)
    {
        m_toB.add(p_toB);
    }
    public void putToB (java.util.Collection p_toB)
    {
        m_toB = new java.util.HashSet(p_toB);
    }
    public void putToB (dfki.rdf.util.RDFResource p_toB)
    {
        m_toB__asURI.add(p_toB);
    }
    // RDFS2Class: end of putter for slot toB

    /** RDFS2Class: getter for slot toB **/
    public java.util.Collection getToB ()
    {
        return m_toB;
    }
    public java.util.Collection GetToB__asURI ()
    {
        return m_toB__asURI;
    }
    // RDFS2Class: end of getter for slot toB

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot subA **/
    java.util.HashSet m_subA = new java.util.HashSet();
    java.util.HashSet m_subA__asURI = new java.util.HashSet();

    /** RDFS2Class: putter for slot subA **/
    public void putSubA (A p_subA)
    {
        m_subA.add(p_subA);
    }
    public void putSubA (java.util.Collection p_subA)
    {
        m_subA = new java.util.HashSet(p_subA);
    }
    public void putSubA (dfki.rdf.util.RDFResource p_subA)
    {
        m_subA__asURI.add(p_subA);
    }
    // RDFS2Class: end of putter for slot subA

    /** RDFS2Class: getter for slot subA **/
    public java.util.Collection getSubA ()
    {
        return m_subA;
    }
    public java.util.Collection GetSubA__asURI ()
    {
        return m_subA__asURI;
    }
    // RDFS2Class: end of getter for slot subA

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot superA **/
    java.util.HashSet m_superA = new java.util.HashSet();
    java.util.HashSet m_superA__asURI = new java.util.HashSet();

    /** RDFS2Class: putter for slot superA **/
    public void putSuperA (A p_superA)
    {
        m_superA.add(p_superA);
    }
    public void putSuperA (java.util.Collection p_superA)
    {
        m_superA = new java.util.HashSet(p_superA);
    }
    public void putSuperA (dfki.rdf.util.RDFResource p_superA)
    {
        m_superA__asURI.add(p_superA);
    }
    // RDFS2Class: end of putter for slot superA

    /** RDFS2Class: getter for slot superA **/
    public java.util.Collection getSuperA ()
    {
        return m_superA;
    }
    public java.util.Collection GetSuperA__asURI ()
    {
        return m_superA__asURI;
    }
    // RDFS2Class: end of getter for slot superA

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (!m_toB.isEmpty()) {
            sb.append(sIndent+"-> toB:\n");
            for (Iterator it_toB = m_toB.iterator(); it_toB.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_toB.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_toB.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_toB__asURI.isEmpty()) {
            sb.append(sIndent+"-> toB(URI):\n");
            for (Iterator it_toB__asURI = m_toB__asURI.iterator(); it_toB__asURI.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_toB__asURI.next()).toStringShort() + "\n" );
            }
        }
        if (!m_subA.isEmpty()) {
            sb.append(sIndent+"-> subA:\n");
            for (Iterator it_subA = m_subA.iterator(); it_subA.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_subA.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_subA.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_subA__asURI.isEmpty()) {
            sb.append(sIndent+"-> subA(URI):\n");
            for (Iterator it_subA__asURI = m_subA__asURI.iterator(); it_subA__asURI.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_subA__asURI.next()).toStringShort() + "\n" );
            }
        }
        if (!m_superA.isEmpty()) {
            sb.append(sIndent+"-> superA:\n");
            for (Iterator it_superA = m_superA.iterator(); it_superA.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_superA.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_superA.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_superA__asURI.isEmpty()) {
            sb.append(sIndent+"-> superA(URI):\n");
            for (Iterator it_superA__asURI = m_superA__asURI.iterator(); it_superA__asURI.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_superA__asURI.next()).toStringShort() + "\n" );
            }
        }
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
    public void assign (dfki.rdf.util.THING thingToAssign, dfki.rdf.util.KnowledgeBase kb)
    {
        A other = (A)thingToAssign;

        // slot subA:
        java.util.Collection collSubA_this  = new java.util.HashSet(getSubA());  // copy collection of old slot values
        putSubA(new java.util.HashSet());                                        // clear collection of slot values
        java.util.Collection collSubA_other = other.getSubA();
        for (Iterator itSubA_this = collSubA_this.iterator(); itSubA_this.hasNext(); )
        {
            A subA_this = (A)itSubA_this.next();
            Object subA_other = find(subA_this, collSubA_other);
            // now: 3 cases:
            // (1) if found subA_other is only a resource, we can leave the old value
            // (2) if found subA_other is a THING, then we take that one, because it contains
            //     already a Java object (which may/will be newer than the potential old one)
            // (3) if no subA_other can be found, the slot value has to be removed
            if (subA_other instanceof dfki.rdf.util.RDFResource)
            {   // case (1)
                putSubA(subA_this);  // insert the old slot value again
                remove(collSubA_other, ((dfki.rdf.util.RDFResource)subA_other).getURI());
            }
            else
            if (subA_other instanceof A)
            {   // case (2)
                putSubA((A)subA_other);  // insert the newer slot value
                kb.put((A)subA_other);   // now the new Java object exists in the knowledge base, too
                remove(collSubA_other, ((dfki.rdf.util.THING)subA_other).getURI());
            }
            else
            {   // case (3) => nothing to do (slot value is already removed)
            }
        }
        // now collSubA_other contains slot values that are new to subA_this => add them all
        for (Iterator itSubA_other = collSubA_other.iterator(); itSubA_other.hasNext(); )
        {
            Object subA_other = itSubA_other.next();
            if (subA_other instanceof dfki.rdf.util.RDFResource)
        }
    }

    Object find (dfki.rdf.util.THING thingThis, Collection collOther)
    {
        for (Iterator itOthers = collOther.iterator(); itOthers.hasNext(); )
        {
            Object other = itOthers.next();
            if ( (other instanceof dfki.rdf.util.RDFResource) &&
                 ((dfki.rdf.util.RDFResource)other).getURI().equals(thingThis.getURI()) )
                return other;
            else
            if ( (other instanceof dfki.rdf.util.THING) &&
                 ((dfki.rdf.util.THING)other).getURI().equals(thingThis.getURI()) )
                return other;
        }
        return null;  // not found
    }

    void remove (Collection coll, String sURI)
    {
        for (Iterator it = coll.iterator(); it.hasNext(); )
        {
            Object obj = it.next();
            if ( (obj instanceof dfki.rdf.util.RDFResource) &&
                 ((dfki.rdf.util.RDFResource)obj).getURI().equals(sURI) )
            {
                it.remove();
                return;
            }
        }
    }

}
// RDFS2Class: end of class A
// EOF

