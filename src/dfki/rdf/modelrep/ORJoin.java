package dfki.rdf.modelrep;

import java.util.*;


public class ORJoin   extends Join
{
//----------------------------------------------------------------------------------------------------
public boolean isActivated ()
{
    for (int i = 0; i < m_alJoinObjects.size(); i++)
    {
        ExecutableObject eoJoinObject = (ExecutableObject)m_alJoinObjects.get(i);
        if (eoJoinObject.isFinished())
            return true;
    }
    // no join objects are finished
    return false;
}

//----------------------------------------------------------------------------------------------------
}

