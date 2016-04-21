package edu.brown.cs.hhalvers.catan;

import edu.brown.cs.api.CatanAPI;
import edu.brown.cs.networking.CatanServer;

public class Main {

	public static void main(String[] args) {
		new CatanServer(new CatanAPI());
	}

}
