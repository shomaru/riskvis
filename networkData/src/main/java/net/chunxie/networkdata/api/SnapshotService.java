package net.chunxie.networkdata.api;

import net.chunxie.networkdata.entity.Forum;
import net.chunxie.networkdata.entity.ForumMessage;
import net.chunxie.networkdata.entity.ForumThread;
import net.chunxie.networkdata.entity.Snapshot;

import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:26
 */
public interface SnapshotService {

    public Snapshot buildSnapshot(Snapshot s);

    public List<Forum> findAllForums();

    public List<ForumThread> findSnapshotThreads(Snapshot s);

    public List<ForumMessage> findSnapshotMessagesWithinThread(Snapshot s, ForumThread t);
}
