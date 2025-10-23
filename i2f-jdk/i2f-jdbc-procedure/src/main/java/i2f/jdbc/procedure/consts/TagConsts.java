package i2f.jdbc.procedure.consts;

/**
 * @author Ice2Faith
 * @date 2025/4/2 19:10
 */
public interface TagConsts {

    String PROCEDURE = "procedure";

    String DEBUGGER = "debugger";
    String LANG_PRINTF = "lang-printf";
    String LANG_PRINTLN = "lang-println";

    String LANG_STRING = "lang-string";
    String LANG_RENDER = "lang-render";
    String LANG_STRING_JOIN = "lang-string-join";

    String LANG_SET = "lang-set";

    String LANG_FORMAT_DATE = "lang-format-date";
    String LANG_FORMAT = "lang-format";

    String LANG_INVOKE = "lang-invoke";

    String LANG_SHELL = "lang-shell";

    String LANG_FILE_EXISTS = "lang-file-exists";
    String LANG_FILE_MKDIRS = "lang-file-mkdirs";
    String LANG_FILE_DELETE = "lang-file-delete";
    String LANG_FILE_LIST = "lang-file-list";
    String LANG_FILE_TREE = "lang-file-tree";
    String LANG_FILE_READ_TEXT = "lang-file-read-text";
    String LANG_FILE_WRITE_TEXT = "lang-file-write-text";

    String LANG_BODY = "lang-body";

    String LANG_EVAL = "lang-eval";

    String LANG_EVAL_JAVA = "lang-eval-java";
    String LANG_JAVA_IMPORT = "lang-java-import";
    String LANG_JAVA_MEMBE = "lang-java-member";
    String LANG_JAVA_BODY = "lang-java-body";

    String LANG_EVAL_GROOVY = "lang-eval-groovy";

    String LANG_EVAL_JAVASCRIPT = "lang-eval-javascript";
    String LANG_EVAL_JS = "lang-eval-js";

    String LANG_EVAL_TINYSCRIPT = "lang-eval-tinyscript";
    String LANG_EVAL_TS = "lang-eval-ts";

    String LANG_IF = "lang-if";
    String LANG_CHOOSE = "lang-choose";
    String LANG_WHEN = "lang-when";
    String LANG_OTHERWISE = "lang-otherwise";

    String LANG_WHILE = "lang-while";
    String LANG_FOREACH = "lang-foreach";
    String LANG_FORI = "lang-fori";

    String LANG_BREAK = "lang-break";
    String LANG_CONTINUE = "lang-continue";

    String LANG_TRY = "lang-try";
    String LANG_CATCH = "lang-catch";
    String LANG_FINALLY = "lang-finally";

    String LANG_THROW = "lang-throw";

    String LANG_ASYNC_ALL = "lang-async-all";
    String LANG_ASYNC = "lang-async";

    String LANG_SYNCHRONIZED = "lang-synchronized";
    String LANG_LOCK = "lang-lock";

    String LANG_LATCH = "lang-latch";
    String LANG_LATCH_AWAIT = "lang-latch-await";
    String LANG_LATCH_DOWN = "lang-latch-down";

    String LANG_SLEEP = "lang-sleep";

    String LANG_NEW_PARAMS = "lang-new-params";

    String PROCEDURE_CALL = "procedure-call";
    String FUNCTION_CALL = "function-call";
    String JAVA_CALL = "java-call";

    String LANG_RETURN = "lang-return";

    String SCRIPT_INCLUDE = "script-include";
    String SCRIPT_SEGMENT = "script-segment";

    String SQL_CURSOR = "sql-cursor";

    String SQL_ETL = "sql-etl";
    String ETL_EXTRA = "etl-extra";
    String ETL_TRANSFORM = "etl-transform";
    String ETL_LOAD = "etl-load";
    String ETL_BEFORE = "etl-before";
    String ETL_AFTER = "etl-after";

    String SQL_QUERY_LIST = "sql-query-list";
    String SQL_QUERY_OBJECT = "sql-query-object";
    String SQL_QUERY_ROW = "sql-query-row";
    String SQL_UPDATE = "sql-update";
    String SQL_QUERY_COLUMNS = "sql-query-columns";

    String SQL_DIALECT = "sql-dialect";
    String SQL_SCOPE = "sql-scope";
    String SQL_SCRIPT = "sql-script";

    String SQL_TRANS_BEGIN = "sql-trans-begin";
    String SQL_TRANS_COMMIT = "sql-trans-commit";
    String SQL_TRANS_NONE = "sql-trans-none";
    String SQL_TRANS_ROLLBACK = "sql-trans-rollback";

    String SQL_TRANSACTIONAL = "sql-transactional";

    String SQL_RUNNER = "sql-runner";

    String CONTEXT_CONVERT_METHOD_CLASS = "context-convert-method-class";
    String CONTEXT_INVOKE_METHOD_CLASS = "context-invoke-method-class";
    String CONTEXT_LOAD_PACKAGE = "context-load-package";

    String EVENT_SEND = "event-send";
    String EVENT_PUBLISH = "event-publish";
}
