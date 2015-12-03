

/**
 *
 * @author Abobakr Bagais
 * e-mail: abobakr.a.2012@gmail.com
 */
import java.util.*;
public class Link
{
         List<List<String>> items;
        linkEnum link;
        public Link(linkEnum link, String itemId1, String itemId2)
        {
            items = new ArrayList<List<String>>();
            List<String> temp = new ArrayList<String>();
            temp.add(itemId1);
            temp.add(itemId2);
            items.add (temp);
            this.link = link;
            
        }
        public void  add_items(String item1, String item2)
        {
            List<String> temp = new ArrayList<String>();
            temp.add(item1);
            temp.add(item2);
            items.add (temp);
        }
        public List<List<String>> get_ITEMS()
        {
           
                return this.items;
            
        }
    
}
