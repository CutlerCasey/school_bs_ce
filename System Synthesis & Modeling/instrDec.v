//Instruction Decoder and Control Signal Generator for RSC chip
module instrDec(rst, enableRegs, busDataIn, opCode, busDataOut, para1Out, para2Out);
	//opCodes
	parameter
		paraAdd = 4'b0001, paraSub = 4'b0010,
		paraAnd = 4'b0011, paraOr = 4'b0100,
		paraXor = 4'b0101, paraXnor = 4'b0110,
		paraNot = 4'b0111,
		paraAddi = 4'b1000, paraSubi = 4'b1001,
		paraMov = 4'b1010, paraMovi = 4'b1011,
		paraLoad = 4'b1100, paraStore = 4'b1101;
	parameter
		true = 1'b1, false = 1'b0;
		
	//start ins and outs
	input wire rst; input wire [2:0] enableRegs;
	input wire [15:0] busDataIn; //Data from the bus
	//output reg [15:0] busDataOut; //data to the bus from an addi or subi instruction
	
	reg [15:0] bus, regist; //regist is the internal register to hold data
	output wire [5:0] para1Out, para2Out;
	output wire [15:0] busDataOut;
	output wire [3:0] opCode;
	
	always @(*) begin
		if (rst) begin
			regist = 16'b0;
		end else if (enableRegs == 3'b100) begin
			regist = busDataIn;
		end
	end
	
	always @(*) begin
		if (rst) begin
			bus = 16'bZ;
		end else if (enableRegs == 3'b010) begin
			bus = 16'b0 + regist[11:6];
		end else if (enableRegs == 3'b001) begin
			bus = 16'b0 + regist[5:0];
		end else begin
			bus = 16'bZ;
		end
	end
	
	assign opCode = regist[15:12];
	assign para1Out = regist[11:6];
	assign para2Out = regist[5:0];
	assign busDataOut = bus;
endmodule