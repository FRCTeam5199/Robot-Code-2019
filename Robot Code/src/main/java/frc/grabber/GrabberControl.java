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

    private boolean hasHatch, claws;
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
        grabber.setGrabber(true);
        grabber.setIntake(0);
        hasHatch = false;
        pokersOut = false;
        claws = false;
        lastTime = System.currentTimeMillis();
        this.lastButton = -1;
        inspeed = 0;
        
        guideOn = false;
    }

/*     private boolean groundPickupActive() {
        return false;
    }
    //this should only be true for the hatch intake button */

    @Override
    public void update(long delta) {
        this.lastButton = panel.lastButton;
        // Cargo: Joystick
        if (Joy.hatUp()) {
            if(lastButton == 2){
                inspeed = -.5;
            }
            else{
                inspeed = -1;
            }
        } 
        else if (Joy.hatDown()) {
            inspeed = 1;
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
        //debug v
       /*  if(Joy.hatUp()){
            grabber.setIntake(-1);
        }

        else if (Joy.hatDown()){
            grabber.setIntake(1);
        }
        else{
            grabber.setIntake(0);
        } */

        //idek what this does v
        /*
         * if (Joy.getHat() != -1){ if (Joy.getHat() == 180){ grabber.setIntake(1); }
         * else if (Joy.getHat() == 0){ grabber.setIntake(-1); } } else if (Joy.getHat()
         * == -1){ grabber.setIntake(0); }
         */

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

            else if (System.currentTimeMillis() > lastTime + 1000 && pokersOut) {
                grabber.setGrabber(false);
                grabber.setPokers(false);
                pokersOut = false;
                hasHatch = false;
                lastTime = System.currentTimeMillis();
            }
            else if (System.currentTimeMillis() > lastTime + 500 && pokersOut) {
                grabber.setGrabber(false);
                grabber.setPokers(true);
                pokersOut = true;
            }
        }
        else if (lastButton == 6){
            if (Joy.getButtonDown(1)) { claws = !claws; }
            grabber.setGrabber(claws);
        }

        
        if(panel.getButtonDown(4)){ guideOn = !guideOn; }
        grabber.setHatchGuide(guideOn);
        //for the hatch guide testing^, needs to be setup with bools from 'lastbutton';

    }
}
