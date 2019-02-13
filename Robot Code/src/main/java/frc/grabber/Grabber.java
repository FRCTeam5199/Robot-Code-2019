package frc.grabber;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Grabber{

    private VictorSPX intake;
    private Solenoid gripperPiston;
    private Solenoid pokePistons;

    public Grabber(){
        intake = new VictorSPX(RobotMap.intakeMotor);
        gripperPiston = new Solenoid(RobotMap.gripperPiston);
        pokePistons = new Solenoid(RobotMap.pokePistons);

    }
}