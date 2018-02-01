To run this program please make sure you are at the directory of 6140, then follow these procedures:

Compile:
1) javac src/*.java -d .
2) jar cfm mvcSol.jar manifest.txt *.class

Run: the generated jar file in the above step is the executable, run it as:
java -jar mvcSol.jar -inst Data/karate.graph -seed 10 -time 600 -alg APPROX


Comments:
"-alg": the name of the algorithm you wanna run, choice are approx, ls1, ls2 and bnb (lowercase or uppercase doesn't matter);
"-inst": the data file name;
"-seed": random seed (for local search);
"-time": cutoff time (in seconds)
