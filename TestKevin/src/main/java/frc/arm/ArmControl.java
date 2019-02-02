package frc.arm;

import frc.controllers.JoystickController;
import frc.interfaces.LoopModule;
import com.revrobotics.ControlType;

public class ArmControl implements LoopModule {

    private final JoystickController Joy;
    private final Arm arm;

    public ArmControl(Arm arm, JoystickController Joy){
        this.arm = arm;
        this.Joy = Joy;
    }

    @Override
    public void init(){

    }

    @Override
    public void update(long delta){

        arm.initAdjustPID();
        arm.adjustPID();
        arm.setWristPos(arm.rotations, ControlType.kPosition);
        
    }
}