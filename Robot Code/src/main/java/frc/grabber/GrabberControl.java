package frc.grabber;

import frc.grabber.Grabber;
import frc.interfaces.LoopModule;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;

public class GrabberControl implements LoopModule {
    private final Grabber grabber;
    private final JoystickController Joy;
    private final ButtonPanel panel;

    public boolean hasHatch, claws;
    private boolean guideOn;
    private boolean pokersOut;
    private double lastTime, inspeed;
    private int lastButton;
    
    public GrabberControl(Grabber grabber, JoystickController Joy, ButtonPanel panel) {
        this.grabber = grabber;
        this.Joy = Joy;
        this.panel = panel;
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
        if (Joy.hatUp()) {
            if(lastButton == 4){
                inspeed = .5;
            }
            else{
                inspeed = 1;
            }
        } 
        else if (Joy.hatDown()) {
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
        if(lastButton != 6){
            if (Joy.getButtonDown(1)) {

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
