package dfki.rdf.modelrep;

import java.util.*;


public class XORSplit   extends Split
{
//----------------------------------------------------------------------------------------------------
protected boolean canActivate (ExecutableObject eoSucc)
{
    for (int i = 0; i < m_alSplitObjects.size(); i++)
    {
        ExecutableObject eoSplitObject = (ExecutableObject)m_alSplitObjects.get(i);
        if (eoSplitObject.isActivated())
            return false;
    }
    // all split objects are NOT activated => ok
    return true;
}

//----------------------------------------------------------------------------------------------------
}

