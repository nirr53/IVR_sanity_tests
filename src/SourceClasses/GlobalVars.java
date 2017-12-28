package SourceClasses;

/**
* This class holds all the data which is used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalVars {
	
	/**
	*  ip             - Ip of the system
	*  port           - port for create the users (i.e. 8081)
	*  url  		  - Default url for access the system (created by ip)
	*  loginPageStr   - Main page string (used for detect good loading of the tested site)
	*  mainPageStr    - Main page welcome string (used for detect good login)
	*  sysUsername    - Default username for access the system
	*  sysPassword    - Default password for access the system
	*  sysMainStr     - Default string in the welcome page (used for verify access)
	*  srcFilesPath	  - Default path for files
	*  chromeDrvPath  - Chrome driver path
	*  ieDrvPath      - IE driver path
	*  geckoPath	  - Default gecko path (for allow FF to run)
	*  downloadsPath  - Downloads path (configured on consructor)
	*  srcFilesArray  - Array for file names
	*  browsersList	  - Array for hold names of browsers for each running
	*/
    public   LogVars 				 logerVars;
	private  String ip               = "10.21.10.45";
	private  String port             = "8090";
    private  String url  		     = ip + ":" + port + "//logout.php";
    private  String loginPageStr	 = "Application Web Administration Login";
    private  String mainPageStr		 = "Application Web Administration";
    private  String sysUsername      = "Admin";
	private  String sysPassword      = "Admin";
	private  String sysMainStr       = "Audiocodes IPP Administration";
	private  String srcFilesPath     = "C:\\Users\\nirk\\Desktop\\myEclipseProjects\\IVR_sanity_tests\\sourceFiles";	
	private  String chromeDrvPath    = "C:\\Users\\nirk\\Desktop\\Selenium\\chromedriver_win32\\chromedriver.exe";
	private  String ieDrvPath        = "C:\\Users\\nirk\\Desktop\\Selenium\\IEDriverServer_x64_2.53.1\\IEDriverServer.exe";	
	private  String geckoPath        = "C:\\Users\\nirk\\Desktop\\Selenium\\geckodriver-v0.11.1-win64\\geckodriver.exe";
	private  String downloadsPath    = "";
	private  String[] srcFilesArray  = {"Greeting.wav", "MOH.wma"};
	public   String CHROME  	     = "Chrome";
	public   String FF 			     = "Firefox";
	public 	 String IE			     = "IE";
	private  Object[][] browsersList = {{CHROME}};

    /**
    *  Non-default constructor for provide another data
    *  @param _url  	Non-default url for access the system
	*  @param _username Non-default username for access the system
	*  @param _password Non-default password for access the system
	*  @param _mainStr  Non-default string for verify good access
    */
	public GlobalVars(String _url, String _username, String _password, String _mainStr) {
		
		System.out.println("GlobalVars constructor is called");
		this.url 		 = _url;
		this.sysUsername = _username;
		this.sysPassword = _password;
		this.sysMainStr  = _mainStr;
    	this.downloadsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads";
    	this.logerVars     = new LogVars();
	}
	
    /**
    *  Default constructor for provide interface
    */
    public GlobalVars() {
    	
    	this.logerVars     = new LogVars();
    	this.downloadsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads";  		
	}
    
	/**
    *  Default method for return the url variable
    *  @return url of the system
    */
	public String getUrl() {
		
		return this.url;
	}
	
	/**
    *  Default method for return the mainPageStr variable
    *  @return main string of the system
    */
	public String getMainPageStr() {
		
		return this.mainPageStr;
	}
	
	/**
	*  Default method for return the loginPageStr variable
	*  @return login string of the system
	*/
	public String getLoginPageStr() {
		
		return this.loginPageStr;
	}
	
    /**
    *  Default method for return the username variable
    *  @return username of the system for Admin
    */
	public String getSysUsername() {
		
		return this.sysUsername;
	}
	
    /**
    *  Default method for return the password variable
    *  @return password of the system for Admin
    */
	public String getSysPassword() {
		
		return this.sysPassword;
	}
	
    /**
    *  Default method for return the main-str variable
    *  @return sysStr of the system
    */
	public String getSysMainStr() {
		
		return this.sysMainStr;
	}

    /**
    *  Default method for return the Chrome driver path
    *  @return chromeDrvPath of the system
    */
	public String getchromeDrvPath() {
		
		return this.chromeDrvPath;
	}
	
    /**
    *  Default method for return the IE driver path
    *  @return ieDrvPath of the system
    */
	public String getIeDrvPath() {
		
		return this.ieDrvPath;
	}
	
    /**
    *  Default method for return the path for the directory of the source files
    *  @return srcFilesPath of the system
    */
	public String getSrcFilesPath() {
		
		return this.srcFilesPath;
	}
	
    /**
    *  Default method for return the System IP
    *  @return ip
    */
	public String getIp() {
		
		return this.ip;
	}
	
    /**
    *  Default method for return the System Port
    *  @return port
    */
	public String getPort() {
		
		return this.port;
	}
	
    /**
    *  Default method for return the downloads path
    *  @return version
    */
	public String getDownloadsPath() {
		
		return this.downloadsPath;
	}
	
    /**
    *  Default method for return a name of source file by given integer
    *  @param idx Index of the current test in the format of <test>.<sub-test>
    *  @return String that represent name of the used file
    */
	public String getImportFile(String idx) {
		
		String usedSrcFile = "";
		switch (idx) {
		
			case "prompt_based_file":
				 usedSrcFile = this.srcFilesArray[0];
				 break;
				 
			case "moh_file":
				 usedSrcFile = this.srcFilesArray[1];
				 break;	

			default:
				usedSrcFile = "";
				break;
		}
		System.out.println("   usedSrcFile - " + usedSrcFile);
		return usedSrcFile;
	}

    /**
    *  Default method for return the used browsers in the current test
    *  @return browsersList
    */
	public Object[][] getBrowsers() {
		
		return this.browsersList;
	}

    /**
    *  Default method for return the gecko driver path (the extarnal driver for ff)
    *  @return ieDrvPath of the system
    */
	public String getGeckoPath() {
		
		return this.geckoPath;
	}
}