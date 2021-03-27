import java.util.HashMap;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import labels.*;

//To do:
//DESIGN:
// [ ] Rework background/color scheme, it's looking too bland
// [ ] Remove window border, manually add exit button
// [ ] Display Base Damage in base stats paragraph
// [ ] Move Drop Down menu so it doesn't obstruct hitboxes for easier comparison

//FUNCTIONALITY:
// [ ] Allow comparison between two weapons somehow (atm: user has to open two windows)
// [ ] Allow toggle to display hint box (= toggle window resize so hint box becomes invisible)
// [ ] Add Bullet Count display (only shows if weapon is shotgun)
// [ ] Tickable box for unit conversions (inches to meter or yards)

public class JavaFXFrontend extends Application {
	

	private String pathWeaponScriptFiles = "data/scripts140210/";
	private String pathWeaponImportFile = "data/weaponsToConsider.txt";
	
//	private ArrayList<String> weaponLibrary.getWeaponNames() = new ArrayList<String>(Arrays.asList("ak47", "aug", "awp", "bizon", "cz75a", "deagle", "elite", "famas", "fiveseven", "g3sg1", "galilar",
//																		"glock", "hkp2000", "m4a1", "m4a1_silencer", "m249", "mac10", "mag7", "mp7", "mp9", "negev", "nova", "p90",
//																		"p250", "sawedoff", "scar20", "sg556", "ssg08", "tec9", "ump45", "usp_silencer", "xm1014"));
	
	
	private WeaponLibrary weaponLibrary = new WeaponLibrary(pathWeaponImportFile, pathWeaponScriptFiles);
	private HashMap<String, Weapon> weaponLibraryMap = weaponLibrary.getWeaponsAsMap();
	
	//This next objects methods will return a specific weapon attribute as String.
	private WeaponAnalyzer weaponAnalyzer = new WeaponAnalyzer();
	//
	private AttributeExplanations attributeExplanations = new AttributeExplanations();
	
	
	//Hitbox image and background objects.
	private final ImageView imageBackground = new ImageView();
	private final ImageView imageViewBackground = new ImageView();
	private final ImageView imageViewHead = new ImageView();
	private final ImageView imageViewChest = new ImageView();
	private final ImageView imageViewStomach = new ImageView();
	private final ImageView imageViewLegs = new ImageView();
	
	
	
	//Color tones used to change hit-box colors
	private ColorAdjust colorAdjustDamageLv0 = new ColorAdjust();
    private ColorAdjust colorAdjustDamageLv1 = new ColorAdjust();
    private ColorAdjust colorAdjustDamageLv2 = new ColorAdjust();
    private ColorAdjust colorAdjustDamageLv3 = new ColorAdjust();
    private ColorAdjust colorAdjustDamageLv4 = new ColorAdjust();
    private ColorAdjust colorAdjustDamageLv5 = new ColorAdjust();
	
	private String activeWeapon = "";
	private boolean armorBoxTicked = false;
	private int activeRange = 1200;
	private boolean userHasSelectedWeaponBefore = false;
	
	private final Label labelHeadDamage = new HitboxLabel(" ");
	private final Label labelChestDamage = new HitboxLabel(" ");
	private final Label labelStomachDamage = new HitboxLabel(" ");
	private final Label labelLegDamage = new HitboxLabel(" ");
	
	private String headlineWaiting = " ... ";
	private String attributeWaiting = " ... ";
	
	
	private final HeadlineLabel labelStWeaponName = new HeadlineLabel(headlineWaiting);
	
	private final Label labelNmPrice = new AttributeLabel("   Price:");
	private final Label labelStPrice = new Label(attributeWaiting);
	private final Label labelNmRunspeed = new AttributeLabel("   Run Speed:");
	private final Label labelStRunspeed = new Label(attributeWaiting);
	private final Label labelNmArmorpen = new AttributeLabel("   Armor Tear:");
	private final Label labelStArmorpen = new Label(attributeWaiting);
	private final Label labelNmRangeFalloff = new AttributeLabel("   Damage Falloff:");
	private final Label labelStRangeFalloff = new Label(attributeWaiting);
	
	private final Label labelNmFirerate = new AttributeLabel("   Fire Rate:");
	private final Label labelStFirerate = new Label(attributeWaiting);
	private final Label labelNmShotinterval = new AttributeLabel("   Shot Interval:");
	private final Label labelStShotinterval = new Label(attributeWaiting);
	private final Label labelNmCapacity = new AttributeLabel("   Capacity:");
	private final Label labelStCapacity = new Label(attributeWaiting);
	private final Label labelNmEmptyAfter = new AttributeLabel("   Empty After:");
	private final Label labelStEmptyAfter = new Label(attributeWaiting);

	private final Label labelNmRecoilResetStand = new AttributeLabel("   Recovery Stand:");
	private final Label labelStRecoilResetStand = new Label(attributeWaiting);
	private final Label labelNmRecoilResetCrouch = new AttributeLabel("   Recovery Crouch:");
	private final Label labelStRecoilResetCrouch = new Label(attributeWaiting);
	private final Label labelNmPlayerpen = new AttributeLabel("   Max Players Hit:");
	private final Label labelStPlayerpen = new Label(attributeWaiting);
	
	private final Label labelNmOHKArm = new AttributeLabel("   Oneshot vs Armor:");
	private final Label labelStOHKArm = new Label(attributeWaiting);
	private final Label labelNmOHKUnarm = new AttributeLabel("   Oneshot vs Eco:");
	private final Label labelStOHKUnarm = new Label(attributeWaiting);
	private final Label labelNm3SKUnarm = new AttributeLabel("   3-Shot Range vs Eco:");
	private final Label labelSt3SKUnarm = new Label(attributeWaiting);
	private final Label labelNm4SKArm = new AttributeLabel("   4-Shot Range vs Armor:");
	private final Label labelSt4SKArm = new Label(attributeWaiting);
	private final Label labelNm5SKArm = new AttributeLabel("   5-Shot Range vs Armor:");
	private final Label labelSt5SKArm = new Label(attributeWaiting);
	
	private final Label labelNmDisplayRangeHint = new Label("                               [?] Display Range Hint");


	
	private final Label label_explanation = new HelperLabel("Select a Weapon from the drop-down menu.\n\nAfterwards, hover your cursor over an attribute to display an explanation here.");
	
	
	@Override
    public void start(final Stage primaryStage) {
		primaryStage.setTitle("aciD's Weapon Library");
		setImages();
	    defineColors();
	    setFonts();

      //--------------------------
    	GridPane weaponSelectionPanel = getWeaponSelectionPanel();
    	    		
    	GridPane hitboxPane = new GridPane();
    		hitboxPane.setMinWidth(226);
    		hitboxPane.setVgap(8);
	    	hitboxPane.add(getHitboxPane(), 0, 0);
	    	hitboxPane.add(getSliderPane(), 0, 1);
	    	hitboxPane.setPadding(new Insets(12,0,0,24));

		GridPane statAnalysisPane = getStatAnalysisPane();
			statAnalysisPane.setMinWidth(292);
			statAnalysisPane.setPadding(new Insets(16,0,0,16));
	    	
		GridPane explanationPane = getExplanationPane();
			explanationPane.setMinWidth(220);
			explanationPane.setMaxWidth(220);
			explanationPane.setPadding(new Insets(8,8,8,8));
			
	    
	  //----- ASSEMBLE PANES, SETTING SCENE -----------------
    	GridPane mainGrid = new GridPane();
    		
    		mainGrid.add(weaponSelectionPanel,	 0, 0, 3, 1);
	    	mainGrid.add(hitboxPane,			 0, 1, 1, 1);
	    	mainGrid.add(statAnalysisPane,		 1, 1, 1, 1);
	    	mainGrid.add(explanationPane,		 2, 1, 1, 1);    	
	        mainGrid.setPadding(new Insets(14,0,0,22));
	        mainGrid.setVgap(16);
	        mainGrid.setHgap(10);
	    	//mainGrid.setGridLinesVisible(true);    	
	        	        
	  //----- GLOBAL PANE TO PLUG ASSEMBLER PANE INTO ----------
	    GridPane globalGrid = new GridPane();
	    	globalGrid.add(imageBackground, 0, 0);
	    	globalGrid.add(mainGrid, 0, 0);
   	
	   
    	Scene mainScene = new Scene(globalGrid, 780, 560); //was 800x580
    	mainScene.getStylesheets().add("data/css/style.css");
    	
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false); //TODO set to false before shipping
        primaryStage.initStyle(StageStyle.DECORATED);
        System.getProperty("java.class.path");
        primaryStage.show();
    }
    
    
	private void updateHitboxes() {
		if(weaponLibraryMap.containsKey(activeWeapon)) {
			Weapon wep = weaponLibraryMap.get(activeWeapon);
			
			int headDamage = weaponAnalyzer.getShotDamage(wep, 4, activeRange, armorBoxTicked);
			assignColorToHitbox(imageViewHead, headDamage);
			labelHeadDamage.setText("        "+headDamage);
			
			int chestDamage = weaponAnalyzer.getShotDamage(wep, 1, activeRange, armorBoxTicked);
			assignColorToHitbox(imageViewChest, chestDamage);
			labelChestDamage.setText("           "+chestDamage);
			
			int stomachDamage = weaponAnalyzer.getShotDamage(wep, 1.25f, activeRange, armorBoxTicked);
			assignColorToHitbox(imageViewStomach, stomachDamage);
			labelStomachDamage.setText("            "+stomachDamage);
			
			int legDamage = weaponAnalyzer.getShotDamage(wep, 0.75f, activeRange, false); //false because legs never have protection
			assignColorToHitbox(imageViewLegs, legDamage);
			labelLegDamage.setText("       "+legDamage);
		} else {
			System.err.println(this+".updateHitboxes() could not find a weapon named: \""+activeWeapon+"\" in the interface's assigned weaponLibrary.");
		}
	}
	
	private void assignColorToHitbox(ImageView hitbox, int damage) {
		if(damage >= 100)
			hitbox.setEffect(colorAdjustDamageLv5);
		else if(damage >= 50) 
			hitbox.setEffect(colorAdjustDamageLv4);
		else if(damage >= 34) 
			hitbox.setEffect(colorAdjustDamageLv3);
		else if(damage >= 25) 
			hitbox.setEffect(colorAdjustDamageLv2);
		else if(damage >= 20) 
			hitbox.setEffect(colorAdjustDamageLv1);
		else
			hitbox.setEffect(colorAdjustDamageLv0);
	}
	
	private void updateCentralPaneInfo() {
		if(weaponLibraryMap.containsKey(activeWeapon)) {
			Weapon wep = weaponLibraryMap.get(activeWeapon);
			
			labelStWeaponName.setText(activeWeapon.toUpperCase());
			
			labelStPrice.setText(weaponAnalyzer.getPrice(wep));
			labelStRunspeed.setText(weaponAnalyzer.getRunSpeed(wep));
			labelStArmorpen.setText(weaponAnalyzer.getArmorPenetration(wep));
			labelStRangeFalloff.setText(weaponAnalyzer.getFalloff(wep));
			
			labelStShotinterval.setText(weaponAnalyzer.getShotInterval(wep));
			labelStFirerate.setText(weaponAnalyzer.getFireRate(wep));
			labelStCapacity.setText(weaponAnalyzer.getCapacity(wep));
			labelStEmptyAfter.setText(weaponAnalyzer.getEmptyAfter(wep));
			
			labelStPlayerpen.setText(weaponAnalyzer.getPlayerPenetration(wep));
			
			
			labelStRecoilResetStand.setText(weaponAnalyzer.getRecoilResetStand(wep));
			labelStRecoilResetCrouch.setText(weaponAnalyzer.getRecoilResetCrouch(wep));
			
			labelStOHKUnarm.setText(weaponAnalyzer.getKillRange(wep, false, 1, 4.0f));
			labelStOHKArm.setText(weaponAnalyzer.getKillRange(wep, true, 1, 4.0f));
			labelSt3SKUnarm.setText(weaponAnalyzer.getKillRange(wep, false, 3, 1.00f));
			labelSt4SKArm.setText(weaponAnalyzer.getKillRange(wep, true, 4, 1.00f));
			labelSt5SKArm.setText(weaponAnalyzer.getKillRange(wep, true, 5, 1.00f));
		}
	}
	
	
    private void setImages() {
    	//Was:
    	//imageBackground.setImage(new Image(new File("PATH").toURI().toString()));
    	imageBackground.setImage(new Image(this.getClass().getResource("data/img/global_background2.png").toString()));
    	imageViewBackground.setImage(new Image(this.getClass().getResource("data/img/target_background2.png").toString()));
    	
    	imageViewHead.setImage(new Image(this.getClass().getResource("data/img/target_head2.png").toString()));
    	imageViewChest.setImage(new Image(this.getClass().getResource("data/img/target_chest3.png").toString()));
    	imageViewStomach.setImage(new Image(this.getClass().getResource("data/img/target_stomach3.png").toString()));
    	imageViewLegs.setImage(new Image(this.getClass().getResource("data/img/target_legs2.png").toString()));
    	imageViewHead.setOpacity(0.5d);
    	imageViewChest.setOpacity(0.5d);
    	imageViewStomach.setOpacity(0.5d);
    	imageViewLegs.setOpacity(0.5d);      
    }
		
	private void defineColors() {
        colorAdjustDamageLv5.setHue(0.08);
        colorAdjustDamageLv5.setSaturation(0.82);
        colorAdjustDamageLv5.setBrightness(0.0);      

        colorAdjustDamageLv4.setHue(0.08);
        colorAdjustDamageLv4.setSaturation(.64);
        colorAdjustDamageLv4.setBrightness(.2);    

        colorAdjustDamageLv3.setHue(0.08);
        colorAdjustDamageLv3.setSaturation(.54);
        colorAdjustDamageLv3.setBrightness(.4);
        
        colorAdjustDamageLv2.setHue(0.08);
        colorAdjustDamageLv2.setSaturation(.34);
        colorAdjustDamageLv2.setBrightness(.6);
        
        colorAdjustDamageLv1.setHue(0.08);
        colorAdjustDamageLv1.setSaturation(.22);
        colorAdjustDamageLv1.setBrightness(.8); 
        
        colorAdjustDamageLv0.setHue(0.08);
        colorAdjustDamageLv0.setSaturation(.10);
        colorAdjustDamageLv0.setBrightness(1);
	}
	
	private void setFonts() {	
		Font damageFont = new Font("Tahoma", 24);	
		labelHeadDamage.setFont(damageFont);
		labelChestDamage.setFont(damageFont);
		labelStomachDamage.setFont(damageFont);
		labelLegDamage.setFont(damageFont);
	}
	
	private void setUpExplanationEvents() {
		labelNmPrice.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getPrice());
			}});
		labelNmRunspeed.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getRunspeed());
			}});
		labelNmArmorpen.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getArmorpen());
			}});
		labelNmRangeFalloff.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getRangeFalloff());
			}});

		
		labelNmFirerate.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getFirerate());
			}});
		labelNmShotinterval.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getShotinterval());
			}});
		labelNmCapacity.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getCapacity());
			}});
		labelNmEmptyAfter.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getEmptyAfter());
			}});
		
		
		labelNmRecoilResetStand.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getRecoilResetStand());
			}});
		labelNmRecoilResetCrouch.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getRecoilResetCrouch());
			}});
		labelNmPlayerpen.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getPlayerpen());
			}});

		
		labelNmOHKUnarm.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getOHKUnarm());
			}});
		labelNmOHKArm.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getOHKArm());
			}});

		labelNm3SKUnarm.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.get3SKUnarm());
			}});
		labelNm4SKArm.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.get4SKArm());
			}});
		labelNm5SKArm.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.get5SKArm());
			}});
	
		labelNmDisplayRangeHint.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				label_explanation.setText(attributeExplanations.getDisplayRangeHint());
			}});	
	}
	
	
	private GridPane getWeaponSelectionPanel() {
		GridPane weaponSelectionPanel = new GridPane();
    	
	    final ComboBox<String> weaponComboBox = new ComboBox<String>();
	    for(String s: weaponLibrary.getWeaponNames()) {
	    	weaponComboBox.getItems().add(s);
	    }
	    //weaponComboBox.getItems().addAll(weaponLibrary.getWeaponNames());
        weaponComboBox.setPromptText("Select a Weapon!");
        weaponComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setActiveWeapon(newValue);
				if(!userHasSelectedWeaponBefore) {
					setUpExplanationEvents();
					label_explanation.setText("Hover your cursor over an attribute to display an explanation here.");
					userHasSelectedWeaponBefore = true;
				}
				//TODO what happens if user selects a valid weapon from dropdown menu
				}
			});
        weaponComboBox.setMinWidth(130);
        weaponSelectionPanel.add(weaponComboBox, 0, 0);
        return weaponSelectionPanel;
	}
	
	private GridPane getHitboxPane() {
		
	GridPane hitboxPane = new GridPane();
	//Target Image and Hitboxes
        GridPane hitboxImagesPane = new GridPane();   
        hitboxImagesPane.add(imageViewBackground,0,0);
        hitboxImagesPane.add(imageViewHead,0,0);
        hitboxImagesPane.add(imageViewChest,0,0);
        hitboxImagesPane.add(imageViewStomach,0,0);
        hitboxImagesPane.add(imageViewLegs,0,0);
        
    //Target Damage Numbers
	    GridPane hitboxImagesOverlayPane = new GridPane();
    	hitboxImagesOverlayPane.setPadding(new Insets(0,0,0,0));
    	hitboxImagesOverlayPane.add(labelHeadDamage,0,1);
    	hitboxImagesOverlayPane.add(labelChestDamage,0,3);
    	hitboxImagesOverlayPane.add(labelStomachDamage,0,5);
    	hitboxImagesOverlayPane.add(labelLegDamage,0,7);
    	hitboxImagesOverlayPane.setVgap(20);
    	
    	hitboxPane.add(hitboxImagesPane, 0, 0);
    	hitboxPane.add(hitboxImagesOverlayPane, 0, 0);
    	
    	hitboxPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				//just to make sure helper text doesnt get replaced by hitbox stuff before user has selected weapon
				if(userHasSelectedWeaponBefore)
					label_explanation.setText(attributeExplanations.getHitbox());
			}});
    	
    	return hitboxPane;
	}
	
	private GridPane getSliderPane() {
	
	//Checkbox for Armor
		GridPane armorPane = new GridPane();
		Label armorLabel = new Label("Armor");
	    CheckBox armorCheckbox = new CheckBox("");
	    armorCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
	        	armorBoxTicked = new_val;
	        	updateHitboxes();
	        }
	    });
	    armorPane.add(armorCheckbox, 0, 0);
	    armorPane.add(armorLabel, 0, 1);
	    
	//Slider 
	    final Slider rangeSlider = new Slider(16, 3072, activeRange);
	    rangeSlider.setMajorTickUnit(250);
	    rangeSlider.setMinorTickCount(0);
	    rangeSlider.setShowTickMarks(false);
	    rangeSlider.setShowTickLabels(false);
	    rangeSlider.setSnapToTicks(false);
	    rangeSlider.setBlockIncrement(5);
	    //rangeSlider.setPrefWidth(320);
	    
	//Range Label
	    final Label currentRange = new Label(activeRange+" inches");
	    rangeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	//update hitboxes by using new_val
                currentRange.setText(String.format("%.0f", new_val)+" inches");
                activeRange = new_val.intValue();
                updateHitboxes();
            }
        });
    
	//Configure Slider Pane
	    GridPane sliderPane = new GridPane();
	    sliderPane.add(rangeSlider, 0, 0);
	    sliderPane.add(armorPane, 1, 0, 1, 2);
	    sliderPane.add(currentRange, 0, 1);
	    sliderPane.setHgap(6);
	    
	    return sliderPane;
	}

	private GridPane getStatAnalysisPane() {

		int vgapOuter = 9;
		int vgapInner = 2;
		int hgapInner = 6;

		GridPane headLineState = new GridPane();
			headLineState.add(labelStWeaponName, 0, 0);
		
		GridPane baseStats = new GridPane();
		baseStats.setVgap(vgapInner);
		baseStats.setHgap(hgapInner);
			baseStats.add(labelNmPrice, 0, 0);
			baseStats.add(labelStPrice, 1, 0);
			
			baseStats.add(labelNmRunspeed, 0, 1);
			baseStats.add(labelStRunspeed, 1, 1);
			
			baseStats.add(labelNmRangeFalloff, 0, 2);
			baseStats.add(labelStRangeFalloff, 1, 2);
			
			baseStats.add(labelNmArmorpen, 0, 3);
			baseStats.add(labelStArmorpen, 1, 3);
			
			
		GridPane shotSpeedStats = new GridPane();
		shotSpeedStats.setVgap(vgapInner);
		shotSpeedStats.setHgap(hgapInner);
		
			shotSpeedStats.add(labelNmFirerate, 0, 0);
			shotSpeedStats.add(labelStFirerate, 1, 0);
		
			shotSpeedStats.add(labelNmShotinterval, 0, 1);
			shotSpeedStats.add(labelStShotinterval, 1, 1);
			
			shotSpeedStats.add(labelNmCapacity, 0, 2);
			shotSpeedStats.add(labelStCapacity, 1, 2);
			
			shotSpeedStats.add(labelNmEmptyAfter, 0, 3);
			shotSpeedStats.add(labelStEmptyAfter, 1, 3);
			
					
		GridPane recoilStats = new GridPane();
		recoilStats.setVgap(vgapInner);
		recoilStats.setHgap(hgapInner);
			recoilStats.add(labelNmRecoilResetStand, 0, 0);
			recoilStats.add(labelStRecoilResetStand, 1, 0);
			
			recoilStats.add(labelNmRecoilResetCrouch, 0, 1);
			recoilStats.add(labelStRecoilResetCrouch, 1, 1);
			
			recoilStats.add(labelNmPlayerpen, 0, 2);
			recoilStats.add(labelStPlayerpen, 1, 2);
				
		
		GridPane oneShotKillStats = new GridPane();
		oneShotKillStats.setVgap(vgapInner);
		oneShotKillStats.setHgap(hgapInner);
					
			oneShotKillStats.add(labelNmOHKArm, 0, 0);
			oneShotKillStats.add(labelStOHKArm, 1, 0);
					
			oneShotKillStats.add(labelNmOHKUnarm, 0, 1);
			oneShotKillStats.add(labelStOHKUnarm, 1, 1);
			

	
		GridPane multiShotKillStats = new GridPane();
			multiShotKillStats.setVgap(vgapInner);
			multiShotKillStats.setHgap(hgapInner);
			
			multiShotKillStats.add(labelNm3SKUnarm, 0, 0);
			multiShotKillStats.add(labelSt3SKUnarm, 1, 0);
			
			multiShotKillStats.add(labelNm4SKArm, 0, 1);
			multiShotKillStats.add(labelSt4SKArm, 1, 1);
			
			multiShotKillStats.add(labelNm5SKArm, 0, 2);
			multiShotKillStats.add(labelSt5SKArm, 1, 2);
			
			
		
		GridPane statAnalysisPane = new GridPane();
		statAnalysisPane.setVgap(vgapOuter);
			statAnalysisPane.add(headLineState, 0, 0);
			statAnalysisPane.add(baseStats, 0, 2);
			statAnalysisPane.add(shotSpeedStats, 0, 4);
			statAnalysisPane.add(recoilStats, 0, 6);
			statAnalysisPane.add(oneShotKillStats, 0, 9);
			statAnalysisPane.add(multiShotKillStats, 0, 10);
			statAnalysisPane.add(labelNmDisplayRangeHint, 0, 12);
			
		return statAnalysisPane;
	}
	
	private GridPane getExplanationPane() {
	    GridPane explanationPane = new GridPane();
    	explanationPane.add(label_explanation, 0, 0);
    	label_explanation.setMaxWidth(240);
    	label_explanation.setWrapText(true);
    	return explanationPane;
	}

	
	private void setActiveWeapon(String weaponName) {
		activeWeapon=weaponName;
		updateHitboxes();
		updateCentralPaneInfo();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
    
	
}