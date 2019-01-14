//PC w/incrementor for RSC chip
module pcwIncrementor(rst, clk, busDataOut, enables);
	//start ins and outs
	input wire rst, clk; //reset
	input wire [1:0] enables; //to make sure the right thing is on the bus and the counter is right
	output reg [15:0] busDataOut; //data to the bus from an addi or subi instruction
	reg countNo;
	reg [3:0] counter;
	wire [3:0] count; assign count = counter;
	wire noCount;
	assign noCount = countNo;
	
	//counter
	always @(posedge clk) begin
		if(rst) begin
			counter <= 16'b0;
		end else if (enables == 2'b10 & count < 4'b1001 & ~noCount) begin
			counter <= counter + 1'b1;
			countNo = 1'b1;
		end
	end
	
	//output of the counter
	always @(*) begin
		if(rst) begin
			countNo = 1'b0;
			busDataOut <= 16'bZ;
		end else if (enables == 2'b01) begin
			countNo = 1'b0;
			busDataOut <= counter;
		end else begin
			busDataOut <= 16'bZ;
		end
	end
	
endmodule