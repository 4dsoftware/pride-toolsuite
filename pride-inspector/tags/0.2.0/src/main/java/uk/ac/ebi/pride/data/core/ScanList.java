package uk.ac.ebi.pride.data.core;

import java.util.List;

/**
 * List and descriptions of scans.
 *
 * In mzML 1.1.0.1, the following cv terms must be added:
 *
 * 1. Must include only one child term of "spectra combination".
 * (sum of spectra, no combination)
 *
 * User: rwang
 * Date: 20-Feb-2010
 * Time: 18:57:52
 */
public class ScanList extends ParamGroup {
    private List<Scan> scans = null;

    /**
     * Constructor
     * @param scans required.
     * @param params    optional.
     */
    public ScanList(List<Scan> scans, ParamGroup params) {
        super(params);
        setScans(scans);
    }

    public List<Scan> getScans() {
        return scans;
    }

    public void setScans(List<Scan> scans) {
        if (scans == null || scans.isEmpty()) {
            throw new IllegalArgumentException("Scans can not be NULL or empty");
        } else {
            this.scans = scans;
        }
    }
}
