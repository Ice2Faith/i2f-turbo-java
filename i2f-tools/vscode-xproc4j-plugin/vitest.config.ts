import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    include: ['test/unit/**/*.test.ts', 'test/grammar/**/*.test.ts'],
    environment: 'node',
    reporters: ['default'],
    testTimeout: 20000,
  },
});
