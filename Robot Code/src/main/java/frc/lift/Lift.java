package frc.lift;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Servo;

public class Lift{
    private VictorSPX winchMotor;
    private VictorSPX liftDriveMotor;
    public DigitalInput liftLimitSwitch;
    private Solenoid locks;
    private Servo clutch;

    public Lift(){
        winchMotor = new VictorSPX(RobotMap.winchMotor);
        liftDriveMotor = new VictorSPX(RobotMap.liftDriveMotor);
        liftLimitSwitch = new DigitalInput(RobotMap.liftLimitSwitch);
        locks = new Solenoid(RobotMap.climbPistons);
        clutch = new Servo(RobotMap.clutchServo);
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
        winchMotor.set(ControlMode.PercentOutput, 1);
        winchMotor.setNeutralMode(NeutralMode.Brake);
    }
    public void winchNoMove(){
        winchMotor.set(ControlMode.PercentOutput, 0);
        winchMotor.setNeutralMode(NeutralMode.Brake);
    }

    public void winchDown(){
        winchMotor.set(ControlMode.PercentOutput, -1);
        winchMotor.setNeutralMode(NeutralMode.Coast);
    }

    public void locksOn(){
        locks.set(true);
    }

    public void clutchOn(){
        clutch.setAngle(0); //CHANGED TO ZERO 9/28/19 FOR HEXFLY SERVO FIX
    }

    public void setClutch(double a){
        clutch.setAngle(a);
    }

    public double getClutchPos(){
        return clutch.get();
    }

    public void winchHover(){
        //Currently enters hover mode if the limit switch returns false. The switch should return false if it is closed. If this doesnt do what we want,
        // the switch probabaly returns false when open. In that case, remove the '!'
        while(!liftLimitSwitch.get()){
            winchMotor.set(ControlMode.PercentOutput, 0);
            winchMotor.setNeutralMode(NeutralMode.Brake);
        }
        winchMotor.setNeutralMode(NeutralMode.Coast);
    }
}