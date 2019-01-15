----------------------------------------------------------------------------------

-- Create Date: 11/01/2015 12:58:23 AM
-- Design Name: 
-- Module Name: hardwareController - Behavioral
--I found a starting duty cycle of 148 worked best when starting the esc. 
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
--use IEEE.NUMERIC_STD.ALL;

--assume that a Hz clock is alway the input
--duty cycle is 110 to 210 for steering and 100 to 190 for speed.  
entity hardwareController is
    Port ( clk : in STD_LOGIC;
		ifsLeft : in std_logic_vector(31 downto 0);
        ifsCenter : in std_logic_vector(31 downto 0);
		ifsRight : in std_logic_vector(31 downto 0);
		speed : out STD_LOGIC_vector (15 downto 0);
		steer : out std_logic_vector(15 downto 0));
end hardwareController;

architecture Behavioral of hardwareController is

signal counter : integer range 0 to 1000 := 0;
signal internalDutyCycle : integer := 146;
constant  minDutyCycle : integer := 100;
constant maxDutyCycle : integer := 190;

begin
    process(clk) begin
        if rising_edge(clk) then
			if ifsCenter < 35 then
				--reverse
				speed
			elsif ifsCenter = 35 then
				--stand still
				if ifsLeft < 148 then
					--straight
					speed <= 
				else
					--Left
				
				end if;
			elsif ifsCenter > 35 & ifsCenter < 80 then
				--forward
				
			else
				--to close to far
				
			end if;
			
            
			
			if ifsRight < 148 then
				--nothing
				
			elsif ifsRight = 148 then
				--straight
				
			else
				--right
				
			end if;
        end if;
    end process;

end Behavioral;
