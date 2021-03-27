import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This Class will parse all requested weapon_<weaponListElement>.txt from weaponsList in the given directory and make a list of Weapons out of them (= the library).
 * @param pathWeaponScriptFiles
 * @param weaponNames
 * @param statsList
 */
public class WeaponLibrary {
	
	
	
	//path to file with weapon names
	private String pathWeaponsToConsider = "data/weaponsToCheck.txt";
	//path to folder with weapon script files (weapon_cz75a.txt etc)
	private String pathWeaponScriptFiles = "data/scripts140210/";
	
	//list with all weapons we want to gather statistics about, these are imported from weaponsToCheck.txt by importWeaponNames()
	private ArrayList<String> weaponNames = new ArrayList<String>();
	//list with weapon objects, gets filled by gatherAttributes()
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	
	
	
	
	public WeaponLibrary(String pathWeaponsToConsider, String pathWeaponScriptFiles) {
		this.pathWeaponScriptFiles = pathWeaponScriptFiles;	
		this.pathWeaponsToConsider = pathWeaponsToConsider;
		
		//import the names of all weapons we want to consider
		importWeaponNames();
		//build library
		initLibrary();
	}
		
	
	
	
	/**
	 * Imports the names of all Weapons this program is supposed to consider
	 * according to the corresponding text file in the programs directory (at the moment: data/weaponsToConsider.txt).
	 * @return
	 */
	private void importWeaponNames() {
		
		BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(new File(this.getClass().getResource(pathWeaponsToConsider).getFile())));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		String nextLine = "";
		try {
			while((nextLine = br.readLine()) != null) {
				nextLine = nextLine.trim();
				if(nextLine.startsWith("//")) {
					continue;
				} else {
					weaponNames.add(nextLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Tries to make all known weapon names into weapons. Stores those weapons in this object's weapons list.
	 */
	private void initLibrary() {

		//for every weapon we want to know the statistics of, try making a weapons object with generateWeaponObject()
		for(String weaponName : weaponNames) {
			try {
				Weapon weapon = generateWeaponObject(weaponName);
				weapons.add(weapon);
			} catch(FileNotFoundException e) {
				System.err.println(this.getClass().toString()+" gatherAttributes() Could not find weapon_" +weaponName+ ".txt in " + this.pathWeaponScriptFiles);
				e.printStackTrace();
			}	
		}
	}
	
	

	
	/**
	 * Seeks given weapon name in CSGO script files and tries to create a weapons object.
	 */
	private Weapon generateWeaponObject(String weaponName) throws FileNotFoundException {

		//todo:
		//crawl through items_game.txt, instantiate Weapon as specified under weapon_<weaponname>_prefab in items_game.txt.
		//then crawl on and overwrite stats of prefab with stats found in items_game.txt
		
		//until the above is implemented, we're limited to using only weapon_<weaponname>.txt files
		//which is a problem if valve patches in new weapons, because that seems to happen through items_game exclusively
		
		
		//crawl through weapon_<weaponname>.txt and look for stats.
		HashMap<String,String> weaponStats = getWeaponScriptFileAsMap(weaponName);
	
		//using the stats we just gathered, set attributes in weapon object accordingly
		Weapon weapon = new Weapon();	
		weapon.setWeaponName(weaponStats.get("WeaponName"));
		weapon.setMaxPlayerSpeed(Integer.parseInt(weaponStats.get("MaxPlayerSpeed")));
		weapon.setWeaponType(weaponStats.get("WeaponType"));
		weapon.setWeaponPrice(Integer.parseInt(weaponStats.get("WeaponPrice")));
		weapon.setWeaponArmorRatio(Double.parseDouble(weaponStats.get("WeaponArmorRatio")));
		weapon.setTeam(weaponStats.get("Team"));
		weapon.setPenetration(Integer.parseInt(weaponStats.get("Penetration")));
		weapon.setDamage(Integer.parseInt(weaponStats.get("Damage")));
		weapon.setRange(Integer.parseInt(weaponStats.get("Range")));
		weapon.setRangeModifier(Double.parseDouble(weaponStats.get("RangeModifier")));
		weapon.setBullets(Integer.parseInt(weaponStats.get("Bullets")));
		weapon.setCycleTime(Double.parseDouble(weaponStats.get("CycleTime")));
		weapon.setRecoveryTimeStand(Double.parseDouble(weaponStats.get("RecoveryTimeStand")));
		weapon.setRecoveryTimeCrouch(Double.parseDouble(weaponStats.get("RecoveryTimeCrouch")));
		weapon.setClip_size(Integer.parseInt(weaponStats.get("clip_size")));
		
		return weapon;
	}
	
	
	/**
	* Converts weapon script file into a HashMap of weapon attributes/values.
	*/
	@Deprecated
	private HashMap<String, String> getWeaponScriptFileAsMap(String weaponName) throws FileNotFoundException {
	
		//relative path to weapon script file
		File weaponFile = new File(this.getClass().getResource(pathWeaponScriptFiles +"weapon_"+ weaponName +".txt").getFile());
		//this hashmap will be filled in the (K,V) format (ATTRIBUTE_NAME, VALUE)
		HashMap<String,String> attributes = new HashMap<String,String>();					

		
		//In the following couple of lines: Check all fields of the Weapon class. Extract corresponding attributes from weapon script file.
		
		//The weapon's name doesn't occur within its own script file. Add that to the map manually
		attributes.put("WeaponName", weaponName);
		for(Field f : Weapon.class.getDeclaredFields()) {
			f.setAccessible(true); //field must be accessible or it won't respond to getName()
			String attributeName = f.getName();
			BufferedReader reader = new BufferedReader(new FileReader(weaponFile));
			String nextLine = "";
			try { 
				//go through entire weapons script file
				while((nextLine = reader.readLine()) != null) {
					//if the current attribute name occurs in a line, clear everything in that line so that only the attribute value is left
					if(nextLine.contains(attributeName)) {
						nextLine = nextLine.replaceAll(attributeName, ""); 		
						nextLine = nextLine.replaceAll(("\\s+"), "");
						nextLine = nextLine.replaceAll(("\\t"),  "");
						nextLine = nextLine.replaceAll(("\""),  "");
						//then, put the attribute name and value in our hashmap and repeat this while loop for the next attribute
						attributes.put(attributeName, nextLine);
						reader.close();
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return attributes;	
	}
	
	
	/**
	* Converts weapon script file into a HashMap of weapon attributes/values MORE EFFICIENTLY, I HOPE
	*/
	private HashMap<String, String> getWeaponScriptFileAsMapV2(String weaponName) throws FileNotFoundException {
	
		//get a list of all Weapon-class attributes
		ArrayList<String> attributeNames = new ArrayList<String>();
		for(Field f : Weapon.class.getDeclaredFields()) {
			f.setAccessible(true); //field must be accessible or it won't respond to getName()
			attributeNames.add(f.getName());
		}
			
		//the hashmap we'll return. It will be filled in the format (attributeName, value)
		HashMap<String,String> attributes = new HashMap<String,String>();					
		BufferedReader reader = new BufferedReader(new FileReader(new File(this.getClass().getResource(pathWeaponScriptFiles +"weapon_"+ weaponName +".txt").getFile())));
		
		
		try { 
			String nextLine = "";
			//while-loop through weapon script file. if line contains one of the Weapon attributes, put it in hashmap
			attributes.put("WeaponName", weaponName);
			while((nextLine = reader.readLine()) != null) {
				for(String attribute : attributeNames) {		
					//if the current attribute name occurs in a line, clear everything in that line so that only the attribute value is left
					if(nextLine.contains(attribute)) {
						nextLine = nextLine.replaceAll(attribute, ""); 		
						nextLine = nextLine.replaceAll(("\\s+"), "");
						nextLine = nextLine.replaceAll(("\\t"),  "");
						nextLine = nextLine.replaceAll(("\""),  "");
						//then, put the attribute name and value in our hashmap and repeat this while loop for the next attribute
						attributes.put(attribute, nextLine);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return attributes;	
	}
	
	
	/**
	 * List with names of all weapons in the library
	 * @return
	 */
	public ArrayList<String> getWeaponNames() {
		return weaponNames;
	}
	
	/**
	 * List with all weapon objects in the library
	 * @return
	 */
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}
	
	/**
	 * HashMap that maps names of cataloged weapons to corresponding weapon objects.
	 * @return
	 */
	public HashMap<String, Weapon> getWeaponsAsMap() {
		HashMap<String, Weapon> libraryMap = new HashMap<String, Weapon>();
		for(Weapon w : weapons) {
			libraryMap.put(w.getWeaponName(), w);
		}
		return libraryMap;
	}
	
}
