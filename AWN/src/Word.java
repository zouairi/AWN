

/**
 *
 * @author Abobakr Bagais
 * e-mail: abobakr.a.2012@gmail.com
 */
import java.util.*;
public class Word
{
        String wordId, value;
        String synsetId;
        List<List<String>> formValue;   
        
  public Word(String wordId, String value, String synsetId)
        {
            this.wordId = wordId;
            this.value = value;
            this.synsetId = synsetId;
            this.formValue = new ArrayList<List<String>>();

        }
        public void addForm(String formValue, String formType)
        {
            List<String> li = new ArrayList<String>();
            li.add(formValue);
            li.add(formType);
            this.formValue.add(li);


        }

        public List<List<String>> get_FormValue()
        {
           
                return this.formValue;
           

        }
        public String get_WordId()
        {
           
                return this.wordId;
           
        }
        public String get_Value()
        {
            
                return this.value;
           
        }
        public String get_SynsitId()
        {
            
                return this.synsetId;
           

        }
      
}
