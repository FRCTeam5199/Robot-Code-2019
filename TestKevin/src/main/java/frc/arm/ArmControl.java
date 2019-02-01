package frc.arm;

import frc.controllers.XBoxController;
import frc.interfaces.LoopModule;
import com.revrobotics.ControlType;

public class ArmControl implements LoopModule {

    private final XBoxController Xbox;
    private final Arm arm;

    public ArmControl(Arm arm, XBoxController Xbox){
        this.arm = arm;
        this.Xbox = Xbox;
    }

    @Override
    public void init(){

    }

    @Override
    public void update(long delta){

        arm.initAdjustPID();
        arm.adjustPID();

        arm.setElbowGoal(arm.rotations, ControlType.kPosition);
        
        if (Math.abs(Xbox.getStickLY()) > 0 ){
            arm.setElbowMotor(Xbox.getStickLY());
        }
        
    }
}