#include <cstdio>
#include <cstdlib>
#include <cuda_runtime.h>

__global__ void addk(const float *a, const float *b, float *c, int n) {
    int i = blockIdx.x * blockDim.x + threadIdx.x;
    if (i < n) c[i] = a[i] + b[i];
}

int main() {
    const int n = 1024;
    size_t sz = n * sizeof(float);
    float *a, *b, *c;
    if (cudaMalloc((void**)&a, sz) != cudaSuccess) { printf("malloc fail\n"); return 1; }
    cudaMalloc((void**)&b, sz);
    cudaMalloc((void**)&c, sz);
    float *h = (float*)malloc(sz);
    for (int i = 0; i < n; i++) h[i] = 1.0f;
    cudaMemcpy(a, h, sz, cudaMemcpyHostToDevice);
    cudaMemcpy(b, h, sz, cudaMemcpyHostToDevice);
    addk<<<(n + 127) / 128, 128>>>(a, b, c, n);
    cudaError_t e = cudaDeviceSynchronize();
    if (e != cudaSuccess) { printf("KERNEL FAIL: %s\n", cudaGetErrorString(e)); return 1; }
    cudaMemcpy(h, c, sz, cudaMemcpyDeviceToHost);
    cudaDeviceProp p;
    cudaGetDeviceProperties(&p, 0);
    printf("kernel OK: c[0]=%.1f  device=%s  sm_%d%d\n", h[0], p.name, p.major, p.minor);
    return 0;
}