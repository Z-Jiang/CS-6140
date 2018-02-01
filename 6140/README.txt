Folder:
1) src : this contains all the java files


Pre-req: you are at the directory of 6140

Compile:
1) javac src/*.java -d .
2) jar cfm mvcSol.jar manifest.txt *.class

Run: the generated jar file in the above step is the executable . You can run t as below:
java -jar mvcSol.jar -inst Data/karate.graph -seed 10 -time 600 -alg APPROX

"-alg": approx, ls1, ls2, bnb (lowercase or uppercase doesn't matter);
"-inst": the data fiile name;
"-seed": random seed;
"-time": cutoff time (in seconds)
