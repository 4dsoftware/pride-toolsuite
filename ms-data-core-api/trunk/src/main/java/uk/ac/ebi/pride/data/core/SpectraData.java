package uk.ac.ebi.pride.data.core;

/**
 * A data set containing spectra data (consisting of one or more spectra).
 * User: yperez
 * Date: 08/08/11
 * Time: 12:07
 */
public class SpectraData extends ExternalData {

    private CvParam spectrumIdFormat = null;

    /**
     * @param id    Generic Id of Spectra Data File
     * @param name  Generic Name of Spectra Data File
     * @param location  Location of the Spectra Data File
     * @param fileFormat  File Format in CvTerm
     * @param externalFormatDocumentationURI   External Format Documentation in CvTerm
     * @param spectrumIdFormat                 Spectrum Id Format
     */
    public SpectraData(String id, String name, String location, CvParam fileFormat,
                       String externalFormatDocumentationURI, CvParam spectrumIdFormat) {
        super(id, name, location, fileFormat, externalFormatDocumentationURI);
        this.spectrumIdFormat = spectrumIdFormat;
    }

    public CvParam getSpectrumIdFormat() {
        return spectrumIdFormat;
    }

    public void setSpectrumIdFormat(CvParam spectrumIdFormat) {
        this.spectrumIdFormat = spectrumIdFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpectraData that = (SpectraData) o;

        return !(spectrumIdFormat != null ? !spectrumIdFormat.equals(that.spectrumIdFormat) : that.spectrumIdFormat != null);

    }

    @Override
    public int hashCode() {
        return spectrumIdFormat != null ? spectrumIdFormat.hashCode() : 0;
    }
}



