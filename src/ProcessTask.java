
public class ProcessTask {
	public double ArrivalTime;   
    public String pid;  
    public int mips;
    public double ExeTime;  //execution time
    public double PriceTotal; // total price of each Task
      
    ProcessTask(double x,int m, String id) {
        ArrivalTime = x;
        mips = m;
        pid = id;
    }  

}
