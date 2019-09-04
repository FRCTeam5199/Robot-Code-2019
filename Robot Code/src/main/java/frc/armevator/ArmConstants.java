package frc.armevator;

public class ArmConstants{

    public static final double a = 0;
    //combot
    // public static final double b = 5;
    //practice bot
    public static final double b = 3.5;
    public static final double hatchOffset = 0; // part :b:roke and red line wouldn't align nicely anymore
    //elbow pos, wrist pos
    //public static final double[] cargoIntake = {53.81612660933514 + a,-117.00028228759766 + b}; OLD VALUES BEFORE CONOR SCREWED WITH IT
    public static final double[] cargoIntake = {52.81612660933514 + a,-113.52286529541016 + b}; //43 worked well //-113 before change to -116 at camarillo
    //public static final double[] hatchIntake = {92.57064943897481 + a,-60.999507904052734 + b}; PRE-CONOR
    public static final double[] hatchIntake = {78.57064943897481 + a,-48.999507904052734 + b + hatchOffset}; //changed from 73 to 78 @ 10:31 8/18/19
    public static final double[] cargo1 = {97.07784598214286 + a,-127.72658081054688 + b};
    public static final double[] cargo2 = {61.644046082788584 + a,-91.52286529541016 + b};
    public static final double[] cargo3 = {157.3743373325893 + a,-169.53048706054688 + b};
    //public static final double[] hatch1 = {81.57064943897481 + a,-58.999507904052734 + b}; PRE-CONOR
    public static final double[] hatch1 = {81.57064943897481 + a,-54.999507904052734 + b + hatchOffset};
    public static final double[] hatch2 = {59.37285940987724 + a,-37.99993133544922 + b + hatchOffset};
    public static final double[] hatch3 = {151.4171952228157 + a,-112.14411926269531 + b + hatchOffset};
    public static final double[] cargoShip = {147.3584868761958 + a,-188.14996337890625 + b};
    public static final double[] drive = {0,0};
    //public static final double[] oow = {81.57064943897481 + a,0};
}