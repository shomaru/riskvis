package net.chunxie.snapshotexplorer;

import net.chunxie.networkdata.api.SnapshotService;
import net.chunxie.networkdata.entity.Forum;

import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:14
 */
public class ForumComboBoxModel extends AbstractListModel implements ComboBoxModel {

    List<Forum> allForums = null;
    Forum selection = null;

    public ForumComboBoxModel(SnapshotService service) {
        allForums = service.findAllForums();
    }

    @Override
    public int getSize() {
        return allForums.size();
    }

    @Override
    public Object getElementAt(int index) {
        return allForums.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selection = (Forum) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}