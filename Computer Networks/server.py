import string
from random import choice
import socket
import sys
from thread import *

WAITING_FOR_CONN = 1
WAITING_FOR_MOVE = 2
WAITING_TO_PLAY_AGAIN = 3
WAITING_TO_START_GAME = 4
GAME_OVER = 5

def make_socket():
    HOST = sys.argv.pop() if len(sys.argv) == 2 else "127.0.0.1" 
    PORT = 5555 #port 5555 is preset.
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    try:
        s.bind((HOST, PORT))
    except socket.error, msg:
        print 'Bind failed. Error Code: ' + str(msg[0]) \
                + ', Error Message: ' + msg[1]
        sys.exit()
    s.listen(1)
    return s

def init_game(messenger):
    game = Game()
    messenger.send("WELCOME TO HANGMAN")
    messenger.send(str(game))
    messenger.send(str(game.gameover))
    return game

def client_thread(conn):
    current_state = WAITING_TO_START_GAME
    messenger = Messenger(conn)
    while current_state is not GAME_OVER:
        if current_state is WAITING_TO_START_GAME:
            game = init_game(messenger)
            current_state = WAITING_FOR_MOVE
        elif current_state is WAITING_FOR_MOVE:
            letter = messenger.read()
            if game.already_guessed(letter):
                reply = "You've already guessed %r." % letter
            else:
                game.guess_letter(letter)
                reply = str(game)

            if game.gameover:
                current_state = WAITING_TO_PLAY_AGAIN
            messenger.send(reply)
            messenger.send(str(game.gameover))
        elif current_state is WAITING_TO_PLAY_AGAIN:
            play_again = messenger.read()
            if play_again == "play":
                current_state = WAITING_TO_START_GAME
            elif play_again == "quit":
                current_state = GAME_OVER

    conn.close()

#You could actually make this into a game.py and import game from GAME
#Then you could run the game with out a server.py, but still run it through the server.
#I was not sure how to do really any coding in Python so this is where I started.
class Game():
    MAX_GUESSES = 7
    HANGMAN_STRINGS = [
"""
            -----
                |
                |
                |
                |
                |
            ---------
""",
"""
            -----
            |   |
                |
                |
                |
                |
            ---------
""",
"""
            -----
            |   |
            O   |
                |
                |
                |
            ---------
""",

"""
            -----
            |   |
            O   |
            |   |
                |
                |
            ---------
""",
"""
            -----
            |   |
            O   |
            |\  |
                |
                |
            ---------
""",
"""
            -----
            |   |
            O   |
           /|\  |
                |
                |
            ---------
""",
"""
            -----
            |   |
            O   |
           /|\  |
             \  |
                |
            ---------
""",
"""
            -----
            |   |
            O   |
           /|\  |
           / \  |
                |
            ---------
"""]

    def __init__(self):
        self.word = self.random_word()
        self.incorrect = []
        self.correct = []
        self.progress = self.get_progress()
        self.gameover = False

    def random_word(self):
        dictionary = open('dictionary.txt', 'r').readlines()
        words = [word.strip() for word in dictionary]
        return choice(words)

    def get_progress(self):
        return "".join([let if let in self.correct else "*" for let in self.word])

    def already_guessed(self, guess):
        return guess in self.correct + self.incorrect

    def guess_letter(self, guess):
        if guess in self.word:
            self.correct.append(guess)
        else:
            self.incorrect.append(guess)

    def status(self):
        if len(self.incorrect) >= Game.MAX_GUESSES:
            self.gameover = True
            return "\nYou lose. The word was %r." % self.word
        if set(self.correct) == set(self.word):
            self.gameover = True
            return "\nYou won!"
        return "" 

    def __str__(self):
        result = "\n" + "=" * 30 + "\n"
        result += Game.HANGMAN_STRINGS[len(self.incorrect)]
        result += "\nIncorrect guesses: %s" % ", ".join(self.incorrect)
        result += "\nProgress: %s" % self.get_progress()
        result += self.status()
        return result

#------------------------------------------------------------------------------
def valid_guess(game):
    while True:
        guess = raw_input("Guess a letter: ").lower()
        if len(guess) > 1 or guess not in string.lowercase:
            print "Please guess a letter."
        elif game.already_guessed(guess):
            print "You've already guessed %r." % guess
        else:
           return guess
		   
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
    print "Server: Up and listening..."
    while True:
        clientsocket, addr = sock.accept()
        print "Connected with " + addr[0] + ":" + str(addr[1])
        start_new_thread(client_thread, (clientsocket,))