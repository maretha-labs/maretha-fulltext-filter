// SPDX-License-Identifier: Apache-2.0
package io.maretha.fulltext;

import org.nuxeo.ecm.core.storage.dbs.DBSRepository;
import org.nuxeo.ecm.core.storage.dbs.DBSRepositoryDescriptor;
import org.nuxeo.ecm.core.storage.dbs.DBSRepositoryFactory;
import org.nuxeo.ecm.core.storage.mongodb.MongoDBRepositoryDescriptor;

/** Mirrors the stock MongoDB factory plus caching wrapper. */
public class FilteringRepositoryFactory extends DBSRepositoryFactory {

    protected final PropertyFulltextFilter filter;

    public FilteringRepositoryFactory(String repositoryName, PropertyFulltextFilter filter) {
        super(repositoryName);
        this.filter = filter;
    }

    @Override
    public Object call() {
        DBSRepositoryDescriptor descriptor = getRepositoryDescriptor();
        DBSRepository repository = new FilteringMongoDBRepository((MongoDBRepositoryDescriptor) descriptor, filter);
        return descriptor.isCacheEnabled() ? new FilteringCachingRepository(repository, descriptor, filter)
                : repository;
    }
}
