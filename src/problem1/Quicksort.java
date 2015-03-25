package problem1;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Quicksort {

	static Random randGen;
	//main function
	public static void main(String[] args) {
		int n;
		//take in data set size 
		if (args.length >0)
		{
				n = Integer.parseInt(args[0]);
		}
		else
		{
			//n = 1000;
			//n = 10*1024;
			n = 50*1024;
			//n = 100*1024;
			//n = 500*1024;
		}
		randGen = new Random();
		//generate set
		//List<Integer> mainSet = randomizeSet(n);
		List<Integer> mainSet = poissonSet(n);
		//record cpu time
		long startTime = System.nanoTime();
		mainSet = My_Choice_Quicksort(mainSet);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		duration = duration/1000000;
		//print cpu time
		System.out.println("Program runtime: "+ duration+" milliseconds");
		
		/*System.out.println("Final Set:");
		int i = 0;
		for(int element : mainSet)
		{
			i++;
			System.out.println(i + ": "+element);
		}*/
	}

	
	
	public static List<Integer> My_Choice_Quicksort(List<Integer> S){
		
	
		if(S.size() < 4)//If size of S is less than 4 elements
		{
			//sort S 
			//output the sorted set
			if(S.size() <= 1)
			{
				return S;
			}
			else if (S.size() == 2)
			{
				if(S.get(0) > S.get(1))
				{
					int temp = S.get(0);
					S.set(0, S.get(1));
					S.set(1, temp);
				}
				return S;
			}
			else
			{
				if (S.get(0) > S.get(1))
				{
					int temp = S.get(0);
					S.set(0, S.get(1));
					S.set(1, temp);
				}
				if (S.get(1) > S.get(2))
				{
					int temp = S.get(1);
					S.set(1, S.get(2));
					S.set(2, temp);
				}
				if (S.get(0) > S.get(1))
				{
					int temp = S.get(0);
					S.set(0, S.get(1));
					S.set(1, temp);
				}
				return S;
			}
		}
		else //else
		{
			int Ai = 0;
			List<Integer> sMinus;
			List<Integer> sPlus;
			while(true)
			{
				int pivot = randGen.nextInt(S.size()); //choose a splitter ai E S uniformly at random
				boolean equalGoesLeft = true;
				Ai = S.get(pivot);
				S.remove(pivot);
				sMinus = new ArrayList<Integer>();
				sPlus = new ArrayList<Integer>();
				for (int Aj : S)//for each element aj of S
				{
					if (Aj < Ai) //put aj in Sminus if Aj < Ai
					{
						sMinus.add(Aj);
					}
					else if (Aj > Ai) //put aj in Splus if Aj > Ai
					{
						sPlus.add(Aj);
					}
					else if (Aj == Ai && equalGoesLeft)//alternate which side to send values equal to the pivot, 
					{								   //treating the pivot as the center point of those values to prevent hang up.
						sMinus.add(Aj);
						equalGoesLeft = false;
					}
					else if (Aj == Ai && !equalGoesLeft)
					{
						sPlus.add(Aj);
						equalGoesLeft = true;
					}
				}//endfor
				if (sMinus.size() >= S.size()/4 && sPlus.size() >= S.size()/4)
				{
					//Ai is the central splitter
					break;	
				}
				else
				{
					S.add(Ai);
				}
			}
			sMinus = My_Choice_Quicksort(sMinus);//recursively call Quicksort(SMinus) and QUicksort(SPlus)
			sPlus = My_Choice_Quicksort(sPlus);
			
			//Output the sorted Set SMinus, then AI, THen the sorted set SPlus
			S.clear();
			S.addAll(sMinus);
			S.add(Ai);
			S.addAll(sPlus);
			
			return S; 
			
		} //end if	
	}
	
	public static List<Integer> randomizeSet(int n)
	{
		List<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i< n; i++)
		{
			temp.add(randGen.nextInt(100));	
		}
		return temp;
	}
	
	//Knuth's Method
	public static int pD(double mean)
	{
		double L = Math.exp(-mean);
		int k = 0;
		double p = 1.0;
		do 
		{
			p = p * randGen.nextDouble();
			k++;
		}while (p > L);
		return k-1;
	}
	
	
	public static List<Integer> poissonSet(int n)
	{
		List<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i<n; i++)
		{
			temp.add(pD(n/2));
		}
		return temp;	
	}
}
