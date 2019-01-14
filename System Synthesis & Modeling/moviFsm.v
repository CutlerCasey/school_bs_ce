//Move FSM
module moviFsm(rst, clk, resMovi, nextFSM, wERmovi, para2, moviRegID);
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
	//inner state
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
	input wire rst, clk;
	input wire [6:0] nextFSM;
	input wire [5:0] para2;
	output reg [2:0] moviRegID;
	output reg resMovi;
	output reg [3:0] wERmovi; //make sure only one is writen at a time
	//vars
	reg [3:0] state, next;
	
	always @(posedge clk or posedge rst) begin
		if (rst) begin
			resMovi <= false;
			moviRegID <= 3'b000;
			wERmovi <= fourBlank;
		end else begin
			case (state)
				s0 : begin
						resMovi <= false;
						moviRegID <= 3'b000;
						wERmovi <= fourBlank;
					end
				s1 : moviRegID <= 3'b010;
				s2 : begin
						case (para2)
							6'b000000 : wERmovi <= fourOne;
							6'b000001 : wERmovi <= fourTwo;
							6'b000010 : wERmovi <= fourFour;
							6'b000011 : wERmovi <= fourEigh;
							default : wERmovi <= fourError;
						endcase
					end
				s3 : wERmovi <= fourBlank;
				s4 : begin
						moviRegID <= 3'b000;
						resMovi <= true;
					end
				default : begin
						resMovi <= false;
						moviRegID <= 3'b000;
						wERmovi <= fourBlank;
					end
			endcase
		end
	end
	
	always @(state) begin
		//mfc wait inside s2
		case (state)
			s0 : next = s1;
			s1 : next = s2;
			s2 : next = s3;
			s3 : next = s4;
			s4 : next = s5;
			s5 : next = s5;
			s6 : next = s7;
			s7 : next = s8;
			s8 : next = s9;
			s9 : next = s10;
			s10 : next = s11;
			s11 : next = s12;
			s12 : next = s13;
			s13 : next = s14;
			s14 : next = s15;
			s15 : next = s15; //wait until restart or the right FSM
			default : next = s0; //error
		endcase
	end
	
	always @(posedge clk or posedge rst) begin
		if (rst) begin
			state <= s15;
		end else if (nextFSM == stateMovi) begin
			state <= s0;
		end else begin
			state <= next;
		end
	end
endmodule