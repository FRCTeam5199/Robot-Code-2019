package frc.arm;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.JoystickController;
import frc.interfaces.LoopModule;
import com.revrobotics.ControlType;

public class ArmControl implements LoopModule {

    private final JoystickController Joy;
    private final Arm arm;
    private boolean override;

    public ArmControl(Arm arm, JoystickController Joy){
        this.arm = arm;
        this.Joy = Joy;
    }

    @Override
    public void init(){

        arm.initAdjustPID();
        
    }

    @Override
    public void update(long delta){

        arm.adjustPID();
        arm.setWristPos(arm.rotations, ControlType.kPosition);

    }
}