
import java.util.List;
import java.lang.String;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
 /*
 * @author Abobakr Bagais
 * e-mail: abobakr.a.2012@gmail.com
 */
public class awnMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
    	//String XMLfilepath = "C:\\Users\\Ayoub\\Desktop\\TFG_Ayoub\\wn-arb-lemon.xml";
    	String XMLfilepath = "C:\\Users\\Ayoub\\workspace\\AWN\\upc_db.xml";
    	AWN awn=new AWN (XMLfilepath,false);
    	System.out.println(awn.Get_Number_Of_items());
    	List<List<String>> ListWordID=awn.Get_List_Of_Words_From_Form_Value("شري");
    	System.out.println(ListWordID.toString());
    	List<List<String>> listOfForm=awn.Get_List_Of_Forms_From_Word_Id(ListWordID.get(1).get(0));
    	System.out.println(listOfForm.toString());
    	System.out.println(ListWordID.get(1).get(0));
    	
    	String SynsetID= awn.Get_Synset_ID_From_Word_Id(ListWordID.get(1).get(0));
    	System.out.println(SynsetID);
    	
    	String offset1 =awn. Get_Offset_Of_item_From_Item_Id(ListWordID.get(1).get(0));
    	String offset2 =awn. Get_Offset_Of_item_From_Item_Id(SynsetID);
    	System.out.println(offset1);
    	System.out.println(offset2);
    	
    }
}
    

