/*
 *
 */


/**
 *
 * @author Abobakr Bagais
 * e-mail: abobakr.a.2012@gmail.com
 */
import java.util.*;
public class form 
{
    String value;
        List<List<String>> word;
        public form(String value, String wordId, String type)
        {
            this.value = value;
            List<String> temp = new ArrayList<String>();
            temp.add(wordId);
            temp.add(type);
            this.word = new ArrayList<List<String>>();
            this.word.add(temp);
        }

        public void addWord(String wordId, String type)
        {
            List<String> temp = new ArrayList<String>();
            temp.add(wordId);
            temp.add(type);
            this.word.add(temp);
        }

        public String get_Value()
        {
           
                return this.value;
            
        }
        public List<List<String>> get_Word()
        {
           
            
                return this.word;
            
        }
}
