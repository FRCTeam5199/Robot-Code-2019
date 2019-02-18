package frc.arm;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.JoystickController;
import frc.interfaces.LoopModule;
import frc.motion.Interpolator;

import frc.util.Vector2;
import java.util.Vector;

import com.revrobotics.ControlType;

public class ArmControl implements LoopModule { 

    private final JoystickController Joy;
    private final Arm arm;
    private boolean wristUp = false;
    // this is in inches
    private double armLength = 20.75;
    //you still need to find elevator max height once the motor comes in, make a method to do it from the SD
    private double eleMaxHeight = 0;
    // --- IMPORTANT!!!
    private double eleMinHeight = 0;
    private double elbowMinAngle = 0;
    //measure this to be @ somewhere
    private double elbowMaxAngle = 100;
    // ---
    private double wristAngle = 0;
    private double elbowAngle = 0;
    private double elbowTarget = 0;
    private long lastTime;
    private boolean wristPos, claws;
    private Vector2 armOffset = new Vector2(12.125, 20.5);
    private Interpolator eleInterpolator, elbowInterpolator, wristInterpolator;
    private Vector2 stow, low, mid, high, cargoship, cargo1, cargo2, cargo3, max;

    public ArmControl(Arm arm, JoystickController Joy) {
        this.arm = arm;
        this.Joy = Joy;

        eleInterpolator = new Interpolator(3);
        elbowInterpolator = new Interpolator(25);
        wristInterpolator = new Interpolator(25);

    }

    @Override
    public void init() {
        arm.enableArmPID();

        elbowAngle = 0;
        wristAngle = 0;
        elbowTarget = elbowAngle;

        lastTime = System.currentTimeMillis();

        arm.setBeak(false);
        arm.setPokers(false);
        claws = true;

        // armGoTo(rest);
        // wristInterpolator.init(arm.getWristPosition(), 0);

    }

    //temp
    private void findElbowAngle(){
        SmartDashboard.putNumber("Elbow Angle", elbowAngle);
    }

    private void moveTo(double h, double a) {
        eleInterpolator.init(arm.getElePosition(), h);
        elbowInterpolator.init(arm.getElbowPosition(), a);

    }

    private double angleIncrementer(double j){
        if(System.currentTimeMillis() > lastTime + 10){
            if(j>0){
                elbowTarget++;
            }
            else if (j<0){
                elbowTarget--;
            }
            lastTime = System.currentTimeMillis();
            elbowAngle = arm.getElbowPosition();
            return elbowTarget;
        }
        else {
            elbowAngle = arm.getElbowPosition();
            return arm.getElbowPosition();
        }
    }

    private void moveArm(double d){

        if(elbowTarget < elbowMaxAngle && elbowTarget > elbowMinAngle){
            arm.setElbow(d);
            arm.setWrist(-d);
        }

    }

/*     private void wristFlip() {
        wristUp = !wristUp;
        if (wristUp) {
            wristInterpolator.init(arm.getWristPosition(), 90);
        } else {
            wristInterpolator.init(arm.getWristPosition(), 0);
        }
        
    } */

/*     public void armGoTo(Vector2 vGlobal) {
        SmartDashboard.putString("Arm Status", "We Good");
        Vector2 v = Vector2.add(vGlobal, armOffset);
        if (v.getX() < armLength) {
            double angle1 = Math.acos(v.getX() / armLength);
            double angle2 = -angle1;
            double height1 = v.getY() - armLength * Math.sin(angle1);
            double height2 = v.getY() - armLength * Math.sin(angle2);
            boolean h1Valid = eleMinHeight < height1 && height1 < eleMaxHeight;
            boolean h2Valid = eleMinHeight < height2 && height2 < eleMinHeight;

            if (h1Valid && h2Valid) {
                double h1Error = Math.abs(height1 - arm.getElePosition());
                double h2Error = Math.abs(height2 - arm.getElePosition());
                if (h1Error < h2Error) {
                    moveTo(height1, angle1);
                    return;
                } else if (h1Error > h2Error) {
                    moveTo(height2, angle2);
                    return;
                } else {
                    double a1Error = Math.abs(angle1 - arm.getElbowPosition());
                    double a2Error = Math.abs(angle2 - arm.getElbowPosition());
                    if (a1Error > a2Error) {
                        moveTo(height1, angle1);
                        return;
                    } else if (a1Error < a2Error) {
                        moveTo(height2, angle2);
                        return;
                    } else {
                        moveTo(height1, angle1);
                        return;
                    }
                }
            } else if (h1Valid && !h2Valid) {
                moveTo(height1, angle1);
                return;
            } else if (h2Valid && !h1Valid) {
                moveTo(height2, angle2);
                return;
            }
        }
        SmartDashboard.putString("Arm Status", "Oh Shit");
    }
    // goal: minimize elevator movement 
    // check which has less ele movement
    // if same movement maximize angle movement */

    @Override
    public void update(long delta) {

        this.findElbowAngle();

        if(Math.abs(Joy.getYAxis()) > 0){
            this.moveArm(angleIncrementer(Joy.getYAxis()));
        }

        ////temp
        // if(Math.abs(Joy.getYAxis()) > 0){
        //     arm.moveElbow(Joy.getYAxis());
        // }
        // if (Joy.getHat() == -1){
        //     arm.moveWrist(0);
        // } 
        // else if (Joy.getHat() == 0){
        //     arm.moveWrist(0.25);
        // }
        // else if (Joy.getHat() == 180){
        //     arm.moveWrist(-.25);
        // }


        
    }
}