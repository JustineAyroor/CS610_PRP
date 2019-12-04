import java.util.concurrent.atomic.AtomicBoolean;

class Lexicon_6777{
    int maxslots;
    int HtableSize;
    int WordsSize;
    int[] hashtable;
    char[] words;

    public Lexicon_6777() {
        super();
    }

    public static void HashCreate(Lexicon_6777 l, int m){
        l.hashtable = new int[m];
        for(int items = 0; items < l.hashtable.length; items++){
            l.hashtable[items] = -1;
        }
        l.words = new char[15*m];
        l.HtableSize = 0;
        l.WordsSize = 0;
        l.maxslots = m;

    }

    public static void HashPrint(Lexicon_6777 l){
        System.out.print("HASHTABLE --> T\t");
        System.out.print("\tWORDS_ARRAY --> A :");
        for(int item = 0; item < l.words.length;item++){
            // l.words[item] = '\0';
            System.out.print(l.words[item]);
        }
        System.out.print("\n");
        for(int slot = 0; slot < l.hashtable.length;slot++){
            if(l.hashtable[slot] == -1){
                System.out.println(slot + " : ");
            }else{
                System.out.println(slot + " : " + l.hashtable[slot]);
            }
        }
        
    }

    public static boolean AFull(Lexicon_6777 l) {
        return (l.WordsSize == l.words.length);
    }

    public static boolean HashEmpty(Lexicon_6777 l){
        return (l.HtableSize == 0);
    }

    public static boolean HashFull(Lexicon_6777 l) {
        return (l.HtableSize == l.hashtable.length);
    }


    public static void HashSearch(Lexicon_6777 l, String word){
        int i = 0;
        int hashedkey = l.HashMe(word,i);
        int reference = l.hashtable[hashedkey];
        // find in array 
        boolean exist = l.WordsSearch(word.toCharArray(), reference);
        if(exist){
            System.out.println("Found " + word + " at " + hashedkey + ".");
        }else{
            do{
                hashedkey = l.HashMe(word,++i);
                reference = l.hashtable[hashedkey];
                exist = l.WordsSearch(word.toCharArray(), reference);
                if(exist){
                    break;
                }
            }while(i<l.maxslots);
            
            if(exist){
                System.out.println("Found " + word + " at " + hashedkey + ".");
            }else{System.out.println("NOT FOUND");}
            
        }
    }


    public static void HashInsert(Lexicon_6777 l, String word){
        // GENERATE KEY
        int i = 0;
        int reference = l.WordsSize;
        int hashedkey = l.HashMe(word,i); 
        int stpIndx = hashedkey;
        // System.out.println(hashedkey + " : " + reference);
        // CHECK IF KEY IS EMPTY
        if(l.hashtable[hashedkey] == -1){
            // STORE REFERENCE INTO HASHTABLE IF EMPTY SLOT FOUND
            l.hashtable[hashedkey] = reference;
             // INSERT INTO WORD ARRAY 
            l.WordsInsert(word.toCharArray());
            // UPDATE HASHTABLE SIZE
            l.HtableSize++;
            // System.out.println(l.HtableSize);
        }else{
            // Qudratic probing Logic
            // System.out.println("It is not Empty Finding Another Slot");
            do{
                hashedkey = l.HashMe(word, ++i);                
            }while(l.hashtable[hashedkey] != -1 || i < l.maxslots);
            if(hashedkey == stpIndx){
                System.out.println("Cannot insert because all Slots Full");
            }else{
                // STORE REFERENCE INTO HASHTABLE IF EMPTY SLOT FOUND
                l.hashtable[hashedkey] = reference;
                // System.out.println(hashedkey + " : " + reference);
                // INSERT INTO WORD ARRAY 
                l.WordsInsert(word.toCharArray());
                l.HtableSize++;
                // System.out.println(l.HtableSize);
            }
        }
    }

    public void ReHashInsert(){
        // Iterate through A and get the new key to insert it at

    }

    public void WordsInsert(char[] w){ 
        int stpIndx = (WordsSize + w.length);
        // System.out.println(stpIndx);
        int wrdCount = 0;
        for(int chars = WordsSize; chars < stpIndx; chars++){
            words[chars] = w[wrdCount++];   
        }
        words[WordsSize+w.length] = '/';
        WordsSize += (w.length+1);
        // System.out.println(WordsSize);
    }

    public boolean WordsSearch(char[] w, int ref){
        int wrdCount = 0;
        boolean found = false;
        int similarityCount = 0;
        for(int i = ref; i < (ref+w.length);i++){
            if(words[i] == w[wrdCount++]){
                similarityCount += 1;
            }else{
                found = false;
                break;
            }  
        }
        if(similarityCount == w.length){found = true;}else{found = false;}
        return found;
    }

    private int HashMe(String word, int index){
        char[] w = word.toCharArray();
        int Ascii_SUM = 0;
        for(char c : w){
            Ascii_SUM += (int)c;
        }
        int key = ((Ascii_SUM%maxslots) + (index*index))%maxslots;
        return key;
    }

    public static void main(String[] args) {
        Lexicon_6777 lex = new Lexicon_6777();
        HashCreate(lex, 11);
        HashInsert(lex, "alex");
        HashInsert(lex, "alexadra");
        // System.out.println(lex.words.length);
        HashInsert(lex, "justy");
        HashInsert(lex, "tom");
        HashInsert(lex, "jerry");
        HashInsert(lex, "john");
        HashInsert(lex, "Dom");
        HashInsert(lex, "justine");
        HashInsert(lex, "julian");
        HashInsert(lex, "choco");
        HashInsert(lex, "loco");
        HashSearch(lex, "alex");
        HashSearch(lex, "loco");
        HashSearch(lex, "choco");
        HashSearch(lex, "vinay");
        HashSearch(lex, "justine");
        // System.out.println(lex.WordsSize);
        HashPrint(lex);
        System.out.println(HashEmpty(lex));
        System.out.println(HashFull(lex));
    }

}