


import java.util.*;
/**
 *
 * @author Abobakr Bagais
 * e-mail: abobakr.a.2012@gmail.com
 */
public class Item {
    // the Bicycle class has
    // three fields
    private String itemId;
    private String name;
    private pos POS;
    private  String offset;
    private String type;
    private List<List<String>> linkIn;
    private List<List<String>> linkOut;
   
    // the Bicycle class has
    // one constructor
    public Item(String itemId ,String name , pos POS, String offset ,String type)
    {
        this.itemId = itemId;
        this.name= name;
        this.POS = POS;
        this.offset = offset;
        this.type= type;
        this.linkIn = new ArrayList<List<String>>();
        this.linkOut = new ArrayList<List<String>>();
        
        
    }
    
     public void Add_Link_In(String itemID, String type)
        {

            List<String> temp = new ArrayList<String>();
            temp.add(itemID);
            temp.add(type);
            this.linkIn.add(temp);
        }
        public void Add_Link_Out(String itemID, String type)
        {
            List<String> temp = new ArrayList<String>();
            temp.add(itemID);
            temp.add(type);
            this.linkOut.add(temp);
        }
        public List<String> returnLink_In(String item2)
        {
            List<String> relation = new ArrayList<String>();
            for (int i = 0; i < linkIn.size(); i++)
            {
                if (linkIn.get(i).contains(item2))
                {
                    relation.add(linkIn.get(i).get(1));

                }
            }
            return relation;
        }
        
        public List<String> returnLink_Out(String item2)
        {
            List<String> relation = new ArrayList<String>();
            for (int i = 0; i < linkOut.size() ; i++)
            {
                if (linkOut.get(i).contains(item2))
                {
                    relation.add(linkOut.get(i).get(1));

                }
            }
            return relation;
        }
    
        public String get_ItemId()
        {
           return this.itemId;
        }
        public String get_Name()
        {  
          return this.name;
        }           
        public String get_Type()
        {
            
         return this.type;
            
        }
        public pos get_POS()
        {
           
           return this.POS;
            
        }
        public String get_Offset()
        {
           
          return this.offset;
            
        }
        public List<List<String>> get_LINKIN()
        {
           
          return this.linkIn;
            
        }
        public List<List<String>> get_LINKOUT()
        {
            
         return this.linkOut;
            
        }
   
   
        
    
}
