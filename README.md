# PLC_Project2

## How to Run the Code
To run the code, follow these steps:
1. Compile the antlr code with the -visitor flag
2. Compile the java files
3. Run the main program with the tests

In order to complete these steps, use these commands in terminal (assuming you have git, antlr4 and java installed)

		```
			antlr4 Calculator.g4 -visitor
			javac *.java
			java Main <testfile>.txt
		```

	Where <testfile>.txt is simply the name of the test file you wish to use
