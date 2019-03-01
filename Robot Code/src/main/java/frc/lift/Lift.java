package frc.lift;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Lift{
    private VictorSPX winchMotor;
    private VictorSPX liftDriveMotor;
    public DigitalInput liftLimitSwitch;

    public Lift(){
        winchMotor = new VictorSPX(RobotMap.winchMotor);
        liftDriveMotor = new VictorSPX(RobotMap.liftDriveMotor);
        liftLimitSwitch = new DigitalInput(RobotMap.liftLimitSwitch);
    }
    public void setLiftDriveFWD(){
            liftDriveMotor.set(ControlMode.PercentOutput, .5);
    }
    public void setLiftDriveOff(){
        liftDriveMotor.set(ControlMode.PercentOutput, 0);
    }
    public void setLiftDriveBWD(){
            liftDriveMotor.set(ControlMode.PercentOutput, -.5);
    }
    public void winchUp(){
        winchMotor.set(ControlMode.PercentOutput, .7);
        winchMotor.setNeutralMode(NeutralMode.Brake);
    }
    public void winchNoMove(){
        winchMotor.set(ControlMode.PercentOutput, 0);
        winchMotor.setNeutralMode(NeutralMode.Brake);
    }

    public void winchDown(){
        winchMotor.set(ControlMode.PercentOutput, -.1);
        winchMotor.setNeutralMode(NeutralMode.Coast);
    }
    public void winchHover(){
        //Currently enters hover mode if the limit switch returns false. The switch should return false if it is closed. If this doesnt do what we want,
        // the switch probabaly returns false when open. In that case, remove the '!'
        while(!liftLimitSwitch.get()){
            winchMotor.set(ControlMode.PercentOutput, 0);
            //not gonna brake it ?
            try {
                Thread.sleep(500);
                //we cant thread sleep on the rio or stuff gets fucked up use system time and a delay
            } catch(InterruptedException e) {
                System.out.println("Man, I dont EVEN have an idea.");
            }
        }
    }
}