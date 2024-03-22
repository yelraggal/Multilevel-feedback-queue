public class process {
    int id;
    int time_remaning;
    private int burst_time;
    int arrival_time;
    private String log ;
    public process (int id , int time , int arrival_time){
        this.id=id;
        this.time_remaning =time;
        this.arrival_time=arrival_time;
        this.burst_time=time;
        this.log="\n";
    }
    public int getArrival_time() {
        return arrival_time;
    }
    public int get_time(){
        return this.time_remaning;
    }
    public void set_time(int new_time){
        this.time_remaning =new_time;
    }
    public void logger (String new_event){
        log+=new_event;
        log+="\n";
    }
    public void i_am_done(int Qnumb){
        String message ;
        if (Qnumb == 1){
            message="1st";
        }else if (Qnumb == 2){
            message="2nd";
        }else{
            message="3rd";;
        }
        System.out.println("\033[38;2;255;107;107m"+"## Process with ID "+this.id +
                "    ## Finished on "+message+" Queue"+"\033[38;2;255;204;92m"
                +"\n## burust_time : "+burst_time +"     ## Arival_time : "+arrival_time +"\033[38;2;168;213;186m");
        System.out.println("life cycle "+"\033[0m"+ log);
    }
    @Override
    public String toString() {
        return "id: " + id + ", time_remaining: " + burst_time + ", arrival_time: " + arrival_time;
    }
}

