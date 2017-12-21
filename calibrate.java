import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.*;

/** Get CPU time in nanoseconds. */
class  NumIslands{
	int value = 0;
}


class Result{
	
	int r;
	int c;
	char[][] fruits ;
	
	public Result(int r,int c, char[][] fruits) {
		super();
		this.r = r;
		this.c = c;
		this.fruits = fruits;
	}
	
	public Result() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMove(int r,int c) {
		this.r = r;
		this.c = c;
	}

	public char[][] getFruits() {
		return fruits;
	}

	public void setFruits(char[][] fruits) {
		this.fruits = fruits;
	}
	

}

public class calibrate {
	
	public static long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadCpuTime( ) : 0L;
	}
	
	static int n=9;
	static int p=6;
	static int depth = 5;
	static int countNodes =0;
	
	
	static void display(char[][] fruits,int score){
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				System.out.print(fruits[i][j]+" ");
			}
			System.out.println();
		}
		
		System.out.println("score "+score);
		System.out.println("###################################################################################");
	}
	
	static int getIndex(int row, int col) 
	{ 
		return row*n+col; 
	}
	
	static int getRow(int index){
		return index/n;
	}
	
	static int getCol(int index){
		return index%n;
	}
	
	static HashMap<Integer,ArrayList<Integer>> getPointsMap(char[][] fruits,NumIslands num){
		
		HashMap <Integer,ArrayList<Integer>> map = new HashMap<Integer,ArrayList<Integer>>();
		if(fruits == null || n==0 ){
			return null;
		}
		
		char [][] fruitsCloned = new  char[n][];
		for(int k = 0; k < n; k++){
			fruitsCloned[k] = fruits[k].clone();
		}
		
		int p=0;
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				p = getPointsAt(fruitsCloned,i,j,fruitsCloned[i][j]);
				if(p>0){
					ArrayList<Integer> positions ;
					if(map.containsKey(p)){
						positions = map.get(p);
					}else {
						positions = new ArrayList<Integer>();
					}
					positions.add(getIndex(i,j));
					map.put(p, positions);
					if(num !=null){
						num.value = num.value+1;
					}
					
				}
		//		points[i][j] = getPointsAt(fruits,i,j,fruits[i][j]);
				
			}
		}
		
		return map;
		
	}
	
	static int getPointsAt(char[][] fruits, int r,int c,char fruitValue){
		if(r<0 || r>=n || c <0 || c>=n || fruitValue =='*' || fruitValue!=fruits[r][c]){
			return 0;
		}
		
		int sum = 1;
		fruits[r][c]='*';
		
		sum+=getPointsAt(fruits,r+1,c,fruitValue);
		sum+=getPointsAt(fruits,r-1,c,fruitValue);
		sum+=getPointsAt(fruits,r,c+1,fruitValue);
		sum+=getPointsAt(fruits,r,c-1,fruitValue);
		
		return sum;
	}
	
	static void applyGravity(char[][] fruits){
		
		for(int c =0 ;c<n;c++){
			int k =n-1;
			for(int r = n-1; r>=0;r-- ){
				if(fruits[r][c]!='*'){
					// it is a fruit 
					if(k!=r){
						// need to copy only when k!=r
						fruits[k][c]= fruits[r][c];
						fruits[r][c]='*';
					} 
					k--;
				} 
			}
		}
		
	}
	
	static int utility(Integer [] arr, boolean max){
		Arrays.sort(arr, Collections.reverseOrder());
		
		int utility = 0;
		for(int key : arr){
			if(max){
				utility+=(key*key);
			
			} else {
				utility-=(key*key);
			}
			max =!max;
		}
		
		return utility;
		
	}
	
	
static int maxValue(char[][] fruits,Integer alpha,Integer beta,int currScore, Result decision, boolean decideMove, int level){
		
        NumIslands num = new NumIslands();
		HashMap<Integer,ArrayList<Integer>> moves = getPointsMap(fruits,num);
		
		Integer veta = Integer.MIN_VALUE;
		Set<Integer> keys = moves.keySet();
		Integer [] arr = keys.toArray(new Integer[keys.size()]);
		Arrays.sort(arr, Collections.reverseOrder());
		
		
			// if(timedoesnotpermits and decideMove is true ) --> return the first move out of moves 
		// decidedPosition = firstElement in Pos and return that if decidemove is true 
		// depth limit is reached // there are no possible moves --which wont be the case 
		if(moves.keySet().size()==0){
				return currScore;
		}else if(depth==level){
			return (int)(0.5*(currScore)+0.5*(utility(arr,true)));
			
		} 
				
				for(int i: arr){
					ArrayList<Integer> movesList = moves.get(i);
					for(Integer pos : movesList){
						
						char [][] nextState =  playMove(fruits, pos);
						veta = Math.max(veta, minValue(nextState,alpha,beta,currScore+(i*i),level+1));
						if(veta >= beta){
							return veta;
						}
						if(veta > alpha){
							// since I updated alpha i should update the move 
							if(decideMove==true){
									decision.setFruits(nextState);
									decision.setMove(getRow(pos),getCol(pos));
							}
							
							alpha = veta;
							
						}
						//alpha = Math.max(veta,alpha);
					}
				}
				return veta;

	}
	
	static int minValue(char[][] fruits,Integer alpha,Integer beta,int currScore,int level){
		
		HashMap<Integer,ArrayList<Integer>> moves = getPointsMap(fruits,null);
				Integer veta = Integer.MAX_VALUE;
				
				Set<Integer> keys = moves.keySet();
				Integer [] arr = keys.toArray(new Integer[keys.size()]);
				Arrays.sort(arr, Collections.reverseOrder());
				
				
				if(moves.keySet().size()==0){
					return currScore;
				}else if(depth==level){
					return (int)(0.5*(currScore)+0.5*(utility(arr,false)));
				} 
				
				for(Integer i: arr){
					ArrayList<Integer> movesList = moves.get(i);
					for(Integer pos : movesList){
						
						char [][] nextState =  playMove(fruits, pos);
						veta = Math.min(veta, maxValue(nextState,alpha,beta,currScore-(i*i),null,false,level+1));
						if(alpha >= veta){
							return veta;
						}
						beta = Math.min(veta,beta);
					}
				}
				return veta;

	}
	
	static char[][] playMove(char[][] fruits,Integer pos){
		
		countNodes++;
		char [][] fruitsCloned = new  char[n][];
		for(int k = 0; k < n; k++){
			fruitsCloned[k] = fruits[k].clone();
		}
		
		int r = getRow(pos);
		int c = getCol(pos);
		int p = getPointsAt(fruitsCloned,r,c,fruitsCloned[r][c]);
		applyGravity(fruitsCloned);
		return fruitsCloned;
		
	}

	
	public static void main(String args[]){
		
		double secondsInNano = 1000000000L;
		long startCPUTime = getCpuTime();
		char [][] fruits ={
				{'1','3','3','1','5','5','5','5','3'},
				{'3','0','2','1','0','5','3','5','0'},
				{'5','3','5','3','2','3','0','0','4'},
				{'1','5','4','3','4','0','1','2','0'},
				{'1','2','4','1','2','5','5','5','1'},
				{'1','4','4','1','5','1','4','1','2'},
				{'4','3','1','2','3','4','1','2','5'},
				{'5','4','1','1','0','0','1','3','2'},
				{'0','1','0','4','5','1','2','4','0'}
			};
	
		Result res = new Result();
		int minimaxValue = maxValue(fruits,Integer.MIN_VALUE,Integer.MAX_VALUE,0,res,true,0);
		
		long taskCPUTimeNano = getCpuTime()-startCPUTime;
		double speed = countNodes * (secondsInNano/taskCPUTimeNano);
		speed = Math.round(speed * 100.0) / 100.0;
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter("calibration.txt");
			bw = new BufferedWriter(fw);
			bw.write(String.valueOf(speed));
		
			if (bw != null)
				bw.close();

			if (fw != null)
				fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		} 		
	}
}
