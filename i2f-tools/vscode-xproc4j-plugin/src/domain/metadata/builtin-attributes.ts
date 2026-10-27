/** 补全兜底与参数索引都会用到的通用属性。 */
export const BUILTIN_ATTRIBUTES = [
  '_lang',
  'id',
  'refid',
  '__file',
  '__line',
  'return',
  'result',
  'value',
] as const;

/** procedure/script-segment 上不作为业务参数传播的属性。 */
export const RESERVED_PARAM_NAMES: ReadonlySet<string> = new Set([
  'id',
  'refid',
  'params',
  'params-share',
  '_lang',
  '__file',
  '__line',
]);
