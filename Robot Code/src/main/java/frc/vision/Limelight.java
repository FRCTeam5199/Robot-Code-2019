package frc.vision;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDSourceType;

public class Limelight implements PIDSource{

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tv = table.getEntry("tv");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry led = table.getEntry("ledMode");
    private static final double kOffset = -2.5;

    public void printValues(){

        //read values periodically
        double v = tv.getDouble(0.0);
        double x = tx.getDouble(0.0) + kOffset;
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);

        //post to smart dashboard periodically
        SmartDashboard.putNumber("Target Found", v);
        SmartDashboard.putNumber("LimelightX", x);
        SmartDashboard.putNumber("LimelightY", y);
        SmartDashboard.putNumber("LimelightArea", area);
    }

    public void setLEDMode(LedMode mode){
        led.setNumber(mode.ordinal());
    }

    @Override
	public void setPIDSourceType(PIDSourceType pidSource) {
        if(pidSource == PIDSourceType.kRate){
            try{
                throw new Exception ("Wrong Camera SourceType");
            } catch(Exception e){
                e.printStackTrace();
            }
        }
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		if(tv.getDouble(0.0) == 1){
            return tx.getDouble(0.0) + kOffset;
        }
        else{
            return 0;
            // return Double.NaN;
        }
    }
    
    public enum LedMode{
        CURRENT, OFF, BLINK, ON; 
    }

}