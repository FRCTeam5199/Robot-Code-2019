package frc.lift;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import java.awt.Button;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ControlType;
import frc.robot.RobotMap;
import frc.lift.Lift;
import frc.interfaces.LoopModule;

public class LiftControl implements LoopModule{
    private final Lift lift;
    private final ButtonPanel panel;
    private boolean winchUpButton = false;
    private boolean winchDownButton = false;
    private boolean liftDriveFWD = false;
    private boolean liftDriveBWD = false;

    public LiftControl(Lift lift, ButtonPanel panel){
        this.lift = lift;
        this.panel = panel;
    }

    public void init(){

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
        // if(panel.getButtonDown(RobotMap.climberUp)){
        //     liftUp();
        // }
        // if(panel.getButtonDown(RobotMap.climberDown)){
        //     liftDown();
        // }
        // if(panel.getButtonDown(RobotMap.climberFWD)){
        //     lift.setLiftDriveFWD();
        // }
        // if(panel.getButtonDown(RobotMap.climberBWD)){
        //     lift.setLiftDriveBWD();
        // }

        ////temp (needed to override limit switch stuff)

        if(panel.getButtonDown(8)){
            liftUp();
        }
        else if(panel.getButtonUp(8)){
            lift.winchNoMove();
        }
        if(panel.getButtonDown(7)){
            liftDown();
        }
        else if (panel.getButtonUp(7)){
            lift.winchNoMove();
        }
        if(panel.getButtonDown(9)){
            lift.setLiftDriveFWD();
        }
        else if(panel.getButtonUp(9)){
            lift.setLiftDriveOff();
        }
    }
}