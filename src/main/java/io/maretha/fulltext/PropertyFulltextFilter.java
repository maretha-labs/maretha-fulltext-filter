// SPDX-License-Identifier: Apache-2.0
package io.maretha.fulltext;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.nuxeo.ecm.core.storage.dbs.DBSDocumentState;
import org.nuxeo.runtime.api.Framework;

/** Limits fulltext extraction to documents whose configured property value is allowlisted. */
public class PropertyFulltextFilter {

    public static final String PROPERTY_PROP = "maretha.fulltext.filter.property";

    public static final String VALUES_PROP = "maretha.fulltext.filter.values";

    protected final String key;

    protected final Set<String> allow;

    public PropertyFulltextFilter(String key, Set<String> allow) {
        this.key = key;
        this.allow = Set.copyOf(allow);
    }

    /** Null when not configured. */
    public static PropertyFulltextFilter fromConfiguration() {
        String key = Framework.getProperty(PROPERTY_PROP, "").trim();
        Set<String> allow = parse(Framework.getProperty(VALUES_PROP, ""));
        return key.isEmpty() || allow.isEmpty() ? null : new PropertyFulltextFilter(key, allow);
    }

    static Set<String> parse(String value) {
        return Arrays.stream(value.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
    }

    public String getKey() {
        return key;
    }

    public Set<String> getAllow() {
        return allow;
    }

    public void apply(Collection<DBSDocumentState> states, Set<String> dirtyStrings, Set<String> dirtyBinaries) {
        for (DBSDocumentState state : states) {
            String id = state.getId();
            Serializable value = state.get(key);
            if (value == null || !allow.contains(value)) {
                dirtyStrings.remove(id);
                dirtyBinaries.remove(id);
            } else if (!Objects.equals(value, state.getOriginalState().get(key))) {
                // moved into the allowlist without a blob change
                dirtyBinaries.add(id);
            }
        }
    }
}
