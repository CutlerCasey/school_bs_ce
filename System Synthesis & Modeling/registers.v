//4 registers for RSC chip
module registers(rst, busDataIn, busDataOut, writeEnables, readEnables);
	//for 4 bit testing
	parameter fourBlank = 4'b0000,
		fourOne = 4'b0001, fourTwo = 4'b0010, fourFour = 4'b0100, fourEigh = 4'b1000,
		fourError = 4'b1111;
	//start ins and outs
	input wire rst; //reset
	//can write to one and read from another at any time
	input wire [3:0] writeEnables; //make sure only one is writen at a time
	input wire [3:0] readEnables; //make sure only one is read at a time
	input wire [15:0] busDataIn; //Data from the bus
	output reg [15:0] busDataOut; //data to the bus from an addi or subi instruction
	
	reg [15:0] reg0, reg1, reg2, reg3;
	
	//writes
	always @(*) begin
		if(rst) begin
			reg0 <= 16'b0; reg1 <= 16'b0; reg2 <= 16'b0; reg3 <= 16'b0;
		end else if (writeEnables == fourOne & readEnables !== fourOne) begin
			reg0 <= busDataIn;
		end else if (writeEnables == fourTwo & readEnables !== fourTwo) begin
			reg1 <= busDataIn;
		end else if (writeEnables == fourFour & readEnables !== fourFour) begin
			reg2 <= busDataIn;
		end else if (writeEnables == fourEigh & readEnables !== fourEigh) begin
			reg3 <= busDataIn;
		end
	end
	
	//reads
	always @(*) begin
		if(rst) begin
			busDataOut = 16'bZ;
		end else if (writeEnables !== fourOne & readEnables == fourOne) begin
			busDataOut = reg0;
		end else if (writeEnables !== fourTwo & readEnables == fourTwo) begin
			busDataOut = reg1;
		end else if (writeEnables !== fourFour & readEnables == fourFour) begin
			busDataOut = reg2;
		end else if (writeEnables !== fourEigh & readEnables == fourEigh) begin
			busDataOut = reg3;
		end else begin
			busDataOut = 16'bZ;
		end
	end
	
endmodule