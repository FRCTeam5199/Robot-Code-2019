package frc.grabber;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Grabber {

    private VictorSPX intake;
    private Solenoid gripperPiston;
    private Solenoid pokePistons;

    public Grabber() {
        gripperPiston = new Solenoid(RobotMap.gripperPiston);
        pokePistons = new Solenoid(RobotMap.pokePistons);
        intake = new VictorSPX(RobotMap.intakeMotor);

        intake.setNeutralMode(NeutralMode.Coast);
    }

    public void setGrabber(boolean b) {
        gripperPiston.set(b);
    }

    public void setPokers(boolean b) {
        pokePistons.set(b);
    }

    public void setIntake(double n) {
        intake.set(ControlMode.PercentOutput, n);
    }
    
}