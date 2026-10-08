// SPDX-License-Identifier: Apache-2.0
package io.maretha.fulltext;

import org.nuxeo.ecm.core.model.Session;
import org.nuxeo.ecm.core.storage.mongodb.MongoDBRepository;
import org.nuxeo.ecm.core.storage.mongodb.MongoDBRepositoryDescriptor;

public class FilteringMongoDBRepository extends MongoDBRepository {

    protected final PropertyFulltextFilter filter;

    public FilteringMongoDBRepository(MongoDBRepositoryDescriptor descriptor, PropertyFulltextFilter filter) {
        super(descriptor);
        this.filter = filter;
    }

    @Override
    public Session getSession() {
        return new FilteringDBSSession(this, filter);
    }
}
