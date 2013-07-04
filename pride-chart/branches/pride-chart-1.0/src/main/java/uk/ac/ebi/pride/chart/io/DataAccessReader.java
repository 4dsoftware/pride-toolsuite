package uk.ac.ebi.pride.chart.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.pride.chart.PrideChartType;
import uk.ac.ebi.pride.chart.dataset.*;
import uk.ac.ebi.pride.chart.utils.PridePlotUtils;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessUtilities;
import uk.ac.ebi.pride.data.core.Modification;
import uk.ac.ebi.pride.data.core.Peptide;
import uk.ac.ebi.pride.data.core.Protein;
import uk.ac.ebi.pride.data.core.Spectrum;
import uk.ac.ebi.pride.mol.MoleculeUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;


/**
 * User: Qingwei
 * Date: 12/06/13
 */
public class DataAccessReader extends PrideDataReader {
    private static Logger logger = Logger.getLogger(DataAccessReader.class);

    private static final int DELTA_BIN_COUNT = 200;
    private static final double DELTA_MIN_BIN_WIDTH = 0.0005;
    private static final double PRE_MIN_BIN_WIDTH = 100;

    private String source = "DataAccessController";
    private DataAccessController controller;

    private boolean noPeptide = true;
    private boolean noSpectra = true;
    private boolean noTandemSpectra = true;

    private List<Double> deltaDomain = new ArrayList<Double>();
    private List<PrideData> deltaRange = new ArrayList<PrideData>();

    private Double[] peptidesDomain = new Double[6];
    private PrideData[] peptidesRange = new PrideData[6];

    private Double[] missedDomain = new Double[5];
    private PrideData[] missedRange = new PrideData[5];

    private List<Double> avgDomain = new ArrayList<Double>();
    private List<PrideData> avgRange = new ArrayList<PrideData>();

    private Double[] preChargeDomain = new Double[8];
    private PrideData[] preChargeRange = new PrideData[8];

    private List<Double> preMassesDomain = new ArrayList<Double>();
    private List<PrideData> preMassesRange = new ArrayList<PrideData>();

    public DataAccessReader(DataAccessController controller) throws PrideDataException {
        if (controller == null) {
            throw new NullPointerException(source + " is null!");
        }
        this.controller = controller;

        readData();
    }

    @Override
    protected void start() {
        // do noting.
    }

    private Double calcDeltaMZ(Peptide peptide) {
        List<Double> modMassList = new ArrayList<Double>();
        for (Modification mod  : peptide.getModifications()) {
            modMassList.add(mod.getMonoisotopicMassDelta().get(0));
        }

        return MoleculeUtilities.calculateDeltaMz(
                peptide.getSequence(),
                peptide.getPrecursorMz(),
                peptide.getPrecursorCharge(),
                modMassList
        );
    }

    private int calcMissedCleavages(Peptide peptide) {
        String sequence = peptide.getSequence();
        //Always remove the last K or R from sequence
        sequence = sequence.replaceAll("[K|R]$", "");

        //We assume the hypothesis KR|P
        sequence = sequence.replaceAll("[K|R]P","");
        int initialLength = sequence.length();

        sequence = sequence.replaceAll("[K|R]","");
        return initialLength - sequence.length();
    }

//    private double calcAverageMZ(Spectrum spectrum) {
//        double[] dataList = spectrum.getMzBinaryDataArray().getDoubleArray();
//        if (dataList == null || dataList.length == 0) {
//            return 0;
//        }
//
//        double sum = 0;
//        for (double v : dataList) {
//            sum += v;
//        }
//
//        return sum / dataList.length;
//    }

    private void readDelta(List<PrideData> deltaMZList) {
        if (noPeptide) {
            errorMap.put(PrideChartType.DELTA_MASS, new PrideDataException(PrideDataException.NO_PEPTIDE));
            return;
        }

        PrideDataType dataType = PrideDataType.IDENTIFIED_SPECTRA;

        PrideEqualWidthHistogramDataSource dataSource = new PrideEqualWidthHistogramDataSource(
                deltaMZList.toArray(new PrideData[deltaMZList.size()]),
                false
        );
        double start = Double.MAX_VALUE;
        double end = Double.MIN_VALUE;
        double v;
        for (PrideData data : deltaMZList) {
            v = data.getData();
            if (v < start) {
                start = v;
            }
            if (v > end) {
                end = v;
            }
        }
        double binWidth = (end - start) / DELTA_BIN_COUNT;
        binWidth = binWidth < DELTA_MIN_BIN_WIDTH ? DELTA_MIN_BIN_WIDTH : binWidth;
        dataSource.appendBins(dataSource.generateBins(-DELTA_BIN_COUNT * binWidth, binWidth, DELTA_BIN_COUNT * 2));

        SortedMap<PrideDataType, SortedMap<PrideHistogramBin, Integer>> histogramMap = dataSource.getHistogramMap();
        SortedMap<PrideHistogramBin, Integer> histogram;
        int maxFreq = 0;
        histogram = histogramMap.get(PrideDataType.IDENTIFIED_SPECTRA);
        for (Integer size : histogram.values()) {
            if (size > maxFreq) {
                maxFreq = size;
            }
        }

        for (PrideHistogramBin bin : histogram.keySet()) {
            deltaDomain.add(bin.getStartBoundary());
            deltaRange.add(new PrideData(histogram.get(bin) * 1.0d / maxFreq, dataType));
        }

        for (int i = 0; i < deltaRange.size(); i++) {
            if (deltaRange.get(i).getData() == null) {
                System.out.println(i);
            }
        }

        xyDataSourceMap.put(PrideChartType.DELTA_MASS, new PrideXYDataSource(
                deltaDomain.toArray(new Double[deltaDomain.size()]),
                deltaRange.toArray(new PrideData[deltaRange.size()]),
                PrideDataType.ALL_SPECTRA
        ));
    }

    private void readPeptide(int[] peptideBars) {
        if (noPeptide) {
            errorMap.put(PrideChartType.PEPTIDES_PROTEIN, new PrideDataException(PrideDataException.NO_PEPTIDE));
            return;
        }

        for (int i = 0; i < peptideBars.length; i++) {
            peptidesRange[i] = new PrideData(peptideBars[i] + 0.0, PrideDataType.ALL);
        }

        xyDataSourceMap.put(PrideChartType.PEPTIDES_PROTEIN, new PrideXYDataSource(
                peptidesDomain,
                peptidesRange,
                PrideDataType.ALL
        ));
    }

    private void readMissed(int[] missedBars) {
        if (noPeptide) {
            errorMap.put(PrideChartType.MISSED_CLEAVAGES, new PrideDataException(PrideDataException.NO_PEPTIDE));
            return;
        }

        for (int i = 0; i < missedBars.length; i++) {
            missedRange[i] = new PrideData(missedBars[i] + 0.0, PrideDataType.ALL_SPECTRA);
        }

        xyDataSourceMap.put(PrideChartType.MISSED_CLEAVAGES, new PrideXYDataSource(
                missedDomain,
                missedRange,
                PrideDataType.ALL_SPECTRA
        ));
    }

    private void readAvg(PrideSpectrumHistogram dataSource) {
        if (noTandemSpectra) {
            errorMap.put(PrideChartType.AVERAGE_MS, new PrideDataException(PrideDataException.NO_TANDEM_SPECTRA));
            return;
        }

        dataSource.appendBins(dataSource.generateBins(0, 1));
        SortedMap<PrideDataType, SortedMap<PrideHistogramBin, Double>> histogramMap = dataSource.getIntensityMap();

        SortedMap<PrideHistogramBin, Double> histogram;
        for (PrideDataType dataType : histogramMap.keySet()) {
            histogram = histogramMap.get(dataType);
            for (PrideHistogramBin bin : histogram.keySet()) {
                avgDomain.add(bin.getStartBoundary());
                avgRange.add(new PrideData(histogram.get(bin), dataType));
            }
        }

        xyDataSourceMap.put(PrideChartType.AVERAGE_MS, new PrideXYDataSource(
                avgDomain.toArray(new Double[avgDomain.size()]),
                avgRange.toArray(new PrideData[avgRange.size()]),
                PrideDataType.ALL_SPECTRA
        ));
    }

    private void readPreCharge(int[] preChargeBars) {
        if (noSpectra) {
            errorMap.put(PrideChartType.PRECURSOR_CHARGE, new PrideDataException(PrideDataException.NO_SPECTRA));
            return;
        }

        for (int i = 0; i < preChargeBars.length; i++) {
            preChargeRange[i] = new PrideData(preChargeBars[i] + 0.0, PrideDataType.IDENTIFIED_SPECTRA);
        }

        xyDataSourceMap.put(PrideChartType.PRECURSOR_CHARGE, new PrideXYDataSource(
                preChargeDomain,
                preChargeRange,
                PrideDataType.IDENTIFIED_SPECTRA
        ));
    }

    private void readPreMasses(List<PrideData> preMassedList) {
        if (noSpectra) {
            errorMap.put(PrideChartType.PRECURSOR_MASSES, new PrideDataException(PrideDataException.NO_SPECTRA));
            return;
        }

        PrideEqualWidthHistogramDataSource dataSource = new PrideEqualWidthHistogramDataSource(
                preMassedList.toArray(new PrideData[preMassedList.size()]),
                true
        );
        dataSource.appendBins(dataSource.generateBins(0d, PRE_MIN_BIN_WIDTH));

        SortedMap<PrideDataType, SortedMap<PrideHistogramBin, Integer>> histogramMap = dataSource.getHistogramMap();
        SortedMap<PrideHistogramBin, Integer> idHistogram = histogramMap.get(PrideDataType.IDENTIFIED_SPECTRA);
        SortedMap<PrideHistogramBin, Integer> unHistogram = histogramMap.get(PrideDataType.UNIDENTIFIED_SPECTRA);
        SortedMap<PrideHistogramBin, Integer> allHistogram = histogramMap.get(PrideDataType.ALL_SPECTRA);

        int identifiedCount = 0;
        if (idHistogram != null) {
            for (PrideHistogramBin bin : idHistogram.keySet()) {
                identifiedCount += idHistogram.get(bin);
            }
            for (PrideHistogramBin bin : idHistogram.keySet()) {
                preMassesDomain.add(bin.getStartBoundary());
                preMassesRange.add(new PrideData(
                        idHistogram.get(bin) * 1.0d / identifiedCount,
                        PrideDataType.IDENTIFIED_SPECTRA
                ));
            }
        }

        int unidentifiedCount = 0;
        if (unHistogram != null) {
            for (PrideHistogramBin bin : unHistogram.keySet()) {
                unidentifiedCount += unHistogram.get(bin);
            }
            for (PrideHistogramBin bin : unHistogram.keySet()) {
                preMassesDomain.add(bin.getStartBoundary());
                preMassesRange.add(new PrideData(
                        unHistogram.get(bin) * 1.0d / unidentifiedCount,
                        PrideDataType.UNIDENTIFIED_SPECTRA
                ));
            }
        }

        int allCount = identifiedCount + unidentifiedCount;
        if (allCount != 0) {
            for (PrideHistogramBin bin : allHistogram.keySet()) {
                preMassesDomain.add(bin.getStartBoundary());
                preMassesRange.add(new PrideData(
                        allHistogram.get(bin) * 1.0d / allCount,
                        PrideDataType.ALL_SPECTRA
                ));
            }
        }

        xyDataSourceMap.put(PrideChartType.PRECURSOR_MASSES, new PrideXYDataSource(
                preMassesDomain.toArray(new Double[preMassesDomain.size()]),
                preMassesRange.toArray(new PrideData[preMassesRange.size()]),
                PrideDataType.ALL_SPECTRA
        ));
    }

    private void readPeakMS(List<PrideData> peaksMSList) {
        if (noTandemSpectra) {
            errorMap.put(PrideChartType.PEAKS_MS, new PrideDataException(PrideDataException.NO_TANDEM_SPECTRA));
            return;
        }

        PrideEqualWidthHistogramDataSource dataSource = new PrideEqualWidthHistogramDataSource(
                peaksMSList.toArray(new PrideData[peaksMSList.size()]),
                false
        );
        dataSource.appendBins(dataSource.generateGranularityBins(0d, 10, 50));

        histogramDataSourceMap.put(PrideChartType.PEAKS_MS, dataSource);
    }

    private void readPeakIntensity(List<PrideData> peaksIntensityList) {
        if (noTandemSpectra) {
            errorMap.put(PrideChartType.PEAK_INTENSITY, new PrideDataException(PrideDataException.NO_TANDEM_SPECTRA));
            return;
        }

        PrideHistogramDataSource dataSource = new PrideHistogramDataSource(
                peaksIntensityList.toArray(new PrideData[peaksIntensityList.size()]),
                true
        );
        dataSource.appendBin(new PrideHistogramBin(0, 10));
        dataSource.appendBin(new PrideHistogramBin(10, 100));
        dataSource.appendBin(new PrideHistogramBin(100, 1000));
        dataSource.appendBin(new PrideHistogramBin(1000, 10000));
        dataSource.appendBin(new PrideHistogramBin(10000, Integer.MAX_VALUE));

        histogramDataSourceMap.put(PrideChartType.PEAK_INTENSITY, dataSource);
    }

    @Override
    protected void reading() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 6; i++) {
            peptidesDomain[i] = i + 1.0;
        }

        for (int i = 0; i < 5; i++) {
            missedDomain[i] = i + 0.0;
        }

        for (int i = 0; i < 8; i++) {
            preChargeDomain[i] = i + 1.0;
        }

        int[] peptideBars = new int[6];
        int[] missedBars = new int[5];
        int[] preChargeBars = new int[8];

        List<PrideData> deltaMZList = new ArrayList<PrideData>();
        List<PrideData> preMassedList = new ArrayList<PrideData>();

        Protein protein;
        List<Peptide> peptideList;
        for (Comparable proteinId : controller.getProteinIds()) {
            protein = controller.getProteinById(proteinId);
            peptideList = protein.getPeptides();
            // fill peptides per protein
            int size = peptideList.size();
            if (size < 6) {
                peptideBars[size - 1]++;
            } else {
                peptideBars[5]++;
            }

            int missedCleavages;
            Double deltaMZ;
            for (Peptide peptide : peptideList) {
                noPeptide = false;
                peptideSize++;

                // fill delta m/z histogram.
                deltaMZ = calcDeltaMZ(peptide);
                if (deltaMZ != null) {
                    deltaMZList.add(new PrideData(deltaMZ, PrideDataType.IDENTIFIED_SPECTRA));
                }

                // fill missed cleavages
                missedCleavages = calcMissedCleavages(peptide);
                if (missedCleavages < 4) {
                    missedBars[missedCleavages]++;
                } else {
                    missedBars[4]++;
                }
            }

        }

        // spectrum level statistics.
        Integer preCharge;
        Double preMZ;
        Peptide peptide;
        Spectrum spectrum;
        List<PrideData> peaksMSList = new ArrayList<PrideData>();
        List<PrideData> peaksIntensityList = new ArrayList<PrideData>();
        PrideSpectrumHistogram avgHistogram = new PrideSpectrumHistogram(true);

        PrideDataType dataType;
        for (Comparable spectrumId : controller.getSpectrumIds()) {
            noSpectra = false;
            spectrum = controller.getSpectrumById(spectrumId);
            peptide = spectrum.getPeptide();

            // precursor charge and mass.
            if (peptide == null) {
                preCharge = DataAccessUtilities.getPrecursorCharge(spectrum.getPrecursors());
                preMZ = DataAccessUtilities.getPrecursorMz(spectrum);
            } else {
                preCharge = peptide.getPrecursorCharge();
                preMZ = peptide.getPrecursorMz();
            }

            if (preCharge != null && controller.isIdentifiedSpectrum(spectrumId)) {
                // Identified spectrum.
                preChargeBars[preCharge - 1]++;
            }

            if (preMZ != null && preCharge != null) {
                preMassedList.add(new PrideData(
                        preMZ * preCharge,
                        controller.isIdentifiedSpectrum(spectrumId) ? PrideDataType.IDENTIFIED_SPECTRA : PrideDataType.UNIDENTIFIED_SPECTRA
                ));
            }

            if (controller.isIdentifiedSpectrum(spectrumId)) {
                identifiedSpectraSize++;
                dataType = PrideDataType.IDENTIFIED_SPECTRA;
            } else {
                unidentifiedSpectraSize++;
                dataType = PrideDataType.UNIDENTIFIED_SPECTRA;
            }

            if (controller.getSpectrumMsLevel(spectrumId) == 2) {
                noTandemSpectra = false;
                peaksMSList.add(new PrideData(spectrum.getMzBinaryDataArray().getDoubleArray().length + 0.0d, PrideDataType.ALL_SPECTRA)) ;
                avgHistogram.addSpectrum(spectrum, dataType);

                for (double v : spectrum.getIntensityBinaryDataArray().getDoubleArray()) {
                    peaksIntensityList.add(new PrideData(v, dataType));
                }
            }
        }
        logger.debug("fill data cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

        // release memory.
        controller = null;

        start = System.currentTimeMillis();
        readPeptide(peptideBars);
        logger.debug("create peptide data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

        start = System.currentTimeMillis();
        readDelta(deltaMZList);
        logger.debug("create delta mz data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

        start = System.currentTimeMillis();
        readMissed(missedBars);
        logger.debug("create missed cleavages data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

        start = System.currentTimeMillis();
        readPreCharge(preChargeBars);
        logger.debug("create precursor charge data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

        start = System.currentTimeMillis();
        readPreMasses(preMassedList);
        logger.debug("create precursor masses data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

        start = System.currentTimeMillis();
        readAvg(avgHistogram);
        logger.debug("create average ms/ms data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

        start = System.currentTimeMillis();
        readPeakMS(peaksMSList);
        logger.debug("create peaks per ms/ms data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

        start = System.currentTimeMillis();
        readPeakIntensity(peaksIntensityList);
        logger.debug("create peak intensity data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));

//        start = System.currentTimeMillis();
//        readPeptide(peptideBars);
//        readDelta(deltaMZList);
//        readMissed(missedBars);
//
//        readPreCharge(preChargeBars);
//        readPreMasses(preMassedList);
//
//        readAvg(avgHistogram);
//        readPeakMS(peaksMSList);
//        readPeakIntensity(peaksIntensityList);
//        logger.debug("create data set cost: " + PridePlotUtils.getTimeCost(start, System.currentTimeMillis()));
    }

    @Override
    protected void end() {
        // do nothing.
    }
}
