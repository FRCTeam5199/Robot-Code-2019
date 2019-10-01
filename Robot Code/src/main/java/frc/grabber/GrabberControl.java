package frc.grabber;

import frc.grabber.Grabber;
import frc.interfaces.LoopModule;
//import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;
import frc.controllers.XBoxController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.shuffleboard.*;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.DigitalInput;

public class GrabberControl{
    public ShuffleboardTab debugTab2 = Shuffleboard.getTab("Driver Control Options");
    private NetworkTableEntry conorMode = debugTab2.addPersistent("Driver Hatch Intake Control", false).getEntry(); //allow driver hatch control
    private NetworkTableEntry cargoToggle = debugTab2.addPersistent("Driver Cargo Intake Control", false).getEntry(); //allow driver full cargo control
    private NetworkTableEntry cargoTriggerSpitterToggle = debugTab2.addPersistent("Driver Cargo Trigger Control", false).getEntry(); //allow driver cargo outtake control
    private NetworkTableEntry hatchBooperTiming = debugTab2.addPersistent("Poker Delay", 22).getEntry(); //the small value
    private NetworkTableEntry hatchBooperTimingB = debugTab2.addPersistent("Poker Delay B", 300).getEntry(); //the big value
    private NetworkTableEntry cargoCompensator = debugTab2.addPersistent("Passive Cargo Intake Speed", 0).getEntry();
    private NetworkTableEntry fullDriverControl = debugTab2.addPersistent("Single Controller Mode", false).getEntry();
    private final Grabber grabber;
    private final JoystickController Joy;
    private final ButtonPanel panel;
    private final XBoxController controller;

    public boolean hasHatch, claws;
    private boolean guideOn;
    private boolean pokersOut;
    private double lastTime, inspeed;
    private int lastButton;
    private Servo clutch;
    private DigitalInput limitSwitch;
    private boolean hasSwitch;
    private boolean triggerFlag;
    
    public GrabberControl(Grabber grabber, JoystickController Joy, ButtonPanel panel, XBoxController controller) {
        this.grabber = grabber;
        this.Joy = Joy;
        this.panel = panel;
        this.controller = controller;
        clutch = new Servo(RobotMap.clutchServoTest);
        limitSwitch = new DigitalInput(9);
    }

    //@Override
    public void init() {
        
        grabber.setIntake(0);
        inspeed = 0;
        pokersOut = false;
        claws = false;
        lastTime = System.currentTimeMillis();
        this.lastButton = -1;

        guideOn = false;
    }

    private boolean getSwitchMomentary(){ //gets the limit switch as a blip boolean 
        boolean returnBool = false;
		if(limitSwitch.get()&&!triggerFlag){
			triggerFlag = true;
			returnBool = true;
		}
		else if(!limitSwitch.get()&&triggerFlag){
			triggerFlag = false;
		}
		else{
			returnBool = false;
		}
		return returnBool;
    }

    //@Override
    public void update() {
        this.lastButton = panel.lastButton;
        boolean driverHatch = conorMode.getBoolean(false);
        boolean driverCargo = cargoToggle.getBoolean(false);
        boolean driverControl = fullDriverControl.getBoolean(false);
        double hatchBooperTime = hatchBooperTiming.getDouble(22);
        double hatchBooperTimeB = hatchBooperTimingB.getDouble(300);
        //SmartDashboard.putBoolean("hatchMode?", driverHatch);
        //SmartDashboard.putBoolean("cargoMode?", driverCargo);
        //SmartDashboard.putNumber("timing A", hatchBooperTime);
        //SmartDashboard.putNumber("timing B", hatchBooperTimeB);
        double cargoCompensation = cargoCompensator.getDouble(0);
        controller.setTriggerSensitivity(0.02);
        //SmartDashboard.putBoolean("joy trigger", Joy.getButtonDown(1));
        //SmartDashboard.putBoolean("xbox trigger", (controller.getRTrigger()>0.05));
        // Cargo: Joystick
        if (Joy.hatUp()||((controller.getStickRY()>0.3)&&(driverCargo||driverControl))||(controller.getLTrigger()>0.1&&cargoTriggerSpitterToggle.getBoolean(false))) { //0.3 seems like a good number, have to intentionally push the stick a ways
            if(lastButton == 4){ //longboye line ^
                inspeed = .5;
            }
            else{
                inspeed = 1;
            }
        } 
        else if (Joy.hatDown()||((controller.getStickRY()<-0.3)&&(driverCargo||driverControl))) { //flip the stick measurements if backwards
            inspeed = -1;
        } 
        else if (Joy.getHat() == -1){
            inspeed = -cargoCompensation; //able to run inwards now
        }

        if (lastButton == 6){
            grabber.setIntake(-inspeed);
        }
        else if (lastButton != 6){
            grabber.setIntake(inspeed);
        }

        //code to grab hatch using limit switch without having the shoot hatch logic
        if(!hasSwitch&&getSwitchMomentary()){ 
            grabber.setGrabber(true);
            grabber.setPokers(false);
            hasHatch = true;
            hasSwitch = true;
        }


        /* //Cargo Intake when in manual override
        if(Joy.getButtonDown(5) || Joy.getButtonDown(6)){
            grabber.setIntake(1);
        }
        else if(Joy.getButtonDown(3) || Joy.getButtonDown(4)){
            grabber.setIntake(-1);
        }
        else if(Joy.getButtonUp(5) || Joy.getButtonUp(6) || Joy.getButtonUp(3) || Joy.getButtonUp(4)){
            grabber.setIntake(0);
        }
        // */

        // Hatch: Trigger
        if(true){ //yikes forget why i did this, something about intake position not letting us grab iirc
            if (Joy.getButtonDown(1)||(controller.getRTriggerMomentary()&&(driverHatch||driverControl))/*||getSwitchMomentary()*/) { //added code to allow driver control of grabber as an experiment to see if i can push cycle time down/personal taste
                                                                //lim switch junk, remove if stuff is screwed up ^^^
                if (!hasHatch) {
                    grabber.setGrabber(true);
                    grabber.setPokers(false);
                    hasHatch = true;
                    clutch.setAngle(60);
                    
                } else {
                    //grabber.setGrabber(false);
                    grabber.setPokers(true);
                    pokersOut = true;
                    lastTime = System.currentTimeMillis();
                    clutch.setAngle(0);
                }

                SmartDashboard.putBoolean("Hatch", hasHatch);
            }
            //change this value for new hatch claws
            else if (System.currentTimeMillis() > lastTime + hatchBooperTimeB && pokersOut) { //big value
                grabber.setGrabber(false);
                grabber.setPokers(false);
                pokersOut = false;
                hasHatch = false;
                lastTime = System.currentTimeMillis();
            }
            //change this value for new hatch claws
            else if (System.currentTimeMillis() > lastTime + hatchBooperTime && pokersOut) { //small value
                grabber.setGrabber(false);
                grabber.setPokers(true);
                pokersOut = true;
            }
        }
        /* else if (lastButton == 6){
            if (Joy.getButtonDown(1)) { claws = !claws; }
            grabber.setGrabber(claws);
        } */
        //for hatch ground pickup

        
        /* if(panel.getButtonDown(n)){ guideOn = !guideOn; }
        grabber.setHatchGuide(guideOn);
        //for the hatch guide testing^, needs to be setup with bools from 'lastbutton'; */

    }
}
