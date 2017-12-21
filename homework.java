import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

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

public class homework {
	
	static double timeRemaining=0.0;
	static int n=0;
	static int p=0;
	static int numberOfIslands =0;
	static double speed=0.0;
	static int depth = 0;
	
	
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
	
	
static int maxValue(char[][] fruits,Integer alpha,Integer beta,int currScore, Result decision, boolean decideMove, int level){
		
        NumIslands num = new NumIslands();
		HashMap<Integer,ArrayList<Integer>> moves = getPointsMap(fruits,num);
		
		
		Integer veta = Integer.MIN_VALUE;
		Set<Integer> keys = moves.keySet();
		Integer [] arr = keys.toArray(new Integer[keys.size()]);
		Arrays.sort(arr, Collections.reverseOrder());
		
		if(decideMove == true){
			
			numberOfIslands = (num!=null) ? num.value : 0;
			double power = (Math.log(numberOfIslands/2))/Math.log(2);
			double timePerMove = Math.pow(0.75 , power) * timeRemaining; 
			double nodesPerMove = timePerMove * speed;
			depth = (int) (Math.log(nodesPerMove)/Math.log(numberOfIslands));
			
		}
		
		if(moves.keySet().size()==0){
				return currScore;
		} else if(depth==level){
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
	
	static char[][] playMove(char[][] fruits,int pos){
		
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
		File fin = null;
		char [][] fruits = null;
		
		try {
			fin = new File("input.txt");
			if(fin!=null){
				
				BufferedReader br;
				br = new BufferedReader(new FileReader(fin));
				String line = null;
				
				 
				 homework.n = Integer.parseInt(br.readLine().trim());
				 homework.p = Integer.parseInt(br.readLine().trim());
				 homework.timeRemaining = Double.parseDouble(br.readLine().trim());
				
				fruits = new char[n][n]; 
			
				int i = 0;
				while (i<n && (line = br.readLine()) != null) {
					char[] row = (line.trim()).toCharArray();
					int j=0;
					while(j<n){
						fruits[i][j] = row[j];
						j++;
					}
					i++;
				}
				br.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		try {
			fin = new File("calibration.txt");
			if(fin!=null){
				
				BufferedReader br;
				br = new BufferedReader(new FileReader(fin));
				String line = null;
				homework.speed = Double.parseDouble(br.readLine().trim());
				br.close();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		Result res = new Result();
		
		if(timeRemaining <=10){
			//take a quick decision
			HashMap<Integer,ArrayList<Integer>> moves = getPointsMap(fruits,null);
			Set<Integer> keys = moves.keySet();
			Integer [] arr = keys.toArray(new Integer[keys.size()]);
			Arrays.sort(arr, Collections.reverseOrder());
			ArrayList<Integer> movesList = moves.get(arr[0]);
			Integer pos = movesList.get(0);
			char [][] nextState =  playMove(fruits, pos);
			res.setFruits(nextState);
			res.setMove(getRow(pos),getCol(pos));
				
		} else {
			maxValue(fruits,Integer.MIN_VALUE,Integer.MAX_VALUE,0,res,true,0);
		}
		
		if( res.fruits!=null){
			
			BufferedWriter bw = null;
			FileWriter fw = null;

			try {

				fw = new FileWriter("output.txt");
				bw = new BufferedWriter(fw);
				if(res!=null){
					
					bw.write((char)(res.c+65)+""+(res.r+1));
					bw.newLine();
						for(int i =0;i < n;i++){
							for(int j =0;j<n ;j++){
								bw.write(res.fruits[i][j]+"");
							}
							bw.newLine();
						}
				}
			
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}
		
	}
}
