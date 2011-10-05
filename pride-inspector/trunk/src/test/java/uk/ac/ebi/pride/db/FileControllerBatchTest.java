package uk.ac.ebi.pride.db;

import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.impl.MzMLControllerImpl;
import uk.ac.ebi.pride.data.controller.impl.PrideXmlControllerImpl;
import uk.ac.ebi.pride.gui.component.table.TableDataRetriever;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Note: this is NOT a unit test, it will have to be run manually.
 * <p/>
 * User: rwang
 * Date: 10-Dec-2010
 * Time: 14:51:26
 */
public class FileControllerBatchTest {

    /**
     * Two input parameters:
     * 1. A input file or folder
     * 2. Reading option: currently either PRIDE XML or mzML
     * @param args  input parameters
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Please make sure you have input the options: \n java -jar pride-inspector.jar [input file or folder] [1 for PRIDE XML, 2 for mzML] [output file]");
            System.exit(1);
        }

        // The input folder which contains a list of files
        File inputFile = new File(args[0]);

        // DataAccessController options
        int option = Integer.parseInt(args[1]);

        // output file
        File outputFile = new File(args[2]);

        PrintWriter writer =  null;
        try {
            writer = new PrintWriter(new FileWriter(outputFile));

            if (inputFile.isDirectory()) {
                visitAllFiles(inputFile, option, writer);
            } else {
                readFile(inputFile, option, writer);
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Travsit all the files with the given folder, including the files within the sub-folders.
     *
     * @param folder    input folder
     * @param option    reading option
     * @param writer    PrintWriter
     */
    private static void visitAllFiles(File folder, int option, PrintWriter writer) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                visitAllFiles(file, option, writer);
            } else {
                readFile(file, option, writer);
            }
        }
    }


    /**
     * Read a file using the given option
     * @param file  file to read
     * @param option    indicates with access controller to use
     * @param writer    PrintWriter
     */
    private static void readFile(File file, int option, PrintWriter writer) {
        try {
            DataAccessController controller = null;
            switch (option) {
                case 1:
                    if (PrideXmlControllerImpl.isValidFormat(file)) {
                        controller = new PrideXmlControllerImpl(file);
                    }
                    break;
                case 2:
                    if (MzMLControllerImpl.isValidFormat(file)) {
                        controller = new MzMLControllerImpl(file);
                    }
                    break;
            }

            if (controller != null) {
                writer.println("File: " + file.getAbsolutePath());
                // read the meta data
                controller.getMetaData();

                // iterate over 50 spectra
                if (controller.hasSpectrum()) {
                    Collection<Comparable> ids = controller.getSpectrumIds();
                    int cnt = 0;
                    for (Comparable id : ids) {
                        // read the spectrum object
                        controller.getSpectrumById(id);
                        cnt ++;
                        if (cnt >= 50) {
                            break;
                        }
                    }
                }

                // iterate over 50 chromatograms
                if (controller.hasChromatogram()) {
                    Collection<Comparable> ids = controller.getChromatogramIds();
                    int cnt = 0;
                    for (Comparable id : ids) {
                        controller.getChromatogramById(id);
                        cnt ++;
                        if (cnt >= 50) {
                            break;
                        }
                    }
                }

                // iterate over 50 identifications
                if (controller.hasIdentification()) {
                    Collection<Comparable> ids = controller.getIdentificationIds();
                    int cnt = 0;
                    for (Comparable id : ids) {
                        controller.getIdentificationById(id);
                        // read identification details
                        TableDataRetriever.getProteinTableRow(controller, id);
                        // read peptide details
                        Collection<Comparable> pepIds = controller.getPeptideIds(id);
                        for (Comparable pepId : pepIds) {
                            TableDataRetriever.getPeptideTableRow(controller, id, pepId);
                        }
                        cnt ++;
                        if (cnt >= 50) {
                            break;
                        }
                    }
                }

                // close the data access controller
                controller.close();
                writer.println("-------------------------------------------------------------------------------");
                writer.flush();
            }
        } catch (Exception ex) {
            StackTraceElement[] traces = ex.getStackTrace();
            for (StackTraceElement trace : traces) {
                writer.println(trace.toString());
            }
            writer.println("-------------------------------------------------------------------------------");
            writer.flush();
        }
    }
}