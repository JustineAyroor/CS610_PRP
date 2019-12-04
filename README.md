# CS610_PRP

# Completed Both OPTION 1 & OPTION 2
OPTION 1 :
COMMAND:
javac Lexicon_6777.java 

# IN MAIN METHOD 
Lexicon_6777 lex = new Lexicon_6777();

--> BATCH TESTING
HashBatch(lex, "your file name in String"); 

--> INDIVIDUAL TESTING
HashDelete(lex, "julian");
HashPrint(lex);
double lf = lex.getLF();
System.out.println("Load Factor : lf = " + lex.HtableSize + " / " + lex.hashtable.length + " ~ " + lf);

OPTION 2 :

a --> HITS 
b --> PAGE RANK  

COMMAND:
java hits_6777 iterations initialvalue filename
java pgrk_6777 iterations initialvalue filename



NOTE : 3 ARGS TO BE FEED WHILE EUNNING THE CODE AS MENTIONED IN PROBLEM STATEMENT

THANK YOU