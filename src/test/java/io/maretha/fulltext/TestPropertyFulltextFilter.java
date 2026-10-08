// SPDX-License-Identifier: Apache-2.0
package io.maretha.fulltext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.nuxeo.ecm.core.storage.State;
import org.nuxeo.ecm.core.storage.dbs.DBSDocumentState;

public class TestPropertyFulltextFilter {

    static final String KEY = "test:category";

    static final PropertyFulltextFilter FILTER = new PropertyFulltextFilter(KEY, Set.of("TypeA"));

    static DBSDocumentState doc(String value) {
        State state = new State();
        state.put("ecm:id", "1");
        if (value != null) {
            state.put(KEY, value);
        }
        return new DBSDocumentState(state);
    }

    static Set<String> dirty() {
        return new HashSet<>(Set.of("1"));
    }

    @Test
    public void testNotAllowlistedSkipped() {
        Set<String> strings = dirty();
        Set<String> binaries = dirty();
        FILTER.apply(List.of(doc("TypeB")), strings, binaries);
        assertTrue(strings.isEmpty());
        assertTrue(binaries.isEmpty());
    }

    @Test
    public void testMissingValueSkipped() {
        Set<String> binaries = dirty();
        FILTER.apply(List.of(doc(null)), dirty(), binaries);
        assertTrue(binaries.isEmpty());
    }

    @Test
    public void testAllowlistedKept() {
        Set<String> strings = dirty();
        Set<String> binaries = dirty();
        FILTER.apply(List.of(doc("TypeA")), strings, binaries);
        assertEquals(Set.of("1"), strings);
        assertEquals(Set.of("1"), binaries);
    }

    @Test
    public void testMovedIntoAllowlistExtracted() {
        DBSDocumentState state = doc("TypeB");
        state.markDirty();
        state.put(KEY, "TypeA");
        Set<String> binaries = new HashSet<>();
        FILTER.apply(List.of(state), new HashSet<>(), binaries);
        assertEquals(Set.of("1"), binaries);
    }

    @Test
    public void testParse() {
        assertEquals(Set.of("A", "B"), PropertyFulltextFilter.parse(" A, B ,,"));
        assertTrue(PropertyFulltextFilter.parse("").isEmpty());
    }
}
