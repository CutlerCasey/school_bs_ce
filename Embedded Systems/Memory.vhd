library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity mem is
    Port (  clk: in STD_LOGIC;
            rst: in boolean;
            code: in boolean;
			follow: in boolean;
			run : in boolean;
            --speedSwithesIn : in STD_LOGIC_VECTOR (3 downto 0);
            --strSwitchesIn : in STD_LOGIC_VECTOR (3 downto 0);
            speedSwithesOut : out STD_LOGIC_VECTOR (3 downto 0);
            steerSwithesOut : out STD_LOGIC_VECTOR (3 downto 0));
end mem;

architecture Behavioral of mem is
component rnd is
    generic ( width : integer :=  32 );
	port (
		clk : in std_logic;
		random_num : out std_logic_vector (width-1 downto 0)   --output vector
	);
end component;

constant modulus : integer := 10000;
constant minMem : integer := 9; --size of the array now
constant maxMem : integer := 70; --size of the array 0 to n

signal slowdownCount : integer := 0;
signal i : integer;
signal count : integer range 0 to maxMem := 0;

--signal NIBBLE : std_logic_vector(3 downto 0);
type NIBBLE is array (0 to maxMem) of std_logic_vector(3 downto 0);
--type MEMo is array (0 to maxMem) of NIBBLE;
constant speedArr : NIBBLE := ("1000", "1001", "1010", "1100", "1110", "1001", "1010",
                              "1000", "0000", "0001", "0010", "0100", "0101", "0000",
                              "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                                "1000", "0000", "0001", "0010", "0100", "0101", "0000",
                                "1000", "0000", "0001", "0010", "0100", "0101", "0000",
                              "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                              "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                                "1000", "0000", "0001", "0010", "0100", "0101", "0000",
                                "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                               "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                               "1000");

--signal NIBBLEs : std_logic_vector(3 downto 0);
type NIBBLEs is array (0 to maxMem) of std_logic_vector(3 downto 0);
--type MEMom is array (0 to maxMem) of NIBBLEs;
constant steerArr : NIBBLEs := ("1000", "1001", "1010", "1100", "1110", "1001", "1010",
                              "1000", "0000", "0001", "0010", "0100", "0101", "0000",
                              "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                                "1000", "0000", "0001", "0010", "0100", "0101", "0000",
                                "1000", "0000", "0001", "0010", "0100", "0101", "0000",
                              "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                              "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                                "1000", "0000", "0001", "0010", "0100", "0101", "0000",
                                "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                               "1000", "1001", "1010", "1100", "1110", "1001", "1010",
                               "1000");
 

signal randomNum : std_logic_vector(3 downto 0);
-- signal speedRnd : std_logic_vector (3 downto 0);
-- signal steerRnd : std_logic_vector (3 downto 0);

begin
randomNumGen: rnd
	generic map(width => 4)
	port map (clk => clk, random_num => randomNum);
	
process (clk) begin
		if rising_edge(clk) then
			if rst then
				speedSwithesOut <= "1000";
				steerSwithesOut <= "1000";
--				for i in 0 to maxMem loop
--					speedArr(i) <= "1000";
--					steerArr(i) <= "1000";
--				end loop;
				count <= 0;
				i <= 0;
			else
				if (count < (modulus * 4)) then
					count <= count + 1;
					if (count mod modulus = 0) then
						if (run) then
							--running what is in the array
							if (i < (maxMem - 1)) then
								speedSwithesOut <= speedArr(i);
								steerSwithesOut <= steerArr(i);
							else
								speedSwithesOut <= "1000";
								steerSwithesOut <= "1000";
							end if;
							i <= i + 1;
--					elsif (code = "0" and run = "0" and follow = "1") then
--							--random only needs to do                           
--							i <= i + 1;
--							if i < maxMem then
--								if i mod 15 < 8 then
--								    --std_logic_vector(to_unsigned(to_integer(unsigned( in )) + 1, 8));
--									speedArr(i) <= std_logic_vector(to_unsigned(i),4) + 8;
--									steerArr(i) <= std_logic_vector(to_unsigned(i),4) - 8;
--								else
--									speedArr(i) <= std_logic_vector(to_unsigned(i),4) + 8;
--									steerArr(i) <= std_logic_vector(to_unsigned(i),4) + 8;
--								end if;
--							elsif i = 0 then
--								speedArr(i) <= "1000";
--								steerArr(i) <= "1000";
--							else
--								speedArr(maxMem) <= "1000";
--								steerArr(maxMem) <= "1000";
--							end if;
--							if ((i < (maxMem - 1)) and (i mod 4 = 0)) then
--								speedArr(i) <= randomNum;
--								steerArr(i) <= randomNum;
--							elsif ((i < (maxMem - 1)) and (i mod 4 = 1)) then
--								--trying to always go to the pause state
--								speedArr(i) <= "1000";
--								steerArr(i) <= "1000";
--							elsif ((i < (maxMem - 1)) and (i mod 4 = 2)) then
--								--faking the pause, since not sure what random was in before
--								speedArr(i) <= "0000";
--								steerArr(i) <= "0000";
--							elsif ((i < (maxMem - 1)) and (i mod 4 = 3)) then
--								--trying to always go to the pause state
--								speedArr(i) <= "1000";
--								steerArr(i) <= "1000";
--							else
--								--idle state
--								speedArr(maxMem) <= "1000";
--								steerArr(maxMem) <= "1000";
--							end if;
--							i <= i + 1;
--							--sitting idle
--							speedSwithesOut <= "1000";
--							steerSwithesOut <= "1000";
--						elsif ((code) and not(run) and not(follow)) then
--							--meant for later and the reason speedSwithesIn & strSwitchesIn
--							if (i < (maxMem - 1)) then
--								speedArr(i) <= speedSwithesIn;
--								steerArr(i) <= strSwitchesIn;
--							else
--								speedArr(maxMem) <= "1000";
--								steerArr(maxMem) <= "1000";
--							end if;
--							speedSwithesOut <= "1000";
--							steerSwithesOut <= "1000";
--							i <= i + 1;
						else
							speedSwithesOut <= "1000";
							steerSwithesOut <= "1000";
							i <= 0;
						end if;
					end if;
				else
					count <= 0;
				end if;
			end if;
		end if;
	end process;
    
end Behavioral;