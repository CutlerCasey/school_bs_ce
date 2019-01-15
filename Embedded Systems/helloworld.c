#include "xparameters.h"
#include "xgpio.h"
#include "xscugic.h"
#include "xil_exception.h"
#include "xil_printf.h"

// Parameter definitions
#define MAX_STEPS 1024 // change later

#define FOLLOW_DELAY 10000000 //  change later
#define REPEAT_DELAY 20000000 // change later

#define NEUTRAL_STEER_OUT 160 // change later
#define NEUTRAL_SPEED_OUT 8

#define MAX_SPEED_OUT 190
#define MAX_STEER_OUT 210

#define MIN_SPEED_OUT 0
#define MIN_STEER_OUT 110

#define STEER_PWM_DEVICE_ID 		XPAR_AXI_GPIO_0_DEVICE_ID
#define SPEED_PWM_DEVICE_ID			XPAR_AXI_GPIO_1_DEVICE_ID
//#define SWITCHES_DEVICE_ID			XPAR_AXI_GPIO_2_DEVICE_ID


XGpio steerPwmInst, speedPwmInst;

/*int speedSteps[MAX_STEPS];
int steerSteps[MAX_STEPS];

static int path_set = 0;

void wait_while_switch(int pos)
{
	int bit = 1;

	int i;
	for(i = 0; i < pos; i++)
	{
		bit *= 2;
	}

	unsigned switchesIn;
	do
	{
		switchesIn = XGpio_DiscreteRead(&switchesInst, 1);
	}
	while(switchesIn & bit);
}*/

int gpio_init()
{
	int status = XGpio_Initialize(&steerPwmInst, STEER_PWM_DEVICE_ID);
	if(status != XST_SUCCESS) return XST_FAILURE;
	XGpio_SetDataDirection(&steerPwmInst, 1, 0xFF);

	status = XGpio_Initialize(&speedPwmInst, SPEED_PWM_DEVICE_ID);
	if(status != XST_SUCCESS) return XST_FAILURE;
	XGpio_SetDataDirection(&speedPwmInst, 1, 0xFF);

	/*status = XGpio_Initialize(&switchesInst, SWITCHES_DEVICE_ID);
	if(status != XST_SUCCESS) return XST_FAILURE;
	XGpio_SetDataDirection(&switchesInst, 1, 0xFF);*/

	return XST_SUCCESS;
}
/*
void get_path()
{
	// read from UART into speedSteps and steerSteps
	path_set = 1;
	XGpio_DiscreteWrite(&steerPwmInst, 1, 180);
	XGpio_DiscreteWrite(&speedPwmInst, 1, 180);
	//xil_printf("test\n");
	wait_while_switch(0);
}*/
/*
void drive()
{
	XGpio_DiscreteWrite(&steerPwmInst, 1, 150);
	wait_while_switch(1);
}*/

int main(void){
	int speedHold = 8;
	int steerHold = 8;
	/* int mod, count;
	mod = 12;
	count = 0; */
	if(gpio_init() != XST_SUCCESS)
	{
		return XST_FAILURE;
	}
	xil_printf("Hope this somewhat works!  "); //hello world

	while(1)
	{
		/* if (count < (mod * 4)) {
			if (count % mod == 0) {

			}
		}
		else
			count++; */
		int speedTemp = XGpio_DiscreteRead(&speedPwmInst, 1);
		if (speedHold != speedTemp) {
			switch (speedTemp) {
				case 0 : xil_printf("speed: %04d, ", 0000); break;
				case 1 : xil_printf("speed: %04d, ", 0001); break;
				case 2 : xil_printf("speed: %04d, ", 0010); break;
				case 3 : xil_printf("speed: %04d, ", 0011); break;
				case 4 : xil_printf("speed: %04d, ", 0100); break;
				case 5 : xil_printf("speed: %04d, ", 0101); break;
				case 6 : xil_printf("speed: %04d, ", 0110); break;
				case 7 : xil_printf("speed: %04d, ", 0111); break;
				case 8 : xil_printf("speed: %04d, ", 1000); break;
				case 9 : xil_printf("speed: %04d, ", 1001); break;
				case 10 : xil_printf("speed: %04d, ", 1010); break;
				case 11 : xil_printf("speed: %04d, ", 1011); break;
				case 12 : xil_printf("speed: %04d, ", 1100); break;
				case 13 : xil_printf("speed: %04d, ", 1101); break;
				case 14 : xil_printf("speed: %04d, ", 1110); break;
				case 15 : xil_printf("speed: %04d, ", 1111); break;
				default : xil_printf("speed: %04d, ", 9999); break;
			}
			//xil_printf("speed: %4d, ", speedTemp);
			speedHold = speedTemp;
		}
		int steerTemp = XGpio_DiscreteRead(&steerPwmInst, 1);
		if (steerHold != steerTemp) {
			switch (steerTemp) {
				case 0 : xil_printf("steer: %04d, ", 0000); break;
				case 1 : xil_printf("steer: %04d, ", 0001); break;
				case 2 : xil_printf("steer: %04d, ", 0010); break;
				case 3 : xil_printf("steer: %04d, ", 0011); break;
				case 4 : xil_printf("steer: %04d, ", 0100); break;
				case 5 : xil_printf("steer: %04d, ", 0101); break;
				case 6 : xil_printf("steer: %04d, ", 0110); break;
				case 7 : xil_printf("steer: %04d, ", 0111); break;
				case 8 : xil_printf("steer: %04d, ", 1000); break;
				case 9 : xil_printf("steer: %04d, ", 1001); break;
				case 10 : xil_printf("steer: %04d, ", 1010); break;
				case 11 : xil_printf("steer: %04d, ", 1011); break;
				case 12 : xil_printf("steer: %04d, ", 1100); break;
				case 13 : xil_printf("steer: %04d, ", 1101); break;
				case 14 : xil_printf("steer: %04d, ", 1110); break;
				case 15 : xil_printf("steer: %04d, ", 1111); break;
				default : xil_printf("steer: %04d, ", 9999); break;
			}
			//xil_printf("steer: %4d, ", steerTemp);
			steerHold = steerTemp;
		}

		/* unsigned switchesIn = XGpio_DiscreteRead(&switchesInst, 1);
		if(switchesIn & 1)
		{
			get_path();
		}
		else if(switchesIn & 2)
		{
			if(path_set)
			{
				drive();
			}
		} */
	}

	return 0;
}
