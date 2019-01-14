//Actual bus, but also the conections for everything is here.
module connectAll(rst, clk, mfc, rw, enable, address, memoryIn, memoryOut, busOut);
	//inputs required
	input wire rst, clk, mfc;
	input wire [15:0] memoryIn;
	//ouputs required
	output wire rw, enable;
	//just to see the bus, but not required
	output wire [15:0] address, busOut, memoryOut;
	
	//variables and assigning them
	//straight connection first
	wire [15:0] bus; //the actual bus
	wire [3:0] opCode; //constantly out of Decoder and into ALU
	wire [5:0] para1, para2; //constantly out of the Decoder and into Alu FSMs
	wire [1:0] enablesPC; //fetch to the program counter
	wire [6:0] nextFSM; //fetch to the other FSMs
	//enable and rw for the memory/TB
	wire fetchRw, storeRw, loadRw, fetchEn, storeEn, loadEn;
	assign enable = (fetchEn | storeEn | loadEn);
	assign rw = (fetchRw | storeRw | loadRw);
	//write/read enables for marMdr from fetch | load | store
	wire [2:0] wMME, rMME, fetchWemm, fetchRemm, loadWemm, loadRemm, storeWemm, storeRemm;
	assign wMME = (fetchWemm | loadWemm | storeWemm);
	assign rMME = (fetchRemm | loadRemm | storeRemm);
	//FSMs or together to the register file
	wire [3:0] writeEnablesReg, readEnablesReg, wERalu2i, rERalu2i, wERalu1r, rERalu1r, wERaluNot,
			rERaluNot, wERmov, rERmov, wERmovi, loadWreg, loadRreg, storeWreg, storeRreg;
	assign writeEnablesReg = (wERalu2i | wERalu1r | wERaluNot | wERmov | wERmovi | loadWreg | storeWreg);
	assign readEnablesReg = (rERalu2i | rERalu1r | rERaluNot | rERmov | storeRreg | loadRreg);
	//restart the fetch FSM
	wire restart, resAlu2reg, resAlu1reg, resAluNot, resMov, resMovi, resLoad, resStore;
	assign restart = (resAlu2reg | resAlu1reg | resAluNot | resMov | resMovi | resLoad | resStore);
	//enables of the ALU
	wire [2:0] aluInOut, aluInOut2reg, aluInOut1reg, aluInOutNot;
	assign aluInOut = (aluInOut2reg | aluInOut1reg | aluInOutNot);
	//enables for instrDec
	wire [2:0] enableDec, fetchED, alu1RegID, moviRegID;
	assign enableDec = (fetchED | alu1RegID | moviRegID);
	
	//FSMs
	alu2regFsm	alu2regFsm(rst, clk, nextFSM, resAlu2reg, para1, para2, aluInOut2reg, wERalu2i, rERalu2i);
	
	alu1regFsm	alu1regFsm(rst, clk, resAlu1reg, nextFSM, aluInOut1reg, wERalu1r, rERalu1r, para1, alu1RegID);
	
	aluNotFsm	aluNotFsm(rst, clk, resAluNot, nextFSM, aluInOutNot, wERaluNot, rERaluNot, para1);
	
	movFsm		movFsm(rst, clk, resMov, nextFSM, wERmov, rERmov, para1, para2);
	
	moviFsm		moviFsm(rst, clk, resMovi, nextFSM, wERmovi, para2, moviRegID);
	
	loadFsm		loadFsm(rst, clk, mfc, loadEn, loadRw, nextFSM, resLoad, para1, para2, loadWemm, loadRemm, loadWreg, loadRreg);
	
	storeFsm	storeFsm(rst, clk, mfc, storeEn, storeRw, nextFSM, resStore, para1, para2, storeWemm, storeRemm, storeWreg, storeRreg);
	
	fetchFsm	fetchFsm(rst, clk, mfc, fetchRw, fetchEn, restart, opCode, enablesPC, fetchWemm, fetchRemm, fetchED, nextFSM);
	//individual parts
	pcwIncrementor	pcwIncrementor(rst, clk, bus, enablesPC);
	
	marMdr		marMdr(rst, address, bus, bus, memoryIn, memoryOut, wMME, rMME);
	
	instrDec	instrDec(rst, enableDec, bus, opCode, bus, para1, para2);
	
	alu			alu(rst, opCode, bus, aluInOut, bus); //aluInOut
	
	registers	registers(rst, bus, bus, writeEnablesReg, readEnablesReg);
	
	assign busOut = bus; //just to easily see the bus, but not required
endmodule
