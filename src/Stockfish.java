import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Stockfish { //wrapper class for stockfish engine
	
	private Process engineProcess;
	private BufferedReader engineReader; // reads command from java 
	private OutputStreamWriter engineWriter; // writes output from stockfish engine 
	private final String PATH = "C:\\Users\\Stump\\Desktop\\Research Project\\stockfish\\stockfish.exe";

	public boolean startEngine() {
		try {
			engineProcess = Runtime.getRuntime().exec(PATH);
			engineReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
		} 
		catch (Exception e) {
			return false;
		}
		return true;
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
	
	
	public void sendCommand(String cmd) {
		try {
			engineWriter.write(cmd + "\n");
			engineWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		
	}

}
