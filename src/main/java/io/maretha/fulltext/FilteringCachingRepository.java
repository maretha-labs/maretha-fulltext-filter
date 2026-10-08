// SPDX-License-Identifier: Apache-2.0
package io.maretha.fulltext;

import org.nuxeo.ecm.core.model.Session;
import org.nuxeo.ecm.core.storage.dbs.DBSCachingRepository;
import org.nuxeo.ecm.core.storage.dbs.DBSRepository;
import org.nuxeo.ecm.core.storage.dbs.DBSRepositoryDescriptor;

public class FilteringCachingRepository extends DBSCachingRepository {

    protected final PropertyFulltextFilter filter;

    public FilteringCachingRepository(DBSRepository repository, DBSRepositoryDescriptor descriptor,
            PropertyFulltextFilter filter) {
        super(repository, descriptor);
        this.filter = filter;
    }

    @Override
    public Session getSession() {
        return new FilteringDBSSession(this, filter);
    }
}
