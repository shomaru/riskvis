package net.chunxie.pgsqldataimpl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 14:03
 */
@OptionsPanelController.SubRegistration(location = "Advanced",
        displayName = "#AdvancedOption_DisplayName_DataSourceConfiguration",
        keywords = "#AdvancedOption_Keywords_DataSourceConfiguration",
        keywordsCategory = "Advanced/DataSourceConfiguration")
@Messages({
        "AdvancedOption_DisplayName_DataSourceConfiguration=Data Source Configuration",
        "AdvancedOption_Keywords_DataSourceConfiguration=data, source, config",
})
public final class DataSourceConfigurationOptionsPanelController extends OptionsPanelController {

    private DataSourceConfigurationPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    public void update() {
        getPanel().load();
        changed = false;
    }

    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    public boolean isValid() {
        return getPanel().valid();
    }

    public boolean isChanged() {
        return changed;
    }

    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private DataSourceConfigurationPanel getPanel() {
        if (panel == null) {
            panel = new DataSourceConfigurationPanel(this);
        }
        return panel;
    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}