package frc.lift;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.lift.Lift;
import frc.interfaces.LoopModule;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.Servo;

public class LiftControl{
    private final Lift lift;
    private final ButtonPanel panel;
    private boolean LocksOn;
    private Servo clutch;

    public LiftControl(Lift lift, ButtonPanel panel){
        this.lift = lift;
        this.panel = panel;
        clutch = new Servo(RobotMap.clutchServoThird);
    }

    public void init(){
        LocksOn = false;
        lift.setClutch(60);
        clutch.setAngle(60);
    }
    public void liftUp(){
        lift.winchUp();
        if(lift.liftLimitSwitch.get()){
            lift.winchHover();
        }
    }
    public void liftDown(){
        lift.winchDown();
    }
    @Override
    public void update(long delta){

        if(panel.getButtonDown(10)){
            liftDown();
            lift.clutchOn();
            //toggle the arm clutch servo to drop the ratchet bar^
            panel.lastButton = 10;
        }
        else if(panel.getButtonUp(10)){
            lift.winchNoMove();
            
        }
        if(panel.getButtonDown(12)){
            liftUp();
            panel.lastButton = 12;
        }
        else if (panel.getButtonUp(12)){
            lift.winchNoMove();
        }
        if(panel.getButtonDown(11)){
            lift.setLiftDriveFWD();
            LocksOn = true;
            panel.lastButton = 11;
        }
        else if(panel.getButtonUp(11)){
            lift.setLiftDriveOff();
        }
        if (panel.getButtonDown(13)){
            lift.setLiftDriveBWD();
            panel.lastButton = 13;
        }
        else if (panel.getButtonUp(13)){
            lift.setLiftDriveOff();
        }
        if(LocksOn){
            lift.locksOn();
            LocksOn = false;
        }
        //turn on pistons that engage the one way wheel jacks for climb

        // System.out.println("Servo : " + lift.getClutchPos());
    }
}