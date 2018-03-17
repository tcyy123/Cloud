import java.text.DecimalFormat;  
import java.util.Scanner;  
  
public class experiment1 {  
    public static void main(String[] args) {  
    	int Total_MIPS = 0;	//Total MIPS of all VMs
    	int Total_Task = 0;	//Total MIPS of all Tasks
    	double Total_Exe_Time = 0;	//Total Execution time of all tasks
    	double Total_Task_Price = 0;	//Total Price of all tasks
    	DecimalFormat df = new DecimalFormat("0.00");		// format
    	
    	// Entry the tasks
        Scanner in = new Scanner(System.in); 
        System.out.println("Please Entry the Number of Tasks：");  
        int n = in.nextInt();            
        ProcessTask[] pt = new ProcessTask[n];
        System.out.println("Please Entry Task's Arrive time，MIPS，ID:");
          
        // Initialize Task
        for(int i = 0; i < n; i++) {
            int arrTime = in.nextInt(); 
            int mip = in.nextInt();
            String pid = in.nextLine();  
            pt[i] = new ProcessTask(arrTime,mip,pid);  
        }  
           
    	// Entry the VMs
        Scanner invm = new Scanner(System.in); 
        System.out.println("The number of VMs：");  
        int m = invm.nextInt();  //the number of VMs         
        ProcessVm[] pv = new ProcessVm[m];
        System.out.println("Please Enter # of VM's Cores, MIPS/Core,ID:");  
        
        // Initialize VM           
        for(int i = 0;i < m;i++) {
            int cor = in.nextInt();  
            int mipsvm = in.nextInt(); 
            String id = in.nextLine();
            pv[i] = new ProcessVm(cor, mipsvm,id);  
        } 

        //Calculate Each VM's Total MIPS
        for(int i = 0;i < pv.length;i++) { 
        	pv[i].TotalMips = pv[i].Core * pv[i].MipsVm;
          System.out.println("Total MIPS for VM " + pv[i].Id + " is: " + pv[i].TotalMips);
        }
        
        //Total MIPS of Tasks   
        for(int j = 0;j < n;j++) {
        	Total_Task = Total_Task + pt[j].mips;		
        }
       	System.out.println("Total Task MIPS：" + Total_Task);

        // Choose Algorithm to Simulate the Tasks Schedule to VMs 
        while (true) {
            System.out.println("-----------------------------------------------------");
            System.out.println("Please Select the Algorithm.  1:Proposed Algorithm  2:FIFO Algorithm  Other Keys:Quit");  
            int select = in.nextInt();  
            if(select == 1) {
                System.out.println("----------------Proposed Algorithm-------------------");  
                for(int i = 0;i < m;i++) { 
                	Total_MIPS = Total_MIPS + pv[i].TotalMips;	//Total MIPS of all VMs 
                	
                	//Google Cloud Price Model
                	switch(pv[i].Core) 
                	{ 
                	   case 2: 
                	       pv[i].Price = 0.126; 
                	       break; 
                	   case 4: 
                		   pv[i].Price = 0.252; 
                	       break; 
                	   case 8: 
                		   pv[i].Price = 0.504;
                	       break; 
                	   case 16: 
                		   pv[i].Price = 1.008;
                	       break; 
                	   case 32: 
                		   pv[i].Price = 2.016;
                	       break; 
                	   default: 
                	       System.out.println("No price for this VM type in Google Cloud."); 
                	       break; 
                	} 
                }  

                // PF & Allotment of Each VM 
                for(int i = 0;i < m;i++) {
                	pv[i].PF = (float)pv[i].TotalMips / Total_MIPS;		
                	pv[i].Allotment = pv[i].PF * Total_Task;
                	System.out.println("PF of VM" + pv[i].Id + " is:" + df.format(pv[i].PF) + ". Allotment of VM" + pv[i].Id + " is:" + df.format(pv[i].Allotment)); 
                }
              // Allocate Each Task to Each VM Using Minimum Difference     
            	System.out.println("Task  VM  Exe-time  Price");
                for(int j = 0;j < n;j++) {
                	double min_diff;		// the minimum difference 
                	double diff;			// the difference
                	String assigned_id = pv[0].Id;
                	int assigned_mips = pv[0].TotalMips;
                	double assigned_price = pv[0].Price;
                	int index = 0;

                	while (pv[index].Allotment < pt[j].mips) index++;	//Initialize the min_diff
                    min_diff = pv[index].Allotment - pt[j].mips;
                   
                    for(int i = 1;i < m;i++) {
                		if(pt[j].mips < pv[i].Allotment) {
                		diff = pv[i].Allotment - pt[j].mips;
                		if (diff <= min_diff) {
                			min_diff = diff;						
                			assigned_id = pv[i].Id;
                			assigned_mips = pv[i].TotalMips;
                			assigned_price = pv[i].Price;
                		}                			
                		}
                	}
                //	System.out.println(pt[j].mips+" "+assigned_mips);
                	pt[j].ExeTime = (float)pt[j].mips / assigned_mips;		// calculate task execution time
                	pt[j].PriceTotal = pt[j].ExeTime * assigned_price;		// calculate task price
                	System.out.println(pt[j].pid +"   " + assigned_id +"    " + df.format(pt[j].ExeTime) + " s     " + df.format(pt[j].PriceTotal));
                	Total_Exe_Time += pt[j].ExeTime;						//calculate total tasks execution time
                	Total_Task_Price += pt[j].PriceTotal;					//calculate total tasks price
                } 
                System.out.println("Total Exe Time    " + " Total Price");
                System.out.println(df.format(Total_Exe_Time) +"     " + df.format(Total_Task_Price));
            }  
            else if(select == 2) {  
                System.out.println("----------------Algorithm FCFS-------------------");  
                //Sort the tasks according to their arrive time 
                insertSort(pt); 
                //Assign tasks to VMs 
                int k = pv.length;
                System.out.println("Task  VM  Exe-time  Price");
                for(int i = 0;i < pt.length; i++) {
                // chose the lowest workload VM
            		double min = pv[0].Total_time;
            		int assign = 0;
            		for(int j = 0;j < k;j++) {
            			if(pv[j].Total_time < min) {
                            min = pv[j].Total_time;
                            assign = j;  
            		}
            	}
            		pt[i].ExeTime = (float)pt[i].mips / pv[assign].TotalMips;
            		pt[i].PriceTotal = pt[i].ExeTime * pv[assign].Price;
            		System.out.println(pt[i].pid + "   " + pv[assign].Id + "   " + df.format(pt[i].ExeTime) + "    " + df.format(pt[i].PriceTotal));
            		pv[assign].Total_time = pv[assign].Total_time + pt[i].ExeTime;
                	Total_Exe_Time += pt[assign].ExeTime;
                	Total_Task_Price += pt[assign].PriceTotal;
                } 
                System.out.println("Total Exe Time    " + " Total Price");
                System.out.println(df.format(Total_Exe_Time) + "     " + df.format(Total_Task_Price));
            }  
            else {  
                break;  
            }
        }  
          
    } 

    //InsertSort 
    public static void insertSort(ProcessTask[] array)  
    {  
        int i,j;  
        int n = array.length;  
        ProcessTask target;  
        for (i = 1; i < n; i++)  
        {  
            j = i;  
            target = array[i];   
            while (j > 0 && target.ArrivalTime < array[j - 1].ArrivalTime) {  
                array[j] = array[j - 1];  
                j--;  
            }   
            array[j] = target;  
        }  
    }  
}  

//Task Class  
class ProcessTask {  
    public double ArrivalTime;   
    public String pid;  
    public int mips;
    public double ExeTime;	//execution time
    public double PriceTotal; // total price of each Task
      
    ProcessTask(double x,int m, String id) {
        ArrivalTime = x;
        mips = m;
        pid = id;
    }  
} 
// VM Class 
class ProcessVm {
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