
/**
 * Write a description of Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import java.util.*;

public class Tester {
    public void testSliceString() {
        VigenereBreaker vb = new VigenereBreaker();
        System.out.println(vb.sliceString("abcdefghijklm", 0, 3));
    }
    
    public void testTryKeyLength() {
        VigenereBreaker vb = new VigenereBreaker();
        FileResource fr = new FileResource();
        String s = fr.asString();
        int[] arr = vb.tryKeyLength(s, 4, 'e');
        for(int i : arr) {
            System.out.println(i);
        }
    }
    
    public void testCountWords() {
        VigenereBreaker vb = new VigenereBreaker();
        FileResource fr = new FileResource();
        String text = fr.asString();
        int[] keys = vb.tryKeyLength(text, 38, 'e');
        VigenereCipher vc = new VigenereCipher(keys);
        String decrypted = vc.decrypt(text);
        HashSet<String> dictionary = vb.readDictionary();
        System.out.println(vb.countWords(decrypted, dictionary));
    }
    
    public void testCountChars() {
        VigenereBreaker vb = new VigenereBreaker();
        System.out.println(vb.mostCommonCharIn(vb.readDictionary()));
    }
}
