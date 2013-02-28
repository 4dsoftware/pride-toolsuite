package uk.ac.ebi.pride.data.core;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;

/**
 * General descriptions shared or used for the whole file.
 * MetaData Description at the Experimental level contain the information for:
 * - Samples Description.
 * - File Version.
 * - List of the Softwares used in the Experimental Protocol.
 * - File Creation Date.
 * - An a List of References.
 * <p/>
 * User: rwang, yperez
 * Date: 08-Mar-2010
 * Time: 10:48:29
 */
public class ExperimentMetaData extends IdentifiableParamGroup {

    /**
     * The date when the experiment is created
     */
    private Date creationDate = null;

    /**
     * The complete set of Contacts Organisations for this file.
     */
    private List<Organization> organizationList = null;

    /**
     * The complete set of Contacts People for this file.
     * In case of MzMl the Person Object Must be constructed whit a set of Params Group.
     * Extracted From CvParams.
     * List of contact details
     * In mzMl 1.1.0.1, each contact must have the following cv terms:
     * May include one or more child terms of "contact person attribute"
     * (contact name, contact address, contact email and et al)
     */
    private List<Person> personList = null;

    /**
     * The Experiment Protocols is an small view of the protocols used in the Experiment
     * the idea is to define an small object like pride experiments that could be used also for MzIdentMl in order
     * to put and small description of the steps in the experiment.
     */
    private ExperimentProtocol protocol = null;

    /**
     * The Provider of the mzIdentML record in terms of the contact and software. The provider
     * is an instance with the the last final software that generate the file.
     */
    private Provider provider = null;

    /**
     * The date when the experiment is made public
     */
    private Date publicDate = null;

    /**
     * List of publications of the experiment
     */
    private List<Reference> references = null;

    /**
     * list and description of samples in the case of mzIdentML.
     */
    private List<Sample> sampleList = null;

    /*
     * Short Label used by Pride XML Object
     */
    private String shortLabel = null;

    /**
     * list and description of software used to acquire and/or process
     * the data in the file.
     */
    private List<Software> softwareList = null;

    /**
     * List and descriptions of the source files
     */
    private List<SourceFile> sourceFiles = null;

    /**
     * List of SpectraData Objects used by MZIdentML to refereed the original mass spectra files.
     * A data set containing spectra data (consisting of one or more spectra).
     *
     * todo: can this be stored in MzGraphMetaData?
     */
    private List<SpectraData> spectraDataList = null;

    /**
     * version of this document used for PRIDE and MzIdentML
     */
    private String version = null;


    /**
     * Constructor for ExperimentMetaData
     *
     * @param params ParamGroup
     * @param id     ID
     * @param name   Name or Title
     * @param version Version of the File or the Database
     * @param shortLabel Short Label for Pride Experiments
     * @param sampleList List of Samples in the Experiment
     * @param softwareList List of Software in the Experiment
     * @param personList   List of Person Contacts
     * @param sourceFiles  List of Source Files
     * @param provider     The last Software that Provide the File (mzidentMl)
     * @param organizationList List of Organization Contacts
     * @param references       References
     * @param creationDate     Date of File or Experiment Creation
     * @param publicDate       Published Date
     * @param protocol         Experiment Protocol
     */
    public ExperimentMetaData(ParamGroup params, Comparable id, String name, String version, String shortLabel,
                              List<Sample> sampleList, List<Software> softwareList, List<Person> personList,
                              List<SourceFile> sourceFiles, Provider provider, List<Organization> organizationList,
                              List<Reference> references, Date creationDate, Date publicDate,
                              ExperimentProtocol protocol) {
        super(params, id, name);
        this.version          = version;
        this.sampleList       = sampleList;
        this.softwareList     = softwareList;
        this.personList       = personList;
        this.sourceFiles      = sourceFiles;
        this.provider         = provider;
        this.organizationList = organizationList;
        this.references       = references;
        this.creationDate     = creationDate;
        this.publicDate       = publicDate;
        this.protocol         = protocol;
        this.shortLabel       = shortLabel;
    }

    /**
     *
     * @param params       Params Group of Experiment Meta Data
     * @param id           Generic Id of the Experiment
     * @param name         Generic Name of the Experiment
     * @param version      File version
     * @param shortLabel   Short Label of the Experiment (PRIDE XML and PRIDE Database)
     * @param sampleList   Sample List
     * @param softwareList Software List used in the Experiment
     * @param personList   Contact List
     * @param sourceFiles  Source Files related with the Experiment
     * @param provider     Last Software and Contact that Provide the File or Experiment Results (mzidentML)
     * @param organizationList Organization List involve in the Experiment
     * @param references       References related with the Experiments
     * @param creationDate     Creation Date
     * @param publicDate       Publication Date (PRIDE XML)
     * @param protocol         Experiment General Protocol (PRIDE XML)
     * @param spectraDataList  Spectra Data Files related with the Experiment (mzIdentML)
     */
    public ExperimentMetaData(ParamGroup params, Comparable id, String name, String version, String shortLabel,
                              List<Sample> sampleList, List<Software> softwareList, List<Person> personList,
                              List<SourceFile> sourceFiles, Provider provider, List<Organization> organizationList,
                              List<Reference> references, Date creationDate, Date publicDate,
                              ExperimentProtocol protocol,List<SpectraData> spectraDataList) {
        this(params, id, name,version,shortLabel,sampleList,softwareList,personList,sourceFiles,provider,organizationList,references,creationDate,publicDate,protocol);
        this.spectraDataList = spectraDataList;
    }




    /**
     * Get Provider. The last software that generate the final file.
     *
     * @return Provider
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * Set Provider. Set the last software that generate the final file.
     *
     * @param provider Provider
     */
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    /**
     * Get List of Organizations
     *
     * @return List<Organization> List of Organization Contacts
     */
    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    /**
     * Set List of Organization
     *
     * @param organizationList Organization Contact List
     */
    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    /**
     * Get Reference List
     *
     * @return List of References
     */
    public List<Reference> getReferences() {
        return references;
    }

    /**
     * Set the List of References
     *
     * @param references References
     */
    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    /**
     * Get the creation date of the file
     *
     * @return Creation Date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Set the Creation Date
     *
     * @param creationDate Creation Date of the File
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Get the Public Date of the Pride Experiment
     *
     * @return Public Date
     */
    public Date getPublicDate() {
        return publicDate;
    }

    /**
     * Set the Public Date of the Pride Experiment
     *
     * @param publicDate Public Date for Pride Experiment
     */
    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }

    /**
     * Get the Protocol used in the Experiment (Pride Protocol)
     *
     * @return Protocol
     */
    public ExperimentProtocol getProtocol() {
        return protocol;
    }

    /**
     * Set the Protocol used in the Experiment (Pride Protocol)
     *
     * @param protocol Protocol
     */
    public void setProtocol(ExperimentProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Get the Version of the File or the experiment in the Database
     *
     * @return Version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the Version of the File or the experiment in the Database
     *
     * @param version Version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get Sample List
     *
     * @return Sample List
     */
    public List<Sample> getSampleList() {
        return sampleList;
    }

    /**
     * Set Sample List
     *
     * @param sampleList Sample List
     */
    public void setSampleList(List<Sample> sampleList) {
        this.sampleList = sampleList;
    }

    /**
     * Set List of Software
     *
     * @return List of Software
     */
    public List<Software> getSoftwares() {
        return softwareList;
    }

    /**
     * Set List of Software
     *
     * @param softwareList List of Software
     */
    public void setSoftwareList(List<Software> softwareList) {
        this.softwareList = softwareList;
    }

    /**
     * Get a List of Person Contacts
     *
     * @return List of Person Contacts
     */
    public List<Person> getPersonList() {
        return personList;
    }

    /**
     * Set a List of Person Contacts
     *
     * @param personList List of Person Contacts
     */
    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    /**
     * Get a List of Source Files
     *
     * @return Source File List
     */
    public List<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    /**
     * Set a List of Source Files
     *
     * @param sourceFiles Source File List
     */
    public void setSourceFiles(List<SourceFile> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    /**
     * Get the Short Label for Pride Experiments
     * @return Short Label
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * Set the Short Label for Pride Experiments
     * @param shortLabel Short Label
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * For mzidentMl the ParamGroup store additional information
     * @return ParamGroup
     */
    public ParamGroup getAdditional() {
        return new ParamGroup(this.getCvParams(), this.getUserParams());
    }

    /**
     * For Pride Objects the ParamGroup Store the FileContent
     * @return ParamGroup
     */
    public ParamGroup getFileContent() {
        return new ParamGroup(this.getCvParams(), this.getUserParams());
    }

    public List<SpectraData> getSpectraDataList() {
        return spectraDataList;
    }

    public void setSpectraDataList(List<SpectraData> spectraDataList) {
        this.spectraDataList = spectraDataList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExperimentMetaData that = (ExperimentMetaData) o;

        return !(creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) && !(organizationList != null ? !organizationList.equals(that.organizationList) : that.organizationList != null) && !(personList != null ? !personList.equals(that.personList) : that.personList != null) && !(protocol != null ? !protocol.equals(that.protocol) : that.protocol != null) && !(provider != null ? !provider.equals(that.provider) : that.provider != null) && !(publicDate != null ? !publicDate.equals(that.publicDate) : that.publicDate != null) && !(references != null ? !references.equals(that.references) : that.references != null) && !(sampleList != null ? !sampleList.equals(that.sampleList) : that.sampleList != null) && !(shortLabel != null ? !shortLabel.equals(that.shortLabel) : that.shortLabel != null) && !(softwareList != null ? !softwareList.equals(that.softwareList) : that.softwareList != null) && !(sourceFiles != null ? !sourceFiles.equals(that.sourceFiles) : that.sourceFiles != null) && !(spectraDataList != null ? !spectraDataList.equals(that.spectraDataList) : that.spectraDataList != null) && !(version != null ? !version.equals(that.version) : that.version != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (organizationList != null ? organizationList.hashCode() : 0);
        result = 31 * result + (personList != null ? personList.hashCode() : 0);
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        result = 31 * result + (provider != null ? provider.hashCode() : 0);
        result = 31 * result + (publicDate != null ? publicDate.hashCode() : 0);
        result = 31 * result + (references != null ? references.hashCode() : 0);
        result = 31 * result + (sampleList != null ? sampleList.hashCode() : 0);
        result = 31 * result + (shortLabel != null ? shortLabel.hashCode() : 0);
        result = 31 * result + (softwareList != null ? softwareList.hashCode() : 0);
        result = 31 * result + (sourceFiles != null ? sourceFiles.hashCode() : 0);
        result = 31 * result + (spectraDataList != null ? spectraDataList.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}



