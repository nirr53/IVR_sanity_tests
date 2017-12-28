package SourceClasses;

public class MenuPaths {
	
	// Xpath strings
	// Backup
	public String aaBackupRestoreSection 	= "//*[@id='c5']";
	public String aaBackupRestoreMenu    	= "//*[@id='c5.1']";
	
	// Management
	public String aaManagementSection	  	= "//*[@id='tab_names']/tbody/tr/td[2]/table/tbody/tr[1]/td[2]/a/img";
	public String aaManagementAASection  	= "//*[@id='c9']";
	public String aaManagementAALangs   	= "//*[@id='c9.2']";
	public String aaManagementAAPrompts  	= "//*[@id='c9.3']";
	public String aaManagementAAMohs  	  	= "//*[@id='c9.4']";
	public String aaManagementBusiHours  	= "//*[@id='c9.5']";
	public String aaManagementHolidays   	= "//*[@id='c9.6']";
	public String aaManagementIvrEndPnts 	= "//*[@id='c9.7']";
	public String aaManagementIvrs		 	= "//*[@id='c9.8']";
	
	// Automatic Call Distribution
	public String acdManagementSection  	= "//*[@id='c10']";
	public String acdAgents					= "//*[@id='c10.1']";
	public String acdGroups					= "//*[@id='c10.2']";
	public String acdQueues					= "//*[@id='c10.3']";
	public String acdAcds					= "//*[@id='c10.4']";

	// Proxy SIP Trunks
	public String proSipTrunksSection  		= "//*[@id='c11']";
	public String proSipTrunks				= "//*[@id='c11.2']";
	
	// Import
	public String aaManagementImportSection = "//*[@id='c12']";
	public String aaManagementImportExport  = "//*[@id='c12.1']";
	public String aaManagementLoadSamples  =  "//*[@id='c12.2']";

	public String[] getPaths(String menuName) {
		
		String[] paths = {"", "", "", ""};
		switch (menuName) {
		
			case "AA_Backup_and_restore":
	            paths[0] = aaBackupRestoreSection;
	            paths[1] = aaBackupRestoreMenu;
	            break;  
			case "AA_Languages":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementAASection;
	            paths[2] = aaManagementAALangs;
	            break;          
			case "AA_prompts":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementAASection;
	            paths[2] = aaManagementAAPrompts;
	            break;
			case "AA_mohs":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementAASection;
	            paths[2] = aaManagementAAMohs;
	            break;
			case "AA_Buhs":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementAASection;
	            paths[2] = aaManagementBusiHours;
	            break;
			case "AA_Holidays":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementAASection;
	            paths[2] = aaManagementHolidays;
	            break;            
			case "AA_IVRendPoints":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementAASection;
	            paths[2] = aaManagementIvrEndPnts;
	            break;  
			case "AA_Ivrs":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementAASection;
	            paths[2] = aaManagementIvrs;
	            break;      
			case "ACD_Agents":
	            paths[0] = aaManagementSection;
	            paths[1] = acdManagementSection;
	            paths[2] = acdAgents;
	            break;
			case "ACD_Groups":
	            paths[0] = aaManagementSection;
	            paths[1] = acdManagementSection;
	            paths[2] = acdGroups;
	            break;
			case "ACD_Queues":
	            paths[0] = aaManagementSection;
	            paths[1] = acdManagementSection;
	            paths[2] = acdQueues;
	            break;     
			case "ACD_Acds":
	            paths[0] = aaManagementSection;
	            paths[1] = acdManagementSection;
	            paths[2] = acdAcds;
	            break;  
			case "SiInter_Proxy_sip_trunks":
	            paths[0] = aaManagementSection;
	            paths[1] = proSipTrunksSection;
	            paths[2] = proSipTrunks;
	            break; 
			case "Import_ImportExport":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementImportSection;
	            paths[2] = aaManagementImportExport;
	            break;      
			case "Import_Load_samples":
	            paths[0] = aaManagementSection;
	            paths[1] = aaManagementImportSection;
	            paths[2] = aaManagementLoadSamples;
	            break;

	         default:
	        	 GlobalFuncs testFuncs = new GlobalFuncs();
	             testFuncs.myFail("Menu name is not recognized !!");    
			}
		
		return paths;      
     }		
}
