import java.util.ArrayList;

public class Freq_objects {
	
	public int no_of_Items;
	public int support;
	public int[] Freq_items;
    
    public Freq_objects(int no_of_Items, int[] Freq_items, int support) 
    {
    	this.no_of_Items =no_of_Items;
    	this.support = support;
    	this.Freq_items = Freq_items;   
    }
	 boolean dbContains(ArrayList<Freq_objects> db,PCY temp) 
	 {
		boolean result = false;
	      for (int i = 0; i < db.size(); i++) 
	      {
	        if (temp.has(db.get(i).Freq_items, this.Freq_items)) 
	        {
	          return true;
	        }
	      }
	      return result;
	}
}
