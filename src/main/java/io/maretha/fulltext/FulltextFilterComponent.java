// SPDX-License-Identifier: Apache-2.0
package io.maretha.fulltext;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.repository.Repository;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.storage.dbs.DBSRepositoryDescriptor;
import org.nuxeo.ecm.core.storage.dbs.DBSRepositoryService;
import org.nuxeo.ecm.core.storage.mongodb.MongoDBRepositoryDescriptor;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentStartOrders;
import org.nuxeo.runtime.model.DefaultComponent;

/** Swaps in the filtering factory before repositories are created. */
public class FulltextFilterComponent extends DefaultComponent {

    private static final Logger log = LogManager.getLogger(FulltextFilterComponent.class);

    @Override
    public int getApplicationStartedOrder() {
        return ComponentStartOrders.REPOSITORY - 1;
    }

    @Override
    public void start(ComponentContext context) {
        PropertyFulltextFilter filter = PropertyFulltextFilter.fromConfiguration();
        if (filter == null) {
            log.info("{} or {} not set, fulltext filter disabled", PropertyFulltextFilter.PROPERTY_PROP,
                    PropertyFulltextFilter.VALUES_PROP);
            return;
        }
        DBSRepositoryService dbs = Framework.getService(DBSRepositoryService.class);
        RepositoryManager manager = Framework.getService(RepositoryManager.class);
        for (String name : new ArrayList<>(manager.getRepositoryNames())) {
            DBSRepositoryDescriptor desc = dbs.getRepositoryDescriptor(name);
            if (!(desc instanceof MongoDBRepositoryDescriptor)) {
                continue;
            }
            manager.addRepository(new Repository(name, desc.label, desc.isDefault(), desc.isHeadless(),
                    new FilteringRepositoryFactory(name, filter), desc.pool));
            log.info("Fulltext filter on repository {}: {} in {}", name, filter.getKey(), filter.getAllow());
        }
    }
}
