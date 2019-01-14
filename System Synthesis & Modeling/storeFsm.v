//Fetch FSM
module storeFsm(rst, clk, mfc, en, rw, nextFSM, resStore, para1, para2, storeWemm, storeRemm, storeWreg, storeRreg);
	//opCodes
	parameter
		paraAdd = 4'b0001, paraSub = 4'b0010,
		paraAnd = 4'b0011, paraOr = 4'b0100,
		paraXor = 4'b0101, paraXnor = 4'b0110,
		paraNot = 4'b0111,
		paraAddi = 4'b1000, paraSubi = 4'b1001,
		paraMov = 4'b1010, paraMovi = 4'b1011,
		paraLoad = 4'b1100, paraStore = 4'b1101;
	//simple true/false
	parameter
		true = 1'b1, false = 1'b0;
	//internal state
	parameter
		s0 = 4'b0000, s1 = 4'b001, s2 = 4'b0010, s3 = 4'b0011, s4 = 4'b0100, s5 = 4'b0101,
		s6  = 4'b0110, s7  = 4'b0111, s8  = 4'b1000, s9  = 4'b1001, s10 = 4'b1010, s11 = 4'b1011,
		s12 = 4'b1100, s13 = 4'b1101, s14 = 4'b1110, s15 = 4'b1111;
	//which FSM besides the fetch
	parameter stateBlank = 7'b0000000,
		stateAluPar2 = 7'b0000001, stateAluPar1 = 7'b0000010, stateAluNot = 7'b0000100,
		stateMove = 7'b0001000, stateMovi = 7'b0010000,
		stateLoad = 7'b0100000, stateStore = 7'b1000000,
		stateError = 7'b1111111;
	//for 4 bit testing
	parameter fourBlank = 4'b0000,
		fourOne = 4'b0001, fourTwo = 4'b0010, fourFour = 4'b0100, fourEigh = 4'b1000,
		fourError = 4'b1111;
	
	//start ins and outs
	input wire rst, clk, mfc;
	input wire [6:0] nextFSM;
	input wire [5:0] para1, para2;
	output reg en, rw, resStore;
	output reg [2:0] storeWemm, storeRemm;
	output reg [3:0] storeWreg, storeRreg;
	
	//vars
	reg [3:0] state, next;
	
	always @(posedge clk or posedge rst) begin
		if (rst) begin
			rw <= false; en <= false;
			storeWemm <= 3'b000;
			storeRemm <= 3'b000;
			resStore <= false;
			storeRreg <= fourBlank;
			storeWreg <= fourBlank;
		end else begin
			case (state)
				s0 : begin
						rw <= false; en <= false;
						storeWemm <= 3'b000;
						storeRemm <= 3'b000;
						resStore <= false;
						storeRreg <= fourBlank;
					end
				s1 : begin //put the register to bus
						case(para1)
							6'b000000 : storeRreg <= fourOne;
							6'b000001 : storeRreg <= fourTwo;
							6'b000010 : storeRreg <= fourFour;
							6'b000011 : storeRreg <= fourEigh;
							default : storeRreg <= fourError;
						endcase
					end
				s2 : storeWemm <= 3'b010; //bus to the mdr
				s3 : storeWemm <= 3'b000;
				s4 : begin //put the register to bus
						case(para2)
							6'b000000 : storeRreg <= fourOne;
							6'b000001 : storeRreg <= fourTwo;
							6'b000010 : storeRreg <= fourFour;
							6'b000011 : storeRreg <= fourEigh;
							default : storeRreg <= fourError;
						endcase
					end
				s5 : storeWemm <= 3'b100; //bus to mar
				s6 : storeWemm <= 3'b000; //stop reading the bus
				s7 : storeRreg <= fourBlank; //nothing to the bus
				s8 : storeRemm <= 3'b100; //mar to the adress
				s9 : begin
						storeRemm <= 3'b001; //mdr to the memory
						rw <= false; en <= true; //making sure both are correct.
					end
				s10 : storeRemm <= 3'b001; //waiting for the mfc to fall
				s11 : begin
						en <= false;
						storeRemm <= fourBlank;
					end
				s12 : resStore <= true;
				default : begin
						rw <= false; en <= false;
						storeWemm <= 3'b000;
						storeRemm <= 3'b000;
						resStore <= false;
						storeRreg <= fourBlank;
					end
			endcase
		end
	end
	
	always @(state or nextFSM or mfc) begin
		//mfc wait inside s2
		case (state)
			s0 : next = s1;
			s1 : next = s2;
			s2 : next = s3;
			s3 : next = s4;
			s4 : next = s5;
			s5 : next = s6;
			s6 : next = s7;
			s7 : next = s8;
			s8 : next = s9;
			s9 : next = s10;
			s10 : if (mfc)
					next = s11;
				else
					next = s10;
			s11 : if (mfc)
					next = s11;
				else
					next = s12;
			s12 : if (mfc)
					next = s12;
				else
					next = s13;
			s13 : next = s14;
			s14 : next = s15;
			s15 : next = s15; //wait until restart or the right FSM
			default : next = s15; //error
		endcase
	end
	
	always @(posedge clk or posedge rst) begin
		if (rst) begin
			state <= s15;
		end else if (nextFSM == stateStore) begin
			state <= s0;
		end else begin
			state <= next;
		end
	end
endmodule