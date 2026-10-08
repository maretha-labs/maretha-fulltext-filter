// SPDX-License-Identifier: Apache-2.0
package io.maretha.fulltext;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.storage.dbs.DBSRepository;
import org.nuxeo.ecm.core.storage.dbs.DBSSession;
import org.nuxeo.ecm.core.storage.dbs.DBSTransactionState;

public class FilteringDBSSession extends DBSSession {

    public FilteringDBSSession(DBSRepository repository, PropertyFulltextFilter filter) {
        super(repository);
        DBSTransactionState stock = transaction;
        try {
            // transaction is final in DBSSession
            FieldUtils.writeField(this, "transaction", new FilteringTransactionState(repository, this, filter), true);
        } catch (IllegalAccessException e) {
            throw new NuxeoException("Cannot install fulltext filter", e);
        }
        stock.close();
    }
}
