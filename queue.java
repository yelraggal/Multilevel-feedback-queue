import java.util.LinkedList;
import java.util.List;

public class queue {
    private int size_of_queue;
    private int time_avalible_on_cpu;
    public LinkedList<process> my_queue;
    public queue(int size_of_queue, int time_avalible_on_cpu) {
        this.my_queue=new LinkedList<>();
        this.size_of_queue = size_of_queue;
        this.time_avalible_on_cpu = time_avalible_on_cpu;
    }
    // this constructor is for the LTS queue , waiting queues (they doennot have size,time on cpu)
    public queue() {
        this.my_queue = new LinkedList<>();
    }
    public int getTime_avalible_on_cpu() {
        return time_avalible_on_cpu;
    }
    public void enqueue (process p){
        my_queue.addLast(p);
    }
    public process dequeue (){
        return my_queue.removeFirst();
    }
    public boolean is_queue_full(){
        return my_queue.size() >= this.size_of_queue;
    }
    public boolean is_queue_empty(){
        return my_queue.isEmpty();
    }
    public void add_all_process_sorted (List<process> s){
        my_queue.addAll(s);
    }
    public process get_first_element(){
        return my_queue.getFirst();
    }
}

