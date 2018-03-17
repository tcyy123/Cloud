
public class ProcessVm {
    public int Core;
    public int MipsVm;
    public String Id;
    public int TotalMips;
    public double PF;			//Power factor
    public double Allotment;	
    public double Price;		// Price of each core
    public double Total_time = 0;	// total time each VM execute tasks
   
    ProcessVm(int c, int m, String id) {
    	Core = c;
    	MipsVm = m;	
    	Id = id;
    }
}  