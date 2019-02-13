package frc.lift;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Lift{
    private VictorSPX winchMotor;
    private VictorSPX liftDriveMotor;
    private DigitalInput liftLimitSwitch;


    public Lift(){
        winchMotor = new VictorSPX(RobotMap.winchMotor);
        liftDriveMotor = new VictorSPX(RobotMap.liftDriveMotor);
        liftLimitSwitch = new DigitalInput(RobotMap.liftLimitSwitch);
    }
    public void setLiftDrive(boolean fwd, boolean bwd){
        if(fwd){
            liftDriveMotor.set(ControlMode.PercentOutput, .5);
        }
        if(bwd){
            liftDriveMotor.set(ControlMode.PercentOutput, -.5);
        }
    }
    public void winchUp(boolean up){
        winchMotor.set(ControlMode.PercentOutput, .5);
    }
    public void winchDown(boolean down){
        winchMotor.set(ControlMode.PercentOutput, 0);
        winchMotor.setNeutralMode(NeutralMode.Coast);
    }
    public void winchHover(boolean hover){
        
        // while(liftLimitSwitch.getButtonDown() && winchUpButton.getButtonDown()){
        //     winchMotor.set(ControlMode.PercentOutput, 0.1);
        // }
    }
}