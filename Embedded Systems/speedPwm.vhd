----------------------------------------------------------------------------------

-- Create Date: 11/01/2015 12:58:23 AM
-- Design Name: 
-- Module Name: speedPwm - Behavioral
--I found a starting duty cycle of 148 worked best when starting the esc. 
----------------------------------------------------------------------------------


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

--use IEEE.NUMERIC_STD.ALL;

--assume that a Hz clock is alway the input
--duty cycle is 110 to 210 for steering and 100 to 190 for speed.  
entity speedPwm is
    Port ( clk : in STD_LOGIC;
            dutyCycle : in integer;
           pwmOut : out STD_LOGIC);
end speedPwm;

architecture Behavioral of speedPwm is

signal counter : integer range 0 to 1000 := 0;
signal internalDutyCycle : integer := 146;
constant  minDutyCycle : integer := 100;
constant maxDutyCycle : integer := 190;

begin
    process(clk)
    begin
        if rising_edge(clk) then
            if counter < 1000 then
                counter <= counter + 1;
            else
                counter <= 0;
                if dutyCycle > maxDutyCycle then
                    internalDutyCycle <= maxDutyCycle;
                elsif dutyCycle < minDutyCycle then
                    internalDutyCycle <= minDutyCycle;
                else
                    internalDutyCycle <= dutyCycle;
                end if;
            end if;
            
            if counter < internalDutyCycle then
                pwmOut <= '1';
            else
                pwmOut <= '0';
            end if;
            
            
        end if;
    end process;

end Behavioral;
