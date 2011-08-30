package uk.ac.ebi.pride.data.coreIdent;

import java.util.Map;

/**
 * Referenceable param group stores a map of reference string to param group.
 * <p/>
 * User: rwang
 * Date: 29-Apr-2010
 * Time: 09:24:33
 */
public class ReferenceableParamGroup implements MassSpecObject {

    private Map<String, ParamGroup> refMap = null;

    /**
     * Constructor
     *
     * @param refMap required.
     */
    public ReferenceableParamGroup(Map<String, ParamGroup> refMap) {
        setRefMap(refMap);
    }

    public Map<String, ParamGroup> getRefMap() {
        return refMap;
    }

    public void setRefMap(Map<String, ParamGroup> refMap) {
        this.refMap = refMap;
    }


    public void addRefParamGroup(String ref, ParamGroup params) {
        refMap.put(ref, params);
    }

    public void removeRefParamGroup(String ref) {
        refMap.remove(ref);
    }

    public ParamGroup getRefParamGroup(String ref) {
        return refMap.get(ref);
    }
}
