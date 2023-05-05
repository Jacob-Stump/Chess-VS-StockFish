import java.io.BufferedReader;
import java.util.Arrays;
import java.util.concurrent.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Stockfish { //wrapper class for stockfish engine
	
	private Process engineProcess;
	private BufferedReader engineReader; // reads command from java 
	private OutputStreamWriter engineWriter; // writes output from stockfish engine 
	private final String PATH = "C:\\Users\\Stump\\Desktop\\Research Project\\stockfish\\stockfish.exe";
	public String output;
	float evaluation;

	public boolean startEngine() {
		try {
			engineProcess = Runtime.getRuntime().exec(PATH);
			engineReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
			engineWriter = new OutputStreamWriter(engineProcess.getOutputStream());
			
		} 
		catch (Exception e) {
			System.out.println("sorry there was an error");
			return false;
		}
		System.out.println("nice, engine started");
		return true;
	}
	
	public void isReady() {
		try {
			engineWriter.write("isready\n");
			engineWriter.flush();
		}
		catch (Exception e) {
			System.out.println("something went wrong");
		}
		System.out.println("readyok");
	}
	
	public void go() {
		try {
			engineWriter.write("go depth 20\n");
			engineWriter.flush();
			engineProcess.waitFor(500, TimeUnit.MILLISECONDS);
			engineWriter.write("stop\n");
			engineWriter.flush();
			
			String line;
			while ((line = engineReader.readLine()) != null) {
				if (line.contains("best")) {
					output = line;
					break;
				}
				System.out.println(line);
			}
			System.out.println(output);
			
			
			/*String line2;
			while ((line2 = engineReader.readLine()) != null) {
				if (line2.contains("seldepth 20")) {
					String[] cpline = line2.split(" ");
					
					for(int i = 0; i < cpline.length; i++) {
						String index = cpline[i];
						if (index.contains("cp")) {
							String eval = cpline[i+1];
							System.out.println(eval);
							int evalu = Integer.parseInt(eval);
							System.out.println(evalu);
							evaluation = (float)evalu/100;
							System.out.println(evaluation);
						}
					}
					break;
				}
			}
			*/
		
			
			
			
			
		}
		catch (Exception e) {
			System.out.println("something went wrong");
		}
		System.out.println("here's your move m8");
	}
	
	
	
	public void position(String FEN) {
		try {
			String cmd = "position fen " + FEN + "\n"; //formatting FEN string to apply to 
			System.out.println(cmd);
			engineWriter.write(cmd);
			engineWriter.flush();
		}	
		catch (Exception e) {
		System.out.println("rip");
		}
	System.out.println("nice");
	}
	
	public void newGame() {
		try {
			engineWriter.write("ucinewgame\n");
			engineWriter.flush();
		} 
		catch (Exception e) {
			System.out.println("Something went wrong");
		}
		System.out.println("Succesfully started new game");
	}
	
	public void uci() {
		try {
			engineWriter.write("uci\n");
			engineWriter.flush();
		}
		catch (Exception e) {
			System.out.println("Something went wrong");
		}
	}
	public void setLevel(int Level) {
		
		try {
			engineWriter.write("setoption name Skill Level value " + Level + "\n");
			engineWriter.flush();
		}
		catch (Exception e) {
			System.out.println("Something went wrong");
		}
	}
	
	public static void main(String[] args) {
		
	}

}
