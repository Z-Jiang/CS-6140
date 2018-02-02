# CS-6140

To run this program in terminal, make sure you are at the directory of "6140" folder, then follow these procedures:

Compile:
1) javac src/*.java -d .
2) jar cfm mvcSol.jar manifest.txt *.class

Run: 
the generated jar file in the above step is the executable, run it as "java -jar mvcSol.jar -inst Data/karate.graph -seed 10 -time 600 -alg APPROX"

Flags:
1) "-alg": the name of the algorithm you want to run, choices are: approx, ls1, ls2 and bnb (lowercase or uppercase doesn't matter)
2) "-inst": the data file name
3) "-seed": random seed (for local search)
4) "-time": cutoff time (in seconds)
