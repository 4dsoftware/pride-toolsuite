package uk.ac.ebi.pride.gui.task.impl;

import org.bushe.swing.event.EventBus;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.component.report.RemovalReportMessage;
import uk.ac.ebi.pride.gui.component.report.SummaryReportMessage;
import uk.ac.ebi.pride.gui.component.startup.ControllerContentPane;
import uk.ac.ebi.pride.gui.component.table.filter.DecoyAccessionFilter;
import uk.ac.ebi.pride.gui.component.table.model.PeptideTableModel;
import uk.ac.ebi.pride.gui.component.table.model.ProteinTableModel;
import uk.ac.ebi.pride.gui.desktop.Desktop;
import uk.ac.ebi.pride.gui.event.SummaryReportEvent;
import uk.ac.ebi.pride.gui.task.TaskAdapter;
import uk.ac.ebi.pride.util.NumberUtilities;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.regex.Pattern;

/**
 * Task to calculate decoy ratio
 *
 * User: rwang
 * Date: 16/09/2011
 * Time: 10:08
 */
public class DecoyRatioTask extends TaskAdapter<Void, Void> {
    private static final String TASK_NAME = "Calculating decoy ratio";
    private static final String TASK_DESCRIPTION = "Calculating decoy ratio for both protein and peptide";

    private DataAccessController controller;
    private DecoyAccessionFilter.Type type;
    private String criteria;

    public DecoyRatioTask(DataAccessController controller, DecoyAccessionFilter.Type type, String criteria) {
        this.controller = controller;
        this.type = type;
        this.criteria = criteria;

        this.setName(TASK_NAME);
        this.setDescription(TASK_DESCRIPTION);
    }

    @Override
    protected Void doInBackground() throws Exception {
        PrideInspectorContext appContext = (PrideInspectorContext) Desktop.getInstance().getDesktopContext();
        // remove previous decoy ratio
        EventBus.publish(new SummaryReportEvent(this, controller, new RemovalReportMessage(Pattern.compile("Decoy.*"))));
        // get content pane
        ControllerContentPane contentPane = (ControllerContentPane) appContext.getDataContentPane(controller);
        // protein tab
        JTable table = contentPane.getProteinTabPane().getIdentificationPane().getIdentificationTable();
        String protAccColName = ProteinTableModel.TableHeader.PROTEIN_ACCESSION_COLUMN.getHeader();
        int index = getAccessionColumnIndex(table.getModel(), protAccColName);
        // protein decoy ratio
        double proteinDecoyRatio = calculateDecoyRatio(table.getModel(), index, type, criteria);
        String proteinDecoyMsg = "Decoy Protein: " + NumberUtilities.scaleDouble(proteinDecoyRatio * 100, 2) + " %";
        EventBus.publish(new SummaryReportEvent(this, controller, new SummaryReportMessage(SummaryReportMessage.Type.INFO, proteinDecoyMsg, proteinDecoyMsg)));

        // peptide tab
        table = contentPane.getPeptideTabPane().getPeptidePane().getPeptideTable();
        protAccColName = PeptideTableModel.TableHeader.PROTEIN_ACCESSION_COLUMN.getHeader();
        index = getAccessionColumnIndex(table.getModel(), protAccColName);
        // peptide decoy ratio
        double peptideDecoyRatio = calculateDecoyRatio(table.getModel(), index, type, criteria);
        String peptideDecoyMsg = "Decoy Peptide: " + NumberUtilities.scaleDouble(peptideDecoyRatio * 100, 2) + " %";
        EventBus.publish(new SummaryReportEvent(this, controller, new SummaryReportMessage(SummaryReportMessage.Type.INFO, peptideDecoyMsg, peptideDecoyMsg)));

        return null;
    }

    /**
     * Get the index of accession column
     *
     * @param tableModel     table model
     * @param protAccColName protein accession column name
     * @return int protein accession column index
     */
    private int getAccessionColumnIndex(TableModel tableModel, String protAccColName) {
        int colCnt = tableModel.getColumnCount();
        for (int i = 0; i < colCnt; i++) {
            if (tableModel.getColumnName(i).equals(protAccColName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calculate decoy ratio
     *
     * @param tableModel table model
     * @param colIndex   protein accession column
     * @param type
     * @param criteria
     * @return double  decoy ratio
     */
    private double calculateDecoyRatio(TableModel tableModel, int colIndex,
                                       DecoyAccessionFilter.Type type, String criteria) {
        double rowCnt = tableModel.getRowCount() + 0d;
        double decoyCnt = 0;

        for (int i = 0; i < rowCnt; i++) {
            String acc = (String) tableModel.getValueAt(i, colIndex);
            if (acc != null) {
                switch (type) {
                    case PREFIX:
                        if (acc.toLowerCase().startsWith(criteria)) {
                            decoyCnt += 1d;
                        }
                        break;
                    case POSTFIX:
                        if (acc.toLowerCase().endsWith(criteria)) {
                            decoyCnt += 1d;
                        }
                        break;
                    case CONTAIN:
                        if (acc.toLowerCase().contains(criteria)) {
                            decoyCnt += 1d;
                        }
                        break;
                }
            }
        }

        return decoyCnt / rowCnt;
    }
}