//MAR and MDR for RSC chip
module marMdr(rst, address, busDataIn, busDataOut, memoryIn, memoryOut, wMME, rMME);
	//start ins and outs
	input wire rst; //reset
	//can write to one and read from another at any time
	input wire [2:0] wMME; //write into the registers in this
	input wire [2:0] rMME; //read from the registers to whatever
	input wire [15:0] busDataIn, memoryIn; //Data from the bus
	output reg [15:0] address, busDataOut, memoryOut; //data to the bus from an addi or subi instruction
	
	reg [15:0] mar, mdr;
	
	//writes
	always @(*) begin
		if(rst) begin
			mar = 16'bZ; mdr = 16'bZ;
		end else if (wMME == 3'b100 & rMME !== 3'b100) begin
			mar = busDataIn;
		end else if (wMME == 3'b010 & rMME !== 3'b001 & rMME !== 3'b010) begin
			mdr = busDataIn;
		end else if (wMME == 3'b001 & rMME !== 3'b001 & rMME !== 3'b010) begin
			mdr = memoryIn;
		end
	end
	
	//reads
	always @(*) begin
		if(rst) begin
			address = 16'bZ;
			memoryOut = 16'bZ; busDataOut = 16'bZ;
		end else if (rMME == 3'b100 & wMME !== 3'b100) begin
			address = mar;
		end else if (rMME == 3'b010 & wMME !== 3'b001 & wMME !== 3'b010) begin
			busDataOut = mdr;
		end else if (rMME == 3'b001 & wMME !== 3'b001 & wMME !== 3'b010) begin
			memoryOut = mdr;
		end else begin
			memoryOut = 16'bZ; busDataOut = 16'bZ;
		end
	end
	
endmodule