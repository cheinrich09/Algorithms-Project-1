package problem2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Comparator;

//class to store a pair of points
class Pair
{
	Point point1;
	Point point2;
	Pair()
	{
		point1 = new Point();
		point2 = new Point();
	}
	Pair(Point p1, Point p2)
	{
		point1 = p1;
		point2 = p2;
	}
	//calculate the distance between the two points
    double distance()
	{
    	//if the two points of the pair are the same, return infinity
		if (point1.x == point2.x && point1.y == point2.y)
		{
			return Double.POSITIVE_INFINITY;
		}
		//otherwise return distance
    	return Math.sqrt(((point2.x-point1.x)*(point2.x-point1.x))+((point2.y-point1.y)*(point2.y-point1.y)));
	}
}

public class ClosestPoints 
{
	static Random randGen;

	public static void main(String[] args) {
		
		int n;
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
		double mean = n/2;
		randGen = new Random();
		Point[] points = new Point[n];
		for (int i = 0; i<n; i++)
		{
			points[i] = new Point(pD(mean), pD(mean));
		}
		long startTime = System.nanoTime();
		Closest_Pair(points);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		duration = duration/1000000;
		System.out.println("Program runtime: "+ duration+" milliseconds");
		
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
	
	
	//Closest-Pair(P)
	public static void Closest_Pair(Point[] points)
	{
		//construct Px and Py 
		Point[] pointsByX = new Point[points.length];
		Point[] pointsByY = new Point[points.length];
		for(int i = 0; i<points.length; i++)
		{
			pointsByX[i] = points[i];
			pointsByY[i] = points[i];
		}
		Arrays.sort(pointsByX, new Comparator<Point>(){
			public int compare(Point p, Point q) {
				if (p.getX() < q.getX()) return -1;
					if (p.getX() > q.getX()) return 1;
				return 0;
			}
		});
		Arrays.sort(pointsByY, new Comparator<Point>(){
			public int compare(Point p, Point q){
				if (p.getY() < q.getY()) return -1;
					if (p.getY() > q.getY()) return 1;
				return 0;
			}
		});
		//(P0*, P1*) = closest-Pair-Rec(Px,Py)
		Pair closestPair = ClosestPairRec(pointsByX, pointsByY); // = ClosestPairRec(Px, Py);
		System.out.println("Point1: ("+closestPair.point1.x+","+closestPair.point1.y+")");
		System.out.println("Point2: ("+closestPair.point2.x+","+closestPair.point2.y+")");
	}
	
	//Closest-Pair-Rec(Px, Py)
	static Pair ClosestPairRec(Point[] Px, Point[] Py)
	{
		//Pair closestPair;
		if ((Px.length)<=3)
		{//if P.size <=3
			//find closest pair by measuring all pairwise distances
			//endif
			Pair currentBest = new Pair();
			if (Px.length < 2)
			{
				//will return currentBest unmodified, which will mean that the distance will return infinity, preventing any edge cases
			}
			else
			{
				for (int n = 0; n < Px.length-1; n++)
				{
					for (int m = n+1; m < Px.length; m++)
					{
						Pair temp = new Pair(Px[n], Px[m]);
						if (temp.distance()<currentBest.distance())
						{
							currentBest = temp;
						}
					}
				}
			}
			return currentBest;
		}
		else
		{	//create the four lists 
			Point[] Qx = new Point[Px.length/2];
			Point[] Qy = new Point[Px.length/2];
			Point[] Rx = new Point[Px.length/2];
			Point[] Ry = new Point[Px.length/2];
			//int h = 0;
			for(int i=0; i<Px.length/2; i++)
			{
				//construct Qx, Qy, 
				Qx[i]=Px[i];
				Qy[i]=Py[i];
				Rx[i] = Px[i+Px.length/2];
				Ry[i] = Py[i+Px.length/2];
			}
			//(Q0*, Q1*) = Closest-Pair-Rec(Qx, Qy)
			//(R0*, R1*) = Closest-Pair-Rec(Rx, Ry)
			Pair Qpair = ClosestPairRec(Qx, Qy);
			Pair Rpair = ClosestPairRec(Rx, Ry);
			double distance;
			if (Qpair.distance()<Rpair.distance())
			{
				distance = Qpair.distance();
			}
			else
			{	
				distance = Rpair.distance();
			}
			//distance delta = min(d(Q0*, Q1*), d(R0*, R1*))
			
			Point X = Qx[Qx.length-1];
			//X* - maximum x-coordinate of a point in set Q
			//L = {(x,y) : x = x*}
			int L = X.x;
			//S = points in P within distance delta of L
			List<Point> S = new ArrayList<Point>();
			for(Point element : Px)
			{
				if(Math.abs(element.x-L) <= distance){
					S.add(element);
				}
			}
			//Construct Sy
			Point[] Sy = new Point[S.size()];
			int i = 0;
			for( Point element : S)
			{
				Sy[i] = element;
				i++;
			}
			Arrays.sort(Sy, new Comparator<Point>(){
				public int compare(Point p, Point q){
					if (p.getY() < q.getY()) return -1;
						if (p.getY() > q.getY()) return 1;
					return 0;
				}
			});
		
			//for each point s E Sy, compute distance from s
				//to each of next 15 points in Sy
			Pair sPair = new Pair();
			for(int s = 0; s < Sy.length; s++)
			{
				for(int sCompare = 0; sCompare < 15; sCompare++)
				{
					if(s+sCompare < Sy.length)
					{
						Pair temp = new Pair(Sy[s],Sy[s+sCompare]);
						if (temp.distance() < sPair.distance())
						{
							//Let s, s' be pair achieving minimum of these distances
							sPair = temp;
						}
					}
					else
					{
						sCompare = 15;
					}
				}
			}
			//if d(s,s') < delta
				//return (s,s')
			//Else if d(Q0*, Q1*) < d(R0*, R1*) then 
				//return (Q0*, Q1*)
			//else
				//return (R0*, R1*
			if (sPair.distance() < distance)
			{
				return sPair;
			}
			else if(Qpair.distance() < distance)
			{
				return Qpair;
			}
			else
			{
				return Rpair;
			}
			//end if
		}
	}
}
