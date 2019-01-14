//https://github.com/jbush001/RISC-Processor/tree/master/verilog/rtl
//http://andrew.gibiansky.com/blog/electrical-engineering/your-very-first-microprocessor/
//http://www-scf.usc.edu/~ee577/tutorial/verilog/
//https://github.com/vlsi1217/ASIC/tree/master/verilog%20100%20examples/RISC8

//Fetch FSM
module fetchFsm(rst, clk, mfc, rw, en, restart, opcode, enablesPC, writeEnablesMarMdr, readEnablesMarMdr, enableDec, nextFSM);
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
	
	//start ins and outs
	input wire rst, clk, mfc, restart;
	input wire [3:0] opcode;
	output reg en, rw;
	output reg [1:0] enablesPC;
	output reg [2:0] writeEnablesMarMdr, readEnablesMarMdr, enableDec;
	output reg [6:0] nextFSM;
	//vars
	reg [3:0] state, next;
	
	always @(posedge clk or posedge rst) begin
		if (rst) begin
			rw <= false; en <= false;
			enablesPC <= 2'b00; //program counter
			writeEnablesMarMdr <= 3'b000; readEnablesMarMdr <= 3'b000; //mar/mdr writes and reads
			enableDec <= 3'b000; //inout of instruction decoder
			nextFSM <= stateBlank;
		end else begin
			case (state)
				s0 : begin
						//just making sure everything is in a the first state
						nextFSM <= stateBlank;
						rw <= false; en <= false;
						readEnablesMarMdr <= 3'b000; //mar/mdr reads
						enableDec <= 3'b000; //inout of instruction decoder
						enablesPC <= 2'b00; //output the counter to the bus
						writeEnablesMarMdr <= 3'b000; //mar recieve from the bus
					end
				s1 : enablesPC <= 2'b01; //output the counter to the bus
				s2 : writeEnablesMarMdr <= 3'b100; //mar recieve from the bus
				s3 : writeEnablesMarMdr <= 3'b000; //mar stop recieving from the bus
				s4 : begin
						enablesPC <= 2'b00; //stop asserting counter to the bus & waiting for m
						readEnablesMarMdr <= 3'b100; //mar to the memory
						rw <= true; //try to get memory
						en <= true;
					end
				s5 : begin
						enablesPC <= 2'b10; //increment Counter and/or stop asserting counter to the bus
						writeEnablesMarMdr <= 3'b001; //read data from mem to mdr
						readEnablesMarMdr <= 3'b000; //disasserting mar to mem
					end
				s6 : begin
						en <= false; //stop getting data from the memory
						writeEnablesMarMdr <= 3'b000; //stop writing to the mdr
						readEnablesMarMdr <= 3'b010; //mdr to the bus
						enableDec <= 3'b100; //decoder read the bus
					end
				s7 : begin
						enableDec <= 3'b000; //decoder stop reading the bus
						rw <= false; //do not have to do, but whatever
					end
				s8 : begin
						readEnablesMarMdr <= 3'b000;
						//default should not happen
						case (opcode)
							paraAdd : nextFSM <= stateAluPar2;
							paraSub : nextFSM <= stateAluPar2;
							paraAnd : nextFSM <= stateAluPar2;
							paraOr : nextFSM <= stateAluPar2;
							paraXor : nextFSM <= stateAluPar2;
							paraXnor : nextFSM <= stateAluPar2;
							paraNot : nextFSM <= stateAluNot;
							paraAddi : nextFSM <= stateAluPar1;
							paraSubi : nextFSM <= stateAluPar1;
							paraMov : nextFSM <= stateMove;
							paraMovi : nextFSM <= stateMovi;
							paraLoad : nextFSM <= stateLoad;
							paraStore : nextFSM <= stateStore;
							default : nextFSM <= stateError;
						endcase
					end
				s9 : nextFSM <= stateBlank;
				default : begin
						nextFSM <= stateBlank;
						rw <= false; en <= false;
						enablesPC <= 2'b00; //program counter
						writeEnablesMarMdr <= 3'b000; readEnablesMarMdr <= 3'b000; //mar/mdr writes and reads
						enableDec <= 3'b000; //inout of instruction decoder
					end
			endcase
		end
	end
	
	always @(state or mfc) begin
		//mfc wait inside s2
		case (state)
			s0 : next = s1;
			s1 : next = s2;
			s2 : next = s3;
			s3 : next = s4;
			s4 : next = s5;
			s5 : if (mfc)
					next = s6;
				else
					next = s5;
			s6 : if (~mfc)
					next = s7;
				else
					next = s6;
			s7 : next = s8;
			s8 : next = s9;
			s9 : next = s10;
			s10 : next = s11;
			s11 : next = s12;
			s12 : next = s13;
			s13 : next = s14;
			s14 : next = s15;
			s15 : next = s15; //wait until restart
			default : next = s15; //error
		endcase
	end	
	
	always @(posedge clk or posedge rst or posedge restart) begin
		//changed based on clock cycles
		if (rst) begin
			state <= s0;
		end else if (restart) begin
			state <= s0;
		end else begin
			state <= next;
		end
	end
endmodule