package frc.motion;

public class Interpolator{

    //k is acceleration
    private double k, start, end, delta;
    private long startTime;


    public Interpolator(double a){
        k = a/2;
    }

    public void init(double start, double end){
        startTime = System.currentTimeMillis();
        this.start = start;
        this.end = end;
        delta = end - this.start;
    }

    public boolean isFinished(){
        return time() * k > Math.PI;
    }
    private double time(){
        return (System.currentTimeMillis()-startTime)/1000d;
    }

    public double get(){
        if(isFinished()){
            return end;
        }
        else{
            return (delta/2)*(1-Math.cos(k*time()))+start;
        }
    }
}