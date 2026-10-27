import { describe, expect, it } from 'vitest';
import { findToken, hasScope, loadXProcGrammar, tokenize } from '../harness';

const SAMPLE = `<?xml version="1.0" encoding="UTF-8"?>
<procedure id="demo">
    <log-info value="hello \${name}" />
    <sql-query-list result="rows" datasource="primary">
        select id, name from sys_user
        where id = \${id}
        <if test="id != null">and status = 1</if>
    </sql-query-list>
    <lang-string _lang="sql">select * from t</lang-string>
    <lang-string>plain \${value}</lang-string>
    <lang-eval-java result="x">
        int a = 1;
        <lang-java-body>return a;</lang-java-body>
    </lang-eval-java>
    <lang-eval-js result="y">const y = 1;</lang-eval-js>
    <lang-string _lang="no-such-lang">plain fallback</lang-string>
</procedure>
<sql-update/>
<foo bar="1"/>`;

const FOREIGN_SAMPLES: Record<string, string> = {
  pom: `<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <properties><java.version>17</java.version></properties>
    <dependencies>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>demo</artifactId>
            <version>\${demo.version}</version>
        </dependency>
    </dependencies>
</project>`,
  spring: `<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans">
    <bean id="demo" class="org.example.Demo">
        <property name="name" value="\${demo.name}"/>
    </bean>
</beans>`,
  mybatis: `<?xml version="1.0" encoding="UTF-8"?>
<mapper namespace="org.example.DemoMapper">
    <select id="find" resultType="Demo">
        select id, name from demo where id = #{id}
    </select>
    <update id="update">update demo set name = #{name}</update>
</mapper>`,
};

describe('S0 grammar verification', () => {
  it('scopes XProc tags and leaves unknown tags alone', async () => {
    const grammar = await loadXProcGrammar();
    const tokens = tokenize(grammar, SAMPLE).flat();
    const procedure = findToken(tokens, 'procedure');
    expect(procedure && hasScope(procedure, 'entity.name.tag.root.xproc4j')).toBe(true);
    const foo = findToken(tokens, 'foo');
    expect(foo && hasScope(foo, 'entity.name.tag.root.xproc4j')).toBe(false);
  });

  it('does not touch POM/Spring/MyBatis XML', async () => {
    const grammar = await loadXProcGrammar();
    for (const [name, sample] of Object.entries(FOREIGN_SAMPLES)) {
      const tokens = tokenize(grammar, sample).flat();
      const leaked = tokens.filter((token) =>
        token.scopes.some((scope) => scope.includes('xproc4j')),
      );
      expect(leaked, `${name} 不应出现 XProc4J scope`).toHaveLength(0);
    }
  });

  it('S0-02 scopes attributes of body-injected tags', async () => {
    const grammar = await loadXProcGrammar();
    const tokens = tokenize(grammar, SAMPLE).flat();
    const result = findToken(tokens, 'result');
    expect(result && hasScope(result, 'entity.other.attribute-name.xproc4j')).toBe(true);
  });

  it('scopes attributes of non-body tags including multiline start tags', async () => {
    const grammar = await loadXProcGrammar();
    const text = [
      '<procedure id="demo"',
      '           IN_X.string=""',
      '>',
      '  <lang-invoke method="format" result="str"/>',
      '  <lang-println tag="now"/>',
      '</procedure>',
    ].join('\n');
    const tokens = tokenize(grammar, text).flat();
    for (const name of ['id', 'IN_X.string', 'method', 'result', 'tag']) {
      const matched = tokens.filter((t) => t.text === name);
      expect(matched.length, name).toBeGreaterThan(0);
      expect(
        matched.every((t) => hasScope(t, 'entity.other.attribute-name.xproc4j')),
        name,
      ).toBe(true);
    }
    const value = findToken(tokens, 'format');
    expect(value && hasScope(value, 'string.quoted.xml')).toBe(true);
  });

  it('keeps nested sql-dialect as XML inside a SQL body', async () => {
    const grammar = await loadXProcGrammar();
    const text = [
      '<procedure>',
      '  <sql-query-object result="count">',
      '    <sql-dialect databases="mysql,gbase">',
      '      select 1',
      '    </sql-dialect>',
      '  </sql-query-object>',
      '</procedure>',
    ].join('\n');
    const tokens = tokenize(grammar, text).flat();
    const dialect = tokens.filter((t) => t.text === 'sql-dialect');
    expect(dialect.length).toBeGreaterThan(0);
    expect(dialect.every((t) => hasScope(t, 'entity.name.tag.sql.xproc4j'))).toBe(true);
    expect(
      dialect.every((t) => !t.scopes.some((s) => s.startsWith('keyword.operator.comparison.sql'))),
    ).toBe(true);
    const attr = findToken(tokens, 'databases');
    expect(attr && hasScope(attr, 'entity.other.attribute-name.xproc4j')).toBe(true);
    const sql = findToken(tokens, 'select');
    expect(sql && hasScope(sql, 'meta.embedded.block.sql.xproc4j')).toBe(true);
  });

  it('S0-01 injects SQL and scopes keywords', async () => {
    const grammar = await loadXProcGrammar();
    const tokens = tokenize(grammar, SAMPLE).flat();
    const select = findToken(tokens, 'select');
    expect(select && hasScope(select, 'meta.embedded.block.sql.xproc4j')).toBe(true);
    expect(select?.scopes.some((s) => s.includes('keyword'))).toBe(true);
  });

  it('injects Java and JavaScript bodies', async () => {
    const grammar = await loadXProcGrammar();
    const tokens = tokenize(grammar, SAMPLE).flat();
    const intToken = findToken(tokens, 'int');
    expect(intToken && hasScope(intToken, 'meta.embedded.block.java.xproc4j')).toBe(true);
    expect(intToken?.scopes.some((s) => s.includes('storage.type') || s.includes('keyword'))).toBe(true);
    const constToken = findToken(tokens, 'const');
    expect(constToken && hasScope(constToken, 'meta.embedded.block.javascript.xproc4j')).toBe(true);
    expect(constToken?.scopes.some((s) => s.includes('keyword') || s.includes('storage'))).toBe(true);
  });

  it('unknown _lang falls back without embedded language scope', async () => {
    const grammar = await loadXProcGrammar();
    const tokens = tokenize(grammar, SAMPLE).flat();
    const fallback = findToken(tokens, 'plain fallback');
    expect(fallback).toBeTruthy();
    expect(fallback?.scopes.some((s) => s.includes('meta.embedded.block.sql'))).toBe(false);
    expect(fallback?.scopes.some((s) => s.includes('meta.embedded.block.java'))).toBe(false);
    expect(fallback?.scopes.some((s) => s.includes('meta.embedded.block.javascript'))).toBe(false);
  });

  it('S0-05 scopes variables in bodies', async () => {
    const grammar = await loadXProcGrammar();
    const tokens = tokenize(grammar, SAMPLE).flat();
    const variable = findToken(tokens, '${id}');
    expect(variable && hasScope(variable, 'support.variable.xproc4j')).toBe(true);
  });

  it('scopes ${}, #{}, $!{} and #!{} variable forms', async () => {
    const grammar = await loadXProcGrammar();
    const text = [
      '<procedure id="p">',
      '  <lang-eval-ts>a=${a}; b=#{b}; c=$!{c}; d=#!{d}; e=$e; empty=${}',
      '  </lang-eval-ts>',
      '</procedure>',
    ].join('\n');
    const tokens = tokenize(grammar, text).flat();
    for (const value of ['${a}', '#{b}', '$!{c}', '#!{d}', '$e']) {
      const token = findToken(tokens, value);
      expect(token && hasScope(token, 'support.variable.xproc4j'), value).toBe(true);
    }
    const empty = findToken(tokens, '${}');
    expect(empty && hasScope(empty, 'invalid.illegal.xproc4j')).toBe(true);
  });

  it('S0-06 self-closing body tag does not leak embedded scope', async () => {
    const grammar = await loadXProcGrammar();
    const tokens = tokenize(grammar, SAMPLE).flat();
    const foo = findToken(tokens, 'foo');
    expect(foo?.scopes.some((s) => s.includes('meta.embedded'))).toBe(false);
  });

  it('S0-07 _lang switches lang-string body to SQL', async () => {
    const grammar = await loadXProcGrammar();
    const tokens = tokenize(grammar, SAMPLE).flat();
    const sqlSelects = tokens.filter(
      (t) => t.text === 'select' && t.scopes.some((s) => s.includes('meta.embedded.block.sql')),
    );
    // 一处来自 sql-query-list，一处来自 _lang="sql" 的 lang-string。
    expect(sqlSelects.length).toBe(2);
    const plainTokens = tokens.filter((t) => t.text.includes('plain'));
    expect(plainTokens.every((t) => t.scopes.every((s) => !s.includes('meta.embedded.block.sql')))).toBe(true);
  });
});
