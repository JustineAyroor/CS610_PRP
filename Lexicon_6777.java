// JUSTINE AYROOR    cs610 6777 prp

import java.io.*;
import java.util.*;


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

    public static boolean HashEmpty(Lexicon_6777 l, int key){
        if(l.hashtable[key] == -1){
            return true;
        }else{
            return false;
        }
    }

    public static boolean HashFull(Lexicon_6777 l) {
        return (l.HtableSize == l.hashtable.length);
    }

    public static int HashSearch(Lexicon_6777 l, String word){
        int i = 0;
        int hashedkey = l.HashMe(word,i);
        int reference = l.hashtable[hashedkey];
        // find in array 
        boolean exist = l.WordsSearch(word.toCharArray(), reference);
        if(exist){
            return hashedkey;
        }else{
            do{
                hashedkey = l.HashMe(word,++i);
                reference = l.hashtable[hashedkey];
                exist = l.WordsSearch(word.toCharArray(), reference);
                if(exist){
                    break;
                }
            }while(i<l.maxslots);
            
            if(exist && i < l.maxslots){
                return hashedkey;
            }else{
                return -1;
            }           
        }
    }
    public static int HashDelete(Lexicon_6777 l, String word){
        // SEARCH FOR KEY IN HASHTABLE
        int index = HashSearch(l, word);
        
        if(index != -1){
            int ref = l.hashtable[index];
            // DELETE FROM WORD 
            l.WordsDelete(word.toCharArray(), ref);
            // DELETE FROM HASH
            l.hashtable[index] = -1;
            l.HtableSize--;
            return index;
        }else{
            return -1;
        }

    }
    public double getLF(){
        double lf = ((double)HtableSize/maxslots);
        // System.out.println(lf);
        return lf;
    }

    public static int HashInsert(Lexicon_6777 l, String word){
        // Check Load Factor
        if(l.getLF() >= 0.80 || HashFull(l)){
            // Rehash All 
            ReHash(l);
        }
        // GENERATE KEY
        int i = 0;
        int reference = l.WordsSize;
        int hashedkey = l.HashMe(word,i); 
        // CHECK IF KEY IS EMPTY
        if(HashEmpty(l, hashedkey)){
            // STORE REFERENCE INTO HASHTABLE IF EMPTY SLOT FOUND
            l.hashtable[hashedkey] = reference;
             // INSERT INTO WORD ARRAY 
            l.WordsInsert(word.toCharArray());
            // UPDATE HASHTABLE SIZE
            l.HtableSize++;
            return hashedkey;
        }else{
            // Qudratic probing Logic
            do{
                hashedkey = l.HashMe(word.trim(), ++i);
            }while(!HashEmpty(l, hashedkey) && i < l.maxslots);
            if(i > l.maxslots){
                System.out.println("Cannot insert because all Slots Full");
                return -1;
            }else{
                // STORE REFERENCE INTO HASHTABLE IF EMPTY SLOT FOUND
                l.hashtable[hashedkey] = reference;

                // INSERT INTO WORD ARRAY 
                l.WordsInsert(word.toCharArray());
                l.HtableSize++;
                return hashedkey;
            }
        }

    }

    public static int[] RehashInsert(Lexicon_6777 l,int[] lT, String word){
        int i = 0;
        int reference = l.hashtable[HashSearch(l, word)];
        int hashedkey = l.HashMe(word.trim(),i); 
        
        // CHECK IF KEY IS EMPTY
        if(lT[hashedkey] == -1){
            // STORE REFERENCE INTO HASHTABLE IF EMPTY SLOT FOUND
            lT[hashedkey] = reference;
        }else{
            // Qudratic probing Logic
            do{
                hashedkey = l.HashMe(word.trim(), ++i);  
                
            }while(lT[hashedkey] != -1 && i < l.maxslots);
            if(i > l.maxslots){
                System.out.println("Cannot insert because all Slots Full in Rehash");
            }else{
                // STORE REFERENCE INTO HASHTABLE IF EMPTY SLOT FOUND
                lT[hashedkey] = reference;
            }
        }
        return lT;
    }
    public static void ReHash(Lexicon_6777 l){
        
        int m = 2*l.hashtable.length;
        l.HtableSize = 0;
        int[] N_hashTable = new int[m];
        char[] A_Copy = new char[15*m];
        for(int i = 0; i < N_hashTable.length; i++){
            N_hashTable[i] = -1; 
        }
        String word = "";
        // Iterate through A 
        for(int i = 0; i < l.words.length; i++){
            if(l.words[i] == '/' ){
                //get the new key to insert it at store in hashTable copy
                N_hashTable = RehashInsert(l, N_hashTable,word.trim());
                word = "";
                l.HtableSize++;
            }else if(l.words[i] == '*'){
                continue;
            }else{
                word += l.words[i];
            }
        }

        //Make final Assignments
        l.hashtable = N_hashTable;
        l.maxslots = m;
        System.arraycopy(l.words, 0, A_Copy, 0, l.words.length);
        l.words = A_Copy;
    }
    

    public void WordsInsert(char[] w){ 
        int stpIndx = (WordsSize + w.length);
        int wrdCount = 0;
        for(int chars = WordsSize; chars < stpIndx; chars++){
            words[chars] = w[wrdCount++];   
        }
        words[WordsSize+w.length] = '/';
        WordsSize += (w.length+1);
    }

    public boolean WordsSearch(char[] w, int ref){
        int wrdCount = 0;
        boolean found = false;
        int similarityCount = 0;
        if(ref == -1){ref = 0;}
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

    public void WordsDelete(char[] w, int ref){
        for(int i = ref; i < (ref+w.length);i++){
            words[i] = '*';
        }
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

    public static void HashBatch(Lexicon_6777 l, String filename){
        String st;
        try{
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null){
                // System.out.println(st);
                String Operation = st.substring(0, 2);
                String WordValue = "";
                if(st.length() > 2){
                    WordValue = st.substring(3, st.length());
                }else{
                    WordValue = "";
                }
                if(Operation.equals("10")){
                    int index = HashInsert(l, WordValue);
                    if(index != -1){
                        // System.out.println("inserted " + WordValue + " at slot : " + index);
                    }else{
                        // System.out.println(WordValue + " not Inserted");
                    }
                }else if(Operation.equals("11")){
                    int index = HashDelete(l, WordValue);
                    if(index != -1){
                        System.out.println("deleted " + WordValue + " from slot : " + index);
                    }else{
                        System.out.println(WordValue + " not found. So not Deleted");
                    }
                }else if(Operation.equals("12")){
                    int index = HashSearch(l, WordValue);
                    if(index != -1){
                        System.out.println(WordValue + " found at slot : " + index);
                    }else{
                        System.out.println(WordValue + " not found");
                    }
                }else if(Operation.equals("13")){
                    HashPrint(l);
                    double lf = l.getLF();
                    System.out.println("Load Factor : lf = " + l.HtableSize + " / " + l.hashtable.length + " ~ " + lf);
                }else if(Operation.equals("14")){
                    HashCreate(l, Integer.parseInt(WordValue));
                }else{
                    continue;
                }
            }
            br.close();
        }catch(Exception e){e.printStackTrace();}
        

    }

    public static void main(String[] args) {
        Lexicon_6777 lex = new Lexicon_6777();

        // BATCH TESTING
        HashBatch(lex, "hashBatch.txt");

        // INDIVIDUAL TESTING
        HashDelete(lex, "julian");
        HashPrint(lex);
        double lf = lex.getLF();
        System.out.println("Load Factor : lf = " + lex.HtableSize + " / " + lex.hashtable.length + " ~ " + lf);
        
    }

}