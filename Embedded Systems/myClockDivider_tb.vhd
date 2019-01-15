
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

--There is no entity for the testbench
entity myClockDivider_tb is
end myClockDivider_tb;


architecture Behavioral of myClockDivider_tb is
    --These two constants are needed because we used generics
    --in our clock divider. We will use them later in the instantiation. 
    constant inFreq : integer := 100000000;
    constant outFreq : integer := 1000;
    
    --This constant is used to make our clock process.
    --I picked 10ns because that is what we need for 
    --a 100MHz clock. 
    constant clk_period : time := 10 ns;
    
    --clk, and ouputClock are the input and outuput to the 
    --clock divider. Notice I set clk to '0' using := '0';
    signal clk : std_logic := '0';
    signal outputClock : std_logic;


--Now I have to do a compoent definition. You can 
--copy and paste your entity and just change the top line and bottom line. 
component myClockDivider is
    generic(inClkFreq : integer; outClkFreq : integer);
    port(
        fastClkIn : in STD_LOGIC;
        slowClkOut : out STD_LOGIC
        );
end component;


begin

--uut is unit under test. Notice the thing on the left is the name internal to 
--the module and the thing on the right is the external value/signal that is 
--in our testbench. Notice how the constants are assigned in the generic. 
uut: myClockDivider
generic map(inClkFreq => inFreq, outClkFreq => outFreq)
 port map (
    fastClkIn => clk,
    slowClkOut => outputClock
);

--This process drive our clock signal. You will need this in any timed
--circuitry that you do. Notice we don't wait for clock_period but clock_period/2 
clk_process : process
begin
    clk <= '0';
    wait for clk_period/2;
    clk <= '1';
    wait for clk_period/2;
end process;

--Stimulus 
--For this example we really don't need anything here but in other cases you would be driving signals
--and waiting for some lenght of time and then driving signals again. 
stim_proc: process
begin
    wait for 1000 ns;
end process;


end Behavioral;