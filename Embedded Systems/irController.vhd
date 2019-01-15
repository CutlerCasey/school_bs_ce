-- IR sensor controller to compute distance from object every 20 ms
-- Assumes incoming values are digital

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

library xil_defaultlib;
use xil_defaultlib.ALL;

Entity irController is
	Port ( clk : in std_logic;
		   leftVoltIn : in Integer;
		   rightVoltIn : in Integer;
		   --centerVoltIn : in Integer; --centerVoltIn will not exist
		   speedOut : out Integer;
		   steerOut : out Integer );
End SpeedController;

Architecture Behavioral of SpeedController is
	signal leftDistance : Integer;
	signal rightDistance : Integer;
	signal centerDistance : Integer;

	variable firstDistance : Integer := 0;
	variable secondDistance : Integer := 0;
	variable speedVarOut : Integer := 85; --what is avg guess 85
	variable steerVarOut : Integer := 85; --what is avg guess 85
	
	constant timeConst : Integer := 10; --the distance of a clock cycle
	
	begin
		sample : process(clk)
		begin
			--we should know how long this takes from rising_edge to falling_edge
			if rising_edge(clk) then
				-- Use closest distance from 3 sensors
				if (leftDistance < rightDistance and leftDistance < centerDistance and leftDistance > 0) and (rightDistance < unk  and centerDistance < unk) then --unk = something really high
					firstDistance := leftDistance;
				elsif (rightDistance < leftDistance and rightDistance < centerDistance and rightDistance > 0) and (leftDistance < unk and centerDistance < unk) then --unk = something really high
					firstDistance := rightDistance;
				else
					firstDistance := centerDistance;
				end if;
				
				--wait for timeConst ms; --we need to think about this timing
				speedVarOut := (secondDistance - firstDistance) / timeConst + unk2; --unk2 makes sure it is never negative, but also makes sure that the equation works
			end if;
			
			if falling_edge(clk) then 
				-- Use closest distance from 3 sensors
				if (leftDistance < rightDistance and leftDistance < centerDistance and leftDistance > 0) and (rightDistance < unk and centerDistance < unk) then --unk = something really high
					secondDistance := leftDistance;
				elsif (rightDistance < leftDistance and rightDistance < centerDistance and rightDistance > 0) and (leftDistance < unk and centerDistance < unk) then --unk = something really high
					secondDistance := rightDistance;
				else
					secondDistance := centerDistance;
				end if;
				
				steerVarOut := (firstDistance - secondDistance) / timeConst + unk2;
			end if;
		end process;
		
		-- Function to get distance from incoming voltage
		leftDistance <= leftVoltIn * something;
		rightDistance <= rightVoltIn * something;
		centerDistance <= (rightVoltIn + leftVoltIn) / 2 * something;
		
		speedOut <= speedVarOut;
		steerOut <= steerVarOut;
		
End Behavioral;
	   