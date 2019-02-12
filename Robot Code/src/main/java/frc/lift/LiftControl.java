package frc.lift;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.interfaces.LoopModule;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ControlType;

public class LiftControl implements LoopModule{
    private final Lift lift;
    private final ButtonPanel buttons;
    private boolean winchUpButton = false;
    private boolean winchDownButton = false;
    private boolean liftDriveFWD = false;
    private boolean liftDriveBWD = false;

    public LiftControl(Lift lift, ButtonPanel buttons){
        this.lift = lift;
        this.buttons = buttons;
    }
    
    @Override
    public void init(){

    }

    @Override
    public void update(long delta){

    }
}