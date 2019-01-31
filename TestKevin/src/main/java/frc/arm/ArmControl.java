package frc.arm;

import frc.controllers.XBoxController;
import frc.interfaces.LoopModule;
import frc.util.Interpolater;

public class ArmControl implements LoopModule {

    private final XBoxController Xbox;
    private final Arm arm;

    private Interpolater smoother;

    public ArmControl(Arm arm, XBoxController Xbox){
        this.arm = arm;
        this.Xbox = Xbox;
    }

    @Override
    public void init(){

    }

    @Override
    public void update(long delta){
        //wrist
        // if (Math.abs(Xbox.getStickRY()) > 0) {
        //     arm.setWristMotor(Xbox.getStickRY());
        // }
        // elbow
        // if (Math.abs(Xbox.getStickLY()) > 0) {
        //     arm.setElbowMotor(Xbox.getStickLY());
        // }
        
        if  (Xbox.getButton(1)) {
            elbowPID.setReference(rotations, ControlType.kPosition);
        }

        SmartDashboard.putNumber("SetPoint", arm.rotations);
        SmartDashboard.putNumber("ProcessVariable", arm.getElbowEncoder().getPosition());
        
    }
}