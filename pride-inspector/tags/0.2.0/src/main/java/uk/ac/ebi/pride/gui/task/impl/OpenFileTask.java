package uk.ac.ebi.pride.gui.task.impl;

import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.gui.PrideViewerContext;
import uk.ac.ebi.pride.gui.desktop.Desktop;
import uk.ac.ebi.pride.gui.task.TaskAdapter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 01-Feb-2010
 * Time: 10:37:49
 */
public class OpenFileTask<D extends DataAccessController> extends TaskAdapter<D, String> {

    private File inputFile = null;
    private Class<D> adaptorClass = null;
    private D adaptor = null;

    public OpenFileTask(File inputFile, Class<D> adaptorClass, String name, String description) {
        this.inputFile = inputFile;
        this.adaptorClass = adaptorClass;
        this.setName(name);
        this.setDescription(description);
    }

    @Override
    protected void succeed(DataAccessController result) {
        // register the adapter to DesktopContext
        if (result != null) {
            PrideViewerContext context = ((PrideViewerContext)Desktop.getInstance().getDesktopContext());
            context.getDataAccessMonitor().addDataAccessController(result);
        }
    }

    @Override
    protected void cancelled() {
        releaseResources();
    }

    @Override
    protected void interrupted(InterruptedException iex) {
        releaseResources();
    }

    @Override
    protected D doInBackground() throws Exception {
        // 1. build a DataAccessController instance
        this.publish("Loading " + inputFile.getName());
        List<DataAccessController> controllers =  ((PrideViewerContext)Desktop.getInstance().getDesktopContext()).getDataAccessMonitor().getControllers();
        boolean fileExist = false;
        for (DataAccessController controller : controllers) {
            Object source = controller.getSource();
            if ((source instanceof File) && (inputFile.equals(source))) {
                fileExist = true;
            }
        }
        if (!fileExist) {
            Constructor<D> cstruct = adaptorClass.getDeclaredConstructor(File.class);
            adaptor = cstruct.newInstance(inputFile);
        }
        return adaptor;
    }

    private void releaseResources() {
        if (adaptor != null)
            adaptor.close();
    }
}
