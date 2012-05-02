package entites;

public class AppData {

	private static int[] lock = new int[0];
	private static AppData instance = null;
	
	private WeiboAlbumEntity album = new WeiboAlbumEntity();
	
	private AppData(){
		
	}
	
	public static AppData getInstance(){
		if( instance == null ){
			synchronized (lock) {
				if( instance == null )
					instance = new AppData();
			}
		}
		return instance;
	}

	public WeiboAlbumEntity getAlbum() {
		return album;
	}

	private void setAlbum(WeiboAlbumEntity album) {
		this.album = album;
	}

	
	
}
