// 该文件由 tools/generate-metadata-module.mjs 生成，请勿手工修改。
import type { MetadataBundle } from '../domain/metadata/types';

export const METADATA: MetadataBundle = {
  "tags": [
    {
      "name": "procedure",
      "category": "root",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "id",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "id"
          ]
        }
      ]
    },
    {
      "name": "script-segment",
      "category": "fragment",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "id",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "id"
          ]
        }
      ]
    },
    {
      "name": "function-call",
      "category": "call",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "params",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "params"
          ]
        },
        {
          "name": "params-share",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "params-share"
          ]
        },
        {
          "name": "refid",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "refid",
            "refid.visit"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "java-call",
      "category": "call",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "target",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "target"
          ]
        }
      ]
    },
    {
      "name": "procedure-call",
      "category": "call",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "params",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "params"
          ]
        },
        {
          "name": "params-share",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "params-share"
          ]
        },
        {
          "name": "refid",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "refid",
            "refid.visit"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "script-include",
      "category": "call",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "refid",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "refid",
            "refid.visit"
          ]
        }
      ]
    },
    {
      "name": "sql-call",
      "category": "call",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "refid",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "refid",
            "refid.visit"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "sql-runner",
      "category": "call",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit"
          ]
        },
        {
          "name": "full-send",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean"
          ],
          "rawNames": [
            "full-send",
            "full-send.boolean"
          ]
        },
        {
          "name": "jump-error",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean"
          ],
          "rawNames": [
            "jump-error",
            "jump-error.boolean"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [
            "trim"
          ],
          "rawNames": [
            "result",
            "result.trim"
          ]
        },
        {
          "name": "script",
          "type": "CDATA",
          "required": false,
          "features": [
            "body-text",
            "visit"
          ],
          "rawNames": [
            "script",
            "script.body-text",
            "script.body-text.render",
            "script.visit"
          ]
        },
        {
          "name": "separator",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "separator",
            "separator.string"
          ]
        }
      ]
    },
    {
      "name": "lang-body",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-break",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-catch",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "e",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "e"
          ]
        },
        {
          "name": "test",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "dialect",
            "eval-ts"
          ],
          "rawNames": [
            "test",
            "test.boolean",
            "test.dialect",
            "test.eval-ts"
          ]
        },
        {
          "name": "type",
          "type": "CDATA",
          "required": false,
          "features": [
            "cause-first",
            "cause-last",
            "cause-raw",
            "cause-root"
          ],
          "rawNames": [
            "type",
            "type.cause-first",
            "type.cause-last",
            "type.cause-raw",
            "type.cause-root"
          ]
        }
      ]
    },
    {
      "name": "lang-choose",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-continue",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-do-while",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "first",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "first"
          ]
        },
        {
          "name": "index",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "index"
          ]
        },
        {
          "name": "test",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "dialect",
            "eval-ts"
          ],
          "rawNames": [
            "test",
            "test.boolean",
            "test.dialect",
            "test.eval-ts"
          ]
        }
      ]
    },
    {
      "name": "lang-finally",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-foreach",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "collection",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "collection"
          ]
        },
        {
          "name": "first",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "first"
          ]
        },
        {
          "name": "index",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "index"
          ]
        },
        {
          "name": "item",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "item"
          ]
        }
      ]
    },
    {
      "name": "lang-fori",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "begin",
          "type": "CDATA",
          "required": false,
          "features": [
            "int"
          ],
          "rawNames": [
            "begin",
            "begin.int"
          ]
        },
        {
          "name": "enclose",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "enclose"
          ]
        },
        {
          "name": "end",
          "type": "CDATA",
          "required": false,
          "features": [
            "int"
          ],
          "rawNames": [
            "end",
            "end.int"
          ]
        },
        {
          "name": "first",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "first"
          ]
        },
        {
          "name": "incr",
          "type": "CDATA",
          "required": false,
          "features": [
            "int"
          ],
          "rawNames": [
            "incr",
            "incr.int"
          ]
        },
        {
          "name": "index",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "index"
          ]
        },
        {
          "name": "item",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "item"
          ]
        }
      ]
    },
    {
      "name": "lang-if",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "test",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "dialect",
            "eval-ts",
            "visit"
          ],
          "rawNames": [
            "test",
            "test.boolean",
            "test.dialect",
            "test.eval-ts",
            "test.visit"
          ]
        }
      ]
    },
    {
      "name": "lang-otherwise",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-retry",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "count",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "count"
          ]
        },
        {
          "name": "delay",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "delay"
          ]
        },
        {
          "name": "incr",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "incr"
          ]
        },
        {
          "name": "max-delay",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "max-delay"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "time-unit",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "time-unit"
          ]
        },
        {
          "name": "type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "type"
          ]
        }
      ]
    },
    {
      "name": "lang-return",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "pattern",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "pattern"
          ]
        },
        {
          "name": "radix",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "radix"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "align",
            "body-text",
            "body-xml",
            "boolean",
            "byte",
            "char",
            "class",
            "current-time-millis",
            "date",
            "date-now",
            "double",
            "eval",
            "eval-funic",
            "eval-java",
            "eval-js",
            "eval-tinyscript",
            "eval-ts",
            "float",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "not",
            "null",
            "render",
            "short",
            "snow-uid",
            "string",
            "test",
            "trim",
            "uuid",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.align",
            "value.body-text",
            "value.body-xml",
            "value.boolean",
            "value.byte",
            "value.char",
            "value.class",
            "value.current-time-millis",
            "value.date",
            "value.date-now",
            "value.double",
            "value.eval",
            "value.eval-funic",
            "value.eval-java",
            "value.eval-js",
            "value.eval-tinyscript",
            "value.eval-ts",
            "value.float",
            "value.int",
            "value.is-empty",
            "value.is-not-empty",
            "value.is-not-null",
            "value.is-null",
            "value.long",
            "value.not",
            "value.null",
            "value.render",
            "value.short",
            "value.snow-uid",
            "value.string",
            "value.test",
            "value.trim",
            "value.uuid",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "lang-throw",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "cause",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "cause"
          ]
        },
        {
          "name": "type",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "type"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "value"
          ]
        }
      ]
    },
    {
      "name": "lang-try",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-when",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "test",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "dialect",
            "eval-ts"
          ],
          "rawNames": [
            "test",
            "test.boolean",
            "test.dialect",
            "test.eval-ts"
          ]
        }
      ]
    },
    {
      "name": "lang-while",
      "category": "flow",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "first",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "first"
          ]
        },
        {
          "name": "index",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "index"
          ]
        },
        {
          "name": "test",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "dialect",
            "eval-ts"
          ],
          "rawNames": [
            "test",
            "test.boolean",
            "test.dialect",
            "test.eval-ts"
          ]
        }
      ]
    },
    {
      "name": "sql-cursor",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "accept-batch",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "accept-batch"
          ]
        },
        {
          "name": "batch-size",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "batch-size"
          ]
        },
        {
          "name": "item",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "item"
          ]
        },
        {
          "name": "use-cursor",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "use-cursor"
          ]
        }
      ]
    },
    {
      "name": "sql-dialect",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "databases",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "databases"
          ]
        },
        {
          "name": "script",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "script"
          ]
        }
      ]
    },
    {
      "name": "sql-etl",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "before-truncate",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "before-truncate"
          ]
        },
        {
          "name": "commit-size",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "commit-size"
          ]
        },
        {
          "name": "item",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "item"
          ]
        },
        {
          "name": "read-batch-size",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "read-batch-size"
          ]
        },
        {
          "name": "sync",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "sync"
          ]
        },
        {
          "name": "use-cursor",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "use-cursor"
          ]
        },
        {
          "name": "write-batch-size",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "write-batch-size"
          ]
        }
      ]
    },
    {
      "name": "sql-query-columns",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "script",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "script"
          ]
        }
      ]
    },
    {
      "name": "sql-query-list",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "limit",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "limit"
          ]
        },
        {
          "name": "offset",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "offset"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "result-type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result-type"
          ]
        },
        {
          "name": "script",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "script"
          ]
        }
      ]
    },
    {
      "name": "sql-query-object",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "limited",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "limited"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "result-type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result-type"
          ]
        },
        {
          "name": "script",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "script"
          ]
        }
      ]
    },
    {
      "name": "sql-query-row",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "limited",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "limited"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "result-type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result-type"
          ]
        },
        {
          "name": "script",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "script"
          ]
        }
      ]
    },
    {
      "name": "sql-scope",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasources",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "datasources"
          ]
        }
      ]
    },
    {
      "name": "sql-script",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "sql-trans-begin",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "isolation",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "isolation"
          ]
        }
      ]
    },
    {
      "name": "sql-trans-commit",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        }
      ]
    },
    {
      "name": "sql-trans-none",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        }
      ]
    },
    {
      "name": "sql-trans-rollback",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        }
      ]
    },
    {
      "name": "sql-transactional",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasources",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "datasources"
          ]
        },
        {
          "name": "isolation",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "isolation"
          ]
        },
        {
          "name": "no-rollback-for",
          "type": "CDATA",
          "required": false,
          "features": [
            "cause-first",
            "cause-last",
            "cause-raw",
            "cause-root"
          ],
          "rawNames": [
            "no-rollback-for",
            "no-rollback-for.cause-first",
            "no-rollback-for.cause-last",
            "no-rollback-for.cause-raw",
            "no-rollback-for.cause-root"
          ]
        },
        {
          "name": "propagation",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "propagation"
          ]
        },
        {
          "name": "read-only",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "read-only"
          ]
        },
        {
          "name": "rollback-for",
          "type": "CDATA",
          "required": false,
          "features": [
            "cause-first",
            "cause-last",
            "cause-raw",
            "cause-root"
          ],
          "rawNames": [
            "rollback-for",
            "rollback-for.cause-first",
            "rollback-for.cause-last",
            "rollback-for.cause-raw",
            "rollback-for.cause-root"
          ]
        }
      ]
    },
    {
      "name": "sql-update",
      "category": "sql",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "script",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "script"
          ]
        }
      ]
    },
    {
      "name": "etl-after",
      "category": "etl",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "etl-before",
      "category": "etl",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "etl-extra",
      "category": "etl",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "table",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "table"
          ]
        }
      ]
    },
    {
      "name": "etl-load",
      "category": "etl",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "datasource",
          "type": "CDATA",
          "required": false,
          "features": [
            "render",
            "string",
            "visit"
          ],
          "rawNames": [
            "datasource",
            "datasource.render",
            "datasource.string",
            "datasource.visit",
            "datasource.visit.string"
          ]
        },
        {
          "name": "table",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "table"
          ]
        }
      ]
    },
    {
      "name": "etl-transform",
      "category": "etl",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "exclude",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "exclude"
          ]
        },
        {
          "name": "external",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "external"
          ]
        },
        {
          "name": "include",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "include"
          ]
        },
        {
          "name": "jdbc-type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "jdbc-type"
          ]
        },
        {
          "name": "source",
          "type": "CDATA",
          "required": false,
          "features": [
            "date-now",
            "eval",
            "eval-java",
            "int",
            "render",
            "string",
            "test",
            "visit"
          ],
          "rawNames": [
            "source",
            "source.date-now",
            "source.eval",
            "source.eval-java",
            "source.int",
            "source.render",
            "source.string",
            "source.test",
            "source.visit"
          ]
        },
        {
          "name": "target",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "target"
          ]
        },
        {
          "name": "type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "type"
          ]
        }
      ]
    },
    {
      "name": "lang-eval-java",
      "category": "java",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-java-body",
      "category": "java",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-java-import",
      "category": "java",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-java-member",
      "category": "java",
      "openAttributes": false,
      "source": "dtd",
      "attributes": []
    },
    {
      "name": "lang-eval",
      "category": "script",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "value"
          ]
        }
      ]
    },
    {
      "name": "lang-eval-funic",
      "category": "script",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-eval-groovy",
      "category": "script",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-eval-javascript",
      "category": "script",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-eval-js",
      "category": "script",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-eval-tinyscript",
      "category": "script",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-eval-ts",
      "category": "script",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-invoke",
      "category": "script",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "arg0",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg0",
            "arg0.boolean",
            "arg0.current-time-millis",
            "arg0.date-now",
            "arg0.double",
            "arg0.eval",
            "arg0.eval-java",
            "arg0.eval-ts",
            "arg0.int",
            "arg0.is-empty",
            "arg0.is-not-empty",
            "arg0.is-not-null",
            "arg0.is-null",
            "arg0.long",
            "arg0.render",
            "arg0.snow-uid",
            "arg0.string",
            "arg0.uuid"
          ]
        },
        {
          "name": "arg1",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg1",
            "arg1.boolean",
            "arg1.current-time-millis",
            "arg1.date-now",
            "arg1.double",
            "arg1.eval",
            "arg1.eval-java",
            "arg1.eval-ts",
            "arg1.int",
            "arg1.is-empty",
            "arg1.is-not-empty",
            "arg1.is-not-null",
            "arg1.is-null",
            "arg1.long",
            "arg1.render",
            "arg1.snow-uid",
            "arg1.string",
            "arg1.uuid"
          ]
        },
        {
          "name": "arg2",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg2",
            "arg2.boolean",
            "arg2.current-time-millis",
            "arg2.date-now",
            "arg2.double",
            "arg2.eval",
            "arg2.eval-java",
            "arg2.eval-ts",
            "arg2.int",
            "arg2.is-empty",
            "arg2.is-not-empty",
            "arg2.is-not-null",
            "arg2.is-null",
            "arg2.long",
            "arg2.render",
            "arg2.snow-uid",
            "arg2.string",
            "arg2.uuid"
          ]
        },
        {
          "name": "arg3",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg3",
            "arg3.boolean",
            "arg3.current-time-millis",
            "arg3.date-now",
            "arg3.double",
            "arg3.eval",
            "arg3.eval-java",
            "arg3.eval-ts",
            "arg3.int",
            "arg3.is-empty",
            "arg3.is-not-empty",
            "arg3.is-not-null",
            "arg3.is-null",
            "arg3.long",
            "arg3.render",
            "arg3.snow-uid",
            "arg3.string",
            "arg3.uuid"
          ]
        },
        {
          "name": "arg4",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg4",
            "arg4.boolean",
            "arg4.current-time-millis",
            "arg4.date-now",
            "arg4.double",
            "arg4.eval",
            "arg4.eval-java",
            "arg4.eval-ts",
            "arg4.int",
            "arg4.is-empty",
            "arg4.is-not-empty",
            "arg4.is-not-null",
            "arg4.is-null",
            "arg4.long",
            "arg4.render",
            "arg4.snow-uid",
            "arg4.string",
            "arg4.uuid"
          ]
        },
        {
          "name": "arg5",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg5",
            "arg5.boolean",
            "arg5.current-time-millis",
            "arg5.date-now",
            "arg5.double",
            "arg5.eval",
            "arg5.eval-java",
            "arg5.eval-ts",
            "arg5.int",
            "arg5.is-empty",
            "arg5.is-not-empty",
            "arg5.is-not-null",
            "arg5.is-null",
            "arg5.long",
            "arg5.render",
            "arg5.snow-uid",
            "arg5.string",
            "arg5.uuid"
          ]
        },
        {
          "name": "arg6",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg6",
            "arg6.boolean",
            "arg6.current-time-millis",
            "arg6.date-now",
            "arg6.double",
            "arg6.eval",
            "arg6.eval-java",
            "arg6.eval-ts",
            "arg6.int",
            "arg6.is-empty",
            "arg6.is-not-empty",
            "arg6.is-not-null",
            "arg6.is-null",
            "arg6.long",
            "arg6.render",
            "arg6.snow-uid",
            "arg6.string",
            "arg6.uuid"
          ]
        },
        {
          "name": "arg7",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg7",
            "arg7.boolean",
            "arg7.current-time-millis",
            "arg7.date-now",
            "arg7.double",
            "arg7.eval",
            "arg7.eval-java",
            "arg7.eval-ts",
            "arg7.int",
            "arg7.is-empty",
            "arg7.is-not-empty",
            "arg7.is-not-null",
            "arg7.is-null",
            "arg7.long",
            "arg7.render",
            "arg7.snow-uid",
            "arg7.string",
            "arg7.uuid"
          ]
        },
        {
          "name": "arg8",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg8",
            "arg8.boolean",
            "arg8.current-time-millis",
            "arg8.date-now",
            "arg8.double",
            "arg8.eval",
            "arg8.eval-java",
            "arg8.eval-ts",
            "arg8.int",
            "arg8.is-empty",
            "arg8.is-not-empty",
            "arg8.is-not-null",
            "arg8.is-null",
            "arg8.long",
            "arg8.render",
            "arg8.snow-uid",
            "arg8.string",
            "arg8.uuid"
          ]
        },
        {
          "name": "arg9",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg9",
            "arg9.boolean",
            "arg9.current-time-millis",
            "arg9.date-now",
            "arg9.double",
            "arg9.eval",
            "arg9.eval-java",
            "arg9.eval-ts",
            "arg9.int",
            "arg9.is-empty",
            "arg9.is-not-empty",
            "arg9.is-not-null",
            "arg9.is-null",
            "arg9.long",
            "arg9.render",
            "arg9.snow-uid",
            "arg9.string",
            "arg9.uuid"
          ]
        },
        {
          "name": "method",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "method"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "target",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "target"
          ]
        }
      ]
    },
    {
      "name": "lang-async",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "await",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "test"
          ],
          "rawNames": [
            "await",
            "await.boolean",
            "await.test"
          ]
        },
        {
          "name": "delay",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "delay"
          ]
        },
        {
          "name": "time-unit",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "time-unit"
          ]
        }
      ]
    },
    {
      "name": "lang-async-all",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "await",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "test"
          ],
          "rawNames": [
            "await",
            "await.boolean",
            "await.test"
          ]
        }
      ]
    },
    {
      "name": "lang-latch",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "count",
          "type": "CDATA",
          "required": false,
          "features": [
            "eval",
            "eval-ts",
            "int",
            "visit"
          ],
          "rawNames": [
            "count",
            "count.eval",
            "count.eval-ts",
            "count.int",
            "count.visit"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-latch-await",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "name",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "name"
          ]
        },
        {
          "name": "time-unit",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "time-unit"
          ]
        },
        {
          "name": "timeout",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "timeout"
          ]
        }
      ]
    },
    {
      "name": "lang-latch-down",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "name",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "name"
          ]
        }
      ]
    },
    {
      "name": "lang-listener",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "name",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "name"
          ]
        },
        {
          "name": "params-share",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "params-share"
          ]
        },
        {
          "name": "target",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "target"
          ]
        },
        {
          "name": "type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "type"
          ]
        }
      ]
    },
    {
      "name": "lang-lock",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "test",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "visit"
          ],
          "rawNames": [
            "test",
            "test.boolean",
            "test.visit"
          ]
        },
        {
          "name": "type",
          "type": "CDATA",
          "required": false,
          "features": [
            "string",
            "visit"
          ],
          "rawNames": [
            "type",
            "type.string",
            "type.visit"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "render",
            "string",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.render",
            "value.string",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "lang-sleep",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "time-unit",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "time-unit"
          ]
        },
        {
          "name": "timeout",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "timeout"
          ]
        }
      ]
    },
    {
      "name": "lang-synchronized",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "target",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "target"
          ]
        }
      ]
    },
    {
      "name": "lang-thread-pool",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "count",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "count"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "type"
          ]
        }
      ]
    },
    {
      "name": "lang-thread-pool-shutdown",
      "category": "concurrent",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "await",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "await"
          ]
        },
        {
          "name": "force",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "force"
          ]
        },
        {
          "name": "pool",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "pool"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "time-unit",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "time-unit"
          ]
        },
        {
          "name": "timeout",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "timeout"
          ]
        }
      ]
    },
    {
      "name": "lang-thread-pool-submit",
      "category": "concurrent",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "pool",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "pool"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-file-delete",
      "category": "io",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "file",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "file",
            "file.string"
          ]
        }
      ]
    },
    {
      "name": "lang-file-exists",
      "category": "io",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "file",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "file",
            "file.string"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-file-list",
      "category": "io",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "file",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "file",
            "file.string"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-file-mkdirs",
      "category": "io",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "file",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "file",
            "file.string"
          ]
        }
      ]
    },
    {
      "name": "lang-file-read-text",
      "category": "io",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "charset",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "charset"
          ]
        },
        {
          "name": "file",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "file",
            "file.string"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-file-tree",
      "category": "io",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "file",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "file",
            "file.string"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "lang-file-write-text",
      "category": "io",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "charset",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "charset"
          ]
        },
        {
          "name": "content",
          "type": "CDATA",
          "required": false,
          "features": [
            "render",
            "string",
            "text-body"
          ],
          "rawNames": [
            "content",
            "content.render",
            "content.string",
            "content.text-body",
            "content.text-body.render",
            "content.text-body.render.trim"
          ]
        },
        {
          "name": "file",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "file",
            "file.string"
          ]
        }
      ]
    },
    {
      "name": "lang-shell",
      "category": "io",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "await",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean"
          ],
          "rawNames": [
            "await",
            "await.boolean"
          ]
        },
        {
          "name": "envp",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "envp"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "int"
          ],
          "rawNames": [
            "result",
            "result",
            "result",
            "result.boolean",
            "result.int"
          ]
        },
        {
          "name": "run-as-file",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean"
          ],
          "rawNames": [
            "run-as-file",
            "run-as-file.boolean"
          ]
        },
        {
          "name": "script",
          "type": "CDATA",
          "required": false,
          "features": [
            "body-text",
            "string"
          ],
          "rawNames": [
            "script",
            "script.body-text",
            "script.body-text.render",
            "script.body-text.render.trim",
            "script.string"
          ]
        },
        {
          "name": "time-unit",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "time-unit"
          ]
        },
        {
          "name": "timeout",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "timeout"
          ]
        },
        {
          "name": "workdir",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "workdir"
          ]
        }
      ]
    },
    {
      "name": "lang-render",
      "category": "string",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "_lang",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "_lang"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [
            "trim"
          ],
          "rawNames": [
            "result",
            "result.trim"
          ]
        }
      ]
    },
    {
      "name": "lang-string",
      "category": "string",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "_lang",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "_lang"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [
            "trim"
          ],
          "rawNames": [
            "result",
            "result.trim"
          ]
        }
      ]
    },
    {
      "name": "lang-string-join",
      "category": "string",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "arg0",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg0",
            "arg0.boolean",
            "arg0.current-time-millis",
            "arg0.date-now",
            "arg0.double",
            "arg0.eval",
            "arg0.eval-java",
            "arg0.eval-ts",
            "arg0.int",
            "arg0.is-empty",
            "arg0.is-not-empty",
            "arg0.is-not-null",
            "arg0.is-null",
            "arg0.long",
            "arg0.null",
            "arg0.render",
            "arg0.snow-uid",
            "arg0.string",
            "arg0.uuid"
          ]
        },
        {
          "name": "arg1",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg1",
            "arg1.boolean",
            "arg1.current-time-millis",
            "arg1.date-now",
            "arg1.double",
            "arg1.eval",
            "arg1.eval-java",
            "arg1.eval-ts",
            "arg1.int",
            "arg1.is-empty",
            "arg1.is-not-empty",
            "arg1.is-not-null",
            "arg1.is-null",
            "arg1.long",
            "arg1.null",
            "arg1.render",
            "arg1.snow-uid",
            "arg1.string",
            "arg1.uuid"
          ]
        },
        {
          "name": "arg2",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg2",
            "arg2.boolean",
            "arg2.current-time-millis",
            "arg2.date-now",
            "arg2.double",
            "arg2.eval",
            "arg2.eval-java",
            "arg2.eval-ts",
            "arg2.int",
            "arg2.is-empty",
            "arg2.is-not-empty",
            "arg2.is-not-null",
            "arg2.is-null",
            "arg2.long",
            "arg2.null",
            "arg2.render",
            "arg2.snow-uid",
            "arg2.string",
            "arg2.uuid"
          ]
        },
        {
          "name": "arg3",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg3",
            "arg3.boolean",
            "arg3.current-time-millis",
            "arg3.date-now",
            "arg3.double",
            "arg3.eval",
            "arg3.eval-java",
            "arg3.eval-ts",
            "arg3.int",
            "arg3.is-empty",
            "arg3.is-not-empty",
            "arg3.is-not-null",
            "arg3.is-null",
            "arg3.long",
            "arg3.null",
            "arg3.render",
            "arg3.snow-uid",
            "arg3.string",
            "arg3.uuid"
          ]
        },
        {
          "name": "arg4",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg4",
            "arg4.boolean",
            "arg4.current-time-millis",
            "arg4.date-now",
            "arg4.double",
            "arg4.eval",
            "arg4.eval-java",
            "arg4.eval-ts",
            "arg4.int",
            "arg4.is-empty",
            "arg4.is-not-empty",
            "arg4.is-not-null",
            "arg4.is-null",
            "arg4.long",
            "arg4.null",
            "arg4.render",
            "arg4.snow-uid",
            "arg4.string",
            "arg4.uuid"
          ]
        },
        {
          "name": "arg5",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg5",
            "arg5.boolean",
            "arg5.current-time-millis",
            "arg5.date-now",
            "arg5.double",
            "arg5.eval",
            "arg5.eval-java",
            "arg5.eval-ts",
            "arg5.int",
            "arg5.is-empty",
            "arg5.is-not-empty",
            "arg5.is-not-null",
            "arg5.is-null",
            "arg5.long",
            "arg5.null",
            "arg5.render",
            "arg5.snow-uid",
            "arg5.string",
            "arg5.uuid"
          ]
        },
        {
          "name": "arg6",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg6",
            "arg6.boolean",
            "arg6.current-time-millis",
            "arg6.date-now",
            "arg6.double",
            "arg6.eval",
            "arg6.eval-java",
            "arg6.eval-ts",
            "arg6.int",
            "arg6.is-empty",
            "arg6.is-not-empty",
            "arg6.is-not-null",
            "arg6.is-null",
            "arg6.long",
            "arg6.null",
            "arg6.render",
            "arg6.snow-uid",
            "arg6.string",
            "arg6.uuid"
          ]
        },
        {
          "name": "arg7",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg7",
            "arg7.boolean",
            "arg7.current-time-millis",
            "arg7.date-now",
            "arg7.double",
            "arg7.eval",
            "arg7.eval-java",
            "arg7.eval-ts",
            "arg7.int",
            "arg7.is-empty",
            "arg7.is-not-empty",
            "arg7.is-not-null",
            "arg7.is-null",
            "arg7.long",
            "arg7.null",
            "arg7.render",
            "arg7.snow-uid",
            "arg7.string",
            "arg7.uuid"
          ]
        },
        {
          "name": "arg8",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg8",
            "arg8.boolean",
            "arg8.current-time-millis",
            "arg8.date-now",
            "arg8.double",
            "arg8.eval",
            "arg8.eval-java",
            "arg8.eval-ts",
            "arg8.int",
            "arg8.is-empty",
            "arg8.is-not-empty",
            "arg8.is-not-null",
            "arg8.is-null",
            "arg8.long",
            "arg8.null",
            "arg8.render",
            "arg8.snow-uid",
            "arg8.string",
            "arg8.uuid"
          ]
        },
        {
          "name": "arg9",
          "type": "CDATA",
          "required": false,
          "features": [
            "boolean",
            "current-time-millis",
            "date-now",
            "double",
            "eval",
            "eval-java",
            "eval-ts",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "null",
            "render",
            "snow-uid",
            "string",
            "uuid"
          ],
          "rawNames": [
            "arg9",
            "arg9.boolean",
            "arg9.current-time-millis",
            "arg9.date-now",
            "arg9.double",
            "arg9.eval",
            "arg9.eval-java",
            "arg9.eval-ts",
            "arg9.int",
            "arg9.is-empty",
            "arg9.is-not-empty",
            "arg9.is-not-null",
            "arg9.is-null",
            "arg9.long",
            "arg9.null",
            "arg9.render",
            "arg9.snow-uid",
            "arg9.string",
            "arg9.uuid"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [
            "trim"
          ],
          "rawNames": [
            "result",
            "result.trim"
          ]
        },
        {
          "name": "separator",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "separator"
          ]
        }
      ]
    },
    {
      "name": "lang-format",
      "category": "assign",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "pattern",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "pattern"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "date-now",
            "double",
            "eval-ts",
            "float",
            "int",
            "long",
            "string",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.date-now",
            "value.double",
            "value.eval-ts",
            "value.float",
            "value.int",
            "value.long",
            "value.string",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "lang-format-date",
      "category": "assign",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "pattern",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "pattern"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "date-now",
            "eval-ts",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.date-now",
            "value.eval-ts",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "lang-set",
      "category": "assign",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "_lang",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "_lang"
          ]
        },
        {
          "name": "pattern",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "pattern"
          ]
        },
        {
          "name": "radix",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "radix"
          ]
        },
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [
            "align",
            "body-text",
            "body-xml",
            "boolean",
            "byte",
            "char",
            "class",
            "current-time-millis",
            "date",
            "date-now",
            "double",
            "eval",
            "eval-funic",
            "eval-java",
            "eval-js",
            "eval-tinyscript",
            "eval-ts",
            "float",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "not",
            "null",
            "render",
            "short",
            "snow-uid",
            "string",
            "test",
            "trim",
            "uuid",
            "visit"
          ],
          "rawNames": [
            "result",
            "result.align",
            "result.body-text",
            "result.body-text.align",
            "result.body-text.trim",
            "result.body-xml",
            "result.body-xml.align",
            "result.body-xml.trim",
            "result.boolean",
            "result.byte",
            "result.char",
            "result.class",
            "result.current-time-millis",
            "result.date",
            "result.date-now",
            "result.double",
            "result.eval",
            "result.eval-funic",
            "result.eval-java",
            "result.eval-js",
            "result.eval-tinyscript",
            "result.eval-ts",
            "result.float",
            "result.int",
            "result.is-empty",
            "result.is-not-empty",
            "result.is-not-null",
            "result.is-null",
            "result.long",
            "result.not",
            "result.null",
            "result.render",
            "result.short",
            "result.snow-uid",
            "result.string",
            "result.test",
            "result.trim",
            "result.uuid",
            "result.visit"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "align",
            "body-text",
            "body-xml",
            "boolean",
            "byte",
            "char",
            "class",
            "current-time-millis",
            "date",
            "date-now",
            "double",
            "eval",
            "eval-funic",
            "eval-java",
            "eval-js",
            "eval-tinyscript",
            "eval-ts",
            "float",
            "int",
            "is-empty",
            "is-not-empty",
            "is-not-null",
            "is-null",
            "long",
            "not",
            "null",
            "render",
            "short",
            "snow-uid",
            "string",
            "test",
            "trim",
            "uuid",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.align",
            "value.body-text",
            "value.body-text.align",
            "value.body-text.trim",
            "value.body-xml",
            "value.body-xml.align",
            "value.body-xml.trim",
            "value.boolean",
            "value.byte",
            "value.char",
            "value.class",
            "value.current-time-millis",
            "value.date",
            "value.date-now",
            "value.double",
            "value.eval",
            "value.eval-funic",
            "value.eval-java",
            "value.eval-js",
            "value.eval-tinyscript",
            "value.eval-ts",
            "value.float",
            "value.int",
            "value.is-empty",
            "value.is-not-empty",
            "value.is-not-null",
            "value.is-null",
            "value.long",
            "value.not",
            "value.null",
            "value.render",
            "value.short",
            "value.snow-uid",
            "value.string",
            "value.test",
            "value.trim",
            "value.uuid",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "debugger",
      "category": "output",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "tag",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "tag"
          ]
        },
        {
          "name": "test",
          "type": "CDATA",
          "required": false,
          "features": [
            "eval-ts"
          ],
          "rawNames": [
            "test",
            "test.eval-ts"
          ]
        }
      ]
    },
    {
      "name": "lang-printf",
      "category": "output",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "tag",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "tag",
            "tag.string"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "align",
            "body-text",
            "body-xml",
            "render",
            "string",
            "trim"
          ],
          "rawNames": [
            "value",
            "value.align",
            "value.body-text",
            "value.body-text.align",
            "value.body-text.trim",
            "value.body-xml",
            "value.body-xml.align",
            "value.body-xml.trim",
            "value.body-xml.trim.render",
            "value.render",
            "value.string",
            "value.trim"
          ]
        }
      ]
    },
    {
      "name": "lang-println",
      "category": "output",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "tag",
          "type": "CDATA",
          "required": false,
          "features": [
            "string"
          ],
          "rawNames": [
            "tag",
            "tag.string"
          ]
        }
      ]
    },
    {
      "name": "log-debug",
      "category": "log",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "e",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "e"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "body-text",
            "render",
            "string",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.body-text",
            "value.body-text.render",
            "value.body-text.trim.render",
            "value.render",
            "value.string",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "log-error",
      "category": "log",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "e",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "e"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "body-text",
            "render",
            "string",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.body-text",
            "value.body-text.render",
            "value.body-text.trim.render",
            "value.render",
            "value.string",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "log-info",
      "category": "log",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "e",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "e"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "body-text",
            "render",
            "string",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.body-text",
            "value.body-text.render",
            "value.body-text.trim.render",
            "value.render",
            "value.string",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "log-warn",
      "category": "log",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "e",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "e"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "body-text",
            "render",
            "string",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.body-text",
            "value.body-text.render",
            "value.body-text.trim.render",
            "value.render",
            "value.string",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "context-convert-method-class",
      "category": "context",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "class",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "class"
          ]
        }
      ]
    },
    {
      "name": "context-invoke-method-class",
      "category": "context",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "class",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "class"
          ]
        }
      ]
    },
    {
      "name": "context-load-package",
      "category": "context",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "class",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "class"
          ]
        },
        {
          "name": "package",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "package"
          ]
        }
      ]
    },
    {
      "name": "lang-ai",
      "category": "context",
      "openAttributes": false,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "result"
          ]
        },
        {
          "name": "role",
          "type": "CDATA",
          "required": false,
          "features": [
            "render",
            "string",
            "visit"
          ],
          "rawNames": [
            "role",
            "role.render",
            "role.string",
            "role.visit"
          ]
        },
        {
          "name": "type",
          "type": "CDATA",
          "required": false,
          "features": [
            "string",
            "visit"
          ],
          "rawNames": [
            "type",
            "type.string",
            "type.visit"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": false,
          "features": [
            "body-text",
            "render",
            "string",
            "visit"
          ],
          "rawNames": [
            "value",
            "value.body-text",
            "value.body-text.render",
            "value.render",
            "value.string",
            "value.visit"
          ]
        }
      ]
    },
    {
      "name": "lang-new-params",
      "category": "context",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "result",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "result"
          ]
        }
      ]
    },
    {
      "name": "event-publish",
      "category": "event",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "event-type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "event-type"
          ]
        }
      ]
    },
    {
      "name": "event-send",
      "category": "event",
      "openAttributes": true,
      "source": "dtd",
      "attributes": [
        {
          "name": "event-type",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "event-type"
          ]
        }
      ]
    },
    {
      "name": "bind",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": [
        {
          "name": "name",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "name"
          ]
        },
        {
          "name": "value",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "value"
          ]
        }
      ]
    },
    {
      "name": "choose",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": []
    },
    {
      "name": "dialect",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": [
        {
          "name": "databases",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "databases"
          ]
        }
      ]
    },
    {
      "name": "dialect-choose",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": []
    },
    {
      "name": "dialect-otherwise",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": []
    },
    {
      "name": "dialect-when",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": [
        {
          "name": "databases",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "databases"
          ]
        }
      ]
    },
    {
      "name": "foreach",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": [
        {
          "name": "close",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "close"
          ]
        },
        {
          "name": "collection",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "collection"
          ]
        },
        {
          "name": "item",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "item"
          ]
        },
        {
          "name": "open",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "open"
          ]
        },
        {
          "name": "separator",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "separator"
          ]
        }
      ]
    },
    {
      "name": "if",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": [
        {
          "name": "test",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "test"
          ]
        }
      ]
    },
    {
      "name": "include",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": [
        {
          "name": "refid",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "refid"
          ]
        }
      ]
    },
    {
      "name": "otherwise",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": []
    },
    {
      "name": "set",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": []
    },
    {
      "name": "trim",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": [
        {
          "name": "prefix",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "prefix"
          ]
        },
        {
          "name": "prefixOverrides",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "prefixOverrides"
          ]
        },
        {
          "name": "suffix",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "suffix"
          ]
        },
        {
          "name": "suffixOverrides",
          "type": "CDATA",
          "required": false,
          "features": [],
          "rawNames": [
            "suffixOverrides"
          ]
        }
      ]
    },
    {
      "name": "when",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": [
        {
          "name": "test",
          "type": "CDATA",
          "required": true,
          "features": [],
          "rawNames": [
            "test"
          ]
        }
      ]
    },
    {
      "name": "where",
      "category": "dynamic",
      "openAttributes": false,
      "source": "dynamic",
      "attributes": []
    }
  ],
  "attributes": [
    {
      "name": "_lang",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-render",
        "lang-set",
        "lang-string"
      ],
      "source": "dtd"
    },
    {
      "name": "accept-batch",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-cursor"
      ],
      "source": "dtd"
    },
    {
      "name": "arg0",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg1",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg2",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg3",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg4",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg5",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg6",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg7",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg8",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "arg9",
      "type": "CDATA",
      "features": [
        "boolean",
        "current-time-millis",
        "date-now",
        "double",
        "eval",
        "eval-java",
        "eval-ts",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "null",
        "render",
        "snow-uid",
        "string",
        "uuid"
      ],
      "tags": [
        "lang-invoke",
        "lang-string-join"
      ],
      "source": "dtd"
    },
    {
      "name": "await",
      "type": "CDATA",
      "features": [
        "boolean",
        "test"
      ],
      "tags": [
        "lang-async",
        "lang-async-all",
        "lang-shell",
        "lang-thread-pool-shutdown"
      ],
      "source": "dtd"
    },
    {
      "name": "batch-size",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-cursor"
      ],
      "source": "dtd"
    },
    {
      "name": "before-truncate",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-etl"
      ],
      "source": "dtd"
    },
    {
      "name": "begin",
      "type": "CDATA",
      "features": [
        "int"
      ],
      "tags": [
        "lang-fori"
      ],
      "source": "dtd"
    },
    {
      "name": "cause",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-throw"
      ],
      "source": "dtd"
    },
    {
      "name": "charset",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-file-read-text",
        "lang-file-write-text"
      ],
      "source": "dtd"
    },
    {
      "name": "class",
      "type": "CDATA",
      "features": [],
      "tags": [
        "context-convert-method-class",
        "context-invoke-method-class",
        "context-load-package"
      ],
      "source": "dtd"
    },
    {
      "name": "close",
      "type": "CDATA",
      "features": [],
      "tags": [
        "foreach"
      ],
      "source": "dtd"
    },
    {
      "name": "collection",
      "type": "CDATA",
      "features": [],
      "tags": [
        "foreach",
        "lang-foreach"
      ],
      "source": "dtd"
    },
    {
      "name": "commit-size",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-etl"
      ],
      "source": "dtd"
    },
    {
      "name": "content",
      "type": "CDATA",
      "features": [
        "render",
        "string",
        "text-body"
      ],
      "tags": [
        "lang-file-write-text"
      ],
      "source": "dtd"
    },
    {
      "name": "count",
      "type": "CDATA",
      "features": [
        "eval",
        "eval-ts",
        "int",
        "visit"
      ],
      "tags": [
        "lang-latch",
        "lang-retry",
        "lang-thread-pool"
      ],
      "source": "dtd"
    },
    {
      "name": "databases",
      "type": "CDATA",
      "features": [],
      "tags": [
        "dialect",
        "dialect-when",
        "sql-dialect"
      ],
      "source": "dtd"
    },
    {
      "name": "datasource",
      "type": "CDATA",
      "features": [
        "render",
        "string",
        "visit"
      ],
      "tags": [
        "etl-extra",
        "etl-load",
        "lang-do-while",
        "lang-if",
        "lang-when",
        "lang-while",
        "sql-call",
        "sql-query-columns",
        "sql-query-list",
        "sql-query-object",
        "sql-query-row",
        "sql-runner",
        "sql-script",
        "sql-trans-begin",
        "sql-trans-commit",
        "sql-trans-none",
        "sql-trans-rollback",
        "sql-update"
      ],
      "source": "dtd"
    },
    {
      "name": "datasources",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-scope",
        "sql-transactional"
      ],
      "source": "dtd"
    },
    {
      "name": "delay",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-async",
        "lang-retry"
      ],
      "source": "dtd"
    },
    {
      "name": "e",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-catch",
        "log-debug",
        "log-error",
        "log-info",
        "log-warn"
      ],
      "source": "dtd"
    },
    {
      "name": "enclose",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-fori"
      ],
      "source": "dtd"
    },
    {
      "name": "end",
      "type": "CDATA",
      "features": [
        "int"
      ],
      "tags": [
        "lang-fori"
      ],
      "source": "dtd"
    },
    {
      "name": "envp",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-shell"
      ],
      "source": "dtd"
    },
    {
      "name": "event-type",
      "type": "CDATA",
      "features": [],
      "tags": [
        "event-publish",
        "event-send"
      ],
      "source": "dtd"
    },
    {
      "name": "exclude",
      "type": "CDATA",
      "features": [],
      "tags": [
        "etl-transform"
      ],
      "source": "dtd"
    },
    {
      "name": "external",
      "type": "CDATA",
      "features": [],
      "tags": [
        "etl-transform"
      ],
      "source": "dtd"
    },
    {
      "name": "file",
      "type": "CDATA",
      "features": [
        "string"
      ],
      "tags": [
        "lang-file-delete",
        "lang-file-exists",
        "lang-file-list",
        "lang-file-mkdirs",
        "lang-file-read-text",
        "lang-file-tree",
        "lang-file-write-text"
      ],
      "source": "dtd"
    },
    {
      "name": "first",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-do-while",
        "lang-foreach",
        "lang-fori",
        "lang-while"
      ],
      "source": "dtd"
    },
    {
      "name": "force",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-thread-pool-shutdown"
      ],
      "source": "dtd"
    },
    {
      "name": "full-send",
      "type": "CDATA",
      "features": [
        "boolean"
      ],
      "tags": [
        "sql-runner"
      ],
      "source": "dtd"
    },
    {
      "name": "id",
      "type": "CDATA",
      "features": [],
      "tags": [
        "procedure",
        "script-segment"
      ],
      "source": "dtd"
    },
    {
      "name": "include",
      "type": "CDATA",
      "features": [],
      "tags": [
        "etl-transform"
      ],
      "source": "dtd"
    },
    {
      "name": "incr",
      "type": "CDATA",
      "features": [
        "int"
      ],
      "tags": [
        "lang-fori",
        "lang-retry"
      ],
      "source": "dtd"
    },
    {
      "name": "index",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-do-while",
        "lang-foreach",
        "lang-fori",
        "lang-while"
      ],
      "source": "dtd"
    },
    {
      "name": "isolation",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-trans-begin",
        "sql-transactional"
      ],
      "source": "dtd"
    },
    {
      "name": "item",
      "type": "CDATA",
      "features": [],
      "tags": [
        "foreach",
        "lang-foreach",
        "lang-fori",
        "sql-cursor",
        "sql-etl"
      ],
      "source": "dtd"
    },
    {
      "name": "jdbc-type",
      "type": "CDATA",
      "features": [],
      "tags": [
        "etl-transform"
      ],
      "source": "dtd"
    },
    {
      "name": "jump-error",
      "type": "CDATA",
      "features": [
        "boolean"
      ],
      "tags": [
        "sql-runner"
      ],
      "source": "dtd"
    },
    {
      "name": "limit",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-query-list"
      ],
      "source": "dtd"
    },
    {
      "name": "limited",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-query-object",
        "sql-query-row"
      ],
      "source": "dtd"
    },
    {
      "name": "max-delay",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-retry"
      ],
      "source": "dtd"
    },
    {
      "name": "method",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-invoke"
      ],
      "source": "dtd"
    },
    {
      "name": "name",
      "type": "CDATA",
      "features": [],
      "tags": [
        "bind",
        "lang-latch-await",
        "lang-latch-down",
        "lang-listener"
      ],
      "source": "dtd"
    },
    {
      "name": "no-rollback-for",
      "type": "CDATA",
      "features": [
        "cause-first",
        "cause-last",
        "cause-raw",
        "cause-root"
      ],
      "tags": [
        "sql-transactional"
      ],
      "source": "dtd"
    },
    {
      "name": "offset",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-query-list"
      ],
      "source": "dtd"
    },
    {
      "name": "open",
      "type": "CDATA",
      "features": [],
      "tags": [
        "foreach"
      ],
      "source": "dtd"
    },
    {
      "name": "package",
      "type": "CDATA",
      "features": [],
      "tags": [
        "context-load-package"
      ],
      "source": "dtd"
    },
    {
      "name": "params",
      "type": "CDATA",
      "features": [],
      "tags": [
        "function-call",
        "procedure-call"
      ],
      "source": "dtd"
    },
    {
      "name": "params-share",
      "type": "CDATA",
      "features": [],
      "tags": [
        "function-call",
        "lang-listener",
        "procedure-call"
      ],
      "source": "dtd"
    },
    {
      "name": "pattern",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-format",
        "lang-format-date",
        "lang-return",
        "lang-set"
      ],
      "source": "dtd"
    },
    {
      "name": "pool",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-thread-pool-shutdown",
        "lang-thread-pool-submit"
      ],
      "source": "dtd"
    },
    {
      "name": "prefix",
      "type": "CDATA",
      "features": [],
      "tags": [
        "trim"
      ],
      "source": "dtd"
    },
    {
      "name": "prefixOverrides",
      "type": "CDATA",
      "features": [],
      "tags": [
        "trim"
      ],
      "source": "dtd"
    },
    {
      "name": "propagation",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-transactional"
      ],
      "source": "dtd"
    },
    {
      "name": "radix",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-return",
        "lang-set"
      ],
      "source": "dtd"
    },
    {
      "name": "read-batch-size",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-etl"
      ],
      "source": "dtd"
    },
    {
      "name": "read-only",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-transactional"
      ],
      "source": "dtd"
    },
    {
      "name": "refid",
      "type": "CDATA",
      "features": [
        "visit"
      ],
      "tags": [
        "function-call",
        "include",
        "procedure-call",
        "script-include",
        "sql-call"
      ],
      "source": "dtd"
    },
    {
      "name": "result",
      "type": "CDATA",
      "features": [
        "align",
        "body-text",
        "body-xml",
        "boolean",
        "byte",
        "char",
        "class",
        "current-time-millis",
        "date",
        "date-now",
        "double",
        "eval",
        "eval-funic",
        "eval-java",
        "eval-js",
        "eval-tinyscript",
        "eval-ts",
        "float",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "not",
        "null",
        "render",
        "short",
        "snow-uid",
        "string",
        "test",
        "trim",
        "uuid",
        "visit"
      ],
      "tags": [
        "function-call",
        "java-call",
        "lang-ai",
        "lang-eval",
        "lang-eval-funic",
        "lang-eval-groovy",
        "lang-eval-java",
        "lang-eval-javascript",
        "lang-eval-js",
        "lang-eval-tinyscript",
        "lang-eval-ts",
        "lang-file-exists",
        "lang-file-list",
        "lang-file-read-text",
        "lang-file-tree",
        "lang-format",
        "lang-format-date",
        "lang-invoke",
        "lang-latch",
        "lang-lock",
        "lang-new-params",
        "lang-render",
        "lang-retry",
        "lang-set",
        "lang-shell",
        "lang-string",
        "lang-string-join",
        "lang-thread-pool",
        "lang-thread-pool-shutdown",
        "lang-thread-pool-submit",
        "procedure-call",
        "sql-call",
        "sql-query-columns",
        "sql-query-list",
        "sql-query-object",
        "sql-query-row",
        "sql-runner",
        "sql-script",
        "sql-update"
      ],
      "source": "dtd"
    },
    {
      "name": "result-type",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-query-list",
        "sql-query-object",
        "sql-query-row"
      ],
      "source": "dtd"
    },
    {
      "name": "role",
      "type": "CDATA",
      "features": [
        "render",
        "string",
        "visit"
      ],
      "tags": [
        "lang-ai"
      ],
      "source": "dtd"
    },
    {
      "name": "rollback-for",
      "type": "CDATA",
      "features": [
        "cause-first",
        "cause-last",
        "cause-raw",
        "cause-root"
      ],
      "tags": [
        "sql-transactional"
      ],
      "source": "dtd"
    },
    {
      "name": "run-as-file",
      "type": "CDATA",
      "features": [
        "boolean"
      ],
      "tags": [
        "lang-shell"
      ],
      "source": "dtd"
    },
    {
      "name": "script",
      "type": "CDATA",
      "features": [
        "body-text",
        "string",
        "visit"
      ],
      "tags": [
        "lang-shell",
        "sql-dialect",
        "sql-query-columns",
        "sql-query-list",
        "sql-query-object",
        "sql-query-row",
        "sql-runner",
        "sql-update"
      ],
      "source": "dtd"
    },
    {
      "name": "separator",
      "type": "CDATA",
      "features": [
        "string"
      ],
      "tags": [
        "foreach",
        "lang-string-join",
        "sql-runner"
      ],
      "source": "dtd"
    },
    {
      "name": "source",
      "type": "CDATA",
      "features": [
        "date-now",
        "eval",
        "eval-java",
        "int",
        "render",
        "string",
        "test",
        "visit"
      ],
      "tags": [
        "etl-transform"
      ],
      "source": "dtd"
    },
    {
      "name": "suffix",
      "type": "CDATA",
      "features": [],
      "tags": [
        "trim"
      ],
      "source": "dtd"
    },
    {
      "name": "suffixOverrides",
      "type": "CDATA",
      "features": [],
      "tags": [
        "trim"
      ],
      "source": "dtd"
    },
    {
      "name": "sync",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-etl"
      ],
      "source": "dtd"
    },
    {
      "name": "table",
      "type": "CDATA",
      "features": [],
      "tags": [
        "etl-extra",
        "etl-load"
      ],
      "source": "dtd"
    },
    {
      "name": "tag",
      "type": "CDATA",
      "features": [
        "string"
      ],
      "tags": [
        "debugger",
        "lang-printf",
        "lang-println"
      ],
      "source": "dtd"
    },
    {
      "name": "target",
      "type": "CDATA",
      "features": [],
      "tags": [
        "etl-transform",
        "java-call",
        "lang-invoke",
        "lang-listener",
        "lang-synchronized"
      ],
      "source": "dtd"
    },
    {
      "name": "test",
      "type": "CDATA",
      "features": [
        "boolean",
        "dialect",
        "eval-ts",
        "visit"
      ],
      "tags": [
        "debugger",
        "if",
        "lang-catch",
        "lang-do-while",
        "lang-if",
        "lang-lock",
        "lang-when",
        "lang-while",
        "when"
      ],
      "source": "dtd"
    },
    {
      "name": "time-unit",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-async",
        "lang-latch-await",
        "lang-retry",
        "lang-shell",
        "lang-sleep",
        "lang-thread-pool-shutdown"
      ],
      "source": "dtd"
    },
    {
      "name": "timeout",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-latch-await",
        "lang-shell",
        "lang-sleep",
        "lang-thread-pool-shutdown"
      ],
      "source": "dtd"
    },
    {
      "name": "type",
      "type": "CDATA",
      "features": [
        "cause-first",
        "cause-last",
        "cause-raw",
        "cause-root",
        "string",
        "visit"
      ],
      "tags": [
        "etl-transform",
        "lang-ai",
        "lang-catch",
        "lang-listener",
        "lang-lock",
        "lang-retry",
        "lang-thread-pool",
        "lang-throw"
      ],
      "source": "dtd"
    },
    {
      "name": "use-cursor",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-cursor",
        "sql-etl"
      ],
      "source": "dtd"
    },
    {
      "name": "value",
      "type": "CDATA",
      "features": [
        "align",
        "body-text",
        "body-xml",
        "boolean",
        "byte",
        "char",
        "class",
        "current-time-millis",
        "date",
        "date-now",
        "double",
        "eval",
        "eval-funic",
        "eval-java",
        "eval-js",
        "eval-tinyscript",
        "eval-ts",
        "float",
        "int",
        "is-empty",
        "is-not-empty",
        "is-not-null",
        "is-null",
        "long",
        "not",
        "null",
        "render",
        "short",
        "snow-uid",
        "string",
        "test",
        "trim",
        "uuid",
        "visit"
      ],
      "tags": [
        "bind",
        "lang-ai",
        "lang-eval",
        "lang-format",
        "lang-format-date",
        "lang-lock",
        "lang-printf",
        "lang-return",
        "lang-set",
        "lang-throw",
        "log-debug",
        "log-error",
        "log-info",
        "log-warn"
      ],
      "source": "dtd"
    },
    {
      "name": "workdir",
      "type": "CDATA",
      "features": [],
      "tags": [
        "lang-shell"
      ],
      "source": "dtd"
    },
    {
      "name": "write-batch-size",
      "type": "CDATA",
      "features": [],
      "tags": [
        "sql-etl"
      ],
      "source": "dtd"
    }
  ],
  "features": [
    {
      "name": "in",
      "group": "annotation",
      "description": ""
    },
    {
      "name": "out",
      "group": "annotation",
      "description": ""
    },
    {
      "name": "int",
      "group": "type",
      "description": ""
    },
    {
      "name": "double",
      "group": "type",
      "description": ""
    },
    {
      "name": "float",
      "group": "type",
      "description": ""
    },
    {
      "name": "string",
      "group": "type",
      "description": ""
    },
    {
      "name": "long",
      "group": "type",
      "description": ""
    },
    {
      "name": "short",
      "group": "type",
      "description": ""
    },
    {
      "name": "char",
      "group": "type",
      "description": ""
    },
    {
      "name": "byte",
      "group": "type",
      "description": ""
    },
    {
      "name": "boolean",
      "group": "type",
      "description": ""
    },
    {
      "name": "null",
      "group": "type",
      "description": ""
    },
    {
      "name": "date",
      "group": "type",
      "description": ""
    },
    {
      "name": "render",
      "group": "string",
      "description": ""
    },
    {
      "name": "trim",
      "group": "string",
      "description": ""
    },
    {
      "name": "align",
      "group": "string",
      "description": ""
    },
    {
      "name": "body-text",
      "group": "string",
      "description": ""
    },
    {
      "name": "body-xml",
      "group": "string",
      "description": ""
    },
    {
      "name": "location-file",
      "group": "location",
      "description": ""
    },
    {
      "name": "location-line",
      "group": "location",
      "description": ""
    },
    {
      "name": "location-tag",
      "group": "location",
      "description": ""
    },
    {
      "name": "location",
      "group": "location",
      "description": ""
    },
    {
      "name": "spacing-left",
      "group": "spacing",
      "description": ""
    },
    {
      "name": "spacing-right",
      "group": "spacing",
      "description": ""
    },
    {
      "name": "spacing",
      "group": "spacing",
      "description": ""
    },
    {
      "name": "lower",
      "group": "transform",
      "description": ""
    },
    {
      "name": "upper",
      "group": "transform",
      "description": ""
    },
    {
      "name": "hashcode",
      "group": "transform",
      "description": ""
    },
    {
      "name": "visit",
      "group": "process",
      "description": ""
    },
    {
      "name": "eval",
      "group": "process",
      "description": ""
    },
    {
      "name": "test",
      "group": "process",
      "description": ""
    },
    {
      "name": "context",
      "group": "process",
      "description": ""
    },
    {
      "name": "executor",
      "group": "process",
      "description": ""
    },
    {
      "name": "node",
      "group": "process",
      "description": ""
    },
    {
      "name": "eval-java",
      "group": "language",
      "description": ""
    },
    {
      "name": "eval-js",
      "group": "language",
      "description": ""
    },
    {
      "name": "eval-tinyscript",
      "group": "language",
      "description": ""
    },
    {
      "name": "eval-ts",
      "group": "language",
      "description": ""
    },
    {
      "name": "eval-groovy",
      "group": "language",
      "description": ""
    },
    {
      "name": "eval-funic",
      "group": "language",
      "description": ""
    },
    {
      "name": "class",
      "group": "language",
      "description": ""
    },
    {
      "name": "not",
      "group": "process",
      "description": ""
    },
    {
      "name": "dialect",
      "group": "process",
      "description": ""
    },
    {
      "name": "is-null",
      "group": "predicate",
      "description": ""
    },
    {
      "name": "is-not-null",
      "group": "predicate",
      "description": ""
    },
    {
      "name": "is-empty",
      "group": "predicate",
      "description": ""
    },
    {
      "name": "is-not-empty",
      "group": "predicate",
      "description": ""
    },
    {
      "name": "date-now",
      "group": "value",
      "description": ""
    },
    {
      "name": "uuid",
      "group": "value",
      "description": ""
    },
    {
      "name": "current-time-millis",
      "group": "value",
      "description": ""
    },
    {
      "name": "snow-uid",
      "group": "value",
      "description": ""
    },
    {
      "name": "new-map",
      "group": "value",
      "description": ""
    },
    {
      "name": "new-list",
      "group": "value",
      "description": ""
    },
    {
      "name": "new-set",
      "group": "value",
      "description": ""
    },
    {
      "name": "cause-first",
      "group": "exception",
      "description": ""
    },
    {
      "name": "cause-last",
      "group": "exception",
      "description": ""
    },
    {
      "name": "cause-raw",
      "group": "exception",
      "description": ""
    },
    {
      "name": "cause-root",
      "group": "exception",
      "description": ""
    }
  ],
  "injections": {
    "body": [
      {
        "id": "sql",
        "tags": [
          "sql-query-list",
          "sql-query-row",
          "sql-query-object",
          "sql-query-columns",
          "sql-update",
          "sql-dialect",
          "sql-script"
        ],
        "contentName": "meta.embedded.block.sql.xproc4j",
        "scope": "source.sql",
        "languageId": "sql",
        "variables": true,
        "dynamicSql": true
      },
      {
        "id": "java",
        "tags": [
          "lang-eval-java",
          "lang-java-import",
          "lang-java-member",
          "lang-java-body"
        ],
        "contentName": "meta.embedded.block.java.xproc4j",
        "scope": "source.java",
        "languageId": "java",
        "variables": true,
        "childTags": [
          "lang-java-import",
          "lang-java-member",
          "lang-java-body"
        ]
      },
      {
        "id": "javascript",
        "tags": [
          "lang-eval-javascript",
          "lang-eval-js"
        ],
        "contentName": "meta.embedded.block.javascript.xproc4j",
        "scope": "source.js",
        "languageId": "javascript",
        "variables": true
      },
      {
        "id": "groovy",
        "tags": [
          "lang-eval-groovy"
        ],
        "contentName": "meta.embedded.block.groovy.xproc4j",
        "scope": "source.groovy",
        "variables": true
      },
      {
        "id": "shell",
        "tags": [
          "lang-shell"
        ],
        "contentName": "meta.embedded.block.shell.xproc4j",
        "scope": "source.shell",
        "languageId": "shellscript",
        "variables": true
      },
      {
        "id": "vars-only",
        "tags": [
          "lang-string",
          "lang-render"
        ],
        "contentName": "meta.embedded.block.variables.xproc4j",
        "variables": true
      },
      {
        "id": "dsl-vars-only",
        "tags": [
          "lang-eval",
          "lang-eval-ts",
          "lang-eval-tinyscript",
          "lang-eval-funic"
        ],
        "contentName": "meta.embedded.block.variables.xproc4j",
        "variables": true
      }
    ],
    "langDynamic": {
      "tags": [
        "lang-string",
        "lang-render"
      ],
      "values": {
        "sql": {
          "scope": "source.sql",
          "contentName": "meta.embedded.block.sql.xproc4j",
          "languageId": "sql"
        },
        "java": {
          "scope": "source.java",
          "contentName": "meta.embedded.block.java.xproc4j",
          "languageId": "java"
        },
        "javascript": {
          "scope": "source.js",
          "contentName": "meta.embedded.block.javascript.xproc4j",
          "languageId": "javascript"
        },
        "js": {
          "scope": "source.js",
          "contentName": "meta.embedded.block.javascript.xproc4j",
          "languageId": "javascript"
        },
        "json": {
          "scope": "source.json",
          "contentName": "meta.embedded.block.json.xproc4j",
          "languageId": "json"
        },
        "xml": {
          "scope": "text.xml",
          "contentName": "meta.embedded.block.xml.xproc4j",
          "languageId": "xml"
        },
        "yaml": {
          "scope": "source.yaml",
          "contentName": "meta.embedded.block.yaml.xproc4j",
          "languageId": "yaml"
        },
        "shell": {
          "scope": "source.shell",
          "contentName": "meta.embedded.block.shell.xproc4j",
          "languageId": "shellscript"
        },
        "bash": {
          "scope": "source.shell",
          "contentName": "meta.embedded.block.shell.xproc4j",
          "languageId": "shellscript"
        },
        "css": {
          "scope": "source.css",
          "contentName": "meta.embedded.block.css.xproc4j",
          "languageId": "css"
        },
        "sass": {
          "scope": "source.css",
          "contentName": "meta.embedded.block.css.xproc4j",
          "languageId": "css"
        },
        "markdown": {
          "scope": "text.html.markdown",
          "contentName": "meta.embedded.block.markdown.xproc4j",
          "languageId": "markdown"
        },
        "md": {
          "scope": "text.html.markdown",
          "contentName": "meta.embedded.block.markdown.xproc4j",
          "languageId": "markdown"
        },
        "regex": {
          "scope": "source.regexp",
          "contentName": "meta.embedded.block.regexp.xproc4j"
        },
        "regexp": {
          "scope": "source.regexp",
          "contentName": "meta.embedded.block.regexp.xproc4j"
        },
        "groovy": {
          "scope": "source.groovy",
          "contentName": "meta.embedded.block.groovy.xproc4j"
        },
        "vtl": {
          "scope": "text.html.velocity",
          "contentName": "meta.embedded.block.velocity.xproc4j"
        },
        "velocity": {
          "scope": "text.html.velocity",
          "contentName": "meta.embedded.block.velocity.xproc4j"
        },
        "freemarker": {
          "scope": "text.html.ftl",
          "contentName": "meta.embedded.block.freemarker.xproc4j"
        },
        "thymeleaf": {
          "scope": "text.html.thymeleaf",
          "contentName": "meta.embedded.block.thymeleaf.xproc4j"
        },
        "spel": {
          "scope": "source.spel",
          "contentName": "meta.embedded.block.spel.xproc4j"
        },
        "el": {
          "scope": "source.el",
          "contentName": "meta.embedded.block.el.xproc4j"
        },
        "expr": {
          "scope": "source.el",
          "contentName": "meta.embedded.block.el.xproc4j"
        },
        "typescript": {
          "scope": "source.ts",
          "contentName": "meta.embedded.block.typescript.xproc4j",
          "languageId": "typescript"
        }
      }
    },
    "attribute": [
      {
        "suffix": "eval-java",
        "scope": "source.java",
        "contentName": "meta.embedded.attribute.java.xproc4j"
      },
      {
        "suffix": "eval-js",
        "scope": "source.js",
        "contentName": "meta.embedded.attribute.javascript.xproc4j"
      },
      {
        "suffix": "eval-groovy",
        "scope": "source.groovy",
        "contentName": "meta.embedded.attribute.groovy.xproc4j"
      }
    ],
    "excludedBodyTags": [
      "lang-body",
      "lang-catch",
      "lang-finally",
      "script-segment",
      "lang-synchronized",
      "lang-async",
      "sql-scope",
      "sql-cursor",
      "sql-etl",
      "sql-transactional",
      "lang-eval",
      "lang-eval-ts",
      "lang-eval-tinyscript",
      "lang-eval-funic"
    ],
    "varsOnlyBodyTags": [
      "lang-eval",
      "lang-eval-ts",
      "lang-eval-tinyscript",
      "lang-eval-funic"
    ],
    "dynamicSqlTags": [
      "bind",
      "choose",
      "dialect",
      "dialect-choose",
      "dialect-otherwise",
      "dialect-when",
      "foreach",
      "if",
      "include",
      "otherwise",
      "set",
      "trim",
      "when",
      "where"
    ]
  },
  "enums": {
    "propagation": [
      "REQUIRED",
      "SUPPORTS",
      "MANDATORY",
      "REQUIRES_NEW",
      "NOT_SUPPORTED",
      "NEVER",
      "NESTED"
    ],
    "isolation": [
      "TRANSACTION_NONE",
      "TRANSACTION_READ_UNCOMMITTED",
      "TRANSACTION_READ_COMMITTED",
      "TRANSACTION_REPEATABLE_READ",
      "TRANSACTION_SERIALIZABLE"
    ],
    "booleanAttributes": [
      "read-only",
      "await",
      "params-share",
      "limited",
      "accept-batch",
      "before-truncate",
      "write-only",
      "enable",
      "disable",
      "jump-error",
      "full-send"
    ],
    "timeUnit": [
      "NANOSECONDS",
      "MICROSECONDS",
      "MILLISECONDS",
      "SECONDS",
      "MINUTES",
      "HOURS",
      "DAYS"
    ],
    "jdbcType": [
      "BIT",
      "TINYINT",
      "SMALLINT",
      "INTEGER",
      "BIGINT",
      "FLOAT",
      "REAL",
      "DOUBLE",
      "NUMERIC",
      "DECIMAL",
      "CHAR",
      "VARCHAR",
      "LONGVARCHAR",
      "DATE",
      "TIME",
      "TIMESTAMP",
      "BINARY",
      "VARBINARY",
      "LONGVARBINARY",
      "NULL",
      "OTHER",
      "JAVA_OBJECT",
      "DISTINCT",
      "STRUCT",
      "ARRAY",
      "BLOB",
      "CLOB",
      "REF",
      "DATALINK",
      "BOOLEAN",
      "ROWID",
      "NCHAR",
      "NVARCHAR",
      "LONGNVARCHAR",
      "NCLOB",
      "SQLXML",
      "REF_CURSOR",
      "TIME_WITH_TIMEZONE",
      "TIMESTAMP_WITH_TIMEZONE"
    ],
    "datePatterns": [
      "yyyy-MM-dd HH:mm:ss.SSS",
      "yyyy-MM-dd HH:mm:ss",
      "yyyy-MM-dd HH:mm",
      "yyyy-MM-dd",
      "yyyy-MM",
      "yyyy",
      "HH:mm:ss",
      "yyyyMMdd",
      "yyyyMM"
    ],
    "methods": [
      "replace",
      "replaceAll",
      "contains",
      "startsWith",
      "endsWith",
      "indexOf",
      "lastIndexOf",
      "substring",
      "parseInteger",
      "parseDouble",
      "parseLong",
      "parseFloat",
      "add",
      "containsKey",
      "remove",
      "get",
      "set",
      "Date.new",
      "String.valueOf",
      "size",
      "length"
    ],
    "databases": [
      "mysql",
      "oracle",
      "mariadb",
      "gbase",
      "dm",
      "postgre"
    ],
    "datasources": [
      "primary",
      "master",
      "main",
      "slave"
    ],
    "resultTypes": [
      "int",
      "long",
      "string",
      "boolean",
      "double",
      "float"
    ],
    "multiValueSeparators": {
      "databases": [
        ",",
        ";",
        "|",
        "/"
      ],
      "datasources": [
        ",",
        ";"
      ],
      "_lang": [
        ",",
        ";",
        "|",
        "/"
      ]
    },
    "languageIds": [
      "bash",
      "css",
      "el",
      "expr",
      "freemarker",
      "groovy",
      "java",
      "javascript",
      "js",
      "json",
      "markdown",
      "md",
      "regex",
      "regexp",
      "sass",
      "shell",
      "spel",
      "sql",
      "thymeleaf",
      "typescript",
      "velocity",
      "vtl",
      "xml",
      "yaml"
    ]
  },
  "keywords": {
    "sqlKeywords": [
      "SELECT",
      "FROM",
      "WHERE",
      "JOIN",
      "LEFT",
      "RIGHT",
      "INNER",
      "OUTER",
      "ON",
      "GROUP",
      "BY",
      "ORDER",
      "HAVING",
      "LIMIT",
      "OFFSET",
      "INSERT",
      "INTO",
      "VALUES",
      "UPDATE",
      "SET",
      "DELETE",
      "CREATE",
      "TABLE",
      "ALTER",
      "DROP",
      "INDEX",
      "VIEW",
      "UNION",
      "ALL",
      "DISTINCT",
      "AS",
      "AND",
      "OR",
      "NOT",
      "NULL",
      "IS",
      "IN",
      "EXISTS",
      "BETWEEN",
      "LIKE",
      "CASE",
      "WHEN",
      "THEN",
      "ELSE",
      "END",
      "COUNT",
      "SUM",
      "AVG",
      "MIN",
      "MAX",
      "COALESCE",
      "CAST",
      "WITH",
      "RECURSIVE",
      "BEGIN",
      "COMMIT",
      "ROLLBACK"
    ],
    "sqlFunctions": [
      "COUNT",
      "SUM",
      "AVG",
      "MIN",
      "MAX",
      "COALESCE",
      "CAST",
      "CONCAT",
      "SUBSTR",
      "LENGTH",
      "NOW",
      "DATE_FORMAT"
    ]
  },
  "snippets": [
    {
      "prefix": "<cdata",
      "description": "XProc4J <cdata",
      "body": "<![CDATA[\n   $0\n]]>",
      "sourceName": "cdata"
    },
    {
      "prefix": "<context-convert-method-class",
      "description": "XProc4J <context-convert-method-class",
      "body": "<context-convert-method-class class=\"${1}\"/>\n$0",
      "sourceName": "context-convert-method-class"
    },
    {
      "prefix": "<context-invoke-method-class",
      "description": "XProc4J <context-invoke-method-class",
      "body": "<context-invoke-method-class class=\"${1}\"/>\n$0",
      "sourceName": "context-invoke-method-class"
    },
    {
      "prefix": "<context-load-package",
      "description": "XProc4J <context-load-package",
      "body": "<context-load-package package=\"${1}\" class=\"${2}\"/>\n$0",
      "sourceName": "context-load-package"
    },
    {
      "prefix": "<debugger",
      "description": "XProc4J <debugger",
      "body": "<debugger tag=\"${1:$TM_FILENAME:$TM_LINE_NUMBER}\" test=\"${2:true}\"/>\n$0",
      "sourceName": "debugger"
    },
    {
      "prefix": "<etl-after",
      "description": "XProc4J <etl-after",
      "body": "<etl-after>\n    $0\n</etl-after>",
      "sourceName": "etl-after"
    },
    {
      "prefix": "<etl-before",
      "description": "XProc4J <etl-before",
      "body": "<etl-before>\n    $0\n</etl-before>",
      "sourceName": "etl-before"
    },
    {
      "prefix": "<etl-extra",
      "description": "XProc4J <etl-extra",
      "body": "<etl-extra datasource=\"${1:oracle}\" table=\"${2:sys_user}\"/>\n$0",
      "sourceName": "etl-extra"
    },
    {
      "prefix": "<etl-load",
      "description": "XProc4J <etl-load",
      "body": "<etl-load datasource=\"${1}\" table=\"${2:sys_user}\"/>\n$0",
      "sourceName": "etl-load"
    },
    {
      "prefix": "<etl-transform",
      "description": "XProc4J <etl-transform",
      "body": "<etl-transform source${1}=\"${2}\" target=\"${3}\"/>\n$0",
      "sourceName": "etl-transform"
    },
    {
      "prefix": "<event-publish",
      "description": "XProc4J <event-publish",
      "body": "<event-publish event-type=\"${1:log}\" ${2}/>\n$0",
      "sourceName": "event-publish"
    },
    {
      "prefix": "<event-send",
      "description": "XProc4J <event-send",
      "body": "<event-send event-type=\"${1:log}\" ${2}/>\n$0",
      "sourceName": "event-send"
    },
    {
      "prefix": "<function-call",
      "description": "XProc4J <function-call",
      "body": "<function-call refid=\"${1}\" result=\"${2}\" ${3:params_share=\\\\\"true\\\\\"}\n    ${4:params=\\\\\"callParams\\\\\"} ${5}/>\n$0",
      "sourceName": "function-call"
    },
    {
      "prefix": "<java-call",
      "description": "XProc4J <java-call",
      "body": "<java-call result=\"${1}\" target=\"${2}\"/>\n$0",
      "sourceName": "java-call"
    },
    {
      "prefix": "<lang-ai",
      "description": "XProc4J <lang-ai",
      "body": "<lang-ai result${1}=\"${2}\" type=\"${3:openai}\" value${4:.render}=\"${5}\" role${6:.string}=\"${7}\">\n    $0\n</lang-ai>",
      "sourceName": "lang-ai"
    },
    {
      "prefix": "<lang-async",
      "description": "XProc4J <lang-async",
      "body": "<lang-async ${1:await=\\\\\"false\\\\\"} ${2:delay=\\\\\"0\\\\\"} ${3:time-unit=\\\\\"SECONDS\\\\\"}>\n    $0\n</lang-async>",
      "sourceName": "lang-async"
    },
    {
      "prefix": "<lang-async-all",
      "description": "XProc4J <lang-async-all",
      "body": "<lang-async-all ${1:await=\\\\\"true\\\\\"}>\n    <lang-body>\n        $0\n    </lang-body>\n</lang-async-all>",
      "sourceName": "lang-async-all"
    },
    {
      "prefix": "<lang-break",
      "description": "XProc4J <lang-break",
      "body": "<lang-break/>$0",
      "sourceName": "lang-break"
    },
    {
      "prefix": "<lang-catch",
      "description": "XProc4J <lang-catch",
      "body": "<lang-catch type=\"${1:RuntimeException}\" e=\"${2:e}\">\n    $0\n</lang-catch>",
      "sourceName": "lang-catch"
    },
    {
      "prefix": "<lang-choose",
      "description": "XProc4J <lang-choose",
      "body": "<lang-choose>\n    <lang-when test${1}=\"${2:true}\">\n        $0\n    </lang-when>\n    <lang-otherwise>\n\n    </lang-otherwise>\n</lang-choose>",
      "sourceName": "lang-choose"
    },
    {
      "prefix": "<lang-continue",
      "description": "XProc4J <lang-continue",
      "body": "<lang-continue/>$0",
      "sourceName": "lang-continue"
    },
    {
      "prefix": "<lang-do-while",
      "description": "XProc4J <lang-do-while",
      "body": "<lang-do-while test${1}=\"${2}\">\n    $0\n</lang-do-while>",
      "sourceName": "lang-do-while"
    },
    {
      "prefix": "<lang-eval",
      "description": "XProc4J <lang-eval",
      "body": "<lang-eval result=\"${1}\" ${2:value=\\\\\"\\\\\"}>\n    ${3}\n</lang-eval>\n$0",
      "sourceName": "lang-eval"
    },
    {
      "prefix": "<lang-eval-groovy",
      "description": "XProc4J <lang-eval-groovy",
      "body": "<lang-eval-groovy result=\"${1}\">\n    $0\n</lang-eval-groovy>",
      "sourceName": "lang-eval-groovy"
    },
    {
      "prefix": "<lang-eval-java",
      "description": "XProc4J <lang-eval-java",
      "body": "<lang-eval-java result=\"${1}\">\n    $0\n    return null;\n</lang-eval-java>",
      "sourceName": "lang-eval-java"
    },
    {
      "prefix": "<lang-eval-javascript",
      "description": "XProc4J <lang-eval-javascript",
      "body": "<lang-eval-javascript result=\"${1}\">\n    $0\n</lang-eval-javascript>",
      "sourceName": "lang-eval-javascript"
    },
    {
      "prefix": "<lang-eval-js",
      "description": "XProc4J <lang-eval-js",
      "body": "<lang-eval-js result=\"${1}\">\n    $0\n</lang-eval-js>",
      "sourceName": "lang-eval-js"
    },
    {
      "prefix": "<lang-eval-tinyscript",
      "description": "XProc4J <lang-eval-tinyscript",
      "body": "<lang-eval-tinyscript result=\"${1}\">\n    $0\n</lang-eval-tinyscript>",
      "sourceName": "lang-eval-tinyscript"
    },
    {
      "prefix": "<lang-eval-ts",
      "description": "XProc4J <lang-eval-ts",
      "body": "<lang-eval-ts result=\"${1}\">\n    $0\n</lang-eval-ts>",
      "sourceName": "lang-eval-ts"
    },
    {
      "prefix": "<lang-file-delete",
      "description": "XProc4J <lang-file-delete",
      "body": "<lang-file-delete file${1}=\"${2}\"/>\n$0",
      "sourceName": "lang-file-delete"
    },
    {
      "prefix": "<lang-file-exists",
      "description": "XProc4J <lang-file-exists",
      "body": "<lang-file-exists result${1}=\"${2:v_result}\" file${3}=\"${4}\"/>\n$0",
      "sourceName": "lang-file-exists"
    },
    {
      "prefix": "<lang-file-list",
      "description": "XProc4J <lang-file-list",
      "body": "<lang-file-list result${1}=\"${2:v_files}\" file${3}=\"${4}\"/>\n$0",
      "sourceName": "lang-file-list"
    },
    {
      "prefix": "<lang-file-mkdirs",
      "description": "XProc4J <lang-file-mkdirs",
      "body": "<lang-file-mkdirs file${1}=\"${2}\"/>\n$0",
      "sourceName": "lang-file-mkdirs"
    },
    {
      "prefix": "<lang-file-read-text",
      "description": "XProc4J <lang-file-read-text",
      "body": "<lang-file-read-text result${1}=\"${2:v_text}\" file${3}=\"${4}\"/>\n$0",
      "sourceName": "lang-file-read-text"
    },
    {
      "prefix": "<lang-file-tree",
      "description": "XProc4J <lang-file-tree",
      "body": "<lang-file-tree result${1}=\"${2:v_files}\" file${3}=\"${4}\"/>\n$0",
      "sourceName": "lang-file-tree"
    },
    {
      "prefix": "<lang-file-write-text",
      "description": "XProc4J <lang-file-write-text",
      "body": "<lang-file-write-text content${1}=\"${2:v_text}\" file${3}=\"${4:v_file}\"/>\n$0",
      "sourceName": "lang-file-write-text"
    },
    {
      "prefix": "<lang-finally",
      "description": "XProc4J <lang-finally",
      "body": " <lang-finally>\n    $0\n</lang-finally>",
      "sourceName": "lang-finally"
    },
    {
      "prefix": "<lang-foreach",
      "description": "XProc4J <lang-foreach",
      "body": "<lang-foreach collection=\"${1:list}\" item=\"${2:item}\">\n    $0\n</lang-foreach>",
      "sourceName": "lang-foreach"
    },
    {
      "prefix": "<lang-fori",
      "description": "XProc4J <lang-fori",
      "body": "<lang-fori item=\"${1:item}\" begin.int=\"${2:0}\" end.int=\"${3:10}\" incr.int=\"${4:1}\">\n    $0\n</lang-fori>",
      "sourceName": "lang-fori"
    },
    {
      "prefix": "<lang-format",
      "description": "XProc4J <lang-format",
      "body": "<lang-format result=\"${1}\" value${2}=\"\" pattern=\"${3:%s}\"/>\n$0",
      "sourceName": "lang-format"
    },
    {
      "prefix": "<lang-format-date",
      "description": "XProc4J <lang-format-date",
      "body": "<lang-format-date result=\"${1}\" value${2}=\"${3}\" pattern=\"${4:yyyy-MM-dd HH:mm:ss}\"/>\n$0",
      "sourceName": "lang-format-date"
    },
    {
      "prefix": "<lang-if",
      "description": "XProc4J <lang-if",
      "body": "<lang-if test${1}=\"${2:true}\">\n    $0\n</lang-if>",
      "sourceName": "lang-if"
    },
    {
      "prefix": "<lang-invoke",
      "description": "XProc4J <lang-invoke",
      "body": "<lang-invoke result=\"${1}\" ${2:target=\\\\\"\\\\\"} method=\"${3:replace}\" arg0=\"${4}\" ${5}/>\n$0",
      "sourceName": "lang-invoke"
    },
    {
      "prefix": "<lang-java-body",
      "description": "XProc4J <lang-java-body",
      "body": "<lang-java-body>\n    $0\n</lang-java-body>",
      "sourceName": "lang-java-body"
    },
    {
      "prefix": "<lang-java-import",
      "description": "XProc4J <lang-java-import",
      "body": "<lang-java-import>\n    $0\n</lang-java-import>",
      "sourceName": "lang-java-import"
    },
    {
      "prefix": "<lang-java-member",
      "description": "XProc4J <lang-java-member",
      "body": "<lang-java-member>\n    $0\n</lang-java-member>",
      "sourceName": "lang-java-member"
    },
    {
      "prefix": "<lang-latch",
      "description": "XProc4J <lang-latch",
      "body": "<lang-latch result=\"${1}\" count${2}=\"${3:1}\"/>\n$0",
      "sourceName": "lang-latch"
    },
    {
      "prefix": "<lang-latch-await",
      "description": "XProc4J <lang-latch-await",
      "body": "<lang-latch-await name=\"${1:v_latch}\"/>\n$0",
      "sourceName": "lang-latch-await"
    },
    {
      "prefix": "<lang-latch-down",
      "description": "XProc4J <lang-latch-down",
      "body": "<lang-latch-down name=\"${1:v_latch}\"/>\n$0",
      "sourceName": "lang-latch-down"
    },
    {
      "prefix": "<lang-listener",
      "description": "XProc4J <lang-listener",
      "body": "<lang-listener name=\"${1}\" type=\"${2}\" target=\"${3:event}\">\n    $0    \n</lang-listener>",
      "sourceName": "lang-listener"
    },
    {
      "prefix": "<lang-lock",
      "description": "XProc4J <lang-lock",
      "body": "<lang-lock type=\"${1:jvm}\" value${2:.render}=\"${3}\">\n     $0\n</lang-lock>",
      "sourceName": "lang-lock"
    },
    {
      "prefix": "<lang-new-params",
      "description": "XProc4J <lang-new-params",
      "body": "<lang-new-params result=\"${1:callParams}\" ${2}/>\n$0",
      "sourceName": "lang-new-params"
    },
    {
      "prefix": "<lang-otherwise",
      "description": "XProc4J <lang-otherwise",
      "body": "<lang-otherwise>\n    $0\n</lang-otherwise>",
      "sourceName": "lang-otherwise"
    },
    {
      "prefix": "<lang-printf",
      "description": "XProc4J <lang-printf",
      "body": "<lang-printf value${1:.string}=\"${2}\"/>\n$0",
      "sourceName": "lang-printf"
    },
    {
      "prefix": "<lang-println",
      "description": "XProc4J <lang-println",
      "body": "<lang-println tag=\"${1:$TM_FILENAME:$TM_LINE_NUMBER}\" ${2}/>\n$0",
      "sourceName": "lang-println"
    },
    {
      "prefix": "<lang-render",
      "description": "XProc4J <lang-render",
      "body": "<lang-render result${1}=\"${2}\" _lang=\"${3:vtl}\">\n    ${4}\n</lang-render>\n$0",
      "sourceName": "lang-render"
    },
    {
      "prefix": "<lang-retry",
      "description": "XProc4J <lang-retry",
      "body": "<lang-retry count${1:.int}=\"${2:3}\" delay=\"${3:1}\" incr=\"${4:1.2}\" time-unit=\"${5:SECONDS}\">\n      $0 \n</lang-retry>",
      "sourceName": "lang-retry"
    },
    {
      "prefix": "<lang-return",
      "description": "XProc4J <lang-return",
      "body": "<lang-return ${1:value=\\\\\"\\\\\"}/>\n$0",
      "sourceName": "lang-return"
    },
    {
      "prefix": "<lang-set",
      "description": "XProc4J <lang-set",
      "body": "<lang-set result${1}=\"${2}\" value${3}=\"${4:callParams.}\"/>\n$0",
      "sourceName": "lang-set"
    },
    {
      "prefix": "<lang-shell",
      "description": "XProc4J <lang-shell",
      "body": "<lang-shell result${1}=\"${2:v_result}\" script${3:.string}=\"${4}\"/>\n$0",
      "sourceName": "lang-shell"
    },
    {
      "prefix": "<lang-sleep",
      "description": "XProc4J <lang-sleep",
      "body": "<lang-sleep timeout=\"${1:1}\" time-unit=\"${2:SECONDS}\"/>\n$0",
      "sourceName": "lang-sleep"
    },
    {
      "prefix": "<lang-string",
      "description": "XProc4J <lang-string",
      "body": "<lang-string result${1}=\"${2}\" _lang=\"${3:shell}\">\n    ${4}\n</lang-string>\n$0",
      "sourceName": "lang-string"
    },
    {
      "prefix": "<lang-string-join",
      "description": "XProc4J <lang-string-join",
      "body": "<lang-string-join result=\"${1}\" ${2:separator=\\\\\",\\\\\"} arg0=\"${3}\"/>\n$0",
      "sourceName": "lang-string-join"
    },
    {
      "prefix": "<lang-synchronized",
      "description": "XProc4J <lang-synchronized",
      "body": "<lang-synchronized target=\"${1:global}\">\n    $0\n</lang-synchronized>",
      "sourceName": "lang-synchronized"
    },
    {
      "prefix": "<lang-thread-pool",
      "description": "XProc4J <lang-thread-pool",
      "body": "<lang-thread-pool result=\"${1:V_POOL}\" count${2}=\"${3:10}\" type=\"${4:forkjoin}\"/>",
      "sourceName": "lang-thread-pool"
    },
    {
      "prefix": "<lang-thread-pool-shutdown",
      "description": "XProc4J <lang-thread-pool-shutdown",
      "body": "<lang-thread-pool-shutdown pool=\"${1:V_POOL}\" await=\"${2:true}\"/>\n$0",
      "sourceName": "lang-thread-pool-shutdown"
    },
    {
      "prefix": "<lang-thread-pool-submit",
      "description": "XProc4J <lang-thread-pool-submit",
      "body": "<lang-thread-pool-submit pool=\"${1:V_POOL}\" ${2}>\n    $0\n</lang-thread-pool-submit>",
      "sourceName": "lang-thread-pool-submit"
    },
    {
      "prefix": "<lang-throw",
      "description": "XProc4J <lang-throw",
      "body": "<lang-throw type=\"${1:RuntimeException}\" value=\"${2:error at $TM_FILENAME:$TM_LINE_NUMBER}\" ${3:cause=\\\\\"e\\\\\"}/>\n$0",
      "sourceName": "lang-throw"
    },
    {
      "prefix": "<lang-try",
      "description": "XProc4J <lang-try",
      "body": "<lang-try>\n    <lang-body>\n        $0\n    </lang-body>\n    <lang-catch type=\"${2:RuntimeException}\" e=\"${3:e}\">\n\n    </lang-catch>\n    <lang-finally>\n\n    </lang-finally>\n</lang-try>",
      "sourceName": "lang-try"
    },
    {
      "prefix": "<lang-when",
      "description": "XProc4J <lang-when",
      "body": "<lang-when test${1}=\"${2:true}\">\n    $0\n</lang-when>",
      "sourceName": "lang-when"
    },
    {
      "prefix": "<lang-while",
      "description": "XProc4J <lang-while",
      "body": "<lang-while test=\"${1:false}\">\n    $0\n</lang-while>",
      "sourceName": "lang-while"
    },
    {
      "prefix": "<log-debug",
      "description": "XProc4J <log-debug",
      "body": "<log-debug value${1:.render}=\"${2}\"/>\n$0",
      "sourceName": "log-debug"
    },
    {
      "prefix": "<log-error",
      "description": "XProc4J <log-error",
      "body": "<log-error value${1:.render}=\"${2}\"/>\n$0",
      "sourceName": "log-error"
    },
    {
      "prefix": "<log-info",
      "description": "XProc4J <log-info",
      "body": "<log-info value${1:.render}=\"${2}\"/>\n$0",
      "sourceName": "log-info"
    },
    {
      "prefix": "<log-warn",
      "description": "XProc4J <log-warn",
      "body": "<log-warn value${1:.render}=\"${2}\"/>\n$0",
      "sourceName": "log-warn"
    },
    {
      "prefix": "<procedure",
      "description": "XProc4J <procedure",
      "body": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE procedure SYSTEM \"procedure.dtd\">\n\n<procedure id=\"${1:procedure}\">\n    <!--\n\n    @author ${2}\n    @date $CURRENT_YEAR-$CURRENT_MONTH-$CURRENT_DATE\n    @time $CURRENT_HOUR:$CURRENT_MINUTE:$CURRENT_SECOND\n    -->\n\n    $0\n</procedure>",
      "sourceName": "procedure"
    },
    {
      "prefix": "<procedure-call",
      "description": "XProc4J <procedure-call",
      "body": "<procedure-call refid=\"${1}\" \n    result=\"${2:callParams}\"\n    ${3}/>\n$0",
      "sourceName": "procedure-call"
    },
    {
      "prefix": "<script-include",
      "description": "XProc4J <script-include",
      "body": "<script-include refid=\"${1:script}\" \n    ${2}/>\n$0",
      "sourceName": "script-include"
    },
    {
      "prefix": "<script-segment",
      "description": "XProc4J <script-segment",
      "body": "<script-segment id=\"${1:script}\" \n    ${2}>\n    $0\n</script-segment>",
      "sourceName": "script-segment"
    },
    {
      "prefix": "<sql-cursor",
      "description": "XProc4J <sql-cursor",
      "body": "<sql-cursor item=\"${1:item}\">\n    <sql-query-list>\n        ${2}\n    </sql-query-list>\n    <lang-body>\n        $0\n    </lang-body>\n</sql-cursor>\n",
      "sourceName": "sql-cursor"
    },
    {
      "prefix": "<sql-dialect",
      "description": "XProc4J <sql-dialect",
      "body": "<sql-dialect databases=\"${1:mysql,mariadb}\">\n    $0\n</sql-dialect>",
      "sourceName": "sql-dialect"
    },
    {
      "prefix": "<sql-etl",
      "description": "XProc4J <sql-etl",
      "body": "<sql-etl>\n    <sql-query-list datasource=\"${1:oracle}\">\n        $0\n    </sql-query-list>\n    <etl-load datasource=\"${3:mysql}\" table=\"${4:sys_user}\"/>\n</sql-etl>",
      "sourceName": "sql-etl"
    },
    {
      "prefix": "<sql-query-list",
      "description": "XProc4J <sql-query-list",
      "body": "<sql-query-list result=\"${1}\">\n    $0\n</sql-query-list>",
      "sourceName": "sql-query-list"
    },
    {
      "prefix": "<sql-query-object",
      "description": "XProc4J <sql-query-object",
      "body": "<sql-query-object result=\"${1}\">\n    $0\n</sql-query-object>",
      "sourceName": "sql-query-object"
    },
    {
      "prefix": "<sql-query-row",
      "description": "XProc4J <sql-query-row",
      "body": "<sql-query-row result=\"${1}\">\n    $0\n</sql-query-row>",
      "sourceName": "sql-query-row"
    },
    {
      "prefix": "<sql-runner",
      "description": "XProc4J <sql-runner",
      "body": "<sql-runner ${1:jump-error=\\\\\"false\\\\\"}>\n    $0\n</sql-runner>",
      "sourceName": "sql-runner"
    },
    {
      "prefix": "<sql-scope",
      "description": "XProc4J <sql-scope",
      "body": "<sql-scope datasources=\"${1}\">\n    $0\n</sql-scope>",
      "sourceName": "sql-scope"
    },
    {
      "prefix": "<sql-script",
      "description": "XProc4J <sql-script",
      "body": "<sql-script result=\"${1:v_sql}\">\n    $0\n</sql-script>",
      "sourceName": "sql-script"
    },
    {
      "prefix": "<sql-transactional",
      "description": "XProc4J <sql-transactional",
      "body": "<sql-transactional datasources${1}=\"${2:all}\"\n                       rollback-for=\"RuntimeException\">\n    $0\n</sql-transactional>",
      "sourceName": "sql-transactional"
    },
    {
      "prefix": "<sql-update",
      "description": "XProc4J <sql-update",
      "body": "<sql-update result=\"${1}\">\n    $0\n</sql-update>",
      "sourceName": "sql-update"
    }
  ]
};
