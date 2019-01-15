/* Matrix multiplication: C = A * B.
 * Host code.
 */

// includes, system
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <sys/time.h>

// includes, project
#include <cutil.h>

// includes, kernels
#include <2Dconvolution_kernel.cu>

////////////////////////////////////////////////////////////////////////////////
// declarations, forward

extern "C"
void computeGold(float*, const float*, const float*, unsigned int, unsigned int);

Matrix AllocateDeviceMatrix(const Matrix M);
Matrix AllocateMatrix(int height, int width, int init);
void CopyToDeviceMatrix(Matrix Mdevice, const Matrix Mhost);
void CopyFromDeviceMatrix(Matrix Mhost, const Matrix Mdevice);
int ReadFile(Matrix* M, char* file_name);
void WriteFile(Matrix M, char* file_name);
void FreeDeviceMatrix(Matrix* M);
void FreeMatrix(Matrix* M);

void ConvolutionOnDevice(const Matrix M, const Matrix N, Matrix P);


struct timeval begin_time;
struct timeval end_time;

////////////////////////////////////////////////////////////////////////////////
// Program main
////////////////////////////////////////////////////////////////////////////////
int main(int argc, char** argv) {
	
	double GPU_process_time;
    double CPU_process_time;

    double GPU_start_time;
    double GPU_end_time;

    double CPU_start_time;
    double CPU_end_time;

	Matrix  M;
	Matrix  N;
	Matrix  P;
	
	int N_height = 16; 
	int N_width = 16; 
	
	srand(2012);
	
	if (argc >2 ) {
		N_height = atoi(argv[1]); 
		N_width = atoi(argv[2]); 	
	}
	
	 M  = AllocateMatrix(KERNEL_SIZE, KERNEL_SIZE, 1);
	 N  = AllocateMatrix(N_height, N_width, 1);
	 P  = AllocateMatrix(N.height, N.width, 0);

	// M * N on the device
	gettimeofday(&begin_time, NULL);
    ConvolutionOnDevice(M, N, P);
    gettimeofday(&end_time, NULL);
    
    GPU_start_time = (double)begin_time.tv_sec + ((double)begin_time.tv_usec)/1000000;
    GPU_end_time = (double)end_time.tv_sec + (double)end_time.tv_usec/1000000;
    GPU_process_time = GPU_end_time - GPU_start_time;
    
    printf("GPU computation complete\n");    
    printf("GPU Processing time = %f\n\n",GPU_process_time);
    
    // compute the matrix multiplication on the CPU for comparison
    gettimeofday(&begin_time, NULL);
    Matrix reference = AllocateMatrix(P.height, P.width, 0);
    computeGold(reference.elements, M.elements, N.elements, N.height, N.width);
    gettimeofday(&end_time, NULL);
    
    CPU_start_time = (double)begin_time.tv_sec + ((double)begin_time.tv_usec)/1000000;
    CPU_end_time = (double)end_time.tv_sec + (double)end_time.tv_usec/1000000;
    CPU_process_time = CPU_end_time - CPU_start_time;
    
    printf("CPU computation complete\n");
    printf("CPU Processing time = %f\n\n",CPU_process_time);    
    
        
    // in this case check if the result is equivalent to the expected soluion
    CUTBoolean res = cutComparefe(reference.elements, P.elements, P.width * P.height, 0.001f);
    printf("Test %s\n", (1 == res) ? "PASSED" : "FAILED");
    
	// Free matrices
    FreeMatrix(&M);
    FreeMatrix(&N);
    FreeMatrix(&P);
	return 0;
}


////////////////////////////////////////////////////////////////////////////////
//! Run a simple test for CUDA
////////////////////////////////////////////////////////////////////////////////
void ConvolutionOnDevice(const Matrix M, const Matrix N, Matrix P)
{
    // Load M and N to the device
    Matrix Md = AllocateDeviceMatrix(M);
    CopyToDeviceMatrix(Md, M);
    Matrix Nd = AllocateDeviceMatrix(N);
    CopyToDeviceMatrix(Nd, N);

    // Allocate P on the device
    Matrix Pd = AllocateDeviceMatrix(P);
    CopyToDeviceMatrix(Pd, P); // Clear memory


    
    printf("Nwidth = %d\n",N.width);
    printf("Nheight = %d\n",N.height);		

	    // Setup the execution configuration	
	/* your code starts here */
	
	dim3 DimBlock(5, 5);
	dim3 DimGrid((int)ceil((double)N.width / 5.0), (int)ceil((double)N.height / 5.0));
	ConvolutionKernelSharedUnroll<<< DimGrid, DimBlock >>> (Md, Nd, Pd);
	
	/* your code ends here */ 
	CopyFromDeviceMatrix(P, Pd); 	

    // Free device matrices
    FreeDeviceMatrix(&Md);
    FreeDeviceMatrix(&Nd);
    FreeDeviceMatrix(&Pd);

}

// Allocate a device matrix of same size as M.
Matrix AllocateDeviceMatrix(const Matrix M)
{
    Matrix Mdevice = M;
    int size = M.width * M.height * sizeof(float);
    cudaMalloc((void**)&Mdevice.elements, size);
    return Mdevice;
}

// Allocate a device matrix of dimensions height*width
//	If init == 0, initialize to all zeroes.  
//	If init == 1, perform random initialization.
//  If init == 2, initialize matrix parameters, but do not allocate memory 
Matrix AllocateMatrix(int height, int width, int init)
{
    Matrix M;
    M.width = M.pitch = width;
    M.height = height;
    int size = M.width * M.height;
    M.elements = NULL;
    
    // don't allocate memory on option 2
    if(init == 2)
		return M;
		
	M.elements = (float*) malloc(size*sizeof(float));

	for(unsigned int i = 0; i < M.height * M.width; i++)
	{
		M.elements[i] = (init == 0) ? (0.0f) : (rand() / (float)RAND_MAX);
		if(rand() % 2)
			M.elements[i] = - M.elements[i];
	}
    return M;
}	

// Copy a host matrix to a device matrix.
void CopyToDeviceMatrix(Matrix Mdevice, const Matrix Mhost)
{
    int size = Mhost.width * Mhost.height * sizeof(float);
    Mdevice.height = Mhost.height;
    Mdevice.width = Mhost.width;
    Mdevice.pitch = Mhost.pitch;
    cudaMemcpy(Mdevice.elements, Mhost.elements, size, 
					cudaMemcpyHostToDevice);
}

// Copy a device matrix to a host matrix.
void CopyFromDeviceMatrix(Matrix Mhost, const Matrix Mdevice)
{
    int size = Mdevice.width * Mdevice.height * sizeof(float);
    cudaMemcpy(Mhost.elements, Mdevice.elements, size, 
					cudaMemcpyDeviceToHost);
}

// Free a device matrix.
void FreeDeviceMatrix(Matrix* M)
{
    cudaFree(M->elements);
    M->elements = NULL;
}

// Free a host Matrix
void FreeMatrix(Matrix* M)
{
    free(M->elements);
    M->elements = NULL;
}

