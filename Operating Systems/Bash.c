//Simple shell max of one pipe and no &&, other commands.
//Cutler, Casey
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sysexits.h>
#include <unistd.h>
#include <errno.h>
//#include <syslog.h> #include <time.h>

#define OK       0
#define NO_INPUT 1
#define TOO_LONG 2
#define CSTRSIZE 100
#define CMDSIZE 30

int parse_command(char *line, char **cmd3, char **cmd4, char *infile, char *outfile);
static int getLine (char *prmpt, char *buff, size_t sz);
void priNotNine(char **cmd3, char **cmd4, char *infile, char *outfile, int j);

void exec_cmd(char **cmd3);
void exec_pipe(char **cmd3, char **cmd4);
void exec_cmd_in(char** cmd3, char* infile);
void exec_cmd_opt_in_append(char** cmd3, char* infile, char* outfile);
void exec_cmd_opt_in_write(char** cmd3, char* infile, char* outfile);
void exec_pipe_in(char** cmd3, char** cmd4, char* infile);
void exec_pipe_opt_in_append(char** cmd3,char** cmd4,char* infile,char* outfile);
void exec_pipe_opt_in_write(char** cmd3,char** cmd4,char* infile,char* outfile);

void swithRules(char **cmd3, char **cmd4, char *infile, char *outfile, int j);
void logFunc(int test); //log function implmentation
void logFuncPr(char **blah1, char *blah2, int num, int test);

int main(int argc, char *argv[]){
	//argc = 2; argv[1] = "quit"; //testing when not on turning
	char *str = argv[1];
    int i;
	
	logFunc(0);
    if (argc > 1) {
		char infil[CSTRSIZE] = "\0";
		char outfil[CSTRSIZE] = "\0";
		char *cmd1[CMDSIZE];
		char *cmd2[CMDSIZE];
		cmd1[0] = cmd2[0] = NULL;
		
		logFunc(1);
        i = parse_command(str, cmd1, cmd2, infil, outfil);
		priNotNine(cmd1, cmd2, infil, outfil, i);
		swithRules(cmd1, cmd2, infil, outfil, i);
		return 0;
	}
	else {
		i = 1;
		while(i != 0){
			int rc;
			char buff[1025] = "\0";
			char infil[CSTRSIZE] = "\0";
			char outfil[CSTRSIZE] = "\0";
			char *cmd1[CMDSIZE];
			char *cmd2[CMDSIZE];
			cmd1[0] = cmd2[0] = NULL;
			
			rc = getLine ("myshell-% ", buff, sizeof(buff));
			if (strlen(buff) == 0) {
				printf ("\nPlease enter some command.\n");
				continue;
			}
			else if (rc == TOO_LONG) {
				printf ("Input too long [%s]\n", buff);
				continue;
			}
			
			logFunc(1);
			i = parse_command(buff, cmd1, cmd2, infil, outfil);
			priNotNine(cmd1, cmd2, infil, outfil, i);
			swithRules(cmd1, cmd2, infil, outfil, i);
			if(i == 0) return 0;
		}
	} return -1;
}

static int getLine (char *prmpt, char *buff, size_t sz) { //done
    int ch, extra;

    // Get line with buffer overrun protection.
    if (prmpt != NULL) {
        printf ("%s", prmpt);
        fflush (stdout);
    }
    if (fgets (buff, sz, stdin) == NULL)
        return NO_INPUT;

    // If it was too long, there'll be no newline. In that case, we flush
    // to end of line so that excess doesn't affect the next call.
    if (buff[strlen(buff)-1] != '\n') {
        extra = 0;
        while (((ch = getchar()) != '\n') && (ch != EOF))
			extra = 1;
        return (extra == 1) ? TOO_LONG : OK;
    }

    // Otherwise remove newline and give string back to caller.
    buff[strlen(buff)-1] = '\0';
    return OK;
}

int parse_command(char *line, char **cmd3, char **cmd4, char *infile, char *outfile) { //done
	char str[strlen(line)+1];
	strcpy(str,line);
	char *res[256];
	char *p = strtok(str, " \n\t");
	int n_spaces = 0;
	int i = 0, ret = 9;
	
	logFunc(2);
	while (p) {
		res[n_spaces] = p;
		n_spaces++;
		p = strtok (NULL, " \n\t");
	}
	
	while(i < (n_spaces)) {
		if(0 == strcmp(res[i], "quit")) { 
			if(i == 0 && n_spaces == 1){
				cmd3[0] = res[i];
				cmd3[1] = NULL;
				return 0;
			}
		}
		i++;
	}
	
	int flagPipe, flagIn, flagOut1, flagOut2; //flag creation
	int space1, space2; //place in array for the 1st & 2nd cmds
	i = flagPipe = flagIn = flagOut1 = flagOut2 = space1 = space2 = 0; //making ints 0
	while (i < (n_spaces)) {
        if (0 == strcmp(res[i], "|")) //pipe
			flagPipe++;
		else if (0 == strcmp(res[i], "<")) { //input redirection
			i++;
			strcpy(infile, res[i]);
			flagIn++;
		}
		else if (0 == strcmp(res[i], ">>")) { //output1 redirection
			i++;
			strcpy(outfile, res[i]);
			flagOut1++;
		}
		else if (0 == strcmp(res[i], ">")) { //output2 redirection
			i++;
			strcpy(outfile, res[i]);
			flagOut2++;
		}
		else if ((flagPipe + flagIn + flagOut1 + flagOut2) == 0) {
			cmd3[space1] = res[i];
			space1++;
		}
		else if ((flagPipe + flagIn + flagOut1 + flagOut2) > 0){
			cmd4[space2] = res[i];
			space2++;
		}
		i++;
	}
	
	//ret for all, but "quit": looks ugly, but works
	if(flagPipe == 0 && flagIn == 0 && flagOut1 == 0 && flagOut2 == 0) { ret = 1; }
	else if(flagPipe == 0 && flagIn == 1 && flagOut1 == 0 && flagOut2 == 0) { ret = 2; }
	else if(flagPipe == 1 && flagIn == 1 && flagOut1 == 0 && flagOut2 == 0) { ret = 6; }
	else if(flagPipe == 0 && flagOut1 == 1 && flagOut2 == 0) { ret = 3; }
	else if(flagPipe == 0 && flagOut1 == 0 && flagOut2 == 1) { ret = 4; }
	else if(flagPipe == 1 && flagIn == 0 && flagOut1 == 0 && flagOut2 == 0) { ret = 5; }
	else if(flagPipe == 1 && flagOut1 == 1 && flagOut2 == 0) { ret = 7; }
	else if(flagPipe == 1 && flagOut1 == 0 && flagOut2 == 1) { ret = 8; }
	else { ret = 9; }
	
	cmd3[space1] = NULL; //making sure last part of cmds are NULL
	cmd4[space2] = NULL;
    return ret;
}

void priNotNine(char **cmd3, char **cmd4, char *infile, char *outfile, int j) { //done
	int k = 0;
	logFunc(3);
	printf("return code is %d\n", j);
	if(j < 9) {
		while (cmd3[k] != NULL){
			logFuncPr(cmd3[k], "", k, 0);
			printf("cmd1[%d] = %s  ", k, cmd3[k]);
			k++;
		}
		if(cmd3[0] != NULL){
			logFuncPr(cmd3[k], "", k, 1);
			printf("\n");
		}
		k=0;
		while (cmd4[k] != NULL){
			logFuncPr(cmd4[k], "", k, 2);
			printf("cmd2[%d] = %s  ", k, cmd4[k]);
			k++;
		} if(cmd4[0] != NULL){
			logFuncPr(cmd4[k], "", k, 1);
			printf("\n");
		}
        if (strlen(infile)) {
			logFuncPr(NULL, infile, k, 3);
			printf("input redirection file name: %s\n", infile);
		}
		if (strlen(outfile)){
			logFuncPr(NULL, outfile, k, 4);
			printf("output redirection file name: %s\n", outfile);
		}
	}
}

void exec_cmd(char **cmd3) { //done
    // Fork process
    pid_t pid = fork();
    // Error
	
    if (pid == -1) {
		logFunc(11);
        char* error = strerror(errno);
        printf("fork: %s\n", error);
		return;
    }
    // Child process
    else if (pid == 0) {
        // Execute command
		logFunc(6);
        execvp(cmd3[0], cmd3);  

        // Error occurred
        char* error = strerror(errno);
        printf("shell: %s: %s\n", cmd3[0], error);
		logFunc(8);
        exit(1);
    }
    // Parent process
    else {
        // Wait for child process to finish
		logFunc(14);
        int childStatus;
        waitpid(pid, &childStatus, 0);
		logFunc(7);
        return;
    }
}

void exec_pipe(char **cmd3, char **cmd4){ //done
	pid_t pid;
	int pipefd[2];

	//Creating pipe from pipefd and error checking
	if(pipe(pipefd)==-1){
		logFunc(13);
		printf("PIPE ERROR! EXITING...\n");
		exit(1);
	}
	//Begin feeding the pipe
	if((pid=fork())<0){
		logFunc(11);
		printf("FORK ERROR! EXITING...\n");
		exit(1);
	}
	else if(pid==0){
		logFunc(6);
		if((pid=fork())<0){
			logFunc(11);
			printf("FORK ERROR! EXITING...\n");
			exit(1);
		}
		else if(pid==0){
			dup2(pipefd[1],1);	//Swap STDOUT_FILENO with pipefd[1]
			close(pipefd[0]);
			close(pipefd[1]);
			logFunc(6);
			execvp(cmd3[0],cmd3);
			//If system call fails
			printf("SYSTEM CALL ERROR! EXITING...\n");
			logFunc(8);
			exit(1);
		}
		dup2(pipefd[0],0);	//Swap STDIN_FILENO with pipefd[0]
		close(pipefd[0]);
		close(pipefd[1]);
		logFunc(6);
		execvp(cmd4[0],cmd4);
		//If system call fails
		printf("SYSTEM CALL ERROR! EXITING...\n");
		logFunc(8);
		exit(1);
	}
	else{
		close(pipefd[0]);
		close(pipefd[1]);
		logFunc(14);
		wait(NULL);
		logFunc(7);
	}
}

void exec_cmd_in(char** cmd3, char* infile){ //done
	pid_t pid;
	int fd_in;
	
	pid = fork();
	if (pid < 0) {
		logFunc(11);
		perror("Could not create process");
		exit(1);
	}
	else if(pid == 0) {
		logFunc(6);
		fd_in = open(infile, O_RDONLY);
		logFunc(4);
		if(fd_in == -1) {
			logFunc(9);
			printf("The Infile does not exist on this system.\n");
			exit(99); //does not need to do the rest and liking 99 for 
		}
		if(dup2(fd_in, 0) == -1){
			logFunc(9);
			printf("Infile not upated in the table.\n");
			exit(100);
		}
		close(fd_in);
		execvp(cmd3[0], cmd3);
		
		//if system call fails
		printf("SYSTEM CALL ERROR! EXITING...\n");
		logFunc(8);
		exit(1);
	}
	else {
		logFunc(14);
		int childStatus2;
		waitpid(pid, &childStatus2, 0);
		logFunc(7);
	}
	//close(fd_in);
	
	return;
}

void exec_cmd_opt_in_append(char** cmd3, char* infile, char* outfile){ //done
	pid_t pid;
	int fd_in;
	int fd_out;
	
	if((pid=fork())<0){
		logFunc(11);
		printf("FORK ERROR! EXITING...\n");
		exit(1);
	}
	else if(pid==0){
		if((fd_in = open(infile, O_RDONLY))==-1){
			logFunc(9);
			//printf("No infile!\n");
		}
		if((fd_out = open(outfile, O_RDWR | O_APPEND | O_CREAT, S_IRUSR | S_IWUSR))==-1){
			logFunc(9);
			printf("No outfile!\n");
		}
		if(dup2(fd_in, 0)==-1){
			logFunc(9);
			//printf("Infile not updated in table!\n");
		}
		if(dup2(fd_out, 1)==-1){
			logFunc(9);
			printf("Outfile not updated in table!\n");
		}
		close(fd_in);
		close(fd_out);
		logFunc(6);
		execvp(cmd3[0],cmd3);

		//If system call fails
		printf("SYSTEM CALL ERROR! EXITING...\n");
		logFunc(8);
		exit(1);
	}
	else{
		logFunc(14);
		wait(NULL);
		logFunc(7);
	}
}

void exec_cmd_opt_in_write(char** cmd3, char* infile, char* outfile){ //done
	pid_t pid;
	int fd_in;
	int fd_out;
	
	if((pid=fork())<0){
		logFunc(11);
		printf("FORK ERROR! EXITING...\n");
		exit(1);
	}
	else if(pid==0){
		if((fd_in = open(infile, O_RDONLY))==-1){
			logFunc(11);
			//printf("No infile!\n");
		}
		if((fd_out = open(outfile, O_WRONLY | O_TRUNC | O_CREAT, S_IRUSR | S_IWUSR))==-1){
			logFunc(11);
			//printf("No outfile!\n");
		}
		if(dup2(fd_in, 0)==-1){
			logFunc(11);
			printf("Infile not updated in table!\n");
		}
		if(dup2(fd_out, 1)==-1){
			logFunc(11);
			printf("Outfile not updated in table!\n");
		}
		close(fd_in);
		close(fd_out);
		logFunc(6);
		execvp(cmd3[0],cmd3);

		//If system call fails
		printf("SYSTEM CALL ERROR! EXITING...\n");
		logFunc(14);
		exit(1);
	}
	else{
		wait(NULL);
		logFunc(7);
	}
}

void exec_pipe_in(char** cmd3, char** cmd4, char* infile){ //done
	pid_t pid;
	int fd_in;
	int pipefd[2];

	//Creating pipe from pipefd and error checking
	if(pipe(pipefd)==-1){
		logFunc(13);
		printf("PIPE ERROR! EXITING...\n");
		exit(1);
	}
	//Begin feeding the pipe
	if((pid=fork())<0){
		logFunc(11);
		printf("FORK ERROR! EXITING...\n");
		exit(1);
	}
	else if(pid==0){
		if((fd_in = open(infile, O_RDONLY))==-1){
			logFunc(9);
			printf("No infile!\n");
			exit(99); //no file exit
		}
		if(dup2(fd_in, 0)==-1){
			logFunc(9);
			printf("Infile not updated in table!\n");
			exit(100); //table not updated
		}
		close(fd_in);

		if((pid=fork())<0){
			logFunc(11);
			printf("FORK ERROR! EXITING...\n");
			exit(1);
		}
		else if(pid==0){
			dup2(pipefd[1],1);	//Swap STDOUT_FILENO with pipefd[1]
			close(pipefd[0]);
			close(pipefd[1]);
			logFunc(6);
			execvp(cmd3[0],cmd3);
			//If system call fails
			printf("SYSTEM CALL ERROR! EXITING...\n");
			logFunc(8);
			exit(1);
		}
		dup2(pipefd[0],0);	//Swap STDIN_FILENO with pipefd[0]
		close(pipefd[0]);
		close(pipefd[1]);
		logFunc(6);
		execvp(cmd4[0],cmd4);
		//If system call fails
		printf("SYSTEM CALL ERROR! EXITING...\n");
		logFunc(8);
		exit(1);
	}
	else{
		close(pipefd[0]);
		close(pipefd[1]);
		logFunc(14);
		wait(NULL);
		logFunc(7);
	}
}

void exec_pipe_opt_in_append(char** cmd3, char** cmd4, char* infile, char* outfile){ //done
	pid_t pid;
	int fd_in;
	int fd_out;
	int pipefd[2];

	//Creating pipe from pipefd and error checking
	if(pipe(pipefd)==-1){
		logFunc(13);
		printf("PIPE ERROR! EXITING...\n");
		exit(1);
	}
	//Begin feeding the pipe
	if((pid=fork())<0){
		logFunc(11);
		printf("FORK ERROR! EXITING...\n");
		exit(1);
	}
	else if(pid==0){
		if((fd_in = open(infile, O_RDONLY))==-1){
			logFunc(9);
			//printf("No infile!\n");
		}
		if((fd_out = open(outfile, O_RDWR | O_APPEND | O_CREAT, S_IRUSR | S_IWUSR))==-1){
			logFunc(9);
			printf("No outfile!\n");
		}
		if(dup2(fd_in, 0)==-1){
			logFunc(9);
			//printf("Infile not updated in table!\n");
		}
		if(dup2(fd_out, 1)==-1){
			logFunc(9);
			printf("Outfile not updated in table!\n");
		}
		close(fd_in);
		close(fd_out);

		if((pid=fork())<0){
			logFunc(11);
			printf("FORK ERROR! EXITING...\n");
			exit(1);
		}
		else if(pid==0){
			dup2(pipefd[1],1);	//Swap STDOUT_FILENO with pipefd[1]
			close(pipefd[0]);
			close(pipefd[1]);
			logFunc(6);
			execvp(cmd3[0],cmd3);
			//If system call fails
			printf("SYSTEM CALL ERROR! EXITING...\n");
			logFunc(8);
			exit(1);
		}
		dup2(pipefd[0],0);	//Swap STDIN_FILENO with pipefd[0]
		close(pipefd[0]);
		close(pipefd[1]);
		logFunc(6);
		execvp(cmd4[0],cmd4);
		//If system call fails
		printf("SYSTEM CALL ERROR! EXITING...\n");
		logFunc(8);
		exit(1);
	}
	else{
		close(pipefd[0]);
		close(pipefd[1]);
		logFunc(14);
		wait(NULL);
		logFunc(7);
	}
}

void exec_pipe_opt_in_write(char** cmd3, char** cmd4, char* infile, char* outfile){ //done
	pid_t pid;
	int fd_in;
	int fd_out;
	int pipefd[2];

	//Creating pipe from pipefd and error checking
	if(pipe(pipefd)==-1){
		logFunc(13);
		printf("PIPE ERROR! EXITING...\n");
		exit(1);
	}

	//Begin feeding the pipe
	if((pid=fork())<0){
		logFunc(11);
		printf("FORK ERROR! EXITING...\n");
		exit(1);
	}
	else if(pid==0){
		if((fd_in = open(infile, O_RDONLY))==-1){
			logFunc(9);
			//printf("No infile!\n");
		}
		if((fd_out = open(outfile, O_WRONLY | O_TRUNC | O_CREAT, S_IRUSR | S_IWUSR))==-1){
			logFunc(9);
			printf("No outfile!\n");
		}
		if(dup2(fd_in, 0)==-1){
			logFunc(9);
			//printf("Infile not updated in table!\n");
		}
		if(dup2(fd_out, 1)==-1){
			logFunc(9);
			printf("Outfile not updated in table!\n");
		}
		close(fd_in);
		close(fd_out);

		if((pid=fork())<0){
			logFunc(11);
			printf("FORK ERROR! EXITING...\n");
			exit(1);
		}
		else if(pid==0){
			dup2(pipefd[1],1);	//Swap STDOUT_FILENO with pipefd[1]
			close(pipefd[0]);
			close(pipefd[1]);
			logFunc(6);
			execvp(cmd3[0],cmd3);
			//If system call fails
			printf("SYSTEM CALL ERROR! EXITING...\n");
			logFunc(8);
			exit(1);
		}
		dup2(pipefd[0],0);	//Swap STDIN_FILENO with pipefd[0]
		close(pipefd[0]);
		close(pipefd[1]);
		logFunc(6);
		execvp(cmd4[0],cmd4);
		//If system call fails
		printf("SYSTEM CALL ERROR! EXITING...\n");
		logFunc(8);
		exit(1);
	}
	else{
		close(pipefd[0]);
		close(pipefd[1]);
		logFunc(14);
		wait(NULL);
		logFunc(7);
	}
}

void logFunc(int test){ //done
	FILE * fp;
	if(test == 0){ fp = fopen("foo.txt","w"); fprintf(fp,"Start of program\n");}
	else if(test >= 1){
		fp = fopen("foo.txt","a");
		if(test == 1) {fprintf(fp,"  Start processing the funtion\n");}
		else if(test == 2) {fprintf(fp,"    In the parsing function\n");}
		else if(test == 3) {fprintf(fp,"    In the printing function\n");}
		else if(test == 4) {fprintf(fp,"    Opening an infile\n");}
		else if(test == 5) {fprintf(fp,"    Opening an outfile\n");}
		else if(test == 6) {fprintf(fp,"    In the child process\n");}
		else if(test == 7) {fprintf(fp,"    In the parent process\n");}
		else if(test == 8) {fprintf(fp,"    Error doing exec\n");}
		else if(test == 9) {fprintf(fp,"    Error with file handling\n");}
		else if(test == 10) {fprintf(fp,"Ending the program\n");}
		else if(test == 11) {fprintf(fp,"    Error forking\n");}
		else if(test == 12) {fprintf(fp,"  Ending processing the funtion\n");}
		else if(test == 13) {fprintf(fp,"    Error piping\n");}
		else if(test == 14) {fprintf(fp,"     Waiting on wait\n");}
		else {fprintf(fp,"Not sure what happened #1\n");}
	}
	else {fprintf(fp,"Not sure what happened #2\n");}
	fclose(fp);
	return;
}

void logFuncPr(char **blah1, char *blah2, int num, int test){
	FILE * fp;
	fp = fopen("foo.txt","a");
	if(test == 0) {fprintf(fp,"cmd1[%d] = %s  ", num, blah1);}
	else if(test == 1) {fprintf(fp,"\n");}
	else if(test == 2) {fprintf(fp,"cmd2[%d] = %s  ", num, blah1);}
	else if(test == 3) {fprintf(fp,"input redirection file name: %s\n", blah2);}
	else if(test == 4) {fprintf(fp,"output redirection file name: %s\n", blah2);}
	else {fprintf(fp,"Not sure what happened #3\n");}
	fclose(fp);
}

void swithRules(char **cmd3, char **cmd4, char *infile, char *outfile, int j){ //done
	switch (j){
		case 0:
			logFunc(12);
			logFunc(10);
			return;
		case 1:
			printf("\n");
			exec_cmd(cmd3);
			break;
		case 2:
			printf("\n");
			exec_cmd_in(cmd3, infile);
			break;
		case 3:
			printf("\n");
			exec_cmd_opt_in_append(cmd3, infile, outfile);
			break;
		case 4:
			printf("\n");
			exec_cmd_opt_in_write(cmd3, infile, outfile);
			break;
		case 5:
			printf("\n");
			exec_pipe(cmd3, cmd4);
			break;
		case 6:
			printf("\n");
			exec_pipe_in(cmd3, cmd4, infile);
			break;
		case 7:
			printf("\n");
			exec_pipe_opt_in_append(cmd3, cmd4, infile, outfile);
			break;
		case 8:
			printf("\n");
			exec_pipe_opt_in_write(cmd3, cmd4, infile, outfile);
			break;
		default:
			printf("\nnot yet handled\n");
	}
	logFunc(12);
}
