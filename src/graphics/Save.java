package graphics;

import java.util.*;
import java.io.*;

public class Save {
	public Save(List<Action> game, String filename) throws IOException {
	
		PrintWriter pw = new PrintWriter(filename);

		 
		for(int i = 0; i < game.size(); i ++) {
    			pw.println(game.get(i).saveString());
		}
	 
		pw.close();
	}
}
