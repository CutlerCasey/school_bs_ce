----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 08/09/2015 07:11:41 PM
-- Design Name: 
-- Module Name: myClkDivider - Behavioral
----------------------------------------------------------------------------------


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity myClkDivider is
    generic(inputFreqency : integer := 100000000; outputFreqency : integer := 666 ); --Hz
    Port ( clk : in STD_LOGIC;
           clkOut : out STD_LOGIC
           );
end myClkDivider;

architecture Behavioral of myClkDivider is
--constant fpgaClk : integer := 100000000;
constant newCounter : integer := (inputFreqency/outputFreqency)/2;
signal counter : integer range 0 to newCounter := 0;
signal tempClk : std_logic := '0';


begin

process(clk)
begin

	if rising_edge(clk) then
        if counter = newCounter then
            tempClk <= not tempClk;
            counter <= 0;
        else
            counter <= counter + 1;
        end if;
	end if;

end process;

clkOut <= tempClk;


end Behavioral;
