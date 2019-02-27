package frc.grabber;

import frc.grabber.Grabber;
import frc.interfaces.LoopModule;

import javax.swing.text.StyleContext.SmallAttributeSet;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;

public class GrabberControl implements LoopModule {
    private final Grabber grabber;
    private final JoystickController Joy;
    private final ButtonPanel panel;

    private boolean hasHatch;
    private double lastTime;
    private int lastButton;

    public GrabberControl(Grabber grabber, JoystickController Joy, ButtonPanel panel) {
        this.grabber = grabber;
        this.Joy = Joy;
        this.panel = panel;
    }

    @Override
    public void init() {
        hasHatch = false;
        lastTime = System.currentTimeMillis();
        lastButton = -1;
    }

    private boolean groundPickupActive () {
        
        if (lastButton == 3 || lastButton == 6) {
            return true;
        }
        return false;
    }

    @Override
    public void update(long delta) {
        
        // Cargo: Joystick
        if (Joy.getHat() != -1){
            if (Joy.getHat() == 180){
                grabber.setIntake(1);
            }
            else if (Joy.getHat() == 0){
                grabber.setIntake(-1);
            }
        }
        else if (Joy.getHat() == -1){
            grabber.setIntake(0);
        }

        // Hatch: Trigger
        if (Joy.getButtonDown(1)){

            // Ground pickup
            if (groundPickupActive()) {

                // Error correction when operator misses hatch
                if (hasHatch) {
                    grabber.setGrabber(false);
                } else {
                    grabber.setGrabber(true);
                }

            }
            // Non-ground pickup
            else {
                // Deploy hatch
                if (hasHatch) {
                    grabber.setPokers(true);
                    grabber.setGrabber(false);
                
                    if (lastTime > System.currentTimeMillis() + 250) {
                        grabber.setPokers(false);
                        hasHatch = false;
                    }
                // Collect hatch
                } else {
                    grabber.setGrabber(true);
                    hasHatch = true;
                }
                
            }
            SmartDashboard.putBoolean("Hatch", hasHatch);
        }
            
    }
}
