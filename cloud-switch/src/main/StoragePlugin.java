package main;

public interface StoragePlugin extends Plugin {
	
	public boolean auth();
	public String downloadAll();
	public void uploadAll(String path);

}
