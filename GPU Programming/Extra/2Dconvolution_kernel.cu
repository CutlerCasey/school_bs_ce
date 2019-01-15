#ifndef _2DCONVOLUTION_KERNEL_H_
#define _2DCONVOLUTION_KERNEL_H_

#include <stdio.h>
#include "2Dconvolution.h"

// Matrix multiplication kernel thread specification
__global__ void ConvolutionKernelSharedUnroll(Matrix filterd, Matrix Nd, Matrix Pd) {
        __shared__ float fsub[5][5];
        int tx = threadIdx.x; 
        int ty = threadIdx.y; 
        int row = 5 * blockIdx.y + ty;
        int col = 5 * blockIdx.x + tx;
        float value = 0;
        int tempx = 0;
        int tempy = 0;
	int Npitch = Nd.pitch;
	int Nwidth = Nd.width;
	int Nheight = Nd.height;

        fsub[tx][ty] = filterd.elements[ty*filterd.pitch + tx];		// Each thread in the block will import an element from the filter matrix
        __syncthreads();						// Wait until all threads have imported their element.

        for(int i = 0; i < 5; i++) {					// Do the matrix convolution. 
		tempy = row + i - 2;
                tempx = col - 2;
                if(tempx >= 0 && tempx < Nwidth && tempy >= 0 && tempy < Nheight)
                	value += Nd.elements[tempy*Npitch + tempx] * fsub[0][i];
		tempx++;
		if(tempx >= 0 && tempx < Nwidth && tempy >= 0 && tempy < Nheight)
                        value += Nd.elements[tempy*Npitch + tempx] * fsub[1][i];
		tempx++;
		if(tempx >= 0 && tempx < Nwidth && tempy >= 0 && tempy < Nheight)
                        value += Nd.elements[tempy*Npitch + tempx] * fsub[2][i];
		tempx++;
		if(tempx >= 0 && tempx < Nwidth && tempy >= 0 && tempy < Nheight)
                        value += Nd.elements[tempy*Npitch + tempx] * fsub[3][i];
		tempx++;
		if(tempx >= 0 && tempx < Nwidth && tempy >= 0 && tempy < Nheight)
                        value += Nd.elements[tempy*Npitch + tempx] * fsub[4][i];
        }
        if(col < Pd.width && row < Pd.height)
                Pd.elements[row*Pd.pitch + col] = value;
}


// Matrix multiplication kernel thread specification
__global__ void ConvolutionKernelShared(Matrix filterd, Matrix Nd, Matrix Pd) {
	
	__shared__ float fsub[5][5];

	int tx = threadIdx.x;
        int ty = threadIdx.y;
        int row = 5 * blockIdx.y + ty;
        int col = 5 * blockIdx.x + tx;
	float value = 0;
	int tempx = 0;
	int tempy = 0;

	fsub[ty][tx] = filterd.elements[ty*filterd.pitch + tx];
	__syncthreads();

	for(int i = 0; i < 5; i++) {
		for(int j = 0; j < 5; j++) {
			tempx = col + j - 2;
			tempy = row + i - 2;
			if(tempx >= 0 && tempx < Nd.width && tempy >= 0 && tempy < Nd.height)
				value += Nd.elements[tempy*Nd.pitch + tempx] * fsub[i][j];
		}
	}
	if(col < Pd.width && row < Pd.height)
		Pd.elements[row*Pd.pitch + col] = value;
}

// Matrix multiplication kernel thread specification
__global__ void ConvolutionKernel(Matrix filterd, Matrix Nd, Matrix Pd) {
        int tx = threadIdx.x;
        int ty = threadIdx.y;
        int row = 5 * blockIdx.y + ty;
        int col = 5 * blockIdx.x + tx;
        float value = 0;
        int tempx = 0;
        int tempy = 0;

        for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 5; j++) {
                        tempx = col + j - 2;
                        tempy = row + i - 2;
                        if(tempx >= 0 && tempx < Nd.width && tempy >= 0 && tempy < Nd.height)
                                value += Nd.elements[tempy*Nd.pitch + tempx] * filterd.elements[i*5 + j];
                }
        }
        if(col < Pd.width && row < Pd.height)
                Pd.elements[row*Pd.pitch + col] = value;
}


#endif // #ifndef _2DCONVOLUTION_KERNEL_H_
