package net.chunxie.networkgraph.util;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:25
 */
public class LayoutComboCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String valueString = value.toString();
        valueString = valueString.substring(valueString.lastIndexOf('.') + 1);
        return super.getListCellRendererComponent(list, valueString, index, isSelected,
                cellHasFocus);
    }
}