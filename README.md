# PLC_Project2

## How to Run the Code
To run the code, follow these steps:
1. Clone the git Repo
2. Compile the antlr code with the -visitor flag
3. Compile the java files
4. Run the main program with the tests

In order to complete these steps, use these commands in terminal (assuming you have git, antlr4 and java installed)

```java
	git clone https://github.com/jrivera97/PLC_Project2.git
	antlr4 Calculator.g4 -visitor
	javac *.java
	java Main <testfile>.txt
```
