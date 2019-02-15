package frc.lift;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ControlType;
import frc.robot.RobotMap;
import frc.lift.Lift;
import frc.interfaces.LoopModule;

public class LiftControl implements LoopModule{
    private final Lift lift;
    private final ButtonPanel button;
    private boolean winchUpButton = false;
    private boolean winchDownButton = false;
    private boolean liftDriveFWD = false;
    private boolean liftDriveBWD = false;

    public LiftControl(Lift lift, ButtonPanel button){
        this.lift = lift;
        this.button = button;
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
        if(button.getButtonDown(RobotMap.climberUp)){
            liftUp();
        }
        if(button.getButtonDown(RobotMap.climberDown)){
            liftDown();
        }
        if(button.getButtonDown(RobotMap.climberFWD)){
            lift.setLiftDriveFWD();
        }
        if(button.getButtonDown(RobotMap.climberBWD)){
            lift.setLiftDriveBWD();
        }
    }
}