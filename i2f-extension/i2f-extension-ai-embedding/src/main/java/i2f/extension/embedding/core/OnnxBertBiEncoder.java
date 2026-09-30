package i2f.extension.embedding.core;


import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

import java.io.*;
import java.nio.LongBuffer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/9/30 15:55
 * @desc copy from langchain4j
 */
public class OnnxBertBiEncoder implements AutoCloseable {
    private static final int MAX_SEQUENCE_LENGTH = 510;
    private final OrtEnvironment environment;
    private final OrtSession session;
    private final Set<String> expectedInputs;
    private final HuggingFaceTokenizer tokenizer;
    private final PoolingMode poolingMode;

    public OnnxBertBiEncoder(InputStream model, InputStream tokenizer, PoolingMode poolingMode) {
        try {
            this.environment = OrtEnvironment.getEnvironment();
            this.session = this.environment.createSession(this.loadModel(model));
            this.expectedInputs = this.session.getInputNames();
            this.tokenizer = HuggingFaceTokenizer.newInstance(tokenizer, Collections.singletonMap("padding", "false"));
            this.poolingMode = poolingMode;
            if (this.poolingMode == null) {
                throw new IllegalArgumentException("poolingMode cannot be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static OnnxBertBiEncoder of(InputStream model, InputStream tokenizer, PoolingMode poolingMode) {
        return new OnnxBertBiEncoder(model, tokenizer, poolingMode);
    }

    public static OnnxBertBiEncoder ofClasspath(String model, String tokenizer, PoolingMode poolingMode) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream modelIs = loader.getResourceAsStream(model);
        InputStream tokenizerIs = loader.getResourceAsStream(tokenizer);
        return of(modelIs, tokenizerIs, poolingMode);
    }

    public static OnnxBertBiEncoder ofFile(File model, File tokenizer, PoolingMode poolingMode) throws IOException {
        FileInputStream modelIs = new FileInputStream(model);
        FileInputStream tokenizerIs = new FileInputStream(tokenizer);
        return of(modelIs, tokenizerIs, poolingMode);
    }

    public EmbeddingAndTokenCount embed(String text) {
        List<String> tokens = this.tokenizer.tokenize(text);
        List<List<String>> partitions = partition(tokens, MAX_SEQUENCE_LENGTH);
        List<float[]> embeddings = new ArrayList<>();
        for (List<String> partition : partitions) {

            try {
                OrtSession.Result result = this.encode(partition);
                Throwable ex = null;

                try {
                    float[] embedding = this.toEmbedding(result);
                    embeddings.add(embedding);
                } catch (Throwable e) {
                    ex = e;
                    throw e;
                } finally {
                    if (result != null) {
                        if (ex != null) {
                            try {
                                result.close();
                            } catch (Throwable e) {
                                ex.addSuppressed(e);
                            }
                        } else {
                            result.close();
                        }
                    }

                }
            } catch (OrtException e) {
                throw new RuntimeException(e);
            }
        }

        List<Integer> weights = partitions.stream()
                .map(List::size)
                .collect(Collectors.toList());
        float[] embedding = normalize(this.weightedAverage(embeddings, weights));
        return new EmbeddingAndTokenCount(embedding, tokens.size());
    }

    @Override
    public void close() throws Exception {
        if(this.tokenizer!=null){
            this.tokenizer.close();
        }
        if(this.session!=null){
            this.session.close();
        }
        if(this.environment!=null){
            this.environment.close();
        }
    }

    private static List<List<String>> partition(List<String> tokens, int partitionSize) {
        List<List<String>> partitions = new ArrayList<>();

        int size = tokens.size();
        for (int from = 1; from < size - 1; from += partitionSize) {
            int to = Math.min(size - 1, from + partitionSize);
            List<String> partition = tokens.subList(from, to);
            partitions.add(partition);
        }

        return partitions;
    }

    private OrtSession.Result encode(List<String> tokens) throws OrtException {
        Encoding encoding = this.tokenizer.encode(this.toText(tokens), true, false);
        long[] inputIds = encoding.getIds();
        long[] attentionMask = encoding.getAttentionMask();
        long[] tokenTypeIds = encoding.getTypeIds();
        long[] shape = new long[]{1L, (long) inputIds.length};
        OnnxTensor inputIdsTensor = OnnxTensor.createTensor(this.environment, LongBuffer.wrap(inputIds), shape);
        Throwable ex = null;

        OrtSession.Result result=null;
        try {
            OnnxTensor attentionMaskTensor = OnnxTensor.createTensor(this.environment, LongBuffer.wrap(attentionMask), shape);
            Throwable tensorEx = null;

            try {
                OnnxTensor tokenTypeIdsTensor = OnnxTensor.createTensor(this.environment, LongBuffer.wrap(tokenTypeIds), shape);
                Throwable runEx = null;

                try {
                    Map<String, OnnxTensor> inputs = new HashMap<>();
                    inputs.put("input_ids", inputIdsTensor);
                    inputs.put("attention_mask", attentionMaskTensor);
                    if (this.expectedInputs.contains("token_type_ids")) {
                        inputs.put("token_type_ids", tokenTypeIdsTensor);
                    }

                    result = this.session.run(inputs);
                } catch (Throwable e) {
                    runEx = e;
                    throw e;
                } finally {
                    if (tokenTypeIdsTensor != null) {
                        if (runEx != null) {
                            try {
                                tokenTypeIdsTensor.close();
                            } catch (Throwable e) {
                                runEx.addSuppressed(e);
                            }
                        } else {
                            tokenTypeIdsTensor.close();
                        }
                    }

                }
            } catch (Throwable e) {
                tensorEx = e;
                throw e;
            } finally {
                if (attentionMaskTensor != null) {
                    if (tensorEx != null) {
                        try {
                            attentionMaskTensor.close();
                        } catch (Throwable e) {
                            tensorEx.addSuppressed(e);
                        }
                    } else {
                        attentionMaskTensor.close();
                    }
                }

            }
        } catch (Throwable e) {
            ex = e;
            throw e;
        } finally {
            if (inputIdsTensor != null) {
                if (ex != null) {
                    try {
                        inputIdsTensor.close();
                    } catch (Throwable e) {
                        ex.addSuppressed(e);
                    }
                } else {
                    inputIdsTensor.close();
                }
            }

        }

        return result;
    }

    private String toText(List<String> tokens) {
        String text = this.tokenizer.buildSentence(tokens);
        List<String> tokenized = this.tokenizer.tokenize(text);
        List<String> tokenizedWithoutSpecialTokens = new LinkedList<>(tokenized);
        tokenizedWithoutSpecialTokens.remove(0);
        tokenizedWithoutSpecialTokens.remove(tokenizedWithoutSpecialTokens.size() - 1);
        return tokenizedWithoutSpecialTokens.equals(tokens) ? text : String.join("", tokens);
    }

    private float[] toEmbedding(OrtSession.Result result) throws OrtException {
        float[][] vectors = ((float[][][]) ((float[][][]) result.get(0).getValue()))[0];
        return this.pool(vectors);
    }

    private float[] pool(float[][] vectors) {
        switch (this.poolingMode) {
            case CLS:
                return clsPool(vectors);
            case MEAN:
                return meanPool(vectors);
            default:
                throw new IllegalArgumentException("Unknown pooling mode: " + this.poolingMode);
        }
    }

    private static float[] clsPool(float[][] vectors) {
        return vectors[0];
    }

    private static float[] meanPool(float[][] vectors) {
        int numVectors = vectors.length;
        int vectorLength = vectors[0].length;
        float[] averagedVector = new float[vectorLength];

        for (int i = 0; i < vectors.length; ++i) {
            float[] vector = vectors[i];

            for (int j = 0; j < vectorLength; ++j) {
                averagedVector[j] += vector[j];
            }
        }

        for (int j = 0; j < vectorLength; ++j) {
            averagedVector[j] /= (float) numVectors;
        }

        return averagedVector;
    }

    private float[] weightedAverage(List<float[]> embeddings, List<Integer> weights) {
        if (embeddings.size() == 1) {
            return (float[]) embeddings.get(0);
        } else {
            int dimensions = ((float[]) embeddings.get(0)).length;
            float[] averagedEmbedding = new float[dimensions];
            int totalWeight = 0;

            for (int i = 0; i < embeddings.size(); ++i) {
                int weight = (Integer) weights.get(i);
                totalWeight += weight;

                for (int j = 0; j < dimensions; ++j) {
                    averagedEmbedding[j] += ((float[]) embeddings.get(i))[j] * (float) weight;
                }
            }

            for (int i = 0; i < dimensions; ++i) {
                averagedEmbedding[i] /= (float) totalWeight;
            }

            return averagedEmbedding;
        }
    }

    private static float[] normalize(float[] vector) {
        float sumSquare = 0.0F;

        for (int i = 0; i < vector.length; ++i) {
            float v = vector[i];
            sumSquare += v * v;
        }

        float norm = (float) Math.sqrt((double) sumSquare);
        float[] normalizedVector = new float[vector.length];

        for (int i = 0; i < vector.length; ++i) {
            normalizedVector[i] = vector[i] / norm;
        }

        return normalizedVector;
    }

    int countTokens(String text) {
        return this.tokenizer.tokenize(text).size();
    }

    private byte[] loadModel(InputStream modelInputStream) {
        try {
            InputStream inputStream = modelInputStream;
            Throwable ex = null;

            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                Throwable readEx = null;

                try {
                    byte[] data = new byte[1024];

                    int len=0;
                    while ((len = inputStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, len);
                    }

                    buffer.flush();
                    byte[] arr = buffer.toByteArray();
                    return arr;
                } catch (Throwable e) {
                    readEx = e;
                    throw e;
                } finally {
                    if (buffer != null) {
                        if (readEx != null) {
                            try {
                                buffer.close();
                            } catch (Throwable e) {
                                readEx.addSuppressed(e);
                            }
                        } else {
                            buffer.close();
                        }
                    }

                }
            } catch (Throwable e) {
                ex = e;
                throw e;
            } finally {
                if (inputStream != null) {
                    if (ex != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable e) {
                            ex.addSuppressed(e);
                        }
                    } else {
                        inputStream.close();
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

