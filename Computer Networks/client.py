import socket
import sys
import string

HOST = sys.argv.pop() if len(sys.argv) == 2 else "127.0.0.1" 
PORT = 5555 #port 5555 is preset.

WAITING_FOR_WELCOME = 0
WAITING_FOR_BOARD = 1
WAITING_FOR_GAME_STATUS = 2
WAITING_FOR_USER_INPUT = 3
WAITING_TO_PLAY_AGAIN = 4
GAME_OVER = 5

def make_socket():
    try:
        c = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    except socket.error, msg:
        print "Client: Failed to create socket. Error code: " + str(msg[0]) \
                + ", Error message: " + msg[1]
        sys.exit()
    return c

EOM = "::"
class Messenger:
    def __init__(self, sock):
        self.data = ""
        self.sock = sock

    def read(self):
        if not EOM in self.data:
            self.data += self.sock.recv(1024)

        msg, self.data = self.data.split(EOM, 1)
        return msg

    def send(self, msg):
        self.sock.sendall(msg + EOM)
		
if __name__ == "__main__":
    sock = make_socket()
    sock.connect((HOST,PORT))
    messenger = Messenger(sock)
    current_state = WAITING_FOR_WELCOME
	
	#none of this is the intelegence for the game. and more just waiting on the server.
    while current_state is not GAME_OVER:
        if current_state is WAITING_FOR_WELCOME:
            print messenger.read()
            current_state = WAITING_FOR_BOARD
        elif current_state is WAITING_FOR_BOARD:
            print messenger.read() 
            current_state = WAITING_FOR_GAME_STATUS
        elif current_state is WAITING_FOR_GAME_STATUS:
            msg = messenger.read()
            if msg == "True":
                current_state = WAITING_TO_PLAY_AGAIN
            elif msg == "False":
                current_state = WAITING_FOR_USER_INPUT
        elif current_state is WAITING_FOR_USER_INPUT:
            guess = raw_input("Guess a letter: ").lower()
            if len(guess) > 1 or guess not in string.lowercase:
                print "Please guess a letter."
            else:
                messenger.send(guess)
                current_state = WAITING_FOR_BOARD
        elif current_state is WAITING_TO_PLAY_AGAIN:
            play_again = raw_input("Play again? (y/n): ").lower()
            if "y" in play_again:
                current_state = WAITING_FOR_WELCOME
                messenger.send("play")
            else:
                current_state = GAME_OVER
                messenger.send("quit")
                print "Thanks for playing!"

    sock.close()