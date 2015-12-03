

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.lang.Math;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Set;
/**
 *
 * @author Abobakr Bagais
 * e-mail: abobakr.a.2012@gmail.com
 */
public class AWN
{
     private  Map<String, Item> itemDict;
        private Map<String, Word> wordDict;
        private Map<String, form> formDict;
        private Map<String, Link> linkDict;
        private Boolean diacritics;
        private Document doc;
        private JFrame frame;
        private List<DefaultMutableTreeNode> top;
        private List<Map<String,DefaultMutableTreeNode>> nodeDict;
        private int noTree;
        public int count=0;
        public  AWN(String XMLpath, Boolean dict)
        {
            File xmlFile = new File(XMLpath);
            if(xmlFile.exists())
            {
                try
                {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
                    doc = docBuilder.parse (xmlFile);
                    // normalize text representation
                    doc.getDocumentElement ().normalize();
                    diacritics= dict;
                    init();
                
                    

                }
                catch (SAXParseException err) 
                {
                    
                }
                catch (SAXException e)
                {
                   
                }
                catch (Throwable t)
                {
                     t.printStackTrace ();
                }
                finally
                {
                  
                }
            }
            else
            {
                frame = new JFrame("Form1");
                JOptionPane.showMessageDialog(frame ,"couldn't find the sourse XML file","error",JOptionPane.ERROR_MESSAGE);
            }
        }
        
      private void clear()
        {
            itemDict = new HashMap<String ,Item>();
            wordDict = new HashMap<String, Word>();
            formDict = new HashMap<String, form>();
            linkDict = new HashMap<String, Link>();
            //reader.Close();
        }
    
        
    private void init()
    {
        init_Item();
        init_Word();
        init_Form();
        init_Link();
        built_IsA_Tree();
    }
    
    @SuppressWarnings("unchecked")
    private void built_IsA_Tree()
    {
       //number of trees
       noTree=61;
       nodeDict = new ArrayList<>();
       top = new ArrayList<>();
       for(int i= 0;i<noTree;i++)
       {          
        nodeDict.add( new HashMap<String,DefaultMutableTreeNode >());
        DefaultMutableTreeNode temp  =  new DefaultMutableTreeNode(Tree.values()[i].toString());
        top.add(temp);
         nodeDict.get(i).put(Tree.values()[i].toString(), top.get(i));
        createChild(top.get(i),i);
       }
       
    }
     private void createChild(DefaultMutableTreeNode parent, int index)
     {
         DefaultMutableTreeNode child = null; 
         String itemID= (String) parent.getUserObject();
         List<List<String>> relationOfParent= Get_List_Of_Links_Out_For_Item(itemID);
         List<String> childHyponym= new ArrayList<String>();
         for (int i=0 ;i<relationOfParent.size();i++)
        {
           if(relationOfParent.get(i).get(1).equals("has_hyponym"))
           {
               //childHyponym.add(relationOfParent.get(i).get(0));
               String temp =relationOfParent.get(i).get(0);
               child =new DefaultMutableTreeNode(temp);
               nodeDict.get(index).put(temp, child);               
               parent.add(child);
               createChild(child,index);
           }
        }
        
    
     }
     
     
     public String Get_Shard_ancestor(String itemId1, String itemId2)
     {
         String sharedAnsestor ="";
         List<String> temp =new ArrayList<>();
         temp.add(itemId1);
         temp.add(itemId2);
         if(!are_Items_related(temp))
         {
             return sharedAnsestor="";
             
         }
         else
         {
             for(int i=0; i<nodeDict.size();i++)
             {
                if(nodeDict.get(i).containsKey(itemId1)== true)
                {
                  DefaultMutableTreeNode tempNode1= nodeDict.get(i).get(itemId1);
                  DefaultMutableTreeNode tempNode2= nodeDict.get(i).get(itemId2);
                  DefaultMutableTreeNode sharedNode = (DefaultMutableTreeNode)tempNode1.getSharedAncestor(tempNode2);
                  sharedAnsestor =(String) sharedNode.getUserObject();
                  
                  break;
                }
             }
         }
         return sharedAnsestor;
     }
     /// <summary>
        ///  check if all element in the list are belongs to one tree  
        /// </summary>
        /// <param name="name_Of_Item">List of ItemId</param>  
    public boolean are_Items_related(List<String> itemIds)
    {
        boolean related =false;
        
        for(int i =0;i<noTree;i++)
        {
            // return all the nodes in the tree i
            List<String> list = new ArrayList<String>(nodeDict.get(i).keySet());
            
            //check if the list pramater are subset of the set of the tree
            if(list.containsAll(itemIds))
            {
                related=true;
                break;
            }
           
        }
        return related ;
    }
    /// <summary>
        ///  check if paramter itemid are in parm specficTree
    public boolean Is_item_In_Tree(String itemId ,Tree specficTree)
    {
        
       boolean found=false;
       // return the full itemID
       String aTree= specficTree.toString();
      for(int i=0 ;i<noTree;i++ )
      {
          if(Tree.values()[i].toString().equals(aTree))
          {
              // check if the dictonary of the specficTree contains the itemId parm
              if(nodeDict.get(i).containsKey(itemId))
              {
                  found =true;
              }
              break;
          }
      }
      return found;
      
    }
    /// <summary>
        ///  return the root itemId for parm itemId
    public List<String> Get_Root_Item_ID(String itemId )
    {
        
       List<String> rootItemId= new ArrayList<>();
      for(int i=0 ;i<noTree;i++ )
      {
         
              if(nodeDict.get(i).containsKey(itemId))
              {
                  rootItemId.add(Tree.values()[i].toString());
                  
              }
             
      }
      return rootItemId;
      }
      
    
    
    
    
     public List<String> Get_All_Node_In_Tree(Tree specficTree)
    {
        
       
       List<String> nodes =  new ArrayList<>();
       String aTree= specficTree.toString();
       
      for(int i=0 ;i<noTree;i++ )
      {
          if(Tree.values()[i].toString().equals(aTree))
          {
             Set temp =  nodeDict.get(i).keySet();
             nodes = new ArrayList<>(temp);
             break;
          }
      }
      return nodes;
      
    }
    
         /// <summary>
        ///  return the full path of parm ItemId to the root to it's  tree if itemId parm dosen't belong to tree the list will have zero size 
        /// </summary>
        /// <param name="name_Of_Item">List of ItemId</param>  
    public List<List<String>> returnPathToRoot (String ItemId)
    {
        TreeNode[] temp;
        List<List<String>> tempString = new ArrayList<List<String>>();
        List<String> onePath = new ArrayList<>();
        for(int i=0; i<nodeDict.size();i++)
        {
            
        if(nodeDict.get(i).containsKey(ItemId)== true)
        {
            DefaultMutableTreeNode tempNode= nodeDict.get(i).get(ItemId);
            temp = tempNode.getPath();
            for(int j =temp.length-1 ;j>=0;j--)
            {   
                onePath.add(temp[j].toString());
            }
            tempString.add(onePath);
            onePath = new ArrayList<>();
        }
        }
        
        
        return tempString;
    }
    
   public int Get_word_similirty_edge_counting(String itemId1 ,String itemId2)
    {
        
        int globalDepth = 0;
        int distanceBetweenTwoNodes=0;
        int similarity=0;
        boolean found= false;
        for(int i =0 ;i<top.size();i++)
        {
        
        if(nodeDict.get(i).containsKey(itemId1)== true && nodeDict.get(i).containsKey(itemId2)==true)
        {
           
           found =true;
          globalDepth = top.get(i).getDepth();  
          DefaultMutableTreeNode tempNode1= nodeDict.get(i).get(itemId1);
        DefaultMutableTreeNode tempNode2= nodeDict.get(i).get(itemId2);
        if(tempNode1.isNodeAncestor(tempNode2)||tempNode2.isNodeAncestor(tempNode1))
        {
             distanceBetweenTwoNodes = Math.abs((tempNode1.getLevel())-(tempNode2.getLevel()));
             
        }
        else
        {
             DefaultMutableTreeNode sharedAncestor =(DefaultMutableTreeNode) tempNode1.getSharedAncestor(tempNode2);
             int dis1 = (tempNode1.getLevel()) -(sharedAncestor.getLevel());
             int dis2=  (tempNode2.getLevel()) -(sharedAncestor.getLevel()); 
             distanceBetweenTwoNodes= dis1+dis2;
             
        }
        break;
        }
        }
        if (found == false)
        {
            return -1;
        }
        
        similarity= (2*globalDepth) - distanceBetweenTwoNodes;
        return similarity;
        
        
          
    }
    
      public double Get_word_similirty_wuP(String itemId1 ,String itemId2)
    {
        
               double similarity=0.0;
               int   numerator =0;
               int  denominator=0;
               boolean found= false;
        for(int i =0 ;i<top.size();i++)
        {
        if(nodeDict.get(i).containsKey(itemId1)== true &&nodeDict.get(i).containsKey(itemId2)==true)
        {
           DefaultMutableTreeNode tempNode1= nodeDict.get(i).get(itemId1);
        DefaultMutableTreeNode tempNode2= nodeDict.get(i).get(itemId2);
        if(tempNode1.isNodeAncestor(tempNode2))
        {
             
             numerator = 2* (tempNode2.getLevel()+1);
             denominator = ((tempNode1.getLevel()+1)- (tempNode2.getLevel()+1) + (2* (tempNode2.getLevel()+1)));
        }
        else if (tempNode2.isNodeAncestor(tempNode1))
        {
             numerator = 2* (tempNode1.getLevel()+1);
             denominator = ((tempNode2.getLevel()+1)- (tempNode1.getLevel()+1)) + (2*(tempNode1.getLevel()+1));
             
        }
        else
        {
            DefaultMutableTreeNode sharedNode= (DefaultMutableTreeNode) tempNode1.getSharedAncestor(tempNode2);
            numerator = 2* (sharedNode.getLevel()+1);
            denominator = ((tempNode1.getLevel()+1)- (sharedNode.getLevel()+1)+ (tempNode2.getLevel()+1)- (sharedNode.getLevel()+1)) + (2* (sharedNode.getLevel()+1));
        }
        found=true;
        break;
        }
       
       
        }
        if( found ==false)
        {
            return -1.0;
        }
        
        similarity= (double) numerator/denominator;
        return similarity;
        
        
          
    }
      public double Get_word_similirty_LeakcockChodorow(String itemId1 ,String itemId2)
    {
        
               double similarity=0.0;
               int   numerator =0;
               int  denominator=0;
               int globalDepth = 0;
               boolean found =false;
        for(int i=0 ;i<top.size();i++)
        {
        if(nodeDict.get(i).containsKey(itemId1)== true && nodeDict.get(i).containsKey(itemId2)==true)
        {
            found =true;
          globalDepth = top.get(i).getDepth();
          DefaultMutableTreeNode tempNode1= nodeDict.get(i).get(itemId1);
          DefaultMutableTreeNode tempNode2= nodeDict.get(i).get(itemId2);
        denominator = 2 * globalDepth ;
        if(tempNode1.isNodeAncestor(tempNode2)||tempNode2.isNodeAncestor(tempNode1))
        {
             numerator = Math.abs((tempNode1.getLevel()+1)-(tempNode2.getLevel()+1));
             
        }
        else
        {
             DefaultMutableTreeNode sharedAncestor =(DefaultMutableTreeNode) tempNode1.getSharedAncestor(tempNode2);
             int dis1 = (tempNode1.getLevel()+1) -(sharedAncestor.getLevel()+1);
             int dis2=  (tempNode2.getLevel()+1) -(sharedAncestor.getLevel()+1); 
             numerator= dis1+dis2;
              
        }
        break;
        }     
        
        }
        if(found ==false)
        {
           return -1.0;
        }
        similarity =(double) numerator/denominator;
        similarity = Math.log10(similarity);
        similarity= similarity *-1.0;
        return similarity;
          
    }
     
        public double Get_word_similirty_Li (String itemId1 ,String itemId2 ,double alpha,double beta)
    {
        
               double similarity=0.0;
               double   numerator =0;
               double  denominator=0;
               int distanceBetweenTwoNodes=0;
               int DepthLso=0;
               int globalDepth=0;
               boolean found =false;
        for(int i=0 ;i<top.size();i++)
        {
        if(nodeDict.get(i).containsKey(itemId1)== true ||nodeDict.get(i).containsKey(itemId2)==true)
        {
          found= true;
         globalDepth = top.get(i).getDepth();
        DefaultMutableTreeNode tempNode1= nodeDict.get(i).get(itemId1);
        DefaultMutableTreeNode tempNode2= nodeDict.get(i).get(itemId2);
        denominator = 2 * globalDepth ;
        if(tempNode1.isNodeAncestor(tempNode2)||tempNode2.isNodeAncestor(tempNode1))
        {
             distanceBetweenTwoNodes = Math.abs((tempNode1.getLevel()+1)-(tempNode2.getLevel()+1));
             DepthLso = Math.min(tempNode1.getLevel()+1,tempNode2.getLevel()+1);
        }
        else
        {
             DefaultMutableTreeNode sharedAncestor =(DefaultMutableTreeNode) tempNode1.getSharedAncestor(tempNode2);
             int dis1 = (tempNode1.getLevel()+1) -(sharedAncestor.getLevel()+1);
             int dis2=  (tempNode2.getLevel()+1) -(sharedAncestor.getLevel()+1); 
             distanceBetweenTwoNodes= dis1+dis2;
             DepthLso = sharedAncestor.getLevel()+1;
              
        }
        break;
        }
        }
        if(found ==false)
        {
          return -1.0;
        }
        double expo = Math.exp(beta*DepthLso);
        double minexpo = Math.exp(-1.0*(beta*DepthLso));
        numerator = expo - minexpo;
        denominator = expo + minexpo;
        similarity =(double) numerator/denominator;
        double leftSide= Math.exp(-1.0*(alpha*distanceBetweenTwoNodes));
        
        similarity = leftSide * similarity;
        return similarity;
          
    }
      
      
    
    
    
    private void init_Item()
    {
        
          NodeList listOfItem = doc.getElementsByTagName("item");
          int totalItem;
          totalItem = listOfItem.getLength();
          itemDict = new HashMap<String, Item>();
          for(int i=0; i< totalItem ; i++)
          {
            Node itemNode = listOfItem.item(i);
            if(itemNode.getNodeType() == Node.ELEMENT_NODE)
            {

               Element itemElement = (Element)itemNode;
               String tempItemID = itemElement.getAttribute("itemid");
               String tempName = itemElement.getAttribute("name");
               if(diacritics== false)
               {
               tempName =removeDict(tempName);
               }
               String tempPOS = itemElement.getAttribute("POS");
               String tempOffset = itemElement.getAttribute("offset");
               String tempType = itemElement.getAttribute("type");
               pos tempPos2 = pos.a;
                        if (tempPOS.equals("n"))
                        {
                            tempPos2 = pos.n;
                        }
                        else if (tempPOS.equals("a"))
                        {
                            tempPos2 = pos.a;
                        }
                        else if (tempPOS.equals("v"))
                        {
                            tempPos2 = pos.v;
                        }
                        else if (tempPOS.equals("r"))
                        {
                            tempPos2 = pos.r;
                        }
                        else if (tempPOS.equals("s"))
                        {
                            tempPos2 = pos.s;
                        }
                 if (tempItemID.equals( null))
                       {
                           JOptionPane.showMessageDialog(frame ,"error on extracting itemid from XML file \n check the XML file \n can't complete the operation ","error",JOptionPane.ERROR_MESSAGE);
                           clear();
                           return;
                        }        
                       
               Item tempItem = new Item(tempItemID ,tempName ,tempPos2 ,tempOffset ,tempType);
               itemDict.put(tempItemID, tempItem);
               
              

            }


        }

    }
    
     private void init_Word()
    {
        NodeList listOfWord = doc.getElementsByTagName("word");
          int totalWord;
          totalWord = listOfWord.getLength();
          wordDict = new HashMap<String, Word>();
          for(int i=0; i<totalWord ; i++)
          {
            Node wordNode = listOfWord.item(i);
            if(wordNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element itemElement = (Element)wordNode;
                 String wordId = itemElement.getAttribute("wordid");
                        String value = itemElement.getAttribute("value");
                        if(diacritics == false)
                        {
                        value =removeDict(value);
                        }
                        String synsetid = itemElement.getAttribute("synsetid");
                        if (wordId.equals(null))
                        {
                            JOptionPane.showMessageDialog(frame ,"error on extracting wordid from XML file \n check the XML file \n can't complete the operation ","error",JOptionPane.ERROR_MESSAGE);
                            clear();
                            return;
                        }
                        if (synsetid.equals(null))
                        {
                            JOptionPane.showMessageDialog(frame ,"error on extracting synsetid from XML file \n check the XML file \n can't complete the operation  ","error",JOptionPane.ERROR_MESSAGE);
                            clear();
                            return;
                        }

                        if (itemDict.containsKey(synsetid) == false)
                        {
                            JOptionPane.showMessageDialog(frame ,"\"one of  synsetid is  not valid \\n check the XML file \\n can't complete the operation ","error",JOptionPane.ERROR_MESSAGE);
                            clear();
                            return;
                        }
                        else
                        {
                            Word wordClasee = new Word(wordId, value, synsetid);
                            wordDict.put(wordId, wordClasee);
                        }
                
               
             
              

            }


        }

    
    }
      private void init_Form()
    {
        NodeList listOfForm = doc.getElementsByTagName("form");
          int totalForm;
          totalForm = listOfForm.getLength();
          formDict = new HashMap<String, form>();
          for(int i=0; i<totalForm ; i++){


            Node formNode = listOfForm.item(i);
            if(formNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element formElement = (Element)formNode;
                         String formValue = formElement.getAttribute("value");
                         if(diacritics == false)
                         {
                         formValue= removeDict(formValue);
                         }
                        String wordId = formElement.getAttribute("wordid");
                        String type = formElement.getAttribute("type");
                        if (formValue == null)
                        {
                             JOptionPane.showMessageDialog(frame ,"error on extracting value of a form  from XML file \n can't complete the operation ","error",JOptionPane.ERROR_MESSAGE);
                             clear();
                            return;
                        }
                        if (wordId == null)
                        {
                            JOptionPane.showMessageDialog(frame ,"error on extracting wordId from XML file \\n can't complete the operation","error",JOptionPane.ERROR_MESSAGE);
                           
                            clear();
                            return;
                        }
                        if (wordDict.containsKey(wordId) == false)
                        {
                            JOptionPane.showMessageDialog(frame ,"error  on wordId on form   \n can't complete the operation ","error",JOptionPane.ERROR_MESSAGE);
                             
                            
                            clear();
                            return;
                        }
                        Word tempWord = wordDict.get(wordId);
                        tempWord.addForm(formValue, type);

                        if (formDict.containsKey(formValue))
                        {
                            form temp = formDict.get(formValue);
                            temp.addWord(wordId, type);
                        }
                        else
                        {
                            form temp = new form(formValue, wordId, type);
                            formDict.put(formValue, temp);
                        }

                        
            }


        }

    
    }
    
      private void init_Link()
     {
          NodeList listOfLink = doc.getElementsByTagName("link");
          int totalLink;
          totalLink = listOfLink.getLength();
          linkDict = new HashMap<String, Link>();
          for(int i=0; i<totalLink ; i++)
          {


            Node linkNode = listOfLink.item(i);
            if(linkNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element linkElement = (Element)linkNode;                 
                  String itemId1 = linkElement.getAttribute("link1");
                        String itemId2 = linkElement.getAttribute("link2");
                        String type = linkElement.getAttribute("type");
                        if (itemId1 == null || itemId2 == null)
                        {
                            JOptionPane.showMessageDialog(frame ,"error on extracting items from link node \n \n check the XML file \n can't complete the operation ","error",JOptionPane.ERROR_MESSAGE);
                            clear();
                            return;
                        }
                        if (itemDict.containsKey(itemId1) && itemDict.containsKey(itemId2))
                        {
                            Item temp = itemDict.get(itemId1);
                            temp.Add_Link_Out(itemId2, type);
                            temp = itemDict.get(itemId2);
                            temp.Add_Link_In(itemId1, type);
                            linkEnum templinkEnum = returnLinkEnum(type);
                            if (linkDict.containsKey(type))
                            {
                                Link tempLink = linkDict.get(type);
                                tempLink.add_items(itemId1, itemId2);

                            }
                            else
                            {
                                Link tempLink = new Link(templinkEnum, itemId1, itemId2);
                                linkDict.put(type, tempLink);
                            }
                            
                            
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(frame ,"error on link1 or link2  of a link node in  XML file \n can't complete the operation ","error",JOptionPane.ERROR_MESSAGE);
                            clear();
                            return;

                        }
            }
          }
     }
      
      /// <summary>
        /// return the list of a list of two items that share  the specific link-type provided by the user 
        /// </summary>
        /// <param name="linkType">The specific link type</param>    
    public List<List<String>> Get_The_Spesfic_Link_Type(linkEnum linkType)
        {
            List<List<String>> temp = new ArrayList<List<String>>();
            if(linkDict.containsKey(String.valueOf(linkType)))
            {
                Link tempLink = linkDict.get(String.valueOf(linkType));
                temp = tempLink.get_ITEMS();
            }
            return temp;
        }
    
    private linkEnum returnLinkEnum(String linkType)
        {
            linkEnum temp = linkEnum.causes;
            if (linkType.equals("usage_term"))
            {
                temp = linkEnum.usage_term;
            }
            else if (linkType.equals("has_holo_madeof"))
            {
                temp = linkEnum.has_holo_madeof;
            }
            else if (linkType.equals("region_term"))
            {
                temp = linkEnum.region_term;
            }
            else if (linkType.equals("has_instance"))
            {
                temp = linkEnum.has_instance;
            }
            else if (linkType.equals("has_derived"))
            {
                temp = linkEnum.has_derived;
            }
            else if (linkType.equals("has_hyponym"))
            {
                temp = linkEnum.has_hyponym;
            }
            else if (linkType.equals("verb_group"))
            {
                temp = linkEnum.verb_group;
            }
            else if (linkType.equals("has_holo_part"))
            {
                temp = linkEnum.has_holo_part;
            }
            else if (linkType.equals("has_subevent"))
            {
                temp = linkEnum.has_subevent;
            }
            else if (linkType.equals("near_antonym"))
            {
                temp = linkEnum.near_antonym;
            }
            else if (linkType.equals("related_to"))
            {
                temp = linkEnum.related_to;
            }
            else if (linkType.equals("see_also_wn15"))
            {
                temp = linkEnum.see_also_wn15;
            }
            else if (linkType.equals("category_term"))
            {
                temp = linkEnum.category_term;
            }
            else if (linkType.equals("near_synonym"))
            {
                temp = linkEnum.near_synonym;
            }
            else if (linkType.equals("causes"))
            {
                temp = linkEnum.causes;
            }
            else if (linkType.equals("has_holo_member"))
            {
                temp = linkEnum.has_holo_member;
            }
            else if (linkType.equals("be_in_state"))
            {
                temp = linkEnum.be_in_state;
            }
            return temp;
        }
    
        /// <summary>
        /// return a list of ItemId   by providing the name of Item attribute 
        /// </summary>
        /// <param name="name_Of_Item">The name attribute of Item Class</param>             
        public List<String> Get_Item_Id_From_Name(String name_Of_Item)
        { 
            if(diacritics== false)
            {
                name_Of_Item= removeDict(name_Of_Item);
            }
             List<String> tempList= new ArrayList<String>();
             for(String key : itemDict.keySet())
            {
                Item tempItem = itemDict.get(key);
               
                if (tempItem.get_Name().equals(name_Of_Item))
                {
                    tempList.add(tempItem.get_ItemId());

                }
            }
           // JOptionPane.showMessageDialog(frame ,"There aren't Item  in xml file has a name   =  \" " + name_Of_Item + "\" in Xml file ","error",JOptionPane.ERROR_MESSAGE);  
           return tempList;
        }
       
        /// <summary>
        /// return ItemId attribute  by providing offset of Item  attribute 
        /// </summary>
        /// <param name="offest_Of_Item">The offset attribute of Item Class</param>
     public String Get_Item_Id_From_Offset(String Offset_Of_Item)
        {
            
            for (String key : itemDict.keySet())
            {
                Item tempItem = itemDict.get(key);
                if (tempItem.get_Offset().equals(Offset_Of_Item))
                {
                    return tempItem.get_ItemId();
                }
            }
           // JOptionPane.showMessageDialog(frame ,"There aren't Item  in xml file has a Offset   =  \\\" \" + Offset_Of_Item + \"\\\" in Xml file ","error",JOptionPane.ERROR_MESSAGE);  
          return "";
        }
        /// <summary>
        /// return a list of  ItemId attribute  of a specfic  POS
        /// </summary>
        /// <param name="POS">The POS attribute of Item Class</param>
         public List<String> Get_List_Of_Item_Id_From_POS(pos pos_Of_Item)
         {

             List<String> tempOfItemId = new ArrayList<String>();
             for (String key : itemDict.keySet())
             {
                 pos  currentPos = pos_Of_Item;

                 Item tempItem = itemDict.get(key);
                 if (tempItem.get_POS().equals(pos_Of_Item))
                 {
                     tempOfItemId.add(tempItem.get_ItemId());
                 }
             }

             if (tempOfItemId.size()==0)
             {
                 //System.Windows.Forms.MessageBox.Show("there aren't Item  in xml file has a POS  =  \" " + pos_Of_Item + "\" in Xml file", "error", MessageBoxButtons.OK, MessageBoxIcon.Error);
             }
             return tempOfItemId;
         }
        /// <summary>
        /// return a list of relation between two items where is ItemId_1i n left side and itemId_2 in the right side 
        /// </summary>
        /// <param name="itemId_1">The first ItemId </param>
        ///  /// <param name="itemId_2">The second ItemID</param>
        public  List<String> Get_List_Of_Links_Between_Two_Items(String itemId_1, String itemId_2)
        {
            List<String> relation = new ArrayList<String>();
            if (itemDict.containsKey(itemId_1))
            {
                Item temp = itemDict.get(itemId_1);
                
                relation = temp.returnLink_Out(itemId_2);
            }
            return relation;
        }

        /// <summary>
        /// return a list of of Link for Item Id where itemId in the right side of the relation
        /// </summary>
        /// <param name="itemId"> Item Id  </param>
      
        public List<List<String>> Get_List_Of_Links_In_For_Item(String itemId)
        {
            List<List<String>> relation = new ArrayList<List<String>>();
            if (itemDict.containsKey(itemId))
            {
                Item temp = itemDict.get(itemId);
                relation = temp.get_LINKIN();
            }

            return relation;
        }
         
            
        
        /// <summary>
        /// return a list of of Link for Item Id where itemId in the left side of the relation
        /// </summary>
        /// <param name="itemId"> Item Id  </param>
      
        public List<List<String>> Get_List_Of_Links_Out_For_Item(String itemId)
        {
            List<List<String>> relation = new ArrayList<List<String>>();
            if (itemDict.containsKey(itemId))
            {
                Item temp = itemDict.get(itemId);
                relation = temp.get_LINKOUT();
            }
            
            return relation;
        }
        
        /// <summary>
        /// return a list of of Link for Item Id where itemId in the left side of the specific relation 
        /// </summary>
        /// <param name="itemId"> Item Id  </param>
      
        public List<String> Get_List_Of_Links_Out_For_Item_With_Specfic_Relation(String itemId,linkEnum link  )
        {
            List<List<String>> relation = new ArrayList<List<String>>();
            List<String> tempList= new ArrayList<String>();
            
            if (itemDict.containsKey(itemId))
            {
                Item temp = itemDict.get(itemId);
                relation = temp.get_LINKOUT();
                for(int i=0;i<relation.size();i++)
                {
                    if(relation.get(i).get(1).equals(link.toString()))
                    {
                        tempList.add(relation.get(i).get(0));
                    }
                }
            }
            
            return tempList;
        }
        /// <summary>
        /// return a list of of Link for Item Id where itemId in the right side of the specific relation 
        /// </summary>
        /// <param name="itemId"> Item Id  </param>
         public List<String> Get_List_Of_Links_In_For_Item_With_Specfic_Relation(String itemId,linkEnum link  )
        {
            List<List<String>> relation = new ArrayList<List<String>>();
            List<String> tempList= new ArrayList<String>();
            
            if (itemDict.containsKey(itemId))
            {
                Item temp = itemDict.get(itemId);
                relation = temp.get_LINKIN();
                for(int i=0;i<relation.size();i++)
                {
                    if(relation.get(i).get(1).equals(link.toString()))
                    {
                        tempList.add(relation.get(i).get(0));
                    }
                }
            }
            
            return tempList;
        }
        
        /// <summary>
         /// return a  name attribute from specific item id
        /// </summary>
        /// <param name="itemId"> ItemId  </param>
        /// 
        public String Get_Name_Of_Item_From_Item_Id(String itemId)
        {
            if (itemDict.containsKey(itemId))
            {
                Item tempItem = itemDict.get(itemId);
                String tempName= tempItem.get_Name();
               
                return tempName;
            }
            else
            {
               //System.Windows.Forms.MessageBox.Show("there aren't ItemId =  \" " + itemId + "\" in Xml file", "error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return "";
            }
        }
        /// <summary>
        /// return  a POS attribute for a specfic item 
        /// </summary>
        /// <param name="itemId">ItemId </param>
        public String Get_Pos_Of_Item_From_Item_Id(String itemId)
        {
            
            if (itemDict.containsKey(itemId))
            {
                Item tempItem = itemDict.get(itemId);

                return String.valueOf(tempItem.get_POS());
            }
            else
            {
               // System.Windows.Forms.MessageBox.Show("there aren't ItemId =  \" " + itemId + "\" in Xml file", "error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return "";
            }
        }
        /// <summary>
        /// return a  type of Item Id
        /// </summary>
        /// <param name="itemId"> ItemId </param>        
        public String Get_Type_Of_Item_From_Item_Id(String itemId)
        {
            if (itemDict.containsKey(itemId))
            {
                Item tempItem = itemDict.get(itemId);
                return tempItem.get_Type();
            }
            else
            {
              // System.Windows.Forms.MessageBox.Show("there aren't ItemId =  \" " + itemId + "\" in Xml file", "error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return "";
            }
        }
        
        /// <summary>
        /// return  an Offset of a spesfic item id
        /// </summary>
        /// <param name="itemId"> ItemId </param>
        public String Get_Offset_Of_item_From_Item_Id(String itemId)
        {
            if (itemDict.containsKey(itemId))
            {
                Item tempItem = itemDict.get(itemId);
                return tempItem.get_Offset();
            }
            else
            {
              // System.Windows.Forms.MessageBox.Show("there aren't ItemId =  \" " + itemId + "\" in Xml file", "error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return "";
            }
        }

        /// <summary>
        /// return  a number of items
        /// </summary>     
        public  int Get_Number_Of_items()
        {
            return itemDict.size();
        }
        /// <summary>
        /// return  a number of words
        /// </summary>
      public  int Get_Number_Of_Words()
        {
            return wordDict.size();
        }
        public  int Get_Number_Of_Form()
        {
            return formDict.size();
        }
        
        /// <summary>
        /// return  a list of Word ID from a specfic word-value
        /// </summary> 
        /// <param name="itemId"> value_Of_Word </param>
        public List<String> Get_List_Word_Id_From_Value(String value_Of_Word)
        {
            List<String> wordIdList = new ArrayList<String>();
             if(diacritics== false)
            {
                value_Of_Word= removeDict(value_Of_Word);
            }
            for (String key : wordDict.keySet())
            {
                Word temp = wordDict.get(key);
                if (temp.get_Value().equals(value_Of_Word))
                {
                    wordIdList.add(temp.get_WordId());
                }
            }
            return wordIdList;
        }
        /// <summary>
        /// return  a list of Word ID from a specfic SynsetId Of  Word
        /// </summary> 
        /// <param name="itemId"> SynsetId Of Word </param>
        public List<String> Get_List_Word_Id_From_Synset_ID(String SynsetId_Of_Word)
        {
            List<String> wordIdList = new ArrayList<String>();
            for (String key : wordDict.keySet())
            {
                Word temp = wordDict.get(key);
                if (temp.get_SynsitId().equals(SynsetId_Of_Word))
                {
                    wordIdList.add(temp.get_WordId());
                }
            }
            return wordIdList;
        }
        
         /// <summary>
        /// return  a Synset ID from  a specfic   Word ID
        /// </summary> 
        /// <param name="itemId"> Word ID </param>
        public String Get_Synset_ID_From_Word_Id(String wordId)
        {
            if (wordDict.containsKey(wordId))
            {
                Word temp = wordDict.get(wordId);
                String synsetId = temp.get_SynsitId();
                return synsetId;
            }
            else
            {
                return "";
            }
        }
         /// <summary>
        /// return a Word Value from  a specific  Word ID
        /// </summary> 
        /// <param name="itemId"> Word ID </param>
        public String Get_Word_Value_From_Word_Id(String wordId)
        {
            if (wordDict.containsKey(wordId))
            {
                Word temp = wordDict.get(wordId);
                String Value = temp.get_Value();
                 
                
                 
                return Value;
            }
            else
            {
                return "";
            }
        }
        
         /// <summary>
        /// return a list of form from provided wordID for each element in the list  has two entry 1-formValue and 2-formType
        /// </summary> 
        /// <param name="itemId"> Word ID </param>
        public List<List<String>>Get_List_Of_Forms_From_Word_Id(String wordId)
        {
            List<List<String>> tempForms = new ArrayList<List<String>>();
            if (wordDict.containsKey(wordId))
            {
                Word temp = wordDict.get(wordId);
                tempForms = temp.get_FormValue();
            }
            return tempForms;


        }
        
        /// <summary>
        /// return a list of word from provided form_Value ,for each element in the list  has two entry 1-wordId and 2-wordType
        /// </summary> 
        /// <param name="itemId"> form_Value </param>
        public List<List<String>> Get_List_Of_Words_From_Form_Value(String form_Value)
        {
             if(diacritics== false)
            {
                form_Value= removeDict(form_Value);
            }
            List<List<String>> ListOfWord = new ArrayList<List<String>>();
            if (formDict.containsKey(form_Value))
            {
                form temp = formDict.get(form_Value);
                ListOfWord = temp.get_Word();
            }
            return ListOfWord;
        }

        
        public List<String> Get_All_Item_Id()
        {   
            List<String> item =new ArrayList<String>();
             for (String key : itemDict.keySet())
            {
                item.add(key);
            }
            
            return item;
        }
        public List<String> Get_All_Word_Id()
        {
            List<String> word = new ArrayList<String>();
                   for (String key : wordDict.keySet())
            {
                word.add(key);
            }
            return word;
        }
        public List<String> Get_All_Form_Value()
        {
            List<String> form =  new ArrayList<String>();
               for (String key : formDict.keySet())
            {
                form.add(key);
            }
            
            return form;
        }
        private String removeDict(String dictWord)
        {
           dictWord= dictWord.replace("َ",""); //fatah
           dictWord= dictWord.replace("ً","");// 2 fatah
           dictWord= dictWord.replace("ُ","");//Tama 
           dictWord= dictWord.replace("ٌ","");//2 Tama
           dictWord= dictWord.replace("ِ","");//Kasra
           dictWord= dictWord.replace("ٍ","");//2kasra
           dictWord= dictWord.replace("ْ","");//skoon
           dictWord= dictWord.replace("ّ","");//Shada
           return dictWord;
                  
        }
    
}
