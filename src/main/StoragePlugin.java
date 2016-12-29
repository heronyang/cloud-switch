package main;

public interface StoragePlugin extends Plugin {
	
	public boolean auth();
	public String downloadAll() throws InterruptedException;
	public void uploadAll(String path);

}
