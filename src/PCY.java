import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
class PCY{
	
	  ArrayList<int[]> baskets;
	  static HashMap<Integer,Integer> buckets;
	  HashMap<Integer,Boolean> bitmap;
	  ArrayList<Freq_objects> items_list;
	  int msupall;
	  static int modValue;
	  public PCY(int mod) throws IOException
	  {
		  	 modValue=mod;
		  //ystem.out.println(modValue);
		    baskets= new ArrayList<int[]>();
		    buckets=new HashMap<Integer,Integer>();
		    bitmap=new HashMap<Integer,Boolean>();
		    FileReader fr=new FileReader("C:\\Users\\syeda\\Documents\\final adb proj\\Freq_Item_Mining\\src\\input.txt");	
		    BufferedReader br=new BufferedReader(fr);  
			String s[]=br.readLine().split(" ");
			int Baskets=Integer.parseInt(s[0]);
			msupall=Integer.parseInt(s[1]);
			for(int i=0;i<Baskets;i++)
			{
				String ss[]=br.readLine().split(",");
				 int[] item=new int[ss.length-1];
				for(int ii=1;ii<ss.length;ii++)
				{
					 item[ii-1]=Integer.parseInt(ss[ii]);
				}
				 baskets.add(item);
			}
	  }
	  
	  public void createPatterns(int[] A, String out, int i, int n, int k)
	  {
	      if (k > n)
	      {
	          return;
	      }
	      if (k == 0) 
	      {
	    	  
	    	String arr[]=out.split(" ");
	    	int intarr[]=new int[arr.length];
	    	for(int p=0;p<arr.length;p++)
	    	intarr[p]=Integer.parseInt(arr[p]);
	    	int Hashvalue=Modfunc(intarr,modValue);
	  		if(buckets.containsKey(Hashvalue))
	  		{
	  			int exnum=buckets.get(Hashvalue);
	  			exnum++;
	  			buckets.put(Hashvalue, exnum);			
	  		}
	  		else
	  		{
	  			buckets.put(Hashvalue,1);
	  		}
	          return;
	        }
	      for (int j = i; j < n; j++)
	      {
	    	if(out.equals(""))
	    	  {
	          createPatterns(A, out + (A[j]) , j + 1, n, k - 1);
	    	  }
	    	else
	    	  {
	    		  createPatterns(A, out+" " + (A[j]) , j + 1, n, k - 1);
	    	  }
	        while (j < n - 1 && A[j] == A[j + 1]) {
	               j++;
	          }
	      }
	  }
	  static int Modfunc(int[] arr,int modvalue)
	  {
		  int sum=0;
			for(int k=0;k<arr.length;k++)
			{	
				sum=sum+arr[k];
			}
		//ystem.out.println(modValue);
			int Hashvalue=sum % modValue;
		  return Hashvalue;
	  }
	  void CreateBucket(ArrayList<int[]> mtrans,int num)
	  {
		
		for(int[] q: mtrans)
		{
			Arrays.sort(q);
			createPatterns(q, "", 0, q.length, num);	
		}
		
		for(int bucket :buckets.keySet())
		{
			int count=buckets.get(bucket);
			if(count>= msupall)
			{
				bitmap.put(bucket, true);
			}
			else
			{
				bitmap.put(bucket, false);
			}
		}
	  }
	  private int[] Get_next_comibination(Freq_objects first, Freq_objects second) 
	   {
		  int[] result;
		  result = new int[first.Freq_items.length + 1];
		    //if(first.Freq_items.length==4)
		    //System.out.println("in pick");
		    int i=0,j=0,k=0;
		    while((i < first.Freq_items.length) || (j < second.Freq_items.length)) 
		    {
		      if (first.Freq_items.length == i) 
		      {
		        result[k++] = second.Freq_items[j];
		        j++;
		      }
		      else if (second.Freq_items.length == j) 
		      {
		        result[k++] = first.Freq_items[i];
		        i++;
		      }
		      else if (first.Freq_items[i] < second.Freq_items[j])
		      {
		        result[k++] = first.Freq_items[i];
		        i++;
		      }
		      else if (first.Freq_items[i] > second.Freq_items[j])
		      {
		       
		        result[k++] = second.Freq_items[j];
		        j++;
		      }
		      else 
		      {
		        result[k++] = first.Freq_items[i];
		        i++;
		        j++;
		      }
		    }
		    return result;
		    
	  }
	  public void pcyDriverMethod() throws IOException 
	  {
		   items_list=new ArrayList<Freq_objects>();
		   ArrayList<Integer> items = new ArrayList<Integer>();
		   for (int i[] :baskets)
		   {
			  for(int j:i)
			  {
		        Integer data = new Integer(j);
		        if (!items.contains(data)) 
		        {
		          items.add(data); 
		        }
			  }
		   }
		   for (int each_item :items) 
		   {
			      int temp[]= {each_item};
			      items_list.add(new Freq_objects(1, temp, Find_Total_Occurences(baskets, temp)));
		   }   
		     while(true) 
		     {
		     ArrayList<Freq_objects> Frequent_sets= List_After_Min_Support(items_list);
		     writetofile(Frequent_sets);
		     if(Frequent_sets.isEmpty())
		     {
		    	 
		    	 System.out.println("in isempty");
		    	 break;
		     }
		     if(!Frequent_sets.isEmpty())
		     {
		    	 if(((Frequent_sets.get(0).no_of_Items)+1)<3)
		    	 {
		             CreateBucket(baskets,((Frequent_sets.get(0).no_of_Items)+1));
                 }
		     }
		     //System.out.println((Frequent_sets.get(0).no_of_Items)+1);
		     ArrayList<Freq_objects> Combinations = Genarate_Combinations(Frequent_sets,Frequent_sets);
		     
		     if (Combinations.size() <= 1)
		     {
		    	 //writetofile(Frequent_sets);
		    	 System.out.println("done");
		        break;
		      }
		     items_list = Combinations;
		     }
		  }
	  
	      int Find_Total_Occurences(ArrayList<int[]> db, int[] key) 
	      {
	    	  int c = 0;
		    
	    	  for (int i = 0; i < db.size(); i++) 
	    	  {
		    	
	    		  if (has(db.get(i), key)) 
	    		  {
		    	  
	    			  c++;
	    		  }
	    	  }
		    
		    return c;
		  }
		   boolean has_freq_Item(ArrayList<Freq_objects> db, int[] temp)
		   {
			    
			    for (Freq_objects i :db)
			    {
			      if (has(i.Freq_items,temp))
			      {
			        return true;
			      }
			    }
			    return false;
		}
		  boolean has(int[] source, int[] key) 
		  {
			    
			  boolean result = false;
			    int c = 0;
			    for (int i = 0; i < key.length; i++) 
			    {
			      for (int j = 0; j < source.length; j++) 
			      {
			        if (source[j] == key[i]) 
			        {
			          c++;
			        }
			      }
			    }
			    if(c == key.length)
			    {
			      result = true;
			    }
			    return result;
		  }
		   
		   ArrayList<Freq_objects> List_After_Min_Support(ArrayList<Freq_objects> db) 
		   {
			   
		   ArrayList<Freq_objects> Frequent_itemset = new ArrayList<Freq_objects>();
		   for (Freq_objects i :db)  
		    {
		      if (i.support>=msupall) 
		      {
		        if (!i.dbContains(Frequent_itemset,this)) 
		        {
		        	
		        	Frequent_itemset.add(i);
		        }
		      }
		    }
		    return Frequent_itemset;
		  }
		  
		  
         ArrayList<Freq_objects> Genarate_Combinations(ArrayList<Freq_objects> Freq_item_set,ArrayList<Freq_objects> second) throws IOException
		  {
	       
			ArrayList<Freq_objects> new_combinations = new ArrayList<Freq_objects>();
			for (int i = 0; i < Freq_item_set.size(); i++) 
			{
			      for (int j = i+1; j < Freq_item_set.size(); j++) 
			      {
			    
			    	  if (checkCommonElements(Freq_item_set.get(i), Freq_item_set.get(j)) == 1) 
			    	  {
			    		  int[] combs = Get_next_comibination(Freq_item_set.get(i), Freq_item_set.get(j));
			    		  int hashval=Modfunc(combs,5);
			    		  if(Freq_item_set.get(i).Freq_items.length+1<3)
			    		  {
			    			  if(bitmap.containsKey(hashval) && bitmap.get(hashval) && has_freq_Item(second, Freq_item_set.get(i).Freq_items)
			    					  && has_freq_Item(second, Freq_item_set.get(j).Freq_items)
			    					  	&&(!has_freq_Item(new_combinations,combs)))
			    			  {
		        	  
			    				  Freq_objects temp = new Freq_objects(Freq_item_set.get(0).no_of_Items + 1, combs, Find_Total_Occurences(baskets, combs));
//			    				  for(int k=0;k<Freq_item_set.size();k++)
//			    				  {
//			    					  for(int m=0;m<Freq_item_set.get(k).Freq_items.length;m++)
//			    					  { 
//			    						  System.out.print(Freq_item_set.get(k).Freq_items[m]+" ");
//			    					  }
//			    					  System.out.println();
//			    				  }
			    				  new_combinations.add(temp);
			    			  }
			    		  }
			    	else
			    	{
		        	  Freq_objects temp = new Freq_objects(Freq_item_set.get(0).no_of_Items + 1, combs, Find_Total_Occurences(baskets, combs));
//		        	  for(int k=0;k<Freq_item_set.size();k++)
//		        	  {
//		        		  for(int m=0;m<Freq_item_set.get(k).Freq_items.length;m++)
//		        		  { 
//		        			  System.out.print(Freq_item_set.get(k).Freq_items[m]+" ");
//		        		  
//		        		  }
//		        		  System.out.println();
//		        	  }
//		        	 System.out.println();
		        	 new_combinations.add(temp);
		          }
			    }
		    }
			}
		    buckets.clear();
		    bitmap.clear();
		    return new_combinations;
		  }
		  
		 int checkCommonElements(Freq_objects one, Freq_objects another) 
		 {

			 int result = one.Freq_items.length;
			 	for (int i = 0; i < one.Freq_items.length; i++) 
			    {
			 		for (int j = 0; j < another.Freq_items.length; j++) 
			 		{
			 			if (one.Freq_items[i] == another.Freq_items[j]) 
			 			{
			 				result--;
			 			}
			 		}
			    }
			    return result;
		 }
		  void writetofile(ArrayList<Freq_objects> second) throws IOException
		  {
			  BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\syeda\\Documents\\final adb proj\\Freq_Item_Mining\\src\\output.txt", true));
			  for(Freq_objects a1:second)
			  {
				  
				  String data=new String("");
				  for(int a2:a1.Freq_items)
				  {
					  data=data+Integer.toString(a2);
					  data=data+" ";
				  }
				  writer.write(data);
				  String msupport="-- "+Integer.toString(a1.support);
				  writer.write(msupport);
				  writer.newLine();
			  }
			  writer.close();
		  }   
}

