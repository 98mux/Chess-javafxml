package graphics;

import java.io.*;
import java.util.*;

public class Load {
	public static Stack<Action> LoadFile(String filename) {
		Stack<Action> list = new Stack<Action>();

		try (Scanner inFile = new Scanner(new FileReader(filename))){
			String line;
		    while (inFile.hasNextLine()) {
		    		line = inFile.nextLine();
		       Action a = Action.loadString(line);
		       list.push(a);
		    }
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		return list;
	}
}
