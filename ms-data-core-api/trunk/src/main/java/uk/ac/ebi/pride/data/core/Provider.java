package uk.ac.ebi.pride.data.core;

/**
 * Provider object.
 * <p/>
 * In mzIdentML 1.1.0., the following the description of this object:
 * <p/>
 * The Provider of the mzIdentML record in terms of the contact and software.
 * This object contains a reference to the last software used to generate the file
 * this software is called the provider.
 * <p/>
 * User: yperez
 * Date: 04/08/11
 * Time: 11:11
 */
public class Provider extends Identifiable {

    /**
     * A reference to the Contact person that provide the mzIdentMl File.
     * (mzIndetMl description: When a ContactRole is used, it specifies which Contact the role is associated with.
     */
    private AbstractContact contact = null;

    /*
     * Role in CvParam
     */
    private CvParam role = null;

    /**
     * The Software that produced the document instance. mzIdentML
     */
    private Software software = null;

    /**
     * @param id
     * @param name
     * @param software
     * @param contact
     * @param role
     */
    public Provider(Comparable id, String name, Software software, AbstractContact contact, CvParam role) {
        super(id, name);
        this.software = software;
        this.contact  = contact;
        this.role     = role;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public AbstractContact getContact() {
        return contact;
    }

    public void setContact(AbstractContact contact) {
        this.contact = contact;
    }

    public CvParam getRole() {
        return role;
    }

    public void setRole(CvParam role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Provider provider = (Provider) o;

        if (contact != null ? !contact.equals(provider.contact) : provider.contact != null) return false;
        if (role != null ? !role.equals(provider.role) : provider.role != null) return false;
        if (software != null ? !software.equals(provider.software) : provider.software != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = contact != null ? contact.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (software != null ? software.hashCode() : 0);
        return result;
    }
}



