package frc.lift;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ControlType;

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
    
    @Override
    public void init(){

    }

    @Override
    public void update(long delta){

    }
}