package frc.arm;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.JoystickController;
import frc.interfaces.LoopModule;
import com.revrobotics.ControlType;

public class ArmControl implements LoopModule {

    private final JoystickController Joy;
    private final Arm arm;
    private boolean override;
    private double rotations;

    public ArmControl(Arm arm, JoystickController Joy){
        this.arm = arm;
        this.Joy = Joy;
    }

    @Override
    public void init(){

        arm.initAdjustPID();
        arm.initWristPos();
        arm.setCoast();

    }

    @Override
    public void update(long delta){

        arm.adjustPID();
        //arm.setWristPos(arm.rotations, ControlType.kPosition);
        SmartDashboard.putNumber("Elbow Encoder", arm.getElbowPosition());
        SmartDashboard.putNumber("Wrist Encoder", arm.getWristPosition());

        if((Math.abs(Joy.getYAxis())>0)){
            arm.setElbowMotor(Joy.getYAxis());
        }
    }
}