# maretha-fulltext-filter

Code example for Nuxeo LTS 2023 (MongoDB): how to adjust what goes into the fulltext index by a metadata value on each document, instead of by document type (the standard approach). Fulltext extraction runs only for documents whose configured property value is allowlisted; everything else skips text extraction.

## Configure

`nuxeo.conf`:

```
maretha.fulltext.filter.property=my:doctype
maretha.fulltext.filter.values=TypeA,TypeB
```

- Property: prefixed name of a top-level string property.
- Either setting unset = stock behavior.

## Build / install

`mvn clean install` (needs access to Nuxeo LTS 2023 artifacts), then deploy `target/maretha-fulltext-filter-1.0.0-SNAPSHOT.jar` in a Nuxeo package or `nxserver/bundles/`.

## Notes

- MongoDB repositories only.
- Built against Nuxeo 2023.54; overrides internal DBS classes, so re-test on each hotfix upgrade.
- Documents leaving the allowlist keep their existing extracted text.
- Bulk action `extractBinaryFulltext` bypasses the filter; restrict its NXQL query.
- Startup log: `Fulltext filter on repository default: ...`.

## License

Apache License 2.0, see [LICENSE](LICENSE) and [NOTICE](NOTICE).

Copyright 2026 Maretha. Author and contact: esteiner@maretha.io
