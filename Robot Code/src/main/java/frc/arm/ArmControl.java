package frc.arm;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;
import frc.interfaces.LoopModule;
import frc.motion.Interpolator;

import frc.util.Vector2;
import java.util.Vector;

import com.revrobotics.ControlType;

public class ArmControl implements LoopModule { 

    private final JoystickController Joy;
    private final ButtonPanel panel;
    private final Arm arm;
    private boolean wristUp = false;
    // this is in inches
    private double armLength = 20.75;

    private double elePos = 0;
    //eleTop : -31.053, loss @ bottom: 0.622-0.854
    //The max height of the elevator is in arbitrary units rn, something is wrong with the eleRatio math or the gearing
    private double eleMaxHeight = -31;
    private double eleUpHeight = -29;
    //ele motor is negative cause its reversed
    private double eleMinHeight = 0;
    private double elbowMinAngle = 0;
    //this number is a guess
    private double elbowMaxAngle = 130;
    //
    private double wristAngle = 0;
    private double wristTarget = 0;
    private double wristMinAngle = 0;
    //measure this to be @ somewhere & then implement a softstop
    private double wristMaxAngle = 0;
    //
    //@ some point you need to start the arm straight out & then move it to the stowed position to see how the arm has to move to exit stow;
    private double hatMod = 0;
    private double elbowAngle = 0;
    private double elbowTarget = 0;
    private double eleMotorPos;

    private long lastTime;
    private Vector2 armOffset = new Vector2(12.125, 20.5);
    private Interpolator eleInterpolator, elbowInterpolator, wristInterpolator, armInterpolator;
    private Vector2 stow, ground, hatch1, hatch2, hatch3, cargoship, cargo1, cargo2, cargo3, max;

    public ArmControl(Arm arm, JoystickController Joy, ButtonPanel panel) {
        this.arm = arm;
        this.Joy = Joy;
        this.panel = panel;

        eleInterpolator = new Interpolator(3);
        elbowInterpolator = new Interpolator(10);
        wristInterpolator = new Interpolator(10);
        armInterpolator = new Interpolator(25);

    }

    @Override
    public void init() {
        arm.enableArmPID();
        arm.disableElePid();
        //arm.disableArmPID();
        // !!!

        hatMod = 0;
        elbowAngle = arm.getElbowPosition();
        elbowTarget = elbowAngle;
        wristAngle = arm.getWristPosition();
        wristTarget = wristAngle;

        lastTime = System.currentTimeMillis();

        // armGoTo(rest);
        // wristInterpolator.init(arm.getWristPosition(), 0);

    }

    //temp
    private void findArmPositions(){
        SmartDashboard.putNumber("Elbow Angle", elbowAngle);
        SmartDashboard.putNumber("Wrist Angle", wristAngle);
        SmartDashboard.putNumber("Elevator Height", elePos);
        SmartDashboard.putNumber("Elevator Motor Rotations", eleMotorPos);

        elePos = arm.getElePosition();
        eleMotorPos = arm.getEleMotorPos();
        elbowAngle = arm.getElbowPosition();
        wristAngle = arm.getWristPosition();
    }

    private void posInterpolator(double e, double w) {
        elbowInterpolator.init(arm.getElbowPosition(), e);
        wristInterpolator.init(arm.getWristPosition(), w);
    }

    private double angleIncrementer(double j){
        if(System.currentTimeMillis() > lastTime + 10){
            if(j>0.1 && elbowTarget < elbowMaxAngle){
                elbowTarget += 1;
            }
            else if (j<-0.1 && elbowTarget > elbowMinAngle){
                elbowTarget -= 1;
            }
            lastTime = System.currentTimeMillis();
            elbowAngle = arm.getElbowPosition();
            wristAngle = arm.getWristPosition();
            return elbowTarget;
        }
        else {
            elbowAngle = arm.getElbowPosition();
            wristAngle = arm.getWristPosition();
            return arm.getElbowPosition();
        }
    }

    private void moveArmTo(double e, double w){
        posInterpolator(e, w);
        while(!elbowInterpolator.isFinished() && !wristInterpolator.isFinished()){
            double epos = elbowInterpolator.get();
            double wpos = wristInterpolator.get();
            arm.setElbow(epos);
            elbowTarget = e;
            arm.setWrist(wpos);
            wristTarget = w;
        }
    }

/*     private void moveArmTo(double e, double w){
        arm.setPointElbow(e);
        arm.setPointWrist(w);
    } */


    private void moveArm(double d){
        if(elbowTarget < elbowMaxAngle && elbowTarget >= elbowMinAngle){
            arm.setElbow(d);
            arm.setWrist(-d + hatMod);
        }
    }

    private void eleUp(){

    }

    private void armStay(){
        arm.setElbow(arm.getElbowPosition());
        arm.setWrist(arm.getWristPosition());
    }

    private void wristIncrease(){
        if (Joy.getHat() == -1){
            hatMod += 0;
        } 
        else if (Joy.getHat() == 180){
            hatMod++;
        }
        else if (Joy.getHat() == 0){
            hatMod--;
        }
    }

    //the new "armGoTo"
    private void armMove(){
        //armInterpolator.init(arm.getElbowPosition(), arm.getElbowPosition());
        if (Joy.getButton(10)){
            armInterpolator.init(arm.getElbowPosition(), 0);
            elbowTarget = 0;
        }
        else if (Joy.getButton(12)){
            armInterpolator.init(arm.getElbowPosition(), 24);
            elbowTarget = 24;
            hatMod = 0;
        }
        else if (Joy.getButton(11)){
            armInterpolator.init(arm.getElbowPosition(), 24);
            elbowTarget = 24;
            //idk what angle so 90 is a guess
            hatMod = 90;
        }


    }

    private void wristFlip() {
        wristUp = !wristUp;
        if (wristUp) {
            wristInterpolator.init(arm.getWristPosition(), 90);
        } else {
            wristInterpolator.init(arm.getWristPosition(), 0);
        }
        
    }

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

        this.findArmPositions();

        if(Math.abs(Joy.getYAxis()) > 0 || Joy.getHat() != -1){
            this.moveArm(angleIncrementer(Joy.getYAxis()));
            this.wristIncrease();
        }

        if(panel.getButton(9)){
            moveArmTo(47.3806, 44.1902);
        }
        //hatch1 ^
        if(panel.getButton(4)){
            //moveArmTo(21.28582, -7.0476122);
            // the old one^
            moveArmTo(15.85718,0.5714277);
        }
        //^ the ground
        if(panel.getButton(3)){
            moveArmTo(68.713668, -18.47626);
        }
        //cargo1^
        if(panel.getButton(2)){
            moveArmTo(129.0, -55.428165);
        }
        //cargo2^
        
        
        //this is temp
        /* if (Math.abs(Joy2.getYAxis()) > 0){
            arm.disableElePid();
            arm.moveEle(Joy2.getYAxis());
        }
        else if (Math.abs(Joy2.getYAxis()) == 0){
            arm.enableElePid();
        }

        this.eleStay();
        this.armStay(); */

        // else if(Joy.getButtonDown(12)){
        //     foo = "down";
        //     STOP = false;
        //     eleInterpolator.init(arm.getElePosition(), -eleUpHeight);
        //     this.eleUp();
        // }
        // else if(Joy.getButtonDown(11)){
        //     this.eleStop();
        // }

        //temp elevator test
        // if(Math.abs(Joy.getYAxis()) > 0){
        //     arm.moveEle(Joy.getYAxis());
        // }
        
    }
}