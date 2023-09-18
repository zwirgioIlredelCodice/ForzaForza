# ForzaForza
a ConnectX player, course of Algorithm and data structure, university of Bologna

## setup

* extract CXGame10.tgz
* copy `ForzaForza` folder in `CXGame1.0/connectx` 

## compile & run (CXGame1.0/connectx/README)

- Command-line compile.  In the connectx/ directory run::

		javac -cp ".." *.java */*.java


CXGame application:

- Human vs Computer.  In the connectx/ directory run:
	
		java -cp ".." connectx.CXGame 6 7 4 connectx.L0.L0


- Computer vs Computer. In the connectx/ directory run:

		java -cp ".." connectx.CXGame 6 7 4 connectx.L0.L0 connectx.L1.L1


CXPlayerTester application:

- Output score only:

	java -cp ".." connectx.CXPlayerTester 6 7 4 connectx.L0.L0 connectx.L1.L1


- Verbose output

	java -cp ".." connectx.CXPlayerTester 6 7 4 connectx.L0.L0 connectx.L1.L1 -v


- Verbose output and customized timeout (1 sec) and number of game repetitions (10 rounds)

	java -cp ".." connectx.CXPlayerTester 6 7 4 connectx.L0.L0 connectx.L1.L1 -v -t 1 -r 10

