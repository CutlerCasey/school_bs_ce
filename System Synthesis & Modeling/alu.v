//Alu for RSC chip
module alu(rst, opCode, inData, aluInOut, outp);
	parameter
		paraAdd = 4'b0001, paraSub = 4'b0010,
		paraAnd = 4'b0011, paraOr = 4'b0100,
		paraXor = 4'b0101, paraXnor = 4'b0110,
		paraNot = 4'b0111,
		paraAddi = 4'b1000, paraSubi = 4'b1001;
		
	//start ins and outs
	input wire rst;
	input wire [2:0] aluInOut; //enables for in1, in2, out
	input wire [15:0] inData; //both come from the bus
	input wire [3:0] opCode; //opCodes
	output reg [15:0] outp; //data out
	
	reg [15:0] inDataHold1, inDataHold2; //outHold
	
	//Input registers
	always @ (aluInOut, rst) begin
		if(rst) begin
			inDataHold1 <= 16'b0;
			inDataHold2 <= 16'b0;
		end else if (aluInOut == 3'b010) begin
			inDataHold2 <= inData;
		end else if (aluInOut == 3'b100) begin
			inDataHold1 <= inData;
		end
	end
	
	//output/alu
	always @(aluInOut, rst) begin
		if (rst) begin
			outp = 16'bZ;
		end else if(aluInOut == 3'b001) begin
			case(opCode)
				paraAdd : outp = inDataHold1 + inDataHold2; //add
				paraSub : outp = inDataHold1 - inDataHold2; //sub 
				paraAnd : outp = inDataHold1 & inDataHold2; //and
				paraOr : outp = inDataHold1 | inDataHold2; //or
				paraXor : outp = inDataHold1 ^ inDataHold2; //xor
				paraXnor : outp = inDataHold1 ~^ inDataHold2; //xnor
				paraNot : outp = ~inDataHold1; //not
				paraAddi : outp = inDataHold1 + inDataHold2; //addi
				paraSubi : outp = inDataHold1 - inDataHold2; //subi
				default : outp = 16'bZ; //outHold;
			endcase
		end else begin
			outp = 16'bZ;
		end
	end
		
endmodule
