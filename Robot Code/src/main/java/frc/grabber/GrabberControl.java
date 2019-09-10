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

public class GrabberControl implements LoopModule {
    public ShuffleboardTab debugTab = Shuffleboard.getTab("Debug");
    private NetworkTableEntry conorMode = debugTab.addPersistent("Driver Hatch Intake Control", false).getEntry();
    private NetworkTableEntry cargoToggle = debugTab.addPersistent("Driver Cargo Intake Control", false).getEntry();
    private NetworkTableEntry cargoTriggerSpitterToggle = debugTab.addPersistent("Driver Cargo Trigger Control", false).getEntry();
    private final Grabber grabber;
    private final JoystickController Joy;
    private final ButtonPanel panel;
    private final XBoxController Xbox;

    public boolean hasHatch, claws;
    private boolean guideOn;
    private boolean pokersOut;
    private double lastTime, inspeed;
    private int lastButton;
    
    public GrabberControl(Grabber grabber, JoystickController Joy, ButtonPanel panel, XBoxController Xbox) {
        this.grabber = grabber;
        this.Joy = Joy;
        this.panel = panel;
        this.Xbox = Xbox;
    }

    @Override
    public void init() {
        
        grabber.setIntake(0);
        inspeed = 0;
        pokersOut = false;
        claws = false;
        lastTime = System.currentTimeMillis();
        this.lastButton = -1;

        guideOn = false;
    }

    @Override
    public void update(long delta) {
        this.lastButton = panel.lastButton;
        // Cargo: Joystick
        if (Joy.hatUp()||(Xbox.getStickRY()>0.3&&cargoToggle.getBoolean(false))||(Xbox.getLTrigger()>0.1&&cargoTriggerSpitterToggle.getBoolean(false))) { //0.3 seems like a good number, have to intentionally push the stick a ways
            if(lastButton == 4){ //longboye line ^
                inspeed = .5;
            }
            else{
                inspeed = 1;
            }
        } 
        else if (Joy.hatDown()||(Xbox.getStickRY()<-0.3&&cargoToggle.getBoolean(false))) { //flip the stick measurements if backwards
            inspeed = -1;
        } 
        else if (Joy.getHat() == -1){
            inspeed = 0;
        }

        if (lastButton == 6){
            grabber.setIntake(-inspeed);
        }
        else if (lastButton != 6){
            grabber.setIntake(inspeed);
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
            if (Joy.getButtonDown(1)||(Xbox.getRTrigger()>0.05&&conorMode.getBoolean(false))) { //added code to allow driver control of grabber as an experiment to see if i can push cycle time down/personal taste

                if (!hasHatch) {
                    grabber.setGrabber(true);
                    grabber.setPokers(false);
                    hasHatch = true;
                } else {
                    //grabber.setGrabber(false);
                    grabber.setPokers(true);
                    pokersOut = true;
                    lastTime = System.currentTimeMillis();
                }

                SmartDashboard.putBoolean("Hatch", hasHatch);
            }
            //change this value for new hatch claws
            else if (System.currentTimeMillis() > lastTime + 300 && pokersOut) {
                grabber.setGrabber(false);
                grabber.setPokers(false);
                pokersOut = false;
                hasHatch = false;
                lastTime = System.currentTimeMillis();
            }
            //change this value for new hatch claws
            else if (System.currentTimeMillis() > lastTime + 22 && pokersOut) {
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
