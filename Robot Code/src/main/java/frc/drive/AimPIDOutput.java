package frc.drive;

import edu.wpi.first.wpilibj.PIDOutput;
import frc.drive.DriveBase;

public class AimPIDOutput implements PIDOutput{

    private DriveBase base;
    private double move;

    public AimPIDOutput(DriveBase base){
        this.base = base;
    }
    
    public void pidWrite(double output){
        base.driveArcade(this.move, output);
    }

    public void setMove(double move){
        this.move = move;
    } 

}