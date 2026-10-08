// SPDX-License-Identifier: Apache-2.0
package io.maretha.fulltext;

import java.util.Set;

import org.nuxeo.ecm.core.storage.dbs.DBSRepository;
import org.nuxeo.ecm.core.storage.dbs.DBSSession;
import org.nuxeo.ecm.core.storage.dbs.DBSTransactionState;

public class FilteringTransactionState extends DBSTransactionState {

    protected final PropertyFulltextFilter filter;

    public FilteringTransactionState(DBSRepository repository, DBSSession session, PropertyFulltextFilter filter) {
        super(repository, session);
        this.filter = filter;
    }

    @Override
    protected void findDirtyDocuments(Set<String> docsWithDirtyStrings, Set<String> docsWithDirtyBinaries) {
        super.findDirtyDocuments(docsWithDirtyStrings, docsWithDirtyBinaries);
        filter.apply(transientStates.values(), docsWithDirtyStrings, docsWithDirtyBinaries);
    }
}
