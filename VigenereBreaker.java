import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder sb = new StringBuilder();
        for(int i = whichSlice; i < message.length(); i += totalSlices) {
            sb.append(message.charAt(i));
        }
        return sb.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        for(int i = 0; i < klength; i++) {
            String s = sliceString(encrypted, i, klength);
            CaesarCracker cc = new CaesarCracker(mostCommon);
            int dkey = cc.getKey(s);
            key[i] = dkey;
        }
        return key;
    }
    
    public HashSet<String> readDictionary(String lang) {
        HashSet<String> hs = new HashSet<String>();
        FileResource fr = new FileResource("dictionaries/"+lang);
        for(String line : fr.lines()) {
            String lower = line.toLowerCase();
            hs.add(lower);
        }
        return hs;
    }
    
    public int countWords(String message, HashSet<String> dictionary) {
        int count = 0;
        for(String word : message.split("\\W+")) {
            String lower = word.toLowerCase();
            if(dictionary.contains(lower)) {
                count++;
            }
        }
        return count;
    }
    
    public String breakForLanguage(String encrypted, HashSet<String> dictionary, char common) {
        int count = 0;
        int keyLength = 0;
        for(int i = 1; i < 100; i++) {
            int[] keys = tryKeyLength(encrypted, i, common);
            VigenereCipher vc = new VigenereCipher(keys);
            String decrypted = vc.decrypt(encrypted);
            int currCount = countWords(decrypted, dictionary);
            if(currCount > count) {
                count = currCount;
                keyLength = i;
            }
        }
        int[] keys = tryKeyLength(encrypted, keyLength, common);
        VigenereCipher vc = new VigenereCipher(keys);
        String decrypted = vc.decrypt(encrypted);
        return count + " " + keyLength + "\n" + decrypted.substring(0, 100);
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary) {
        HashMap<Character,Integer> counts = new HashMap<Character,Integer>();
        for(String word : dictionary) {
            for(int i = 0; i < word.length(); i++) {
                char currChar = word.charAt(i);
                if(!counts.containsKey(currChar)) {
                    counts.put(currChar, 1);
                } else {
                    counts.put(currChar, counts.get(currChar) + 1);
                }
            }
        }
        int max = 0;
        char mostChar = 'a';
        for(char c : counts.keySet()) {
            int currCount = counts.get(c);
            if(currCount > max) {
                max = currCount;
                mostChar = c;
            }
        }
        return mostChar;
    }
        
    public void breakForAllLangs(String encrypted, HashMap<String,HashSet<String>> languages) {
        int max = 0;
        String bestLang = null;
        for(String lang : languages.keySet()) {
            HashSet<String> currLang = languages.get(lang);
            char mostCommon = mostCommonCharIn(currLang);
            String broken = breakForLanguage(encrypted, currLang, mostCommon);
            int currCount = Integer.parseInt(broken.split("\\W+")[0]);
            if(currCount > max) {
                max = currCount;
                bestLang = lang;
            }
        }
        HashSet<String> finalLang = languages.get(bestLang);
        System.out.println("Language: " + bestLang);
        System.out.println(breakForLanguage(encrypted, finalLang, mostCommonCharIn(finalLang)));
    }

    public void breakVigenere () {
        FileResource fr = new FileResource();
        String text = fr.asString();
        HashMap<String,HashSet<String>> dictionaries = new HashMap<String,HashSet<String>>();
        String[] languages = {"Danish", "Dutch", "English", "French", "German", "Italian", "Portuguese", "Spanish"};
        for(String lang : languages) {
            dictionaries.put(lang, readDictionary(lang));
        }
        breakForAllLangs(text, dictionaries);
    }
    
}
