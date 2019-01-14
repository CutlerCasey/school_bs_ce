//test the ALU
`timescale 1ns/100ps
module memoryTB;
	//all opCodes
	parameter
		paraNop = 4'b0000,
		paraAdd = 4'b0001, paraSub = 4'b0010,
		paraAnd = 4'b0011, paraOr = 4'b0100,
		paraXor = 4'b0101, paraXnor = 4'b0110,
		paraNot = 4'b0111,
		paraAddi = 4'b1000, paraSubi = 4'b1001,
		paraMov = 4'b1010, paraMovi = 4'b1011,
		paraLoad = 4'b1100, paraStore = 4'b1101;
	//outputs
	reg clk = 0, rst = 1, mfc = 0;
	reg [15:0] memoryIn; //to the mdr
	//inputs
	wire enable, rw;
	wire [15:0] memoryOut, address, busOut; //from the mdr
	//internal memory usage
	reg [15:0] mem [65535:0]; //hex for FFFF
	integer x;

	connectAll connectAll(rst, clk, mfc, rw, enable, address, memoryIn, memoryOut, busOut);
	initial begin
		//003141411 commands
		//r0 0, r1 1, r2 10, r3 11
		//oppCode 4 bits, 6 bits for the next two paras
		//MOVI #1B, R0 movi 1011
		//MOVI R1, #2B 2016
		x = 0; mem [x] = 16'b1011101011000000;
		//MOV R1, R0 mov 1010
		//MOV R2, R1 2016 
		x = 1; mem [x] = 16'b1010000010000001;
		//ADDI R1, #A
		//ADDI R2, #1A 2016
		x = 2; mem [x] = 16'b1000000010011010;
		//SUBI R0, #2
		//SUBI R1, #4 2016
		x = 3; mem [x] = 16'b1001000001000100;
		//XOR R0, R1
		//XOR R1, R2 2016
		x = 4; mem [x] = 16'b0101000001000010;
		//INV R0
		//INV R1 2016
		x = 5; mem [x] = 16'b0111000001111111; //second register does not mater
		//STORE R1, (R0)
		//STORE R2, (R1) 2016
		x = 6; mem [x] = 16'b1101000010000001;
		//LOAD (R0), R2
		//LOAD (R1), R3 2016
		x = 7; mem [x] = 16'b1100000001000011;
		
		for (x = 8; x < 65535; x = x + 1) begin
			mem[x] = 16'b0;
		end
		memoryIn = 16'bZ;
		@(negedge clk) rst = 0;
	end

	always begin
		#10 clk = ~clk;
	end

	always @(address or memoryOut) begin
		if (enable & rw) begin
			memoryIn = mem[address];
			#100 mfc = 1;
			#200 mfc = 0;
		end else if (enable & ~rw)begin
			mem[address] = memoryOut;
			#100 mfc = 1;
			#200 mfc = 0;
		end else begin
			memoryIn = 16'bZ;
		end
	end
endmodule
