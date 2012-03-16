package uk.ac.ebi.pride.data.controller.cache.impl;

import uk.ac.ebi.pride.data.controller.cache.CacheCategory;
import uk.ac.ebi.pride.data.controller.impl.ControllerImpl.MzMLControllerImpl;
import uk.ac.ebi.pride.data.io.file.MzMLUnmarshallerAdaptor;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: yperez
 * Date: 3/13/12
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class MzMlCacheBuilder extends AbstractAccessCacheBuilder{

     public MzMlCacheBuilder(MzMLControllerImpl c) {
        super(c);
    }

    /**
     * For the moment, MzXmlCacheBuilder only caches spectrum ids and chromatogram ids.
     *
     * @throws Exception error while caching the ids.
     */
    @Override
    public void populate() throws Exception {
        super.populate();

        // get a direct reference to unmarshaller
        MzMLUnmarshallerAdaptor unmarshaller = ((MzMLControllerImpl) controller).getUnmarshaller();

        // clear and add spectrum ids
        cache.clear(CacheCategory.SPECTRUM_ID);
        cache.storeInBatch(CacheCategory.SPECTRUM_ID, new ArrayList<Comparable>(unmarshaller.getSpectrumIds()));

        // clear and add chromatograms ids
        cache.clear(CacheCategory.CHROMATOGRAM_ID);
        cache.storeInBatch(CacheCategory.CHROMATOGRAM_ID, new ArrayList<Comparable>(unmarshaller.getChromatogramIds()));
    }
}
